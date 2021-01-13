package com.client.service.clientservice.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {
    @Value("${clientBaseUrl}")
    private String CLIENT_BASE_URL;

    @Value("${fallbackBaseUrl}")
    private String FALLBACK_BASE_URL;

    @Bean
    public WebClient clientWebClient(){
        return WebClient.builder().baseUrl(CLIENT_BASE_URL).build();
    }

    @Bean
    public WebClient fallbackWebClient(){
        return WebClient.builder().baseUrl(FALLBACK_BASE_URL).build();
    }
}
