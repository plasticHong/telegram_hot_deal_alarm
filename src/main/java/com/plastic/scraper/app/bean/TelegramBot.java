package com.plastic.scraper.app.bean;

import lombok.RequiredArgsConstructor;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class TelegramBot {


    @Value("${telegram.bot.token}")
    private String token;

    @Value("${telegram.bot.chat-id}")
    private String chatId;

    public void messageSendWithApache(String rawUri)  {

        String replaced = rawUri.replace("&", "%26");

        String apiUrl = "https://api.telegram.org/bot" +
                token + "/sendmessage?chat_id=" +
                chatId +
                "&text=" +
                replaced;

        HttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(apiUrl);
        try {
            httpClient.execute(httpGet);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
