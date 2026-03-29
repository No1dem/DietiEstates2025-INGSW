package com.example.prova2.service;
import com.example.prova2.dto.ClienteDTO;
import com.example.prova2.dto.ClienteDatiDTO;
import com.example.prova2.dto.GetAllRicercheResponse;
import com.example.prova2.dto.UpdateDatiResponseDTO;
import com.example.prova2.factory.AlertFactory;
import com.example.prova2.factory.HttpRequestBuilderFactory;
import com.example.prova2.model.Cliente;
import com.example.prova2.model.Utente;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static com.example.prova2.service.GestoreService.getGetAllRicercheResponses;

public class ClienteService {

    private static final HttpClient client = HttpClient.newHttpClient();

    private ClienteService() {
    }


    private static final ObjectMapper objectMapper = new ObjectMapper();


    public static ClienteDTO registrati(Cliente cliente) {
        String query = buildString(cliente);
        String url = "http://localhost:9094/cliente/add";
        HttpRequest request = HttpRequestBuilderFactory.buildPOST(url, query);
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return objectMapper.readValue(response.body(), ClienteDTO.class);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return null;
        } catch (IOException e) {
            return null;
        }
    }

    public static ClienteDatiDTO getDatiCliente(String email,String token){
        String url = "http://localhost:9094/cliente/getUtente?email="+email;
        HttpRequest request = HttpRequestBuilderFactory.buildGetWithToken(url,token);
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println(response.body());
            return objectMapper.readValue(response.body(), ClienteDatiDTO.class);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return null;
        } catch (IOException e) {
            return null;
        }
    }

    public static UpdateDatiResponseDTO updateDatiCliente(Cliente cliente,Utente utenteVecchio){
        String url = "http://localhost:9094/cliente/update?emailAttuale="+utenteVecchio.getAccountAgente().getEmail();
        String jsonInputString = String.format("""
                {
                    "nome" : "%s",
                    "cognome" : "%s",
                    "telefono" : "3664398509",
                    "email" : "%s"
                }
            """,cliente.getNome(),cliente.getCognome(),cliente.getAccountAgente().getEmail());
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

    static CompletableFuture<UpdateDatiResponseDTO> getDati(String url, String jsonInputString, HttpClient client,
                                                            ObjectMapper objectMapper,String token) {
        HttpRequest request = HttpRequestBuilderFactory.buildPOSTtoken(url,jsonInputString,token);
        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(response -> {
                    try {
                        response.body();
                        response.statusCode();
                        UpdateDatiResponseDTO response1= objectMapper.readValue(response.body(), UpdateDatiResponseDTO.class);
                        if(response1.getDuplicato()){
                            AlertFactory.creaAlertErroreEmailRegistrata();
                            return null;
                        }
                        if(response1.getErroreInterno()){
                            AlertFactory.creaAlertErroreEmailRegistrata();
                            return null;
                        }
                        return response1;
                    } catch (Exception e) {
                        e.printStackTrace();
                        return null;
                    }
                });
    }

    private static String buildString(Cliente c) {
        return String.format("""
                            {
                                "email": "%s",
                                "password": "%s",
                                "nome": "%s",
                                "cognome": "%s",
                                "telefono": "%s"
                            }
                        """, c.getAccountAgente().getEmail(), c.getAccountAgente().getPassword(), c.getNome(), c.getCognome(),
                c.getTelefono());
    }

    public static List<GetAllRicercheResponse> getAllRicerche(Utente utente){
        String url = "http://localhost:9094/cliente/getRicerche?email="+utente.getAccountAgente().getEmail();
        return getGetAllRicercheResponses(utente, url, client, objectMapper);
    }
}