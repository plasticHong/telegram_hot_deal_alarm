package com.plastic.scraper.app;

import com.plastic.scraper.bean.RuliwebHotDealScrapper;
import com.plastic.scraper.bean.TelegramBot;
import com.plastic.scraper.dto.ScrapingResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ScrapingManager {

    private final TelegramBot telegramBot;
    private final RuliwebHotDealScrapper ruliwebScrapper;

    public void scrapingAndMessageSend(){
        Optional<ScrapingResponse> scrappingResponse = ruliwebScrapper.doScraping();

        scrappingResponse.ifPresent(response -> telegramBot.messageSend(response.getLink()));
    }

}
