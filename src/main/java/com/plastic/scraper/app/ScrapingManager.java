package com.plastic.scraper.app;

import com.plastic.scraper.app.bean.FmKoreaHotDealScraper;
import com.plastic.scraper.app.bean.PpomPpuHotDealScraper;
import com.plastic.scraper.app.bean.RuliwebHotDealScraper;
import com.plastic.scraper.app.bean.TelegramBot;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ScrapingManager {

    private final TelegramBot telegramBot;

    private final RuliwebHotDealScraper ruliwebScraper;
    private final PpomPpuHotDealScraper ppomPpuScraper;
    private final FmKoreaHotDealScraper fmKoreaScraper;

    public void scrapingAndMessageSend() throws InterruptedException {

        ruliwebScraper.doScraping().ifPresent(telegramBot::messageSend);
        ppomPpuScraper.doScraping().ifPresent(telegramBot::messageSend);
        fmKoreaScraper.doScraping().ifPresent(telegramBot::messageSend);

    }

}
