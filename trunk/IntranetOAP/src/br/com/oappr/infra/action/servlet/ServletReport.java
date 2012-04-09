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
 * Servlet respons�vel pelas regras para gerar o laudo online em pdf.
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
		final String nrrequisicao = req.getParameter("nrrequisicao");
		final String nroCadastroPaciente = req.getParameter("nroCadastroPaciente");
		System.out.printf(
		    "%n-> Gerando laudo PDF [Resulado: %s] [Requisi��o: %s] [Paciente: %s]%n",
		    nrseqresultado, nrrequisicao, nroCadastroPaciente);
		try
		{
			this.gerarRelatorio(str, nrseqresultado, nroCadastroPaciente, nrrequisicao, res);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			throw new ServletException(ex);
		}
		finally
		{
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
	 * M�todo responsavel por montar estrutra do laudo e gerar o relatio pdf.
	 * @param str
	 * @param nrseqresultado
	 * @param nroCadastroPaciente
	 * @param nrrequisicao
	 * @param res
	 * @throws Exception
	 */
	private final void gerarRelatorio (final String str, final String nrseqresultado,
	    final String nroCadastroPaciente, final String nrrequisicao, final HttpServletResponse res)
	    throws Exception
	{
		if ("rtf".equals(str))
		{
			final List<LaudoVO> listaLaudos = DaoFactory.getInstance().getLaudos(
			    GenericUtils.toLong(nroCadastroPaciente), GenericUtils.toLong(nrseqresultado),
			    GenericUtils.toLong(nrrequisicao));

			if (!Validator.notEmptyCollection(listaLaudos) || (listaLaudos.get(0) == null))
			{
				throw new Exception("Laudo n�o localizado!!");
			}

			// Recuperar Laudo Original do Clinitools gravado na base
			// Oracle.
			final LaudoVO laudo = listaLaudos.get(0);

			// converter blob rtf para byte[]
			final byte[] relatorioRTF = laudo.getDsrtf().getBytes(1, (int)laudo.getDsrtf().length());

			// converter o conte�do laudo RTF para PDF.
			final byte[] relatorioPDF = new ConvertPDF().convertPDF(relatorioRTF);

			// Recuperar os dados da empresa.
			final PessoaVO empresa = DaoFactory.getInstance().getDadosEmpresa();
			if (empresa == null)
			{
				throw new Exception("N�o foi poss�vel localizar os dados da Cl�nica!!");
			}
			// gerar array de bytes via JasperReport contendo o cabe�alho pdf
			// para o relat�rio.
			final byte[] cabecalho = this.gerarCabecalhoPDF(laudo, empresa);

			// merge entre pdf com o cabe�alho e o conteudo do laudo.
			final List<InputStream> pdfs = new ArrayList<InputStream>();
			pdfs.add(new ByteArrayInputStream(cabecalho));
			pdfs.add(new ByteArrayInputStream(relatorioPDF));
			final MergePDF mergePDF = new MergePDF();
			final byte[] byteArrayMerged = mergePDF.concatPDFs(pdfs, empresa);

			// apresentar o pdf final.
			final String fileName = "laudoOnlinePDF";
			final LaudoReport report = new LaudoReport();
			report.showReport(res, fileName, byteArrayMerged, GenericReport.PDF_TYPE);
		}

	}

	/**
	 * Gerar o cabe�alho do relat�rio.
	 * @param laudo
	 * @param empresa
	 * @return byte[]
	 * @throws Exception
	 */
	private final byte[] gerarCabecalhoPDF (final LaudoVO laudo, final PessoaVO empresa)
	    throws Exception
	{
		try
		{
			final HttpServletRequest request = ServletActionContext.getRequest();
			final HttpServletResponse response = ServletActionContext.getResponse();

			// Parametros do cabe�alho
			final PessoaVO paciente = DaoFactory.getInstance().getPacienteByMatricula(
			    laudo.getCdpessoa());
			// dados do paciente
			if (paciente != null)
			{
				parameters.put(PACIENTE, "PACIENTE : ".concat(paciente.getNomePessoa() != null
				    ? paciente.getNomePessoa().toUpperCase()
				    : ""));
			}

			// M�dico solicitante do exame.
			final PessoaVO medicoSolicitante = DaoFactory.getInstance().getMedicoSolicitante(
			    laudo.getNrseqresultado());
			if ((medicoSolicitante != null)
			    && !Validator.isBlankOrNull(medicoSolicitante.getNomePessoa()))
			{
				parameters.put(DESCR_MEDICO,
				    "A/C : ".concat(GenericUtils.nullToBlank(medicoSolicitante.getNomePessoa())));
			}
			// Parametros para o m�dico respons�vel pelo Exame.
			final MedicoVO medRespExam = DaoFactory.getInstance().getMedicoResponsavelPorExame(
			    laudo.getNrrequisicao(), laudo.getNrseqresultado());
			if (medRespExam != null)
			{
				final StringBuilder buffer = new StringBuilder("Exame realizado pel");
				buffer.append(("F".equalsIgnoreCase(medRespExam.getSexo()) ? "a " : "o "));
				buffer.append(GenericUtils.nullToBlank(medRespExam.getSiglaTratamento())).append(
				    " ").append(GenericUtils.nullToBlank(medRespExam.getNomePessoa()));

				parameters.put(MEDICO_RESPONSAVEL_EXAME, buffer.toString());
				parameters.put(CRM_MEDICO_RESPONSAVEL_EXAME, medRespExam.getDescrCRM());
				if (empresa != null)
				{
					parameters.put(
					    CIDADE_DATA_EXAME,
					    GenericUtils.nullToBlank(empresa.getCidade()).concat(", ").concat(
					        GenericUtils.nullToBlank(DateUtils.formatDateExtenso(laudo.getDtconsulta()))));
				}
			}

			// Parametro Nome do Laudo
			parameters.put(NOME_LAUDO, laudo.getDsexamecompl());
			this.setParameters(parameters);

			final String fileName = "TONOMETRIA";// nome do jasper
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
