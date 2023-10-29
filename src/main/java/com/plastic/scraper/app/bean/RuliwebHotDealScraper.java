package com.plastic.scraper.app.bean;

import com.plastic.scraper.app.ScrapingResult;
import com.plastic.scraper.entity.HotDealRecord;
import com.plastic.scraper.entity.RuliwebLastData;
import com.plastic.scraper.repository.HotDealRecordRepo;
import com.plastic.scraper.repository.RuliwebLastDataRepo;
import com.plastic.scraper.app.util.GlobalUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

@Component
@Log4j2
@RequiredArgsConstructor
public class RuliwebHotDealScraper {

    private final static String URL = "https://bbs.ruliweb.com/market/board/1020?page=";

    private final RuliwebLastDataRepo ruliwebLastDataRepo;
    private final HotDealRecordRepo hotDealRecordRepo;


    @Transactional
    public Optional<ScrapingResult> doScraping() {

        RuliwebLastData savedLastData = ruliwebLastDataRepo.findAll().stream().findFirst().orElse(new RuliwebLastData());

        Optional<ScrapingResult> scrapingResponse = ruliwebScrapingAndFindNewArticle(savedLastData.getTitle());

        if (scrapingResponse.isEmpty()) {
            return Optional.empty();
        }

        ScrapingResult scrapingResult = scrapingResponse.get();
        ruliwebLastDataSave(scrapingResult);

        String originalHotDealUrl = getOriginalHotDealUrl(scrapingResult);

        Optional<HotDealRecord> hotDealRecord = hotDealRecordRepo.findByUrl(originalHotDealUrl);

        if (hotDealRecord.isPresent()) {
            return Optional.empty();
        }

        hotDealRecordRepo.save(new HotDealRecord(originalHotDealUrl));

        return scrapingResponse;
    }

    private void ruliwebLastDataSave(ScrapingResult scrapedLastData) {

        Optional<RuliwebLastData> byId = ruliwebLastDataRepo.findAll()
                .stream()
                .findFirst();

        if (byId.isEmpty()){return;}

        byId.get().setTitle(scrapedLastData.getTitle());
    }

    private String getOriginalHotDealUrl(ScrapingResult scrapingResult){

        String scrapedPageUrl = scrapingResult.getUrl();

        Elements select = GlobalUtil.getDocumentByUrl(scrapedPageUrl)
                .select(".source_url")
                .select("a");

        return select.text();
    }


    private Optional<ScrapingResult> ruliwebScrapingAndFindNewArticle(String lastData) {

        List<ScrapingResult> responseList;

        OptionalInt matchingIdxByLastData;

        int pageNum = 1;

        do {

            Document document = GlobalUtil.getDocumentByUrl(URL, pageNum);
            Elements aTags = findElement(document);

            responseList = aTags.stream()
                    .map(e -> new ScrapingResult(e.text().trim(), e.attr("href"))).toList();

            matchingIdxByLastData = GlobalUtil.findMatchingIdxByLastData(responseList, lastData);

            if (matchingIdxByLastData.isEmpty()) {
                log.info("일치하는 단어가 없습니다. 다음 페이지에서 다시 가조오세요");
            }

            pageNum += 1;

        } while (matchingIdxByLastData.isEmpty());

        return GlobalUtil.getScrapingResult(responseList, matchingIdxByLastData);
    }

    private Elements findElement(Document document) {
        return document
                .select("tr:not(.inside)")
                .select("tr:not(.notice)")
                .select(".deco")
                .select("a");
    }

}
