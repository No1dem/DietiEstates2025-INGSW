package com.example.prova2.controller;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

import com.example.prova2.controller.dashBoard.DashboardAgenteController;
import static com.example.prova2.controller.dashBoard.DashboardAgenteController.caricaVal;
import com.example.prova2.dto.AddVisitaRequestDTO;
import com.example.prova2.dto.ImmobileResponseRicercaDTO;
import com.example.prova2.facade.AgenteServiceFacade;
import com.example.prova2.facade.VisitaServiceFacade;
import com.example.prova2.factory.AlertFactory;
import static com.example.prova2.factory.AnnunciFactory.createImageView;
import com.example.prova2.model.Account;
import com.example.prova2.model.Agente;
import com.example.prova2.model.Cliente;
import com.example.prova2.model.GestoreAgenziaImmobiliare;
import com.example.prova2.model.Immobile;
import com.example.prova2.model.Utente;
import com.example.prova2.service.AmazonS3Service;
import com.example.prova2.service.ImmagineService;
import com.example.prova2.service.MappaService2;
import com.example.prova2.view.DashBoardCliente;
import com.example.prova2.view.DashboardAgente;
import com.example.prova2.view.DashboardAmministratore;
import com.example.prova2.view.EffettuaPrenotazione;
import com.example.prova2.view.HomePage;
import com.example.prova2.view.LasciaRecensione;

import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Duration;
import netscape.javascript.JSObject;

public class SchermataAnnuncioController implements AnnullaVisita {

    // =========================================================================
    // COMPONENTI UI (FXML) - PANNELLI E CONTENITORI
    // =========================================================================
    @FXML private Pane paneAnnullaVisita;
    @FXML protected Pane paneSuccessoRecensione;
    @FXML private Pane paneLasciaValutazione;
    @FXML private ScrollPane scrollImageAnnuncio;
    @FXML private ScrollPane annunciScrollPane;
    @FXML private WebView webViewMap;

    // =========================================================================
    // COMPONENTI UI (FXML) - ETICHETTE E TESTI
    // =========================================================================
    @FXML private Label annullaTasto;
    @FXML private Label scrittaCaricamento;
    @FXML private Label ascensoreLbl;
    @FXML private Label tipoImmobileLbl;
    @FXML private Label indirizzoLbl;
    @FXML private Label labelPrezzo;
    @FXML private Label labelInfoTitolo;
    @FXML private Label infoDescrizioneLbl;
    @FXML private Label numeroStanzeLbl;
    @FXML private Label numeroBagniLbl;
    @FXML private Label numCucineLbl;
    @FXML private Label numeroSoggiorniLbl;
    @FXML private Label numeroPianoLbl;
    @FXML private Label superficieLbl;
    @FXML private Label arredamentoLbl;
    @FXML private Label classeEnergeticaLbl;
    @FXML private Label speseCondominialiLbl;
    @FXML private Label labelNomeAgente;

    // =========================================================================
    // COMPONENTI UI (FXML) - BOTTONI E IMMAGINI
    // =========================================================================
    @FXML private ImageView imageAgente;
    @FXML private Button btnPrenotaVisita;
    @FXML private Button btnLasciaRecensione;
    @FXML private Button btnIndietro2;
    @FXML private Button btnIndietroImage;
    @FXML private Button btnAvantiImage;
    @FXML private Button btnUserDashboard;
    @FXML private Hyperlink linkHome;

    // =========================================================================
    // VARIABILI DI STATO E SERVIZI
    // =========================================================================
    private static Utente utenteCheCerca;
    private static ImmobileResponseRicercaDTO annuncio;
    private static BigDecimal prezzoMin;
    private static BigDecimal prezzoMax;
    private static Scene previousScene;

    private WebEngine webEngine;
    private boolean isMapLoaded = false;
    private final MappaService2 mappaService = new MappaService2();
    private AddVisitaRequestDTO visitaFatta;
    private boolean flag;

    // =========================================================================
    // COSTRUTTORE E GETTER/SETTER
    // =========================================================================
    public SchermataAnnuncioController() {}

    public void setFlag(boolean flag) { this.flag = flag; }
    public boolean isFlag() { return flag; }

    public void setPreviousScene(Scene previousScene) { SchermataAnnuncioController.previousScene = previousScene; }

    public static void setUtenteCheCerca(Utente utenteCheCerca) { SchermataAnnuncioController.utenteCheCerca = utenteCheCerca; }
    public static Utente getUtenteCheCerca() { return utenteCheCerca; }

