package scraper;

import scraper.service.MainService;

public class Main {

    public static void main(String[] args) {
        MainService mainService = new MainService();
        mainService.start();
    }
}