package com.plastic.scraper.app.util;

import com.plastic.scraper.app.ScrapingResult;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.hibernate.mapping.Join;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

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
                .filter(index -> responseList.get(index).getArticleId().equals(lastDataTitle)).findFirst();
    }


    public static Document getDocumentByUrl(String url) {
        Document document;

        if (url.contains("fmkorea")){

            WebDriverManager.chromedriver().setup();
            WebDriver driver = new ChromeDriver();

            driver.get(url);

            String pageSource = driver.getPageSource();
            driver.quit();

            return Jsoup.parse(pageSource);

        }

        try {
            document = Jsoup.connect(url).get();
        }catch (IOException e) {
            throw new RuntimeException(e);
        }
        return document;
    }

}
