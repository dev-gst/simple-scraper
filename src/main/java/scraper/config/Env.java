package scraper.config;

import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

public class Env {
    private static final String CONFIG_FILE_PATH = "src/main/resources/config/application.yaml";
    private static final String RECIPIENTS_FILE_PATH = "src/main/resources/email/recipients.txt";
    private static final String DOWNLOADS_DIR = "src/main/resources/downloads";

    private static final String EMAIL_HOST;
    private static final String EMAIL_PORT;
    private static final String EMAIL_USERNAME;
    private static final String EMAIL_PASSWORD;
    private static final String EMAIL_FROM;
    private static final String EMAIL_STARTTLS_ENABLE;
    private static final String EMAIL_AUTH;

    static {
        Yaml yaml = new Yaml();
        Map<String, Map<String, String>> config;

        try (InputStream applicationConfigs = Files.newInputStream(Paths.get(CONFIG_FILE_PATH))) {
            config = yaml.load(applicationConfigs);
        } catch (Exception e) {
            throw new RuntimeException("Error loading application configs", e);
        }

        EMAIL_HOST = config.get("email").get("host");
        EMAIL_PORT = config.get("email").get("port");
        EMAIL_USERNAME = config.get("email").get("username");
        EMAIL_PASSWORD = config.get("email").get("password");
        EMAIL_FROM = config.get("email").get("from");
        EMAIL_STARTTLS_ENABLE = config.get("email").get("starttls-enable");
        EMAIL_AUTH = config.get("email").get("auth");
    }

    public static String getEmailHost() {
        return EMAIL_HOST;
    }

    public static String getEmailPort() {
        return EMAIL_PORT;
    }

    public static String getEmailUsername() {
        return EMAIL_USERNAME;
    }

    public static String getEmailPassword() {
        return EMAIL_PASSWORD;
    }

    public static String getEmailFrom() {
        return EMAIL_FROM;
    }
    
    public static String getEmailStarttlsEnable() {
        return EMAIL_STARTTLS_ENABLE;
    }

    public static String getEmailAuth() {
        return EMAIL_AUTH;
    }

    public static String getRecipientsFilePath() {
        return RECIPIENTS_FILE_PATH;
    }

    public static String getDownloadsDir() {
        return DOWNLOADS_DIR;
    }
}