    public static void setDatiRicerca(ImmobileResponseRicercaDTO annuncioDto, BigDecimal prezzoMin, BigDecimal prezzoMax) {
        SchermataAnnuncioController.annuncio = annuncioDto;
        SchermataAnnuncioController.prezzoMin = prezzoMin;
        SchermataAnnuncioController.prezzoMax = prezzoMax;
    }

    // =========================================================================
    // INIZIALIZZAZIONE E CARICAMENTO DATI
    // =========================================================================
    public void initialize() {
        annunciScrollPane.addEventFilter(ScrollEvent.SCROLL, event -> {
            if (event.getDeltaY() != 0) {
                double delta = event.getDeltaY();
                annunciScrollPane.setVvalue(annunciScrollPane.getVvalue() - delta / 200);
                event.consume();
            }
        });
        scrollImageAnnuncio.setHbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        webViewMap.addEventFilter(ScrollEvent.SCROLL, Event::consume);
    }

    public void loadAnnuncio(ImmobileResponseRicercaDTO annuncioDto) {
        new Thread(() -> {
            if (utenteCheCerca instanceof Agente || utenteCheCerca instanceof GestoreAgenziaImmobiliare) {
                btnPrenotaVisita.setDisable(true);
                btnLasciaRecensione.setDisable(true);
            }
            Platform.runLater(() -> {
                caricaImmagini(annuncioDto);
                setLabelImmobile(annuncioDto);
                setOnActioPrenota(annuncioDto);
                setFotoAgente(annuncioDto);
                loadMap(annuncioDto);
                btnIndietroImage.setDisable(false);
            });
        }).start();
    }

    private void setLabelImmobile(ImmobileResponseRicercaDTO annuncioDto) {
        setDatiLabel(annuncioDto);
    }

    private void setDatiLabel(ImmobileResponseRicercaDTO annuncioDto) {
        String tipoImmobile = String.valueOf(annuncioDto.getTipoimmobile()).toLowerCase();
        tipoImmobile = tipoImmobile.substring(0, 1).toUpperCase() + tipoImmobile.substring(1);
        String tipoArredamento = String.valueOf(annuncioDto.getArredamento()).toLowerCase();
        tipoArredamento = tipoArredamento.substring(0, 1).toUpperCase() + tipoArredamento.substring(1);

        tipoImmobileLbl.setText(tipoImmobile);
        arredamentoLbl.setText(tipoArredamento);

        indirizzoLbl.setText(annuncioDto.getComune() + " " + annuncioDto.getVia() + " " + annuncioDto.getNumeroCivico());
        labelPrezzo.setText("€ " + annuncioDto.getPrezzo());
        String tipoVendita = capitalize(annuncioDto.getTipovendita().toString());

        labelInfoTitolo.setText(annuncioDto.getTitolo() + " in " + tipoVendita);
        infoDescrizioneLbl.setText(annuncioDto.getDescrizione());
        numeroStanzeLbl.setText(String.valueOf(annuncioDto.getNumeroStanze()));
        numeroBagniLbl.setText(String.valueOf(annuncioDto.getNumeroBagni()));
        numCucineLbl.setText(String.valueOf(annuncioDto.getNumeroCucine()));
        numeroSoggiorniLbl.setText(String.valueOf(annuncioDto.getNumeroSoggiorni()));
        numeroPianoLbl.setText(String.valueOf(annuncioDto.getPiano()));
        superficieLbl.setText(annuncioDto.getSuperficie() + " m²");

        classeEnergeticaLbl.setText(String.valueOf(annuncioDto.getClasseEnergetica()));
        speseCondominialiLbl.setText("€ " + annuncioDto.getSpeseCondominiali());
        labelNomeAgente.setText(annuncioDto.getNomeAgente() + " " + annuncioDto.getCognomeAgente());
        
        if (annuncioDto.isAscensore()) {
            ascensoreLbl.setText("Dotato di ascensore");
        } else {
            ascensoreLbl.setText("Non Dotato di ascensore");
        }
    }

    private String capitalize(String str) {
        if (str == null || str.isEmpty()) return str;
        return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
    }

    // =========================================================================
    // GESTIONE IMMAGINI ANNUNCIO E SCORRIMENTO
    // =========================================================================
    private void caricaImmagini(ImmobileResponseRicercaDTO annuncioDto) {
        Task<List<String>> imagesTask = prendiImmagini(annuncioDto);
        Thread thread = new Thread(imagesTask);
        thread.setDaemon(true);
        thread.start();
    }

