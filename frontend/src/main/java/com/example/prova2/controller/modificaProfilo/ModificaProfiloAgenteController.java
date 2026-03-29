package com.example.prova2.controller.modificaProfilo;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.UnaryOperator;

import com.example.prova2.controller.dashBoard.DashboardAgenteController;
import static com.example.prova2.controller.registrazione.RegistrazioneUtenteController.campoVuoto;
import static com.example.prova2.controller.registrazione.RegistrazioneUtenteController.eliminaMsgErrore;
import com.example.prova2.facade.ModificaProfiloFacade;
import com.example.prova2.factory.AlertFactory;
import com.example.prova2.model.Account;
import com.example.prova2.model.Agente;
import com.example.prova2.model.Utente;
import com.example.prova2.view.CambiaFoto;
import com.example.prova2.view.CambioPassword;
import com.example.prova2.view.DashboardAgente;

import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.binding.BooleanBinding;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;

public class ModificaProfiloAgenteController implements ModificaPasswordInterface {

    // =========================================================================
    // COSTANTI (RegEx)
    // =========================================================================
    protected static final String EMAIL_PATTERN = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
    private static final String NAME_PATTERN = "^[a-zA-ZàèéìòùÀÈÉÌÒÙ'\\-\\s]{2,}$";
    private static final String PHONE_PATTERN = "^\\d{10}$";

    // =========================================================================
    // COMPONENTI UI (FXML) - PANNELLI E IMMAGINI
    // =========================================================================
    @FXML protected Pane paneAnnullaCambio;
    @FXML protected Pane paneConfermaModifica;
    @FXML protected ImageView fotoProfilo; 

    // =========================================================================
    // COMPONENTI UI (FXML) - TEXTFIELD E LABEL
    // =========================================================================
    @FXML protected TextField textFieldNome;
    @FXML protected  TextField textFieldCognome;
    @FXML protected TextField textFieldNumeroTelefono;
    @FXML protected TextField textFieldEmail;
    @FXML protected  TextField textFieldBio;
    
    @FXML protected Label nomeEcognome;
    @FXML protected Label erroreNome;
    @FXML protected Label erroreCognome;
    @FXML protected Label erroreTelefono;
    @FXML protected Label erroreEmail;
    @FXML protected Label erroreBio;

    // =========================================================================
    // COMPONENTI UI (FXML) - BOTTONI
    // =========================================================================
    @FXML protected Button tornaIndietroBottone;
    @FXML protected Button aggiornaDati;
    @FXML protected Button btnCancella;
    @FXML protected Button btnCancellaBio;
    @FXML protected Button aggiornaBio;

    // =========================================================================
    // VARIABILI DI STATO E GETTER/SETTER
    // =========================================================================
    protected  static Utente utente;
    private String passwordVecchia;

    public static void setUtente(Utente agenteDash) { utente = agenteDash; }
    public Utente getAgente() { return utente; }

    public String getPasswordVecchia() { return passwordVecchia; }
    @Override public void setPasswordVecchia(String password) { this.passwordVecchia = password; }

    // =========================================================================
    // INIZIALIZZAZIONE E SETUP UI
    // =========================================================================
    public void initProfilo() {
        aggiornaDati.setDefaultButton(true);
        CompletableFuture.supplyAsync(() -> {
            initTextField();
            DashboardAgenteController.setAgenteNuovo((Agente) utente);
            return DashboardAgenteController.getAgente();
        })
        .thenAccept(agente -> Platform.runLater(() -> {
            setDatiOnTextField(agente);
            setBtnsQuandoCampiModificati();
            setBottoniBio(textFieldBio.textProperty().isNotEqualTo(agente.getBio()), aggiornaBio, btnCancellaBio);
        }))
        .thenRunAsync(this::setFotoProfiloAsincrono);
    }

    public void initTextField() {
        addListenerMaiuscoleENoNumeri(textFieldNome);
        addListenerMaiuscoleENoNumeri(textFieldCognome);
        addListenerNumeri(textFieldNumeroTelefono);
    }

    protected void setDatiOnTextField(Utente utente) {
        Platform.runLater(() -> {
            nomeEcognome.setText(utente.getNome() + " " + utente.getCognome());
            textFieldNome.setText(utente.getNome());
            textFieldCognome.setText(utente.getCognome());
            textFieldEmail.setText(utente.getAccountAgente().getEmail());
            textFieldNumeroTelefono.setText(utente.getTelefono());
            textFieldBio.setText(utente.getBio());
        });
    }

