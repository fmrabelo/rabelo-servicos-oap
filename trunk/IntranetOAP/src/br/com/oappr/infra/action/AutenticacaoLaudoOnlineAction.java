/**
 * 
 */
package br.com.oappr.infra.action;

import java.awt.Dialog;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;
import net.sourceforge.rtf.RTFTemplate;
import net.sourceforge.rtf.helper.RTFTemplateBuilder;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;

import br.com.oappr.infra.DAO.DaoFactory;
import br.com.oappr.infra.util.DateUtils;
import br.com.oappr.intranet.vo.LaudoVO;
import br.com.oappr.intranet.vo.PessoaVO;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.HeaderFooter;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.rtf.RtfWriter2;
import com.lowagie.text.rtf.parser.RtfParser;
import com.opensymphony.xwork2.ActionSupport;

/**
 * @author desenvolvimento
 */
// @Validations(requiredStrings = {@RequiredStringValidator(fieldName =
// "nroCadastroPaciente", message = "Valor obrigatório")}, stringLengthFields =
// {@StringLengthFieldValidator(fieldName = "nroCadastroPaciente", minLength =
// "5", message = "Min. 5 car.")})
public class AutenticacaoLaudoOnlineAction
    extends ActionSupport
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 13102010197645454L;

	private PessoaVO pessoaVo;

	// parametros para jasper report
	HashMap<String, Object> parameters = new HashMap<String, Object>();

	// numero do cadastro do paciente
	private Long nroCadastroPaciente;
	private Date dataNascimento;
	// numero de laudo especifico.
	private Long nroLaudo;

	/**
	 * @return the parameters
	 */
	public HashMap<String, Object> getParameters ()
	{
		return parameters;
	}

	/**
	 * @param parameters the parameters to set
	 */
	public void setParameters (HashMap<String, Object> parameters)
	{
		this.parameters = parameters;
	}

	/**
	 * 
	 */
	@Override
	@Action(value = "autenticacaoLaudoOnline", results = {@Result(location = "/jsp/autenticacaoLaudoOnline.jsp", name = "success")})
	public String execute ()
	{
		System.out.println("Executando a lógica com Struts2");
		this.setDataNascimento(Calendar.getInstance().getTime());
		// return "ok";
		return com.opensymphony.xwork2.Action.SUCCESS;
	}

	/**
	 * @return
	 * @throws Exception
	 */
	@Action(value = "listarLaudos", results = {@Result(location = "/jsp/listaLaudos.jsp", name = "success")})
	public String listarLaudos () throws Exception
	{
		// pesquisar laudos
		try
		{
			this.setListaLaudos(DaoFactory.getInstance().getLaudos(this.getNroCadastroPaciente(),
			    null));
			System.out.println("listar laudos. Quantidade-> "
			    + (this.getListaLaudos() != null ? this.getListaLaudos().size() : 0));
			if ((this.getListaLaudos() != null) && !this.getListaLaudos().isEmpty())
			{
				for (LaudoVO v : this.getListaLaudos())
				{
					System.out.printf("ID: %s Laudo: %s %n", v.getNrlaudo(), v.getDsexamecompl());
				}
			}
			else
			{
				this.addFieldError("nroCadastroPaciente", "Nenhum registro de Laudo localizado!!");
				return com.opensymphony.xwork2.Action.ERROR;
			}
		}
		catch (Exception ex)
		{
			throw new Exception(ex);
		}
		return com.opensymphony.xwork2.Action.SUCCESS;
	}

	/**
	 * @param filename
	 * @param blob
	 * @return
	 */
	public static boolean blobToFile2 (String filename, Blob blob)
	{
		boolean success = true;
		try
		{
			// Get as a BLOB and put in a byte array.
			byte[] allBytesInBlob = blob.getBytes(1, (int)blob.length());
			final FileOutputStream outStream = new FileOutputStream(new File(filename));
			outStream.write(allBytesInBlob);
			outStream.flush();
			outStream.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			System.out.println("ERROR(djv_exportBlob) Unable to export:" + filename);
			success = false;
		}
		finally
		{
			return success;
		}
	}

	/**
	 * @param filename
	 * @param blob
	 * @return
	 */
	public static boolean blobToFile (String filename, Blob blob)
	{
		boolean success = true;
		try
		{
			final FileOutputStream outStream = new FileOutputStream(new File(filename));
			byte[] blobBuffer = new byte[(int)blob.length()];
			final InputStream inStream = blob.getBinaryStream();
			int length = -1;
			while ((length = inStream.read(blobBuffer)) != -1)
			{
				outStream.write(blobBuffer, 0, length);
				outStream.flush();
			}
			inStream.close();
			outStream.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			System.out.println("ERROR(djv_exportBlob) Unable to export:" + filename);
			success = false;
		}
		finally
		{
			return success;
		}
	}

	/**
	 * @param lista
	 * @param caminhoRel
	 * @throws Exception
	 */
	public void gerarRelPdf (List lista, String caminhoRel, String caminhoLogo) throws Exception
	{
		try
		{
			// passar o rtf como relatorio....
			caminhoRel = "C:\\downloads\\text_375688.rtf";

			// InputStream relatorio =
			// this.getClass().getResourceAsStream(caminhoRel);
			HashMap<String, Object> parametros = new HashMap<String, Object>();
			parametros.put("LOGO", caminhoLogo);
			// JRBeanCollectionDataSource colection = new
			// JRBeanCollectionDataSource(lista);
			// JREmptyDataSource colection = new JREmptyDataSource();
			// JasperPrint impressao = JasperFillManager.fillReport(relatorio,
			// parametros, colection);
			JasperPrint impressao = JasperFillManager.fillReport(caminhoRel, parametros);
			JasperExportManager.exportReportToPdf(impressao);

			// JasperReport report = JasperCompileManager.compileReport(jrxml);
			// JasperPrint impressao = JasperFillManager.fillReport(report,
			// parameters);

			final JasperViewer jv = new JasperViewer(impressao, false);
			jv.setVisible(true);
			jv.setModalExclusionType(Dialog.ModalExclusionType.APPLICATION_EXCLUDE);

		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			// JOptionPane.showMessageDialog(null, "Não foi possivel abrir o
			// relatório");
		}
	}

	public static void itextTemplate ()
	{
		String rtfSource = "C:\\downloads\\text_rtf.rtf";
		String rtfTarget = "C:\\downloads\\DocumentoFinal.rtf";
		try
		{
			// 1. Get default RTFtemplateBuilder
			RTFTemplateBuilder builder = RTFTemplateBuilder.newRTFTemplateBuilder();

			// 2. Get RTFtemplate with default Implementation of template engine
			// (Velocity)
			RTFTemplate rtfTemplate = builder.newRTFTemplate();

			// 3. Set the RTF model source
			rtfTemplate.setTemplate(new File(rtfSource));

			// 4. AKI ALTERA AS VARIAVEIS
			rtfTemplate.put("project", "Jakarta Velocity project");

			// 5. AKI GRAVA O DOCUMENTO FINAL
			rtfTemplate.merge(rtfTarget);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}

	/**
	 * @throws Exception
	 */
	public static void addInRtf () throws Exception
	{
		// step 1: creation of a document-object
		Document document = new Document();

		// step 2:
		// we create a writer that listens to the document
		// and directs a RTF-stream to a file
		RtfWriter2.getInstance(document, new FileOutputStream("C:\\downloads\\text_rtf.rtf"));
		// step 3: we open the document
		document.open();
		// header
		Phrase phrase = new Phrase("This is the first chapter");
		HeaderFooter header1 = new HeaderFooter(phrase, true);
		document.setHeader(header1);

		Font titleFont = new Font(Font.TIMES_ROMAN, 14, Font.BOLD);
		document.add(new Paragraph("Title1", titleFont));
		// step 4: we add a paragraph to the document
		Paragraph p = new Paragraph("Hello", titleFont);
		String args[] = new String[]{
		    "O exame de Microscopia Especular de Córnea foi realizado no Konan Specular Microscope Robo Pachy SP-9000.",
		    "ANÁLISE MORFO-CELULARES",
		    "- Predomínio de células hexagonais (6ª) em 00% na área analisada."};
		for (int i = 0; i < args.length; i++)
		{
			p.add("\n");
			p.add(args[i]);
		}
		document.add(p);

		// step 5: we close the document
		document.close();
	}

	/**
	 * 
	 */
	public static void convertRTFToPDF ()
	{
		String inputFile = "C:\\downloads\\text_375688.rtf";
		String outputFile = "C:\\downloads\\text_375688__itext.pdf";
		// create a new document
		Document document = new Document();
		try
		{
			// create a PDF writer to save the new document to disk
			PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(outputFile));
			// open the document for modifications
			document.open();
			Font titleFont = new Font(Font.TIMES_ROMAN, 14, Font.BOLD);
			document.add(new Paragraph("Title1", titleFont));
			// create a new parser to load the RTF file
			RtfParser parser = new RtfParser();
			// read the rtf file into a compatible document
			parser.convertRtfDocument(new FileInputStream(inputFile), document);
			// save the pdf to disk
			document.close();
			System.out.println("Finished");
		}
		catch (DocumentException e)
		{
			e.printStackTrace();
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Gerar relatorio pdf.
	 * @return
	 */
	public String gerarLaudoPdf ()
	{
		System.out.println("Print PDF laudo especifico...");
		// pesquisar laudos
		try
		{
			Long nroCadastroPaciente2 = this.getNroCadastroPaciente();
			if (nroCadastroPaciente2 == null)
			{
				nroCadastroPaciente2 = 114863L;
			}
			this.setListaLaudos(DaoFactory.getInstance().getLaudos(nroCadastroPaciente2,
			    this.getNroLaudo()));
			System.out.println("listar laudo. Quantidade-> "
			    + (this.getListaLaudos() != null ? this.getListaLaudos().size() : 0));
			if ((this.getListaLaudos() != null) && !this.getListaLaudos().isEmpty())
			{
				for (LaudoVO v : this.getListaLaudos())
				{
					System.out.printf("ID: %s Laudo: %s %n", v.getNrlaudo(), v.getDsexamecompl());
				}
			}
			else
			{
				this.addFieldError("nroCadastroPaciente", "Nenhum registro de Laudo localizado!!");
				return com.opensymphony.xwork2.Action.ERROR;
			}

			// HttpServletRequest request = ServletActionContext.getRequest();
			// String imgPath = "";
			//
			// if (request != null)
			// {
			// imgPath = request.getSession().getServletContext().getRealPath(
			// new
			// StringBuilder(File.separator).append("images").append(File.separator).append(
			// "logo").append(File.separator).append("logo2.jpg").toString());
			// }
			// String id = request.getParameter("id");

			// recuperar laudo pelo id

			// this.setListaLaudos(this.getLaudos());
			// if ((this.getListaLaudos() != null) &&
			// !this.getListaLaudos().isEmpty())
			// {
			// for (final LaudoVO v : this.getListaLaudos())
			// {
			// if (v.getCdpessoa().longValue() == new Long(id).longValue())
			// {
			// // laudo = v.getDescricao();
			// break;
			// }
			// }
			// }

			// if ((this.getListaLaudos() != null) &&
			// !this.getListaLaudos().isEmpty())
			// {
			// System.out.println("Lista Laudos OK. " +
			// this.getListaLaudos().size());
			// }
			// else
			// {
			// this.listarLaudos();
			// }

			// teste RTF to PDF
			// convertRTFToPDF();
			addInRtf();
			//
			// byte[] allBytesInBlob =
			// this.getListaLaudos().get(0).getDsrtf().getBytes(1,
			// (int)this.getListaLaudos().get(0).getDsrtf().length());
			// System.out.println(new String(allBytesInBlob));
			//
			// // parameters.put("LAUDO_RTF", new String(allBytesInBlob));
			//
			// // parameters.put("LAUDO_RTF",
			// // this.getListaLaudos().get(0).getDsrtf().getBinaryStream());
			//
			// parameters.put("LAUDO_RTF",
			// "{\\rtf1\\ansi\\ansicpg1252\\deff0\\deflang1046{\\fonttbl{\\f0\fswiss\\fprq2\\fcharset0
			// Arial;}}");
			//
			// parameters.put("LOGO_PATH", imgPath);
			// parameters.put("PACIENTE", "PACIENTE : " +
			// this.getPessoaVo().getNomePessoa());
			// parameters.put("DESCR_MEDICO", "A/C : " + " XING LING PING ");
			// parameters.put("DESCR_LAUDO",
			// this.getListaLaudos().get(0).getDsexamecompl());
			// this.setParameters(parameters);
			//
			// String jrxml =
			// request.getSession().getServletContext().getRealPath(
			// new
			// StringBuilder(File.separator).append("reports").append(File.separator).append(
			// "TONOMETRIA").append(".jrxml").toString());
			//
			// String jasper =
			// request.getSession().getServletContext().getRealPath(
			// new
			// StringBuilder(File.separator).append("reports").append(File.separator).append(
			// "TONOMETRIA").append(".jasper").toString());

			// abrir relatorio
			// JasperCompileManager.compileReportToFile(jrxml, jasper);

			// this.gerarRelPdf(new ArrayList<Object>(), jrxml, imgPath);

			// JasperReport report = JasperCompileManager.compileReport(jrxml);
			// JasperPrint impressao = JasperFillManager.fillReport(report,
			// parameters);
			//
			// JRPdfExporter pdf = new JRPdfExporter();
			// pdf.setParameter(JRExporterParameter.JASPER_PRINT, impressao);
			// pdf.setParameter(JRExporterParameter.INPUT_STREAM,
			// this.getListaLaudos().get(0).getDsrtf().getBinaryStream());
			// pdf.exportReport();

		}
		catch (Exception e)
		{
			e.printStackTrace();
			return com.opensymphony.xwork2.Action.ERROR;
		}

		return com.opensymphony.xwork2.Action.SUCCESS;
	}

	/**
	 * @return
	 */
	@Action(value = "autenticarPaciente", results = {
	    @Result(location = "/jsp/listaLaudos.jsp", name = "success"),
	    @Result(location = "/jsp/autenticacaoLaudoOnline.jsp", name = "error")})
	public String autenticarPaciente ()
	{
		final Long cdPessoa = this.getNroCadastroPaciente();

		// validar campos de entrada.
		if (cdPessoa == null)
		{
			this.addFieldError("nroCadastroPaciente", "* Campo Obrigatório. (Usar apenas Números)");
			return com.opensymphony.xwork2.Action.ERROR;
		}
		if (this.getDataNascimento() == null)
		{
			this.addFieldError("dataNascimento",
			    "* Campo Obrigatório. Preencher com uma Data Válida no formato dd/mm/aaaa (Exemplo: 01/05/1989)");
			return com.opensymphony.xwork2.Action.ERROR;
		}

		try
		{
			this.setPessoaVo(DaoFactory.getInstance().getAcPessoaByMatricula(cdPessoa));
			if ((this.getPessoaVo() == null) || (this.getPessoaVo().getDataNascimento() == null))
			{
				this.addFieldError("nroCadastroPaciente", "* Este Nº de Matricula está Incorreto!!");
				return com.opensymphony.xwork2.Action.ERROR;
			}
			// pessoa localizada, validar a data nascimento. Adicionar +1h nas
			// datas para garantir distorção no horário de verão em algumas
			// regioes.
			final Date dataNascPessoa = DateUtils.addAmountDate(
			    this.getPessoaVo().getDataNascimento(), +1, Calendar.HOUR_OF_DAY);
			final Date dataNascField = DateUtils.addAmountDate(this.getDataNascimento(), +1,
			    Calendar.HOUR_OF_DAY);

			if (dataNascPessoa.compareTo(dataNascField) != 0)
			{
				this.addFieldError("dataNascimento",
				    "Data de Nascimento Incorreta para a Matrícula Informada!!");
				// Dt.Nasc. Matr: " +
				// (DateUtils.formatDateDDMMYYYY(dataNascPessoa) + " Dt.Nasc.
				// Informada: " + DateUtils.formatDate(dataNascField)));
				return com.opensymphony.xwork2.Action.ERROR;
			}

			// Pessoa localizada e data nascimento conferida. pesquisar os
			// laudos.
			this.listarLaudos();
		}
		catch (RuntimeException ex)
		{
			ex.printStackTrace();
			this.addFieldError("nroCadastroPaciente", ex.getMessage());
			return com.opensymphony.xwork2.Action.ERROR;
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			this.addFieldError("nroCadastroPaciente", ex.getMessage());
			return com.opensymphony.xwork2.Action.ERROR;
		}

		return com.opensymphony.xwork2.Action.SUCCESS;
	}

	/**
	 * Monta lista de laudos.
	 * @return
	 */
	private final List<LaudoVO> getLaudos ()
	{
		List<LaudoVO> list = new ArrayList<LaudoVO>();
		LaudoVO l1 = new LaudoVO();
		// l1.setId(1098L);
		// l1.setDescricao("LAUDO DE TONOMETRIA");
		// l1.setDataFinalizacao(Calendar.getInstance().getTime());
		// l1.setFinalizado(true);
		// list.add(l1);
		//
		// LaudoVO l2 = new LaudoVO();
		// l2.setId(3356L);
		// l2.setDescricao("LAUDO DE ACUIDADE VISUAL (PAM)");
		// l2.setDataFinalizacao(Calendar.getInstance().getTime());
		// l2.setFinalizado(true);
		// list.add(l2);
		//
		// LaudoVO l3 = new LaudoVO();
		// l3.setId(1584L);
		// l3.setDescricao("LAUDO DE BIOMETRIA ULTRASSONICA");
		// l3.setDataFinalizacao(Calendar.getInstance().getTime());
		// l3.setFinalizado(false);
		// list.add(l3);
		//
		// LaudoVO l4 = new LaudoVO();
		// l4.setId(1123L);
		// l4.setDescricao("LAUDO DE MICROSCOPIA ESPECULAR DE CORNEA");
		// l4.setDataFinalizacao(Calendar.getInstance().getTime());
		// l4.setFinalizado(false);
		// list.add(l4);
		//
		// LaudoVO L5 = new LaudoVO();
		// L5.setId(7454L);
		// L5.setDescricao("LAUDO DE CERATOSCOPIA COMPUTADORIZADA");
		// L5.setDataFinalizacao(Calendar.getInstance().getTime());
		// L5.setFinalizado(true);
		// list.add(L5);
		//
		// LaudoVO L6 = new LaudoVO();
		// L6.setId(9652L);
		// L6.setDescricao("LAUDO DE MAPEAMENTO DE RETINA");
		// L6.setDataFinalizacao(Calendar.getInstance().getTime());
		// L6.setFinalizado(false);
		// list.add(L6);
		//
		// LaudoVO L7 = new LaudoVO();
		// L7.setId(3625L);
		// L7.setDescricao("LAUDO DE RETINOGRAFIA FLUORESCENTE");
		// L7.setDataFinalizacao(Calendar.getInstance().getTime());
		// L7.setFinalizado(true);
		// list.add(L7);

		// l1 = l2 = l3 = l4 = L5 = L6 = null;
		return list;
	}

	/**
	 * @return the listaLaudos
	 */
	public List<LaudoVO> getListaLaudos ()
	{
		return this.getPessoaVo() != null ? this.getPessoaVo().getListaLaudos() : null;
	}

	/**
	 * @param listaLaudos the listaLaudos to set
	 */
	public void setListaLaudos (List<LaudoVO> listaLaudos)
	{
		if (this.getPessoaVo() != null)
		{
			this.getPessoaVo().setListaLaudos(listaLaudos);
		}
		else
		{
			this.setPessoaVo(new PessoaVO());
			this.getPessoaVo().setListaLaudos(listaLaudos);
		}
	}

	/**
	 * @return the nroCadastroPaciente
	 */
	public Long getNroCadastroPaciente ()
	{
		return nroCadastroPaciente;
	}

	/**
	 * @param nroCadastroPaciente the nroCadastroPaciente to set
	 */
	public void setNroCadastroPaciente (Long nroCadastroPaciente)
	{
		this.nroCadastroPaciente = nroCadastroPaciente;
	}

	/**
	 * @return the dataNascimento
	 */
	public Date getDataNascimento ()
	{
		return dataNascimento;
	}

	/**
	 * @param dataNascimento the dataNascimento to set
	 */
	public void setDataNascimento (Date dataNascimento)
	{
		this.dataNascimento = dataNascimento;
	}

	/**
	 * @return the pessoaVo
	 */
	public PessoaVO getPessoaVo ()
	{
		return pessoaVo;
	}

	/**
	 * @param pessoaVo the pessoaVo to set
	 */
	public void setPessoaVo (PessoaVO pessoaVo)
	{
		this.pessoaVo = pessoaVo;
	}

	/**
	 * @return the nroLaudo
	 */
	public Long getNroLaudo ()
	{
		return nroLaudo;
	}

	/**
	 * @param nroLaudo the nroLaudo to set
	 */
	public void setNroLaudo (Long nroLaudo)
	{
		this.nroLaudo = nroLaudo;
	}

}
