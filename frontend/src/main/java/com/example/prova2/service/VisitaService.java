package com.example.prova2.service;
import com.example.prova2.dto.AddVisitaRequestDTO;
import com.example.prova2.dto.GetVisiteResponse;
import com.example.prova2.dto.InformazioniVisitaDTO;
import com.example.prova2.factory.HttpRequestBuilderFactory;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Collections;
import java.util.List;

public class VisitaService {

    private static final HttpClient client = HttpClient.newHttpClient();

    private static ObjectMapper objectMapper = new ObjectMapper();

    public static List<GetVisiteResponse> getVisiteOfAgente(String cf,String token){
        objectMapper.registerModule(new JavaTimeModule());
        List<GetVisiteResponse> visitas;
        String url = "http://localhost:9094/visita/getVisiteOf?cf="+cf;
        HttpRequest request = HttpRequestBuilderFactory.buildGetWithToken(url,token);
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println(response);
            visitas = objectMapper.readValue(response.body(), new TypeReference<List<GetVisiteResponse>>(){});
            return visitas;
        }catch (InterruptedException e){
            e.printStackTrace();
            Thread.currentThread().interrupt();
            return Collections.emptyList();
        }catch (IOException e){
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    public static InformazioniVisitaDTO getInfoAboutVisita(int idNotifica,String token){
        objectMapper.registerModule(new JavaTimeModule());
        String url = "http://localhost:9094/visita/getInfoAboutVisita?idNotifica="+idNotifica;
        HttpRequest request = HttpRequestBuilderFactory.buildGetWithToken(url,token);
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println(response.body());
            return objectMapper.readValue(response.body(),InformazioniVisitaDTO.class);
        }catch (InterruptedException e){
            e.printStackTrace();
            Thread.currentThread().interrupt();
            return null;
        }catch (IOException e){
            e.printStackTrace();
            return null;
        }
    }

    public static boolean annullaInvioVisita(int idNotifica,String token){
        String url = "http://localhost:9094/not/"+idNotifica+"/annullaInvioNotifica";
        HttpRequest request = HttpRequestBuilderFactory.buildPUTtoken(url,"",token);
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println(response.body());
            return response.statusCode() == 200;
        }catch (InterruptedException e){
            e.printStackTrace();
            Thread.currentThread().interrupt();
            return false;
        }catch (IOException e){
            e.printStackTrace();
            return false;
        }
    }

    public static boolean checkVisitaClienteGiaPresente(String email,int idImmobile,String token){
        String url = "http://localhost:9094/visita/checkVisita/"+idImmobile+"?email="+email;
        HttpRequest request = HttpRequestBuilderFactory.buildGetWithToken(url,token);
        try{
            HttpResponse<String> response=client.send(request,HttpResponse.BodyHandlers.ofString());
            return response.statusCode() == 200;
        } catch (InterruptedException e) {
           e.printStackTrace();
           Thread.currentThread().interrupt();
           return false;
        }catch (IOException e){
            e.printStackTrace();
            return false;
        }
    }

    public static boolean annullaVisita(AddVisitaRequestDTO dto){
        String url = "http://localhost:9094/visita/delete/visita";
        String jsonInputString = String.format("""
                {
                       "dataVisita": "%s",
                        "horaInizioVisita": "%s",
                         "horaFineVisita": "%s",
                         "descrizione": "sfhsdufsuf",
                          "emailClienteChePrenotaVisita": "%s",
                           "cfAgente": "%s",
                           "immobileDaVisitare": %d
                }
            """, dto.getDataVisita(),dto.getHoraInizioVisita(),dto.getHoraFineVisita(),dto.getEmailClienteChePrenotaVisita(),dto.getCfAgente(),dto.getImmobileDaVisitare());
        HttpRequest request = HttpRequestBuilderFactory.buildPOST(url,jsonInputString);
        try {
            HttpResponse<String> addTodoResponse = client.send(request, HttpResponse.BodyHandlers.ofString());
            return addTodoResponse.statusCode() == 200;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return false;
        }catch (IOException e){
            e.printStackTrace();
            Thread.currentThread().interrupt();
            return false;
        }
    }


}
