package com.example.prova2.view;

import java.io.IOException;

import com.example.prova2.controller.LasciaRecensioneController;
import com.example.prova2.controller.SchermataAnnuncioController;
import com.example.prova2.model.Utente;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class LasciaRecensione {
    public static Stage modalStage;

    public static SchermataAnnuncioController schermataAnnuncioController;

    public static void initPage(Stage proprietario, String nomeCognomeAgente, Utente utente, String cfAgente, SchermataAnnuncioController controllerPrecedente) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(LoginPage.class.getResource("/com/example/prova2/views/shared/lasciaRecensione.fxml"));
            Pane modalLayout = fxmlLoader.load();
            LasciaRecensioneController lasciaRecensioneController = fxmlLoader.getController();

            schermataAnnuncioController = controllerPrecedente;
            modalStage = new Stage();
            modalStage.initOwner(proprietario);
            modalStage.initModality(Modality.APPLICATION_MODAL);
            lasciaRecensioneController.setUtente(utente);
            lasciaRecensioneController.initialize(nomeCognomeAgente,cfAgente);
            lasciaRecensioneController.setControllerPrec(controllerPrecedente);
            modalStage.setTitle("Lascia Recensione");
            modalStage.setScene(new Scene(modalLayout, 955, 495));
            modalStage.setResizable(false);
            modalStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Stage getModalStage() {
        return modalStage;
    }
}
