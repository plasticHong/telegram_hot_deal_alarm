package com.plastic.scraper.app;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ScrapingResult {
    private String articleId;
    private String title;
    private String url;

    public ScrapingResult(String articleId, String title, String url) {
        this.articleId = articleId;
        this.title = title;
        this.url = url;
    }
}
