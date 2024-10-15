package scraper.service;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class ParserService {

    public static String parseLink(Document doc, String matchingText) {
        System.out.println("Parsing HTML...");

        String newUri = doc.getElementsByTag("a")
                .stream()
                .filter(e -> e.text().contains(matchingText))
                .map(e -> e.attr("href"))
                .findFirst()
                .orElse(null);

        if (newUri == null) {
            throw new NoSuchElementException("Could not find link with text: " + matchingText);        }

        return newUri;
    }

    public static List<String> parseTissHistory(Document doc) {
        Elements tableRows = doc.select("tr");
        if (tableRows.isEmpty()) {
            throw new RuntimeException("Could not find table rows");
        }

        Elements parsedRows = new Elements();
        for (Element row : tableRows) {

            Element parsedRow = new Element("tr");
            for (int i = 0; i < 3; i++) {
                Element td = row.child(0);

                if (i == 2) {
                    td.text(td.text().trim());
                } else {
                    td.text(td.text().trim() + ",");
                }

                parsedRow.appendChild(td);
            }

            parsedRows.add(parsedRow);

            if (parsedRow.text().contains("Jan/2016")) {
                break;
            }
        }

        List<String> lines = new ArrayList<>();

        String header = parsedRows.get(0).text().replaceAll(",\\s", ",");
        lines.add(header);

        for (int i = parsedRows.size() - 1; i >= 1; i--) {
            String line = parsedRows.get(i).text().replaceAll(",\\s", ",");

            lines.add(line);
        }

        return lines;
    }
}
