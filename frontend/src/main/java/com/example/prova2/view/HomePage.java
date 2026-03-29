package com.example.prova2.view;

import java.io.IOException;
import java.util.Optional;

import com.example.prova2.controller.HomePageController;
import com.example.prova2.dto.ClienteDatiDTO;
import com.example.prova2.facade.ClienteServiceFacade;
import com.example.prova2.factory.AlertFactory;
import com.example.prova2.model.Cliente;
import com.example.prova2.model.Utente;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

public class HomePage {

    
    private HomePage() {}

    // =========================================================================
    // METODI DI INIZIALIZZAZIONE
    // =========================================================================

    /**
     * Inizializzazione standard per l'utente loggato normalmente.
     */
    public static void initializeHomePage(Stage primaryStage, Utente utente) throws IOException {
        HomePageController controller = caricaInterfacciaBase(primaryStage);
        controller.initialize(utente);
    }

    /**
     * Inizializzazione specifica per l'utente loggato tramite Google.
     * Prevede il recupero dei dati aggiuntivi e il controllo del profilo.
     */
    public static void initializeHomePageWithGoogle(Stage primaryStage, Utente utente) throws IOException {
        HomePageController controller = caricaInterfacciaBase(primaryStage);

        // 1. Recupero dati specifici per l'accesso Google
        ClienteDatiDTO datiClienteDTO = ClienteServiceFacade.recuperoDati(utente);
        Cliente clienteGoogle = new Cliente(datiClienteDTO.getNome(), datiClienteDTO.getCognome(), datiClienteDTO.getEmail(), datiClienteDTO.getTelefono());
        clienteGoogle.setToken(utente.getToken());

        // 2. Setup del controller
        HomePageController.setUtente(clienteGoogle);
        controller.initialize(clienteGoogle);

        // 3. Mostra la home PRIMA di fare il check
        primaryStage.show();

        // 4. Controllo asincrono per verificare se il profilo è completo
        Platform.runLater(() -> {
            try {
                gestisciControlloProfilo(primaryStage, clienteGoogle);
            } catch (IOException e) {
                AlertFactory.creaAlertErroreInterno();
            }
        });
    }


    // =========================================================================
    // METODI PRIVATI DI SUPPORTO
    // =========================================================================

    /**
     * Metodo di supporto per caricare il file FXML e impostare la Scena base.
     * Evita di duplicare il codice nei due metodi di inizializzazione.
     */
    private static HomePageController caricaInterfacciaBase(Stage primaryStage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HomePage.class.getResource("/com/example/prova2/views/shared/schermataHome.fxml"));
        Scene mainScene = new Scene(fxmlLoader.load(), 1540, 790);
        primaryStage.setScene(mainScene);
        primaryStage.setTitle("Home");
        
        return fxmlLoader.getController();
    }

    /**
     * Gestisce la logica di verifica del profilo Google.
     */
    private static void gestisciControlloProfilo(Stage primaryStage, Utente utente) throws IOException {
        if (isProfiloIncompleto(utente)) {
            boolean vuoleCompletare = mostraAlertProfiloIncompleto();
            if (vuoleCompletare) {
                ModificaProfiloGoogle.initializePageModificaProfiloClienteGoogle(primaryStage, utente);
            }
        }
    }

    /**
     * Verifica se i campi obbligatori dell'utente sono vuoti.
     */
    private static boolean isProfiloIncompleto(Utente cliente) {
        System.out.println("Verifica profilo: " + cliente.getNome() + " " + cliente.getCognome());
        return cliente.getNome() == null || cliente.getNome().trim().isEmpty() ||
               cliente.getCognome() == null || cliente.getCognome().trim().isEmpty();
    }

    /**
     * Mostra l'Alert grafico per chiedere all'utente di completare il profilo.
     */
    private static boolean mostraAlertProfiloIncompleto() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Profilo Incompleto");
        alert.setHeaderText("Attenzione! Il tuo profilo è incompleto.");
        alert.setContentText("Per continuare al meglio, completa il tuo profilo con nome e cognome.");

        ButtonType completaOra = new ButtonType("Completa ora");
        ButtonType continua = new ButtonType("Continua senza modificare", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(completaOra, continua);

        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == completaOra;
    }
}