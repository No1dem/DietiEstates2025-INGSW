package com.example.prova2.view;

import java.io.IOException;

import com.example.prova2.controller.notifiche.VisualizzaNotificheClienteController;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class VisualizzaNotificheCliente {
    public static Stage modalStage;

    public static void initPage(Stage proprietario) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(LoginPage.class.getResource("/com/example/prova2/views/cliente/visualizzaNotifiche.fxml"));
            Pane modalLayout = fxmlLoader.load();
            modalStage = new Stage();
            modalStage.initOwner(proprietario);
            modalStage.initModality(Modality.APPLICATION_MODAL);
            VisualizzaNotificheClienteController visualizzaNotificheController = fxmlLoader.getController();
            visualizzaNotificheController.init();
            modalStage.setTitle("Notifiche");
            modalStage.setScene(new Scene(modalLayout, 1150, 745));
            modalStage.setResizable(false);
            modalStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Stage getModalStage() {
        return modalStage;
    }
}
