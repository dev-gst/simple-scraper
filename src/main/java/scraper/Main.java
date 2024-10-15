package scraper;

import scraper.service.ScraperService;

public class Main {

    public static void main(String[] args) {
        ScraperService scraperService = new ScraperService();
        scraperService.start();
    }
}