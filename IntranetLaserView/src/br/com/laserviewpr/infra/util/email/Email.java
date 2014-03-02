package br.com.laserviewpr.infra.util.email;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import br.com.laserviewpr.infra.util.email.attachment.InputStreamDataSource;

/**
 * <ul>
 * A classe e-mail é responsável por setar os atributos de e-mail.
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
	public static final String conteudoEmailRestrito = "Esta é uma mensagem automática, por favor não responda a este remetente.\n Se você não sabe do que se trata este email, favor entrar em contato com o departamento de TI da Empresa.\n\n";
	/**
	 * Buffer que contém o email de quem está enviando
	 */
	private final StringBuffer from = new StringBuffer();
	/**
	 * Buffer que contém o(s) email(s) de quem está recebendo
	 */
	private final StringBuffer to = new StringBuffer();
	/**
	 * Buffer que contém o(s) email(s) de quem está recebendo
	 */
	private StringBuffer cc = new StringBuffer();
	/**
	 * Buffer que contém o(s) email(s) de quem está recebendo
	 */
	private StringBuffer bcc = new StringBuffer();
	/**
	 * Buffer que contém o assunto do email
	 */
	private final StringBuffer subject = new StringBuffer();
	/**
	 * Buffer que contém o corpo da mensagem
	 */
	private final StringBuffer message = new StringBuffer();
	/**
	 * Buffer que contém o corpo da mensagem
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
	 * Construtor da classe Email sem parâmetros.
	 */
	public Email ()
	{
		super();
	}

	/**
	 * Adiciona dados a referência da Lista de Emails.
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
	 * Método getter que retorna a referência da Lista de Emails.
	 * @return List lista de e-mails
	 */
	public List<Object> getEmails ()
	{
		return emails;
	}

	/**
	 * Método getter que retorna a referência do Buffer FROM.
	 * @return remetente do e-mail
	 */
	public String getFrom ()
	{
		return from.toString();
	}

	/**
	 * Método getter que retorna a referência do Buffer MENSAGEM.
	 * @return mensagem do tipo String
	 */
	public String getMessage ()
	{
		return message.toString();
	}

	/**
	 * Método getter que retorna a referência do Buffer ASSUNTO.
	 * @return assuunto do e-mail
	 */
	public String getSubject ()
	{
		return subject.toString();
	}

	/**
	 * Método getter que retorna a referência do Buffer PARA.
	 * @return destinatário do e-mail
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
	 * Método setter para inserir dados a referência da Lista de Emails.
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
	 * Método setter para inserir dados a referência do Buffer FROM.
	 * @param from String Buffer DE
	 */
	public void setFrom (String from)
	{
		this.from.append(from + " ");
	}

	/**
	 * Método setter para inserir dados a referência do Buffer MENSAGEM.
	 * @param message String Buffer MENSAGEM
	 */
	public void setMessage (String message)
	{
		this.message.append(message);
	}

	/**
	 * Método setter para inserir dados a referência do Buffer ASSUNTO.
	 * @param subject String Buffer ASSUNTO
	 */
	public void setSubject (String subject)
	{
		this.subject.append(subject);
	}

	/**
	 * Método setter para inserir dados a referência do Buffer PARA.
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