package com.example.prova2.service;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.example.prova2.factory.HttpRequestBuilderFactory;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;


public class ImmagineService {

    private ImmagineService(){}

    private static final HttpClient client = HttpClient.newHttpClient();
    private static ObjectMapper objectMapper = new ObjectMapper();



    public static CompletableFuture<List<String>> getFotoOfImmobile(int idImmobile) {
        String url = "http://localhost:9094/image/getImmagineByIdImmobile?idImmobile=" + idImmobile;
        HttpRequest request = HttpRequestBuilderFactory.buildGET(url);

        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(response -> {
                    if (response.statusCode() == 200) {
                        try {
                            return objectMapper.readValue(response.body(), new TypeReference<List<String>>() {});
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    return Collections.<String>emptyList();
                })
                .exceptionally(ex -> {
                    ex.printStackTrace();
                    return Collections.<String>emptyList();
                });
    }




}
