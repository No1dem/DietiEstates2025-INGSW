package com.example.prova2.view;

import com.example.prova2.controller.notifiche.InfoAboutPrenotazioneClienteController;
import com.example.prova2.controller.notifiche.VisualizzaNotificheClienteController;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class InfoAboutPrenotazioneCliente {
    
    public static void initPage(Stage proprietario, VisualizzaNotificheClienteController VisualizzaNotificheController, int notifica) {
        try {
            FXMLLoader loader = new FXMLLoader(InfoAboutPrenotazioneCliente.class.getResource("/com/example/prova2/views/cliente/infoAboutPrenotazioneCliente.fxml"));
            Parent modalLayout = loader.load(); 
            InfoAboutPrenotazioneClienteController infoPrenotazioneController = loader.getController();
            Stage modalStage = new Stage();

            modalStage.initOwner(proprietario);
            modalStage.initModality(Modality.NONE); 
            modalStage.setTitle("Dettagli Prenotazione");

            infoPrenotazioneController.setController(VisualizzaNotificheController);
            infoPrenotazioneController.initPage(notifica);

            Scene scene = new Scene(modalLayout, 800, 650);
            modalStage.setScene(scene);
            modalStage.setMinWidth(800);
            modalStage.setMinHeight(650);
            modalStage.show(); 
            
        } catch (Exception e) {
            e.printStackTrace(); 
        }
    }
}