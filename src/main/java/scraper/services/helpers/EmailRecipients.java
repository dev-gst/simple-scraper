package scraper.services.helpers;

import jakarta.mail.Address;
import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;
import scraper.config.Env;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class EmailRecipients {

    public static Address[] getAddresses() {
        List<Address> recipients;
        try (BufferedReader br = new BufferedReader(new FileReader(Env.getRecipientsFilePath()))) {
            recipients = readAddressFromFile(br);
        } catch (Exception e) {
            throw new RuntimeException("Error reading recipients file", e);
        }

        if (recipients.isEmpty()) {
            throw new NoSuchElementException("No recipients found");
        }

        return recipients.toArray(new Address[0]);
    }

    private static List<Address> readAddressFromFile(BufferedReader br) throws IOException, AddressException {
        List<Address> recipients = new ArrayList<>();

        String line;
        while ((line = br.readLine()) != null) {
            if (line.isBlank() || line.startsWith("#")) {
                continue;
            }

            recipients.add(InternetAddress.parse(line, true)[0]);
        }

        return recipients;
    }
}
