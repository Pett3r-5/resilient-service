package com.client.service.clientservice.services;

import com.client.service.clientservice.models.Response;
import io.github.resilience4j.bulkhead.Bulkhead;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.reactor.bulkhead.operator.BulkheadOperator;
import io.github.resilience4j.reactor.circuitbreaker.operator.CircuitBreakerOperator;
import lombok.Data;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@Data
public class ClientService {

    private WebClient webClient;
    private Bulkhead bulkhead;
    private CircuitBreaker circuitBreaker;


    public ClientService(WebClient webclient, Bulkhead bulkhead, CircuitBreaker circuitBreaker){
        this.webClient = webclient;
        this.bulkhead = bulkhead;
        this.circuitBreaker = circuitBreaker;
    }

    public Mono<Response> findById(String id){
        // you could also import resilience4J spring boot module and just use annotations, instead of these decorator functions
        return webClient.get()
                .uri("/product?id=" + id)
                .retrieve()
                .bodyToMono(Response.class)
                .transformDeferred(BulkheadOperator.of(bulkhead))
                .transformDeferred(CircuitBreakerOperator.of(circuitBreaker));
    }
}
