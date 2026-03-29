package com.example.prova2.view;

import java.io.IOException;

import com.example.prova2.controller.modificaProfilo.ModificaProfiloGestoreController;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.Window;

public class ModificaProfiloGestore {
    private ModificaProfiloGestore(){}

    public static void initializePageModificaProfiloGestore(Window w) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(LoginPage.class.getResource("/com/example/prova2/views/admin/modificaProfilo.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1540, 900);
        Stage stage = (Stage) w;
        ModificaProfiloGestoreController modificaProfiloController = fxmlLoader.getController();
        modificaProfiloController.initProfilo();
        stage.setScene(scene);
        stage.show();
    }
}
