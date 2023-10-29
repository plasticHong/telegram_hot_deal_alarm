package com.plastic.scraper.app;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
public class ScrapingResult {
    private String title;
    private String url;
}
