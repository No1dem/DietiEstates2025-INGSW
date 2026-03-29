package com.example.prova2.service;

import com.example.prova2.dto.RecensioneDTO;
import com.example.prova2.factory.HttpRequestBuilderFactory;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class RecensioneService {

    private static final HttpClient client = HttpClient.newHttpClient();

    private RecensioneService(){}
    public static boolean updateRecensione(String agenteDaRecensire, int valutazione, String token) throws IOException, InterruptedException {

        RecensioneDTO recensione = new RecensioneDTO(agenteDaRecensire, valutazione);
        String baseUrl = "http://localhost:9094/cliente/lasciaRecensione";
        String query = stringBuildRecensione(recensione);
        System.out.println(query);
        System.out.println("Token usato: " + token);

        HttpRequest request = HttpRequestBuilderFactory.buildPOSTtoken(baseUrl,query,token);
        HttpResponse<String> response=client.send(request,HttpResponse.BodyHandlers.ofString());
        return response.statusCode()==200;
    }



    private static String stringBuildRecensione(RecensioneDTO recensione) {
        return String.format("""
                            {
                                  "agenteDaRecensire": "%s",
                                  "valutazione": "%d"
                            }
                        """,recensione.getAgenteDaRecensire(),recensione.getValutazione()
        );
    }
}




