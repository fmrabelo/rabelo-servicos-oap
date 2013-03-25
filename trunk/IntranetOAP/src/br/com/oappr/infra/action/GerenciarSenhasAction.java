/**
 * 
 */
package br.com.oappr.infra.action;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;

import br.com.oappr.infra.DAO.DaoFactory;
import br.com.oappr.infra.exceptions.OAPInternalException;
import br.com.oappr.infra.util.Constants;
import br.com.oappr.infra.util.Validator;
import br.com.oappr.intranet.vo.UsuarioWebOapVO;

import com.opensymphony.xwork2.ActionSupport;

/**
 * Classe respons�vel pela l�gica de neg�cio para Gerenciar as senhas de
 * usu�rios cadastrados na web.
 * @author Rabelo Servi�os.
 */
public class GerenciarSenhasAction
    extends ActionSupport
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 131023565122768418L;

	// campos para confirma��o de dados.
	private UsuarioWebOapVO user;
	private String confirmEmailweb;
	private String confirmSenhaweb;
	private String reConfirmSenhaweb;

	// mensagem de valida��o
	final String msg = "Campo Obrigat�rio n�o preenchido: ?";

	/**
	 * Entrada para altera��o de senha corrente.
	 */
	@Override
	@Action(value = "initAlterarSenha", results = {@Result(location = "/jsp/alterarSenha.jsp", name = "success")})
	public String execute ()
	{
		this.init();
		this.setReConfirmSenhaweb(null);
		return SUCCESS;
	}

	/**
	 * Entrada para lembrar de senha esquecida.
	 */
	@Action(value = "initLembrarSenha", results = {@Result(location = "/jsp/lembrarSenha.jsp", name = "success")})
	public String executeLembrarSenha ()
	{
		this.init();
		return SUCCESS;
	}

	/**
	 * Iniciar vari�veis.
	 */
	private final void init ()
	{
		user = new UsuarioWebOapVO();
		this.setConfirmSenhaweb(null);
		this.setConfirmEmailweb(null);
	}

	/**
	 * @return
	 */
	public UsuarioWebOapVO autenticar ()
	{
		if (this.isValidLogin())
		{
			try
			{
				return DaoFactory.getInstance().autenticarUsuarioWebOAP(this.getUser());

			}
			catch (OAPInternalException e)
			{
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * @return
	 */
	@Action(value = "alterarSenha", results = {
	    @Result(location = "/jsp/alterarSenha.jsp", name = "success"),
	    @Result(location = "/jsp/alterarSenha.jsp", name = "error")})
	public String alterarSenha ()
	{
		// validar campos de entrada.
		try
		{
			if (this.isValidAlterarSenha())
			{
				// DaoFactory.getInstance().insertUsuarioWebOAP(this.getUser());
				this.addActionMessage("Senha alterada com sucesso!");
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
			this.addActionError("N�o foi poss�vel alterar a senha!");
			return ERROR;
		}
		return SUCCESS;
	}

	/**
	 * @return
	 */
	@Action(value = "lembrarSenha", results = {
	    @Result(location = "/jsp/lembrarSenha.jsp", name = "success"),
	    @Result(location = "/jsp/lembrarSenha.jsp", name = "error")})
	public String lembrarSenha ()
	{
		// validar campos de entrada.
		try
		{
			if (this.isValid())
			{
				// DaoFactory.getInstance().insertUsuarioWebOAP(this.getUser());
				this.addActionMessage("Uma nova senha foi gerada e enviada para o email cadastrado para "
				    + this.getUser().getNmUsuario());
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
			this.addActionError("N�o foi poss�vel gerar nova senha!");
			return ERROR;
		}
		return SUCCESS;
	}

	/**
	 * @return the user
	 */
	public UsuarioWebOapVO getUser ()
	{
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

	public String getReConfirmSenhaweb ()
	{
		return reConfirmSenhaweb;
	}

	public void setReConfirmSenhaweb (String reConfirmSenhaweb)
	{
		this.reConfirmSenhaweb = reConfirmSenhaweb;
	}

	/**
	 * @return
	 */
	private final boolean isValidLogin ()
	{
		if ((this.getUser().getNrUsuario() == null) || (this.getUser().getNrUsuario() < 1))
		{
			this.addFieldError("nrusuario", msg.replace("?", "N�mero do Usu�rio"));
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
			this.addFieldError("email", "Email inv�lido.");
			return false;
		}
		if (!this.getUser().getEmailweb().equals(this.getConfirmEmailweb()))
		{
			this.addFieldError("email", "O Email e o Email de Confirma��o est�o diferentes.");
			return false;
		}
		if (this.getUser().getSenhaweb().length() < Constants.PASSWORD_MIN_LENGTH)
		{
			this.addFieldError("password", "Senha deve conter no m�nimo "
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
			this.addFieldError("password", "A senha e a Confirma��o de senha n�o correspondem!");
			return false;
		}
		return true;
	}

	/**
	 * @return
	 */
	private final boolean isValidAlterarSenha ()
	{
		// validar usuario e senha
		if (!this.isValidLogin())
		{
			return false;
		}
		// validar nova senha e nova senha.
		if (Validator.isBlankOrNull(this.getConfirmSenhaweb()))
		{
			this.addFieldError("password", msg.replace("?", "nova senha"));
			return false;
		}
		if (Validator.isBlankOrNull(this.getReConfirmSenhaweb()))
		{
			this.addFieldError("password", msg.replace("?", "Confirma��o de senha"));
			return false;
		}
		if (!this.getReConfirmSenhaweb().equals(this.getConfirmSenhaweb()))
		{
			this.addFieldError("password", "A senha e a Confirma��o de senha n�o correspondem!");
			return false;
		}
		if (this.getConfirmSenhaweb().length() < Constants.PASSWORD_MIN_LENGTH)
		{
			this.addFieldError("password", "Nova senha deve conter no m�nimo "
			    + Constants.PASSWORD_MIN_LENGTH + " caracteres!");
			return false;
		}
		// validar se usu�rio valido.
		user = this.autenticar();
		if (user == null)
		{
			this.addActionError("Usu�rio inexistente ou n�o autenticado!");
			return false;
		}
		return true;
	}
}
