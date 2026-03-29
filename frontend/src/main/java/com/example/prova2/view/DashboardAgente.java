package com.example.prova2.view;

import java.io.IOException;

import com.example.prova2.controller.dashBoard.DashboardAgenteController;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.Window;

public class DashboardAgente {

    private DashboardAgente(){}
    public static void initializePageDashboardAgente(Window w) throws IOException, InterruptedException {
        FXMLLoader fxmlLoader = new FXMLLoader(LoginPage.class.getResource("/com/example/prova2/views/agente/dashboardAgente.fxml"));
        Scene newScene = new Scene(fxmlLoader.load(), 1540, 790);
        DashboardAgenteController dashAgenteController = fxmlLoader.getController();
        new Thread(dashAgenteController::initialize).start();
        // Imposta la scena precedente prima di cambiare la finestra
        Stage stage = (Stage) w;
        Scene previousScene = stage.getScene(); // Prende scena attuale uguale alla precedente
        dashAgenteController.setPreviousScene(previousScene);

        stage.setScene(newScene);
        stage.show();
    }
}
