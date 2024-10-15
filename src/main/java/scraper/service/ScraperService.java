package scraper.service;

import groovyx.net.http.HttpBuilder;
import org.jsoup.nodes.Document;
import scraper.data.file.DataIO;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.List;

public class ScraperService {

    private static final List<String> task1 = List.of(
            "Espaço do Prestador de Serviços de Saúde",
            "TISS - Padrão para Troca de Informação de Saúde Suplementar",
            "Clique aqui para acessar a versão ",
            "Componente de Comunicação"
    );

    private static final List<String> task2 = List.of(
            "Espaço do Prestador de Serviços de Saúde",
            "TISS - Padrão para Troca de Informação de Saúde Suplementar",
            "Clique aqui para acessar todas as versões dos Componentes"
    );

    private static final List<String> task3 = List.of(
            "Espaço do Prestador de Serviços de Saúde",
            "TISS - Padrão para Troca de Informação de Saúde Suplementar",
            "Clique aqui para acessar as planilhas",
            "Clique aqui para baixar a tabela de erros no envio para a ANS (.xlsx)"
    );

    private static final String DOWNLOAD_PATH = "src/main/resources/downloads";

    private static boolean crawling = true;
    private static int taskNumber = 0;

    private static String URI;

    public void start() {

        System.out.println("\nStarting tasks...");

        while (crawling) {
            getTask(++taskNumber);
        }

        System.out.println("\nTasks completed");
    }

    private void getTask(int taskNumber) {
        URI = "https://www.gov.br/ans/pt-br/";

        switch (taskNumber) {
            case 1:
                System.out.println("\nTask 1: started");
                executeSubtasks(task1); // Correct
                System.out.println("\nTask 1: completed");
                break;
            case 2:
                System.out.println("\nTask 2: started");
                executeSubtasks(task2);
                System.out.println("\nTask 2: completed");
                break;
            case 3:
                System.out.println("\nTask 3: started");
                executeSubtasks(task3);
                System.out.println("\nTask 3: completed");
                break;
            default:
                crawling = false;
        }
    }

    private void executeSubtasks(List<String> task) {
        try {
            for (String subtask : task) {

                Document doc = scrape(URI);
                URI = ParserService.parseLink(doc, subtask);
                if (subtask.equals(task2.get(2))) {
                    Thread.sleep(2000);
                    Document tissHistory = scrape(URI);
                    List<String> lines = ParserService.parseTISSHistory(tissHistory);

                    DataIO.saveTissHistory(lines);
                    return;
                }

                if (URI.endsWith(".zip") || URI.endsWith(".xlsx")) {
                    downloadFile(URI);
                    return;
                }

                Thread.sleep(2000);
            }
        } catch (InterruptedException e) {
            throw new RuntimeException("Error executing subtasks", e);
        }
    }

    private void executeFinalSubtask() {

    }

    private Document scrape(String uri) {
        System.out.println("\nScraping... " + uri);
        Document response = null;

        try (HttpBuilder request = HttpBuilder.configure(config -> {
            config.getRequest().setUri(uri);
        })) {

            response = request.get(Document.class, config -> {});

        } catch (IOException e) {
            System.err.println("Error " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.err.println("The URI is invalid: " + e.getMessage());
        }

        return response;
    }

    private void downloadFile(String uri) {
        System.out.println("\nDownloading file from: " + uri);

        try (HttpBuilder request = HttpBuilder.configure(config -> {
            config.getRequest().setUri(uri);
        })) {
            byte[] response = request.get(byte[].class, config -> config.getRequest().getUri());

            try (InputStream inputStream = new ByteArrayInputStream(response)) {
                DataIO.saveFileTo(inputStream, DOWNLOAD_PATH, Paths.get(uri).getFileName().toString());
            }

        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }

        System.out.println("File downloaded to: " + DOWNLOAD_PATH);
    }
}
