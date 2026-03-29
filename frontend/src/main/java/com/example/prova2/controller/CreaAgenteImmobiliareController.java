package com.example.prova2.controller;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.function.UnaryOperator;

import com.example.prova2.controller.dashBoard.DashboardAmministratoreController;
import static com.example.prova2.controller.registrazione.RegistrazioneUtenteController.visualizzaPassword;
import com.example.prova2.facade.AgenteServiceFacade;
import com.example.prova2.facade.GestoreServiceFacade;
import com.example.prova2.factory.AlertFactory;
import com.example.prova2.model.Account;
import com.example.prova2.model.Agente;
import com.example.prova2.model.GestoreAgenziaImmobiliare;
import com.example.prova2.view.DashboardAmministratore;

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
import javafx.scene.control.TextFormatter;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class CreaAgenteImmobiliareController {

    // =========================================================================
    // COSTANTI (RegEx Pattern)
    // =========================================================================
    private static final String NAME_PATTERN = "^[a-zA-ZàèéìòùÀÈÉÌÒÙ'\\-\\s]{2,}$";
    protected static final String CFPATTERN = "^[A-Z]{6}[0-9]{2}[A-Z][0-9]{2}[A-Z][0-9]{3}[A-Z]$";
    protected static final String TELEFONOPATTERN = "^[0-9]{10}$";
    protected static final String EMAILPATTERN = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
    protected static final String PASSWORDPATTERN = "^.{8,}$";

    // =========================================================================
    // COMPONENTI UI (FXML)
    // =========================================================================
    @FXML private TextField textFieldNome;
    @FXML private TextField textFieldCognome;
    @FXML private TextField textFieldNumeroTelefono;
    @FXML private TextField textFieldEmail;
    @FXML private TextField textFieldCf;
    @FXML private PasswordField textFieldPassword;
    @FXML private TextField mostraPwd;

    @FXML private Label erroreNome;
    @FXML private Label erroreCognome;
    @FXML private Label erroreTelefono;
    @FXML private Label erroreEmail;
    @FXML private Label errorePassword;
    @FXML private Label erroreCodiceFiscale;
    @FXML private Label scrittaCaricamento;
    @FXML private Label puntiniCaricamento;

    @FXML private Button tornaIndietroBottone;
    @FXML private Button buttonCrea;
    @FXML private CheckBox mostraPasswordCheckBox;
    @FXML private ImageView logoCaricamento;

    // =========================================================================
    // VARIABILI DI STATO
    // =========================================================================
    protected Stage stagePrecedente;
    private Scene scenePrecedente;

    // =========================================================================
    // INIZIALIZZAZIONE E GETTER/SETTER
    // =========================================================================
    public void init() {
        addListenerMaiuscoleENoNumeri(textFieldNome);
        addListenerMaiuscoleENoNumeri(textFieldCognome);
        addListenerNumeri(textFieldNumeroTelefono);
        addListenerTuttoMaiuscolo(textFieldCf);
        mostraPwd.textProperty().bindBidirectional(textFieldPassword.textProperty());
    }

    public Scene getScenePrecedente() { return scenePrecedente; }
    public void setScenePrecedente(Scene scenePrecedente) { this.scenePrecedente = scenePrecedente; }

    public void setStagePrecedente(Stage stagePrecedente) { this.stagePrecedente = stagePrecedente; }

    // =========================================================================
    // AZIONI UI (Event Handlers)
    // =========================================================================
    @FXML
    public void registraCliente() {
        boolean flag = true;
        flag &= checkCampi();
        if (flag) {
            inviaDati();
        }
    }

    @FXML
    public void tornaIndietro() {
        if (stagePrecedente != null && scenePrecedente != null) {
            stagePrecedente.setScene(scenePrecedente);
        }
    }

    @FXML
    public void svuotaDati() {
        textFieldCf.clear();
        mostraPwd.clear();
        textFieldNumeroTelefono.clear();
        textFieldEmail.clear();
        textFieldPassword.clear();
        textFieldCognome.clear();
        textFieldNome.clear();
    }

    @FXML
    public void gestioneMostraPwd(ActionEvent actionEvent) {
        visualizzaPassword(mostraPasswordCheckBox, mostraPwd, textFieldPassword);
    }

    // Reset degli errori (chiamati dagli eventi in interfaccia)
    @FXML public void eliminaNomeErrorMsg() { eliminaMsgErrore(erroreNome); }
    @FXML public void eliminaMsgErroreCognome() { eliminaMsgErrore(erroreCognome); }
    @FXML public void eliminaMsgErroreCF() { eliminaMsgErrore(erroreCodiceFiscale); }
    @FXML public void eliminaMsgErroreNumeroTelefono() { eliminaMsgErrore(erroreTelefono); }
    @FXML public void eliminaMsgErroreEmail() { eliminaMsgErrore(erroreEmail); }
    @FXML public void eliminaMsgErrorePassword() { eliminaMsgErrore(errorePassword); }

    // =========================================================================
    // LOGICA DI SALVATAGGIO E NAVIGAZIONE
    // =========================================================================
    private void inviaDati() {
        if (salvaDatiConSuccesso()) {
            mostraPopUpConferma();
        }
    }

    private boolean salvaDatiConSuccesso() {
        Agente agente = new Agente(
                textFieldNome.getText().trim(),
                textFieldCognome.getText().trim(),
                textFieldCf.getText().trim(),
                textFieldNumeroTelefono.getText().trim()
        );
        Account accountAgente = new Account(textFieldEmail.getText(), textFieldPassword.getText());
        GestoreAgenziaImmobiliare gestoreRiferimento = DashboardAmministratoreController.getAdmin();
        
        agente.setAccountAgente(accountAgente);
        agente.setGestoreRiferimento(gestoreRiferimento);
        
        return AgenteServiceFacade.addAgente(agente, gestoreRiferimento);
    }

    private void mostraPopUpConferma() {
        Alert alert = creaSuccessMsg();
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent()) {
            if (result.get().getText().equals("Torna alla dashboard")) {
                navigaAllaDashboard();
            } else if (result.get().getText().equals("Continua a creare altri agenti")) {
                navigaACreaGestore();
            }
        }
    }

    private void navigaAllaDashboard() {
        try {
            DashboardAmministratore.initializePageDashboardAmministratore((Stage) buttonCrea.getScene().getWindow());
        } catch (IOException e) {
            AlertFactory.creaAlertErrore("Errore di navigazione", "Si è verificato un problema durante il caricamento della dashboard.");
        }
    }

    private void navigaACreaGestore() {
        // Logica futura per la navigazione
    }

    // =========================================================================
    // METODI DI VALIDAZIONE (Business Logic UI)
    // =========================================================================
    public boolean checkCampi() {
        boolean flag = true;
        flag &= isNomeValido(textFieldNome.getText());
        flag &= isCognomeValido(textFieldCognome.getText());
        flag &= isPasswordValida(textFieldPassword.getText().trim(), errorePassword, PASSWORDPATTERN);
        flag &= isEmailValida(textFieldEmail.getText().trim(), erroreEmail, EMAILPATTERN);
        flag &= isNumeroTelefonoValido(textFieldNumeroTelefono.getText().trim());
        flag &= isCfValido(textFieldCf.getText().trim());
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
        if (campoMatcha(phone, TELEFONOPATTERN)) { eliminaMsgErrore(erroreTelefono); return true; }
        return inserisciCampoValido(erroreTelefono, "Si prega di inserire un telefono valido!");
    }

    public boolean isCfValido(String cf) {
        if (cf == null) return false;
        if (campoVuoto(cf)) { scriviMessaggioErrore(erroreCodiceFiscale, "Si prega di inserire un codice fiscale!"); return false; }
        if (campoMatcha(cf, CFPATTERN)) { eliminaMsgErrore(erroreCodiceFiscale); return true; }
        return inserisciCampoValido(erroreCodiceFiscale, "Si prega di inserire un codice fiscale valido!");
    }

    @FXML
    private boolean checkDuplicatoCF(String cf) {
        List<String> cfs = GestoreServiceFacade.getCfGestore(DashboardAmministratoreController.getAdmin().getToken());
        if (exists(cf, cfs)) {
            scriviMessaggioErrore(erroreCodiceFiscale, "Si prega di inserire un codice fiscale non registrato!");
            return false;
        }
        return true;
    }

    @FXML
    private boolean checkDuplicatoEmail(String email) {
        List<String> emailList = GestoreServiceFacade.getEmailGestore(DashboardAmministratoreController.getAdmin());
        if (exists(email, emailList)) {
            scriviMessaggioErrore(erroreEmail, "Si prega di inserire una email non registrata!");
            return false;
        }
        return true;
    }

    // =========================================================================
    // METODI HELPER E UTILITY STATICHE
    // =========================================================================
    private void addListenerMaiuscoleENoNumeri(TextField text) {
        text.textProperty().addListener((observable, oldValue, newValue) -> {
            String filtered = newValue.replaceAll("[0-9]", "");
            if (!filtered.isEmpty()) {
                text.setText(filtered.substring(0, 1).toUpperCase() + filtered.substring(1));
            } else {
                text.setText("");
            }
        });
    }

    private void addListenerTuttoMaiuscolo(TextField text) {
        text.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.equals(newValue.toUpperCase())) {
                text.setText(newValue.toUpperCase());
            }
        });
    }

    private void addListenerNumeri(TextField textField) {
        aggiungi(textField);
    }

    static void aggiungi(TextField textField) {
        UnaryOperator<TextFormatter.Change> filter = change -> {
            String newText = change.getControlNewText();
            if (newText.matches("0|([1-9]\\d*)?")) return change;
            return null;
        };
        textField.setTextFormatter(new TextFormatter<>(filter));
    }

    public static Alert creaSuccessMsg() {
        return AlertFactory.creaAlertSuccess("Torna alla dashboard", "Continua a creare altri agenti", "Operazione completata!", "Hai creato con successo il tuo agente!", "Complimenti ti arriverà una notifica di riepilogo, ricordati che puoi sempre cancellare gli agenti creati nella tua dashboard");
    }

    public static boolean isEmailValida(String email, Label erroreLabel, String emailPattern) {
        if (email == null) return false;
        if (campoVuoto(email)) { scriviMessaggioErrore(erroreLabel, "Si prega di inserire una email!"); return false; }
        if (campoMatcha(email, emailPattern)) { eliminaMsgErrore(erroreLabel); return true; }
        return inserisciCampoValido(erroreLabel, "Si prega di inserire una email valida!");
    }

    public static boolean isPasswordValida(String password, Label errorePass, String passwordPattern) {
        return passwordValidation(password, errorePass, passwordPattern);
    }

    public static boolean passwordValidation(String password, Label errorePassword, String patternPassword) {
        if (password == null) return false;
        if (campoVuoto(password)) { scriviMessaggioErrore(errorePassword, "Si prega di inserire una password!"); return false; }
        if (campoMatcha(password, patternPassword)) { eliminaMsgErrore(errorePassword); return true; }
        return inserisciCampoValido(errorePassword, "Si prega di inserire una password valida!");
    }

    public static boolean inserisciCampoValido(Label label, String s) {
        scriviMessaggioErrore(label, s);
        return false;
    }

    public static boolean campoMatcha(String name, String pattern) {
        return name.matches(pattern);
    }

    public static void scriviMessaggioErrore(Label label, String text) {
        label.setText(text);
        label.setVisible(true);
    }

    public static boolean campoVuoto(String campo) {
        return campo.isEmpty();
    }

    public static void eliminaMsgErrore(Label label) {
        label.setVisible(false);
    }

    private boolean exists(String campo, List<String> campi) {
        for (String s : campi) {
            if (campo.equals(s)) return true;
        }
        return false;
    }
}