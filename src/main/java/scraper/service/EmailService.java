package scraper.service;

import jakarta.mail.*;
import jakarta.mail.internet.*;
import scraper.config.Env;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Properties;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class EmailService {

    public static void sendEmail() throws MessagingException {
        Properties prop = getProperties();

        Session session = Session.getInstance(prop, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(Env.getEmailUsername(), Env.getEmailPassword());
            }
        });

        Message message = new MimeMessage(session);

        List<Address> recipients = getAddresses();
        for (Address recipient : recipients) {
            message.addRecipient(Message.RecipientType.TO, recipient);
        }

        Multipart multipart = getMimeMultipart();

        message.setFrom(InternetAddress.parse(Env.getEmailFrom(), false)[0]);
        message.setSubject("Files are ready");
        message.setContent(multipart);

        Transport.send(message);
    }

    private static Multipart getMimeMultipart() throws MessagingException {
        String textContent = "Files generated by the scraper are ready for download.\n" +
                "Files parsed at: " + Instant.now().toString();

        MimeBodyPart messageBodyPart = new MimeBodyPart();
        messageBodyPart.setContent(textContent, "text/html; charset=utf-8");

        MimeBodyPart attachmentBodyParts = getAttachments();

        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(messageBodyPart);
        multipart.addBodyPart(attachmentBodyParts);

        return multipart;
    }

    private static List<Address> getAddresses() {
        List<Address> recipients = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(Env.getRecipientsFilePath()))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.isBlank() || line.startsWith("#")) {
                    continue;
                }

                recipients.add(InternetAddress.parse(line, true)[0]);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error reading recipients file", e);
        }

        if (recipients.isEmpty()) {
            throw new NoSuchElementException("No recipients found");
        }

        return recipients;
    }

    private static Properties getProperties() {
        Properties prop = new Properties();
        prop.put("mail.smtp.auth", Env.getEmailAuth());
        prop.put("mail.smtp.starttls.enable", Env.getEmailStarttlsEnable());
        prop.put("mail.smtp.host", Env.getEmailHost());
        prop.put("mail.smtp.port", Env.getEmailPort());
        prop.put("mail.smtp.ssl.trust", Env.getEmailHost());

        return prop;
    }

    // TODO has to be a list. Only one attachment is being supported for now
    private static MimeBodyPart getAttachments() {
        MimeBodyPart mimeBodyPart = new MimeBodyPart();
        List<Path> paths = getPathsFromDirectory();

        for (Path path : paths) {
            try {
                mimeBodyPart.attachFile(new File(path.toString()));
            } catch (IOException | MessagingException e) {
                throw new RuntimeException(e);
            }
        }

        return mimeBodyPart;
    }

    private static List<Path> getPathsFromDirectory() {
        try (Stream<Path> paths = Files.list(Paths.get(Env.getDownloadsDir()))) {
            return paths.filter(Files::isRegularFile)
                        .collect(Collectors.toList());

        } catch (IOException e) {
            throw new RuntimeException("Error listing files", e);
        }
    }
}
