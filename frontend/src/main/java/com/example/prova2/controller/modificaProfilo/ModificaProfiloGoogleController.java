package com.example.prova2.controller.modificaProfilo;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.example.prova2.controller.dashBoard.DashBoardClienteController;
import com.example.prova2.facade.ClienteServiceFacade;
import com.example.prova2.facade.ModificaProfiloFacade;
import com.example.prova2.factory.AlertFactory;
import com.example.prova2.model.Account;
import com.example.prova2.model.Cliente;
import com.example.prova2.model.Utente;
import com.example.prova2.service.UpdatePasswordService;
import com.example.prova2.view.CambioPassword;
import com.example.prova2.view.DashBoardCliente;
import com.example.prova2.view.HomePage;

import javafx.application.Platform;
import javafx.beans.binding.BooleanBinding;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class ModificaProfiloGoogleController extends ModificaProfiloClienteController {

    // =========================================================================
    // COMPONENTI UI (FXML)
    // =========================================================================
    @FXML 
    private Button aggiornaDati; // Nota: Effettua shadowing del bottone del padre, mantenuto per sicurezza

    // =========================================================================
    // VARIABILI DI STATO E GETTER/SETTER
    // =========================================================================
    private Utente utente;

    public void setUtenteGoogle(Utente utente) {
        this.utente = utente;
    }

    // =========================================================================
    // INIZIALIZZAZIONE E SETUP UI
    // =========================================================================
    public void initProfiloGoogle(Utente utenteGoogle) {
        CompletableFuture.supplyAsync(() -> {
            initTextField(); // Metodo ereditato dal padre

            // Castiamo l'utente a Cliente e lo settiamo
            Cliente nuovoCliente = (Cliente) utenteGoogle;
            DashBoardClienteController.setCliente(nuovoCliente);

            return nuovoCliente;
        }).thenAccept(nuovoCliente -> Platform.runLater(() -> {
            setDatiOnTextFieldWithGoogle(nuovoCliente);
            setBtnsQuandoCampiModificatiGoogle(nuovoCliente);
        }));
    }

    public void setDatiOnTextFieldWithGoogle(Utente utente) {
        Platform.runLater(() -> {
            textFieldEmail.setText(utente.getAccountAgente().getEmail());
        });
    }

    public void setBtnsQuandoCampiModificatiGoogle(Utente utente) {
        BooleanBinding campiModificati = textFieldNome.textProperty().isNotEqualTo(utente.getNome())
                .or(textFieldCognome.textProperty().isNotEqualTo(utente.getCognome()))
                .or(textFieldNumeroTelefono.textProperty().isNotEqualTo(utente.getTelefono()));
        
        btnCancella.visibleProperty().bind(campiModificati);
        aggiornaDati.setVisible(true);
        aggiornaDati.disableProperty().bind(campiModificati.not());
    }

    // =========================================================================
    // AZIONI UI E SALVATAGGIO (Event Handlers)
    // =========================================================================
    @FXML
    @Override
    public void salvaDati() {
        if (checkCampi()) {
            Cliente nuovoCliente = new Cliente(textFieldNome.getText(), textFieldCognome.getText(), textFieldEmail.getText(), textFieldNumeroTelefono.getText());
            ClienteServiceFacade.updateDatiCliente(nuovoCliente, utente);
            aggiornaClienteProfilo(nuovoCliente);
            initProfilo();
        }
    }

    @FXML
    public void salvaDatiGoogle() {
        if (checkCampi()) {
            Cliente nuovoCliente = new Cliente(textFieldNome.getText(), textFieldCognome.getText(), textFieldEmail.getText(), textFieldNumeroTelefono.getText());
            ClienteServiceFacade.updateDatiCliente(nuovoCliente, utente);
            aggiornaClienteProfilo(nuovoCliente);
            initProfilo();
            try {
                nuovoCliente.setToken(utente.getToken());
                HomePage.initializeHomePage((Stage) aggiornaDati.getScene().getWindow(), nuovoCliente);
            } catch (IOException e) {
                AlertFactory.creaAlertErroreCaricamentoPagina();
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
    // METODI HELPER E VALIDAZIONE
    // =========================================================================
    @Override
    public boolean checkCampi() {
        boolean flag = true;
        flag &= isNomeValido(textFieldNome.getText());
        flag &= isCognomeValido(textFieldCognome.getText());
        flag &= isEmailValida(textFieldEmail.getText().trim());
        flag &= isNumeroTelefonoValido(textFieldNumeroTelefono.getText().trim());
        flag &= checkDuplicatoEmail(textFieldEmail.getText().trim(), utente);
        return flag;
    }

    public boolean checkDuplicatoEmail(String email, Utente utente) {
        List<String> emails = ModificaProfiloFacade.getEmailGestore(utente);
        if (emailVecchiaUgualeNuova()) {
            return true;
        }
        if (exists(email, emails)) {
            scriviMessaggioErrore(erroreEmail, "Si prega di inserire un'email non registrata!");
            return false;
        }
        return true;
    }

    private boolean emailVecchiaUgualeNuova() {
        return utente.getAccountAgente().getEmail().equals(textFieldEmail.getText().trim());
    }

    private boolean exists(String campo, List<String> campi) {
        for (String s : campi) {
            if (campo.equals(s)) {
                return true;
            }
        }
        return false;
    }

    private void aggiornaClienteProfilo(Cliente nuovoCliente) {
        utente.setAccountAgente(new Account(nuovoCliente.getAccountAgente().getEmail()));
        utente.setNome(nuovoCliente.getNome());
        utente.setCognome(nuovoCliente.getCognome());
        utente.setTelefono(nuovoCliente.getTelefono());
    }
}