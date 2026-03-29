package com.example.prova2.controller.modificaProfilo;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

import com.example.prova2.controller.dashBoard.DashBoardClienteController;
import com.example.prova2.facade.ClienteServiceFacade;
import com.example.prova2.factory.AlertFactory;
import com.example.prova2.model.Account;
import com.example.prova2.model.Cliente;
import com.example.prova2.model.Utente;
import com.example.prova2.service.UpdatePasswordService;
import com.example.prova2.view.CambioPassword;
import com.example.prova2.view.DashBoardCliente;

import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.beans.binding.BooleanBinding;
import javafx.fxml.FXML;
import javafx.util.Duration;

public class ModificaProfiloClienteController extends ModificaProfiloAgenteController {

    // =========================================================================
    // INIZIALIZZAZIONE E SETUP UI
    // =========================================================================
    @Override
    public void initProfilo() {
        aggiornaDati.setDefaultButton(true);
        CompletableFuture.supplyAsync(() -> {
            initTextField();
            DashBoardClienteController.setCliente((Cliente) utente);
            return DashBoardClienteController.getCliente();
        }).thenAccept(nuovoCliente -> Platform.runLater(() -> {
            setDatiOnTextField(nuovoCliente);
            setBtnsQuandoCampiModificati();
        }));
    }

    @Override
    public void initTextField() {
        addListenerMaiuscoleENoNumeri(textFieldNome);
        addListenerMaiuscoleENoNumeri(textFieldCognome);
    }

    @Override
    public void setDatiOnTextField(Utente utente) {
        Platform.runLater(() -> {
            nomeEcognome.setText(utente.getNome() + " " + utente.getCognome());
            textFieldNome.setText(utente.getNome());
            textFieldCognome.setText(utente.getCognome());
            textFieldEmail.setText(utente.getAccountAgente().getEmail());
        });
    }

    public void setDatiOnTextFieldWithGoogle(Utente utente) {
        Platform.runLater(() -> {
            textFieldEmail.setText(utente.getAccountAgente().getEmail());
        });
    }

    @Override
    public void setBtnsQuandoCampiModificati() {
        BooleanBinding campiModificati = textFieldNome.textProperty().isNotEqualTo(utente.getNome())
                .or(textFieldCognome.textProperty().isNotEqualTo(utente.getCognome()))
                .or(textFieldEmail.textProperty().isNotEqualTo(utente.getAccountAgente().getEmail()));
        
        btnCancella.visibleProperty().bind(campiModificati);
        aggiornaDati.setVisible(true);
        aggiornaDati.disableProperty().bind(campiModificati.not());
    }

    // =========================================================================
    // AZIONI UI (Event Handlers)
    // =========================================================================
    @FXML
    @Override
    public void salvaDati() {
        if (checkCampi()) {
            Cliente nuovoCliente = new Cliente(textFieldNome.getText(), textFieldCognome.getText(), textFieldEmail.getText());
            
            if (ClienteServiceFacade.updateDatiCliente(nuovoCliente, utente) != null) {
                aggiornaClienteProfilo(nuovoCliente);
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
            DashBoardCliente.initializePageDashboardCliente(tornaIndietroBottone.getScene().getWindow(), utente);
        } catch (IOException e) {
            AlertFactory.creaAlertErroreCaricamentoPagina();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            AlertFactory.creaAlertErroreCaricamentoPagina();
        }
    }

    @FXML
    @Override
    public void tornaIndietroPassword() {
        new Thread(() -> { 
            UpdatePasswordService.updatePasswordCliente(getPasswordVecchia(), DashBoardClienteController.getCliente());
        }).start();
        Platform.runLater(() -> { paneAnnullaCambio.setVisible(false); });
    }

    @FXML
    @Override
    public void apriPaginaModificaPassword() {
        try {
            CambioPassword.initializePageCambioPassword(btnCancella.getScene().getWindow(), this, utente);
        } catch (IOException e) {
            e.printStackTrace();
            AlertFactory.creaAlertErrore("Errore apertura pagina!", "Siamo spiacenti si è verificato un errore nel caricamento della pagina cambio password");
        }
    }

    // =========================================================================
    // METODI HELPER E VALIDAZIONE LOGICA
    // =========================================================================
    @Override
    public boolean checkCampi() {
        boolean flag = true;
        flag &= isNomeValido(textFieldNome.getText());
        flag &= isCognomeValido(textFieldCognome.getText());
        flag &= isEmailValida(textFieldEmail.getText().trim());
        return flag;
    }

    private void aggiornaClienteProfilo(Cliente nuovoCliente) {
        utente.setAccountAgente(new Account(nuovoCliente.getAccountAgente().getEmail()));
        utente.setNome(nuovoCliente.getNome());
        utente.setCognome(nuovoCliente.getCognome());
    }
}