package com.example.prova2.controller.dashBoard;

import java.io.IOException;
import java.util.Optional;

import com.example.prova2.controller.modificaProfilo.ModificaProfiloAgenteController;
import com.example.prova2.controller.notifiche.VisualizzaNotificheClienteController;
import com.example.prova2.dto.ClienteDatiDTO;
import com.example.prova2.facade.ClienteServiceFacade;
import com.example.prova2.factory.AlertFactory;
import com.example.prova2.model.Cliente;
import com.example.prova2.model.Utente;
import com.example.prova2.view.HomePage;
import com.example.prova2.view.LoginPage;
import com.example.prova2.view.ModificaProfiloCliente;
import com.example.prova2.view.VisualizzaNotificheCliente;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class DashBoardClienteController {

    // =========================================================================
    // COMPONENTI UI (FXML) - ETICHETTE (LABEL)
    // =========================================================================
    @FXML private Label labelNomeProf;
    @FXML private Label labelCognomeProf;
    @FXML private Label labelTel;
    @FXML private Label labelMail;
    @FXML private Label nomeLbl;
    @FXML private Label labelCognome;

    // =========================================================================
    // COMPONENTI UI (FXML) - BOTTONI E IMMAGINI
    // =========================================================================
    @FXML private Button btnCercaAnnunci;
    @FXML private Button btnLogout;
    @FXML private Button btnIndietro;
    @FXML private ImageView frecciaIndietro;

    // =========================================================================
    // VARIABILI DI STATO E GETTER/SETTER
    // =========================================================================
    private static Cliente cliente = new Cliente();
    private Scene previousScene;

    public static Cliente getCliente() {
        return cliente;
    }

    public static void setCliente(Cliente cliente) {
        DashBoardClienteController.cliente = cliente;
    }

    public void setPreviousScene(Scene scene) {
        this.previousScene = scene;
    }

    // =========================================================================
    // INIZIALIZZAZIONE E RECUPERO DATI (Thread Asincrono)
    // =========================================================================
    public void initDati(Utente utente) {
        new Thread(() -> {
            System.out.println("Init: " + utente.getNome());
            System.out.println("Init 2: " + utente.getToken());
            
            ClienteDatiDTO dati = ClienteServiceFacade.recuperoDati(utente);
            
            Platform.runLater(() -> {
                Cliente cliente1 = new Cliente(dati.getNome(), dati.getCognome(), dati.getEmail());
                cliente1.setToken(utente.getToken());
                setCliente(cliente1);
                
                labelNomeProf.setText(dati.getNome());
                labelCognomeProf.setText(dati.getCognome());
                labelMail.setText(dati.getEmail());
                nomeLbl.setText(dati.getNome());
                labelCognome.setText(dati.getCognome());
            });
        }).start();
    }

    // =========================================================================
    // AZIONI UI E NAVIGAZIONE (Event Handlers)
    // =========================================================================
    @FXML
    public void apriPaginaCercaAnnunci() {
        try {
            HomePage.initializeHomePage((Stage) btnCercaAnnunci.getScene().getWindow(), cliente);
        } catch (IOException e) {
            AlertFactory.creaAlertErroreCaricamentoPagina();
        }
    }

    @FXML
    public void apriPaginaModificaProfilo() {
        try {
            System.out.println(cliente.getToken());
            ModificaProfiloAgenteController.setUtente(cliente);
            ModificaProfiloCliente.initializePageModificaProfiloCliente(labelCognome.getScene().getWindow());
        } catch (IOException e) {
            AlertFactory.creaAlertErroreCaricamentoPagina();
        }
    }

    @FXML
    public void apriPaginaVisualizzaNotifiche() {
        try {
            VisualizzaNotificheClienteController.setClienteNotifiche(cliente);
            VisualizzaNotificheCliente.initPage((Stage) labelNomeProf.getScene().getWindow());
        } catch (RuntimeException e) {
            e.printStackTrace();
            AlertFactory.creaAlertErroreInterno();
        }
    }

    @FXML
    public void vaiIndietro() {
        if (previousScene != null) {
            Stage stage = (Stage) btnIndietro.getScene().getWindow();
            stage.setScene(previousScene);
        } else {
            System.out.println("Errore: previousScene è null");
            AlertFactory.creaAlertErroreInterno();
        }
    }

    @FXML
    public void faiLogout() {
        Alert alert = AlertFactory.creaAlertLogout();
        Optional<ButtonType> result = alert.showAndWait();
        
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                System.out.println("ciao");
                DashBoardClienteController.setCliente(null);
                LoginPage.startMainApp((Stage) btnLogout.getScene().getWindow());
            } catch (IOException e) {
                AlertFactory.creaAlertErroreCaricamentoPagina();
            }
        } else {
            System.out.println("Logout annullato");
        }
    }
}