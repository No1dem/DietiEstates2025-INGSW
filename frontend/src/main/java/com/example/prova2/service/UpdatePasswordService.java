package com.example.prova2.service;
import com.example.prova2.model.Agente;
import com.example.prova2.model.Cliente;
import com.example.prova2.model.GestoreAgenziaImmobiliare;
import com.example.prova2.factory.AlertFactory;
import com.example.prova2.factory.HttpRequestBuilderFactory;
import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class UpdatePasswordService {
    private UpdatePasswordService(){}



    private static final HttpClient client = HttpClient.newHttpClient();

    public static boolean updatePasswordGestore(String nuovaPass, GestoreAgenziaImmobiliare gestore) {
        String url = "http://localhost:9094/admin/update";
        String jsonInputString = String.format("""
                {
                   "password": "%s",
                    "email": "%s"
                }
            """, nuovaPass,gestore.getAccountAgente().getEmail());
        return update(url, jsonInputString, gestore.getToken());
    }

    public static boolean updatePasswordAgente(String nuovaPass, Agente agente) {
        String url = "http://localhost:9094/agente/updatePassword/agente";
        String jsonInputString = String.format("""
                {
                   "password": "%s",
                    "email": "%s"
                }
            """, nuovaPass,agente.getAccountAgente().getEmail());
        return update(url, jsonInputString, agente.getToken());
    }

    public static boolean updatePasswordCliente(String nuovaPass, Cliente cliente) {
        String url = "http://localhost:9094/cliente/updatePassword";
        String jsonInputString = String.format("""
                {
                   "password": "%s",
                    "email": "%s"
                }
            """, nuovaPass,cliente.getAccountAgente().getEmail());
        return update(url, jsonInputString, cliente.getToken());
    }

    private static boolean update(String url, String jsonInputString, String token) {
        System.out.println(token);
        HttpRequest addTodoRequest;
        addTodoRequest = HttpRequestBuilderFactory.buildPOSTtoken(url,jsonInputString, token);
        try {
            HttpResponse<String> addTodoResponse = client.send(addTodoRequest, HttpResponse.BodyHandlers.ofString());
            System.out.println(addTodoResponse);
            return addTodoResponse.body().equals("true");
        }
        catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            AlertFactory.creaAlertErrore("Errore","Errore nella richiesta");
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            AlertFactory.creaAlertErrore("Errore","Errore nella richiesta");
            return false;
        }
    }
}
