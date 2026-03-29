package com.example.prova2.controller.prenotaVisita;

import java.io.IOException;

import com.example.prova2.controller.SchermataAnnuncioController;
import com.example.prova2.factory.AlertFactory;
import com.example.prova2.model.Immobile;
import com.example.prova2.view.EffettuaPrenotazione;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class AlertVisitaController {

    // =========================================================================
    // COMPONENTI UI 
    // =========================================================================
    @FXML private Label nomeAgente;
    @FXML private Label richiediVisitaLbl;

    // =========================================================================
    // VARIABILI DI STATO
    // =========================================================================
    private Immobile immobile;
    private SchermataAnnuncioController controllerPrec;

    // =========================================================================
    // INIZIALIZZAZIONE
    // =========================================================================
    public void init(Immobile immobile, SchermataAnnuncioController controllerPrec) {
        this.immobile = immobile;
        this.controllerPrec = controllerPrec;
        nomeAgente.setText(immobile.getNomeAgente() + " " + immobile.getCognomeAgente());
    }

    // =========================================================================
    // AZIONI UI 
    // =========================================================================
    @FXML
    public void richiediVisita() {
        try {
            EffettuaPrenotazione.initPage(
                    (Stage) richiediVisitaLbl.getScene().getWindow(),
                    SchermataAnnuncioController.getUtenteCheCerca().getAccountAgente().getEmail(), 
                    immobile, 
                    controllerPrec
            );
        } catch (IOException e) {
            e.printStackTrace();
            AlertFactory.creaAlertErroreInterno();
        }
    }
}