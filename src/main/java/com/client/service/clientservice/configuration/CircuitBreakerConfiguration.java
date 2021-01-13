package com.client.service.clientservice.configuration;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.micrometer.tagged.TaggedCircuitBreakerMetrics;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
@Slf4j
public class CircuitBreakerConfiguration {
    @Value("${resilience.circuitBreaker.failureThreshold}")
    private int FAILURE_THRESHOLD;

    @Value("${resilience.circuitBreaker.halfOpen.callsLimit}")
    private int CALLS_LIMIT;

    @Bean
    @PostConstruct
    CircuitBreaker circuitBreaker(){
        CircuitBreakerConfig config = CircuitBreakerConfig.custom()
                .failureRateThreshold(FAILURE_THRESHOLD)
                .permittedNumberOfCallsInHalfOpenState(CALLS_LIMIT)
                .build();

        CircuitBreakerRegistry circuitBreakerRegistry = CircuitBreakerRegistry.of(config);

        //monitoring with Micrometer
        MeterRegistry meterRegistry = new SimpleMeterRegistry();
        TaggedCircuitBreakerMetrics
                .ofCircuitBreakerRegistry(circuitBreakerRegistry)
                .bindTo(meterRegistry);


        CircuitBreaker circuitBreaker = circuitBreakerRegistry.circuitBreaker("Default");


        //logs for events
        circuitBreaker.getEventPublisher()
                .onFailureRateExceeded(e -> System.out.println("Circuit Breaker Failure Rate Exceeded:" + e));
        circuitBreaker.getEventPublisher()
                .onError(e -> System.out.println("Circuit Breaker Error:" + e));
        circuitBreaker.getEventPublisher()
                .onStateTransition(e -> System.out.println("Circuit Breaker transitioning state:" + e));
        circuitBreaker.getEventPublisher()
                .onReset(e -> System.out.println("Circuit Breaker resetting:" + e));

        return circuitBreaker;
    }
}
