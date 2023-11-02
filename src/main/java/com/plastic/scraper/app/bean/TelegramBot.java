package com.plastic.scraper.app.bean;

import com.plastic.scraper.app.ScrapingResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Component
@RequiredArgsConstructor
@Log4j2
public class TelegramBot {


    @Value("${telegram.bot.token}")
    private String token;

    @Value("${telegram.bot.chat-id}")
    private String chatId;

    public void messageSend(ScrapingResult scrapingResult)  {

        String escapedUri = scrapingResult.getUrl().replace("&", "%26");
        String encodedMessage = URLEncoder.encode(scrapingResult.getTitle()+" ", StandardCharsets.UTF_8);

        System.out.println("Message = " + scrapingResult.getTitle());
        System.out.println("escapedUri = " + escapedUri);

        String apiUrl = "https://api.telegram.org/bot" +
                token + "/sendmessage?chat_id=" +
                chatId +
                "&text=" +
                encodedMessage+
                escapedUri;

        HttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(apiUrl);

        try {
            HttpResponse execute = httpClient.execute(httpGet);
            if (execute.getStatusLine().getStatusCode()!=200) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(execute.getEntity().getContent()));
                StringBuilder result = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }
                log.info("Response: [{}]", result);
            }
            Thread.sleep(1000);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
