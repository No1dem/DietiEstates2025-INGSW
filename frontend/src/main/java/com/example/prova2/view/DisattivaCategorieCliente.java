package com.example.prova2.view;

import java.io.IOException;

import com.example.prova2.controller.notifiche.DisattivaCategorieClienteController;
import com.example.prova2.controller.notifiche.VisualizzaNotificheClienteController;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class DisattivaCategorieCliente {
    public static Stage modalStage;

    public static void initPage(Stage proprietario, VisualizzaNotificheClienteController visualizzaNotificheController) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(LoginPage.class.getResource("/com/example/prova2/views/cliente/sbloccaNotifiche.fxml"));
        Pane modalLayout = fxmlLoader.load();
        DisattivaCategorieClienteController disattivaCategorieClienteController = fxmlLoader.getController();
        
        modalStage = new Stage();

        modalStage.initOwner(proprietario);
        modalStage.initModality(Modality.APPLICATION_MODAL);
        modalStage.setTitle("Notifiche");
        modalStage.setScene(new Scene(modalLayout, 975, 740));
        modalStage.setResizable(false);

        disattivaCategorieClienteController.initCategoriDisattivate(visualizzaNotificheController);
        modalStage.showAndWait();
    }
}
