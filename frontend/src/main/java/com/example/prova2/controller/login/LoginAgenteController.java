package com.example.prova2.controller.login;

import java.io.IOException;

import com.example.prova2.controller.dashBoard.DashboardAgenteController;
import static com.example.prova2.controller.registrazione.RegistrazioneUtenteController.campoMatcha;
import static com.example.prova2.controller.registrazione.RegistrazioneUtenteController.campoVuoto;
import static com.example.prova2.controller.registrazione.RegistrazioneUtenteController.eliminaMsgErrore;
import static com.example.prova2.controller.registrazione.RegistrazioneUtenteController.inserisciCampoValido;
import static com.example.prova2.controller.registrazione.RegistrazioneUtenteController.passwordValidation;
import static com.example.prova2.controller.registrazione.RegistrazioneUtenteController.scriviMessaggioErrore;
import static com.example.prova2.controller.registrazione.RegistrazioneUtenteController.visualizzaPassword;
import com.example.prova2.dto.AgenteDTO;
import com.example.prova2.facade.AutenticazioneServiceFacade;
import com.example.prova2.factory.AlertFactory;
import com.example.prova2.model.Account;
import com.example.prova2.model.Agente;
import com.example.prova2.view.DashboardAgente;
import com.example.prova2.view.LoginPage;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginAgenteController {

    // =========================================================================
    // COSTANTI 
    // =========================================================================
    protected static final String PASSWORDPATTERN = "^.{8,}$";
    protected static final String EMAILPATTERN = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";

    // =========================================================================
    // COMPONENTI UI 
    // =========================================================================
    @FXML private TextField textFieldEmail;
    @FXML private PasswordField textFieldPassword;
    @FXML private TextField mostraPwd;
    
    @FXML private Label errorUsername;
    @FXML private Label errorPassword;
    
    @FXML private Button loginButton;
    @FXML private Button tornaHome;
    @FXML private CheckBox mostraPasswordCheckBox;

    // =========================================================================
    // VARIABILI DI STATO E GETTER/SETTER
    // =========================================================================
    private Agente agenteLogin;

    public Agente getAgente() { return agenteLogin; }
    public void setAgente(Agente agente) { this.agenteLogin = agente; }

    // =========================================================================
    // INIZIALIZZAZIONE E SETUP UI
    // =========================================================================
    @FXML
    public void initialize() {
        loginButton.setDefaultButton(true);
        sincronizzaPasswordFieldTextField();
    }

    private void sincronizzaPasswordFieldTextField() {
        mostraPwd.textProperty().bindBidirectional(textFieldPassword.textProperty());
    }

    // =========================================================================
    // AZIONI UI 
    // =========================================================================
    @FXML
    public void handlerLogin() {
        if (checkCampi()) {
            inviaDati();
        }
    }

    @FXML
    public void resetLogin() {
        textFieldEmail.clear();
        textFieldPassword.clear();
        mostraPwd.clear();
    }

    @FXML
    public void gestioneMostraPwd() {
        visualizzaPassword(mostraPasswordCheckBox, mostraPwd, textFieldPassword);
    }

    @FXML
    public void tornaHome() {
        try {
            LoginPage.startMainApp((Stage) tornaHome.getScene().getWindow());
        } catch (IOException e) {
            e.printStackTrace();
            AlertFactory.creaAlertErroreCaricamentoPagina();
        }
    }

    // Reset degli errori (chiamati dagli eventi UI)
    @FXML public void nascondiMessaggioErroreUsername() { errorUsername.setVisible(false); }
    @FXML public void nascondiMessaggioErrorePwd() { errorPassword.setVisible(false); }

    // =========================================================================
    // LOGICA DI AUTENTICAZIONE E NAVIGAZIONE
    // =========================================================================
    private void inviaDati() {
        Account accountSemplice = new Account(textFieldEmail.getText().trim(), textFieldPassword.getText().trim());
        gestisciAuth(accountSemplice);
    }

    private void gestisciAuth(Account accountSemplice) {
        if (isAuthRiuscita(accountSemplice)) {
            apriDashBoardAgente();
        }
    }

    private boolean isAuthRiuscita(Account accountSemplice) {
        Agente agente = new Agente();
        agente.setAccountAgente(accountSemplice);
        
        AgenteDTO dto = AutenticazioneServiceFacade.login(agente.getAccountAgente().getEmail(), agente.getAccountAgente().getPassword());
        
        if (dto == null) {
            AlertFactory.creaAlertErroreCredenziali();
            return false;
        }
        if (!dto.getRole().equals("Agente")) {
            AlertFactory.creaAlertErroreCredenziali();
            return false;
        }
        
        agenteLogin = new Agente();
        agenteLogin.setToken(dto.getToken());
        return true;
    }

    public void apriDashBoardAgente() {
        try {
            Agente agenteDash = new Agente();
            agenteDash.setAccountAgente(new Account(textFieldEmail.getText().trim(), textFieldPassword.getText().trim()));
            agenteDash.setToken(agenteLogin.getToken());
            
            DashboardAgenteController.setAgente(agenteDash);
            DashboardAgente.initializePageDashboardAgente(loginButton.getScene().getWindow());
        } catch (IOException e) {
            e.printStackTrace();
            AlertFactory.creaAlertErroreCaricamentoPagina();
        } catch (InterruptedException e) {
            AlertFactory.creaAlertErroreCaricamentoPagina();
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }
    }

    // =========================================================================
    // METODI HELPER E VALIDAZIONE LOGICA
    // =========================================================================
    private boolean checkCampi() {
        boolean flag = true;
        flag &= checkEmail();
        flag &= checkPassword();
        return flag;
    }

    public boolean checkEmail() {
        String username = this.textFieldEmail.getText();
        return isEmailValida(username);
    }

    public boolean checkPassword() {
        String password = this.textFieldPassword.getText();
        return isPasswordValida(password);
    }

    public boolean isEmailValida(String email) {
        if (email == null) return false;
        if (campoVuoto(email)) { scriviMessaggioErrore(errorUsername, "Si prega di inserire una email!"); return false; }
        if (campoMatcha(email, EMAILPATTERN)) { eliminaMsgErrore(errorUsername); return true; }
        return inserisciCampoValido(errorUsername, "Si prega di inserire uno email valida!");
    }

    public boolean isPasswordValida(String password) {
        return passwordValidation(password, errorPassword, PASSWORDPATTERN);
    }
}