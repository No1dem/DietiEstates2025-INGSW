package com.example.prova2.facade;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.example.prova2.dto.UpdateDatiResponseDTO;
import com.example.prova2.factory.AlertFactory;
import com.example.prova2.model.GestoreAgenziaImmobiliare;
import com.example.prova2.model.Utente;
import com.example.prova2.service.GestoreService;

public final class GestoreServiceFacade {

    // Costruttore privato blindato
    private GestoreServiceFacade() {
        throw new UnsupportedOperationException("Questa è una classe di utilità e non può essere istanziata");
    }

    // =========================================================================
    // RECUPERO DATI (Anagrafiche Gestori)
    // =========================================================================

    public static List<String> getCfGestore(String token) {
        try {
            return GestoreService.getAllCfGestore(token);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            e.printStackTrace();
            return new ArrayList<>();
        } catch (IOException e) {
            System.err.println("Errore di connessione nel recupero CF Gestore: " + e.getMessage());
            return new ArrayList<>();
        }
    }

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

    // =========================================================================
    // GESTIONE PROFILO E AGGIORNAMENTO DATI
    // =========================================================================

    public static UpdateDatiResponseDTO updateDatiGestore(GestoreAgenziaImmobiliare gestore, Utente utentevecchio) {
        UpdateDatiResponseDTO response = GestoreService.updateDatiGestore(gestore, utentevecchio);
        
        // Controllo di sicurezza: se il server non risponde, si ferma subito
        if (response == null) {
            AlertFactory.creaAlertErroreInterno();
            return null;
        }
        
        if (response.getErroreInterno()) {
            AlertFactory.creaAlertErroreInterno();
            return null;
        }
        
        if (response.getDuplicato()) {
            AlertFactory.creaAlertErroreEmailRegistrata();
            return null;
        }
        
        return response;
    }

    // =========================================================================
    // VALIDAZIONE E SICUREZZA
    // =========================================================================

    public static boolean isPasswordVecchiaValida(String email, String password, String token) {
        return GestoreService.checkPassword(email, password, token);
    }
}