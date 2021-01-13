package com.client.service.clientservice.services;

import com.client.service.clientservice.models.Response;
import io.github.resilience4j.bulkhead.Bulkhead;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.decorators.Decorators;
import lombok.Data;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Service
@Data
public class ClientService {


    private WebClient clientWebClient;
    private WebClient fallbackWebClient;
    private Bulkhead bulkhead;
    private CircuitBreaker circuitBreaker;


    public ClientService(@Qualifier("clientWebClient") WebClient clientWebClient,
                         @Qualifier("fallbackWebClient") WebClient fallbackWebClient,
                         Bulkhead bulkhead, CircuitBreaker circuitBreaker) {
        this.clientWebClient = clientWebClient;
        this.fallbackWebClient = fallbackWebClient;
        this.bulkhead = bulkhead;
        this.circuitBreaker = circuitBreaker;
    }

    public Mono<Response> fallbackMessage(String id) {
        return Decorators.ofSupplier(()->fallbackWebClient.get()
                .uri("/product?id=")
                .retrieve()
                .bodyToMono(Map.class)
                .switchIfEmpty(Mono.just(new HashMap()))
                .flatMap(res->{
                    return Mono.just(new Response(id,"Fallback mocked message"));
                }))
                .withBulkhead(bulkhead)
                .get();
    }

    public Mono<Response> findById(String id){
        // you could also import resilience4J spring boot module and just use annotations, instead of these decorator functions
        return Decorators.ofSupplier(()->clientWebClient.get()
                .uri("/product?id=" + id)
                .retrieve()
                .bodyToMono(Response.class))
        .withBulkhead(bulkhead)
        .withCircuitBreaker(circuitBreaker)
        .withFallback(Arrays.asList(Exception.class), e-> fallbackMessage(id)).get();
    }


}
