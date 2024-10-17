package scraper.services.helpers;

import scraper.config.Env;

import java.util.Properties;

public class EmailProperties {

    public static Properties getProperties() {
        Properties prop = new Properties();
        prop.put("mail.smtp.auth", Env.getEmailAuth());
        prop.put("mail.smtp.starttls.enable", Env.getEmailStarttlsEnable());
        prop.put("mail.smtp.host", Env.getEmailHost());
        prop.put("mail.smtp.port", Env.getEmailPort());
        prop.put("mail.smtp.ssl.trust", Env.getEmailHost());

        return prop;
    }
}
