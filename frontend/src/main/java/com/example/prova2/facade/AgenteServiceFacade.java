package com.example.prova2.facade;

import java.io.IOException;
import java.util.List;

import com.example.prova2.dto.AgenteCreazioneDTO;
import com.example.prova2.factory.AlertFactory;
import com.example.prova2.model.Agente;
import com.example.prova2.model.GestoreAgenziaImmobiliare;
import com.example.prova2.service.AgenteService;

public final class AgenteServiceFacade {

    // =========================================================================
    // VARIABILI DI STATO GLOBALI
    // =========================================================================
    public static boolean nonErrore = true;

    // Costruttore privato per impedire l'istanziazione di questa classe di utilità
    private AgenteServiceFacade() {
        throw new UnsupportedOperationException("Questa è una classe di utilità e non può essere istanziata");
    }

    // =========================================================================
    // GESTIONE AGENTI
    // =========================================================================

    public static boolean addAgente(Agente agente, GestoreAgenziaImmobiliare gestore) {
        try {
            AgenteCreazioneDTO dto = AgenteService.addAgente(agente, gestore);
            
            // Log di debug
            System.out.println(dto.isCfRegistrato());
            System.out.println(dto.isEmailRegistrata());
            
            if (dto.isCfRegistrato()) {
                AlertFactory.creaAlertErroreCFRegistrato();
                return false;
            }
            if (dto.isEmailRegistrata()) {
                AlertFactory.creaAlertErroreEmailRegistrata();
                return false;
            }
            
            return true;
            
        } catch (IOException e) {
            return false;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return false;
        }
    }

    public static List<String> getFotoAgente(String cf) {
        return AgenteService.getFotoAgente(cf);
    }

    // =========================================================================
    // VALUTAZIONI AGENTE
    // =========================================================================

    public static Integer getValutazioneAgente(String cf, String token) {
        try {
            return AgenteService.getValutazioneAgente(cf, token);
        } catch (NullPointerException e) {
            AlertFactory.creaAlertErroreInterno();
        }
        return null;
    }

    public static Integer getValutazioneAgenteByEmail(String email, String token) {
        try {
            return AgenteService.getValutazioneAgenteByEmail(email, token);
        } catch (NullPointerException e) {
            AlertFactory.creaAlertErroreInterno();
        }
        return null;
    }
}