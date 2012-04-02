/**
 * 
 */
package br.com.oappr.infra.action;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.sql.Blob;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;

import br.com.oappr.infra.DAO.DaoFactory;
import br.com.oappr.infra.report.GenericReport;
import br.com.oappr.infra.report.ReportParameters;
import br.com.oappr.infra.report.laudo.LaudoReport;
import br.com.oappr.infra.util.DateUtils;
import br.com.oappr.intranet.vo.LaudoVO;
import br.com.oappr.intranet.vo.PessoaVO;

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
    implements ReportParameters
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 13102010197645454L;

	private PessoaVO pessoaVo;

	// parametros para jasper report
	HashMap<String, String> parameters = new HashMap<String, String>();

	// numero do cadastro do paciente
	private Long nroCadastroPaciente;
	private Date dataNascimento;
	// numero de laudo especifico.
	private Long nroLaudo;

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
	public void gerarRelPdf (String caminhoRel) throws Exception
	{
		try
		{
			// passar o rtf como relatorio....
			// caminhoRel = "C:\\downloads\\text_375688.rtf";

			// InputStream relatorio =
			// this.getClass().getResourceAsStream(caminhoRel);
			// HashMap<String, Object> parametros = new HashMap<String,
			// Object>();
			// parametros.put("LOGO", caminhoLogo);
			// JRBeanCollectionDataSource colection = new
			// JRBeanCollectionDataSource(lista);
			JREmptyDataSource colection = new JREmptyDataSource();
			JasperPrint impressao = JasperFillManager.fillReport(caminhoRel, parameters, colection);
			// JasperPrint impressao = JasperFillManager.fillReport(caminhoRel,
			// parameters);
			JasperExportManager.exportReportToPdf(impressao);

			// JasperReport report = JasperCompileManager.compileReport(jrxml);
			// JasperPrint impressao = JasperFillManager.fillReport(report,
			// parameters);

			// final JasperViewer jv = new JasperViewer(impressao, false);
			// jv.setVisible(true);
			// jv.setModalExclusionType(Dialog.ModalExclusionType.APPLICATION_EXCLUDE);

		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			// JOptionPane.showMessageDialog(null, "Não foi possivel abrir o
			// relatório");
		}
	}

	/**
	 * Gerar relatorio pdf.
	 * @return
	 */
	public String gerarLaudoPdf ()
	{
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

			final HttpServletRequest request = ServletActionContext.getRequest();
			final HttpServletResponse response = ServletActionContext.getResponse();

			String id = request.getParameter("id");
			if (id == null)
			{
				id = "114863";
			}

			// recuperar laudo pelo id
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
			//
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

			// Parametros do cabeçalho
			parameters.put(PACIENTE, "PACIENTE : " + this.getPessoaVo().getNomePessoa());
			parameters.put(DESCR_MEDICO, "A/C : " + " DRA MARIA ISABEL BORA CASTALDO ANDRADE ");

			// Parametros para responsável pelo Exame
			parameters.put(MEDICO_RESPONSAVEL_EXAME,
			    "Exame realizado pela Dra Maria Isabel Bora Castaldo Andrade ");
			parameters.put(CRM_MEDICO_RESPONSAVEL_EXAME, "CRM-14831 PR");
			parameters.put(CIDADE_DATA_EXAME, "Curitiba, 01 de setembro de 2011");

			// TODO: ADICIONAR parametros FOOTER
			// ..............

			// Parametro Nome do Laudo
			parameters.put(NOME_LAUDO, this.getListaLaudos().get(0).getDsexamecompl());
			this.setParameters(parameters);
			//

			final String fileName = "TONOMETRIA";
			final LaudoReport report = new LaudoReport();
			report.viewReport(request, response, fileName, null, parameters, GenericReport.PDF_TYPE);

			// String jasper =
			// request.getSession().getServletContext().getRealPath(
			// new
			// StringBuilder(File.separator).append("reports").append(File.separator).append(
			// "TONOMETRIA").append(".jasper").toString());

			// abrir relatorio
			// JasperCompileManager.compileReportToFile(jrxml, jasper);

			// this.gerarRelPdf(jrxml);

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
			this.setPessoaVo(DaoFactory.getInstance().getAcPacienteByMatricula(cdPessoa));
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
