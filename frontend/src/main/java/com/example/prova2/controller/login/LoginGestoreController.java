package com.example.prova2.controller.login;

import java.io.IOException;

import com.example.prova2.controller.dashBoard.DashboardAmministratoreController;
import com.example.prova2.controller.registrazione.RegistrazioneUtenteController;
import com.example.prova2.dto.AgenteDTO;
import com.example.prova2.facade.AutenticazioneServiceFacade;
import com.example.prova2.factory.AlertFactory;
import com.example.prova2.model.Account;
import com.example.prova2.model.GestoreAgenziaImmobiliare;
import com.example.prova2.view.DashboardAmministratore;
import com.example.prova2.view.LoginPage;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginGestoreController {

    // =========================================================================
    // COSTANTI (RegEx Pattern)
    // =========================================================================
    protected static final String PASSWORDPATTERN = "^.{8,}$";
    protected static final String EMAILPATTERN = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";

    // =========================================================================
    // COMPONENTI UI (FXML)
    // =========================================================================
    @FXML private TextField usernameTextField;
    @FXML private PasswordField passwordTextField;
    @FXML private TextField mostraPwd;
    
    @FXML private Label errorLabel;
    @FXML private Label compilaPassword;
    
    @FXML private Button bottoneAccedi;
    @FXML private Button tornaAllaHomeBtn;
    @FXML private CheckBox mostraPasswordCheckBox;

    // =========================================================================
    // INIZIALIZZAZIONE E SETUP UI
    // =========================================================================
    @FXML
    public void initialize() {
        sincronizzaPasswordFieldTextField();
        bottoneAccedi.setDefaultButton(true);
    }

    private void sincronizzaPasswordFieldTextField() {
        mostraPwd.textProperty().bindBidirectional(passwordTextField.textProperty());
    }

    // =========================================================================
    // AZIONI UI (Event Handlers)
    // =========================================================================
    @FXML
    public void accedi() {
        if (!controllaCampi()) {
            return;
        }
        String username = usernameTextField.getText();
        String password = passwordTextField.getText();
        
        Account account = new Account(username, password);
        GestoreAgenziaImmobiliare gestoreAgenziaImmobiliare = new GestoreAgenziaImmobiliare();
        gestoreAgenziaImmobiliare.setAccountAgente(account);
        
        AgenteDTO dto = AutenticazioneServiceFacade.login(gestoreAgenziaImmobiliare.getAccountAgente().getEmail(), gestoreAgenziaImmobiliare.getAccountAgente().getPassword());
        
        if (dto != null) {
            apriDashBoard(new Account(username, password), dto);
            return;
        }
        
        AlertFactory.creaAlertErroreCredenziali();
    }

    @FXML
    public void tornaHome() {
        try {
            LoginPage.startMainApp((Stage) tornaAllaHomeBtn.getScene().getWindow());
        } catch (IOException e) {
            AlertFactory.creaAlertErroreCaricamentoPagina();
            e.printStackTrace();
        }
    }

    @FXML
    public void resetLogin() {
        usernameTextField.clear();
        passwordTextField.clear();
        mostraPwd.clear();
    }

    @FXML
    public void gestioneMostraPwd() {
        RegistrazioneUtenteController.visualizzaPassword(mostraPasswordCheckBox, mostraPwd, passwordTextField);
    }

    @FXML
    public void eliminaMessaggioDiErroreUsr() {
        errorLabel.setText("");
    }

    @FXML
    public void eliminaMessaggioDiErrorePass() {
        compilaPassword.setText("");
    }

    // =========================================================================
    // LOGICA DI AUTENTICAZIONE E NAVIGAZIONE
    // =========================================================================
    public void apriDashBoard(Account accountGestore, AgenteDTO dto) {
        try {
            GestoreAgenziaImmobiliare gestore = creaGestore(accountGestore, dto);
            DashboardAmministratoreController.setAdmin(gestore);
            DashboardAmministratore.initializePageDashboardAmministratore((Stage) bottoneAccedi.getScene().getWindow());
        } catch (IOException e) {
            e.printStackTrace();
            gestisciErroreCaricamentoDashboard(e);
        }
    }

    private GestoreAgenziaImmobiliare creaGestore(Account accountGestore, AgenteDTO dto) {
        GestoreAgenziaImmobiliare gestore = new GestoreAgenziaImmobiliare();
        gestore.setAccountAgente(accountGestore);
        gestore.setAdmin(dto.isAdmin());
        gestore.setToken(dto.getToken());
        return gestore;
    }

    private void gestisciErroreCaricamentoDashboard(Exception e) {
        AlertFactory.creaAlertErroreCaricamentoPagina();
        if (e instanceof InterruptedException) {
            Thread.currentThread().interrupt();
        }
        e.printStackTrace();
    }

    // =========================================================================
    // METODI DI VALIDAZIONE (Helper)
    // =========================================================================
    public boolean controllaCampi() {
        boolean flag = true;
        flag &= checkCampo(usernameTextField.getText(), errorLabel, EMAILPATTERN, "Si prega di inserire una email valida!");
        flag &= checkCampo(passwordTextField.getText(), compilaPassword, PASSWORDPATTERN, "Si prega di inserire una password valida!");
        return flag;
    }

    public boolean checkCampo(String username, Label errorLabel, String pattern, String message) {
        if (username.matches(pattern)) {
            return true;
        } else {
            if (username.isEmpty()) {
                errorLabel.setVisible(true);
                errorLabel.setText("Si prega di compilare questo campo!");
            } else {
                errorLabel.setVisible(true);
                errorLabel.setText(message);
            }
            return false;
        }
    }
}