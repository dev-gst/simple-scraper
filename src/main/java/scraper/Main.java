package scraper;

import jakarta.mail.*;
import jakarta.mail.internet.MimeMessage;
import scraper.config.Env;
import scraper.services.EmailService;
import scraper.services.MainService;
import scraper.services.helpers.EmailContent;
import scraper.services.helpers.EmailProperties;
import scraper.services.helpers.EmailRecipients;
import scraper.services.helpers.EmailSession;

import java.util.Properties;

public class Main {

    public static void main(String[] args) throws MessagingException{
        MainService mainService = new MainService();
        mainService.start();

        Properties properties = EmailProperties.getProperties();
        Session session = EmailSession.getSession(properties, Env.getEmailUsername(), Env.getEmailPassword());

        Message message = new MimeMessage(session);
        Address[] recipients = EmailRecipients.getAddresses();
        String subject = EmailContent.getSubject();
        Multipart content = EmailContent.getMimeMultipart();

        EmailService emailService = new EmailService(message, recipients, subject, content);
        emailService.constructMessage();

        Transport.send(message);
    }
}