    private Task<List<String>> prendiImmagini(ImmobileResponseRicercaDTO annuncioDto) {
        Task<List<String>> fetchImagesTask = new Task<>() {
            @Override
            protected List<String> call() throws Exception {
                return ImmagineService.getFotoOfImmobile(annuncioDto.getIdImmobile()).join();
            }
        };

        fetchImagesTask.setOnSucceeded(event -> {
            List<String> images = fetchImagesTask.getValue();
            System.out.println("DEBUG FOTO - Il DB ha restituito " + (images != null ? images.size() : 0) + " foto per l'annuncio " + annuncioDto.getIdImmobile());

            if (images != null && !images.isEmpty()) {
                double x = 0;
                HBox hBox = new HBox(50);
                hBox.setAlignment(Pos.CENTER);
                
                int contatore = 1;
                for (String imageName : images) {
                    System.out.println("👉 Foto " + contatore + " richiesta dal DB: [" + imageName + "]");
                    try {
                        if (imageName == null || imageName.trim().isEmpty()) {
                            System.err.println("   ERRORE: Il DB ha un campo vuoto al posto del nome della foto!");
                            continue; 
                        }
                        String url = AmazonS3Service.getImageFromS3(imageName);
                        if (url != null) {
                            aggiungiImmaginiAlloScroll(hBox, scrollImageAnnuncio, url, x);
                            x += 0.50;
                        } else {
                            System.err.println("   ATTENZIONE: S3 non ha restituito l'URL per la foto: " + imageName);
                        }
                    } catch (Exception e) {
                        System.err.println("   ERRORE GRAVE caricando la foto " + imageName + ": " + e.getMessage());
                    }
                    contatore++;
                }
                
                if (btnIndietroImage != null) btnIndietroImage.setVisible(false);
                if (btnAvantiImage != null) btnAvantiImage.setVisible(hBox.getChildren().size() > 1);

                scrittaCaricamento.setVisible(false);
                btnIndietro2.setVisible(true);

                if (utenteCheCerca instanceof Cliente) {
                    btnLasciaRecensione.setDisable(false);
                    btnPrenotaVisita.setDisable(false);
                }
            }
        });

        fetchImagesTask.setOnFailed(event -> {
            Throwable ex = fetchImagesTask.getException();
            System.err.println("Errore durante il recupero delle immagini dal DB: " + ex.getMessage());
            ex.printStackTrace();
        });

        return fetchImagesTask;
    }

    private void aggiungiImmaginiAlloScroll(HBox hBox, ScrollPane scrollPane, String url, double x) {
        if (scrollPane != null) {
            ImageView imageView = createImageView(url, 925, 400, x, 14);
            hBox.getChildren().add(imageView);
            scrollPane.setContent(hBox);
        }
    }

    @FXML
    private void handlerTastoAvanti() {
        System.out.println("AVANTI");
        double currentHvalue = scrollImageAnnuncio.getHvalue();
        scrollImageAnnuncio.setHvalue(Math.min(1, currentHvalue + 0.4));
        aggiornaVisibilitaFrecce();
    }

    @FXML
    private void handlerTastoIndietro() {
        double currentHvalue = scrollImageAnnuncio.getHvalue();
        double newHvalue = Math.max(0, currentHvalue - 0.4);
        scrollImageAnnuncio.setHvalue(newHvalue);
        aggiornaVisibilitaFrecce();
    }

    public void aggiornaVisibilitaFrecce() {
        double currentHvalue = scrollImageAnnuncio.getHvalue();

        if (btnIndietroImage != null) {
            boolean mostraIndietro = currentHvalue > 0.05;
            if (!mostraIndietro && btnIndietroImage.isFocused()) {
                if (btnAvantiImage != null) btnAvantiImage.requestFocus();
            }
            btnIndietroImage.setVisible(mostraIndietro);
        }

        if (btnAvantiImage != null) {
            boolean mostraAvanti = currentHvalue < 0.95;
            if (!mostraAvanti && btnAvantiImage.isFocused()) {
                if (btnIndietroImage != null) btnIndietroImage.requestFocus();
                else scrollImageAnnuncio.requestFocus(); 
            }
            btnAvantiImage.setVisible(mostraAvanti);
        }
    }

    public void setBtnIndietro() {
        double currentHvalue = scrollImageAnnuncio.getHvalue();
        btnIndietroImage.setVisible(currentHvalue > 0.1);
    }

