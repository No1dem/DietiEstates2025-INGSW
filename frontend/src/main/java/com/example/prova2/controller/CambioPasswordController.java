package com.example.prova2.controller;

import java.util.Optional;

import com.example.prova2.controller.modificaProfilo.ModificaPasswordInterface;
import static com.example.prova2.controller.registrazione.RegistrazioneUtenteController.visualizzaPassword;
import com.example.prova2.facade.GestoreServiceFacade;
import com.example.prova2.facade.UtenteFacade;
import com.example.prova2.factory.AlertFactory;
import com.example.prova2.model.Utente;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class CambioPasswordController {

    // =========================================================================
    // COSTANTI
    // =========================================================================
    protected static final String PASSWORDPATTERN = "^.{8,}$";

    // =========================================================================
    // COMPONENTI UI (FXML)
    // =========================================================================
    @FXML private PasswordField pwdAttualeField;
    @FXML private PasswordField nuovaPwdField;
    @FXML private PasswordField reinserimentoNuovaPwd;
    
    @FXML private TextField mostraPa1;
    @FXML private TextField mostraPa2;
    @FXML private TextField mostraPa3;
    
    @FXML private Button salvaBottone;
    @FXML private Button tornaIndietroBottone;
    
    @FXML private Label errorreVecchiaPass;
    @FXML private Label erroreNuovaPass;
    @FXML private Label erroreRinserNuovaPass;
    @FXML private Label nuovaPwdLabel;
    @FXML private Label inserisciNuovaPwdLabel;
    
    @FXML private CheckBox mostraPasswordCheckBox;
    @FXML private CheckBox mostraPasswordCheckBox1;
    @FXML private CheckBox mostraPasswordCheckBox11;

    // =========================================================================
    // VARIABILI DI STATO
    // =========================================================================
    private Scene scene;
    private Utente utenteCheCambiaPassword;
    private ModificaPasswordInterface controllerPrecedente;

    // =========================================================================
    // INIZIALIZZAZIONE E GETTER/SETTER
    // =========================================================================
    public void init() {
        mostraPa1.textProperty().bindBidirectional(pwdAttualeField.textProperty());
        mostraPa2.textProperty().bindBidirectional(nuovaPwdField.textProperty());
        mostraPa3.textProperty().bindBidirectional(reinserimentoNuovaPwd.textProperty());
    }

    public Utente getUtenteCheCambiaPassword() { return utenteCheCambiaPassword; }
    public void setUtenteCheCambiaPassword(Utente utenteCheCambiaPassword) { this.utenteCheCambiaPassword = utenteCheCambiaPassword; }

    public ModificaPasswordInterface getControllerPrecedente() { return controllerPrecedente; }
    public void setControllerPrecedente(ModificaPasswordInterface controllerPrecedente) { this.controllerPrecedente = controllerPrecedente; }

    public Scene getScene() { return scene; }
    public void setScene(Scene scene) { this.scene = scene; }

    // =========================================================================
    // AZIONI UI (Event Handlers)
    // =========================================================================
    @FXML
    public void cambiaPassword() {
        if (checkCampi()) {
            if (isCambioAvvenuto()) {
                mostraAlertConfermaPassword();
            } else {
                mostraAlertErrorePassword();
            }
        }
    }

    @FXML
    public void tornaDash() {
        Stage stage = (Stage) salvaBottone.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void svuota(ActionEvent actionEvent) {
        pwdAttualeField.setText("");
        nuovaPwdField.setText("");
        reinserimentoNuovaPwd.setText("");
        mostraPa1.setText("");
        mostraPa2.setText("");
        mostraPa3.setText("");
    }

    @FXML
    public void gestioneMostraPwd1() {
        visualizzaPassword(mostraPasswordCheckBox, mostraPa1, pwdAttualeField);
    }

    @FXML
    public void gestioneMostraPwd2() {
        visualizzaPassword(mostraPasswordCheckBox1, mostraPa2, nuovaPwdField);
    }

    @FXML
    public void gestioneMostraPwd3() {
        visualizzaPassword(mostraPasswordCheckBox11, mostraPa3, reinserimentoNuovaPwd);
    }

    @FXML
    public void eliminaErrorMsgVecchiaPwd() { errorreVecchiaPass.setVisible(false); }
    
    @FXML
    public void eliminaErrorMsgNuovaPwd() { erroreNuovaPass.setVisible(false); }
    
    @FXML
    public void eliminaErrorMsgReinserimentoNuovaPwd() { erroreRinserNuovaPass.setVisible(false); }

    // =========================================================================
    // METODI HELPER E VALIDAZIONE LOGICA
    // =========================================================================
    public boolean checkCampi() {
        boolean flag = true;
        flag &= isVecchiaPasswordValida();
        flag &= isNuovaPasswordValida();
        System.out.println(flag);
        flag &= checkRePassword();
        return flag;
    }

    public boolean isVecchiaPasswordValida() {
        String password = pwdAttualeField.getText();
        if (isPasswordVecchiaValida(password)) {
            errorreVecchiaPass.setVisible(false);
            return true;
        } else {
            if (password.isEmpty()) {
                scriviMessaggioErrore("Si prega di inserire una password!");
            } else {
                scriviMessaggioErrore("La password inserita non corrisponde con quella attuale!");
            }
            errorreVecchiaPass.setVisible(true);
            return false;
        }
    }

    public boolean isNuovaPasswordValida() {
        if (nuovaPwdField.getText().matches(PASSWORDPATTERN)) {
            erroreNuovaPass.setVisible(false);
            return true;
        } else {
            erroreNuovaPass.setText("Si prega di inserire una password di almeno 8 caratteri!");
            erroreNuovaPass.setVisible(true);
            return false;
        }
    }

    public boolean checkRePassword() {
        if (isPasswordReinseritaUgualePasswordNuova()) {
            erroreRinserNuovaPass.setVisible(false);
            return true;
        } else {
            erroreRinserNuovaPass.setText("Si prega di inserire due password uguali!");
            erroreRinserNuovaPass.setVisible(true);
            return false;
        }
    }

    private boolean isPasswordVecchiaValida(String password) {
        return GestoreServiceFacade.isPasswordVecchiaValida(
                utenteCheCambiaPassword.getAccountAgente().getEmail(), 
                password,
                utenteCheCambiaPassword.getToken()
        );
    }

    private boolean isPasswordReinseritaUgualePasswordNuova() {
        return reinserimentoNuovaPwd.getText().equals(nuovaPwdField.getText());
    }

    private void scriviMessaggioErrore(String s) {
        errorreVecchiaPass.setText(s);
    }

    private boolean isCambioAvvenuto() {
        new Thread(() -> {
            UtenteFacade.updatePassword(nuovaPwdField.getText(), utenteCheCambiaPassword);
        }).start();
        return true;
    }

    private void mostraAlertConfermaPassword() {
        Alert alert = AlertFactory.creaAlertSuccessPasswordCambiata(
                "Ok",
                "Operazione completata!",
                "La password è stata cambiata con successo.",
                "Complimenti! La password è stata aggiornata con successo.\nOra potrai utilizzare la nuova password per accedere a tutti i \nservizi di DietiEstates."
        );
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get().getText().equals("Ok")) {
            Stage stage = (Stage) salvaBottone.getScene().getWindow();
            stage.close();
            controllerPrecedente.setPasswordVecchia(pwdAttualeField.getText());
            controllerPrecedente.annullaCambioPassword();
        }
    }

    private static void mostraAlertErrorePassword() {
        Alert alert = AlertFactory.creaAlertErrore(
                "Errore nel cambio della password!",
                "Non è stato possibile cambiare la password. Siamo spiacenti, si è verificato un errore durante l'aggiornamento della password."
        );
        alert.showAndWait();
    }
}