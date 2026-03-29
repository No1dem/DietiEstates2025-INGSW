package com.example.prova2.view;

import java.io.IOException;

import com.example.prova2.controller.notifiche.InfoAbouPrenotazioneAgenteController;
import com.example.prova2.controller.notifiche.VisualizzaNotificheAgenteController;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class InfoAboutPrenotazioneAgente {
    public static void initPage(Stage proprietario,VisualizzaNotificheAgenteController VisualizzaNotificheController, int notifica,Pane pane) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(LoginPage.class.getResource("/com/example/prova2/views/agente/infoAboutPrenotazione.fxml"));
        Pane modalLayout = fxmlLoader.load();
        Stage modalStage = new Stage();
        InfoAbouPrenotazioneAgenteController infoPrenotazioneAgenteController =fxmlLoader.getController();

        modalStage.initOwner(proprietario);
        modalStage.initModality(Modality.APPLICATION_MODAL);
        modalStage.setTitle("Informazioni sulla tua prenotazione!");
        infoPrenotazioneAgenteController.setController(VisualizzaNotificheController);
        infoPrenotazioneAgenteController.initPage(notifica,pane);
        modalStage.setScene(new Scene(modalLayout, 785,560 ));
        modalStage.setResizable(false);
        modalStage.showAndWait();
    }
}
