package br.com.laserviewpr.infra.util.email;

import java.io.Serializable;

/**
 * <ul>
 * A classe EmailException disponibiliza m�todos respons�veis por gerar uma
 * exce��o referente a email.
 * </ul>
 * @author Desenvolvimento
 */
public final class EmailException
    extends Exception
    implements Serializable
{

	/**
	 * N�mero serial da classe.
	 */
	private static final long serialVersionUID = -5517799350867844155L;

	/**
	 * Construtor da classe EmailException sem parametro.
	 */
	public EmailException ()
	{
		super();
	}

	/**
	 * Construtor da classe EmailException com o parametro msg
	 * @param msg String Mensagem para a exce��o
	 */
	public EmailException (String msg)
	{
		super(msg);
	}

	/**
	 * @param exception
	 */
	public EmailException (Exception exception)
	{
		super(exception);
	}

	/**
	 * @param exception
	 * @param msg
	 */
	public EmailException (Exception exception, String msg)
	{
		super(msg, exception);
	}
}