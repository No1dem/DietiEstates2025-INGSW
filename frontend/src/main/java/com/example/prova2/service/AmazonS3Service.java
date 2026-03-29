package com.example.prova2.service;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

import javax.imageio.ImageIO;

import com.example.prova2.factory.AlertFactory;

import javafx.application.Platform;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.exception.SdkException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

public class AmazonS3Service {
    private static final AwsBasicCredentials awsCredentials = AwsBasicCredentials.create(
            "AKIAVYGUXZRTXXBFL5FR",      //AKIA
            "vjosMJy6b06pO/t5KIAYW/DxTmSaV/Do6FC6QhS9"       //segreto
    );



    private static final S3Client s3Client = S3Client.builder()
            .region(Region.EU_NORTH_1)
            .credentialsProvider(StaticCredentialsProvider.create(awsCredentials)) // Usa le credenziali fornite
            .build();



    private static final String bucketName = "dietiestates2025-no1dem";


    private static final HttpClient client = HttpClient.newHttpClient();


    public static void caricaImmagine(String imageUrl, String key,String cf) {
        URL url = null;
        System.out.println("immagine:"+imageUrl);
        try {
            url = new File(imageUrl).toURI().toURL();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try {
            assert url != null;
            try (InputStream inputStream = url.openStream()) {
                inviaImmagineAS3(key, inputStream, url);
                inviaChiaveAlBack(key, cf);
            }
        }
        catch (Exception e) {
            System.err.println("Errore durante il caricamento: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void uploadImageWithoutBack(String imageUrl, String key) {
        URL url = null;
        try {
            url = new File(imageUrl).toURI().toURL();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try {
            assert url != null;
            try (InputStream inputStream = url.openStream()) {
                inviaImmagineAS3(key, inputStream, url);
            }
        }
        catch (Exception e) {
            System.err.println("Errore durante il caricamento: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void inviaChiaveAlBack(String key, String cf) {
        new Thread(()->{
            boolean result=uploadFoto(cf, key);
            if(!result){
                Platform.runLater(()->{AlertFactory.creaAlertErroreInterno();});
            }
        }).start();
    }

    private static void inviaImmagineAS3(String key, InputStream inputStream, URL url) throws IOException {
        // Leggi immagine originale
        BufferedImage originale = ImageIO.read(inputStream);

        // Ridimensiona immagine
        BufferedImage ridimensionata = new BufferedImage(800,600, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = ridimensionata.createGraphics();
        g.drawImage(originale.getScaledInstance(800, 600, Image.SCALE_SMOOTH), 0, 0, null);
        g.dispose();

        // Converti in InputStream
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ImageIO.write(ridimensionata, "jpg", os);
        InputStream nuovoInputStream = new ByteArrayInputStream(os.toByteArray());

        // Invia immagine ridotta a S3
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();
        s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(nuovoInputStream, os.size()));

        // Chiudi lo stream originale
        inputStream.close();
    }

    public static String getImageFromS3(String key) {
        try (S3Presigner presigner = S3Presigner.builder()
                .credentialsProvider(StaticCredentialsProvider.create(awsCredentials))
                .region(Region.EU_NORTH_1)
                .build()) {
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .build();
            PresignedGetObjectRequest presignedRequest = presigner.presignGetObject(
                    presignRequest -> presignRequest.getObjectRequest(getObjectRequest)
                            .signatureDuration(Duration.ofHours(1))
            );
            URL presignedUrl = presignedRequest.url();
            return presignedUrl.toString();
        } catch (SdkException e) {
            e.printStackTrace();
        }
        return null;
    }


    // public static String getImageFromS3(String key) {
    //     try (S3Presigner presigner = S3Presigner.builder()
    //             .credentialsProvider(StaticCredentialsProvider.create(awsCredentials))
    //             .region(Region.US_EAST_1)
    //             .build()) {
            
    //         // 1. Crea la richiesta per l'oggetto
    //         GetObjectRequest getObjectRequest = GetObjectRequest.builder()
    //                 .bucket(bucketName)
    //                 .key(key)
    //                 .build();

    //         // 2. Crea la richiesta di firma (valida per 1 ora)
    //         GetObjectPresignRequest getObjectPresignRequest = GetObjectPresignRequest.builder()
    //                 .signatureDuration(Duration.ofHours(1))
    //                 .getObjectRequest(getObjectRequest)
    //                 .build();

    //         // 3. Genera l'URL
    //         PresignedGetObjectRequest presignedRequest = presigner.presignGetObject(getObjectPresignRequest);
            
    //         return presignedRequest.url().toString();
            
    //     } catch (SdkException e) {
    //         e.printStackTrace();
    //     }
    //     return null;
    // }

    // public static boolean uploadFoto(String cf,String key){
    //     String url = "http://localhost:9094/image/upload?codicefiscale="+cf;
    //     String jsonInputString = String.format("""
    //             {
    //                "key": "%s"
    //             }
    //         """,key);
    //     HttpRequest request = HttpRequestBuilderFactory.buildPOST(url,jsonInputString);
    //     try {
    //         HttpResponse<String> response = mandaRichiesta(request);
    //         return rispostaAndataBuonFine(response);
    //     } catch (IOException e) {
    //         return false;
    //     }
    //     catch (InterruptedException e) {
    //         Thread.currentThread().interrupt();
    //         return false;
    //     }
    // }

    public static boolean uploadFoto(String cf,String key) {
        // 1. Assicuriamoci che il CF non sia nullo o contenga spazi che rompono l'URL
        String codiceFiscalePulito = (cf != null) ? cf.trim().replace(" ", "%20") : "";
        String url = "http://localhost:9094/image/upload?codicefiscale=" + codiceFiscalePulito;
        
        String jsonInputString = String.format("""
                {
                   "key": "%s"
                }
            """, key);

        System.out.println("--- DEBUG INVIO AL BACKEND ---");
        System.out.println("URL: " + url);
        System.out.println("BODY: " + jsonInputString);

        // 2. Creiamo la richiesta manualmente mettendo le intestazioni corrette per il JSON!
        HttpRequest request = java.net.http.HttpRequest.newBuilder()
                .uri(java.net.URI.create(url))
                .header("Content-Type", "application/json") // QUESTO È FONDAMENTALE!
                .header("Accept", "application/json")
                .POST(java.net.http.HttpRequest.BodyPublishers.ofString(jsonInputString))
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            
            System.out.println("STATUS CODE: " + response.statusCode());
            System.out.println("BODY RISPOSTA: " + response.body());
            System.out.println("------------------------------");
            
            return rispostaAndataBuonFine(response);
        } catch (Exception e) {
            System.out.println("ERRORE DI CONNESSIONE AL BACKEND: " + e.getMessage());
            return false;
        }
    }

    public static boolean rispostaAndataBuonFine(HttpResponse<String> response) {
        return response.statusCode() == 200;
    }


    public static HttpResponse<String> mandaRichiesta(HttpRequest request) throws IOException, InterruptedException {
        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

}
