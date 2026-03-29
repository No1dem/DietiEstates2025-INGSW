package com.example.prova2.controller.notifiche;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.example.prova2.controller.dashBoard.DashBoardClienteController;
import com.example.prova2.facade.NotificaServiceFacade;
import com.example.prova2.factory.AlertFactory;
import com.example.prova2.model.CategoriaNotifica;
import com.example.prova2.view.DisattivaCategorieCliente;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public class DisattivaCategorieClienteController {

    // =========================================================================
    // COMPONENTI UI (FXML) - PANNELLI E CHECKBOX
    // =========================================================================
    @FXML private Pane paneConsiglioImmobile;
    @FXML private Pane paneEsitoViste;
    
    @FXML private CheckBox checkBoxesitoCategoria;
    @FXML private CheckBox checkBoxConsiglioImmobile;

    // =========================================================================
    // COMPONENTI UI (FXML) - ETICHETTE E IMMAGINI
    // =========================================================================
    @FXML private Label esitoCategoriaVisita;
    @FXML private Label consigliImmobileCategoria;
    @FXML private Label scrittaNoNot;
    @FXML private Label scrittaNoNot2;
    @FXML private Label scrittaInAlto;
    @FXML private Label scrittaCar;

    @FXML private ImageView immagineNoNot;
    @FXML private ImageView logoCar;

    // =========================================================================
    // COMPONENTI UI (FXML) - BOTTONI
    // =========================================================================
    @FXML private Button btnCancellaCategorie;
    @FXML private Button btnAggiorna;

    // =========================================================================
    // VARIABILI DI STATO E MAPPE
    // =========================================================================
    private HashMap<Label, CheckBox> mapForLabelCheckBox = new HashMap<>();
    private HashMap<Label, Pane> mapForPaneLabel = new HashMap<>();
    private VisualizzaNotificheClienteController controllerPaginaPrecedente;

    // =========================================================================
    // INIZIALIZZAZIONE E SETUP UI
    // =========================================================================
    public void initCategoriDisattivate(VisualizzaNotificheClienteController visualizzaNotificheController) {
        this.controllerPaginaPrecedente = visualizzaNotificheController;
        checkBoxesitoCategoria.setSelected(true);
        checkBoxConsiglioImmobile.setSelected(true);
        
        mapForLabelCheckBox = makeHashMapForLabelCheckBox();
        List<Label> categorieDisponibili = initArrayCategorieDisponibili();
        
        new Thread(() -> {
            List<CategoriaNotifica> categorieDisattivate = NotificaServiceFacade.getCategorieDisattivateCliente(DashBoardClienteController.getCliente());
            Platform.runLater(() -> {
                initCategorie(categorieDisattivate, categorieDisponibili, mapForLabelCheckBox);
            });
        }).start();
    }

    private void initCategorie(List<CategoriaNotifica> categorieDisattivate, List<Label> categorieDisponibili, HashMap<Label, CheckBox> map) {
        List<CategoriaNotifica> categorieRecuperate = NotificaServiceFacade.getCategorieDispCliente(DashBoardClienteController.getCliente().getAccountAgente().getEmail(), DashBoardClienteController.getCliente().getToken());
        gestisciCasoNoCategorie(categorieRecuperate);
        
        if (!categorieRecuperate.isEmpty()) {
            scrittaCar.setVisible(false);
            logoCar.setVisible(false);
            scrittaInAlto.setVisible(true);
            btnAggiorna.setVisible(true);
        }
        
        attivaPane(categorieRecuperate, categorieDisponibili);
        spostaPaneSeUnoSolo(categorieRecuperate);

        for (CategoriaNotifica categoria : categorieDisattivate) {
            for (Label categoriaDisponibile : categorieDisponibili) {
                if (isCategoriaDisattivataUgualeCategoriaDisponibile(categoria, categoriaDisponibile)) {
                    map.get(categoriaDisponibile).setSelected(false);
                }
            }
        }
    }

    // =========================================================================
    // AZIONI UI (Event Handlers)
    // =========================================================================
    @FXML
    public void aggiornaCategorie() {
        new Thread(this::aggiornaCategorieInBaseAllePreferenze).start();
        Alert alert = AlertFactory.creaAlertAggiornamentoCategoria();
        alert.showAndWait().ifPresentOrElse(responseUtente -> {
            chiudiFinestra();
        }, this::chiudiFinestra);
    }

    @FXML
    public void annullaCategorie() {
        // Metodo predisposto per l'evento Annulla 
    }

    public void chiudiFinestra() {
        DisattivaCategorieCliente.modalStage.close();
        controllerPaginaPrecedente.init();
    }

    private void aggiornaCategorieInBaseAllePreferenze() {
        for (Label categoria : mapForLabelCheckBox.keySet()) {
            if (isPaneVisibile(categoria)) {
                if (mapForLabelCheckBox.get(categoria).isSelected()) {
                    NotificaServiceFacade.attivaCategoriaPerCliente(categoria.getText(), DashBoardClienteController.getCliente().getAccountAgente().getEmail(), DashBoardClienteController.getCliente().getToken());
                } else {
                    NotificaServiceFacade.disattivaCategoriaPerCliente(categoria.getText(), DashBoardClienteController.getCliente().getAccountAgente().getEmail(), DashBoardClienteController.getCliente().getToken());
                }
            }
        }
        controllerPaginaPrecedente.init();
    }

    // =========================================================================
    // METODI HELPER E GESTIONE LOGICA GRAFICA
    // =========================================================================
    private void spostaPaneSeUnoSolo(List<CategoriaNotifica> categorieRecuperate) {
        if (categorieRecuperate.size() == 1 && (categorieRecuperate.getFirst().getLabel().equals("ESITOVISITA"))) {
            mapForPaneLabel.get(esitoCategoriaVisita).setLayoutY(200);
        }
    }

    private void gestisciCasoNoCategorie(List<CategoriaNotifica> categorieRecuperate) {
        if (categorieRecuperate.isEmpty()) {
            scrittaCar.setVisible(false);
            logoCar.setVisible(false);
            scrittaInAlto.setVisible(true);
            btnAggiorna.setVisible(true);

            scrittaInAlto.setVisible(false);
            btnAggiorna.setVisible(false);
            scrittaNoNot.setVisible(true);
            scrittaNoNot2.setVisible(true);
            immagineNoNot.setVisible(true);
        }
    }

    private void attivaPane(List<CategoriaNotifica> categorieRecuperate, List<Label> categorieDisponibili) {
        for (CategoriaNotifica categoria : categorieRecuperate) {
            for (Label categoriaDisponibile : categorieDisponibili) {
                if (confrontoCategorie(categoriaDisponibile.getText(), categoria.getLabel())) {
                    Pane disattivaPannello = mapForPaneLabel.get(categoriaDisponibile);
                    disattivaPannello.setVisible(true);
                }
            }
        }
    }

    private HashMap<Label, CheckBox> makeHashMapForLabelCheckBox() {
        HashMap<Label, CheckBox> map = new HashMap<>();
        map.put(consigliImmobileCategoria, checkBoxConsiglioImmobile);
        map.put(esitoCategoriaVisita, checkBoxesitoCategoria);
        return map;
    }

    private boolean isCategoriaDisattivataUgualeCategoriaDisponibile(CategoriaNotifica categoria, Label categoriaDisponibile) {
        return confrontoCategorie(categoria.getLabel(), categoriaDisponibile.getText());
    }

    private boolean confrontoCategorie(String label, String text) {
        String labelNormalized = label.toLowerCase().replaceAll("\\s+", "");
        String textNormalized = text.toLowerCase().replaceAll("\\s+", "");
        return labelNormalized.equals(textNormalized);
    }

    private List<Label> initArrayCategorieDisponibili() {
        List<Label> categorieDisponibili = new ArrayList<>();
        categorieDisponibili.add(consigliImmobileCategoria);
        categorieDisponibili.add(esitoCategoriaVisita);
        mapForPaneLabel.put(consigliImmobileCategoria, paneConsiglioImmobile);
        mapForPaneLabel.put(esitoCategoriaVisita, paneEsitoViste);
        return categorieDisponibili;
    }

    private boolean isPaneVisibile(Label categoria) {
        return mapForPaneLabel.get(categoria).isVisible();
    }
}