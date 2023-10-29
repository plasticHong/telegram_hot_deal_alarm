package com.plastic.scraper.app.bean;

import com.plastic.scraper.dto.ScrapingResult;
import com.plastic.scraper.entity.FmKoreaLastData;
import com.plastic.scraper.entity.HotDealRecord;
import com.plastic.scraper.repository.FmKoreaLastDataRepo;
import com.plastic.scraper.repository.HotDealRecordRepo;
import com.plastic.scraper.util.GlobalUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.regex.Pattern;

@Component
@Log4j2
@RequiredArgsConstructor
public class FmKoreaHotDealScraper {

    private final static String URL = "https://www.fmkorea.com/index.php?mid=hotdeal&page=";

    private final FmKoreaLastDataRepo fmKoreaLastDataRepo;
    private final HotDealRecordRepo hotDealRecordRepo;


    @Transactional
    public Optional<ScrapingResult> doScraping() {

        FmKoreaLastData savedLastData = fmKoreaLastDataRepo.findAll().stream().findFirst().orElse(new FmKoreaLastData());

        Optional<ScrapingResult> scrapingResponse = fmKoreaScrapingAndFindNewArticle(savedLastData.getTitle());

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

        Optional<FmKoreaLastData> byId = fmKoreaLastDataRepo.findAll()
                .stream()
                .findFirst();

        if (byId.isEmpty()) {
            return;
        }

        byId.get().setTitle(scrapedLastData.getTitle());
    }

    private String getOriginalHotDealUrl(ScrapingResult scrapingResult) {

        String scrapedPageUrl = scrapingResult.getUrl();

        Elements select = GlobalUtil.getDocumentByUrl(scrapedPageUrl)
                .select(".xe_content")
                .select(".hotdeal_url");

        return select.attr("href");
    }


    private Optional<ScrapingResult> fmKoreaScrapingAndFindNewArticle(String lastData) {

        List<ScrapingResult> responseList;

        OptionalInt matchingIdxByLastData;

        int pageNum = 1;

        do {

            Document document = GlobalUtil.getDocumentByUrl(URL, pageNum);
            Elements aTags = findElement(document);

            String fmKoreaPreFix = "https://www.fmkorea.com";

            String pattern = "\\s\\[\\d+\\]";
            Pattern regex = Pattern.compile(pattern);

            responseList = aTags.stream()
                    .map(e -> new ScrapingResult(regex.matcher(e.text()).replaceAll("").trim(), fmKoreaPreFix + e.attr("href"))).toList();

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
                .select(".fm_best_widget")
                .select(".title")
                .select("a");
    }
}
