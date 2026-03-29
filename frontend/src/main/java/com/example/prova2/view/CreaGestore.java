package com.example.prova2.view;
import java.io.IOException;

import com.example.prova2.controller.CreaGestoreController;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class CreaGestore {

    private CreaGestore(){}

    public static void initializePageCreaGestore(Stage stagePrecedente) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(LoginPage.class.getResource("/com/example/prova2/views/admin/creaGestore.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1540, 900);
        CreaGestoreController gestoreController = fxmlLoader.getController();
    
        gestoreController.init();
        gestoreController.setStage(stagePrecedente);
        gestoreController.setScenePrecedente(stagePrecedente.getScene());


        stagePrecedente.setScene(scene);
        stagePrecedente.show();
    }

}