    private void setFotoProfiloAsincrono() {
        setFotoProfilo(utente.getFotoProfilo().getPath());
    }

    public void setFotoProfilo(String foto) {
        fotoProfilo.setImage(new Image(foto));
    }

    public void setBtnsQuandoCampiModificati() {
        BooleanBinding campiModificati = textFieldNome.textProperty().isNotEqualTo(utente.getNome())
                .or(textFieldCognome.textProperty().isNotEqualTo(utente.getCognome()))
                .or(textFieldNumeroTelefono.textProperty().isNotEqualTo(utente.getTelefono()))
                .or(textFieldEmail.textProperty().isNotEqualTo(utente.getAccountAgente().getEmail()));
        
        btnCancella.visibleProperty().bind(campiModificati);
        aggiornaDati.setVisible(true);
        aggiornaDati.disableProperty().bind(campiModificati.not());
    }

    public void setBottoniBio(BooleanBinding textFieldBio, Button aggiornaBio, Button btnCancellaBio) {
        aggiornaBio.setVisible(true);
        aggiornaBio.disableProperty().bind(textFieldBio.not());
        btnCancellaBio.visibleProperty().bind(textFieldBio);
    }

    // =========================================================================
    // AZIONI UTENTE (Event Handlers)
    // =========================================================================
    @FXML
    public void salvaDati() {
        if (checkCampi()) {
            Agente dati = new Agente(textFieldNome.getText(), textFieldCognome.getText(), textFieldNumeroTelefono.getText());
            dati.setCf(utente.getCf());
            dati.setAccountAgente(new Account(textFieldEmail.getText()));
            dati.setToken(DashboardAgenteController.getAgente().getToken());
            
            if (ModificaProfiloFacade.updateDatiAgente(dati, DashboardAgenteController.getAgente().getAccountAgente().getEmail()) != null) {
                paneConfermaModifica.setVisible(true);
                PauseTransition pausa = new PauseTransition(Duration.seconds(4));
                pausa.setOnFinished(event -> paneConfermaModifica.setVisible(false));
                pausa.play();
                
                aggiornaAgenteProfilo(dati);
                initProfilo();
            }
        }
    }

    @FXML
    public void aggiornaBiografia() {
        if (checkBio()) {
            AlertFactory.creaAlertSuccessModificaBio();
            CompletableFuture.supplyAsync(() -> {
                if (invioBioNuova()) {
                    utente.setBio(textFieldBio.getText());
                    initProfilo();
                }
                return null;
            });
        }
    }

    @FXML
    public void resetBio() {
        textFieldBio.setText(DashboardAgenteController.getAgente().getBio());
    }

    @FXML
    public void apriPaginaModificaFoto() {
        CambiaFoto cambiaFoto = new CambiaFoto();
        cambiaFoto.initPage((Stage) fotoProfilo.getScene().getWindow());
        
        // Aggiorniamo l'immagine quando il popup si chiude
        if (utente.getFotoProfilo() != null && utente.getFotoProfilo().getPath() != null) {
            fotoProfilo.setImage(new Image(utente.getFotoProfilo().getPath()));
        }
    }

    @FXML
    public void apriPaginaModificaPassword() {
        try {
            CambioPassword.initializePageCambioPassword(btnCancella.getScene().getWindow(), this, utente);
        } catch (IOException e) {
            e.printStackTrace();
            AlertFactory.creaAlertErrore("Errore apertura pagina!", "Siamo spiacenti si è verificato un errore nel caricamento della pagina cambio password");
        }
    }

    @FXML
    public void annullaModificheDati() {
        setDatiOnTextField(utente);
        levaTuttiIMessaggiDiErrore();
    }

    @FXML
    public void tornaIndietro() {
        try {
            DashboardAgente.initializePageDashboardAgente(tornaIndietroBottone.getScene().getWindow());
        } catch (IOException e) {
            AlertFactory.creaAlertErroreCaricamentoPagina();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            AlertFactory.creaAlertErroreCaricamentoPagina();
        }
    }

    @FXML
    public void tornaIndietroPassword() {
        new Thread(() -> { 
            ModificaProfiloFacade.updatePasswordAgente(passwordVecchia, DashboardAgenteController.getAgente());
        }).start();
        Platform.runLater(() -> { paneAnnullaCambio.setVisible(false); });
    }

