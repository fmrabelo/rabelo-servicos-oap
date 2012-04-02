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
import br.com.oappr.infra.util.GenericUtils;
import br.com.oappr.infra.util.Validator;
import br.com.oappr.intranet.vo.LaudoVO;
import br.com.oappr.intranet.vo.PessoaVO;
import br.com.oappr.merge.pdf.MergePDF;

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

	@Override
	public void doGet (HttpServletRequest req, HttpServletResponse res) throws IOException,
	    ServletException
	{
		final String str = req.getParameter("Text1");
		final String nrseqresultado = req.getParameter("nrseqresultado");
		final String nroCadastroPaciente = req.getParameter("nroCadastroPaciente");
		try
		{
			System.out.println("nrseqresultado: " + nrseqresultado);
			System.out.println("nroCadastroPaciente: " + nroCadastroPaciente);
			if ("rtf".equals(str))
			{
				final List<LaudoVO> listaLaudos = DaoFactory.getInstance().getLaudos(
				    GenericUtils.toLong(nroCadastroPaciente), GenericUtils.toLong(nrseqresultado));

				if (!Validator.notEmptyCollection(listaLaudos))
				{
					return;
				}

				// Recuperar Laudo Original do Clinitools gravado na base
				// Oracle.
				final LaudoVO laudo = listaLaudos.get(0);

				// converter blob para byte[]
				final byte[] relatorioRTF = laudo.getDsrtf().getBytes(1,
				    (int)laudo.getDsrtf().length());

				// converter o laudo CLINITOOLS de RTF para PDF.
				final byte[] relatorioPDF = new ConvertPDF().convertPDF(relatorioRTF);

				// gerar arquivo pdf contendo o cabeçalho padrão com
				// JasperReport.
				final byte[] cabecalho = this.gerarCabecalhoPDF(laudo);

				// merge entre o cabeçalho e o conteudo do laudo.
				final List<InputStream> pdfs = new ArrayList<InputStream>();
				pdfs.add(new ByteArrayInputStream(cabecalho));
				pdfs.add(new ByteArrayInputStream(relatorioPDF));
				final MergePDF mergePDF = new MergePDF();
				final byte[] byteArrayMerge = mergePDF.concatPDFs(pdfs);

				// apresentar o pdf final.
				final String fileName = "laudoOnlinePDF";
				this.report(res, fileName, GenericReport.PDF_TYPE, byteArrayMerge);
			}
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
	 * @param response
	 * @param fileName
	 * @param reportType
	 * @param relatorio
	 * @throws Exception
	 */
	public void report (HttpServletResponse response, String fileName, String reportType,
	    byte[] relatorio) throws Exception
	{
		if ((relatorio != null) && (relatorio.length > 0))
		{
			response.setContentType(new StringBuilder("application/").append(reportType).toString());
			response.setHeader("Content-disposition",
			    new StringBuilder("inline; filename=\"").append(fileName).append(".").append(
			        reportType).append("\";").toString());

			this.setResponseHeader(response);
			response.setContentLength(relatorio.length);
			response.getOutputStream().write(relatorio, 0, relatorio.length);
			response.getOutputStream().flush();
			response.flushBuffer();
			response.getOutputStream().close();
		}
		relatorio = null;
	}

	/**
	 * Seta parametros comuns no header do response
	 * @param response
	 */
	private void setResponseHeader (HttpServletResponse response)
	{
		response.setHeader("Expires", "0");
		response.setHeader("Pragma", "public");
		response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
		response.setHeader("Content-Encoding:", " gzip");
	}

	/**
	 * 
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

			// Parametros do cabeçalho
			parameters.put(PACIENTE, "PACIENTE : ".concat(paciente.getNomePessoa() != null
			    ? paciente.getNomePessoa().toUpperCase()
			    : ""));
			parameters.put(DESCR_MEDICO, "A/C : " + " DRA MARIA ISABEL BORA CASTALDO ANDRADE ");

			// Parametros para responsável pelo Exame
			parameters.put(MEDICO_RESPONSAVEL_EXAME,
			    "Exame realizado pela Dra Maria Isabel Bora Castaldo Andrade ");
			parameters.put(CRM_MEDICO_RESPONSAVEL_EXAME, "CRM-14831 PR");
			parameters.put(CIDADE_DATA_EXAME, "Curitiba, 01 de setembro de 2011");

			// TODO: ADICIONAR parametros FOOTER
			// ..............

			// Parametro Nome do Laudo
			parameters.put(NOME_LAUDO, laudo.getDsexamecompl());
			this.setParameters(parameters);
			//

			final String fileName = "TONOMETRIA";
			final LaudoReport report = new LaudoReport();
			return report.getByteArrayPDF(request, response, fileName, null, parameters,
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