    private void setFotoAgente(ImmobileResponseRicercaDTO annuncioDto) {
        new Thread(() -> {
            Platform.runLater(() -> {
                String cfAgente = annuncioDto.getCfAgente();
                System.out.println("cf agente" + cfAgente);
                List<String> image = AgenteServiceFacade.getFotoAgente(cfAgente);
                String urlImage = AmazonS3Service.getImageFromS3(image.getFirst());
                if (urlImage != null) {
                    imageAgente.setImage(new Image(urlImage));
                }
            });
        }).start();
    }

    // =========================================================================
    // GESTIONE MAPPA WEBVIEW
    // =========================================================================
    private void loadMap(ImmobileResponseRicercaDTO annuncioDto) {
        webEngine = webViewMap.getEngine();
        comunicationJavaJavascript(webEngine);
        mappaService.setWebEngine(webEngine);
        mappaService.loadMap();
        addMarkerMap(annuncioDto);
    }

    private void comunicationJavaJavascript(WebEngine webEngine) {
        Platform.runLater(() -> {
            webEngine.setUserDataDirectory(new File("user-data"));
            webEngine.getLoadWorker().stateProperty().addListener((obs, oldState, newState) -> {
                if (newState == Worker.State.SUCCEEDED) {
                    JSObject window = (JSObject) webEngine.executeScript("window");
                    window.setMember("java", this);
                    isMapLoaded = true;
                }
            });
        });
    }

    private void addMarkerMap(ImmobileResponseRicercaDTO annuncioDto) {
        new Thread(() -> {
            String viaPulita = annuncioDto.getVia() != null ? annuncioDto.getVia().split(",")[0].trim() : "";
            String indirizzo = viaPulita + " " + annuncioDto.getNumeroCivico() + ", " + annuncioDto.getComune();
            
            System.out.println("GEO-DEBUG ANNUNCIO: Cerco " + indirizzo);
            String coordinates = MappaService2.getCoordinatesFromAddress(indirizzo);
            
            if (coordinates == null) {
                coordinates = MappaService2.getCoordinatesFromAddress(annuncioDto.getComune());
            }
            
            if (coordinates != null) {
                String[] dati = coordinates.split(",");
                double lat = Double.parseDouble(dati[0]);
                double lng = Double.parseDouble(dati[1]);
                
                String coordinatesComune = MappaService2.getCoordinatesFromAddress(annuncioDto.getComune());
                if (coordinatesComune != null) {
                    String[] datiComune = coordinatesComune.split(",");
                    if (datiComune[0] != null && datiComune[1] != null) {
                        double latComune = Double.parseDouble(datiComune[0]);
                        double lngComune = Double.parseDouble(datiComune[1]);
                        annuncioDto.setLatitudine(lat);
                        annuncioDto.setLongitudine(lng);
                        
                        Platform.runLater(() -> mappaService.addMarker2(latComune, lngComune, lat, lng));
                    }
                }
            } else {
                System.err.println("GEO-ERRORE ANNUNCIO: Coordinate non trovate per " + indirizzo);
            }
        }).start();
    }

    // =========================================================================
    // AZIONI UTENTE (Visite e Recensioni)
    // =========================================================================
    private void setOnActioPrenota(ImmobileResponseRicercaDTO annuncioDto) {
        btnPrenotaVisita.setOnAction(event -> apriPaginaPrenotazioni(annuncioDto));
    }

    private EventHandler<ActionEvent> apriPaginaPrenotazioni(ImmobileResponseRicercaDTO ricercaDTO) {
        try {
            EffettuaPrenotazione.initPage((Stage) btnPrenotaVisita.getScene().getWindow(),
                    utenteCheCerca.getAccountAgente().getEmail(), new Immobile(ricercaDTO), this);
        } catch (IOException e) {
            e.printStackTrace();
            AlertFactory.creaAlertErroreCaricamentoPagina();
        }
        return null;
    }

    public void mostraPaneAnnullaVisita(AddVisitaRequestDTO visita) {
        this.visitaFatta = visita;
        paneAnnullaVisita.setVisible(true);
        PauseTransition pausa = new PauseTransition(Duration.seconds(6));
        pausa.setOnFinished(event -> paneAnnullaVisita.setVisible(false));
        pausa.play();
    }

    public void annulla(MouseEvent mouseEvent) {
        paneAnnullaVisita.setVisible(false);
        new Thread(() -> {
            VisitaServiceFacade.annullaVisita(visitaFatta);
        }).start();
    }

