package br.com.oappr.infra.util.email;

import java.io.Serializable;
import java.security.Security;
import java.util.Date;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 * <ul>
 * A classe SendEmail disponibiliza metódos responsáveis pelo envio de e-mails
 * atráves de um servidor SMTP.
 * </ul>
 * @author Desenvolvimento
 */
public final class SendEmail
    implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1897889754221L;
	private final Email email;
	private static final boolean debug = false;

	/**
	 * Construtor da classe SendEmail com o parâmetro: email
	 */
	public SendEmail (Email email)
	{
		this.email = email;
	}

	/**
	 * Método responsável pelo envio de Emails
	 * @exception EmailException
	 */
	public void send () throws EmailException
	{
		Properties mailProps = email.getProperties();
		Authenticator auth = null;
		Session session = null;
		MimeMessage message = null;
		try
		{
			// configurações gerais para protocolo smtp.
			Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
			auth = new SMTPAuthenticator();
			// Session session = Session.getDefaultInstance(mailProps, auth);
			session = Session.getInstance(mailProps, auth);
			// opção para degug,ativo se true
			session.setDebug(debug);
			message = new MimeMessage(session);
			// Buffer que contém o email de quem está enviando
			message.setSender(new InternetAddress(email.getFrom()));
			// Setter para o Assunto da mensagem
			message.setSubject(email.getSubject());
			// Setter para a data de envio
			message.setSentDate(new Date());
			// Setter para adicionar os emails dos destinatarios da mensagem
			if (email.getTo().indexOf(',') > 0)
			{
				// Parse the given string and create an InternetAddress. If
				// strict
				// is false, the detailed syntax of the address isn't checked.
				final InternetAddress[] internetAddress = InternetAddress.parse(email.getTo(),
				    false);
				message.setRecipients(Message.RecipientType.TO, internetAddress);
				// message.setRecipients(Message.RecipientType.TO,
				// InternetAddress.parse(this.email.getTo()));
			}
			else
			{
				message.setRecipient(Message.RecipientType.TO, new InternetAddress(email.getTo()));
			}

			// validar se contem anexos e inserir arquivos ao email.
			if ((email.getInputStream() == null) || email.getInputStream().isEmpty())
			{
				// email com texto simples, sem anexo.
				message.setContent(email.getMessage(), "text/plain");
			}
			else
			{
				// Create the message part (Email Attachment)
				BodyPart messageBodyPart = new MimeBodyPart();

				// Fill the message
				messageBodyPart.setText(email.getMessage());
				// Create a Multipart
				Multipart multipart = new MimeMultipart();
				// Add part one
				multipart.addBodyPart(messageBodyPart);

				// TODO: Attachments.

				// Part two is attachment
				// Create second body part
				// Part two is attachment
				// for (InputStreamDataSource attach : email.getInputStream())
				// {
				// messageBodyPart = new MimeBodyPart();
				// // attachment
				// messageBodyPart.setDataHandler(new DataHandler(attach));
				// // filename
				// messageBodyPart.setFileName(attach.getName());
				// // Add part two
				// multipart.addBodyPart(messageBodyPart);
				// }

				// Put parts in message
				message.setContent(multipart);
			}

			Transport.send(message);
			System.out.println("Email enviado com sucesso: " + email.getTo());
		}
		catch (Exception e)
		{
			throw new EmailException(e, "Sua mensagem não pôde ser enviada!");
		}
		finally
		{
			auth = null;
			mailProps = null;
			session = null;
			message = null;
		}
	}

	private class SMTPAuthenticator
	    extends javax.mail.Authenticator
	{
		@Override
		public PasswordAuthentication getPasswordAuthentication ()
		{
			return new PasswordAuthentication(this.getUser(), this.getPwd());
		}

		private final String getPwd ()
		{
			return email.getProperties().getProperty("mail.smtp.pass");
		}

		private final String getUser ()
		{
			return email.getProperties().getProperty("mail.smtp.user");
		}
	}

	/**
	 * @return Email
	 */
	@SuppressWarnings("unqualified-field-access")
	public Email getEmail ()
	{
		return email;
	}
}