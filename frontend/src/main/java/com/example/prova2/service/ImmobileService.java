package com.example.prova2.service;

import com.example.prova2.dto.*;
import com.example.prova2.factory.HttpRequestBuilderFactory;
import com.example.prova2.model.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class ImmobileService {

    private ImmobileService() {}

    private static final HttpClient client = HttpClient.newHttpClient();
    private static final ObjectMapper objectMapper = new ObjectMapper();


    public static List<ImmobileResponseRicercaDTO> getAnnunciForAgente(String mail,String token) throws IOException, InterruptedException {
        String baseUrl = "http://localhost:9094/immobile/AnnunciAgente?mail=" + mail;
        HttpRequest request = HttpRequestBuilderFactory.buildGetWithToken(baseUrl,token);
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response);
        if (response.statusCode() == 200) {
            try {
                List<ImmobileResponseRicercaDTO> immobili = objectMapper.readValue(response.body(), new TypeReference<List<ImmobileResponseRicercaDTO>>() {
                });
                if (!immobili.isEmpty()) {
                    return immobili;
                }
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                return new ArrayList<>();
            }
        }
        return new ArrayList<>();
    }



    public static boolean addAnnuncio(AddImmobileRequestDTO annuncio, String token) throws IOException, InterruptedException {
        formattazioni result = getFormattazioni(annuncio);
        Locale.setDefault(Locale.US);
        String jsonInputString = String.format("""
                        {
                            "idImmobile": %d,
                            "tipoimmobile": "%s",
                            "tipovendita": "%s",
                            "descrizione": "%s",
                            "via": "%s",
                            "numeroStanze": %d,
                            "piano": %d,
                            "classeEnergetica": "%s",
                            "superficie": %d,
                            "arredamento": "%s",
                            "prezzo": %s,
                            "speseCondominiali": %s,
                            "numeroBagni": %d,
                            "numeroCucine": %d,
                            "numeroSoggiorni": %d,
                            "cfAgente": "%s",
                            "titolo": "%s",
                            "longitudine": %f,
                            "latitudine": %f,
                            "citta": "%s",
                            "comune": "%s",
                            "numeroCivico": "%s",
                            "fotoDelImmobile": %s,
                            "ascensore" : "%s"
                        }
                        """, annuncio.getIdImmobile(), annuncio.getTipoimmobile(), annuncio.getTipovendita(), annuncio.getDescrizione(), annuncio.getVia(), annuncio.getNumeroStanze(),
                annuncio.getPiano(), annuncio.getClasseEnergetica(), annuncio.getSuperficie(), annuncio.getArredamento().getValue() , annuncio.getPrezzo().toPlainString(), annuncio.getSpeseCondominiali().toPlainString(),
                annuncio.getNumeroBagni(), annuncio.getNumeroCucine(), annuncio.getNumeroSoggiorni(), annuncio.getCfAgente(), annuncio.getTitolo(), result.lat(), result.lon(),
                annuncio.getCitta(), annuncio.getComune(), annuncio.getNumeroCivico(), result.output(),annuncio.isAscensore()
        );
        String baseUrl = "http://localhost:9094/immobile/add";
        HttpRequest addTodoRequest;
        addTodoRequest = HttpRequestBuilderFactory.buildPOSTtoken(baseUrl,jsonInputString,token);
        HttpResponse<String> addTodoResponse = client.send(addTodoRequest, HttpResponse.BodyHandlers.ofString());
        return addTodoResponse.statusCode()==200;
    }

    private static formattazioni getFormattazioni(AddImmobileRequestDTO annuncio) {
        float prezzo = Float.parseFloat(String.format(Locale.US, "%.2f", annuncio.getPrezzo()));
        float spese = Float.parseFloat(String.format(Locale.US, "%.2f", annuncio.getSpeseCondominiali()));
        float lat = Float.parseFloat(String.format(Locale.US, "%.6f", annuncio.getLatitudine())); // più precisione per le coordinate
        float lon = Float.parseFloat(String.format(Locale.US, "%.6f", annuncio.getLongitudine()));

        String input = String.valueOf(annuncio.getUriFotoImmobile());
        input = input.replaceAll("[\\[\\]]", "");
        String[] paths = input.split(",\\s*");
        StringBuilder output = new StringBuilder("[");
        for (int i = 0; i < paths.length; i++) {
            String path = paths[i].replace("\\", "/").trim(); // Trim rimuove spazi indesiderati
            output.append("\"").append(path).append("\"");
            if (i < paths.length - 1) {
                output.append(", "); // Aggiunge la virgola tra gli URL
            }
        }
        output.append("]");
        return new formattazioni(prezzo, spese, lat, lon, output);
    }

    private record formattazioni(float prezzo, float spese, float lat, float lon, StringBuilder output) {
    }


    public static List<ImmobileResponseRicercaDTO> searchAnnunci(Ricerca ricerca, Utente utente) {
        String baseUrl = "http://localhost:9094/immobile/searchImmobile";
        String query = buildStringAnnunci(ricerca.getComune(),ricerca.getTipoVendita(), ricerca.getPrezzoMinimo(),
                ricerca.getPrezzoMaximo(), ricerca.getNumeroStanze(), ricerca.getClasseEnergetica(),utente, ricerca.getSessioneUtente());
        HttpRequest request = HttpRequestBuilderFactory.buildPOSTtoken(baseUrl, query, utente.getToken());
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (rispostaEsatta(response)) return getImmobileResponseRicercaDTOS(response);
        } catch (IOException e) {
            return Collections.emptyList();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return Collections.emptyList();
        }
        return new ArrayList<>();
    }

    public static boolean deleteImmobile(int id){
        String baseUrl="http://localhost:9094/immobile/deleteImmobile?id="+id;
        HttpRequest request = HttpRequestBuilderFactory.buildDelete(baseUrl);
        try{
            HttpResponse<String> response = client.send(request,HttpResponse.BodyHandlers.ofString());
            if(rispostaEsatta(response)) return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            e.printStackTrace();
            return false;
        }
        return false;
    }

    private static boolean rispostaEsatta(HttpResponse<String> response) {
        return response.statusCode() == 200;
    }

    private static List<ImmobileResponseRicercaDTO> getImmobileResponseRicercaDTOS(HttpResponse<String> response) {
        try {
            return objectMapper.readValue(response.body(), new TypeReference<List<ImmobileResponseRicercaDTO>>() {
            });
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }



    private static String buildStringAnnunci(String comune, TipoVendita tv , BigDecimal prezzoMin, BigDecimal prezzoMax, Integer numStanze, ClasseEnergetica ce, Utente utente, int sessioneUtente) {
        String utenteDaMettere = switch (sessioneUtente) {
            case 0, 1 -> utente.getCf();
            case 2 -> utente.getAccountAgente().getEmail();
            default -> null;
        };
        return String.format("""
                            {
                                "tipologiaVendita":%s,
                                "prezzoMinimo": %s ,
                                "prezzoMaximo": %s,
                                "numeroStanze": %s,
                                "classeEnergetica": %s,
                                "comune": %s,
                                "citta": null,
                                "utenteCheRicerca" : "%s",
                                "sessioneUtente": "%s"
                            }
                        """, tv != null ? "\"" + tv + "\"" : "null",
                (prezzoMin != null && prezzoMin.compareTo(BigDecimal.ZERO) > 0) ? prezzoMin.toPlainString() : "null",
                (prezzoMax != null && prezzoMax.compareTo(BigDecimal.ZERO) > 0) ? prezzoMax.toPlainString() : "null",
                (numStanze != null && numStanze > 0) ? numStanze : "null",
                ce != null ? "\"" + ce + "\"" : "null",
                comune != null ? "\"" + comune + "\"" : "null",utenteDaMettere,sessioneUtente
                );
    }

    public static VisitaResponseDTO addVisita(AddVisitaRequestDTO visita){
        String baseUrl = "http://localhost:9094/visita/addVisita";
        String query=buildStringVisita(visita);
        HttpRequest request = HttpRequestBuilderFactory.buildPOSTtoken(baseUrl,query,visita.getToken());
        try{
            HttpResponse<String> response=client.send(request,HttpResponse.BodyHandlers.ofString());
            return objectMapper.readValue(response.body(),VisitaResponseDTO.class);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static DatiImmobileDTO getInfo(int idImmobile,String token){
        String baseUrl = "http://localhost:9094/immobile/getInfoAboutImmobile?idImmobile="+idImmobile;
        HttpRequest request = HttpRequestBuilderFactory.buildGetWithToken(baseUrl,token);
        try{
            HttpResponse<String> response=client.send(request,HttpResponse.BodyHandlers.ofString());
            return objectMapper.readValue(response.body(),DatiImmobileDTO.class);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ImmobileResponseRicercaDTO getInfoById(int idImmobile, String token) throws IOException, InterruptedException {
        String baseUrl = "http://localhost:9094/immobile/getInfoAboutImmobileById?idImmobile=" + idImmobile;
        HttpRequest request = HttpRequestBuilderFactory.buildGetWithToken(baseUrl, token);

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // Stampa lo status code e il body della risposta
        System.out.println("HTTP Response Code: " + response.statusCode());
        System.out.println("HTTP Response Body: " + response.body());

        if (rispostaEsatta(response)) {
            return getImmobileResponseRicercaDTOSCliente(response);
        }

        return null;  // Restituisce null in caso di errore o risposta non valida
    }


    private static ImmobileResponseRicercaDTO getImmobileResponseRicercaDTOSCliente(HttpResponse<String> response) {
        try {
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

            // Stampa il contenuto prima di provare a deserializzare
            System.out.println("Parsing JSON Response: " + response.body());

            return objectMapper.readValue(response.body(), ImmobileResponseRicercaDTO.class);
        } catch (Exception e) {
            System.err.println("Errore durante la deserializzazione: " + e.getMessage());
            e.printStackTrace();
            return null; // Restituisce null in caso di errore
        }
    }






    private static String buildStringVisita(AddVisitaRequestDTO request) {
        return String.format("""
                            {
                                  "dataVisita": "%s",
                                  "horaInizioVisita": "%s",
                                  "horaFineVisita": "%s",
                                  "descrizione": "Ciao vorrei prenotare questo immobile!",
                                  "emailClienteChePrenotaVisita": "%s",
                                  "cfAgente": "%s",
                                  "immobileDaVisitare": "%d"
                            }
                        """,request.getDataVisita(),request.getHoraInizioVisita(),request.getHoraFineVisita(),
                request.getEmailClienteChePrenotaVisita(),request.getCfAgente(),request.getImmobileDaVisitare()
                );
    }
}
