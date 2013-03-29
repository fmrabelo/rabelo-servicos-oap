package br.com.oappr.infra.util.email;

import java.io.Serializable;
import java.util.List;
import java.util.Properties;

import br.com.oappr.infra.properties.LoadResourceProperties;
import br.com.oappr.infra.util.email.attachment.InputStreamDataSource;

/**
 * <ul>
 * A classe CreateEmail é responsável por criar objeto da classe de Email para
 * aplicações e processar a lógica para configurações SMTP de Email.
 * </ul>
 * @author Desenvolvimento
 */

public final class CreateEmail
    implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 9744534234001L;

	/**
	 * Método responsável por setar e criar a mensagem no buffer, setar os
	 * valores do properties e redirecionar para o envio de e-mail.
	 * @param subject assunto da mensagem
	 * @param message mensagem do email
	 * @param from endereço de origem do email.
	 * @param sendTo endereço(s) de destino do email.
	 * @param listAttach TODO
	 * @throws Exception
	 */
	public void createMail (String subject, String message, String from, String sendTo,
	    List<InputStreamDataSource> listAttach) throws Exception
	{
		SendEmail sendEmail = null;
		Properties properties = null;
		Email email = null;
		try
		{
			properties = this.loadEmailProperties();
			email = new Email();
			email.setProperties(properties);
			// seta configurações do remetente, destinatario, mensagem e título
			// do email. Se os parametros do método forem nulos, o sistema
			// recupera os valores padrões no arquivo de propriedades de email.
			if ((from != null) && (from.trim().length() > 0))
			{
				email.setFrom(from);
			}
			else
			{
				email.setFrom(properties.getProperty("mail.from"));
			}
			if ((sendTo != null) && (sendTo.trim().length() > 0))
			{
				email.setTo(sendTo);
			}
			else
			{
				email.setTo(properties.getProperty("mail.to"));
			}
			if ((subject != null) && (subject.trim().length() > 0))
			{
				email.setSubject(subject);
			}
			else
			{
				email.setSubject(properties.getProperty("mail.smtp.subject"));
			}
			// anexos, não utilizado.
			// if ((listAttach != null) && !listAttach.isEmpty())
			// {
			// email.setInputStream(listAttach);
			// }
			email.setMessage(message);
			sendEmail = new SendEmail(email);
			sendEmail.send();
			email = null;
		}
		catch (EmailException e)
		{
			throw e;
		}
		catch (Exception e)
		{
			// if (!(e instanceof IntranetException))
			// throw new IntranetException(e, new Object[]{subject, message,
			// host, remetente,
			// propertiesSendTo, propertiesSubject});
			throw e;
		}
		finally
		{
			sendEmail = null;
			properties = null;
			email = null;
		}
	}

	/**
	 * Carga das propriedades do serviço de email.
	 * @return
	 * @throws Exception
	 */
	public final Properties loadEmailProperties () throws Exception
	{
		Properties properties = null;
		try
		{

			properties = LoadResourceProperties.getInstance().getPropertie("email.properties");
			properties.put("mail.transport.protocol", "smtp");
			properties.put("mail.smtp.starttls.enable", "false");
			// properties.put("mail.smtp.auth", "true");
			properties.put("mail.smtp.auth", "false");
			properties.put("mail.smtp.socketFactory.port", properties.getProperty("mail.smtp.port"));
			properties.put("mail.smtp.socketFactory.fallback", "false");
			properties.put("mail.smtp.debug", "true");
			// If set to false, the QUIT command is sent and the connection is
			// immediately closed.
			properties.put("mail.smtp.quitwait", "false");
		}
		catch (Exception e)
		{
			throw e;
		}
		finally
		{
		}
		return properties;
	}
}