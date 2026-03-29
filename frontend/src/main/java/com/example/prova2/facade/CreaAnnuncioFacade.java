package com.example.prova2.facade;

import com.example.prova2.dto.AddImmobileRequestDTO;
import com.example.prova2.factory.AlertFactory;
import com.example.prova2.service.ImmobileService;
import com.example.prova2.service.MappaService;
import com.example.prova2.service.AmazonS3Service;

import javafx.scene.web.WebEngine;

public final class CreaAnnuncioFacade {

    // =========================================================================
    // COSTANTI
    // =========================================================================
    private static final String ERROR_TITLE = "Errore";
    private static final String ERROR_IMMOBILE_NON_INSERITO = "Immobile non inserito";

    // =========================================================================
    // STATO (Componenti UI)
    // =========================================================================
    private static MappaService mappaService;

    // Costruttore privato per impedire l'istanza
    private CreaAnnuncioFacade() {
        throw new UnsupportedOperationException("Questa è una classe di utilità e non può essere istanziata");
    }

    // =========================================================================
    // GESTIONE ANNUNCI (Database)
    // =========================================================================

    public static boolean addAnnuncio(AddImmobileRequestDTO annuncio, String token) {
        try {
            return ImmobileService.addAnnuncio(annuncio, token);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            AlertFactory.creaAlertErrore(ERROR_TITLE, "Operazione interrotta.");
            return false;
        } catch (Exception e) {
            AlertFactory.creaAlertErrore(ERROR_TITLE, ERROR_IMMOBILE_NON_INSERITO);
            return false;
        }
    }

    // =========================================================================
    // GESTIONE MAPPA E COORDINATE
    // =========================================================================

    public static void initMappa(WebEngine webEngine) {
        mappaService = new MappaService();
        mappaService.setWebEngine(webEngine);
        mappaService.loadMap();
    }

    public static void updateMarker(Double lat, Double lng) {
        // Attenzione: se questo metodo viene chiamato prima di initMappa(),
        // mappaService sarà null e causerà un NullPointerException.
        mappaService.updateMarker(lat, lng);
    }

    public static String getCoordinateFromAddress(String address) {
        return MappaService.getCoordinatesFromAddress(address);
    }

    // =========================================================================
    // GESTIONE CLOUD E IMMAGINI
    // =========================================================================

    public static void caricaImmagine(String path, String key) {
        AmazonS3Service.uploadImageWithoutBack(path, key);
    }
}