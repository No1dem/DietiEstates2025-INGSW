package com.example.prova2.view;

import java.io.IOException;

import com.example.prova2.controller.CambioPasswordController;
import com.example.prova2.controller.modificaProfilo.ModificaPasswordInterface;
import com.example.prova2.model.Utente;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

public class CambioPassword {
    private CambioPassword(){}


    public static void initializePageCambioPassword(Window window, ModificaPasswordInterface controllerPrecedente, Utente utente) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(LoginPage.class.getResource("/com/example/prova2/views/shared/cambioPasswordNew.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 400, 600);

        Stage stage = new Stage();
        stage.setScene(scene);
        stage.initOwner(window);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.setTitle("Cambio Password");
        stage.setResizable(false);

        CambioPasswordController controllerCambioPw = fxmlLoader.getController();
        controllerCambioPw.init();
        controllerCambioPw.setScene(scene);
        controllerCambioPw.setUtenteCheCambiaPassword(utente);
        controllerCambioPw.setControllerPrecedente(controllerPrecedente);
        stage.show();
    }

}
