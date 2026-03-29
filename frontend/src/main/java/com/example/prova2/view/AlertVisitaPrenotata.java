package com.example.prova2.view;

import java.io.IOException;

import com.example.prova2.controller.SchermataAnnuncioController;
import com.example.prova2.controller.prenotaVisita.AlertVisitaController;
import com.example.prova2.model.Immobile;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class AlertVisitaPrenotata {

    public void initPage(Stage owner, Immobile immobile, SchermataAnnuncioController controllerAnnuncio) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/prova2/views/shared/visitaGiaPrenotataAlert.fxml"));
            Pane pane = fxmlLoader.load();
            AlertVisitaController controllerVisita = fxmlLoader.getController();
            Stage stage = new Stage();
            
            stage.initOwner(owner);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Visita già prenotata!");
            stage.setScene(new Scene(pane, 570, 380));
            stage.setResizable(false);
            controllerVisita.init(immobile,controllerAnnuncio);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
