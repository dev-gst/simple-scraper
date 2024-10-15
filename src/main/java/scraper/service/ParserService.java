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

    public static List<String> parseTISSHistory(Document doc, String rowToStop) {
        Elements tableRows = doc.select("tr");
        if (tableRows.isEmpty()) {
            throw new RuntimeException("Could not find table rows");
        }

        return constructTableLines(tableRows, rowToStop);
    }

    private static List<String> constructTableLines(Elements tableRows, String rowToStop) {
        List<String> lines = new ArrayList<>();
        for (Element row : tableRows) {

            // Get only the first 3 columns of each row
            StringBuilder line = new StringBuilder();
            for (int i = 0; i < 3; i++) {
                line.append(row.child(i).text().trim());
                line.append(",");
            }

            line.replace(line.length() - 1, line.length(), "");
            lines.add(line.toString());

            if (line.toString().contains(rowToStop)) {
                break;
            }
        }

        return orderTableLines(lines);
    }

    private static List<String> orderTableLines(List<String> originalLines) {
        List<String> lines = new ArrayList<>(originalLines);

        String header = lines.get(0);
        lines.sort((a, b) -> {
            if (a.equals(header) || b.equals(header)) {
                return 0;
            }

            return lines.indexOf(b) - lines.indexOf(a);
        });

        return lines;
    }
}