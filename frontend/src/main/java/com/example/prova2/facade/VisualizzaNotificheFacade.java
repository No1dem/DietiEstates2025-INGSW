package com.example.prova2.facade;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.example.prova2.dto.DatiDTO;
import com.example.prova2.dto.GetNotificheDTO;
import com.example.prova2.factory.AlertFactory;
import com.example.prova2.service.AgenteService;
import com.example.prova2.service.NotificaService;
import com.example.prova2.service.VisitaService;

public final class VisualizzaNotificheFacade {

    private VisualizzaNotificheFacade() {
        throw new UnsupportedOperationException("Questa è una classe di utilità e non può essere istanziata");
    }

    // =========================================================================
    // RECUPERO DATI E NOTIFICHE
    // =========================================================================

    public static List<GetNotificheDTO> getNotificaAgente(String cf, String token) {
        try {
            return NotificaService.getNotificationsForAgente(cf, token);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return new ArrayList<>();
        } catch (IOException e) {
            System.err.println("Errore di connessione nel recupero Notifiche Agente: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public static DatiDTO getAgenteByEmail(String email, String token) {
        DatiDTO dto = AgenteService.getAgente(email, token);
        
        // Se non trova l'agente o il server fallisce, mostra un alert
        if (dto == null) {
            AlertFactory.creaAlertErroreInterno();
        }
        
        return dto;
    }

    // =========================================================================
    // GESTIONE STATO NOTIFICHE (Accettazione e Rifiuto)
    // =========================================================================

    public static void setAccept(int idNotifica, String token) {
        NotificaService.setNotificationAccepted(idNotifica, token);
    }

    public static void setDecline(int idNotifica, String token) {
        NotificaService.setNotificationRejected(idNotifica, token);
    }

    // =========================================================================
    // GESTIONE VISITE
    // =========================================================================

    public static void annullaInvioVisita(int id, String token) {
        VisitaService.annullaInvioVisita(id, token);
    }
}