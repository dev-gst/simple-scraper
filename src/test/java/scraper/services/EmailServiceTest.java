package scraper.services;

import jakarta.mail.*;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import scraper.config.Env;
import scraper.mocks.EnvMock;

import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

public class EmailServiceTest {

    AutoCloseable closeable;

    MockedStatic<Env> envMockedStatic;
    MockedStatic<Transport> transportMockedStatic;

    Address[] mockRecipients;

    @Mock
    Multipart mockContent;

    @Mock
    MimeMessage mockMessage;

    EmailService emailService;

    @BeforeEach
    void setUp() {
        envMockedStatic = EnvMock.getEnvMockedStatic();
        transportMockedStatic = mockStatic(Transport.class);
        closeable = openMocks(this);

        mockRecipients = new Address[0];

        emailService = new EmailService(
                mockMessage,
                mockRecipients,
                "Files are ready",
                mockContent
        );
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
        envMockedStatic.close();
        transportMockedStatic.close();
    }

    @Test
    public void testConstructMessage() throws MessagingException {
        emailService.constructMessage();

        verify(mockMessage).setRecipients(eq(Message.RecipientType.TO), any(Address[].class));
        verify(mockMessage).setFrom(any(Address.class));
        verify(mockMessage).setSubject("Files are ready");
        verify(mockMessage).setContent(any());
    }

    @Test
    public void testSendEmail() throws MessagingException {
        emailService.sendEmail();

        transportMockedStatic.verify(() -> Transport.send(mockMessage));
    }
}
