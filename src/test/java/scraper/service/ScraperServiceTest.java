package scraper.service;

import groovyx.net.http.HttpBuilder;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import scraper.data.file.DataIO;

import java.io.InputStream;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ScraperServiceTest {

    private AutoCloseable closeable;

    @Mock
    private HttpBuilder mockHttpBuilder;

    @Mock
    private Document mockDocument;

    private MockedStatic<HttpBuilder> mockedStaticHttpBuilder;

    @BeforeEach
    public void setUp() {
        mockedStaticHttpBuilder = Mockito.mockStatic(HttpBuilder.class);
        closeable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    public void tearDown() throws Exception {
        closeable.close();
        mockedStaticHttpBuilder.close();
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testScrapeSuccess() {
        when(mockHttpBuilder.get(eq(Document.class), any(Consumer.class))).thenReturn(mockDocument);
        mockedStaticHttpBuilder.when(() -> HttpBuilder.configure(any(Consumer.class))).thenReturn(mockHttpBuilder);

        Document result = ScraperService.scrape(anyString());

        assertNotNull(result);
        assertEquals(mockDocument, result);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testDownloadFile() {
        MockedStatic<DataIO> mockedStaticDataIO = Mockito.mockStatic(DataIO.class);

        when(mockHttpBuilder.get(eq(byte[].class), any(Consumer.class))).thenReturn(new byte[0]);
        mockedStaticHttpBuilder.when(() -> HttpBuilder.configure(any(Consumer.class))).thenReturn(mockHttpBuilder);

        ScraperService.downloadFile(anyString());

        verify(mockHttpBuilder, times(1)).get(eq(byte[].class), any(Consumer.class));
        mockedStaticDataIO.verify(() -> DataIO.saveFileTo(any(InputStream.class), anyString(), anyString()), times(1));
    }
}
