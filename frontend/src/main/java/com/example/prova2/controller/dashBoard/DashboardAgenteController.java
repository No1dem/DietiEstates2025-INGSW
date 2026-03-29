package com.example.prova2.controller.dashBoard;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import com.example.prova2.controller.HomePageController;
import com.example.prova2.controller.modificaProfilo.ModificaProfiloAgenteController;
import com.example.prova2.controller.notifiche.VisualizzaNotificheAgenteController;
import com.example.prova2.dto.DatiDTO;
import com.example.prova2.dto.ImmobileResponseRicercaDTO;
import com.example.prova2.facade.AgenteServiceFacade;
import com.example.prova2.facade.VisualizzaNotificheFacade;
import com.example.prova2.factory.AlertFactory;
import com.example.prova2.factory.AnnunciFactory;
import com.example.prova2.model.Agente;
import com.example.prova2.service.ImmobileService;
import com.example.prova2.service.AmazonS3Service;
import com.example.prova2.view.CalendarioAgente;
import com.example.prova2.view.CreaAnnuncioAgente;
import com.example.prova2.view.HomePage;
import com.example.prova2.view.LoginAgenteImmobiliare;
import com.example.prova2.view.ModificaProfiloAgente;
import com.example.prova2.view.VisualizzaNotificheAgente;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class DashboardAgenteController {

    // =========================================================================
    // COMPONENTI UI (FXML) - PANNELLI E SCROLL
    // =========================================================================
    @FXML private ScrollPane annunciScrollPane;
    @FXML private Pane paneLasciaValutazione;
    @FXML private Pane paneEliminaImmobile;

    // =========================================================================
    // COMPONENTI UI (FXML) - ETICHETTE (LABEL)
    // =========================================================================
    @FXML private Label nomeLbl;
    @FXML private Label labelCognome;
    @FXML private Label labelMail;
    @FXML private Label labelNomeProf;
    @FXML private Label labelCognomeProf;
    @FXML private Label labelTel;
    @FXML private Label labelBioProf;
    @FXML private Label labelValutazione;
    @FXML private Label scrittaNoAnnunci1;
    @FXML private Label scrittaNoAnnunci2;

    // =========================================================================
    // COMPONENTI UI (FXML) - BOTTONI E IMMAGINI
    // =========================================================================
    @FXML private ImageView immagineProfiloAgente;
    @FXML private Button btnCreaAnnunci;
    @FXML private Button btnModificaIlProfilo;
    @FXML private Button btnNotifiche;
    @FXML private Button btnIndietro;
    @FXML private Button btnGestisciApp;
    @FXML private Button buttonLogout;
    @FXML private Button bottoneNoAnnunci;

    // =========================================================================
    // VARIABILI DI STATO E GETTER/SETTER
    // =========================================================================
    private static Agente agente;
    private static Scene previousScene;

    public static void setAgenteNuovo(Agente agenteNuovo) {
        agente = agenteNuovo;
    }

    public static Agente getAgente() {
        return agente;
    }

    public static void setAgente(Agente agente) {
        DashboardAgenteController.agente = agente;
    }

    public void setPreviousScene(Scene scene) {
        previousScene = scene;
    }

    // =========================================================================
    // INIZIALIZZAZIONE E CARICAMENTO DASHBOARD (Thread asincroni)
    // =========================================================================
    @FXML
    public void initialize() {
        CompletableFuture<Void> first = CompletableFuture.runAsync(() ->
                getDatiAgente(agente.getAccountAgente().getEmail(), agente.getToken())
        );

        CompletableFuture<Void> second = CompletableFuture.runAsync(this::getAnnunciForAgente);

        CompletableFuture<Void> third = CompletableFuture.runAsync(this::loadValutazione, Platform::runLater);
    }

    private void getDatiAgente(String email, String token) {
        CompletableFuture.supplyAsync(() -> VisualizzaNotificheFacade.getAgenteByEmail(email, token))
                .thenAcceptAsync(datiAgente -> {
                    if (datiAgente != null) {
                        Platform.runLater(() -> {
                            extracted(datiAgente);
                            CompletableFuture.runAsync(this::fetchFoto);
                        });
                    }
                });
    }

    private void extracted(DatiDTO dto) {
        nomeLbl.setText(dto.getNome());
        labelCognome.setText(dto.getCognome());
        labelNomeProf.setText(dto.getNome());
        labelCognomeProf.setText(dto.getCognome());
        labelMail.setText(dto.getEmail());
        labelTel.setText(dto.getTelefono());
        labelBioProf.setText(dto.getBio());
        agente.setCf(dto.getCf());
        agente.setDto(dto);
    }

    // =========================================================================
    // NAVIGAZIONE E AZIONI UTENTE (Event Handlers)
    // =========================================================================
    @FXML
    public void apriPaginaCreaAnnunci() {
        try {
            CreaAnnuncioAgente.initializePageCreaAnnuncio((Stage) btnCreaAnnunci.getScene().getWindow(), String.valueOf(agente.getCf()), this);
        } catch (IOException e) {
            AlertFactory.creaAlertErroreCaricamentoPagina();
            e.printStackTrace();
        }
    }

    @FXML
    public void apriPaginaModificaProfilo() {
        try {
            ModificaProfiloAgenteController.setUtente(agente);
            ModificaProfiloAgente.initializePageModificaProfilo(btnModificaIlProfilo.getScene().getWindow());
        } catch (IOException e) {
            e.printStackTrace();
            AlertFactory.creaAlertErroreCaricamentoPagina();
        }
    }

    @FXML
    public void apriPaginaVisualizzaNotifiche() {
        VisualizzaNotificheAgenteController.setAgente(agente);
        VisualizzaNotificheAgente.initPage((Stage) btnNotifiche.getScene().getWindow());
    }

    @FXML
    public void apriCalendario() {
        CalendarioAgente.initCalendario((Stage) btnGestisciApp.getScene().getWindow());
    }

    @FXML
    public void apriPaginaCercaImmobili() {
        try {
            HomePageController.setUtente(DashboardAgenteController.getAgente());
            HomePage.initializeHomePage((Stage) buttonLogout.getScene().getWindow(), agente);
        } catch (IOException e) {
            AlertFactory.creaAlertErroreCaricamentoPagina();
        }
    }

    @FXML
    public void faiLogout() {
        Alert alert = AlertFactory.creaAlertLogout();
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            agente = null;
            vaiIndietro();
        } else {
            System.out.println("Logout annullato");
        }
    }

    @FXML
    public void vaiIndietro() {
        try {
            LoginAgenteImmobiliare.initializePageLoginAgente(buttonLogout.getScene().getWindow());
        } catch (IOException e) {
            AlertFactory.creaAlertErroreCaricamentoPagina();
        }
    }

    @FXML
    public void vaiAllaSchermataPrecedente() {
        if (previousScene != null) {
            Stage stage = (Stage) btnIndietro.getScene().getWindow();
            stage.setScene(previousScene);
        } else {
            System.out.println("Errore: previousScene è null");
            AlertFactory.creaAlertErroreInterno();
        }
    }

    // =========================================================================
    // RECUPERO DATI ASINCRONI: FOTO, ANNUNCI, VALUTAZIONE
    // =========================================================================
    private void fetchFoto() {
        List<String> urls = AgenteServiceFacade.getFotoAgente(agente.getCf());
        if (agenteHaFoto(urls)) {
            String urlImmagine = AmazonS3Service.getImageFromS3(urls.getFirst());
            setImmagineProfilo(urlImmagine);
        } else {
            setImmagineProfilo("C:/Users/Utente/Desktop/frontend/src/main/resources/com/example/prova2/images/user.png/");
        }
    }

    private static boolean agenteHaFoto(List<String> urls) {
        return !urls.isEmpty();
    }

    private void setImmagineProfilo(String s) {
        immagineProfiloAgente.setImage(new Image(s));
        agente.setFotoProfilo(s);
    }

    private List<ImmobileResponseRicercaDTO> prendiAnnunci() throws IOException, InterruptedException {
        return ImmobileService.getAnnunciForAgente(agente.getAccountAgente().getEmail(), agente.getToken());
    }

    public void getAnnunciForAgente() {
        CompletableFuture.runAsync(() -> {
            try {
                List<ImmobileResponseRicercaDTO> immobili = prendiAnnunci();
                if (immobili.isEmpty()) {
                    return;
                }
                
                bottoneNoAnnunci.setVisible(false);
                scrittaNoAnnunci1.setVisible(false);
                scrittaNoAnnunci2.setVisible(false);
                
                VBox vBox = new VBox();
                vBox.setSpacing(10);
                vBox.setPadding(new Insets(10));
                vBox.setFillWidth(true);

                int i = 1;
                for (ImmobileResponseRicercaDTO annunci : immobili) {
                    HBox hbox = AnnunciFactory.createHboxAnnunciAgente(
                            String.valueOf(i),
                            annunci.getTitolo(),
                            annunci.getComune(),
                            annunci.getVia(),
                            String.valueOf(annunci.getSuperficie()),
                            String.valueOf(annunci.getPrezzo()),
                            paneEliminaImmobile,
                            vBox,
                            annunci.getIdImmobile()
                    );
                    vBox.getChildren().add(hbox);
                    i++;
                }
                Platform.runLater(() -> annunciScrollPane.setContent(vBox));

            } catch (IOException e) {
                e.printStackTrace();
                AlertFactory.creaAlertErroreCaricamentoPagina();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                AlertFactory.creaAlertErroreCaricamentoPagina();
            }
        });
    }

    public void loadValutazione() {
        Image starEmpty = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/example/prova2/images/casaVuota.png")));
        Image starFull = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/example/prova2/images/casaPiena.png")));

        HBox starBox = new HBox(15);
        starBox.setPadding(new Insets(10));
        starBox.setAlignment(Pos.CENTER);
        starBox.setLayoutX(13);
        starBox.setLayoutY(31);
        
        CompletableFuture.supplyAsync(() ->
                AgenteServiceFacade.getValutazioneAgenteByEmail(agente.getAccountAgente().getEmail(), agente.getToken())
        ).thenAcceptAsync(valutazioneObj -> {
            Platform.runLater(() -> {
                starBox.getChildren().clear();
                caricaVal(starEmpty, starFull, starBox, valutazioneObj, paneLasciaValutazione);
            });
        });
    }

    public static void caricaVal(Image starEmpty, Image starFull, HBox starBox, Integer valutazioneObj, Pane paneLasciaValutazione) {
        int valutazione = (valutazioneObj != null) ? valutazioneObj : 0;
        System.out.println(valutazione + "la tua");
        
        for (int i = 0; i < 5; i++) {
            ImageView star = new ImageView(i < valutazione ? starFull : starEmpty);
            star.setFitWidth(40);
            star.setFitHeight(40);
            starBox.getChildren().add(star);
        }

        paneLasciaValutazione.getChildren().add(starBox);
    }
}