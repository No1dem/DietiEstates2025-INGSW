package com.example.prova2.service;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javafx.application.Platform;
import javafx.scene.control.ListView;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class RicercaComuniService {

    private RicercaComuniService(){}

    public static void getSuggestions(String query, ListView<String> suggestionsList) {
        HttpClient client = HttpClient.newHttpClient();
        String url = "https://nominatim.openstreetmap.org/search?format=json&q=" + query;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("User-Agent", "JavaFX-Autocomplete/1.0")
                .build();

        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(response -> {
                    Platform.runLater(() -> {
                        JsonArray results = JsonParser.parseString(response).getAsJsonArray();
                        suggestionsList.getItems().clear();
                        for (JsonElement element : results) {
                            Platform.runLater(()->{JsonObject location = element.getAsJsonObject();
                                String displayName = location.get("display_name").getAsString();
                                suggestionsList.getItems().add(displayName);});
                        }
                    });

                })
                .exceptionally(e -> {
                    e.printStackTrace();
                    return null;
                });
        client.close();
    }
}
