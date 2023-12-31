package com.plastic.scraper.app.bean;

import com.plastic.scraper.app.ScrapingResult;
import com.plastic.scraper.entity.PpomPpuLastData;
import com.plastic.scraper.entity.HotDealRecord;
import com.plastic.scraper.repository.PpomPpuLastDataRepo;
import com.plastic.scraper.repository.HotDealRecordRepo;
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
public class PpomPpuHotDealScraper {

    private final static String URL = "https://www.ppomppu.co.kr/zboard/zboard.php?id=ppomppu&page=";

    private final PpomPpuLastDataRepo ppomPpuLastDataRepo;
    private final HotDealRecordRepo hotDealRecordRepo;


    @Transactional
    public Optional<ScrapingResult> doScraping() {

        PpomPpuLastData savedLastData = ppomPpuLastDataRepo.findAll().stream().findFirst().orElse(new PpomPpuLastData());

        Optional<ScrapingResult> scrapingResponse = ppomppuScrapingAndFindNewArticle(savedLastData.getArticleId());

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

        Optional<PpomPpuLastData> byId = ppomPpuLastDataRepo.findAll()
                .stream()
                .findFirst();

        if (byId.isEmpty()) {
            return;
        }

        byId.get().setTitle(scrapedLastData.getTitle());
        byId.get().setArticleId(scrapedLastData.getArticleId());
    }

    private String getOriginalHotDealUrl(ScrapingResult scrapingResult) {

        String scrapedPageUrl = scrapingResult.getUrl();

        Elements select = GlobalUtil.getDocumentByUrl(scrapedPageUrl)
                .select(".wordfix")
                .select("a");

        return select.text();
    }


    private Optional<ScrapingResult> ppomppuScrapingAndFindNewArticle(String lastData) {

        List<ScrapingResult> responseList;

        OptionalInt matchingIdxByLastData;

        Document document = GlobalUtil.getDocumentByUrl(URL);
        Elements aTags = findElement(document);

        String ppomppuPreFix = "https://www.ppomppu.co.kr/zboard/";

        responseList = aTags.stream()
                .map(element -> new ScrapingResult(
                        element
                                .select(":first-child")
                                .first().text().trim(),
                        element.select("tbody")
                                .select("tr")
                                .select("td")
                                .select("div")
                                .select("a")
                                .select("font").text().trim(),
                        ppomppuPreFix + element
                                .select("tbody")
                                .select("tr")
                                .select("td")
                                .select("div")
                                .select("a")
                                .attr("href"))
                )
                .toList();

        matchingIdxByLastData = GlobalUtil.findMatchingIdxByLastData(responseList, lastData);

        if (matchingIdxByLastData.isEmpty()) {
            matchingIdxByLastData = OptionalInt.of(1);
        }

        return GlobalUtil.getScrapingResult(responseList, matchingIdxByLastData);
    }

    private Elements findElement(Document document) {
        return document.select("tbody")
                .select(".common-list1,.common-list0");
    }

}
