package com.example.prova2.service;
import com.example.prova2.dto.GetNotificheDTO;
import com.example.prova2.dto.NotificaDTO;
import com.example.prova2.factory.AlertFactory;
import com.example.prova2.model.*;
import com.example.prova2.factory.HttpRequestBuilderFactory;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.example.prova2.service.AgenteService.mandaRichiesta;
import static com.example.prova2.service.AgenteService.rispostaAndataBuonFine;

public class NotificaService {

    private NotificaService(){}

    private static final HttpClient client = HttpClient.newHttpClient();
    private static ObjectMapper objectMapper = new ObjectMapper(); //per deserializzare la notifica


    public static List<GetNotificheDTO> getNotificationsForAdmin(GestoreAgenziaImmobiliare g) throws IOException, InterruptedException {
        String baseUrl="http://localhost:9094/not?cf="+g.getCf();
        return getNotifiche(baseUrl,g.getToken());
    }

    public static List<GetNotificheDTO> getNotificationsForAgente(String cf,String token) throws IOException, InterruptedException {
        String baseUrl="http://localhost:9094/not/agente?cf="+cf;
        return getNotifiche(baseUrl,token);
    }

    public static List<GetNotificheDTO> getNotificationsForCliente(Cliente cliente) throws IOException, InterruptedException {
        String baseUrl="http://localhost:9094/not/cliente?email="+cliente.getAccountAgente().getEmail();
        return getNotifiche(baseUrl,cliente.getToken());
    }

    private static List<GetNotificheDTO> getNotifiche(String baseUrl, String token) throws IOException, InterruptedException {
        HttpRequest request = HttpRequestBuilderFactory.buildGetWithToken(baseUrl,token);
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response);
        if(rispostaAndataABuonFine(response)) {
            try {
                List<GetNotificheDTO> notifiche = objectMapper.readValue(response.body(), new TypeReference<List<GetNotificheDTO>>() {});
                if (nonCiSonoNotifiche(notifiche)) {
                    return notifiche;
                }
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        return new ArrayList<>();
    }


    public static List<CategoriaNotifica> getCategorieDisattivate(Agente agente){
        String url = "http://localhost:9094/not/categorieDisattivateAgente?cf="+agente.getCf();
        return getCategoriaNotifiche(url,agente.getToken());
    }

    public static List<CategoriaNotifica> getCategorieDisattivateCliente(Cliente cliente){
        String url = "http://localhost:9094/not/categorieDisattivateCliente?email="+cliente.getAccountAgente().getEmail();
        return getCategoriaNotifiche(url,cliente.getToken());
    }

    private static List<CategoriaNotifica> getCategoriaNotifiche(String url,String token) {
        HttpRequest request = HttpRequestBuilderFactory.buildGetWithToken(url,token);
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (rispostaAndataABuonFine(response)) {
                return objectMapper.readValue(response.body(), new TypeReference<List<CategoriaNotifica>>() {
                });
            }
        }catch (IOException e){
            AlertFactory.creaAlertErroreConnessione();
            e.printStackTrace();
        }
        catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            AlertFactory.creaAlertErroreConnessione();
            e.printStackTrace();
        }
        return Collections.emptyList();
    }


    private static boolean rispostaAndataABuonFine(HttpResponse<String> response) {
        return response.statusCode() == 200;
    }

    private static boolean nonCiSonoNotifiche(List<GetNotificheDTO> notifiche) {
        return !notifiche.isEmpty();
    }


    public static void setNotificationAccepted(int idNotifica,String token) {
        String url = "http://localhost:9094/not";
        setNotAcc(url + "/" + idNotifica + "/accetta",token);
    }

    public static void setNotificationRejected(int idNotifica,String token) {
        String url = "http://localhost:9094/not";
        setNotRej(url + "/" + idNotifica + "/elimina",token);
    }

    public static void setNotAcc(String url,String token) {
        HttpRequest request = HttpRequestBuilderFactory.buildPUTtoken(url,HttpRequest.BodyPublishers.noBody().toString(), token);
        mandRichiestaNotifica(request);
    }

    public static void setNotRej(String url,String token) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .DELETE()
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer "+token)
                .build();
        mandRichiestaNotifica(request);
    }

    private static NotificaDTO mandRichiestaNotifica(HttpRequest request) {
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return objectMapper.readValue(response.body(),NotificaDTO.class);
        }catch (IOException e) {
            return null;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return null;
        }
    }


    public static List<CategoriaNotifica> getCategorieDisponibiliCliente(String email,String token){
        String url = "http://localhost:9094/not/categorieCliente?email="+email;
        return getCategoriaNotifiche(url,token);
    }

    public static List<CategoriaNotifica> getCategorieDisponibiliAgente(String cf,String token){
        String url = "http://localhost:9094/not/categorieAgente?cf="+cf;
        return getCategoriaNotifiche(url,token);
    }


    public static boolean disattivaCategoriaNotificaPerCliente(String categoriaDaDisattivare,String email,String token){
        String url = "http://localhost:9094/not/disattivaCategoria/cliente?email="+email;
        String jsonInputString = String.format("""
                {
                   "categoriaNotifica" : "%s"
                }
            """,categoriaDaDisattivare);
        return getResponse(url, jsonInputString,token);
    }

    public static boolean attivaCategoriaNotificaPerCliente(String categoriaDaDisattivare,String email,String token){
        String url = "http://localhost:9094/not/attivaCategoria/cliente?email="+email;
        String jsonInputString = String.format("""
                {
                   "categoriaNotifica" : "%s"
                }
            """,categoriaDaDisattivare);
        return getResponse(url, jsonInputString,token);
    }



    public static boolean disattivaCategoriaNotificaPerAgente(String categoriaDaDisattivare,String cf,String token){
        String url = "http://localhost:9094/not/disattivaCategoria/agente?cf="+cf;
        String jsonInputString = String.format("""
                {
                   "categoriaNotifica" : "%s"
                }
            """,categoriaDaDisattivare);
        return getResponse(url, jsonInputString,token);
    }

    public static boolean attivaCategoriaNotificaPerAgente(String categoriaDaDisattivare,String cf,String token){
        String url = "http://localhost:9094/not/attivaCategoria/agente?cf="+cf;
        String jsonInputString = String.format("""
                {
                   "categoriaNotifica" : "%s"
                }
            """,categoriaDaDisattivare);
        return getResponse(url, jsonInputString,token);
    }

    public static boolean getResponse(String url, String jsonInputString,String token) {
        HttpRequest request = HttpRequestBuilderFactory.buildPOSTtoken(url,jsonInputString,token);
        try {
            HttpResponse<String> response = mandaRichiesta(request);
            return rispostaAndataBuonFine(response);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return false;
        }
        catch (IOException e) {
            return false;
        }
    }
}
