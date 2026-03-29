package com.example.prova2.service;

import java.net.URL;
import java.net.http.HttpClient;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.scene.web.WebEngine; 

public class MappaService2 {


    private static final String JSON_ITEMS_KEY = "items";
    private static final String JSON_POSITION_KEY = "position";
    private static final String JSON_LAT_KEY = "lat";
    private static final String JSON_LNG_KEY = "lng";

    private static final String API_KEY = "f2pO_B6aRFHQ46i-7ZqWjnhevwe_b3Cm255BB4V04SI";
    private static final String GEOCODE_URL = "https://geocode.search.hereapi.com/v1/geocode?q=%s&apiKey=%s";
    private static final HttpClient CLIENT = HttpClient.newHttpClient();
    //levato url per reverseGeoCoding non andava

    private static final Logger logger = Logger.getLogger(MappaService2.class.getName());

    private WebEngine webEngine;

    public void setWebEngine(WebEngine webEngine) {
        this.webEngine = webEngine;
    }

    public void loadMap() {
        URL url = getClass().getResource("/com/example/prova2/views/shared/loadMapSearch.html");
        if (url != null) {
            webEngine.load(url.toExternalForm());
        } else {
            logger.log(Level.SEVERE, "File HTML non trovato. Percorso: /com/example/prova2/views/shared/loadMapSearch.html");
        }
    }

    // Aggiorna il marker sulla mappa con le nuove coordinate
    public void updateMarker(double lat, double lng) {
        webEngine.executeScript("placeMarker(" + lat + ", " + lng + ");");
    }


    public void addMarker(double latComune, double lngComune, double lat, double lng, String titolo, String prezzo, String descrizione, String idImmobile) {
    
    // 1. Pulisci i testi: rimuove apici e "a capo" che fanno crashare JavaScript
    String titoloSicuro = titolo != null ? titolo.replace("'", "\\'").replace("\n", " ") : "";
    String descSicura = descrizione != null ? descrizione.replace("'", "\\'").replace("\n", " ") : "";
    String prezzoSicuro = prezzo != null ? prezzo.replace("'", "\\'") : "";
    String idSicuro = idImmobile != null ? idImmobile.replace("'", "\\'") : "";

    // 2. Costruisci lo script forzando Locale.US (obbliga Java a usare il PUNTO per i decimali, non la virgola)
    String script = String.format(java.util.Locale.US, 
        "addMarker(%f, %f, %f, %f, '%s', '%s', '%s', '%s');", 
        latComune, lngComune, lat, lng, titoloSicuro, prezzoSicuro, descSicura, idSicuro);

    // 3. Invia il comando a JavaScript ASSOLUTAMENTE tramite Platform.runLater (il thread grafico di JavaFX)
    javafx.application.Platform.runLater(() -> {
        try {
            // Stampa nel terminale esattamente cosa stiamo inviando alla mappa
            System.out.println("MAPPADEBUG - Invio comando: " + script);
            webEngine.executeScript(script);
        } catch (Exception e) {
            System.err.println("MAPPADEBUG - Errore JavaScript: " + e.getMessage());
        }
    });
}

    public void addMarker2(double latComune, double lngComune, double lat, double lng) {
        // Usiamo Locale.US per forzare il punto al posto della virgola per i decimali
        String script = String.format(java.util.Locale.US, 
            "addMarkerForAnnuncio(%f, %f, %f, %f);", 
            latComune, lngComune, lat, lng);

        javafx.application.Platform.runLater(() -> {
            // 1. Se la pagina ha GIÀ finito di caricare, esegui subito
            if (webEngine.getLoadWorker().getState() == javafx.concurrent.Worker.State.SUCCEEDED) {
                try {
                    System.out.println("MAPPADEBUG ANNUNCIO - Invio Pin: " + script);
                    webEngine.executeScript(script);
                } catch (Exception e) {
                    System.err.println("Errore Mappa Annuncio: " + e.getMessage());
                }
            } else {
                // 2. Altrimenti, mettiti in ascolto e ASPETTA che finisca di caricare
                webEngine.getLoadWorker().stateProperty().addListener((observable, oldValue, newValue) -> {
                    if (newValue == javafx.concurrent.Worker.State.SUCCEEDED) {
                        try {
                            System.out.println("MAPPADEBUG ANNUNCIO (Caricamento completato) - Invio Pin: " + script);
                            webEngine.executeScript(script);
                        } catch (Exception e) {
                            System.err.println("Errore Mappa Annuncio ritardato: " + e.getMessage());
                        }
                    }
                });
            }
        });
    }




    // Ottieni le coordinate dall'indirizzo
    public static String getCoordinatesFromAddress(String address) {
        try {
            // Prepara l'indirizzo e l'URL di HERE Maps
            String encodedAddress = java.net.URLEncoder.encode(address, java.nio.charset.StandardCharsets.UTF_8);
            String urlString = String.format(GEOCODE_URL, encodedAddress, API_KEY);

            java.net.http.HttpRequest request = java.net.http.HttpRequest.newBuilder()
                    .uri(java.net.URI.create(urlString))
                    .GET()
                    .build();

            // Invia la richiesta
            java.net.http.HttpResponse<String> response = CLIENT.send(request, java.net.http.HttpResponse.BodyHandlers.ofString());
            
            // Passa la risposta al metodo di parsing
            return parseCoordinatesFromResponse(response.body());
            
        } catch(Exception e) {
            System.err.println("Errore Geocoding HERE Maps: " + e.getMessage());
            return null;
        }
    }

    // --- PARSING DELLA RISPOSTA DI HERE MAPS ---
    private static String parseCoordinatesFromResponse(String jsonResponse) {
        try {
            if (jsonResponse == null || jsonResponse.isEmpty()) return null;
            
            org.json.JSONObject json = new org.json.JSONObject(jsonResponse);
            
            // HERE Maps restituisce un array chiamato "items"
            if (json.has("items") && !json.getJSONArray("items").isEmpty()) {
                org.json.JSONObject firstItem = json.getJSONArray("items").getJSONObject(0);
                
                // Le coordinate si trovano dentro l'oggetto "position"
                if (firstItem.has("position")) {
                    org.json.JSONObject position = firstItem.getJSONObject("position");
                    double lat = position.getDouble("lat");
                    double lng = position.getDouble("lng");
                    return lat + "," + lng; 
                }
            }
        } catch (Exception e) {
            // Se non trova le coordinate o c'è un errore, fallisce silenziosamente.
            // I nostri Controller intercetteranno il "null" e ripiegheranno sulla ricerca del Comune!
        }
        return null;
    }

}
