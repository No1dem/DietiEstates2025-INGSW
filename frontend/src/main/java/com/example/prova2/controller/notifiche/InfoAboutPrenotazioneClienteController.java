package com.example.prova2.controller.notifiche;

import com.example.prova2.controller.dashBoard.DashBoardClienteController;
import com.example.prova2.dto.InformazioniVisitaDTO;
import com.example.prova2.facade.VisitaServiceFacade;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class InfoAboutPrenotazioneClienteController extends InfoAbouPrenotazioneAgenteController {

    // =========================================================================
    // COMPONENTI UI (FXML) SPECIFICI DI QUESTA SOTTOCLASSE
    // =========================================================================
    @FXML private Label nomeLbl;

    // =========================================================================
    // VARIABILI DI STATO E DEPENDENCIES
    // =========================================================================
    private VisualizzaNotificheClienteController controller;

    // =========================================================================
    // GETTER E SETTER
    // =========================================================================
    public void setController(VisualizzaNotificheClienteController controller) {
        this.controller = controller;
    }

    public VisualizzaNotificheClienteController getController() {
        return controller;
    }

    // =========================================================================
    // INIZIALIZZAZIONE E CARICAMENTO DATI
    // =========================================================================
    public void initPage(int notifica) {
        
        // Operazioni immediate sulla UI (prima del thread)
        // NOTA: btnAccetta e btnRifiuta sono ereditati dal padre. 
        // Assicurati che nel padre siano 'protected' e non 'private'!
        if (btnAccetta != null) btnAccetta.setVisible(false);
        if (btnRifiuta != null) btnRifiuta.setVisible(false);
        
        new Thread(() -> {
            try {
                // Scarico i dati (lavoro pesante fuori dal thread UI)
                InformazioniVisitaDTO visita = VisitaServiceFacade.getInfoAboutVisita(
                        notifica, 
                        DashBoardClienteController.getCliente().getToken()
                );
                
                if (visita != null) {
                    Platform.runLater(() -> {
                        try {
                            setDati(visita); // Metodo ereditato dalla classe padre
                            
                            if (nomeLbl != null) {
                                nomeLbl.setText(visita.getAgente());
                            }
                            
                            System.out.println("5. Grafica popolata con successo!");
                        } catch (Exception e) {
                            System.err.println("Errore nel setDati: " + e.getMessage());
                        }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
}