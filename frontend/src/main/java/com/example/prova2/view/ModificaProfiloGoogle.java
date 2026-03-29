package com.example.prova2.view;

import java.io.IOException;

import com.example.prova2.controller.modificaProfilo.ModificaProfiloGoogleController;
import com.example.prova2.model.Utente;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.Window;

public class ModificaProfiloGoogle {

    public static void initializePageModificaProfiloClienteGoogle(Window window, Utente utente) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(LoginPage.class.getResource("/com/example/prova2/views/cliente/modificaProfiloGoogle.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1540, 900);
        Stage stage = (Stage) window;
        ModificaProfiloGoogleController modificaProfiloController = fxmlLoader.getController();
        modificaProfiloController.setUtenteGoogle(utente);
        System.out.println("ModificaProfilo Google: "+utente.getToken());
        modificaProfiloController.initProfiloGoogle(utente);
        stage.setScene(scene);
        stage.show();
    }
}
