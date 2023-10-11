package com.plastic.scraper.app;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@EnableScheduling
public class ScrapperRunner {

    private static final Long EVERY_MINUTE = 60000L;
    private final ScrappingManager scrappingManager;

    @Scheduled(fixedDelay = EVERY_MINUTE)
    public void run() {
        scrappingManager.scrappingAndMessageSend();
    }


}
