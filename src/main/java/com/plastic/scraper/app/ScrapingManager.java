package com.plastic.scraper.app;

import com.plastic.scraper.app.bean.FmKoreaHotDealScraper;
import com.plastic.scraper.app.bean.PpomPpuHotDealScraper;
import com.plastic.scraper.app.bean.RuliwebHotDealScraper;
import com.plastic.scraper.app.bean.TelegramBot;
import com.plastic.scraper.dto.ScrapingResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ScrapingManager {

    private final TelegramBot telegramBot;

    private final RuliwebHotDealScraper ruliwebScrapper;
    private final PpomPpuHotDealScraper ppomPpuScraper;
    private final FmKoreaHotDealScraper fmKoreaScraper;

    public void scrapingAndMessageSend(){

        List<Optional<ScrapingResult>> scrapingResultList = new ArrayList<>();

        scrapingResultList.add(ruliwebScrapper.doScraping());
        scrapingResultList.add(ppomPpuScraper.doScraping());
        scrapingResultList.add(fmKoreaScraper.doScraping());

        scrapingResultList.forEach(optional->
                 optional.ifPresent(scrapingResult ->
                         telegramBot.messageSendWithApache(scrapingResult.getUrl())
                 )
        );

    }

}
