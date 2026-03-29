package com.example.prova2.service;
import com.example.prova2.dto.AgenteDTO;
import com.example.prova2.factory.HttpRequestBuilderFactory;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class GoogleService {

    public static AgenteDTO exchangeCodeForToken(String code) {
        HttpClient client = HttpClient.newHttpClient();
        String url = "http://localhost:9094/auth/exchange_token?code=" + code;
        HttpRequest request = HttpRequestBuilderFactory.buildGET(url);
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode jsonResponse = objectMapper.readTree(response.body());
                return objectMapper.treeToValue(jsonResponse, AgenteDTO.class);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

}