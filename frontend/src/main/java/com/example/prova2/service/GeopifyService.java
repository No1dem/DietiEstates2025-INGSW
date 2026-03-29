package com.example.prova2.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class GeopifyService {
    private static final String API_KEY = "312cc390dbf04ac8bb2a76e8e0c74103";

    public static List<String> searchLocation(String query) {
        ArrayList<String> suggerimenti = new ArrayList<>();
        try {
            String urlString = "https://api.geoapify.com/v1/geocode/autocomplete?text=" +
                    query + "&type=city&lang=it&apiKey=" + API_KEY;
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuilder content = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();
            conn.disconnect();

            // Parsing JSON
            JSONObject response = new JSONObject(content.toString());
            JSONArray features = response.getJSONArray("features");

            for (int i = 0; i < features.length(); i++) {
                JSONObject properties = features.getJSONObject(i).getJSONObject("properties");
                String fullLocation = properties.getString("formatted");

                // Estrai solo il nome della città (prima parte prima della virgola)
                String cityName = fullLocation.split(",")[0].trim();
                suggerimenti.add(cityName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return suggerimenti;
    }
}
