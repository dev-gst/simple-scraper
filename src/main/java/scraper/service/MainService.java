package scraper.service;

import org.jsoup.nodes.Document;
import scraper.data.file.DataIO;

import java.util.List;

import static scraper.service.ScraperService.downloadFile;
import static scraper.service.ScraperService.scrape;

public class MainService {

    private static final String MAIN_URI = "https://www.gov.br/ans/pt-br/";
    private static final String DOWNLOAD_FOLDER = "src/main/resources/downloads/";

    private static final List<String> linkAnchorPatterns1 = List.of(
            "Espaço do Prestador de Serviços de Saúde",
            "TISS - Padrão para Troca de Informação de Saúde Suplementar",
            "Clique aqui para acessar a versão ",
            "Componente de Comunicação"
    );

    private static final List<String> linkAnchorPatterns2 = List.of(
            "Espaço do Prestador de Serviços de Saúde",
            "TISS - Padrão para Troca de Informação de Saúde Suplementar",
            "Clique aqui para acessar todas as versões dos Componentes"
    );

    private static final List<String> linkAnchorPatterns3 = List.of(
            "Espaço do Prestador de Serviços de Saúde",
            "TISS - Padrão para Troca de Informação de Saúde Suplementar",
            "Clique aqui para acessar as planilhas",
            "Clique aqui para baixar a tabela de erros no envio para a ANS (.xlsx)"
    );

    private static final String STOP_PARSING_AT = "Jan/2016";

    private static boolean crawling = true;
    private static int linkAnchorPatternsNumber = 0;
    private static String URI;


    public void start() {
        System.out.println("\nStarting tasks...");

        while (crawling) {
            getLinkAnchorPatterns(++linkAnchorPatternsNumber);
        }

        System.out.println("\nTasks completed");
    }

    private void getLinkAnchorPatterns(int n) {
        URI = MAIN_URI;

        switch (n) {
            case 1:
                System.out.println("\nTask 1: started");
                processLink(linkAnchorPatterns1);
                System.out.println("\nTask 1: completed");
                break;
            case 2:
                System.out.println("\nTask 2: started");
                processLink(linkAnchorPatterns2);
                System.out.println("\nTask 2: completed");
                break;
            case 3:
                System.out.println("\nTask 3: started");
                processLink(linkAnchorPatterns3);
                System.out.println("\nTask 3: completed");
                break;
            default:
                crawling = false;
        }
    }

    private void processLink(List<String> linkAnchors) {
        try {
            for (String linkAnchor : linkAnchors) {
                Document doc = scrape(URI);
                URI = ParserService.parseLink(doc, linkAnchor);

                processLastLink(linkAnchor, URI);

                Thread.sleep(2000);
            }
        } catch (InterruptedException e) {
            throw new RuntimeException("Error scraping links", e);
        }
    }

    private void processLastLink(String linkAnchor, String uri) throws InterruptedException {
        if (linkAnchor.equals(linkAnchorPatterns2.get(2))) {
            Thread.sleep(2000);
            Document tissHistory = scrape(uri);
            List<String> lines = ParserService.parseTISSHistory(tissHistory, STOP_PARSING_AT);

            DataIO.saveTissHistory(lines, DOWNLOAD_FOLDER + "tiss_history.csv");
            return;
        }

        if (uri.endsWith(".zip") || uri.endsWith(".xlsx")) {
            downloadFile(uri);
        }
    }
}
