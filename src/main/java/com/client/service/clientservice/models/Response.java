package com.client.service.clientservice.models;

import lombok.Data;

@Data
public class Response {
    private String id;
    private String messasge;

    public Response(String id, String messasge) {
        this.id = id;
        this.messasge = messasge;
    }

    public Response(String messasge) {
        this.messasge = messasge;
    }
}
