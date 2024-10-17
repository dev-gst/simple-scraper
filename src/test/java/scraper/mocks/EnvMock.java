package scraper.mocks;

import org.mockito.MockedStatic;
import scraper.config.Env;

import static org.mockito.Mockito.mockStatic;

public class EnvMock {



    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static MockedStatic<Env> getEnvMockedStatic() {
        MockedStatic<Env> envMockedStatic = mockStatic(Env.class);

        envMockedStatic.when(Env::getEmailHost).thenReturn("testHost");
        envMockedStatic.when(Env::getEmailPort).thenReturn("testPort");
        envMockedStatic.when(Env::getEmailUsername).thenReturn("testEmail");
        envMockedStatic.when(Env::getEmailPassword).thenReturn("testPassword");
        envMockedStatic.when(Env::getEmailFrom).thenReturn("testEmailFrom@testEmailFrom");
        envMockedStatic.when(Env::getEmailStarttlsEnable).thenReturn("testStarttlsEnable");
        envMockedStatic.when(Env::getEmailAuth).thenReturn("testAuth");
        envMockedStatic.when(Env::getRecipientsFilePath).thenReturn("");
        envMockedStatic.when(Env::getDownloadsDir).thenReturn("");

        return envMockedStatic;
    }
}
