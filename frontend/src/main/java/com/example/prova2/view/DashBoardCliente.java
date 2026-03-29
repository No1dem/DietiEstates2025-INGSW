package com.example.prova2.view;
import java.io.IOException;

import com.example.prova2.controller.dashBoard.DashBoardClienteController;
import com.example.prova2.model.Utente;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.Window;

public class DashBoardCliente {
    private DashBoardCliente(){}
    public static void initializePageDashboardCliente(Window w, Utente u) throws IOException, InterruptedException {
        FXMLLoader fxmlLoader = new FXMLLoader(LoginPage.class.getResource("/com/example/prova2/views/cliente/dashboardCliente.fxml"));
        Scene newScene = new Scene(fxmlLoader.load(), 950, 700);
        DashBoardClienteController dashClienteController = fxmlLoader.getController();

        // Imposta la scena precedente prima di cambiare la finestra
        Stage stage = (Stage) w;
        Scene previousScene = stage.getScene(); // Prende la scena attuale come precedente
        dashClienteController.setPreviousScene(previousScene);
        System.out.println("Dash : "+ u.getNome());
        dashClienteController.initDati(u);
        stage.setScene(newScene);
        stage.show();
    }

}
