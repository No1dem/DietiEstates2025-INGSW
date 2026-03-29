package com.example.prova2.view;

import java.io.IOException;

import com.example.prova2.controller.CreaAnnuncioController;
import com.example.prova2.controller.dashBoard.DashboardAgenteController;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class CreaAnnuncioAgente {

    private CreaAnnuncioAgente(){}


    public static void initializePageCreaAnnuncio(Stage stagePrecedente, String cf, DashboardAgenteController controllerP) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(LoginPage.class.getResource("/com/example/prova2/views/agente/creaAnnuncio.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1540, 790);
        CreaAnnuncioController controller = fxmlLoader.getController();
        controller.setCf(cf);
        controller.setControllerPrec(controllerP);
        Stage stage = new Stage();
        controller.stagePrecedente = stagePrecedente;
        controller.stageOra = stage;

        stage.setScene(scene);
        stage.show();
    }

    public static void initializePageCreaAnnuncioFromCreateAnnunci(String cf) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(LoginPage.class.getResource("/com/example/prova2/views/agente/creaAnnuncioDaCreaAnnuncio.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1540, 790);
        CreaAnnuncioController controller = fxmlLoader.getController();
        controller.setCf(cf);

        Stage stage = new Stage();

        stage.setScene(scene);
        stage.show();
    }


}
