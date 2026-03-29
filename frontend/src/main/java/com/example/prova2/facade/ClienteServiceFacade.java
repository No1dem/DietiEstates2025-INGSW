package com.example.prova2.facade;

import java.util.Collections;
import java.util.List;

import com.example.prova2.dto.ClienteDTO;
import com.example.prova2.dto.ClienteDatiDTO;
import com.example.prova2.dto.GetAllRicercheResponse;
import com.example.prova2.dto.UpdateDatiResponseDTO;
import com.example.prova2.factory.AlertFactory;
import com.example.prova2.model.Agente;
import com.example.prova2.model.Cliente;
import com.example.prova2.model.GestoreAgenziaImmobiliare;
import com.example.prova2.model.Utente;
import com.example.prova2.service.AgenteService;
import com.example.prova2.service.ClienteService;
import com.example.prova2.service.GestoreService;

import javafx.application.Platform;

public final class ClienteServiceFacade {

    
    private ClienteServiceFacade() {
        throw new UnsupportedOperationException("Questa è una classe di utilità e non può essere istanziata");
    }

    // =========================================================================
    // REGISTRAZIONE
    // =========================================================================

    public static ClienteDTO registrati(Cliente cliente) {
        ClienteDTO dto = ClienteService.registrati(cliente);

        if (dto == null) {
            AlertFactory.creaAlertErroreConnessione();
            return null;
        }

        if (dto.isErroreInterno()) {
            AlertFactory.creaAlertErroreConnessione();
        }
        
        System.out.println("token: " + dto.getToken());
        return dto;
    }

    // =========================================================================
    // GESTIONE PROFILO (DATI CLIENTE)
    // =========================================================================

    public static ClienteDatiDTO recuperoDati(Utente cliente) {
        System.out.println("recuperoDati cliente email: " + cliente.getAccountAgente().getEmail());
        System.out.println("recuperoDati cliente token: " + cliente.getToken());
        
        ClienteDatiDTO datiCliente = ClienteService.getDatiCliente(cliente.getAccountAgente().getEmail(), cliente.getToken());
        
        if (datiCliente != null) {
            return datiCliente;
        }
        
        Platform.runLater(AlertFactory::creaAlertErroreInterno);
        return null;
    }

    public static UpdateDatiResponseDTO updateDatiCliente(Cliente cliente, Utente utentevecchio) {
        UpdateDatiResponseDTO response = ClienteService.updateDatiCliente(cliente, utentevecchio);

        if (response == null) {
            AlertFactory.creaAlertErroreInterno();
            return null;
        }

        System.out.println("Duplicato: " + response.getDuplicato());
        System.out.println("Errore interno: " + response.getErroreInterno());

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
    // RICERCHE
    // =========================================================================

    public static List<GetAllRicercheResponse> getAllRicerche(Utente utente) {
        return switch (utente) {
            case Cliente cliente -> ClienteService.getAllRicerche(utente);
            case Agente agente -> AgenteService.getAllRicerche(utente);
            case GestoreAgenziaImmobiliare gestore -> GestoreService.getAllRicerche(utente);
            default -> Collections.emptyList();
        };
    }
}