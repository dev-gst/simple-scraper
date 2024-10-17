package scraper.services;

import groovyx.net.http.HttpBuilder;
import org.jsoup.nodes.Document;
import scraper.data.file.DataIO;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;

public class ScraperService {

    private static final String DOWNLOAD_PATH = "src/main/resources/downloads";

    public static Document scrape(String uri) {
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

    public static void downloadFile(String uri) {
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
