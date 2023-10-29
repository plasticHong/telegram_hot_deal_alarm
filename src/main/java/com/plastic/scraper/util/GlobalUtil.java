package com.plastic.scraper.util;

import com.plastic.scraper.dto.ScrapingResult;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.stream.IntStream;

public class GlobalUtil {

    public static Optional<ScrapingResult> getScrapingResult(List<ScrapingResult> responseList, OptionalInt matchingIdxByLastData) {
        int matchingIndex = matchingIdxByLastData.getAsInt();

        if (matchingIndex == 0) {
            return Optional.empty();
        }

        List<ScrapingResult> list = responseList.subList(0, matchingIndex).stream().toList();
        return Optional.ofNullable(list.get(list.size() - 1));
    }

    public static OptionalInt findMatchingIdxByLastData(List<ScrapingResult> responseList, String lastDataTitle) {

        if (lastDataTitle==null){
            return OptionalInt.of(0);
        }

        return IntStream.range(0, responseList.size())
                .filter(index -> responseList.get(index).getTitle().equals(lastDataTitle)).findFirst();
    }

    public static Document getDocumentByUrl(String url, Integer pageNum) {
        Document document;

        if (pageNum != 1) {
            url += pageNum;
        }

        try {
            document = Jsoup.connect(url).get();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return document;
    }

    public static Document getDocumentByUrl(String url) {
        Document document;

        try {
            document = Jsoup.connect(url).get();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return document;
    }

}
