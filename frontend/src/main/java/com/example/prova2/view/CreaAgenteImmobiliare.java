package com.example.prova2.view;

import java.io.IOException;

import com.example.prova2.controller.CreaAgenteImmobiliareController;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class CreaAgenteImmobiliare {

    private CreaAgenteImmobiliare(){}

    public static void initializePageCreaAgente(Stage stagePrecedente) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(LoginPage.class.getResource("/com/example/prova2/views/admin/creaAgenteImmobiliare.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1540, 900);
        CreaAgenteImmobiliareController controller = fxmlLoader.getController();
        controller.init();
        controller.setStagePrecedente(stagePrecedente);
        controller.setScenePrecedente(stagePrecedente.getScene());


        stagePrecedente.setScene(scene);
        stagePrecedente.show();
    }
}