    public void apriLasciRecensione() {
        String nomeCognomeAgente = annuncio.getNomeAgente() + " " + annuncio.getCognomeAgente();
        String cfAgente = annuncio.getCfAgente();
        try {
            LasciaRecensione.initPage((Stage) btnLasciaRecensione.getScene().getWindow(), nomeCognomeAgente, utenteCheCerca, cfAgente, this);
        } catch (RuntimeException e) {
            e.printStackTrace();
            AlertFactory.creaAlertErroreInterno();
        }
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
                AgenteServiceFacade.getValutazioneAgente(annuncio.getCfAgente(), HomePageController.getUtente().getToken())
        ).thenAcceptAsync(valutazioneObj -> {
            Platform.runLater(() -> {
                starBox.getChildren().clear();
                System.out.println("Valutazione" + valutazioneObj);
                caricaVal(starEmpty, starFull, starBox, valutazioneObj, paneLasciaValutazione);
            });
        });
    }

    // =========================================================================
    // GESTIONE NAVIGAZIONE (Dashboard, Indietro, Home)
    // =========================================================================
    public void apriDashboard() {
        if (webViewMap != null && webViewMap.getEngine() != null) webViewMap.getEngine().load(null);

        Stage stage = (Stage) btnUserDashboard.getScene().getWindow();

        switch (utenteCheCerca) {
            case Cliente cliente -> apriDashboardCliente();
            case Agente agente -> apriDashboardAgente();
            case GestoreAgenziaImmobiliare gestoreAgenziaImmobiliare -> apriDashboardGestore();
            default -> AlertFactory.creaAlertErroreInterno();
        }

        stage.sizeToScene();
        stage.centerOnScreen();
    }

    private void apriDashboardGestore() {
        try { DashboardAmministratore.initializePageDashboardAmministratore((Stage) btnUserDashboard.getScene().getWindow()); } 
        catch (IOException e) { AlertFactory.creaAlertErroreInterno(); }
    }

    private void apriDashboardCliente() {
        try { DashBoardCliente.initializePageDashboardCliente(btnUserDashboard.getScene().getWindow(), utenteCheCerca); } 
        catch (InterruptedException | IOException e) {
            if (e instanceof InterruptedException) Thread.currentThread().interrupt();
            AlertFactory.creaAlertErroreCaricamentoPagina();
        }
    }

    private void apriDashboardAgente() {
        try {
            Agente agenteDash = new Agente();
            agenteDash.setAccountAgente(new Account(utenteCheCerca.getAccountAgente().getEmail(), utenteCheCerca.getAccountAgente().getPassword()));
            agenteDash.setToken(utenteCheCerca.getToken());
            DashboardAgenteController.setAgente(agenteDash);
            DashboardAgente.initializePageDashboardAgente(btnUserDashboard.getScene().getWindow());
        } catch (IOException | InterruptedException e) {
            if (e instanceof InterruptedException) Thread.currentThread().interrupt();
            e.printStackTrace();
            AlertFactory.creaAlertErroreCaricamentoPagina();
        }
    }

    public void tornaSchermataRicerca() {
        if (webViewMap != null && webViewMap.getEngine() != null) webViewMap.getEngine().load(null);

        if (previousScene != null) {
            Stage stage = (Stage) btnIndietro2.getScene().getWindow();
            stage.setScene(previousScene);
            stage.sizeToScene();
            stage.centerOnScreen();
        }
    }

    @FXML
    public void setLinkHome() throws IOException {
        if (webViewMap != null && webViewMap.getEngine() != null) webViewMap.getEngine().load(null);

        Stage stage = (Stage) linkHome.getScene().getWindow();
        caricaHome(stage, utenteCheCerca);
        stage.sizeToScene();
        stage.centerOnScreen();
    }

    @FXML
    public void tornaHome(ActionEvent event) {
        if (webViewMap != null && webViewMap.getEngine() != null) webViewMap.getEngine().load(null);

        try {
            HomePageController.setUtente(utenteCheCerca);
            Node source = (Node) event.getSource();
            Stage stage = (Stage) source.getScene().getWindow();
            HomePage.initializeHomePage(stage, utenteCheCerca);

            stage.sizeToScene();
            stage.centerOnScreen();
        } catch (Exception e) {
            System.err.println("Errore durante il ritorno alla Home: " + e.getMessage());
            e.printStackTrace();
            AlertFactory.creaAlertErroreInterno();
        }
    }

    private void caricaHome(Window window, Utente utente) throws IOException {
        try { HomePage.initializeHomePage((Stage) window, utente); } 
        catch (IOException e) { AlertFactory.creaAlertErroreCaricamentoPagina(); }
    }
}