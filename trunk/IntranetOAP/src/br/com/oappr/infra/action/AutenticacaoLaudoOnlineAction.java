/**
 * 
 */
package br.com.oappr.infra.action;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;

import br.com.oappr.infra.DAO.DaoFactory;
import br.com.oappr.infra.util.DateUtils;
import br.com.oappr.intranet.vo.LaudoVO;
import br.com.oappr.intranet.vo.PessoaVO;

import com.opensymphony.xwork2.ActionSupport;

/**
 * @author rabelo
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
	HashMap<String, String> parameters = new HashMap<String, String>();

	// numero do cadastro do paciente
	private Long nroCadastroPaciente;
	private Date dataNascimento;

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

	@Override
	@Action(value = "autenticacaoLaudoOnline", results = {@Result(location = "/jsp/autenticacaoLaudoOnline.jsp", name = "success")})
	public String execute ()
	{
		System.out.println("Executando a lógica com Struts2");
		this.setDataNascimento(Calendar.getInstance().getTime());
		// return "ok";
		return com.opensymphony.xwork2.Action.SUCCESS;
	}

	@Action(value = "listarLaudos", results = {@Result(location = "/jsp/listaLaudos.jsp", name = "success")})
	public String listarLaudos () throws Exception
	{
		// pesquisar laudos
		try
		{
			this.setListaLaudos(DaoFactory.getInstance().getLaudos(this.getNroCadastroPaciente()));
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
	 * Gerar relatorio pdf.
	 * @return
	 */
	public String gerarLaudoPdf ()
	{
		System.out.println("PDF laudos...");

		// HttpServletRequest request = ServletActionContext.getRequest();
		// String imgPath = "";
		//
		// if (request != null)
		// {
		// imgPath = request.getSession().getServletContext().getRealPath(
		// new
		// StringBuilder(File.separator).append("logo").append(File.separator).append(
		// "logo2.jpg").toString());
		// }
		// String id = request.getParameter("id");

		// recuperar laudo pelo id

		// this.setListaLaudos(this.getLaudos());
		// if ((listaLaudos != null) && !listaLaudos.isEmpty())
		// {
		// for (final LaudoVO v : listaLaudos)
		// {
		// if (v.getCdpessoa().longValue() == new Long(id).longValue())
		// {
		// laudo = v.getDescricao();
		// break;
		// }
		// }
		// }
		//
		// parameters.put("LOGO_PATH", imgPath);
		// parameters.put("PACIENTE", "PACIENTE : " + paciente);
		// parameters.put("DESCR_MEDICO", "A/C : " + medico);
		// parameters.put("DESCR_LAUDO", laudo);
		//
		// this.setParameters(parameters);
		// try
		// {
		// if ((listaLaudos != null) && !listaLaudos.isEmpty())
		// {
		// System.out.println("Lista Laudos OK. " + listaLaudos.size());
		// }
		// else
		// {
		// this.listarLaudos();
		// }
		// //
		// JasperCompileManager.compileReportToFile("/reports/TONOMETRIA.jrxml",
		// // "/reports/TONOMETRIA.jasper");
		// }
		// catch (Exception e)
		// {
		// e.printStackTrace();
		// return com.opensymphony.xwork2.Action.ERROR;
		// }

		return com.opensymphony.xwork2.Action.SUCCESS;
	}

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
			// pessoa localizada, validar a data nascimento.
			if (this.getPessoaVo().getDataNascimento().compareTo(this.getDataNascimento()) == 0)
			{
				this.addFieldError(
				    "dataNascimento",
				    "Data de Nascimento Incorreta para a Matrícula Informada!! Dt.Nasc. Matr: "
				        + (DateUtils.formatDateDDMMYYYY(this.getPessoaVo().getDataNascimento())
				            + " Dt.Nasc. Informada: " + DateUtils.formatDate(this.getDataNascimento())));
				return com.opensymphony.xwork2.Action.ERROR;
			}

			System.out.println("pessoa.dtnasc: " + this.getPessoaVo().getDataNascimento());
			System.out.println("dtnasc field: " + this.getDataNascimento());

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

}
