package com.example.prova2.controller.notifiche;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.example.prova2.controller.dashBoard.DashboardAgenteController;
import com.example.prova2.facade.NotificaServiceFacade;
import com.example.prova2.factory.AlertFactory;
import com.example.prova2.model.Agente;
import com.example.prova2.model.CategoriaNotifica;
import com.example.prova2.view.DisattivaCategorieAgente;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public class DisattivaCategorieAgenteController {

    // =========================================================================
    // COMPONENTI UI 
    // =========================================================================
    @FXML private Pane paneVisitaImmobile;

    @FXML private CheckBox checkBoxvisitaImmobile;

    @FXML private Label visitaImmobileCategoria;
    @FXML private Label scrittaInAlto;
    @FXML private Label scrittaNoNot1;
    @FXML private Label scrittaNoNot2;
    @FXML private Label scrittaCar;

    @FXML private ImageView immagineNoNot;
    @FXML private ImageView logoCar;

    @FXML private Button btnCancellaCategorie;
    @FXML private Button btnaggiorna;

    // =========================================================================
    // VARIABILI DI STATO E MAPPE
    // =========================================================================
    private HashMap<Label, CheckBox> mapForLabelCheckBox = new HashMap<>();
    private HashMap<Label, Pane> mapForLabelPane = new HashMap<>();
    private VisualizzaNotificheAgenteController controllerPaginaPrecedente;

    // =========================================================================
    // INIZIALIZZAZIONE E SETUP UI
    // =========================================================================
    public void initCategoriDisattivate(VisualizzaNotificheAgenteController visualizzaNotificheController) {
        this.controllerPaginaPrecedente = visualizzaNotificheController;
        checkBoxvisitaImmobile.setSelected(true);
        
        mapForLabelCheckBox = makeHashMapForLabelCheckBox();
        mapForLabelPane.put(visitaImmobileCategoria, paneVisitaImmobile);
        
        List<Label> categorieDisponibili = initArrayCategorieDisponibili();
        
        new Thread(() -> {
            List<CategoriaNotifica> categorieDisattivate = NotificaServiceFacade.getCategorieDisattivate(DashboardAgenteController.getAgente());
            Platform.runLater(() -> {
                initCategorie(categorieDisattivate, categorieDisponibili, mapForLabelCheckBox);
            });
        }).start();
    }

    private void initCategorie(List<CategoriaNotifica> categorieDisattivate, List<Label> categorieDisponibili, HashMap<Label, CheckBox> map) {
        List<CategoriaNotifica> allcategorieAgente = NotificaServiceFacade.getCategorieDispAgente(DashboardAgenteController.getAgente().getCf(), DashboardAgenteController.getAgente().getToken());
        gestisciCasoNonCiSonoNotifiche(allcategorieAgente);
        
        if (!allcategorieAgente.isEmpty()) {
            scrittaCar.setVisible(false);
            logoCar.setVisible(false);
            scrittaInAlto.setVisible(true);
            btnaggiorna.setVisible(true);
        }
        
        attivaPane(allcategorieAgente, categorieDisponibili);
        
        for (CategoriaNotifica categoria : categorieDisattivate) {
            for (Label categoriaDisponibile : categorieDisponibili) {
                if (isCategoriaDisattivataUgualeCategoriaDisponibile(categoria, categoriaDisponibile)) {
                    map.get(categoriaDisponibile).setSelected(false);
                }
            }
        }
    }

    // =========================================================================
    // AZIONI UI 
    // =========================================================================
    @FXML
    public void aggiorna() {
        new Thread(this::aggiornaCategorieInBaseAllePreferenze).start();
        Alert alert = AlertFactory.creaAlertAggiornamentoCategoria();
        alert.showAndWait().ifPresentOrElse(responseUtente -> {
            chiudiFinestra();
        }, this::chiudiFinestra);
    }

    public void chiudiFinestra() {
        DisattivaCategorieAgente.modalStage.close();
        controllerPaginaPrecedente.init(DashboardAgenteController.getAgente());
    }

    private void aggiornaCategorieInBaseAllePreferenze() {
        for (Label categoria : mapForLabelCheckBox.keySet()) {
            if (mapForLabelCheckBox.get(categoria).isSelected()) {
                NotificaServiceFacade.attivaCategoriaPerAgente(categoria.getText(), DashboardAgenteController.getAgente().getCf(), DashboardAgenteController.getAgente().getToken());
            } else {
                NotificaServiceFacade.disattivaCategoriaPerAgente(categoria.getText(), DashboardAgenteController.getAgente().getCf(), DashboardAgenteController.getAgente().getToken());
            }
        }
        controllerPaginaPrecedente.init(new Agente(DashboardAgenteController.getAgente().getAccountAgente().getEmail(), DashboardAgenteController.getAgente().getToken()));
    }

    // =========================================================================
    // METODI HELPER E GESTIONE LOGICA
    // =========================================================================
    private void gestisciCasoNonCiSonoNotifiche(List<CategoriaNotifica> allcategorieAgente) {
        if (allcategorieAgente.isEmpty()) {
            scrittaCar.setVisible(false);
            logoCar.setVisible(false);
            scrittaInAlto.setVisible(true);
            btnaggiorna.setVisible(true);

            scrittaInAlto.setVisible(false);
            btnaggiorna.setVisible(false);
            scrittaNoNot1.setVisible(true);
            scrittaNoNot2.setVisible(true);
            immagineNoNot.setVisible(true);
        }
    }

    private void attivaPane(List<CategoriaNotifica> allcategorieAgente, List<Label> categorieDisponibili) {
        for (CategoriaNotifica categoria : allcategorieAgente) {
            for (Label categoriaDisponibile : categorieDisponibili) {
                if (confrontoCategorie(categoria.getLabel(), categoriaDisponibile.getText())) {
                    mapForLabelPane.get(categoriaDisponibile).setVisible(true);
                }
            }
            System.out.println(categoria.getLabel());
        }
    }

    private HashMap<Label, CheckBox> makeHashMapForLabelCheckBox() {
        HashMap<Label, CheckBox> map = new HashMap<>();
        map.put(visitaImmobileCategoria, checkBoxvisitaImmobile);
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
        categorieDisponibili.add(visitaImmobileCategoria);
        return categorieDisponibili;
    }
}