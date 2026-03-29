package com.example.prova2.controller.notifiche;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import com.example.prova2.controller.AnnullaVisita;
import com.example.prova2.controller.dashBoard.DashBoardClienteController;
import com.example.prova2.dto.AddVisitaRequestDTO;
import com.example.prova2.dto.DatiImmobileDTO;
import com.example.prova2.dto.GetNotificheDTO;
import com.example.prova2.facade.ImmobileFacade;
import com.example.prova2.facade.NotificaServiceFacade;
import com.example.prova2.facade.UtenteFacade;
import com.example.prova2.facade.VisitaServiceFacade;
import com.example.prova2.factory.AlertFactory;
import com.example.prova2.factory.factoryNotifiche.FactoryForPageNotifica;
import com.example.prova2.factory.factoryNotifiche.FactoryNotificheCliente;
import com.example.prova2.model.Cliente;
import com.example.prova2.model.Immobile;
import com.example.prova2.service.MappaService2;
import com.example.prova2.service.NotificaService;
import com.example.prova2.view.DisattivaCategorieCliente;
import com.example.prova2.view.EffettuaPrenotazione;
import com.example.prova2.view.VisualizzaNotificheAgente;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class VisualizzaNotificheClienteController extends VisualizzaNotificheAgenteController implements AnnullaVisita {

    // =========================================================================
    // COMPONENTI UI (FXML)
    // =========================================================================
    @FXML private Button tornaAllaHomeBtn;
    @FXML private Label singoleCategorie;
    @FXML private Pane paneAnnullamentoVisitaPrenotata;
    @FXML private Label annullaTasto;

    // =========================================================================
    // VARIABILI DI STATO E GETTER/SETTER
    // =========================================================================
    private static Cliente clienteNotifiche;
    private AddVisitaRequestDTO visitaPrenotata;
    private int notificationIndex = 0;

    public static void setClienteNotifiche(Cliente clienteNotifiche) {
        VisualizzaNotificheClienteController.clienteNotifiche = clienteNotifiche;
    }

    public static Cliente getClienteNotifiche() {
        return clienteNotifiche;
    }

    // =========================================================================
    // INIZIALIZZAZIONE E CARICAMENTO NOTIFICHE
    // =========================================================================
    public void init() {
        Platform.runLater(this::caricaNotifiche);
    }

    @Override
    public void caricaNotifiche() {
        notificationIndex = 0;
        if (clienteNotifiche.getAccountAgente().getEmail() != null) {
            List<GetNotificheDTO> notificationsOfCliente = prendiNotificheCliente();
            
            // Metodo ereditato dal padre. 
            gestisciImmagineNoNotifica(notificationsOfCliente);
            
            Platform.runLater(() -> {
                // Variabile ereditata dal padre.
                notificheContainer.getChildren().clear();
                VBox notificationList = new VBox();
                notificationList.setSpacing(10);
                
                for (GetNotificheDTO notifica : notificationsOfCliente) {
                    Pane pane = null;
                    try {
                        pane = creaNotificaPaneCliente(notifica);
                    } catch (IOException | InterruptedException e) {
                        AlertFactory.creaAlertErroreInterno();
                    }
                    notificationList.getChildren().add(pane);
                }
                
                if (notificationsOfCliente.size() > 3) {
                    aggiungiScrollPane(notificationList, notificheContainer);
                } else {
                    notificheContainer.getChildren().add(notificationList);
                }
            });
        }
    }

    private List<GetNotificheDTO> prendiNotificheCliente() {
        return NotificaServiceFacade.getNotificaCliente(clienteNotifiche);
    }

    // =========================================================================
    // GENERAZIONE DINAMICA DELLA UI 
    // =========================================================================
    public static void aggiungiScrollPane(VBox notificationList, VBox notificheContainer) {
        ScrollPane scrollPane = new ScrollPane(notificationList);
        scrollPane.setFitToWidth(true);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent; -fx-border-color: transparent;");
        
        StackPane stackPane = new StackPane(scrollPane);
        stackPane.setStyle("-fx-background-color: transparent; -fx-padding: 10px;");
        stackPane.setMaxHeight(500);
        stackPane.setPrefHeight(900);
        
        notificheContainer.getChildren().add(stackPane);
    }

    private Pane creaNotificaPaneCliente(GetNotificheDTO notifica) throws IOException, InterruptedException {
        notificationIndex++;
        Pane pane = new Pane();
        pane.setPrefSize(700, 162); 
        
        pane.setStyle("-fx-background-color: rgba(255, 255, 255, 0.15); " +
                      "-fx-background-radius: 20; " +
                      "-fx-border-color: rgba(255,255,255,0.3); " +
                      "-fx-border-radius: 20; " +
                      "-fx-border-width: 1;");

        Label titolo;
        Pane titoloPane;
        ImageView imgLeft = new ImageView();
        
        imgLeft.setFitWidth(55);
        imgLeft.setFitHeight(55);
        imgLeft.setPreserveRatio(true);
        imgLeft.setLayoutX(25);
        imgLeft.setLayoutY(45);

        Button selezionaAltroOrario = null;
        
        if (isCategoriaEsitoVisita(notifica)) {
            boolean esitoNotifica = notifica.getNomeNotifica().contains("accettata");
            if (isNotificaRifiutata(esitoNotifica)) {
                selezionaAltroOrario = selezionaNuovoOrario(notifica);
            }
            titolo = FactoryNotificheCliente.createScrittaEsitoImmobile(esitoNotifica);
            titoloPane = FactoryNotificheCliente.createPaneEsitoNotifica(esitoNotifica);
            
            new Thread(() -> {            
                UtenteFacade.caricaImmagineProfiloAsync(notifica, imgLeft, DashBoardClienteController.getCliente().getToken());
            }).start();

            FactoryNotificheCliente.aggiustaImmagineProfilo(imgLeft);
            imgLeft.setClip(new javafx.scene.shape.Circle(27.5, 27.5, 27.5)); 
            
        } else {
            titoloPane = FactoryNotificheCliente.createPaneConsiglioImmobile();
            titolo = FactoryNotificheCliente.createScrittaConsiglioImmobile("Consiglio immobile");
            imgLeft.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/example/prova2/images/DE25Logo.png"))));
            
            FactoryForPageNotifica.createLogo(imgLeft);
            imgLeft.setFitWidth(55);
            imgLeft.setFitHeight(55);
            imgLeft.setLayoutX(35); 
            imgLeft.setLayoutY(50);
        }

        titoloPane.getChildren().add(titolo);
        
        Label msg = FactoryNotificheCliente.setContenuto(notifica, notificationIndex);
        Label dettagli = FactoryNotificheCliente.createLabelVisualizzaDettagli(this, notifica, clienteNotifiche);
        Button okBtn = FactoryForPageNotifica.createButtonOk("Ok");

        if (selezionaOrarioNonDisponibile(selezionaAltroOrario)) {
            aggiungiOggettiAlPane(pane, titoloPane, msg, imgLeft, dettagli, okBtn);
        } else {
            aggiungiButtonSelezionaOrario(pane, titoloPane, msg, imgLeft, dettagli, okBtn, selezionaAltroOrario);
        }
        
        aggiungiListenerPerOkNotifica(notifica, okBtn, pane, titoloPane);
        return pane;
    }

    // =========================================================================
    // HELPER PER LA COSTRUZIONE UI E EVENTI CLICK
    // =========================================================================
    private Button selezionaNuovoOrario(GetNotificheDTO notifica) {
        Button selezionaAltroOrarioButton = FactoryForPageNotifica.createButtonSelezioneNuovaOra("Seleziona un altro orario");
        selezionaAltroOrarioButton.setVisible(true);
        aggiungiListenerperSelezionaOrario(selezionaAltroOrarioButton, notifica);
        return selezionaAltroOrarioButton;
    }

    private void aggiungiListenerperSelezionaOrario(Button selezionaUnAltroOrarioButton, GetNotificheDTO notifica) {
        selezionaUnAltroOrarioButton.setOnAction(e -> {
            DatiImmobileDTO dto = ImmobileFacade.getInfo(notifica.getIdImmobile(), DashBoardClienteController.getCliente().getToken());
            Platform.runLater(() -> {
                String indirizzoCompleto = dto.getIndirizzo() + "," + dto.getNumeroCivico() + "," + dto.getComune();
                String coordinate = MappaService2.getCoordinatesFromAddress(indirizzoCompleto);
                Double lat = null;
                Double lng = null;
                if (coordinate != null) {
                    String[] dati = coordinate.split(",");
                    lat = Double.parseDouble(dati[0]);
                    lng = Double.parseDouble(dati[1]);
                }
                Immobile immobile = new Immobile(dto, notifica, lat, lng);
                apriPaginaPrenotazione(selezionaUnAltroOrarioButton, immobile);
            });
        });
    }

    private void aggiungiListenerPerOkNotifica(GetNotificheDTO notifica, Button accetta, Pane pane, Pane titoloPane) {
        accetta.setOnAction(e -> {
            notificheContainer.getChildren().remove(pane);
            pane.setVisible(false);
            pane.setManaged(false);
            new Thread(() -> {            
                NotificaService.setNotificationAccepted(notifica.getIdNotifica(), DashBoardClienteController.getCliente().getToken());
            }).start();
        });
    }

    // =========================================================================
    // AZIONI UI E NAVIGAZIONE (Handlers)
    // =========================================================================
    @FXML
    public void tornaAllaDash() {
        VisualizzaNotificheAgente.getModalStage().close();
    }

    @FXML
    public void disattivaCategorie() {
        try {
            DisattivaCategorieCliente.initPage((Stage) singoleCategorie.getScene().getWindow(), this);
        } catch (IOException e) {
            AlertFactory.creaAlertErroreInterno();
        }
    }

    @Override
    public void mostraPaneAnnullaVisita(AddVisitaRequestDTO dto) {
        paneAnnullamentoVisitaPrenotata.setVisible(true);
        this.visitaPrenotata = dto;
    }

    @FXML
    public void annulla(MouseEvent mouseEvent) {
        paneAnnullamentoVisitaPrenotata.setVisible(false);
        VisitaServiceFacade.annullaVisita(visitaPrenotata);
    }

    private void apriPaginaPrenotazione(Button selezionaAltroOrarioButton, Immobile immobile) {
        try {
            EffettuaPrenotazione.initPage((Stage) selezionaAltroOrarioButton.getScene().getWindow(),
                    clienteNotifiche.getAccountAgente().getEmail(), immobile, this);
        } catch (IOException e) {
            e.printStackTrace();
            AlertFactory.creaAlertErroreInterno();
        }
    }

    // =========================================================================
    // METODI UTILITY STATIC E DI CONTROLLO LOGICO
    // =========================================================================
    private static boolean selezionaOrarioNonDisponibile(Button selezionaAltroOrario) {
        return selezionaAltroOrario == null;
    }

    private static void aggiungiButtonSelezionaOrario(Pane pane, Pane titoloPane, Label msg, ImageView imgLeft, Label dettagli, Button okBtn, Button selezionaAltroOrario) {
        pane.getChildren().addAll(titoloPane, msg, imgLeft, dettagli, okBtn, selezionaAltroOrario);
    }

    private static void aggiungiOggettiAlPane(Pane pane, Pane titoloPane, Label msg, ImageView imgLeft, Label dettagli, Button okBtn) {
        pane.getChildren().addAll(titoloPane, msg, imgLeft, dettagli, okBtn);
    }

    private static boolean isNotificaRifiutata(boolean esitoNotifica) {
        return !esitoNotifica;
    }

    static boolean isCategoriaEsitoVisita(GetNotificheDTO notifica) {
        return notifica.getCategoria().toString().equals("ESITOVISITA");
    }
}