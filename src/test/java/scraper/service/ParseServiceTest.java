package scraper.service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.NoSuchElementException;

public class ParseServiceTest {

    @Test
    public void testParseLink() {
        String html = "<html><body><a href='https://example.com/task1'>Task 1</a></body></html>";
        Document doc = Jsoup.parse(html);

        String expectedLink = "https://example.com/task1";
        String actualLink = ParserService.parseLink(doc, "Task 1");

        Assertions.assertEquals(expectedLink, actualLink);
    }

    @Test
    public void testParseLinkNoLinkFound() {
        String html = "<html><body><a href='https://example.com/task1'>Text 1</a></body></html>";
        Document doc = Jsoup.parse(html);

        Assertions.assertThrows(NoSuchElementException.class, () ->
                ParserService.parseLink(doc, "Text 2"));
    }

    @Test
    public void testParseTISSHistory() throws IOException {
        Document doc;
        FileReader file = new FileReader("src/test/resources/TISSHistoryTableForTest.html", StandardCharsets.UTF_8);
        BufferedReader br = new BufferedReader(file);

        StringBuilder sb = new StringBuilder();
        String fileLine;
        while ((fileLine = br.readLine()) != null) {
            sb.append(fileLine);
        }

        doc = Jsoup.parse(sb.toString());

        List<String> actualLines = ParserService.parseTISSHistory(doc);
        List<String> expectedLines = List.of(
                "Competência,Publicação,Início de Vigência",
                "Jan/2016,15/1/2016,15/1/2016",
                "abr/2025,30/04/2025,01/05/2025",
                "mar/2025,30/03/2025,01/04/2025",
                "fev/2025,28/02/2025,01/03/2025",
                "jan/2025,30/01/2025,01/02/2025",
                "dez/2024,30/12/2024,01/01/2025",
                "nov/2024,29/11/2024,01/12/2024",
                "out/2024,30/10/2024,01/11/2024",
                "set/2024,25/09/2024,01/10/2024",
                "jul/2024,30/07/2024,01/08/2024",
                "mai/2024,29/05/2024,01/06/2024"
        );

        Assertions.assertEquals(expectedLines, actualLines);
    }
}

