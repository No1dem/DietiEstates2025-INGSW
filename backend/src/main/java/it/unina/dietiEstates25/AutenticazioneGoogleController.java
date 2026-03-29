package it.unina.dietiEstates25;

import it.unina.dietiEstates25.db.DatabaseConfiguration;
import it.unina.dietiEstates25.dto.response.AgenteDTO;
import it.unina.dietiEstates25.service.ClienteService;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import static it.unina.dietiEstates25.AutenticazioneController.creazioneJWT;

@Path("/auth")
public class AutenticazioneGoogleController {

    // =========================================================================
    // COSTANTI E VARIABILI DI ISTANZA
    // =========================================================================
    private static final String CLIENT_ID = "39635848827-mgl3pct74q6e40ir7ud9i889p439r6d0.apps.googleusercontent.com";
    private static final String CLIENT_SECRET = DatabaseConfiguration.getClientSecret();
    private static final String REDIRECT_URI = "http://localhost:9094/auth/exchange_token";
    private static final String TOKEN_URL = "https://oauth2.googleapis.com/token";
    private static final String USER_INFO_URL = "https://www.googleapis.com/oauth2/v2/userinfo";
    
    private final HttpClient client = HttpClient.newHttpClient();

    // =========================================================================
    // ENDPOINT PUBBLICI (OAuth2 Flow)
    // =========================================================================
    @GET
    @Path("/exchange_token")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response exchangeToken(@QueryParam("code") String authorizationCode) {
        try {
            String rawTokenResponse = fetchAccessToken(authorizationCode);
            JsonNode parsedTokenJson = new ObjectMapper().readTree(rawTokenResponse);
            String googleAccessToken = parsedTokenJson.get("access_token").asText();
            
            String rawUserInfo = fetchUserInfo(googleAccessToken);
            ObjectMapper jsonMapper = new ObjectMapper();
            JsonNode parsedUserInfo = jsonMapper.readTree(rawUserInfo);
            
            String extractedGoogleId = parsedUserInfo.get("id").asText();
            String extractedEmail = parsedUserInfo.get("email").asText();
            
            String jwtToken = creazioneJWT(extractedEmail, TimeUnit.DAYS.toMillis(365));
            AgenteDTO agentDtoResult = ClienteService.addCliente(extractedEmail, extractedGoogleId);
            
            if (agentDtoResult != null) {
                agentDtoResult.setToken(jwtToken);
            }

            return Response.status(Response.Status.OK).entity(agentDtoResult).build();
        } catch (Exception authException) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Errore durante l'autenticazione").build();
        }
    }

    // =========================================================================
    // METODI PRIVATI E HELPER (HTTP Requests)
    // =========================================================================
    private String fetchAccessToken(String authCode) {
        String requestBodyParams = "code=" + authCode +
                "&client_id=" + CLIENT_ID +
                "&client_secret=" + CLIENT_SECRET +
                "&redirect_uri=" + REDIRECT_URI +
                "&grant_type=authorization_code";

        return sendPostRequest(requestBodyParams);
    }

    private String fetchUserInfo(String validAccessToken) {
        try {
            String userInfoEndpoint = USER_INFO_URL + "?access_token=" + validAccessToken;
            HttpRequest infoRequest = HttpRequest.newBuilder()
                    .uri(URI.create(userInfoEndpoint))
                    .GET()
                    .build();
            HttpResponse<String> infoResponse = client.send(infoRequest, HttpResponse.BodyHandlers.ofString());
            return infoResponse.body();
        } catch (IOException ioException) {
            return null;
        } catch (InterruptedException interruptedEx) {
            Thread.currentThread().interrupt();
            return null;
        }
    }

    private String sendPostRequest(String postBody) {
        try {
            HttpRequest postRequest = HttpRequest.newBuilder()
                    .uri(URI.create(AutenticazioneGoogleController.TOKEN_URL))
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .POST(HttpRequest.BodyPublishers.ofString(postBody, StandardCharsets.UTF_8))
                    .build();

            HttpResponse<String> postResponse = client.send(postRequest, HttpResponse.BodyHandlers.ofString());
            return postResponse.body();
        } catch (IOException reqIoException) {
            return null;
        } catch (InterruptedException reqInterruptedEx) {
            Thread.currentThread().interrupt();
            return null;
        }
    }
}