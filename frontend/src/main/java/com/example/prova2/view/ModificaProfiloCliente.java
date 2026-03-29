package com.example.prova2.view;
import java.io.IOException;

import com.example.prova2.controller.modificaProfilo.ModificaProfiloClienteController;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.Window;

public class ModificaProfiloCliente {

    private ModificaProfiloCliente(){}

    public static void initializePageModificaProfiloCliente(Window w) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(LoginPage.class.getResource("/com/example/prova2/views/cliente/modificaProfilo.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 950, 700);
        Stage stage = (Stage) w;
        ModificaProfiloClienteController modificaProfiloController = fxmlLoader.getController();
        modificaProfiloController.initProfilo();
        stage.setScene(scene);
        stage.show();
    }


}
