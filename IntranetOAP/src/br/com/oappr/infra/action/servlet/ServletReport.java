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

	/***************************************************************************
	 * Hist�rico de Atualiza��es: Configura��o utilizada at� 13/03/2013 para
	 * imagens sem aumento de tamanho: <br>
	 * EXAMES_MENOR_ZOOM[] = {"ceratoscopia", "campimetria", "Iol Master"};
	 */

	// Exames cujas imagens n�o necessitam muito Zoom
	public static final String EXAMES_MENOR_ZOOM[] = {"ceratoscopia", "campimetria"};

	// parametros para relatorios jasper report
	HashMap<String, String> parameters = null;

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
		System.out.printf("%n%s%n", "---------------------------------------------------");
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
			res.getOutputStream().println(
			    "<html><p><span style='font-size:12pt; color:#0000FF;'>Ocorreu um erro ao Gerar o Laudo, favor entrar em contato com a OAP.</span></p>");
			res.getOutputStream().println(
			    "<p>C�digo de Erro:<br><span style='color:red'>" + ex.getMessage() + "</p></html>");
			// throw new ServletException(ex);
		}
		finally
		{
			// teste fechar conexao jdbc.
			try
			{
				DaoFactory.getInstance().closeConection(null, null);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
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
	 * M�todo respons�vel por montar a estrutra do laudo e gerar o relat�rio
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

		long totalTime = System.currentTimeMillis(), iniTime = 0;
		final String msg = "Laudo n�o localizado!!";
		if ("rtf".equals(str))
		{
			iniTime = System.currentTimeMillis();
			System.out.println("> getLaudo()....");
			final List<LaudoVO> listaLaudos = DaoFactory.getInstance().getLaudos(
			    GenericUtils.toLong(nroCadastroPaciente), GenericUtils.toLong(nrseqresultado),
			    GenericUtils.toLong(nrrequisicao));
			print((System.currentTimeMillis() - iniTime));

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
			iniTime = System.currentTimeMillis();
			System.out.print("> converter blob rtf para byte[]....");
			final byte[] relatorioRTF = laudo.getDsrtf().getBytes(1, (int)laudo.getDsrtf().length());
			print((System.currentTimeMillis() - iniTime));

			// converter o conte�do laudo RTF para PDF.
			iniTime = System.currentTimeMillis();
			System.out.print("> convertRTF_To_PDF....");
			final byte[] relatorioPDF = new ConvertPDF().convertRTF_To_PDF(relatorioRTF);
			print((System.currentTimeMillis() - iniTime));

			// Recuperar os dados da empresa.
			iniTime = System.currentTimeMillis();
			System.out.print("> Recuperar os dados da empresa....");
			final PessoaVO empresa = DaoFactory.getInstance().getDadosEmpresa();
			print((System.currentTimeMillis() - iniTime));
			if (empresa == null)
			{
				throw new Exception("N�o foi poss�vel localizar os dados da Cl�nica!!");
			}

			// gerar array de bytes via JasperReport contendo o cabe�alho pdf
			// para o relat�rio.
			iniTime = System.currentTimeMillis();
			System.out.print("> gerar cabe�alho com jasper para pdf ....");
			final byte[] cabecalho = this.gerarCabecalhoPDF(laudo, empresa);
			print((System.currentTimeMillis() - iniTime));

			// merge entre pdf com o cabe�alho e o conteudo do laudo.
			iniTime = System.currentTimeMillis();
			System.out.print("> merge entre pdf com o cabe�alho e o conteudo do laudo ....");
			final List<InputStream> pdfs = new ArrayList<InputStream>();
			pdfs.add(new ByteArrayInputStream(cabecalho));
			pdfs.add(new ByteArrayInputStream(relatorioPDF));
			print((System.currentTimeMillis() - iniTime));

			// imagens
			// TODO: APENAS TESTE DE LEITURA DA TABELA DE TESTE DA OAP E
			// ESCREVER EM ARQUIVO
			// this.testeLerFotoGravarEmArquivo();

			// converter imagens dos laudos para byte[]
			// final byte[] images = laudo.getImages().getBytes(1,
			// (int)laudo.getImages().length());

			// TODO: L�gica para tratar especificamente as imagens de
			// Microscopia, que provavelmente ser�o distribuidas 6 por p�gina
			// A4.
			final String nomeExame = GenericUtils.replaceValidUpperChar(laudo.getDsexamecompl());
			// if ((nomeExame != null) &&
			// nomeExame.trim().toUpperCase().startsWith("MICROSCOPIA"))
			// {
			// System.out.println(" � MICROSCOPIA... ");
			// }

			// Processamento das Imagens do laudo.
			if ((laudo.getImages() != null) && (laudo.getImages().size() > 0))
			{
				iniTime = System.currentTimeMillis();
				System.out.print("> Processamento das Imagens do laudo....");
				for (final Blob bl : laudo.getImages())
				{
					// existe conte�do na imagem
					if (bl != null)
					{
						final byte[] imagesPDF = new ConvertPDF().convertJPG_To_PDF(bl, nomeExame);
						// adiciona imagem ao pdf.
						pdfs.add(new ByteArrayInputStream(imagesPDF));
					}
				}
				print((System.currentTimeMillis() - iniTime));
			}

			iniTime = System.currentTimeMillis();
			System.out.print("> concatena PDFs....");
			final MergePDF mergePDF = new MergePDF();
			final byte[] byteArrayMerged = mergePDF.concatPDFs(pdfs, empresa);
			print((System.currentTimeMillis() - iniTime));

			// apresentar o pdf final com nome para o arquivo pdf.
			iniTime = System.currentTimeMillis();
			System.out.print("> apresentar o pdf final com nome para o arquivo pdf....");
			final StringBuilder pdfFileName = new StringBuilder("laudoOnline_");
			pdfFileName.append(laudo.getCdpessoa() != null ? laudo.getCdpessoa() : "").append("_").append(
			    nomeExame != null ? nomeExame.toUpperCase() : "").append("_").append(
			    GenericUtils.replaceALL(DateUtils.formatDateDDMMYYYY(laudo.getDtconsulta()), "/",
			        "_"));
			final LaudoReport report = new LaudoReport();
			report.showReport(res, pdfFileName.toString(), byteArrayMerged, GenericReport.PDF_TYPE);
			print((System.currentTimeMillis() - iniTime));
			System.out.println("> TEMPO TOTAL:  " + (System.currentTimeMillis() - totalTime) / 1000
			    + "s");
		}
	}

	public static void print (final long time)
	{
		System.out.printf("%s%d%s%n", ".... OK  ", time, "ms");
	}

	/**
	 * Gerar arquivo f�sico local contendo a assinatura digital do M�dico.
	 * @throws Exception
	 */
	private final String assinaturaDigitalMedico (final Blob blob, String nomeMedico)
	{
		if (Validator.isBlankOrNull(nomeMedico))
		{
			nomeMedico = "assinaturaMedicoOAP";
		}
		try
		{
			File tempFile = File.createTempFile(nomeMedico.trim(), ".jpg");
			final FileOutputStream fos = new FileOutputStream(tempFile);
			final byte[] byteArrayImg = blob.getBytes(1, (int)blob.length());
			fos.write(byteArrayImg);
			fos.flush();
			fos.close();
			if ((tempFile != null) && (tempFile.length() > 0))
			{
				tempFile.deleteOnExit();
				return tempFile.getAbsolutePath();
			}
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
		return "";
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
	 * Gerar o cabe�alho do relat�rio.
	 * @param laudo
	 * @param empresa
	 * @return byte[]
	 * @throws Exception
	 */
	private final byte[] gerarCabecalhoPDF (final LaudoVO laudo, final PessoaVO empresa)
	    throws Exception
	{
		parameters = new HashMap<String, String>();
		try
		{
			final HttpServletRequest request = ServletActionContext.getRequest();
			final HttpServletResponse response = ServletActionContext.getResponse();

			// Parametros do cabe�alho
			final PessoaVO paciente = DaoFactory.getInstance().getPacienteByCodMatricula(
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
				// parametro para assinatura digital do m�dico
				if ((medRespExam.getAssinaturaDigital() != null)
				    && (medRespExam.getAssinaturaDigital().length() > 0))
				{
					final String path = this.assinaturaDigitalMedico(
					    medRespExam.getAssinaturaDigital(), medRespExam.getNomePessoa());
					if (!Validator.isBlankOrNull(path))
					{
						parameters.put(LABEL_ASSINATURA_MEDICO_PATH, path);
						System.out.printf("%n-> Assinatura Digital do Medico : %s%n", path);
					}
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
