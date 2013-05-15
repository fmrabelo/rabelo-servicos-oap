/**
 * 
 */
package br.com.oappr.infra.action;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;

import br.com.oappr.infra.DAO.DaoFactory;
import br.com.oappr.infra.report.ReportParameters;
import br.com.oappr.infra.util.DateUtils;
import br.com.oappr.infra.util.Validator;
import br.com.oappr.intranet.vo.AgendaMedicaVO;
import br.com.oappr.intranet.vo.UsuarioWebOapVO;

import com.opensymphony.xwork2.ActionSupport;

/**
 * Classe responsável pela lógica de negócio da Agenda Médica.
 * @author Rabelo Serviços.
 */
public class AgendaMedicaAction
    extends ActionSupport
    implements ReportParameters
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 13102010197615003L;

	// numero do usuário/medico
	private Long nroUsuario;
	private Date dataAgenda;
	private List<AgendaMedicaVO> listaAgenda;

	/**
	 * 
	 */
	@Override
	@Action(value = "agendaMedica", results = {@Result(location = "/jsp/agendaMedica.jsp", name = "success")})
	public String execute ()
	{
		this.setDataAgenda(Calendar.getInstance().getTime());
		this.listarLaudos();
		return SUCCESS;
	}

	/**
	 * Método util na pesquisa da Agenda.<br>
	 */
	@Action(value = "listarAgenda", results = {
	    @Result(location = "/jsp/agendaMedica.jsp", name = "success"),
	    @Result(location = "/jsp/agendaMedica.jsp", name = "error")})
	public String listarLaudos ()
	{
		try
		{
			if ((this.getUserSessionOAP() != null)
			    && (this.getUserSessionOAP().getNrUsuario() != null)
			    && (this.getUserSessionOAP().getNrUsuario() > 0))
			{
				if (this.getDataAgenda() != null)
				{
					final Long nroUsr = this.getUserSessionOAP().getNrUsuario();
					this.setListaAgenda(DaoFactory.getInstance().getAgendaMedica(nroUsr,
					    DateUtils.formatDateDDMMYYYY(this.getDataAgenda())));

					if (!Validator.notEmptyCollection(this.getListaAgenda()))
					{
						// final List<AgendaMedicaVO> lista = new
						// ArrayList<AgendaMedicaVO>();
						// lista.add(new AgendaMedicaVO());
						// this.setListaAgenda(lista);
						this.addFieldError("", "Nenhuma Agenda localizada para o dia "
						    + DateUtils.formatDateDDMMYYYY(this.getDataAgenda()));
						return ERROR;
					}
				}
				else
				{
					this.addFieldError("", "Deve ser fornecida uma data válida para a Agenda!!");
					return ERROR;
				}
			}
			else
			{
				ServletActionContext.getResponse().sendRedirect("loginOap.action");
			}

		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
		return SUCCESS;
	}

	/**
	 * Método util na pesquisa da Agenda.<br>
	 */
	@Action(value = "proximoDia", results = {
	    @Result(location = "/jsp/agendaMedica.jsp", name = "success"),
	    @Result(location = "/jsp/agendaMedica.jsp", name = "error")})
	public String proximoDia ()
	{
		if (this.getDataAgenda() != null)
		{
			this.setDataAgenda(DateUtils.addDiasDate(this.getDataAgenda(), +1));
		}
		return this.listarLaudos();
	}

	/**
	 * Método util na pesquisa da Agenda.<br>
	 */
	@Action(value = "diaAnterior", results = {
	    @Result(location = "/jsp/agendaMedica.jsp", name = "success"),
	    @Result(location = "/jsp/agendaMedica.jsp", name = "error")})
	public String diaAnterior ()
	{
		if (this.getDataAgenda() != null)
		{
			this.setDataAgenda(DateUtils.addDiasDate(this.getDataAgenda(), -1));
		}
		return this.listarLaudos();
	}

	/**
	 * @return the nroUsuario
	 */
	public Long getNroUsuario ()
	{
		return nroUsuario;
	}

	/**
	 * @param nroUsuario the nroUsuario to set
	 */
	public void setNroUsuario (Long nroUsuario)
	{
		this.nroUsuario = nroUsuario;
	}

	/**
	 * @return the dataAgenda
	 */
	public Date getDataAgenda ()
	{
		return dataAgenda;
	}

	/**
	 * @param dataAgenda the dataAgenda to set
	 */
	public void setDataAgenda (Date dataAgenda)
	{
		this.dataAgenda = dataAgenda;
	}

	public List<AgendaMedicaVO> getListaAgenda ()
	{
		return listaAgenda;
	}

	public void setListaAgenda (List<AgendaMedicaVO> listaAgenda)
	{
		this.listaAgenda = listaAgenda;
	}

	/**
	 * Retorna usuario logado.
	 * @return
	 */
	public UsuarioWebOapVO getUserSessionOAP ()
	{
		return LaudoColaboradoresAction.getUserSession();
	}
}
