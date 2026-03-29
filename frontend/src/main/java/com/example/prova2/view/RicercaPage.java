package com.example.prova2.view;
import java.io.IOException;
import java.math.BigDecimal;

import com.example.prova2.controller.SchermataRicercaController;
import com.example.prova2.model.ClasseEnergetica;
import com.example.prova2.model.TipoVendita;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class RicercaPage {
    private RicercaPage() {}

    public static void caricamento(Stage primaryStage, String comune, TipoVendita tv, BigDecimal prezzoMin, BigDecimal prezzoMax, Integer numStanze, ClasseEnergetica ce) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(LoginPage.class.getResource("/com/example/prova2/views/shared/schermataRicerca.fxml"));
        Scene mainScene = new Scene(fxmlLoader.load(), 1600, 850);
        SchermataRicercaController ricercaController = fxmlLoader.getController();

        new Thread(() -> {
            ricercaController.initialize(comune,tv,prezzoMin,prezzoMax,numStanze,ce);
            ricercaController.loadAnnunci(comune, tv, prezzoMin, prezzoMax, numStanze, ce);
            ricercaController.setTextIniziali(tv,prezzoMin,prezzoMax,numStanze,ce);
        }).start();
        
        primaryStage.setScene(mainScene);
        primaryStage.setTitle("I tuo immobili");
    }
}

