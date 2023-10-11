package com.plastic.scraper.bean;

import com.plastic.scraper.dto.ScrappingResponse;
import com.plastic.scraper.entity.RuliwebLastData;
import com.plastic.scraper.repository.RuliwebLastDataRepo;
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

@Component
@Log4j2
@RequiredArgsConstructor
public class RuliwebHotDealScrapper {

    private final static String URL = "https://bbs.ruliweb.com/market/board/1020?page=";

    private final RuliwebLastDataRepo ruliwebLastDataRepo;

    @Transactional
    public Optional<ScrappingResponse> doScrapping() {

        RuliwebLastData savedLastData = ruliwebLastDataRepo.findAll().stream().findFirst().orElse(new RuliwebLastData());

        Optional<ScrappingResponse> scrappingResponse = ruliwebScrappingAndFindNewArticle(savedLastData.getTitle());

        if (scrappingResponse.isEmpty()) {
            return Optional.empty();
        }

        ruliwebLastDataSave(scrappingResponse.get());

        return scrappingResponse;
    }

    private void ruliwebLastDataSave(ScrappingResponse scrapedLastData) {

        Optional<RuliwebLastData> byId = ruliwebLastDataRepo.findById(25L);

        if (byId.isEmpty()){return;}

        byId.get().setTitle(scrapedLastData.getTitle());
    }

    public Optional<ScrappingResponse> ruliwebScrappingAndFindNewArticle(String lastData) {

        List<ScrappingResponse> responseList;

        OptionalInt matchingIdxByLastData;

        int pageNum = 1;

        do {

            Document document = GlobalUtil.getDocumentByUrl(URL, pageNum);
            Elements aTags = findElement(document);

            responseList = aTags.stream()
                    .map(e -> new ScrappingResponse(e.text(), e.attr("href"))).toList();

            matchingIdxByLastData = GlobalUtil.findMatchingIdxByLastData(responseList, lastData);

            if (matchingIdxByLastData.isEmpty()) {
                log.info("일치하는 단어가 없습니다. 다음 페이지에서 다시 가조오세요");
            }

            pageNum += 1;

        } while (matchingIdxByLastData.isEmpty());

        int matchingIndex = matchingIdxByLastData.getAsInt();

        if (matchingIndex == 0) {
            return Optional.empty();
        }

        List<ScrappingResponse> list = responseList.subList(0, matchingIndex).stream().toList();
        return Optional.ofNullable(list.get(list.size() - 1));
    }

    private Elements findElement(Document document) {
        return document
                .select("tr:not(.inside)")
                .select("tr:not(.notice)")
                .select(".deco")
                .select("a");
    }

}
