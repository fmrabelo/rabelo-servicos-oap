/**
 * 
 */
package br.com.oappr.infra.action.servlet;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import br.com.oappr.conversor.ConvertPDF;
import br.com.oappr.infra.DAO.DaoFactory;
import br.com.oappr.infra.report.GenericReport;
import br.com.oappr.infra.report.ReportParameters;
import br.com.oappr.infra.report.laudo.LaudoReport;
import br.com.oappr.infra.util.DateUtils;
import br.com.oappr.infra.util.GenericUtils;
import br.com.oappr.infra.util.Validator;
import br.com.oappr.intranet.vo.LaudoVO;
import br.com.oappr.intranet.vo.MedicoVO;
import br.com.oappr.intranet.vo.PessoaVO;
import br.com.oappr.merge.pdf.MergePDF;

/**
 * Servlet responsável pelas regras para gerar o laudo online em pdf.
 * @author rabelo
 */
public final class ServletReport
    extends HttpServlet
    implements ReportParameters, Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 13206501245454L;

	// parametros para jasper report
	HashMap<String, String> parameters = new HashMap<String, String>();

	/**
	 * doget
	 */
	@Override
	public void doGet (HttpServletRequest req, HttpServletResponse res) throws IOException,
	    ServletException
	{
		final String str = req.getParameter("Text1");
		final String nrseqresultado = req.getParameter("nrseqresultado");
		final String nroCadastroPaciente = req.getParameter("nroCadastroPaciente");
		try
		{
			this.gerarRelatorio(str, nrseqresultado, nroCadastroPaciente, res);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			throw new ServletException(ex);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doPost (HttpServletRequest req, HttpServletResponse resp)
	    throws ServletException, IOException
	{
		super.doPost(req, resp);
	}

	/**
	 * Método responsavel por montar estrutra e gerar o relatio pdf.
	 * @param str
	 * @param nrseqresultado
	 * @param nroCadastroPaciente
	 */
	private final void gerarRelatorio (final String str, final String nrseqresultado,
	    final String nroCadastroPaciente, final HttpServletResponse res) throws Exception
	{
		if ("rtf".equals(str))
		{
			final List<LaudoVO> listaLaudos = DaoFactory.getInstance().getLaudos(
			    GenericUtils.toLong(nroCadastroPaciente), GenericUtils.toLong(nrseqresultado));

			if (!Validator.notEmptyCollection(listaLaudos))
			{
				throw new Exception("Laudo não localizado!!");
			}

			// Recuperar Laudo Original do Clinitools gravado na base
			// Oracle.
			final LaudoVO laudo = listaLaudos.get(0);

			// converter blob rtf para byte[]
			final byte[] relatorioRTF = laudo.getDsrtf().getBytes(1, (int)laudo.getDsrtf().length());

			// converter o conteúdo laudo RTF para PDF.
			final byte[] relatorioPDF = new ConvertPDF().convertPDF(relatorioRTF);

			// gerar array de bytes via JasperReport contendo o cabeçalho pdf
			// para o relatório.
			final byte[] cabecalho = this.gerarCabecalhoPDF(laudo);

			// merge entre pdf com o cabeçalho e o conteudo do laudo.
			final List<InputStream> pdfs = new ArrayList<InputStream>();
			pdfs.add(new ByteArrayInputStream(cabecalho));
			pdfs.add(new ByteArrayInputStream(relatorioPDF));
			final MergePDF mergePDF = new MergePDF();
			final byte[] byteArrayMerged = mergePDF.concatPDFs(pdfs);

			// apresentar o pdf final.
			final String fileName = "laudoOnlinePDF";
			final LaudoReport report = new LaudoReport();
			report.showReport(res, fileName, byteArrayMerged, GenericReport.PDF_TYPE);
		}

	}

	/**
	 * Gerar o cabeçalho do relatório.
	 */
	private final byte[] gerarCabecalhoPDF (final LaudoVO laudo) throws Exception
	{
		try
		{
			final HttpServletRequest request = ServletActionContext.getRequest();
			final HttpServletResponse response = ServletActionContext.getResponse();

			// dados do paciente
			final PessoaVO paciente = DaoFactory.getInstance().getAcPacienteByMatricula(
			    laudo.getCdpessoa());
			if (paciente == null)
			{
				throw new Exception("Paciente não localizado!!");
			}

			// Parametros do cabeçalho
			parameters.put(PACIENTE, "PACIENTE : ".concat(paciente.getNomePessoa() != null
			    ? paciente.getNomePessoa().toUpperCase()
			    : ""));
			// Médico solicitante do exame.
			parameters.put(DESCR_MEDICO, "A/C : "
			    + DaoFactory.getInstance().getMedicoSolicitante(laudo.getCdpessoa()));

			// Parametros para o médico responsável pelo Exame.
			final MedicoVO medRespExam = DaoFactory.getInstance().getMedicoResponsavelPorExame(
			    laudo.getNrrequisicao(), laudo.getCdproced(), laudo.getNrlaudo());
			if (medRespExam == null)
			{
				throw new Exception("Médico responsável pelo Exame não localizado!!");
			}

			final StringBuilder buffer = new StringBuilder("Exame realizado pel");
			buffer.append(("F".equalsIgnoreCase(medRespExam.getSexo()) ? "a " : "o "));
			buffer.append(medRespExam.getSiglaTratamento()).append(" ").append(
			    medRespExam.getNomePessoa());

			parameters.put(MEDICO_RESPONSAVEL_EXAME, buffer.toString());
			parameters.put(CRM_MEDICO_RESPONSAVEL_EXAME, medRespExam.getDescrCRM());
			parameters.put(CIDADE_DATA_EXAME, medRespExam.getCidade().concat(", ").concat(
			    DateUtils.formatDateDDMMYYYY(laudo.getDtconsulta())));

			// Parametro Nome do Laudo
			parameters.put(NOME_LAUDO, laudo.getDsexamecompl());
			this.setParameters(parameters);
			//

			final String fileName = "cabecalhoPDF";
			final LaudoReport report = new LaudoReport();
			return report.getByteArrayCabecalhoPDF(request, response, fileName, null, parameters,
			    GenericReport.PDF_TYPE);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			throw e;
		}
	}

	/**
	 * @return the parameters
	 */
	public HashMap<String, String> getParameters ()
	{
		return parameters;
	}

	/**
	 * @param parameters the parameters to set
	 */
	public void setParameters (HashMap<String, String> parameters)
	{
		this.parameters = parameters;
	}

}
