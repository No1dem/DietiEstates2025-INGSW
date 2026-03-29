package com.example.prova2.view;

import java.io.IOException;

import com.example.prova2.controller.dashBoard.DashboardAmministratoreController;
import com.example.prova2.controller.notifiche.VisualizzaNotificheGestoreController;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class VisualizzaNotificheGestore {
    public static Stage modalStage;


    public static void initPage(Stage owner, DashboardAmministratoreController controllerPrecedente) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(LoginPage.class.getResource("/com/example/prova2/views/admin/visualizzaNotifiche.fxml"));
            Pane modalLayout = fxmlLoader.load();
            modalStage = new Stage();
            modalStage.initOwner(owner);
            modalStage.initModality(Modality.APPLICATION_MODAL);
            VisualizzaNotificheGestoreController visualizzaNotificheController = fxmlLoader.getController();
            visualizzaNotificheController.init();
            visualizzaNotificheController.setWindowPrecedente(owner);
            visualizzaNotificheController.setControllerPrecedente(controllerPrecedente);
            modalStage.setTitle("Notifiche");
            modalStage.setScene(new Scene(modalLayout, 1150, 745));
            modalStage.setResizable(false);
            modalStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
