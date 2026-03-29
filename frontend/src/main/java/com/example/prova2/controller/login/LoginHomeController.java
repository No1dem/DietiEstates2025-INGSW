package com.example.prova2.controller.login;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import com.example.prova2.controller.CreaAgenteImmobiliareController;
import com.example.prova2.controller.dashBoard.DashboardAgenteController;
import com.example.prova2.controller.dashBoard.DashboardAmministratoreController;
import com.example.prova2.controller.registrazione.RegistrazioneUtenteController;
import com.example.prova2.dto.AgenteDTO;
import com.example.prova2.facade.AutenticazioneServiceFacade;
import com.example.prova2.factory.AlertFactory;
import com.example.prova2.model.Account;
import com.example.prova2.model.Agente;
import com.example.prova2.model.Cliente;
import com.example.prova2.model.GestoreAgenziaImmobiliare;
import com.example.prova2.view.AccediAdmin;
import com.example.prova2.view.DashboardAgente;
import com.example.prova2.view.DashboardAmministratore;
import com.example.prova2.view.HomePage;
import com.example.prova2.view.LoginAgenteImmobiliare;
import com.example.prova2.view.RegistrazioneUtente;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

public class LoginHomeController {

    // =========================================================================
    // COSTANTI (RegEx e Google OAuth)
    // =========================================================================
    protected static final String PASSWORDPATTERN = "^.{8,}$";
    protected static final String EMAILPATTERN = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";

    private static final String CLIENT_ID = "39635848827-mgl3pct74q6e40ir7ud9i889p439r6d0.apps.googleusercontent.com";
    private static final String REDIRECT_URI = "http://localhost:9094/auth/exchange_token";
    private static final String AUTH_URL = "https://accounts.google.com/o/oauth2/auth?response_type=code&client_id=" + CLIENT_ID + "&redirect_uri=" + REDIRECT_URI + "&scope=email";

    // =========================================================================
    // COMPONENTI UI (FXML)
    // =========================================================================
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private TextField mostraPwd;
    
    @FXML private Label registrazioneBtn;
    @FXML private Label emailErrorLbl;
    @FXML private Label pwdErrorLbl;

    @FXML private Button btnGoogle;
    @FXML private Button buttonAgenteImmobiliare;
    @FXML private Button accediBtn;
    @FXML private Button btnAmministratore;
    
    @FXML private CheckBox mostraPasswordCheckBox;

    // =========================================================================
    // VARIABILI DI STATO E GETTER/SETTER
    // =========================================================================
    private static String tokenId;

    public static void setToken(String token) { tokenId = token; }
    public String getEmail() { return emailField.getText(); }
    public String getPassword() { return passwordField.getText(); }

    // =========================================================================
    // INIZIALIZZAZIONE E SETUP UI
    // =========================================================================
    @FXML
    public void initialize() {
        sincronizzaPasswordFieldTextField();
        accediBtn.setDefaultButton(true);
    }

    private void sincronizzaPasswordFieldTextField() {
        mostraPwd.textProperty().bindBidirectional(passwordField.textProperty());
    }

    // =========================================================================
    // AZIONI UI (Event Handlers Navigazione e Login Standard)
    // =========================================================================
    @FXML
    public void gestioneAccesso() {
        boolean flag = true;
        flag &= checkEmail();
        flag &= checkPassword();
        if (campiValidi(flag) && credenzialiEsatte()) {
            apriHome();
        }
    }

    @FXML
    public void apriPaginaRegistrazioneUtente() {
        try {
           RegistrazioneUtente.initializePageRegistrazioneUtente(registrazioneBtn.getScene().getWindow());
        } catch (IOException e) {
            e.printStackTrace();
            AlertFactory.creaAlertErroreCaricamentoPagina();
        }
    }

    @FXML
    private void gestioneAgenteBtn() throws IOException {
        LoginAgenteImmobiliare.initializePageLoginAgente(buttonAgenteImmobiliare.getScene().getWindow());
    }

    @FXML
    private void gestioneAmministratoreBtn() throws IOException {
        AccediAdmin.initializePageAccediAdmin(btnAmministratore.getScene().getWindow());
    }

    @FXML
    public void gestioneMostraPwd() {
        RegistrazioneUtenteController.visualizzaPassword(mostraPasswordCheckBox, mostraPwd, passwordField);
    }

    // =========================================================================
    // LOGICA DI AUTENTICAZIONE E VALIDAZIONE (Login Standard)
    // =========================================================================
    private void apriHome() {
        try {
            Cliente utente = new Cliente();
            utente.setAccountAgente(new Account(getEmail(), getPassword()));
            utente.setToken(tokenId);
            HomePage.initializeHomePage((Stage) accediBtn.getScene().getWindow(), utente);
        } catch (IOException e) {
            e.printStackTrace();
            AlertFactory.creaAlertErroreCaricamentoPagina();
        }
    }

