package com.example.prova2.controller.registrazione;

import java.io.IOException;
import java.util.Optional;

import com.example.prova2.dto.ClienteDTO;
import com.example.prova2.facade.ClienteServiceFacade;
import com.example.prova2.factory.AlertFactory;
import com.example.prova2.model.Account;
import com.example.prova2.model.Cliente;
import com.example.prova2.view.HomePage;
import com.example.prova2.view.LoginPage;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class RegistrazioneUtenteController {

    // =========================================================================
    // COSTANTI (RegEx Pattern)
    // =========================================================================
    private static final String PASSWORDPATTERN = "^.{8,}$";
    protected static final String EMAIL_PATTERN = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
    private static final String NAME_PATTERN = "^[a-zA-ZàèéìòùÀÈÉÌÒÙ'\\-\\s]{2,}$";
    private static final String PHONE_PATTERN = "^\\d{10}$";

    // =========================================================================
    // COMPONENTI UI (FXML)
    // =========================================================================
    @FXML private TextField textFieldNome;
    @FXML private TextField textFieldCognome;
    @FXML private TextField textFieldTelefono;
    @FXML private TextField textFieldEmail;
    @FXML private PasswordField passwordField;
    @FXML private TextField mostraPwd;

    @FXML private Label erroreNome;
    @FXML private Label erroreCognome;
    @FXML private Label erroreTelefono;
    @FXML private Label erroreMail;
    @FXML private Label errorePassword;

    @FXML private Button tornaSchermataInizialeBtn;
    @FXML private CheckBox mostraPasswordCheckBox;

    // =========================================================================
    // VARIABILI DI STATO E GETTER/SETTER
    // =========================================================================
    private String tokenId;

    public String getTokenId() { return tokenId; }
    public void setTokenId(String tokenId) { this.tokenId = tokenId; }

    // =========================================================================
    // INIZIALIZZAZIONE E SETUP
    // =========================================================================
    public void init() {
        addListenerMaiuscoleENoNumeri(textFieldNome);
        addListenerMaiuscoleENoNumeri(textFieldCognome);
        addListenerMaxCampo(textFieldEmail);
        addListenerMaxCampo(passwordField);
        addListenerMaxCampo(mostraPwd);
        mostraPwd.textProperty().bindBidirectional(passwordField.textProperty());
    }

    private void addListenerMaiuscoleENoNumeri(TextField text) {
        text.textProperty().addListener((observable, oldValue, newValue) -> {
            String filtered = newValue.replaceAll("[0-9]", "");
            if (filtered.length() > 20) {
                filtered = filtered.substring(0, 20); // Limita a 20 caratteri
            }
            if (!filtered.isEmpty()) {
                text.setText(filtered.substring(0, 1).toUpperCase() + filtered.substring(1));
            } else {
                text.setText("");
            }
        });
    }

    private void addListenerMaxCampo(TextField field) {
        field.textProperty().addListener((observable, oldValue, newValue) -> {
            String filtered = newValue.replaceAll("[0-9]", "");
            if (filtered.length() > 40) {
                filtered.substring(0, 40); // NOTA: questa riga non salva il risultato, ma mantengo la tua logica originale
            }
        });
    }

    // =========================================================================
    // AZIONI UI (Event Handlers)
    // =========================================================================
    @FXML
    public void registraCliente() {
        if (checkCampi()) {
            Account cliente = new Account(textFieldEmail.getText().trim(), passwordField.getText().trim());
            ClienteDTO dto = ClienteServiceFacade.registrati(new Cliente(textFieldNome.getText().trim(), textFieldCognome.getText().trim(), cliente));
            if (dto != null) {
                setTokenId(dto.getToken());
                Platform.runLater(() -> { 
                    isErroreInEmail(dto);
                });
            }
        }
    }

    @FXML
    public void tornaSchermataIniziale() {
        try {
            LoginPage.startMainApp((Stage) tornaSchermataInizialeBtn.getScene().getWindow());
        } catch (IOException e) {
            AlertFactory.creaAlertErroreCaricamentoPagina();
        }
    }

    @FXML
    public void resetField() {
        textFieldNome.setText("");
        textFieldCognome.setText("");
        textFieldEmail.setText("");
        passwordField.setText("");
        mostraPwd.setText("");
    }

    @FXML
    public void gestioneMostraPwd() {
        visualizzaPassword(mostraPasswordCheckBox, mostraPwd, passwordField);
    }

    // Reset degli errori (chiamati dagli eventi UI in JavaFX)
    @FXML public void eliminaNomeErrorMsg() { eliminaMsgErrore(erroreNome); }
    @FXML public void eliminaMsgErroreCognome() { eliminaMsgErrore(erroreCognome); }
    @FXML public void eliminaMsgErroreNumeroTelefono() { eliminaMsgErrore(erroreTelefono); }
    @FXML public void eliminaMsgErroreEmail() { eliminaMsgErrore(erroreMail); }
    @FXML public void eliminaMsgErrorePassword() { eliminaMsgErrore(errorePassword); }

    // =========================================================================
    // LOGICA DI SALVATAGGIO, RISPOSTA E NAVIGAZIONE
    // =========================================================================
    private void isErroreInEmail(ClienteDTO dto) {
        if (dto.isDuplicato()) {
            erroreMail.setVisible(true);
            erroreMail.setText("L'email è già in uso, scegli un'altra!");
            return;
        }
        if (dto.isErroreInterno()) {
           AlertFactory.creaAlertErroreInterno();
           return;
        }
        visualizzaMessaggioConferma();
    }

    public void visualizzaMessaggioConferma() {
        Alert alert = creaSuccessMsg();
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent()) {
            if (result.get().getText().equals("Torna alla pagina di accesso")) {
                apriLoginPage();
            } else if (result.get().getText().equals("Inizia a esplorare le opportunità immobiliari")) {
                apriHome();
            }
        }
    }

    public void apriLoginPage() {
        try {
            LoginPage.startMainApp((Stage) tornaSchermataInizialeBtn.getScene().getWindow());
        } catch (IOException e) {
            AlertFactory.creaAlertErroreCaricamentoPagina();
        }
    }

    public void apriHome() {
        try {
            Cliente cliente = new Cliente();
            cliente.setAccountAgente(new Account(textFieldEmail.getText(), passwordField.getText()));
            cliente.setToken(tokenId);
            System.out.println(tokenId);
            HomePage.initializeHomePage((Stage) tornaSchermataInizialeBtn.getScene().getWindow(), cliente);
        } catch (IOException e) {
            AlertFactory.creaAlertErroreCaricamentoPagina();
        }
    }

    // =========================================================================
    // VALIDAZIONE E METODI HELPER
    // =========================================================================
    public boolean checkCampi() {
        boolean flag = true;
        flag &= isNomeValido(textFieldNome.getText());
        flag &= isCognomeValido(textFieldCognome.getText());
        flag &= isPasswordValida(passwordField.getText().trim());
        flag &= isEmailValida(textFieldEmail.getText().trim());
        return flag;
    }

    public boolean isNomeValido(String name) {
        if (name == null) return false;
        if (campoVuoto(name)) { scriviMessaggioErrore(erroreNome, "Si prega di inserire un nome!"); return false; }
        if (campoMatcha(name, NAME_PATTERN)) { eliminaMsgErrore(erroreNome); return true; }
        return inserisciCampoValido(erroreNome, "Si prega di inserire un nome valido!");
    }

    public boolean isCognomeValido(String cognome) {
        if (cognome == null) return false;
        if (campoVuoto(cognome)) { scriviMessaggioErrore(erroreCognome, "Si prega di inserire un cognome!"); return false; }
        if (campoMatcha(cognome, NAME_PATTERN)) { eliminaMsgErrore(erroreCognome); return true; }
        return inserisciCampoValido(erroreCognome, "Si prega di inserire un cognome valido!");
    }

    public boolean isNumeroTelefonoValido(String phone) {
        if (phone == null) return false;
        if (campoVuoto(phone)) { scriviMessaggioErrore(erroreTelefono, "Si prega di inserire un numero di telefono!"); return false; }
        if (campoMatcha(phone, PHONE_PATTERN)) { eliminaMsgErrore(erroreTelefono); return true; }
        return inserisciCampoValido(erroreTelefono, "Si prega di inserire un telefono valido!");
    }

    public boolean isEmailValida(String email) {
        if (email == null) return false;
        if (campoVuoto(email)) { scriviMessaggioErrore(erroreMail, "Si prega di inserire una email!"); return false; }
        if (campoMatcha(email, EMAIL_PATTERN)) { eliminaMsgErrore(erroreMail); return true; }
        return inserisciCampoValido(erroreMail, "Si prega di inserire una email valida!");
    }

    public boolean isPasswordValida(String password) {
        return passwordValidation(password, errorePassword, PASSWORDPATTERN);
    }

    // I METODI STATIC DI SUPPORTO 
    public static Alert creaSuccessMsg() {
        return AlertFactory.creaAlertSuccess(
                "Torna alla pagina di accesso",
                "Inizia a esplorare le opportunità immobiliari",
                "Registrazione Completata con Successo",
                "Benvenuto in DietiEstates25",
                "Siamo lieti di darti il benvenuto su DietiEstates25. Ora puoi iniziare a cercare la tua futura casa con facilità e sicurezza. Se hai bisogno di assistenza, non esitare a contattarci."
        );
    }

    public static void visualizzaPassword(CheckBox mostraPasswordCheckBox, TextField mostraPwd, PasswordField passwordField) {
        if (mostraPasswordCheckBox.isSelected()) {
            mostraPwd.setVisible(true);
            mostraPwd.setEditable(true);
            passwordField.setVisible(false);
        } else {
            mostraPwd.setVisible(false);
            mostraPwd.setEditable(false);
            passwordField.setVisible(true);
        }
    }

    public static boolean passwordValidation(String password, Label errorePassword, String passwordpattern) {
        if (password == null) return false;
        if (campoVuoto(password)) { scriviMessaggioErrore(errorePassword, "Si prega di inserire una password!"); return false; }
        if (campoMatcha(password, passwordpattern)) { eliminaMsgErrore(errorePassword); return true; }
        return inserisciCampoValido(errorePassword, "Si prega di inserire una password valida!");
    }

    public static boolean inserisciCampoValido(Label lbl, String s) {
        scriviMessaggioErrore(lbl, s);
        return false;
    }

    public static boolean campoMatcha(String name, String pattern) {
        return name.matches(pattern);
    }

    public static void scriviMessaggioErrore(Label lbl, String text) {
        lbl.setText(text);
        lbl.setVisible(true);
    }

    public static boolean campoVuoto(String campo) {
        return campo.isEmpty();
    }

    public static void eliminaMsgErrore(Label lbl) {
        lbl.setVisible(false);
    }
}