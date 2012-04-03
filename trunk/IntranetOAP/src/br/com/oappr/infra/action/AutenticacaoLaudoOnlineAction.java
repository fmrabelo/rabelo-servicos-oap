/**
 * 
 */
package br.com.oappr.infra.action;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;

import br.com.oappr.infra.DAO.DaoFactory;
import br.com.oappr.infra.report.ReportParameters;
import br.com.oappr.infra.util.DateUtils;
import br.com.oappr.infra.util.Validator;
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

	// numero do cadastro do paciente
	private Long nroCadastroPaciente;
	private Date dataNascimento;
	// numero de laudo especifico.
	private Long nroLaudo;

	/**
	 * 
	 */
	@Override
	@Action(value = "autenticacaoLaudoOnline", results = {@Result(location = "/jsp/autenticacaoLaudoOnline.jsp", name = "success")})
	public String execute ()
	{
		this.setDataNascimento(Calendar.getInstance().getTime());
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
			    null, null));
			if (!Validator.notEmptyCollection(this.getListaLaudos()))
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
			this.setPessoaVo(DaoFactory.getInstance().getPacienteByMatricula(cdPessoa));
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
		if (this.getPessoaVo() == null)
		{
			this.setPessoaVo(new PessoaVO());
		}
		this.getPessoaVo().setListaLaudos(listaLaudos);
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
