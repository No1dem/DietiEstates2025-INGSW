package com.example.prova2.controller.modificaProfilo;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

import com.example.prova2.controller.dashBoard.DashboardAmministratoreController;
import com.example.prova2.facade.GestoreServiceFacade;
import com.example.prova2.factory.AlertFactory;
import com.example.prova2.model.Account;
import com.example.prova2.model.GestoreAgenziaImmobiliare;
import com.example.prova2.model.Utente;
import com.example.prova2.service.UpdatePasswordService;
import com.example.prova2.view.DashboardAmministratore;

import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.stage.Stage;
import javafx.util.Duration;

public class ModificaProfiloGestoreController extends ModificaProfiloAgenteController {

    // =========================================================================
    // INIZIALIZZAZIONE E SETUP UI
    // =========================================================================
    @Override
    public void initProfilo() {
        aggiornaDati.setDefaultButton(true);
        CompletableFuture.supplyAsync(() -> {
            initTextField();
            DashboardAmministratoreController.setAdmin((GestoreAgenziaImmobiliare) utente);
            return DashboardAmministratoreController.getAdmin();
        }).thenAccept(gestoreNuovo -> Platform.runLater(() -> {
            setDatiOnTextField(gestoreNuovo);
            setBtnsQuandoCampiModificati(); 
        }));
    }

    @Override
    public void setDatiOnTextField(Utente utente) {
        Platform.runLater(() -> {
            nomeEcognome.setText(utente.getNome() + " " + utente.getCognome());
            textFieldNome.setText(utente.getNome());
            textFieldCognome.setText(utente.getCognome());
            textFieldEmail.setText(utente.getAccountAgente().getEmail());
            textFieldNumeroTelefono.setText(utente.getTelefono());
        });
    }

    // =========================================================================
    // AZIONI UI E SALVATAGGIO (Event Handlers)
    // =========================================================================
    @FXML
    @Override
    public void salvaDati() {
        System.out.println("ciao");
        if (checkCampi()) {
            GestoreAgenziaImmobiliare gestoreNuovo = new GestoreAgenziaImmobiliare(
                    textFieldNome.getText().trim(), 
                    textFieldCognome.getText().trim(), 
                    textFieldNumeroTelefono.getText().trim()
            );
            gestoreNuovo.setAccountAgente(new Account(textFieldEmail.getText().trim()));
            
            if (GestoreServiceFacade.updateDatiGestore(gestoreNuovo, utente) != null) {
                aggiornaGestoreProfilo(gestoreNuovo);
                initProfilo();
                
                paneConfermaModifica.setVisible(true);
                PauseTransition pausa = new PauseTransition(Duration.seconds(4));
                pausa.setOnFinished(event -> paneConfermaModifica.setVisible(false));
                pausa.play();
            }
        }
    }

    @FXML
    @Override
    public void tornaIndietro() {
        try {
            System.out.println("ciao");
            DashboardAmministratore.initializePageDashboardAmministratore((Stage) tornaIndietroBottone.getScene().getWindow());
        } catch (IOException e) {
            AlertFactory.creaAlertErroreCaricamentoPagina();
        }
    }

    @FXML
    @Override
    public void tornaIndietroPassword() {
        new Thread(() -> { 
            UpdatePasswordService.updatePasswordGestore(getPasswordVecchia(), DashboardAmministratoreController.getAdmin());
        }).start();
        
        Platform.runLater(() -> { 
            paneAnnullaCambio.setVisible(false); 
        });
    }

    // =========================================================================
    // METODI HELPER
    // =========================================================================
    private void aggiornaGestoreProfilo(Utente utente) {
        // Possiamo accedere a 'utente' direttamente perché è protected static nel padre
        ModificaProfiloAgenteController.utente.setAccountAgente(new Account(utente.getAccountAgente().getEmail()));
        ModificaProfiloAgenteController.utente.setNome(utente.getNome());
        ModificaProfiloAgenteController.utente.setCognome(utente.getCognome());
        ModificaProfiloAgenteController.utente.setTelefono(utente.getTelefono());
    }
}