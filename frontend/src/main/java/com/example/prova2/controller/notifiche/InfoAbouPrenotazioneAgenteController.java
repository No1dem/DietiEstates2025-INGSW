package com.example.prova2.controller.notifiche;

import java.util.Optional;

import com.example.prova2.controller.dashBoard.DashboardAgenteController;
import com.example.prova2.dto.GetNotificheDTO;
import com.example.prova2.dto.InformazioniVisitaDTO;
import com.example.prova2.facade.VisitaServiceFacade;
import com.example.prova2.factory.AlertFactory;
import com.example.prova2.service.NotificaService;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

public class InfoAbouPrenotazioneAgenteController {

    // =========================================================================
    // COMPONENTI UI (FXML)
    // =========================================================================
    @FXML private Label casaLbl;
    @FXML private Label viaLbl;
    @FXML private Label oraLbl;
    @FXML private Label nomeLbl;
    
    @FXML protected Button btnAccetta;
    @FXML protected  Button btnRifiuta;

    // =========================================================================
    // VARIABILI DI STATO E DEPENDENCIES
    // =========================================================================
    private VisualizzaNotificheAgenteController controller;
    private Pane panePrecedente;
    private int numeroNotifica;

    // =========================================================================
    // COSTRUTTORI
    // =========================================================================
    public InfoAbouPrenotazioneAgenteController() {}

    // =========================================================================
    // INIZIALIZZAZIONE E SETUP
    // =========================================================================
    public void setController(VisualizzaNotificheAgenteController controller) {
        this.controller = controller;
    }

    public void initPage(int notifica, Pane pane) {
        this.numeroNotifica = notifica;
        new Thread(() -> {
            InformazioniVisitaDTO visita = VisitaServiceFacade.getInfoAboutVisita(notifica, DashboardAgenteController.getAgente().getToken());
            Platform.runLater(() -> {
                setDati(visita);
                nomeLbl.setText(visita.getEmailClientePrenotatoVisita());
                this.panePrecedente = pane;
            });
        }).start();
    }

    public void setDati(InformazioniVisitaDTO visita) {
        casaLbl.setText(visita.getTipoImmobile().toString());
        viaLbl.setText("Via" + visita.getViaImmobile() + " " + "n°" + visita.getNumeroCivico() + " " + visita.getComune());
        oraLbl.setText(visita.getDataVisita().toString() + " " + visita.getOraInizioVisita().toString() + "-" + visita.getOraFineVisita());
    }

    // =========================================================================
    // AZIONI UI (Event Handlers)
    // =========================================================================
    @FXML
    public void accettaVisita() {
        Alert alert = AlertFactory.creaAlertAccettaNotifica();
        Optional<ButtonType> result = alert.showAndWait();
        gestisciAccettazione(result);
    }

    @FXML
    public void rifiutaVisita() {
        Alert alert = AlertFactory.creaAlertRifiutaNotifica();
        Optional<ButtonType> result = alert.showAndWait();
        gestisciRifiuto(result);
    }

    // =========================================================================
    // METODI HELPER E LOGICA DI BUSINESS
    // =========================================================================
    private void gestisciAccettazione(Optional<ButtonType> result) {
        if (result.isPresent() && result.get() == ButtonType.OK) {
            NotificaService.setNotificationAccepted(this.numeroNotifica, DashboardAgenteController.getAgente().getToken());
            Alert alert = AlertFactory.creaAlertVisitaAccettata();
            chiudiFinestra(alert, btnAccetta);
            controller.mostraPaneAnnullaNotifica(new GetNotificheDTO(this.numeroNotifica), "Visita accettata", panePrecedente);
        }
    }

    private void gestisciRifiuto(Optional<ButtonType> result) {
        if (result.isPresent()) {
            if (result.isPresent() && result.get() == ButtonType.OK) {
                NotificaService.setNotificationRejected(this.numeroNotifica, DashboardAgenteController.getAgente().getToken());
                Alert alert = AlertFactory.creaAlertVisitaRifiutata();
                chiudiFinestra(alert, btnRifiuta);
                controller.mostraPaneAnnullaNotifica(new GetNotificheDTO(this.numeroNotifica), "Visita rifiutata", panePrecedente);
            }
        }
    }

    public void chiudiFinestra(Alert alert, Button button) {
        Optional<ButtonType> result1 = alert.showAndWait();
        if (!result1.isPresent() || result1.get() == ButtonType.OK) {
            btnAccetta.getScene().getWindow().hide();
        }
    }
}