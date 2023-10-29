package com.plastic.scraper.app.bean;

import com.plastic.scraper.app.ScrapingResult;
import lombok.RequiredArgsConstructor;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Component
@RequiredArgsConstructor
public class TelegramBot {


    @Value("${telegram.bot.token}")
    private String token;

    @Value("${telegram.bot.chat-id}")
    private String chatId;

    public void messageSend(ScrapingResult scrapingResult)  {

        String escapedUri = scrapingResult.getUrl().replace("&", "%26");
        String encodedMessage = URLEncoder.encode(scrapingResult.getTitle(), StandardCharsets.UTF_8);

        String apiUrl = "https://api.telegram.org/bot" +
                token + "/sendmessage?chat_id=" +
                chatId +
                "&text=" +
                encodedMessage+
                escapedUri;

        HttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(apiUrl);
        try {
            httpClient.execute(httpGet);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
