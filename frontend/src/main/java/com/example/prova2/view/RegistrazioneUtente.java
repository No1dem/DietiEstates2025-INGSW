package com.example.prova2.view;

import java.io.IOException;

import com.example.prova2.controller.registrazione.RegistrazioneUtenteController;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.Window;

public class RegistrazioneUtente {
    private RegistrazioneUtente(){}

    public static void initializePageRegistrazioneUtente(Window w) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(LoginPage.class.getResource("/com/example/prova2/views/cliente/registrazioneCliente.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1400, 950);
        Stage stage = (Stage) w;
        stage.setScene(scene);
        RegistrazioneUtenteController registrazioneController = fxmlLoader.getController();
        registrazioneController.init();
        stage.show();
    }
}
