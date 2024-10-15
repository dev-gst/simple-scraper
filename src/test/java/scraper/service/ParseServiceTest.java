package scraper.service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

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
}

