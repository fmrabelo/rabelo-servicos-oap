/**
 * 
 */
package br.com.oappr.infra.action.servlet;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.sql.Blob;
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
 * Servlet responsável pelas regras para receber dados do formulario web e em
 * seguida pesquisar os dados e gerar o laudo online em pdf.
 * @author Rabelo Serviços.
 */
public final class ServletReport
    extends HttpServlet
    implements ReportParameters, Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 13206501245454L;

	// parametros para relatorios jasper report
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
		    "%n-> Gerando laudo PDF [Resulado: %s] [Requisição: %s] [Paciente: %s]%n",
		    nrseqresultado, nrrequisicao, nroCadastroPaciente);
		try
		{
			this.gerarRelatorio(str, nrseqresultado, nroCadastroPaciente, nrrequisicao, res);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			res.getOutputStream().println(
			    "<html><p><span style='font-size:12pt; color:#0000FF;'>Ocorreu um erro ao Gerar o Laudo, favor entrar em contato com a OAP.</span></p>");
			res.getOutputStream().println(
			    "<p>Código de Erro:<br><span style='color:red'>" + ex.getMessage() + "</p></html>");
			// throw new ServletException(ex);
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
	 * Método responsável por montar a estrutra do laudo e gerar o relatório
	 * PDF.
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
		final String msg = "Laudo não localizado!!";
		if ("rtf".equals(str))
		{
			final List<LaudoVO> listaLaudos = DaoFactory.getInstance().getLaudos(
			    GenericUtils.toLong(nroCadastroPaciente), GenericUtils.toLong(nrseqresultado),
			    GenericUtils.toLong(nrrequisicao));

			if (!Validator.notEmptyCollection(listaLaudos) || (listaLaudos.get(0) == null))
			{
				throw new Exception(msg);
			}

			// Recuperar Laudo Original do Clinitools gravado na base
			// Oracle.
			final LaudoVO laudo = listaLaudos.get(0);
			if ((laudo == null) || (laudo.getDsrtf() == null) || (laudo.getDsrtf().length() <= 0))
			{
				throw new Exception(msg);
			}

			// converter blob rtf para byte[]
			final byte[] relatorioRTF = laudo.getDsrtf().getBytes(1, (int)laudo.getDsrtf().length());
			// converter o conteúdo laudo RTF para PDF.
			final byte[] relatorioPDF = new ConvertPDF().convertRTF_To_PDF(relatorioRTF);

			// Recuperar os dados da empresa.
			final PessoaVO empresa = DaoFactory.getInstance().getDadosEmpresa();
			if (empresa == null)
			{
				throw new Exception("Não foi possível localizar os dados da Clínica!!");
			}
			// gerar array de bytes via JasperReport contendo o cabeçalho pdf
			// para o relatório.
			final byte[] cabecalho = this.gerarCabecalhoPDF(laudo, empresa);

			// merge entre pdf com o cabeçalho e o conteudo do laudo.
			final List<InputStream> pdfs = new ArrayList<InputStream>();
			pdfs.add(new ByteArrayInputStream(cabecalho));
			pdfs.add(new ByteArrayInputStream(relatorioPDF));

			// imagens
			// TODO: APENAS TESTE DE LEITURA DA TABELA DE TESTE DA OAP E
			// ESCREVER EM ARQUIVO
			// this.testeLerFotoGravarEmArquivo();

			// converter imagens dos laudos para byte[]
			// final byte[] images = laudo.getImages().getBytes(1,
			// (int)laudo.getImages().length());

			final String nomeExame = laudo.getDsexamecompl();
			// if ((nomeExame != null) &&
			// nomeExame.trim().toUpperCase().startsWith("MICROSCOPIA"))
			// {
			// System.out.println(" É MICROSCOPIA FIMMMMM... ");
			// }

			for (final Blob bl : laudo.getImages())
			{
				if (bl != null)
				{
					final byte[] imagesPDF = new ConvertPDF().convertJPG_To_PDF(bl);
					pdfs.add(new ByteArrayInputStream(imagesPDF));
				}
			}
			final MergePDF mergePDF = new MergePDF();
			final byte[] byteArrayMerged = mergePDF.concatPDFs(pdfs, empresa);

			// apresentar o pdf final com nome para o arquivo pdf.
			final StringBuilder pdfFileName = new StringBuilder("laudoOnline_");
			pdfFileName.append(nomeExame != null ? nomeExame : "").append("_").append(
			    laudo.getCdpessoa() != null ? laudo.getCdpessoa() : "");
			final LaudoReport report = new LaudoReport();
			report.showReport(res, pdfFileName.toString(), byteArrayMerged, GenericReport.PDF_TYPE);
		}
	}

	/**
	 * @throws Exception
	 */
	@Deprecated
	@SuppressWarnings({"deprecation", "unused"})
	private void testeLerFotoGravarEmArquivo () throws Exception
	{
		try
		{
			List<LaudoVO> list = DaoFactory.getInstance().getLaudosLixo();
			for (LaudoVO lixo : list)
			{
				final String output = "c://temp//img_oap//RETORNO_TABELA_LIXOIMAGEM_ID_"
				    + lixo.getCdconvenio() + ".jpg";
				File f = new File(output);
				final FileOutputStream fos = new FileOutputStream(f);
				// while ((length = in.read(buffer)) != -1)
				// {
				// fos.write(buffer, 0, length);
				// }
				// in.close();
				fos.flush();
				fos.close();
				f = null;
			}
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			throw ex;
		}
	}

	/**
	 * Gerar o cabeçalho do relatório.
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

			// Parametros do cabeçalho
			final PessoaVO paciente = DaoFactory.getInstance().getPacienteByCodMatricula(
			    laudo.getCdpessoa());
			// dados do paciente
			String DSCR_PACIENTE = "";
			if ((paciente != null) && !Validator.isBlankOrNull(paciente.getNomePessoa()))
			{
				DSCR_PACIENTE = paciente.getNomePessoa().toUpperCase();
			}
			parameters.put(LABEL_PACIENTE, ("PACIENTE: ").concat(DSCR_PACIENTE));

			// Médico solicitante do exame.
			String DESCR_MEDICO = "";
			final PessoaVO medicoSolicitante = DaoFactory.getInstance().getMedicoSolicitante(
			    laudo.getNrseqresultado());
			if ((medicoSolicitante != null)
			    && !Validator.isBlankOrNull(medicoSolicitante.getNomePessoa()))
			{
				DESCR_MEDICO = medicoSolicitante.getNomePessoa();
			}
			parameters.put(LABEL_DESCR_MEDICO, "A/C: ".concat(DESCR_MEDICO));

			// Parametros para o médico responsável pelo Exame.
			final MedicoVO medRespExam = DaoFactory.getInstance().getMedicoResponsavelPorExame(
			    laudo.getNrrequisicao(), laudo.getNrseqresultado());
			final StringBuilder buffer = new StringBuilder("");
			String DESC_CRM_MEDICO = "";
			String DESC_CIDADE_EXAME = "";
			if (medRespExam != null)
			{
				// buffer.append("Exame realizado pel");
				// buffer.append(("F".equalsIgnoreCase(medRespExam.getSexo()) ?
				// "a " : "o "));
				buffer.append(GenericUtils.nullToBlank(medRespExam.getSiglaTratamento())).append(
				    " ").append(GenericUtils.nullToBlank(medRespExam.getNomePessoa()));
				DESC_CRM_MEDICO = medRespExam.getDescrCRM();
				if (empresa != null)
				{
					DESC_CIDADE_EXAME = GenericUtils.nullToBlank(empresa.getCidade()).concat(", ").concat(
					    GenericUtils.nullToBlank(DateUtils.formatDateExtenso(laudo.getDtconsulta())));
				}
			}
			parameters.put(LABEL_MEDICO_RESPONSAVEL_EXAME, buffer.toString());
			parameters.put(LABEL_CRM_MEDICO_RESPONSAVEL_EXAME, DESC_CRM_MEDICO);
			parameters.put(LABEL_CIDADE_DATA_EXAME, DESC_CIDADE_EXAME);

			// Parametro Nome do Laudo
			parameters.put(LABEL_NOME_LAUDO, laudo.getDsexamecompl());
			this.setParameters(parameters);

			// nome do arquivo jasper, alterar este nome somente se alterar o
			// nome do arquivo fonte jrxml.
			final String jasperFileName = "TONOMETRIA";
			final LaudoReport report = new LaudoReport();
			return report.getByteArrayCabecalhoPDF(request, response, jasperFileName, null,
			    parameters, GenericReport.PDF_TYPE);
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
