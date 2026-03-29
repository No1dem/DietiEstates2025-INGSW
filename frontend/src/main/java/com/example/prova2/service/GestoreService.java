package com.example.prova2.service;
import com.example.prova2.dto.*;
import com.example.prova2.factory.HttpRequestBuilderFactory;
import com.example.prova2.model.GestoreAgenziaImmobiliare;
import com.example.prova2.model.Utente;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class GestoreService {
    private static String url = "http://localhost:9094/gestore";

    private static final HttpClient client = HttpClient.newHttpClient();

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static List<String> getAllCfGestore(String token) throws IOException, InterruptedException {
        return getAllGestore(url+"/cf",token);
    }

    public static List<String> getAllEmailGestore(String token) throws IOException, InterruptedException {
        return getAllGestore(url+"/email",token);
    }


    public static List<String> getAllGestore(String what,String token) throws IOException, InterruptedException {
        HttpRequest request = HttpRequestBuilderFactory.buildGetWithToken(what,token);
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if(response.statusCode() == 200) {
            try {
                List<String> cfs = objectMapper.readValue(response.body(), new TypeReference<List<String>>() {
                });
                if (!cfs.isEmpty()) {
                    return cfs;
                }
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                return new ArrayList<>();
            }
        }
        return new ArrayList<>();
    }

    public static GestoreAgenziaImmobiliareDatiDTO getGestoreByUsername(String username,String token){
        String url = "http://localhost:9094/gestore/dati?email="+username;
        HttpRequest request = HttpRequestBuilderFactory.buildGetWithToken(url,token);
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return objectMapper.readValue(response.body(), GestoreAgenziaImmobiliareDatiDTO.class);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static GestoreDTO addGestore(GestoreAgenziaImmobiliare g) throws IOException, InterruptedException {
        String url = "http://localhost:9094/gestore/add";
        GestoreDTO dto= makeString(g, url);
        System.out.println(dto.isDuplicatoCF());
        return dto;
    }

    static GestoreDTO makeString(GestoreAgenziaImmobiliare g, String url) throws IOException, InterruptedException {
        String jsonInputString = String.format("""
                {
                        "cfGestoreAdmin" : "%s",
                        "nomeAgenzia" : "Agenzia",
                        "password": "%s",
                        "email": "%s",
                        "nome": "%s",
                        "cognome": "%s",
                        "cf": "%s",
                        "telefono": "%s"
                }
            """, g.getAdminAppartenente().getCf()
                , g.getAccountAgente().getPassword(),g.getAccountAgente().getEmail(),g.getNome(),
                g.getCognome(),g.getCf(),g.getTelefono());
        HttpRequest addTodoRequest;
        addTodoRequest = HttpRequestBuilderFactory.buildPOSTtoken(url,jsonInputString,g.getToken());
        HttpResponse<String> addTodoResponse = GestoreService.client.send(addTodoRequest, HttpResponse.BodyHandlers.ofString());
        ObjectMapper objectMapper = new ObjectMapper();
        System.out.println(addTodoResponse.statusCode());
        return objectMapper.readValue(addTodoResponse.body(), GestoreDTO.class);
    }

    public static boolean checkPassword(String email, String password,String token) {
        String url = "http://localhost:9094/gestore/checkPassword";
        String jsonInputString = String.format("""
                {
                  "password": "%s",
                  "email": "%s"
                }
            """, password,email);
        HttpRequest addTodoRequest;
        addTodoRequest = HttpRequestBuilderFactory.buildPOSTtoken(url,jsonInputString,token);
        try {
            HttpResponse<String> addTodoResponse = client.send(addTodoRequest, HttpResponse.BodyHandlers.ofString());
            return addTodoResponse.statusCode()==200;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return false;
        } catch (IOException e) {
            return false;
        }
    }

    public static List<GetAllRicercheResponse> getAllRicerche(Utente utente){
        String url = "http://localhost:9094/gestore/ricerche/"+utente.getCf();
        return getGetAllRicercheResponses(utente, url, client, objectMapper);
    }

    static List<GetAllRicercheResponse> getGetAllRicercheResponses(Utente utente, String url, HttpClient client, ObjectMapper objectMapper) {
        HttpRequest request = HttpRequestBuilderFactory.buildGetWithToken(url,utente.getToken());
        try {
            HttpResponse<String> response= client.send(request, HttpResponse.BodyHandlers.ofString());
            if(response.body().isEmpty()){
                return Collections.emptyList();
            }
            return objectMapper.readValue(response.body(), new TypeReference<List<GetAllRicercheResponse>>(){});
        } catch (IOException e) {
            e.printStackTrace();
            return Collections.emptyList();
        } catch (InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
            return Collections.emptyList();
        }
    }

    public static UpdateDatiResponseDTO updateDatiGestore(GestoreAgenziaImmobiliare gestore, Utente utenteVecchio){
        String url = "http://localhost:9094/gestore/update?emailAttuale="+utenteVecchio.getAccountAgente().getEmail();
        String jsonInputString = String.format("""
                {
                    "nome" : "%s",
                    "cognome" : "%s",
                    "telefono" : "%s",
                    "email" : "%s"
                }
            """,gestore.getNome(),gestore.getCognome(),gestore.getTelefono(),gestore.getAccountAgente().getEmail());
        HttpRequest request = HttpRequestBuilderFactory.buildPOSTtoken(url,jsonInputString,utenteVecchio.getToken());
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return objectMapper.readValue(response.body(), UpdateDatiResponseDTO.class);
        } catch (IOException e) {
            return null;
        }
        catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return null;
        }
    }
}
