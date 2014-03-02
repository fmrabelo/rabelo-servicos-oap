/**
 * 
 */
package br.com.oappr.infra.action;

import java.util.ArrayList;
import java.util.List;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;

import br.com.oappr.infra.DAO.DaoFactory;
import br.com.oappr.infra.exceptions.OAPInternalException;
import br.com.oappr.infra.report.ReportParameters;
import br.com.oappr.infra.util.Constants;
import br.com.oappr.infra.util.Validator;
import br.com.oappr.intranet.vo.PessoaVO;
import br.com.oappr.intranet.vo.UsuarioWebOapVO;

import com.opensymphony.xwork2.ActionSupport;

/**
 * Classe responsável pela lógica de negócio para cadastro, autenticação e
 * pesquisa de pacientes pelos colaboradores da OAP.
 * @author Rabelo Serviços.
 */
public class LaudoColaboradoresAction
    extends ActionSupport
    implements ReportParameters
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 13102010197665488L;

	// campos para cadastro e autenticação de colaboradores internos da OAP.
	private UsuarioWebOapVO user;
	private String confirmEmailweb;
	private String confirmSenhaweb;

	// pesquisar lista de pacientes por médico/colaborador OAP
	private String nomePaciente;
	private Long codPaciente;
	private List<PessoaVO> listPacientes;

	// mensagem de validação
	final String msg = "Campo Obrigatório não preenchido: ?";

	/**
	 * 
	 */
	@Override
	@Action(value = "initRegisterUser", results = {@Result(location = "/jsp/registerOapUser.jsp", name = "success")})
	public String execute ()
	{
		this.init();
		return SUCCESS;
	}

	/**
	 * Iniciar variáveis.
	 */
	private final void init ()
	{
		user = new UsuarioWebOapVO();
		this.setConfirmEmailweb(null);
		this.setConfirmSenhaweb(null);
	}

	/**
	 * @return
	 */
	@Action(value = "insertColaboradorOap", results = {
	    @Result(location = "/jsp/registerOapUser.jsp", name = "success"),
	    @Result(location = "/jsp/registerOapUser.jsp", name = "error")})
	public String insertColaboradorOap ()
	{
		// validar campos de entrada.
		try
		{
			if (this.isValid())
			{
				DaoFactory.getInstance().insertUsuarioWebOAP(this.getUser());
				this.addActionMessage("Registro cadastrado com sucesso!");
			}
		}
		catch (OAPInternalException ex)
		{
			this.addActionError(ex.getMessage());
			return ERROR;
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			this.addActionError("Não foi possível cadastrar colaborador!");
			return ERROR;
		}
		return SUCCESS;
	}

	/**
	 * @return
	 */
	@Action(value = "loginOap", results = {
	    @Result(location = "/jsp/loginOapUser.jsp", name = "success"),
	    @Result(location = "/jsp/loginOapUser.jsp", name = "error")})
	public String initLogin ()
	{
		this.setUser(new UsuarioWebOapVO());
		setUserSession(null);
		ServletActionContext.getRequest().getSession().invalidate();
		return SUCCESS;
	}

	/**
	 * @return
	 */
	@Action(value = "autenticarColaboradorOap", results = {
	    @Result(location = "/jsp/listaPacientes.jsp", name = "success"),
	    @Result(location = "/jsp/loginOapUser.jsp", name = "error")})
	public String autenticarPaciente ()
	{
		if (this.isValidLogin())
		{
			try
			{
				final UsuarioWebOapVO oapUser = DaoFactory.getInstance().autenticarUsuarioWebOAP(
				    this.getUser());
				if (oapUser != null)
				{
					this.setUser(oapUser);
					setUserSession(oapUser);
					this.initListarPacientes();
					return SUCCESS;
				}
				else
				{
					this.addActionError("Colaborador não autenticado!");
				}
			}
			catch (OAPInternalException e)
			{
				this.addActionError(e.getMessage());
			}
			catch (Exception e)
			{
				this.addActionError("Colaborador não autenticado!");
				e.printStackTrace();
			}
		}
		return ERROR;
	}

	/**
	 * Inicializa o fluxo para Listar pacientes pelo nome ou código.
	 * @return String
	 */
	@Action(value = "initListarPacientes", results = {@Result(location = "/jsp/listaPacientes.jsp", name = "success")})
	public String initListarPacientes ()
	{
		this.setListPacientes(null);
		this.setCodPaciente(null);
		this.setNomePaciente(null);
		return SUCCESS;
	}

	/**
	 * Listar pacientes pelo nome ou código.
	 * @return String
	 */
	@Action(value = "listarPacientes", results = {
	    @Result(location = "/jsp/listaPacientes.jsp", name = "success"),
	    @Result(location = "/jsp/listaPacientes.jsp", name = "error"),
	    @Result(location = "/jsp/loginOapUser.jsp", name = "login")})
	public String listarPacientes ()
	{
		String msg = "Nenhum Paciente Localizado.";
		try
		{
			// validar se usuario logado.
			if (this.getUser() == null)
			{
				this.initLogin();
				this.addActionError("Por motivos de segurança o seu tempo de conexão expirou, favor efetuar novo Login.");
				return "login";
			}
			this.setListPacientes(null);
			if ((this.getCodPaciente() != null) && (this.getCodPaciente() > 0))
			{
				this.setListPacientes(new ArrayList<PessoaVO>());
				this.getListPacientes().add(
				    DaoFactory.getInstance().getPacienteByCodMatricula(this.getCodPaciente()));
			}
			else if (!Validator.isBlankOrNull(this.getNomePaciente()))
			{
				if (this.getNomePaciente().trim().length() < 4)
				{
					msg = "Nome do Paciente deve ser preenchido com no mínimo 4 caracteres alfabéticos.";
					this.addActionError(msg);
					return ERROR;
				}
				else
				{
					this.setListPacientes(DaoFactory.getInstance().getPessoaByName(
					    this.getNomePaciente()));
				}
			}
			if ((this.getListPacientes() != null) && (this.getListPacientes().size() == 1)
			    && (this.getListPacientes().get(0).getCdPessoa() == null))
			{
				this.setListPacientes(new ArrayList<PessoaVO>());
			}
			if (!Validator.notEmptyCollection(this.getListPacientes()))
			{
				this.addActionMessage(msg);
			}
		}
		catch (OAPInternalException e)
		{
			this.addActionError(e.getMessage());
		}
		catch (Exception e)
		{
			this.addActionError(msg);
			e.printStackTrace();
		}
		return SUCCESS;
	}

	/**
	 * @return the user
	 */
	public UsuarioWebOapVO getUser ()
	{
		if (user == null)
		{
			user = getUserSession();
		}
		return user;
	}

	/**
	 * @param user the user to set
	 */
	public void setUser (UsuarioWebOapVO user)
	{
		this.user = user;
	}

	/**
	 * @return the confirmEmailweb
	 */
	public String getConfirmEmailweb ()
	{
		return confirmEmailweb;
	}

	/**
	 * @param confirmEmailweb the confirmEmailweb to set
	 */
	public void setConfirmEmailweb (String confirmEmailweb)
	{
		this.confirmEmailweb = confirmEmailweb;
	}

	/**
	 * @return the confirmSenhaweb
	 */
	public String getConfirmSenhaweb ()
	{
		return confirmSenhaweb;
	}

	/**
	 * @param confirmSenhaweb the confirmSenhaweb to set
	 */
	public void setConfirmSenhaweb (String confirmSenhaweb)
	{
		this.confirmSenhaweb = confirmSenhaweb;
	}

	/**
	 * @return
	 */
	private final boolean isValidLogin ()
	{
		if ((this.getUser().getNrUsuario() == null) || (this.getUser().getNrUsuario() < 1))
		{
			this.addFieldError("nrusuario", msg.replace("?", "Número do Usuário"));
			return false;
		}
		if (Validator.isBlankOrNull(this.getUser().getSenhaweb()))
		{
			this.addFieldError("password", msg.replace("?", "password"));
			return false;
		}
		return true;
	}

	/**
	 * @return
	 */
	private final boolean isValid ()
	{
		if (!this.isValidLogin())
		{
			return false;
		}
		if (Validator.isBlankOrNull(this.getUser().getEmailweb()))
		{
			this.addFieldError("email", msg.replace("?", "Email"));
			return false;
		}
		else if (!Validator.isEmail(this.getUser().getEmailweb()))
		{
			this.addFieldError("email", "Email inválido.");
			return false;
		}
		if (!this.getUser().getEmailweb().equals(this.getConfirmEmailweb()))
		{
			this.addFieldError("email", "O Email e o Email de Confirmação estão diferentes.");
			return false;
		}
		if (this.getUser().getSenhaweb().length() < Constants.PASSWORD_MIN_LENGTH)
		{
			this.addFieldError("password", "Senha deve conter no mínimo "
			    + Constants.PASSWORD_MIN_LENGTH + " caracteres!");
			return false;
		}
		if (Validator.isBlankOrNull(this.getConfirmSenhaweb()))
		{
			this.addFieldError("password", msg.replace("?", "Confirm Password"));
			return false;
		}
		if (!this.getUser().getSenhaweb().equals(this.getConfirmSenhaweb()))
		{
			this.addFieldError("password", "A senha e a Confirmação de senha não correspondem!");
			return false;
		}
		return true;
	}

	/**
	 * Insere usuario oap na sessao http.
	 * @param user
	 */
	private final static void setUserSession (final UsuarioWebOapVO user)
	{
		ServletActionContext.getRequest().getSession(true).setAttribute(Constants.SESSION_USER_OAP,
		    user);
	}

	/**
	 * Insere usuario oap na sessao http.
	 * @param user
	 */
	public final static UsuarioWebOapVO getUserSession ()
	{
		return (UsuarioWebOapVO)ServletActionContext.getRequest().getSession().getAttribute(
		    Constants.SESSION_USER_OAP);
	}

	/**
	 * @return the listPacientes
	 */
	public List<PessoaVO> getListPacientes ()
	{
		return listPacientes;
	}

	/**
	 * @param listPacientes the listPacientes to set
	 */
	public void setListPacientes (List<PessoaVO> listPacientes)
	{
		this.listPacientes = listPacientes;
	}

	/**
	 * @return the nomePaciente
	 */
	public String getNomePaciente ()
	{
		return nomePaciente;
	}

	/**
	 * @param nomePaciente the nomePaciente to set
	 */
	public void setNomePaciente (String nomePaciente)
	{
		this.nomePaciente = nomePaciente;
	}

	/**
	 * @return the codPaciente
	 */
	public Long getCodPaciente ()
	{
		return codPaciente;
	}

	/**
	 * @param codPaciente the codPaciente to set
	 */
	public void setCodPaciente (Long codPaciente)
	{
		this.codPaciente = codPaciente;
	}

}
