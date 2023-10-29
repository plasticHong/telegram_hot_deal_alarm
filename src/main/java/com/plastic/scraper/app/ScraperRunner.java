package com.plastic.scraper.app;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@EnableScheduling
public class ScraperRunner {

    private static final long EVERY_MINUTE = 60000;
    private static final long TEN_SEC = 10000;
    private final ScrapingManager scrapingManager;

    @Scheduled(fixedDelay = EVERY_MINUTE)
    public void run() {
        scrapingManager.scrapingAndMessageSend();
    }


}
