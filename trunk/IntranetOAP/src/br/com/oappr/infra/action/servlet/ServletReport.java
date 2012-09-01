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
 * Servlet respons�vel pelas regras para receber dados do formulario web e em
 * seguida pesquisar os dados e gerar o laudo online em pdf.
 * @author Rabelo Servi�os.
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
			// gravar arquivo
			final String PATH = "c://temp//ceratoscopia_04082012.jpg";
			final File imagem = new File(PATH);
			if (imagem.exists())
			{
				// ByteArrayOutputStream ous = null;
				// InputStream ios = null;
				// try
				// {
				// byte[] buffer = new byte[4096];
				// ous = new ByteArrayOutputStream();
				// ios = new FileInputStream(imagem);
				// int read = 0;
				// while ((read = ios.read(buffer)) != -1)
				// {
				// ous.write(buffer, 0, read);
				// }
				// }
				// finally
				// {
				// try
				// {
				// if (ous != null)
				// {
				// ous.close();
				// }
				// }
				// catch (IOException e)
				// {
				// // swallow, since not that important
				// }
				// try
				// {
				// if (ios != null)
				// {
				// ios.close();
				// }
				// }
				// catch (IOException e)
				// {
				// // swallow, since not that important
				// }
				// }
				// final FileInputStream is = new FileInputStream(imagem);
				DaoFactory.getInstance().gravaImagem(imagem);
			}

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
			final byte[] relatorioPDF = new ConvertPDF().convertRTF_To_PDF(relatorioRTF);

			// converter imagens dos laudos para byte[]
			final byte[] images = laudo.getImages().getBytes(1, (int)laudo.getImages().length());
			try
			{
				List<LaudoVO> list = DaoFactory.getInstance().getLaudosLixo();
				final String output = "c://temp//ceratoscopia_RETORNO.jpg";
				File f = new File(output);
				final FileOutputStream fos = new FileOutputStream(f);
				InputStream in = list.get(0).getImages().getBinaryStream();
				int length = (int)laudo.getImages().length();
				int bufferSize = 1024;
				byte[] buffer = new byte[bufferSize];
				while ((length = in.read(buffer)) != -1)
				{
					fos.write(buffer, 0, length);
				}
				in.close();
				fos.flush();
				fos.close();
				f = null;

			}
			catch (Exception ex)
			{
				ex.printStackTrace();
				throw ex;
			}

			// converter images para PDF.
			final byte[] imagesPDF = new ConvertPDF().convertJPG_To_PDF(images);

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
			// pdfs.add(new ByteArrayInputStream(imagesPDF));
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
			String DSCR_PACIENTE = "";
			if ((paciente != null) && !Validator.isBlankOrNull(paciente.getNomePessoa()))
			{
				DSCR_PACIENTE = paciente.getNomePessoa().toUpperCase();
			}
			parameters.put(LABEL_PACIENTE, ("PACIENTE: ").concat(DSCR_PACIENTE));

			// M�dico solicitante do exame.
			String DESCR_MEDICO = "";
			final PessoaVO medicoSolicitante = DaoFactory.getInstance().getMedicoSolicitante(
			    laudo.getNrseqresultado());
			if ((medicoSolicitante != null)
			    && !Validator.isBlankOrNull(medicoSolicitante.getNomePessoa()))
			{
				DESCR_MEDICO = medicoSolicitante.getNomePessoa();
			}
			parameters.put(LABEL_DESCR_MEDICO, "A/C: ".concat(DESCR_MEDICO));

			// Parametros para o m�dico respons�vel pelo Exame.
			final MedicoVO medRespExam = DaoFactory.getInstance().getMedicoResponsavelPorExame(
			    laudo.getNrrequisicao(), laudo.getNrseqresultado());
			final StringBuilder buffer = new StringBuilder("");
			String DESC_CRM_MEDICO = "";
			String DESC_CIDADE_EXAME = "";
			if (medRespExam != null)
			{
				buffer.append("Exame realizado pel");
				buffer.append(("F".equalsIgnoreCase(medRespExam.getSexo()) ? "a " : "o "));
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