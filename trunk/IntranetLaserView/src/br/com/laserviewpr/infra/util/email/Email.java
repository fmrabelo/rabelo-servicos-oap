package br.com.laserviewpr.infra.util.email;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import br.com.laserviewpr.infra.util.email.attachment.InputStreamDataSource;

/**
 * <ul>
 * A classe e-mail � respons�vel por setar os atributos de e-mail.
 * </ul>
 * @author Desenvolvimento
 */

public final class Email
    implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 197112545455L;

	// mensagem automatica de envio de email.
	public static final String conteudoEmailRestrito = "Esta � uma mensagem autom�tica, por favor n�o responda a este remetente.\n Se voc� n�o sabe do que se trata este email, favor entrar em contato com o departamento de TI da Empresa.\n\n";
	/**
	 * Buffer que cont�m o email de quem est� enviando
	 */
	private final StringBuffer from = new StringBuffer();
	/**
	 * Buffer que cont�m o(s) email(s) de quem est� recebendo
	 */
	private final StringBuffer to = new StringBuffer();
	/**
	 * Buffer que cont�m o(s) email(s) de quem est� recebendo
	 */
	private StringBuffer cc = new StringBuffer();
	/**
	 * Buffer que cont�m o(s) email(s) de quem est� recebendo
	 */
	private StringBuffer bcc = new StringBuffer();
	/**
	 * Buffer que cont�m o assunto do email
	 */
	private final StringBuffer subject = new StringBuffer();
	/**
	 * Buffer que cont�m o corpo da mensagem
	 */
	private final StringBuffer message = new StringBuffer();
	/**
	 * Buffer que cont�m o corpo da mensagem
	 */
	private boolean contentHtml = false;

	/**
	 * Lista com todas as pessoas que receberam email
	 */
	private List<Object> emails = new ArrayList<Object>();

	/**
	 * Lista com objetos InputStreamDataSource para anexar ao email.
	 */
	private final List<InputStreamDataSource> inputStream = new ArrayList<InputStreamDataSource>();

	/**
	 * Propriedades do email.
	 */
	private Properties properties = null;

	/**
	 * Construtor da classe Email sem par�metros.
	 */
	public Email ()
	{
		super();
	}

	/**
	 * Adiciona dados a refer�ncia da Lista de Emails.
	 * @param obj Object Email
	 */
	public void addEmails (Object obj)
	{
		emails.add(obj);
	}

	/**
	 * Retorna um verdadero se for o tipo contentHtml
	 * @return boolean
	 */
	public boolean getContentHtml ()
	{
		return contentHtml;
	}

	/**
	 * M�todo getter que retorna a refer�ncia da Lista de Emails.
	 * @return List lista de e-mails
	 */
	public List<Object> getEmails ()
	{
		return emails;
	}

	/**
	 * M�todo getter que retorna a refer�ncia do Buffer FROM.
	 * @return remetente do e-mail
	 */
	public String getFrom ()
	{
		return from.toString();
	}

	/**
	 * M�todo getter que retorna a refer�ncia do Buffer MENSAGEM.
	 * @return mensagem do tipo String
	 */
	public String getMessage ()
	{
		return message.toString();
	}

	/**
	 * M�todo getter que retorna a refer�ncia do Buffer ASSUNTO.
	 * @return assuunto do e-mail
	 */
	public String getSubject ()
	{
		return subject.toString();
	}

	/**
	 * M�todo getter que retorna a refer�ncia do Buffer PARA.
	 * @return destinat�rio do e-mail
	 */
	public String getTo ()
	{
		return to.toString();
	}

	/**
	 * Seta o contentHtml.
	 * @param contentHtml
	 */
	public void setContentHtml (boolean contentHtml)
	{
		this.contentHtml = contentHtml;
	}

	/**
	 * M�todo setter para inserir dados a refer�ncia da Lista de Emails.
	 * @param emails List Lista de Emails
	 */
	public void setEmails (List<Object> emails)
	{
		this.emails = emails;

		for (Iterator<Object> iter = emails.iterator(); iter.hasNext();)
		{
			this.setTo((String)iter.next());

		}
	}

	/**
	 * M�todo setter para inserir dados a refer�ncia do Buffer FROM.
	 * @param from String Buffer DE
	 */
	public void setFrom (String from)
	{
		this.from.append(from + " ");
	}

	/**
	 * M�todo setter para inserir dados a refer�ncia do Buffer MENSAGEM.
	 * @param message String Buffer MENSAGEM
	 */
	public void setMessage (String message)
	{
		this.message.append(message);
	}

	/**
	 * M�todo setter para inserir dados a refer�ncia do Buffer ASSUNTO.
	 * @param subject String Buffer ASSUNTO
	 */
	public void setSubject (String subject)
	{
		this.subject.append(subject);
	}

	/**
	 * M�todo setter para inserir dados a refer�ncia do Buffer PARA.
	 * @param to String Buffer PARA
	 */
	public void setTo (String to)
	{
		this.to.append(to + " ");
	}

	/**
	 * Retorna um bcc
	 * @return StringBuffer
	 */
	@SuppressWarnings("unqualified-field-access")
	public StringBuffer getBcc ()
	{
		return bcc;
	}

	/**
	 * Retorna um cc
	 * @return StringBuffer
	 */
	@SuppressWarnings("unqualified-field-access")
	public StringBuffer getCc ()
	{
		return cc;
	}

	/**
	 * Atribui um bcc
	 * @param bcc do tipo StringBuffer
	 */
	public void setBcc (StringBuffer bcc)
	{
		this.bcc = bcc;
	}

	/**
	 * Atribui um cc.
	 * @param cc do tipo StringBuffer
	 */
	public void setCc (StringBuffer cc)
	{
		this.cc = cc;
	}

	/**
	 * @return the properties
	 */
	public Properties getProperties ()
	{
		return properties;
	}

	/**
	 * @param properties the properties to set
	 */
	public void setProperties (Properties properties)
	{
		this.properties = properties;
	}

	/**
	 * @return the InputStreamDataSource
	 */
	public final List<InputStreamDataSource> getInputStream ()
	{
		return inputStream;
	}

	/**
	 * @param InputStreamDataSource the inputStream to set
	 */
	// public final void setInputStream (List<InputStreamDataSource>
	// inputStream)
	// {
	// this.inputStream = inputStream;
	// }
}