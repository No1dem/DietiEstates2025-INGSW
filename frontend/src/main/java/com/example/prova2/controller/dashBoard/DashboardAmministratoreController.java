package com.example.prova2.controller.dashBoard;

import java.io.IOException;
import java.util.Optional;

import com.example.prova2.controller.HomePageController;
import com.example.prova2.controller.modificaProfilo.ModificaPasswordInterface;
import com.example.prova2.controller.modificaProfilo.ModificaProfiloAgenteController;
import com.example.prova2.controller.notifiche.VisualizzaNotificheGestoreController;
import com.example.prova2.dto.GestoreAgenziaImmobiliareDatiDTO;
import com.example.prova2.factory.AlertFactory;
import com.example.prova2.model.Account;
import com.example.prova2.model.Admin;
import com.example.prova2.model.GestoreAgenziaImmobiliare;
import com.example.prova2.service.GestoreService;
import com.example.prova2.service.UpdatePasswordService;
import com.example.prova2.view.AccediAdmin;
import com.example.prova2.view.CambioPassword;
import com.example.prova2.view.CreaAgenteImmobiliare;
import com.example.prova2.view.CreaGestore;
import com.example.prova2.view.HomePage;
import com.example.prova2.view.LoginPage;
import com.example.prova2.view.ModificaProfiloGestore;
import com.example.prova2.view.VisualizzaNotificheGestore;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

public class DashboardAmministratoreController implements ModificaPasswordInterface {

    // =========================================================================
    // COSTANTI
    // =========================================================================
    private static final String STRERROR = "errore di navigazione";

    // =========================================================================
    // COMPONENTI UI (FXML) - PANNELLI
    // =========================================================================
    @FXML private Pane paneAnnullaCambio;
    @FXML private Pane modificaProfiloPane;
    @FXML private VBox vbox1;

    // =========================================================================
    // COMPONENTI UI (FXML) - ETICHETTE (LABEL)
    // =========================================================================
    @FXML private Label nomeLbl;
    @FXML private Label labelCognome;
    @FXML private Label labelMail;
    @FXML private Label labelNomeProf;
    @FXML private Label labelCognomeProf;
    @FXML private Label labelTel;

    // =========================================================================
    // COMPONENTI UI (FXML) - BOTTONI
    // =========================================================================
    @FXML private Button btnCreaAgentiImmobiliari;
    @FXML private Button buttonTornaIndietro;
    @FXML private Button buttonNotifiche;
    @FXML private Button modificaProfiloButton;
    @FXML private Button btnCreaGestore;
    @FXML private Button buttonLogout;

    // =========================================================================
    // VARIABILI DI STATO E GETTER/SETTER
    // =========================================================================
    private static GestoreAgenziaImmobiliare gestore;
    private String passwordVecchia;
    private Stage previousStage;

    public void setPreviousStage(Stage stage) { this.previousStage = stage; }
    
    @Override
    public void setPasswordVecchia(String passwordVecchia) { this.passwordVecchia = passwordVecchia; }
    public String getPasswordVecchia() { return passwordVecchia; }

    public static void setAdmin(GestoreAgenziaImmobiliare admin) {
        DashboardAmministratoreController.gestore = admin;
    }

    public static GestoreAgenziaImmobiliare getAdmin() {
        if (!gestore.isAdmin()) {
            return gestore;
        } else {
            Account a = new Account(gestore.getAccountAgente().getEmail());
            Admin admin = new Admin(gestore.getNome(), gestore.getCognome(), gestore.getCf());
            admin.setAccountAgente(a);
            admin.setToken(gestore.getToken());
            admin.setTelefono(gestore.getTelefono());
            return admin;
        }
    }

    // =========================================================================
    // INIZIALIZZAZIONE E RECUPERO DATI (Thread asincroni)
    // =========================================================================
    @FXML
    public void initialize() {
        if (gestore != null) {
            btnCreaGestore.setVisible(gestore.isAdmin());
            getGestoreData(gestore.getAccountAgente().getEmail());
        }
    }

    public void getGestoreData(String email) {
        new Thread(() -> {
            GestoreAgenziaImmobiliareDatiDTO gestoreData = GestoreService.getGestoreByUsername(email, gestore.getToken());
            if (gestoreData != null) {
                gestore.setDto(gestoreData);
                extracted(gestoreData.getNome(), gestoreData.getCognome(), gestoreData.getEmail(), gestoreData.getTelefono());
            } else {
                Platform.runLater(() -> {
                    AlertFactory.creaAlertErrore("Errore nel ritrovo dei dati!", "Siamo spiacenti al momento " +
                            "non abbiamo potuto recuperare i tuoi dati, si prega di riprovare più tardi");
                });
            }
        }).start();
    }

