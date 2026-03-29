package com.example.prova2.service;

import com.example.prova2.dto.WeatherResponseDTO;
import com.example.prova2.factory.HttpRequestBuilderFactory;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class MeteoService {

    private static final HttpClient client = HttpClient.newHttpClient();

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static WeatherResponseDTO getWeather(Double lat, Double lon,String dataInizio,String dataFine) {
        String apiUrl = "https://api.open-meteo.com/v1/forecast?latitude=" + lat + "&longitude=" + lon +
                "&daily=temperature_2m_max,temperature_2m_min,precipitation_sum&hourly=temperature_2m,precipitation&start_date=" +dataInizio + "&end_date=" +dataFine + "&current_weather=true" ;
        HttpRequest request = HttpRequestBuilderFactory.buildGET(apiUrl);
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println(response.body());
            return objectMapper.readValue(response.body(), WeatherResponseDTO.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }
}
