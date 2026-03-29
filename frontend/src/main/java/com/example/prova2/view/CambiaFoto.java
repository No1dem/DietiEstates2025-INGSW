package com.example.prova2.view;

import java.io.IOException;

import com.example.prova2.controller.CambiaFotoController;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class CambiaFoto {

    public void initPage(Stage owner) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/prova2/views/shared/cambiaFoto.fxml"));
            Pane pane = fxmlLoader.load();
            CambiaFotoController controllerCambiaFoto = fxmlLoader.getController();
            Stage modalStage = new Stage();

            modalStage.initOwner(owner);
            modalStage.initModality(Modality.APPLICATION_MODAL);
            modalStage.setTitle("Modifica foto");
            modalStage.setScene(new Scene(pane, 500, 550));
            modalStage.setResizable(false);
            
            controllerCambiaFoto.setModalStage(modalStage);
            controllerCambiaFoto.initFoto(); 
            
            modalStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}