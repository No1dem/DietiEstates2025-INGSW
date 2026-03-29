package com.example.prova2.service;
import com.example.prova2.dto.AgenteDTO;
import com.example.prova2.factory.HttpRequestBuilderFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;


public class AutenticazioneService {

    private static final HttpClient client = HttpClient.newHttpClient();

    private AutenticazioneService(){}

    private static final String queryJson = """
                {
                     "email" : "%s",
                     "password" : "%s"
                }
            """;


    private static final String URL= "http://localhost:9094/auth";

    private static final ObjectMapper objectMapper = new ObjectMapper();


    public static AgenteDTO requestLogin(String email, String password) throws InterruptedException, IOException {
        String jsonInputString = String.format(queryJson, email,password);
        HttpRequest addTodoRequest = HttpRequestBuilderFactory.buildPOST(URL,jsonInputString);
        HttpResponse<String> response = client.send(addTodoRequest, HttpResponse.BodyHandlers.ofString());
        return objectMapper.readValue(response.body(), AgenteDTO.class);
    }




}
