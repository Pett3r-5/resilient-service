package com.client.service.clientservice.controllers;

import com.client.service.clientservice.models.Response;
import com.client.service.clientservice.services.ClientService;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import reactor.core.publisher.Mono;

@Controller
@RequestMapping("product")
public class ClientController {
    private final ClientService clientService;

    ClientController(ClientService clientService){
        this.clientService = clientService;
    }


    @GetMapping
    public Mono<Response> findById(@RequestParam("id") String id){
        return clientService.findById(id);
    }
}