    // Gestione degli errori (Reset label)
    @FXML public void eliminaNomeErrorMsg() { eliminaMsgErrore(erroreNome); }
    @FXML public void eliminaMsgErroreCognome() { eliminaMsgErrore(erroreCognome); }
    @FXML public void eliminaMsgErroreNumeroTelefono() { eliminaMsgErrore(erroreTelefono); }
    @FXML public void eliminaMsgErroreEmail() { eliminaMsgErrore(erroreEmail); }

    private void levaTuttiIMessaggiDiErrore() {
        eliminaNomeErrorMsg();
        eliminaMsgErroreCognome();
        eliminaMsgErroreNumeroTelefono();
        eliminaMsgErroreEmail();
    }

    // =========================================================================
    // METODI HELPER E VALIDAZIONE LOGICA (DB/Dati)
    // =========================================================================
    @Override
    public void annullaCambioPassword() {
        Platform.runLater(() -> {
            paneAnnullaCambio.setVisible(true);
            Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(15), event -> {
                paneAnnullaCambio.setVisible(false);
            }));
            timeline.setCycleCount(1);
            timeline.play();
        });
    }

    public void caricaFotoNuova() {
        CompletableFuture.supplyAsync(() -> {
            List<String> urlList = ModificaProfiloFacade.getFotoAgente(utente.getCf());
            System.out.println("aaa" + urlList.getFirst());
            String urlE = ModificaProfiloFacade.getImageFromS3(urlList.getFirst());
            System.out.println(urlE);
            aggiornaAgente(urlE);
            return null;
        });
    }

    private void aggiornaAgente(String url) {
        fotoProfilo.setImage(new Image(url));
        utente.getFotoProfilo().setPath(fotoProfilo.getImage().getUrl());
        setFotoProfiloAsincrono();
    }

    private void aggiornaAgenteProfilo(Agente nuovo) {
        utente.setCf(nuovo.getCf());
        utente.setAccountAgente(new Account(textFieldEmail.getText()));
        utente.setNome(nuovo.getNome());
        utente.setCognome(nuovo.getCognome());
        utente.setTelefono(nuovo.getTelefono());
    }

    private boolean invioBioNuova() {
        return ModificaProfiloFacade.updateBio(textFieldBio.getText(), DashboardAgenteController.getAgente().getCf(), DashboardAgenteController.getAgente().getToken());
    }

    public boolean checkCampi() {
        boolean flag = true;
        flag &= isNomeValido(textFieldNome.getText());
        flag &= isCognomeValido(textFieldCognome.getText());
        flag &= isEmailValida(textFieldEmail.getText().trim());
        flag &= isNumeroTelefonoValido(textFieldNumeroTelefono.getText().trim());
        return flag;
    }

    public boolean checkBio() {
        if (textFieldBio.getText().length() >= 3000) {
            erroreBio.setVisible(true);
            return false;
        }
        return true;
    }

    // --- Metodi di Validazione Base (Regex) ---
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

    public boolean isNumeroTelefonoValido(String numeroTelefono) {
        if (numeroTelefono == null) return false;
        if (campoVuoto(numeroTelefono)) { scriviMessaggioErrore(erroreTelefono, "Si prega di inserire un numero di telefono!"); return false; }
        if (campoMatcha(numeroTelefono, PHONE_PATTERN)) { eliminaMsgErrore(erroreTelefono); return true; }
        return inserisciCampoValido(erroreTelefono, "Si prega di inserire un telefono valido!");
    }

    public boolean isEmailValida(String email) {
        if (email == null) return false;
        if (campoVuoto(email)) { scriviMessaggioErrore(erroreEmail, "Si prega di inserire una email!"); return false; }
        if (campoMatcha(email, EMAIL_PATTERN)) { eliminaMsgErrore(erroreEmail); return true; }
        return inserisciCampoValido(erroreEmail, "Si prega di inserire una email valida!");
    }

    // --- Metodi Helper Statici ---
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

    void addListenerMaiuscoleENoNumeri(TextField text) {
        text.textProperty().addListener((observable, oldValue, newValue) -> {
            String filtered = newValue.replaceAll("[0-9]", "");
            if (!filtered.isEmpty()) {
                text.setText(filtered.substring(0, 1).toUpperCase() + filtered.substring(1));
            } else {
                text.setText("");
            }
        });
    }
}