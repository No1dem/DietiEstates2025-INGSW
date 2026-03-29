package com.example.prova2.view;

import java.io.IOException;

import com.example.prova2.controller.login.LoginAgenteController;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.Window;

public class LoginAgenteImmobiliare {

    private LoginAgenteImmobiliare(){}

    public static void initializePageLoginAgente(Window w) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(LoginPage.class.getResource("/com/example/prova2/views/agente/loginAgente.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1540, 900);
        Stage stage = (Stage) w;
        LoginAgenteController controllerLogin = fxmlLoader.getController();
        controllerLogin.initialize();
        stage.setScene(scene);
        stage.show();
    }
}