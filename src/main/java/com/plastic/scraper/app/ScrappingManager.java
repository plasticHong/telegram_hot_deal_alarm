package com.plastic.scraper.app;

import com.plastic.scraper.bean.RuliwebHotDealScrapper;
import com.plastic.scraper.bean.TelegramBot;
import com.plastic.scraper.dto.ScrappingResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ScrappingManager {

    private final TelegramBot telegramBot;
    private final RuliwebHotDealScrapper ruliwebScrapper;

    public void scrappingAndMessageSend(){
        Optional<ScrappingResponse> scrappingResponse = ruliwebScrapper.doScrapping();

        scrappingResponse.ifPresent(response -> telegramBot.messageSend(response.getLink()));
    }

}
