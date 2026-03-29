package com.example.prova2.view;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class InfoPage {
    public static void faqPageInit() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(LoginPage.class.getResource("/com/example/prova2/views/shared/FAQpage.fxml"));
        Parent root = fxmlLoader.load();


        Stage faqStage = new Stage();
        faqStage.initModality(Modality.APPLICATION_MODAL); // Blocca l'interazione con altre finestre se il popup è aperto
        faqStage.setTitle("FAQ - DietiEstates25");

        Scene scene = new Scene(root, 650, 600); // Dimensioni del popup
        faqStage.setScene(scene);

        faqStage.showAndWait(); // Mostra il popup e aspetta la chiusura 
    }

    public static void privacyPageInit() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(LoginPage.class.getResource("/com/example/prova2/views/shared/privacyPage.fxml"));
        Parent root = fxmlLoader.load();

        Stage privacyStage = new Stage();
        privacyStage.initModality(Modality.APPLICATION_MODAL); // Blocca l'interazione con altre finestre finché il popup è aperto
        privacyStage.setTitle("Privacy - DietiEstates25");

        Scene scene = new Scene(root, 650, 600); // Dimensioni del popup
        privacyStage.setScene(scene);

        privacyStage.showAndWait(); // Mostra il popup e aspetta la chiusura
    }


    public static void chiSiamoPageInit() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(LoginPage.class.getResource("/com/example/prova2/views/shared/chiSiamoPage.fxml"));
        Parent root = fxmlLoader.load();

        Stage chiSiamoStage = new Stage();
        chiSiamoStage.initModality(Modality.APPLICATION_MODAL); // Blocca l'interazione con altre finestre finché il popup è aperto
        chiSiamoStage.setTitle("Chi siamo - DietiEstates25");

        Scene scene = new Scene(root, 650, 600); // Dimensioni del popup
        chiSiamoStage.setScene(scene);

        chiSiamoStage.showAndWait(); // Mostra il popup e aspetta la chiusura 
    }

}
