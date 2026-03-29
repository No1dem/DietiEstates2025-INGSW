package com.example.prova2.view;

import java.io.IOException;
import java.math.BigDecimal;

import com.example.prova2.controller.SchermataAnnuncioController;
import com.example.prova2.dto.ImmobileResponseRicercaDTO;
import com.example.prova2.model.Utente;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class SchermataAnnuncio {

    // =========================================================================
    // METODI PUBBLICI DI INIZIALIZZAZIONE
    // =========================================================================

    /**
     * Inizializza la schermata dell'annuncio standard usando la finestra (Stage) corrente.
     */
    public static void initializeSchermataAnnuncio(Pane pane, ImmobileResponseRicercaDTO annuncio, Utente utente, BigDecimal prezzoMin, BigDecimal prezzoMax) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(SchermataAnnuncio.class.getResource("/com/example/prova2/views/shared/schermataAnnuncio.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1540, 900);
        
        SchermataAnnuncioController annuncioController = fxmlLoader.getController();
        Stage stage = (Stage) pane.getScene().getWindow();
        
        // Setup del controller e salvataggio scena precedente
        annuncioController.setPreviousScene(stage.getScene());
        setupController(annuncioController, annuncio, utente, prezzoMin, prezzoMax);
        
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Inizializza la schermata dell'annuncio specifico per le notifiche del cliente.
     * Chiude la finestra precedente e ne apre una nuova centrata.
     */
    public static void initializeSchermataAnnuncioClienteNotifiche(Stage previousStage, ImmobileResponseRicercaDTO annuncio, Utente utente) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(SchermataAnnuncio.class.getResource("/com/example/prova2/views/cliente/schermataAnnuncioClienteNotifiche.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1540, 900);
        
        SchermataAnnuncioController annuncioController = fxmlLoader.getController();
        
        // Setup del controller (prezzi null per questa specifica view)
        setupController(annuncioController, annuncio, utente, null, null);
        
        if (previousStage != null && previousStage.getScene() != null) {
            annuncioController.setPreviousScene(previousStage.getScene());
        }
        
        // Configurazione della nuova finestra
        Stage newStage = new Stage();
        newStage.setScene(scene);
        newStage.setWidth(1550);
        newStage.setHeight(810);
        
        centraFinestraNelloSchermo(newStage);
        
        // Chiusura della vecchia finestra e apertura della nuova
        if (previousStage != null) {
            previousStage.close();
        }
        newStage.show();
    }

    // =========================================================================
    // METODI PRIVATI DI SUPPORTO
    // =========================================================================

    private static void setupController(SchermataAnnuncioController controller, ImmobileResponseRicercaDTO annuncio, Utente utente, BigDecimal prezzoMin, BigDecimal prezzoMax) {
        SchermataAnnuncioController.setDatiRicerca(annuncio, prezzoMin, prezzoMax);
        SchermataAnnuncioController.setUtenteCheCerca(utente);
        controller.loadAnnuncio(annuncio);
        controller.loadValutazione();
    }

    /**
     * Calcola le dimensioni dello schermo e centra la finestra passata come parametro.
     */
    private static void centraFinestraNelloSchermo(Stage stage) {
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        stage.setX((screenBounds.getWidth() - stage.getWidth()) / 2);
        stage.setY((screenBounds.getHeight() - stage.getHeight()) / 2);
    }
}