    private void extracted(String nome, String cognome, String email, String telefono) {
        Platform.runLater(() -> {
            nomeLbl.setText(nome);
            labelCognome.setText(cognome);
            labelNomeProf.setText(nome);
            labelCognomeProf.setText(cognome);
            labelMail.setText(email);
            labelTel.setText(telefono);
        });
    }

    // =========================================================================
    // AZIONI UTENTE E NAVIGAZIONE (Event Handlers)
    // =========================================================================
    @FXML
    public void handleTornaHome() {
        if (previousStage != null) {
            Stage currentStage = (Stage) buttonTornaIndietro.getScene().getWindow();
            previousStage.show();
            currentStage.close();
        } else {
            try {
                AccediAdmin.initializePageAccediAdmin(buttonTornaIndietro.getScene().getWindow());
            } catch (IOException e) {
                AlertFactory.creaAlertErroreCaricamentoPagina();
                e.printStackTrace();
            }
        }
    }

    @FXML
    public void apriPaginaCreaGestori() {
        try {
            CreaGestore.initializePageCreaGestore((Stage) btnCreaGestore.getScene().getWindow());
        } catch (IOException e) {
            e.printStackTrace();
            AlertFactory.creaAlertErrore(STRERROR, "Siamo spiacenti, si è verificato un errore durante il caricamento della pagina creazione gestori");
        }
    }

    @FXML
    public void apriCreaAgenti() {
        try {
            CreaAgenteImmobiliare.initializePageCreaAgente((Stage) btnCreaAgentiImmobiliari.getScene().getWindow());
        } catch (IOException e) {
            e.printStackTrace();
            AlertFactory.creaAlertErrore(STRERROR, "Si è verificato un problema durante il caricamento della dashboard.");
        }
    }

    @FXML
    public void apriPaginaNotifiche() {
        VisualizzaNotificheGestoreController.setGestoreNotifiche(gestore);
        VisualizzaNotificheGestore.initPage((Stage) buttonNotifiche.getScene().getWindow(), this);
    }

    @FXML
    public void apriPaginaModificaProfilo() {
        try {
            ModificaProfiloAgenteController.setUtente(gestore);
            ModificaProfiloGestore.initializePageModificaProfiloGestore(buttonTornaIndietro.getScene().getWindow());
        } catch (IOException e) {
            e.printStackTrace();
            AlertFactory.creaAlertErroreInterno();
        }
    }

    @FXML
    public void apriPaginaCambiaPassword() {
        try {
            CambioPassword.initializePageCambioPassword(btnCreaGestore.getScene().getWindow(), this, gestore);
        } catch (IOException e) {
            e.printStackTrace();
            AlertFactory.creaAlertErrore("Errore apertura pagina!", "Siamo spiacenti si è verificato un errore nel caricamento della pagina cambio password");
        }
    }

    @FXML
    public void apriPaginaCercaAnnunci() {
        try {
            HomePageController.setUtente(DashboardAmministratoreController.getAdmin());
            HomePage.initializeHomePage((Stage) buttonLogout.getScene().getWindow(), DashboardAmministratoreController.getAdmin());
        } catch (IOException e) {
            AlertFactory.creaAlertErroreCaricamentoPagina();
        }
    }

    @FXML
    public void faiLogout() {
        Alert alert = AlertFactory.creaAlertLogout();
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                LoginPage.startMainApp((Stage) buttonLogout.getScene().getWindow());
            } catch (IOException e) {
                e.printStackTrace();
                AlertFactory.creaAlertErrore(STRERROR, "Siamo spiacenti, si è verificato un errore durante il caricamento della pagina di login");
            }
        }
    }

    // =========================================================================
    // GESTIONE PASSWORD (ModificaPasswordInterface)
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

    @FXML
    public void tornaIndietroPassword() {
        Platform.runLater(() -> { 
            paneAnnullaCambio.setVisible(false); 
        });
        new Thread(() -> {
            UpdatePasswordService.updatePasswordGestore(passwordVecchia, gestore);
        }).start();
    }
}