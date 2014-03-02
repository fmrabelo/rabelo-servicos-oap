/**
 * 
 */
package br.com.laserviewpr.infra.action;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;

import br.com.laserviewpr.infra.DAO.DaoFactory;
import br.com.laserviewpr.infra.exceptions.InternalException;
import br.com.laserviewpr.infra.util.Constants;
import br.com.laserviewpr.infra.util.GenericUtils;
import br.com.laserviewpr.infra.util.Validator;
import br.com.laserviewpr.infra.util.email.Email;
import br.com.laserviewpr.infra.util.email.googleSimples.SimpleGoogleMail;
import br.com.laserviewpr.intranet.vo.UsuarioInternoVO;

import com.opensymphony.xwork2.ActionSupport;

/**
 * Classe responsável pela lógica de negócio para Gerenciar as senhas de
 * usuários cadastrados na web.
 * @author Rabelo Serviços.
 */
public class GerenciarSenhasAction
    extends ActionSupport
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 131023565122768418L;

	// campos para confirmação de dados.
	private UsuarioInternoVO user;
	private String confirmEmailweb;
	private String confirmSenhaweb;
	private String reConfirmSenhaweb;
	private String dataNascimento;

	// mensagem de validação
	final String msg = "Campo Obrigatório não preenchido: ?";

	/**
	 * Entrada para alteração de senha corrente.
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
	 * Iniciar variáveis.
	 */
	private final void init ()
	{
		user = new UsuarioInternoVO();
		this.setConfirmSenhaweb(null);
		this.setConfirmEmailweb(null);
	}

	/**
	 * @return
	 */
	public UsuarioInternoVO autenticar ()
	{
		if (this.isValidLogin())
		{
			try
			{
				return DaoFactory.getInstance().autenticarUsuarioWeb(this.getUser());
			}
			catch (InternalException e)
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
				this.getUser().setSenhaweb(this.getConfirmSenhaweb());
				DaoFactory.getInstance().alterarSenhaUsuarioWeb(this.getUser());
				this.addActionMessage("OK. Senha alterada com sucesso!");
			}
		}
		catch (InternalException ex)
		{
			this.addActionError(ex.getMessage());
			return ERROR;
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			this.addActionError("Não foi possível alterar a senha!");
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
			if (this.isValidLembrarSenha())
			{
				// setar nova senha.
				final String senha = GenericUtils.getRandomPwd();
				this.getUser().setSenhaweb(senha);
				DaoFactory.getInstance().alterarSenhaUsuarioWeb(this.getUser());
				// send mail
				final StringBuilder conteudoEmail = new StringBuilder(
				    "Foi solicitado uma nova senha para o usuário Interno ");
				conteudoEmail.append(this.getUser().getNrUsuario());
				conteudoEmail.append("\nEssa senha é gerada aleatoriamente pelo sistema; \nrecomendamos que a altere por uma de mais fácil memorização.");
				conteudoEmail.append("\n\nGuarde a Nova Senha:    ").append(senha).append("\n\n");
				conteudoEmail.append(Email.conteudoEmailRestrito);
				final String emailDestinatario = this.getUser().getEmailweb();
				// final CreateEmail email = new CreateEmail();
				// email.createMail("Email automatico ", conteudoEmail,
				// null, emailDestinatario,
				// null);

				SimpleGoogleMail.sendMailViaGoogle(emailDestinatario, conteudoEmail.toString());

				System.out.println("[ok] email com nova senha enviado com sucesso para "
				    + emailDestinatario);
				this.addActionMessage("Uma nova senha foi gerada pelo sistema e enviada para o email cadastrado: "
				    + this.getUser().getEmailweb());
			}
		}
		catch (InternalException ex)
		{
			this.addActionError(ex.getMessage());
			return ERROR;
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			this.addActionError("Não foi possível gerar nova senha!");
			return ERROR;
		}
		return SUCCESS;
	}

	/**
	 * @return the user
	 */
	public UsuarioInternoVO getUser ()
	{
		return user;
	}

	/**
	 * @param user the user to set
	 */
	public void setUser (UsuarioInternoVO user)
	{
		user = user;
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

	public String getDataNascimento ()
	{
		return dataNascimento;
	}

	public void setDataNascimento (String dataNascimento)
	{
		this.dataNascimento = dataNascimento;
	}

	/**
	 * @return
	 */
	private final boolean isValidLogin ()
	{
		if (this.getUser() == null)
		{
			this.addFieldError("nrusuario", msg.replace("?", "Todos os campos são obrigatórios"));
		}
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
	 * @throws Exception
	 */
	private final boolean isValidLembrarSenha () throws Exception
	{
		this.getUser().setSenhaweb("xxxxxx");// fack test only.
		if (!this.isValidLogin())
		{
			return false;
		}
		// TODO: USUARIO WEB NAO POSSUI CAMPO NA TABELA PARA A DATA DE
		// NASCIMENTO

		// if (Validator.isBlankOrNull(this.getDataNascimento()))
		// {
		// this.addFieldError("dataNascimento", "Data de Nascimento é campo
		// obrigatório!");
		// return false;
		// }
		// if (!Validator.isDate(DateUtils.parseDate(this.getDataNascimento())))
		// {
		// this.addFieldError("dataNascimento",
		// "Data de Nascimento não é uma data válida (Ex: 01/01/2010) ");
		// return false;
		// }
		if (Validator.isBlankOrNull(this.getUser().getEmailweb()))
		{
			this.addFieldError("email", msg.replace("?", "Email"));
			return false;
		}
		// validar se existe usuário cadastrado com código informado.
		final UsuarioInternoVO user = DaoFactory.getInstance().findUsuarioWebById(
		    this.getUser().getNrUsuario());
		if ((user == null) || (user.getNrUsuario() == null))
		{
			this.addActionError("Usuário " + this.getUser().getNrUsuario()
			    + " não existe ou não está cadastrado no sistema WEB. ");
			return false;
		}
		else
		{
			if (Validator.isBlankOrNull(user.getEmailweb())
			    || !Validator.isEmail(user.getEmailweb()))
			{
				this.addFieldError(
				    "email",
				    "Atenção: Este usuário ainda não possui um email válido cadastrado no sistema. Favor cadastrador o email ou solicitar para alguém da Empresa");
				return false;
			}
			else if (!user.getEmailweb().equalsIgnoreCase(this.getUser().getEmailweb()))
			{
				this.addFieldError("email",
				    "O email informado não corresponde ao email cadastrado no sistema!");
				return false;
			}

			// TODO: USUARIO WEB NAO POSSUI CAMPO NA TABELA PARA A DATA DE
			// NASCIMENTO

			// final Date dataNascUser =
			// DateUtils.addAmountDate(user.getDataNascimento(), +1,
			// Calendar.HOUR_OF_DAY);
			// final Date dataNascField = DateUtils.addAmountDate(
			// DateUtils.parseDate(this.getDataNascimento()), +1,
			// Calendar.HOUR_OF_DAY);
			//
			// if (dataNascUser.compareTo(dataNascField) != 0)
			// {
			// this.addFieldError("dataNascimento",
			// "Data de Nascimento não corresponde a data cadastrado no
			// sistema!");
			// return false;
			// }
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
		// validar confirmação da nova senha.
		if (Validator.isBlankOrNull(this.getConfirmSenhaweb()))
		{
			this.addFieldError("password", msg.replace("?", "nova senha"));
			return false;
		}
		if (Validator.isBlankOrNull(this.getReConfirmSenhaweb()))
		{
			this.addFieldError("password", msg.replace("?", "Confirmação da nova senha"));
			return false;
		}
		if (!this.getReConfirmSenhaweb().equals(this.getConfirmSenhaweb()))
		{
			this.addFieldError("password",
			    "A nova senha e a Confirmação da nova senha não correspondem!");
			return false;
		}
		if (this.getConfirmSenhaweb().length() < Constants.PASSWORD_MIN_LENGTH)
		{
			this.addFieldError("password", "Nova senha deve conter no mínimo "
			    + Constants.PASSWORD_MIN_LENGTH + " caracteres!");
			return false;
		}
		// validar se senha atual é iqual a nova senha.
		if (this.getUser().getSenhaweb().equals(this.getConfirmSenhaweb()))
		{
			this.addFieldError("password", "A nova senha e a atual são iguais!");
			return false;
		}
		// validar se usuário valido.
		user = this.autenticar();
		if (user == null)
		{
			this.addActionError("Usuário inexistente ou não autenticado!");
			return false;
		}
		return true;
	}
}
