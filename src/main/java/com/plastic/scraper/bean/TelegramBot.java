package com.plastic.scraper.bean;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class TelegramBot {


    @Value("${telegram.bot.token}")
    private String token;

    @Value("${telegram.bot.chat-id}")
    private String chatId;


    public void messageSend(String message) {

        String url = "https://api.telegram.org/bot" +
                token + "/sendmessage?chat_id=" +
                chatId + "&text=" +
                message;

        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<String> requestEntity = new HttpEntity<>(null);

        restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class);
    }


}
