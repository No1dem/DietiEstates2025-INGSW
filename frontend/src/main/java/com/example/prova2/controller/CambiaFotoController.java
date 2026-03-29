package com.example.prova2.controller;

import java.io.File;
import java.util.Objects;

import com.example.prova2.controller.dashBoard.DashboardAgenteController;
import com.example.prova2.facade.ModificaProfiloFacade;
import com.example.prova2.model.Agente;
import com.example.prova2.model.Foto;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;

public class CambiaFotoController {

    // =========================================================================
    // COSTANTI
    // =========================================================================
    private static final String DEFAULT_IMAGE_PATH = "/com/example/prova2/images/user.png";

    // =========================================================================
    // COMPONENTI UI (FXML)
    // =========================================================================
    @FXML private ImageView fotoProfilo;
    @FXML private Button salvaButton;
    @FXML private Button indietroBtn;

    // =========================================================================
    // VARIABILI DI STATO
    // =========================================================================
    private Stage modalStage;
    private Agente agente;
    private File immagineSelezionata;

    // =========================================================================
    // INIZIALIZZAZIONE E SETTER
    // =========================================================================
    public void setAgente(Agente agente) {
        this.agente = agente;
    }

    public void setModalStage(Stage modalStage) {
        this.modalStage = modalStage;
    }

    public void initFoto() {
        setAgente(DashboardAgenteController.getAgente());
        
        if (agente != null && agente.getFotoProfilo() != null && agente.getFotoProfilo().getPath() != null) {
            fotoProfilo.setImage(new Image(agente.getFotoProfilo().getPath()));
        } else {
            fotoProfilo.setImage(new Image(Objects.requireNonNull(getClass().getResource(DEFAULT_IMAGE_PATH)).toExternalForm()));
        }
    }

    // =========================================================================
    // AZIONI UI (Event Handlers)
    // =========================================================================
    public void caricaImmagine() {
        File immagine = prendiFileDaConsole(fotoProfilo.getScene());
        
        if (immagine != null && isImmagineDiversa(immagine)) {
            this.immagineSelezionata = immagine;
            fotoProfilo.setImage(new Image(immagine.toURI().toString()));
            attivaBottone();
        }
    }

    public void cancellaImmagine() {
        try {
            String urlImmagineDefault = Objects.requireNonNull(getClass().getResource(DEFAULT_IMAGE_PATH)).toExternalForm();
            fotoProfilo.setImage(new Image(urlImmagineDefault));
            attivaBottone(); 
        } catch (Exception e) {
            System.out.println("Errore: Immagine di default non trovata!");
        }
    }

    public void salvataggioFoto() {
        if (immagineSelezionata == null || agente == null) return;
        
        String pathPuro = immagineSelezionata.getAbsolutePath();
        
        if (ModificaProfiloFacade.uploadFotoAgente(agente.getCf(), new Foto(pathPuro))) {
            creaAlertSuccessFotoProfilo();
        }
    }

    public void tornaIndietro() {
        handleClose();
    }

    @FXML
    private void handleClose() {
        if (modalStage != null) {
            modalStage.close();
        }
    }

    // =========================================================================
    // METODI HELPER (Logica di supporto interna)
    // =========================================================================
    static File prendiFileDaConsole(Scene scene) {
        Window currentWindow = scene.getWindow();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleziona un file");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Immagini", "*.png", "*.jpg", "*.jpeg"),
                new FileChooser.ExtensionFilter("Tutti i file", "*.*")
        );
        return fileChooser.showOpenDialog(currentWindow);
    }

    private void attivaBottone() {
        salvaButton.setDisable(false);
    }

    private boolean isImmagineDiversa(File immagine) {
        if (agente.getFotoProfilo() == null || agente.getFotoProfilo().getPath() == null) {
            return true;
        }
        return !immagine.toURI().toString().equals(agente.getFotoProfilo().getPath());
    }

    public void creaAlertSuccessFotoProfilo() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Foto Profilo Aggiornata");
        alert.setHeaderText("Hai aggiornato la tua foto profilo!");
        alert.setContentText("La tua foto profilo è stata aggiornata, ora tutti possono vederla!");
        alert.getButtonTypes().setAll(ButtonType.OK);
        
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    String urlNuovo = fotoProfilo.getImage() != null ? fotoProfilo.getImage().getUrl() : "";
                    if (agente.getFotoProfilo() == null) {
                        agente.setFotoProfilo(new Foto(urlNuovo));
                    } else {
                        agente.getFotoProfilo().setPath(urlNuovo); 
                    }
                } catch (Exception e) {
                    System.out.println("Errore aggiornamento memoria: " + e.getMessage());
                }
                handleClose();
            }
        });
    }
}