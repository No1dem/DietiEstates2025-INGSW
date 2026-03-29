package com.example.prova2.view;
import java.io.IOException;

import com.example.prova2.controller.dashBoard.DashboardAmministratoreController;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class DashboardAmministratore {
    private DashboardAmministratore(){}
    public static void initializePageDashboardAmministratore(Stage stagePrecedente) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(LoginPage.class.getResource("/com/example/prova2/views/admin/dashboardAdmin.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1540, 790);

        DashboardAmministratoreController dashAmministratoreController = fxmlLoader.getController();
        dashAmministratoreController.setPreviousStage(stagePrecedente);

        Stage newStage = new Stage();
        newStage.setScene(scene);
        newStage.setTitle("Dashboard Amministratore");
        newStage.show();

        stagePrecedente.close();
    }

}
