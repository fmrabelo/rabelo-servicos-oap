/**
 * 
 */
package br.com.oappr.infra.util.email.googleSimples;

import java.io.Serializable;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import br.com.oappr.infra.properties.LoadResourceProperties;

/**
 * @author rabelo
 */
public final class SimpleGoogleMail
    implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2499314013506964600L;

	public static void sendMailViaGoogle (final String to, final String content)
	    throws MessagingException
	{
		final Properties props = LoadResourceProperties.getInstance().getPropertie(
		    "email.properties");
		props.put("mail.smtp.starttls.enable", "true");
		final String username = props.getProperty("mail.smtp.user");
		final String password = props.getProperty("mail.smtp.pass");
		final String from = props.getProperty("mail.from");
		final String subject = props.getProperty("mail.smtp.subject");

		final Session session = Session.getInstance(props, new javax.mail.Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication ()
			{
				return new PasswordAuthentication(username, password);
			}
		});

		try
		{
			final Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(from));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
			message.setSubject(subject);
			message.setText(content);
			Transport.send(message);
		}
		catch (MessagingException e)
		{
			throw e;
		}
	}
}
