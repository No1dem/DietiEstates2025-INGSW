package com.example.prova2.facade;

import java.util.List;

import com.example.prova2.dto.AddVisitaRequestDTO;
import com.example.prova2.dto.GetVisiteResponse;
import com.example.prova2.dto.InformazioniVisitaDTO;
import com.example.prova2.factory.AlertFactory;
import com.example.prova2.service.VisitaService;

public final class VisitaServiceFacade {

    private VisitaServiceFacade() {
        throw new UnsupportedOperationException("Questa è una classe di utilità e non può essere istanziata");
    }

    // =========================================================================
    // RECUPERO DATI E INFORMAZIONI SULLE VISITE
    // =========================================================================

    public static List<GetVisiteResponse> getVisiteOfAgente(String cf, String token) {
        return VisitaService.getVisiteOfAgente(cf, token);
    }

    public static InformazioniVisitaDTO getInfoAboutVisita(int idNotifica, String token) {
        InformazioniVisitaDTO response = VisitaService.getInfoAboutVisita(idNotifica, token);
        
        if (response == null) {
            AlertFactory.creaAlertErroreInterno(); // il metodo ritorna comunque null
        }
        
        return response;
    }

    // =========================================================================
    // VERIFICA E GESTIONE DEGLI APPUNTAMENTI
    // =========================================================================

    public static boolean checkVisitaGiaPresente(String email, int idImmobile, String token) {
        return VisitaService.checkVisitaClienteGiaPresente(email, idImmobile, token);
    }

    public static boolean annullaVisita(AddVisitaRequestDTO dto) {
        return VisitaService.annullaVisita(dto);
    }
}