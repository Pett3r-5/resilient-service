package com.client.service.clientservice.configuration;

import io.github.resilience4j.bulkhead.Bulkhead;
import io.github.resilience4j.bulkhead.BulkheadConfig;
import io.github.resilience4j.bulkhead.BulkheadRegistry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BulkheadConfiguration {
    @Value("${resilience.bulkhead.maxConcurrentCalls}")
    private int maxConcurrency;

    @Bean
    Bulkhead bulkhead() {
        BulkheadConfig bulkheadConfig = BulkheadConfig.custom().maxConcurrentCalls(maxConcurrency).build();
        BulkheadRegistry bulkheadRegistry = BulkheadRegistry.of(bulkheadConfig);
        Bulkhead bulkhead = bulkheadRegistry.bulkhead("defaultBulkhead");
        return bulkhead;
    }
}
