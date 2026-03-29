package com.example.prova2.factory;

import java.net.URI;
import java.net.http.HttpRequest;

public class HttpRequestBuilderFactory {
    private HttpRequestBuilderFactory() {}

    public static HttpRequest buildPOST(final String BASE_URL, final String jsonInputString) {
        return HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL))
                .headers("Content-type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonInputString))
                .build();
    }

    public static HttpRequest buildDelete(final String BASE_URL) {
        return HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL))
                .headers("Content-type", "application/json")
                .DELETE().build();
    }

    public static HttpRequest buildPut(final String BASE_URL, final String jsonInputString) {
        return HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL))
                .headers("Content-type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(jsonInputString))
                .build();
    }

    public static HttpRequest buildGET(final String BASE_URL) {
        return HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL))
                .header("Content-Type", "application/json")
                .GET()
                .build();
    }

    public static HttpRequest buildGetWithToken(final String BASE_URL, final String token) {
        return HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + token)
                .GET()
                .build();
    }

    public static HttpRequest buildPOSTtoken(final String BASE_URL, final String jsonInputString,final String token) {
        return HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL))
                .header("Content-type", "application/json")
                .header("Authorization", "Bearer " + token)
                .POST(HttpRequest.BodyPublishers.ofString(jsonInputString))
                .build();
    }

    public static HttpRequest buildPUTtoken(final String BASE_URL, final String jsonInputString,final String token) {
        return HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL))
                .header("Content-type", "application/json")
                .header("Authorization", "Bearer " + token)
                .PUT(HttpRequest.BodyPublishers.ofString(jsonInputString))
                .build();
    }

}
