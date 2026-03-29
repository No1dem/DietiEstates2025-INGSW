package com.example.prova2.controller.notifiche;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import com.example.prova2.controller.dashBoard.DashboardAmministratoreController;
import com.example.prova2.dto.GetNotificheDTO;
import com.example.prova2.factory.AlertFactory;
import com.example.prova2.factory.factoryNotifiche.FactoryForPageNotifica;
import com.example.prova2.factory.factoryNotifiche.FactoryNotificheCliente;
import com.example.prova2.model.GestoreAgenziaImmobiliare;
import com.example.prova2.service.NotificaService;
import com.example.prova2.view.CambioPassword;

import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

public class VisualizzaNotificheGestoreController extends VisualizzaNotificheAgenteController {

    // =========================================================================
    // VARIABILI DI STATO E DEPENDENCIES
    // =========================================================================
    private static GestoreAgenziaImmobiliare gestoreNotifiche;
    
    private DashboardAmministratoreController controllerPrecedente;
    private Stage schermataPrecedente; // Mantenuto il nome originale (nota: c'è un piccolo refuso 'Precednete' nell'originale)
    
    private Button okBtn;
    private int notificationIndex = 0;

    // =========================================================================
    // GETTER E SETTER
    // =========================================================================
    public void setWindowPrecedente(Stage schermataPrecedente) {
        this.schermataPrecedente = schermataPrecedente;
    }

    public void setControllerPrecedente(DashboardAmministratoreController controllerPrecedente) {
        this.controllerPrecedente = controllerPrecedente;
    }

    public DashboardAmministratoreController getControllerPrecedente() {
        return controllerPrecedente;
    }

    public static GestoreAgenziaImmobiliare getGestoreNotifiche() {
        return gestoreNotifiche;
    }

    public static void setGestoreNotifiche(GestoreAgenziaImmobiliare gestoreNotifiche) {
        VisualizzaNotificheGestoreController.gestoreNotifiche = gestoreNotifiche;
    }

    // =========================================================================
    // INIZIALIZZAZIONE E CARICAMENTO NOTIFICHE
    // =========================================================================
    public void init() {
        setGestoreNotifiche(DashboardAmministratoreController.getAdmin());
        Platform.runLater(this::caricaNotifiche);
    }

    @Override
    public void caricaNotifiche() {
        notificationIndex = 0;
        if (gestoreNotifiche.getCf() != null) {
            List<GetNotificheDTO> notificationsOfAgente = prendiNotificheGestore();
            
            // Metodo ereditato dalla classe padre
            gestisciImmagineNoNotifica(notificationsOfAgente);
            
            Platform.runLater(() -> {
                // Variabile ereditata dalla classe padre
                notificheContainer.getChildren().clear();
                VBox notificationList = new VBox();
                notificationList.setSpacing(10);
                
                for (GetNotificheDTO notifica : notificationsOfAgente) {
                    Pane pane = creaNotificaPaneCliente(notifica);
                    notificationList.getChildren().add(pane);
                }
                
                if (notificationsOfAgente.size() > 3) {
                    aggiungiScrollPane(notificationList); // Metodo ereditato
                } else {
                    notificheContainer.getChildren().add(notificationList);
                }
            });
        }
    }

    private List<GetNotificheDTO> prendiNotificheGestore() {
        try {
            return NotificaService.getNotificationsForAdmin(DashboardAmministratoreController.getAdmin());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return Collections.emptyList();
        } catch (IOException e) {
            return Collections.emptyList();
        }
    }

    // =========================================================================
    // GENERAZIONE DINAMICA DELLA UI (Pannelli)
    // =========================================================================
    private Pane creaNotificaPaneCliente(GetNotificheDTO notifica) {
        notificationIndex++;
        Pane pane = new Pane();
        pane.setPrefSize(900, 162);
        pane.setStyle("-fx-background-color: white; -fx-background-radius: 20;");
        
        Label titolo;
        Pane titoloPane;
        ImageView imgLeft = new ImageView();

        titoloPane = FactoryNotificheCliente.creaPaneForCategoria(notifica.getCategoria().toString());
        titolo = FactoryNotificheCliente.createScrittaLabel(notifica.getCategoria().toString());
        imgLeft.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/example/prova2/images/DE25Logo.png"))));
        
        FactoryForPageNotifica.createLogo(imgLeft);
        assert titoloPane != null;
        titoloPane.getChildren().add(titolo);
        
        Label messaggio = FactoryNotificheCliente.setContenuto(notifica, notificationIndex);
        
        if (isNotificaCambioPassword(notifica)) {
            okBtn = FactoryForPageNotifica.createButtonConferma("Cambia password");
            aggiungiListenerPerCambioPasswordButton(okBtn);
        } else {
            okBtn = FactoryForPageNotifica.createButtonOk("Ok");
            aggiungiListenerPerOkNotifica(notifica, okBtn, pane);
        }
        
        pane.getChildren().addAll(titoloPane, imgLeft, okBtn, messaggio);
        return pane;
    }

    // =========================================================================
    // GESTIONE EVENTI (Listeners e Navigazione)
    // =========================================================================
    private void aggiungiListenerPerCambioPasswordButton(Button okBtn) {
        okBtn.setOnAction(e -> {
            apriPaginaCambiaPassword();
        });
    }

    private void apriPaginaCambiaPassword() {
        try {
            Stage stage = (Stage) okBtn.getScene().getWindow();
            CambioPassword.initializePageCambioPassword(schermataPrecedente, getControllerPrecedente(), gestoreNotifiche);
            
            PauseTransition delay = new PauseTransition(Duration.seconds(0.5));
            delay.setOnFinished(event -> stage.close()); // Chiude la finestra dopo mezzo secondo
            delay.play(); // Avvia il ritardo

        } catch (IOException e) {
            e.printStackTrace();
            AlertFactory.creaAlertErrore("Errore apertura pagina!", "Siamo spiacenti si è verificato un errore nel caricamento della pagina cambio password");
        }
    }

    private void aggiungiListenerPerOkNotifica(GetNotificheDTO notifica, Button accetta, Pane pane) {
        accetta.setOnAction(e -> {
            new Thread(() -> {
                pane.setVisible(false);
                NotificaService.setNotificationAccepted(notifica.getIdNotifica(), DashboardAmministratoreController.getAdmin().getToken());
            }).start();
        });
    }

    // =========================================================================
    // METODI HELPER STATIC
    // =========================================================================
    private static boolean isNotificaCambioPassword(GetNotificheDTO notifica) {
        return notifica.getCategoria().getLabel().equals("CAMBIO PASSWORD");
    }
}