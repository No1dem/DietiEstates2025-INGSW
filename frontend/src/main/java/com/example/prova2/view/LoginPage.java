package com.example.prova2.view;
import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class LoginPage extends Application {

    @Override
    public void start(Stage primaryStage) {
            try {
                // Carica la schermata principale e sostituisci la scena
                startMainApp(primaryStage);
            } catch (IOException e) {
                e.printStackTrace();
            }
    }

    public static void startMainApp(Stage primaryStage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(LoginPage.class.getResource("/com/example/prova2/views/shared/loginPage.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 900, 600);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Login");
        primaryStage.show();
        primaryStage.setResizable(false);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
