package com.plastic.scraper.app;

import com.plastic.scraper.bean.RuliwebHotDealScrapper;
import com.plastic.scraper.bean.TelegramBot;
import com.plastic.scraper.dto.ScrapingResult;
import com.plastic.scraper.repository.HotDealRecordRepo;
import com.plastic.scraper.util.GlobalUtil;
import lombok.RequiredArgsConstructor;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ScrapingManager {

    private final TelegramBot telegramBot;
    private final HotDealRecordRepo hotDealRecordRepo;

    private final RuliwebHotDealScrapper ruliwebScrapper;

    public void scrapingAndMessageSend(){
        Optional<ScrapingResult> scrappingResponse = ruliwebScrapper.doScraping();

        scrappingResponse.ifPresent(response -> telegramBot.messageSend(response.getUrl()));
    }





}
