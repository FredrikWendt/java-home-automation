package se.wendt.home.util;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class PlainTextSender {

	private final String smtpHost;

	public PlainTextSender() {
		this("localhost");
	}

	public PlainTextSender(String smtpHost) {
		this.smtpHost = smtpHost;
	}

	public void send(String subject, String body) {
		try {
			doSend(subject, body);
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}

	private void doSend(String subject, String body) throws MessagingException {
		Properties props = new Properties();
		props.setProperty("mail.transport.protocol", "smtp");
		props.setProperty("mail.host", smtpHost);

		Session mailSession = getJavaxMailSession(props);
		Transport transport = mailSession.getTransport();

		MimeMessage message = new MimeMessage(mailSession);
		message.setSubject(subject);
		message.setContent(body, "text/plain");
		message.setFrom(new InternetAddress("fredrik@wendt.se"));
		message.addRecipient(Message.RecipientType.TO, new InternetAddress("fredrik@wendt.se"));

		transport.connect();
		transport.sendMessage(message, message.getRecipients(Message.RecipientType.TO));
		transport.close();
	}

	protected Session getJavaxMailSession(Properties props) {
		Session mailSession = Session.getDefaultInstance(props, null);
		return mailSession;
	}

}