    private boolean credenzialiEsatte() {
        Account a = new Account(emailField.getText().trim(), passwordField.getText().trim());
        Cliente cliente = new Cliente();
        cliente.setAccountAgente(a);
        
        AgenteDTO dto = AutenticazioneServiceFacade.login(cliente.getAccountAgente().getEmail(), cliente.getAccountAgente().getPassword());
        
        if (dto != null) {
            if (!dto.getRole().equals("Cliente")) {
                AlertFactory.creaAlertErroreCredenziali();
                return false;
            }
            setToken(dto.getToken());
            return true;
        }
        
        AlertFactory.creaAlertErroreCredenziali();
        return false;
    }

    private static boolean campiValidi(boolean flag) { return flag; }

    private boolean checkEmail() {
        return CreaAgenteImmobiliareController.isEmailValida(emailField.getText(), emailErrorLbl, EMAILPATTERN);
    }

    private boolean checkPassword() {
        return CreaAgenteImmobiliareController.isPasswordValida(passwordField.getText(), pwdErrorLbl, PASSWORDPATTERN);
    }

    @FXML public void eliminaMsgErrorePassword() { eliminaMsgErrore(pwdErrorLbl); }
    @FXML public void eliminaMsgErroreEmail() { eliminaMsgErrore(emailErrorLbl); }

    public void eliminaMsgErrore(Label label) {
        label.setVisible(false);
    }

    // =========================================================================
    // AUTENTICAZIONE CON GOOGLE OAUTH2 E ROUTING DASHBOARD
    // =========================================================================
    @FXML
    public void authGoogle() {
        WebView webView = new WebView();
        WebEngine webEngine = webView.getEngine();
        webEngine.load(AUTH_URL);
        
        webEngine.locationProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.startsWith(REDIRECT_URI)) {
                String code = extractCodeFromUrl(newValue);
                if (code != null) {
                    AgenteDTO response = AutenticazioneServiceFacade.authWithGoogle(code);
                    Stage stage = (Stage) webView.getScene().getWindow();
                    stage.close();
                    apriPaginaPerUtente(response);
                }
            }
        });

        Stage stage = new Stage();
        stage.setScene(new Scene(webView));
        stage.show();
    }

    private String extractCodeFromUrl(String url) {
        try {
            URI uri = new URI(url);
            String query = uri.getQuery();
            for (String param : query.split("&")) {
                if (param.startsWith("code=")) {
                    return param.split("=")[1];
                }
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void apriPaginaPerUtente(AgenteDTO response) {
        switch (response.getRole()) {
            case "Cliente":
                apriPaginaCliente(response);
                break;
            case "Agente":
                apriPaginaAgente(response);
                break;
            case "Gestore":
                apriPaginaGestore(response);
                break;
        }
    }

    private void apriPaginaCliente(AgenteDTO response) {
        try {
            Cliente cliente = new Cliente();
            cliente.setToken(response.getToken());
            System.out.println("token giu:" + response.getToken());
            cliente.setAccountAgente(new Account(response.getEmail()));

            HomePage.initializeHomePageWithGoogle((Stage) accediBtn.getScene().getWindow(), cliente);
        } catch (IOException e) {
            e.printStackTrace();
            AlertFactory.creaAlertErroreCaricamentoPagina();
        }
    }

    private void apriPaginaAgente(AgenteDTO response) {
        try {
            Agente agente = new Agente();
            agente.setToken(response.getToken());
            agente.setAccountAgente(new Account(response.getEmail()));
            DashboardAgenteController.setAgente(agente);
            DashboardAgente.initializePageDashboardAgente(btnGoogle.getScene().getWindow());
        } catch (IOException e) {
            e.printStackTrace();
            AlertFactory.creaAlertErroreCaricamentoPagina();
        } catch (InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
            AlertFactory.creaAlertErroreCaricamentoPagina();
        }
    }

    private void apriPaginaGestore(AgenteDTO response) {
        try {
            GestoreAgenziaImmobiliare gestore = new GestoreAgenziaImmobiliare();
            gestore.setToken(response.getToken());
            gestore.setAccountAgente(new Account(response.getEmail()));
            gestore.setAdmin(response.isAdmin());
            DashboardAmministratoreController.setAdmin(gestore);
            DashboardAmministratore.initializePageDashboardAmministratore((Stage) btnGoogle.getScene().getWindow());
        } catch (IOException e) {
            e.printStackTrace();
            AlertFactory.creaAlertErroreCaricamentoPagina();
        }
    }
}