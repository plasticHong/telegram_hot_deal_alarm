package com.plastic.scraper.app.bean;

import lombok.RequiredArgsConstructor;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

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

    public void messageSend(String rawUri)  {

        String replaced = rawUri.replace("&", "%26");


        String apiUrl = "https://api.telegram.org/bot" +
                token + "/sendmessage?chat_id=" +
                chatId +
                "&text=" +
                rawUri;

        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<String> requestEntity = new HttpEntity<>(null);

        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(apiUrl);
        URI uri = builder.build(true).toUri();

        restTemplate.exchange(uri, HttpMethod.GET, requestEntity, String.class);
    }


}
