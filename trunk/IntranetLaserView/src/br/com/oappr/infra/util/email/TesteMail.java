package br.com.oappr.infra.util.email;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class TesteMail
{

	// TODO: **** TESTAR SETAR PARAMETROS ABAIXO

	// props.put("mail.transport.protocol", "smtp");
	// props.put("mail.smtp.starttls.enable","true");
	// props.put("mail.smtp.host", "gmail-smtp.l.google.com");
	// props.put("mail.smtp.auth", "true");
	// props.put("mail.smtp.user", "meumail@gmail.com");
	// props.put("mail.smtp.host", "gmail-smtp.l.google.com");
	// props.put("mail.smtp.auth", "true");
	// props.put("mail.debug", "true");
	// props.put("mail.smtp.starttls.enable", "true");
	// props.put("mail.smtp.port", "465");
	// props.put("mail.smtp.socketFactory.port", "465");
	// props.put("mail.smtp.socketFactory.class",
	// "javax.net.ssl.SSLSocketFactory");
	// props.put("mail.smtp.socketFactory.fallback", "false");

	/**
	 * @param args
	 */
	public static void main (String[] args)
	{
		// TesteMail mail = new TesteMail();
		try
		{
			TesteMail.testMailGoogle();
			// mail.sendMail(null, message, null, null);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Envia um e-mail de acordo com os parâmetros.
	 * @param subject - Assunto da mensagem.
	 * @param message - Mensagem.
	 * @param remetente - Rementente da mensagem.
	 * @param propertiesSendTo - Chave no arquivo de properties
	 *        (email.properties), que contém o email para qual deve ser enviada
	 *        a mensagem.
	 * @param propertiesSubject - Chave no arquivo de properties
	 *        (email.properties), que contém o conteúdo referente ao assunto que
	 *        comporá a mensagem.
	 * @throws Exception
	 */
	public void sendMail (String subject, String message, String remetente, String propertiesSendTo)
	    throws Exception
	{
		try
		{
			CreateEmail email = new CreateEmail();
			email.createMail(subject, message, remetente, propertiesSendTo, null);
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
	}

	/**
	 * Teste send email from google.
	 */
	public static void testMailGoogle ()
	{
		final String username = "oap.pr.email@gmail.com";
		final String password = "oap@netuno";

		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");

		Session session = Session.getInstance(props, new javax.mail.Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication ()
			{
				return new PasswordAuthentication(username, password);
			}
		});

		try
		{
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress("contato@oap.com.br"));
			message.setRecipients(Message.RecipientType.TO,
			    InternetAddress.parse("fmrabelo@gmail.com"));
			message.setSubject("Testing Subject");
			message.setText("Dear Mail Crawler," + "\n\n No spam to my email, please!");
			Transport.send(message);
			System.out.println("Done");
		}
		catch (MessagingException e)
		{
			throw new RuntimeException(e);
		}
	}
}
