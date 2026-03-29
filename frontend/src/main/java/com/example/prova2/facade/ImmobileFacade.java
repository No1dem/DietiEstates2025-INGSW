package com.example.prova2.facade;

import java.io.IOException;
import java.util.List;

import com.example.prova2.dto.AddVisitaRequestDTO;
import com.example.prova2.dto.DatiImmobileDTO;
import com.example.prova2.dto.ImmobileResponseRicercaDTO;
import com.example.prova2.dto.VisitaResponseDTO;
import com.example.prova2.factory.AlertFactory;
import com.example.prova2.model.Agente;
import com.example.prova2.model.Cliente;
import com.example.prova2.model.GestoreAgenziaImmobiliare;
import com.example.prova2.model.Ricerca;
import com.example.prova2.model.Utente;
import com.example.prova2.service.ImmobileService;

public final class ImmobileFacade {

    private ImmobileFacade() {
        throw new UnsupportedOperationException("Questa è una classe di utilità e non può essere istanziata");
    }

    // =========================================================================
    // RICERCA E VISUALIZZAZIONE ANNUNCI
    // =========================================================================

    public static List<ImmobileResponseRicercaDTO> ricercaImmobile(Ricerca ricerca, Utente utente) {
        int sessioneUtente = switch (utente) {
            case Cliente cliente -> 2;
            case Agente agente -> 1;
            case GestoreAgenziaImmobiliare gestore -> 0;
            case null, default -> -1;
        };
        
        ricerca.setSessioneUtente(sessioneUtente);
        return ImmobileService.searchAnnunci(ricerca, utente);
    }

    // =========================================================================
    // GESTIONE VISITE E PRENOTAZIONI
    // =========================================================================

    public static VisitaResponseDTO addVisita(AddVisitaRequestDTO addVisitaRequestDTO) {
        VisitaResponseDTO response = ImmobileService.addVisita(addVisitaRequestDTO);
        
        // Controllo critico: se non c'è risposta dal server, blocca subito l'operazione
        if (response == null) {
            AlertFactory.creaAlertErroreInterno();
            return null;
        }
        
        if (response.getFail()) {
            AlertFactory.creaAlertErroreInterno();
        }
        
        return response;
    }

    // =========================================================================
    // RECUPERO DATI IMMOBILE
    // =========================================================================

    public static DatiImmobileDTO getInfo(int idImmobile, String token) {
        DatiImmobileDTO datiImmobile = ImmobileService.getInfo(idImmobile, token);
        
        if (datiImmobile == null) {
            AlertFactory.creaAlertErroreInterno();
        }
        
        return datiImmobile;
    }

    public static ImmobileResponseRicercaDTO getInfoById(int idImmobile, String token) throws IOException, InterruptedException {
        ImmobileResponseRicercaDTO datiImmobile = ImmobileService.getInfoById(idImmobile, token);
        
        if (datiImmobile == null) {
            AlertFactory.creaAlertErroreInterno();
        }
        
        return datiImmobile;
    }

    // =========================================================================
    // ELIMINAZIONE IMMOBILE
    // =========================================================================

    public static boolean deleteImmobile(int id) {
        boolean response = ImmobileService.deleteImmobile(id);
        
        if (!response) {
            AlertFactory.creaAlertErroreEliminazioneImmobile();
            return false;
        }
        
        return true;
    }
}