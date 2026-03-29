package com.example.prova2.view;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.Window;

public class AccediAdmin {

    private AccediAdmin(){}

    public static void initializePageAccediAdmin(Window window) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(LoginPage.class.getResource("/com/example/prova2/views/admin/loginAdminAccedi.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1540, 900);
        Stage stage = (Stage) window;
        stage.setScene(scene);
        stage.show();
    }
}
