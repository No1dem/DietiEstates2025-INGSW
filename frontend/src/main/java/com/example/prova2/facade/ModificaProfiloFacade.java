package com.example.prova2.facade;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.example.prova2.dto.UpdateDatiResponseDTO;
import com.example.prova2.factory.AlertFactory;
import com.example.prova2.model.Agente;
import com.example.prova2.model.Foto;
import com.example.prova2.model.Utente;
import com.example.prova2.service.AgenteService;
import com.example.prova2.service.GestoreService;
import com.example.prova2.service.AmazonS3Service;
import com.example.prova2.service.UpdatePasswordService;

public final class ModificaProfiloFacade {

    // Costruttore privato blindato per impedire l'istanza
    private ModificaProfiloFacade() {
        throw new UnsupportedOperationException("Questa è una classe di utilità e non può essere istanziata");
    }

    // =========================================================================
    // AGGIORNAMENTO DATI PROFILO (Anagrafica e Bio)
    // =========================================================================

    public static UpdateDatiResponseDTO updateDatiAgente(Agente agente, String agenteVecchio) {
        UpdateDatiResponseDTO dto = AgenteService.updateDatiAgente(agente, agenteVecchio);
        
        if (dto == null) {
            return null;
        }
        
        if (dto.getDuplicato()) {
            AlertFactory.creaAlertErroreEmailRegistrata();
            return null;
        }
        
        if (dto.getErroreInterno()) {
            AlertFactory.creaAlertErroreInterno();
            return null;
        }
        
        return dto;
    }

    public static boolean updateBio(String bio, String cf, String token) {
        boolean esito = AgenteService.updateBio(cf, bio, token);
        
        if (!esito) {
            AlertFactory.creaAlertErroreInterno();
            return false;
        }
        
        return true;
    }

    // =========================================================================
    // SICUREZZA E CREDENZIALI
    // =========================================================================

    public static boolean updatePasswordAgente(String pass, Agente agente) {
        return UpdatePasswordService.updatePasswordAgente(pass, agente);
    }

    // =========================================================================
    // GESTIONE IMMAGINI E CLOUD (AWS S3)
    // =========================================================================

    public static boolean uploadFotoAgente(String cf, Foto foto) {
        AmazonS3Service.caricaImmagine(foto.getPath(), "images/" + new File(foto.getPath()).getName(), cf);
        return true;
    }

    public static List<String> getFotoAgente(String cf) {
        return AgenteService.getFotoAgente(cf);
    }

    public static String getImageFromS3(String key) {
        return AmazonS3Service.getImageFromS3(key);
    }

    // =========================================================================
    // RECUPERO DATI AGGIUNTIVI
    // =========================================================================

    public static List<String> getEmailGestore(Utente utente) {
        try {
            return GestoreService.getAllEmailGestore(utente.getToken());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            e.printStackTrace();
            return new ArrayList<>();
        } catch (IOException e) {
            System.err.println("Errore di connessione nel recupero Email Gestore: " + e.getMessage());
            return new ArrayList<>();
        }
    }
}