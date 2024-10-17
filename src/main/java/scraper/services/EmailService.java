package scraper.services;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import scraper.config.Env;

public class EmailService {

    private final Message message;

    private final Address[] recipients;
    private final String subject;
    private final Multipart content;

    public EmailService(
            Message message,
            Address[] recipients,
            String subject,
            Multipart content
    ) {
        this.recipients = recipients;
        this.message = message;
        this.subject = subject;
        this.content = content;
    }

    public void constructMessage() throws MessagingException {
        message.setRecipients(Message.RecipientType.TO, recipients);
        message.setFrom(InternetAddress.parse(Env.getEmailFrom(), false)[0]);
        message.setSubject(subject);
        message.setContent(content);
    }

    public void sendEmail() throws MessagingException {
        constructMessage();

        Transport.send(message);
    }
}
