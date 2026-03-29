package com.example.prova2.controller;

import com.example.prova2.controller.dashBoard.DashboardAgenteController;
import com.example.prova2.dto.ImmobileResponseRicercaDTO;
import com.example.prova2.facade.ImmobileFacade;
import com.example.prova2.factory.AlertFactory;
import com.example.prova2.factory.AnnunciFactory;
import com.example.prova2.model.*;
import com.example.prova2.service.GeopifyService;
import com.example.prova2.service.ImmagineService;
import com.example.prova2.service.MappaService2;
import com.example.prova2.service.AmazonS3Service;
import com.example.prova2.view.*;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.*;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import netscape.javascript.JSObject;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.example.prova2.factory.AnnunciFactory.createImageView;

public class SchermataRicercaController {

    // =========================================================================
    // COMPONENTI UI (FXML) - PANNELLI, SCROLL E MAPPA
    // =========================================================================
    @FXML private Pane paneInfoAnnunci;
    @FXML private VBox boxErrore;
    @FXML private ScrollPane filtriScrollPane;
    @FXML private ScrollPane annunciScrollPane;
    @FXML private WebView webViewMap;
    @FXML private ListView<String> listMaps;

    // =========================================================================
    // COMPONENTI UI (FXML) - IMMAGINI ED ETICHETTE (LABEL)
    // =========================================================================
    @FXML private ImageView immagineNoCar;
    @FXML private ImageView immagineX;
    @FXML private Label scrittaCaricamento;
    @FXML private Label immobileNonTrovatoLbl;
    @FXML private Label immobileNonTrovatoLbl2;
    @FXML private Label immobileNonTrovatoLbl3;
    @FXML private Label comuniLbl;

    // =========================================================================
    // COMPONENTI UI (FXML) - INPUT, MENU E FILTRI
    // =========================================================================
    @FXML private MenuButton statoMenu;
    @FXML private MenuButton classeMenu;
    @FXML private TextField prezzoMinText;
    @FXML private TextField prezzoMaxText;
    @FXML private TextField textNumeroLocali;
    @FXML private TextField localitàTextField;

    // =========================================================================
    // COMPONENTI UI (FXML) - BOTTONI E LINK
    // =========================================================================
    @FXML private Button btnIndietro;
    @FXML private Button btnAggiornaRicerca;
    @FXML private Button btnUserDashboard;
    @FXML private Hyperlink linkHome;

    // =========================================================================
    // VARIABILI DI STATO E SERVIZI
    // =========================================================================
    private static Utente utenteCheCerca;

    private String comuneCercato;
    private TipoVendita tv;
    private ClasseEnergetica ce;
    private Integer numeroLocali;
    private BigDecimal prezzoMin;
    private BigDecimal prezzoMax;

    private final MappaService2 mappaService = new MappaService2();
    private WebEngine webEngine;
    private boolean isMapLoaded = false;
    private boolean flagNonAutoComplete = true;

    // =========================================================================
    // GETTER E SETTER
    // =========================================================================
    public static void setUtenteCheCerca(Utente utenteCheCerca) { SchermataRicercaController.utenteCheCerca = utenteCheCerca; }
    public static Utente getUtenteCheCerca() { return utenteCheCerca; }

    public void setPrezzoMin(BigDecimal prezzoMin) { this.prezzoMin = prezzoMin; }
    public void setPrezzoMax(BigDecimal prezzoMax) { this.prezzoMax = prezzoMax; }

    // =========================================================================
    // INIZIALIZZAZIONE E SETUP
    // =========================================================================
    public void initialize(String comune, TipoVendita tv, BigDecimal PrezzoMin, BigDecimal PrezzoMax, Integer numStanze, ClasseEnergetica ce) {
        Platform.runLater(() -> {
            webEngine = webViewMap.getEngine();
            comunicationJavaJavascript(webEngine);
            mappaService.setWebEngine(webEngine);
            mappaService.loadMap();
            
            HomePageController.aggiungiListener(prezzoMaxText);
            HomePageController.aggiungiListener(prezzoMinText);
            btnIndietro.setVisible(false);
            
            if (comune == null || comune.isEmpty()) {
                comuniLbl.setText("Località : " + "Qualsiasi");
                immagineX.setVisible(false);
            } else {
                comuniLbl.setText("Località: " + comune);
            }
            
            checkPrezzoMinMax(PrezzoMin, PrezzoMax);
            checkTipoVendita(tv);
            checkClasseEnergetica(ce);
            checkNumStanze(numStanze);
            
            ArrayList<ClasseEnergetica> options3 = new ArrayList<>();
            ArrayList<TipoVendita> options4 = new ArrayList<>();
            aggiungiOpzioniArrayClasse(options3);
            aggiungiOpzioniArrayTipi(options4);
            addListenerClasseEn(options3);
            addListenerTipologia(options4);
            
            addTextFormatter(localitàTextField);
            
            listMaps.focusedProperty().addListener((obs, oldVal, newVal) -> {
                if (Boolean.FALSE.equals(newVal)) {
                    listMaps.setVisible(false);
                }
            });

            annunciScrollPane.addEventFilter(ScrollEvent.SCROLL, event -> {
                if (event.getDeltaY() != 0) {
                    double delta = event.getDeltaY();
                    annunciScrollPane.setVvalue(annunciScrollPane.getVvalue() - delta / 900);
                    event.consume();
                }
            });

            listMaps.setOnMouseClicked(event -> {
                String selected = listMaps.getSelectionModel().getSelectedItem();
                if (selected != null) {
                    flagNonAutoComplete = true;
                    localitàTextField.setText(selected);
                    flagNonAutoComplete = false;
                    Platform.runLater(() -> {
                        listMaps.getItems().clear();
                        listMaps.setVisible(false);
                    });
                }
            });
            
            new Thread(() -> {
                prezzoMax = PrezzoMax;
                prezzoMin = PrezzoMin;
                this.comuneCercato = comune;
                this.tv = tv;
                this.ce = ce;
                this.numeroLocali = numStanze;
            }).start();
        });
    }

    private void addTextFormatter(TextField textField) {
        textField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                String sanitized = newValue.replaceAll("[^a-zA-Z ,]", "");
                if (!sanitized.isEmpty()) {
                    sanitized = sanitized.substring(0, 1).toUpperCase() + sanitized.substring(1);
                }
                if (sanitized.length() > 30) {
                    sanitized = sanitized.substring(0, 30);
                }
                if (!sanitized.equals(newValue)) {
                    textField.setText(sanitized);
                }
            }
        });
    }

    // =========================================================================
    // GESTIONE DELLA MAPPA WEBVIEW
    // =========================================================================
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

    private void aggiornaMappa(ImmobileResponseRicercaDTO ricercaDTO, creaPane result) {
        new Thread(() -> {
            String viaPulita = ricercaDTO.getVia().split(",")[0].trim();
            String indirizzoPerMappa = viaPulita + " " + ricercaDTO.getNumeroCivico() + ", " + ricercaDTO.getComune();
            
            String coordinates = MappaService2.getCoordinatesFromAddress(indirizzoPerMappa);
            
            if (coordinates == null) {
                System.err.println("GEO-WARNING: Via non trovata, ripiego sul centro di: " + ricercaDTO.getComune());
                coordinates = MappaService2.getCoordinatesFromAddress(ricercaDTO.getComune());
            }
            
            if (coordinates != null) {
                String[] dati = coordinates.split(",");
                double lat = Double.parseDouble(dati[0]);
                double lng = Double.parseDouble(dati[1]);
                
                String coordinatesComune = MappaService2.getCoordinatesFromAddress(ricercaDTO.getComune());
                
                if (coordinatesComune != null) {
                    String[] datiComune = coordinatesComune.split(",");
                    if (datiComune[0] != null && datiComune[1] != null) {
                        double latComune = Double.parseDouble(datiComune[0]);
                        double lngComune = Double.parseDouble(datiComune[1]);
                        ricercaDTO.setLatitudine(lat);
                        ricercaDTO.setLongitudine(lng);
                        
                        Platform.runLater(() -> mappaService.addMarker(latComune, lngComune, lat, lng, result.titolo(),
                                result.Prezzo(), result.descrizione(), String.valueOf(ricercaDTO.getIdImmobile())));
                    }
                }
            } else {
                System.err.println("GEO-ERRORE FATALE: Non ho trovato nemmeno il comune: " + ricercaDTO.getComune());
            }
        }).start();
    }

    // =========================================================================
    // RECUPERO E GENERAZIONE ANNUNCI ASINCRONA
    // =========================================================================
    public void loadAnnunci(String comune, TipoVendita tv, BigDecimal prezzoMin, BigDecimal prezzoMax, Integer numStanze, ClasseEnergetica ce) {
        Platform.runLater(() -> {
            if (!isMapLoaded) {
                webViewMap.getEngine().getLoadWorker().stateProperty().addListener((obs, oldState, newState) -> {
                    if (newState == Worker.State.SUCCEEDED) {
                        caricaAnnunciAsync(comune, tv, prezzoMin, prezzoMax, numStanze, ce);
                    }
                });
            } else {
                caricaAnnunciAsync(comune, tv, prezzoMin, prezzoMax, numStanze, ce);
            }
        });
    }

    private void caricaAnnunciAsync(String comune, TipoVendita tv, BigDecimal prezzoMin, BigDecimal prezzoMax, Integer numStanze, ClasseEnergetica ce) {
        List<ImmobileResponseRicercaDTO> ricerca = ImmobileFacade.ricercaImmobile(new Ricerca(ce, tv, comune, numStanze, prezzoMin, prezzoMax), utenteCheCerca);
        
        if (ricerca.isEmpty()) {
            scrittaCaricamento.setVisible(false);
            annunciScrollPane.setVisible(false);
            boxErrore.setVisible(true);
        }
        
        VBox vbox = getVBox();
        for (ImmobileResponseRicercaDTO ricercaDTO : ricerca) {
            Platform.runLater(() -> {
                try {
                    creaPane result = getCreaPane(ricercaDTO, vbox);
                    aggiornaMappa(ricercaDTO, result);
                    Task<List<String>> fetchImagesTask = recuperaImmagini(ricercaDTO, result);
                    Thread thread = new Thread(fetchImagesTask);
                    thread.setDaemon(true);
                    thread.start();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
    }

    private Task<List<String>> recuperaImmagini(ImmobileResponseRicercaDTO ricercaDTO, creaPane result) {
        Task<List<String>> fetchImagesTask = new Task<>() {
            @Override
            protected List<String> call() throws Exception {
                return ImmagineService.getFotoOfImmobile(ricercaDTO.getIdImmobile()).join();
            }
        };
        fetchImagesTask.setOnSucceeded(event -> {
            List<String> images = fetchImagesTask.getValue();
            System.out.println("DEBUG FOTO - Il DB ha restituito " + images.size() + " foto per questo annuncio.");
            if (!images.isEmpty()) {
                String url = AmazonS3Service.getImageFromS3(images.get(0));
                aggiungiImmaginiAlPane(result.pane, url);
                scrittaCaricamento.setVisible(false);
            }
        });
        fetchImagesTask.setOnFailed(event -> {
            Throwable ex = fetchImagesTask.getException();
            ex.printStackTrace();
        });
        return fetchImagesTask;
    }

    private void aggiungiImmaginiAlPane(Pane pane, String image) {
        if (pane != null) {
            ImageView imageView = createImageView(image, 331, 413, 14, 14);
            pane.getChildren().addAll(imageView);
        }
    }

    private creaPane getCreaPane(ImmobileResponseRicercaDTO ricercaDTO, VBox vbox) {
        String Prezzo = String.valueOf(ricercaDTO.getPrezzo());
        String numBagn = String.valueOf(ricercaDTO.getNumeroBagni());
        String numSuper = String.valueOf(ricercaDTO.getSuperficie());
        String numPiano = String.valueOf(ricercaDTO.getPiano());
        String numLocali = String.valueOf(ricercaDTO.getNumeroStanze());
        String tipologia = String.valueOf(ricercaDTO.getTipoimmobile());
        String indirizzo = ricercaDTO.getVia() + "," + ricercaDTO.getNumeroCivico() + "," + ricercaDTO.getComune();
        String descrizione = ricercaDTO.getDescrizione();
        String titolo = ricercaDTO.getTitolo();
        String comuneInd = ricercaDTO.getComune();

        Pane pane = AnnunciFactory.createPaneAnnunci();
        Pane paneInfo = AnnunciFactory.createPaneInfo();
        paneInfo.setId("annunci_" + ricercaDTO.getIdImmobile());
        Pane panePrezzi = AnnunciFactory.createPaneInfoPrezzi(Prezzo);
        Pane paneForniture = AnnunciFactory.createPaneInfoForniture(numBagn, numLocali, numPiano, numSuper);
        Label labelTipo = AnnunciFactory.createLabelInfoTipo(tipologia);
        Label indirizzoLbl = AnnunciFactory.createLabelIndirizzo(indirizzo);
        Pane paneDescrizione = AnnunciFactory.createPaneInfoDescrizione(descrizione);
        Pane paneImmagini = AnnunciFactory.createPaneImmagini("");

        pane.getChildren().addAll(paneInfo, paneImmagini);
        paneInfo.getChildren().addAll(panePrezzi, paneForniture, labelTipo, indirizzoLbl, paneDescrizione);
        
        pane.setOnMouseClicked(e -> {
            try {
                SchermataAnnuncio.initializeSchermataAnnuncio(pane, ricercaDTO, utenteCheCerca, prezzoMin, prezzoMax);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        
        vbox.getChildren().add(pane);
        annunciScrollPane.setContent(vbox);
        return new creaPane(Prezzo, indirizzo, descrizione, titolo, comuneInd, paneImmagini);
    }

    public record creaPane(String Prezzo, String indirizzo, String descrizione, String titolo, String comuneInd, Pane pane) {}

    private static VBox getVBox() {
        VBox vbox = new VBox();
        vbox.setPrefHeight(Region.USE_COMPUTED_SIZE);
        vbox.setPadding(new Insets(10));
        vbox.setSpacing(10);
        return vbox;
    }

    // =========================================================================
    // MOTORE DI RICERCA, AUTOCOMPLETE E FILTRI
    // =========================================================================
    public void gestioneRicercaImmobile() throws IOException {
        String comuneNuovo;
        if (localitàTextField.getText().isEmpty() && comuniLbl.getText().equals("Località : Qualsiasi")) {
            comuneNuovo = null;
        } else if (!localitàTextField.getText().isEmpty()) {
            comuneNuovo = localitàTextField.getText().trim();
        } else {
            comuneNuovo = this.comuneCercato;
        }
        
        String numLocaliInput = textNumeroLocali.getText();
        Integer numStanze = (numLocaliInput == null || numLocaliInput.trim().isEmpty() || !numLocaliInput.matches("\\d+"))
                ? null : Integer.parseInt(numLocaliInput);
        
        BigDecimal prezzoMin = parseBigDecimal(prezzoMinText.getText());
        BigDecimal prezzoMax = parseBigDecimal(prezzoMaxText.getText());
        
        TipoVendita tipoVendita = (Objects.equals(statoMenu.getText(), "Stato Vendita") || statoMenu.getText().trim().isEmpty())
                ? null : TipoVendita.valueOf(statoMenu.getText().trim());
                
        ClasseEnergetica classeEnergetica = (Objects.equals(classeMenu.getText(), "Classe energetica") || classeMenu.getText().trim().isEmpty())
                ? null : ClasseEnergetica.fromString(classeMenu.getText().trim());

        if (Objects.equals(comuneNuovo, this.comuneCercato) && Objects.equals(numStanze, this.numeroLocali) 
                && Objects.equals(prezzoMin, this.prezzoMin) && Objects.equals(prezzoMax, this.prezzoMax)
                && this.ce == classeEnergetica && this.tv == tipoVendita) {
            return;
        }
        RicercaPage.caricamento((Stage) btnAggiornaRicerca.getScene().getWindow(), comuneNuovo, tipoVendita, prezzoMin, prezzoMax, numStanze, classeEnergetica);
    }

    @FXML
    public void suggerimento() {
        fetchAsync(localitàTextField.getText());
    }

    private void fetchAsync(String newText) {
        Task<Void> task = new Task<>() {
            @Override
            public Void call() {
                List<String> suggerimenti = GeopifyService.searchLocation(newText);
                Platform.runLater(() -> {
                    if (suggerimenti.isEmpty()) {
                        listMaps.setVisible(false);
                        return;
                    }
                    listMaps.getItems().clear();
                    listMaps.getItems().addAll(suggerimenti);
                    listMaps.setVisible(true);
                });
                return null;
            }
        };
        new Thread(task).start();
    }

    private BigDecimal parseBigDecimal(String input) {
        if (input == null || input.trim().isEmpty()) return null;
        String cleanInput = input.replaceAll("[^0-9,]", "").replace(",", ".");
        try {
            return new BigDecimal(cleanInput);
        } catch (NumberFormatException e) {
            System.err.println("Errore nel parsing del BigDecimal: " + input);
            return null;
        }
    }

    public String formattaStringa(String input) {
        if (input == null || input.trim().isEmpty() || 
            input.equals("Inserire il prezzo minimo") || 
            input.equals("Inserire il prezzo massimo")) {
            return input;
        }
        try {
            String cleanInput = input.replaceAll("[^0-9]", "");
            if (cleanInput.isEmpty()) return input;
            long number = Long.parseLong(cleanInput);
            DecimalFormat formatter = new DecimalFormat("#,###");
            return formatter.format(number);
        } catch (NumberFormatException e) {
            System.err.println("Errore formattazione: " + input);
            return input;
        }
    }

    // =========================================================================
    // EVENTI UI SCORRIMENTO E CHECK FILTRI (Helper)
    // =========================================================================
    @FXML
    private void handlerTastoAvanti() {
        double currentHvalue = filtriScrollPane.getHvalue();
        filtriScrollPane.setHvalue(Math.min(1, currentHvalue + 0.2));
        setBtnIndietro();
    }

    @FXML
    private void handlerTastoIndietro() {
        double currentHvalue = filtriScrollPane.getHvalue();
        filtriScrollPane.setHvalue(Math.max(0, currentHvalue - 0.2));
        setBtnIndietro();
    }

    public void setBtnIndietro() {
        double currentHvalue = filtriScrollPane.getHvalue();
        btnIndietro.setVisible(currentHvalue > 0.2);
    }

    public void levaComune(ActionEvent actionEvent) {
        comuniLbl.setText("Località : " + "Qualsiasi");
    }

    public void setTextIniziali(TipoVendita tv, BigDecimal PrezzoMin, BigDecimal PrezzoMax, Integer numStanze, ClasseEnergetica ce) {
        String tipoVendita = (tv != null) ? tv.toString() : "Stato Vendita";
        String pMin = (PrezzoMin != null) ? PrezzoMin.toString() : "Inserire il prezzo minimo";
        String pMax = (PrezzoMax != null) ? PrezzoMax.toString() : "Inserire il prezzo massimo";
        String nLocali = (numStanze != null) ? numStanze.toString() : "Inserisci numero locali";
        String classeEnergetica = (ce != null) ? ce.toString() : "Classe energetica";

        statoMenu.setText(tipoVendita);
        classeMenu.setText(classeEnergetica);
        prezzoMinText.setText(formattaStringa(pMin));
        prezzoMaxText.setText(formattaStringa(pMax));
        textNumeroLocali.setText(nLocali);
    }

    private void checkTipoVendita(TipoVendita tipoVendita) {
        String value = String.valueOf(tipoVendita);
        if (value.equals("null")) statoMenu.setText("Stato Vendita");
        else statoMenu.setText(String.valueOf(tipoVendita));
    }

    private void checkPrezzoMinMax(BigDecimal prezzoMin, BigDecimal prezzoMax) {
        if (prezzoMin == null) prezzoMinText.setPromptText("Inserisci prezzo minimo");
        else prezzoMinText.setText(String.valueOf(prezzoMin));

        if (prezzoMax == null) prezzoMaxText.setPromptText("Inserisci prezzo massimo");
        else prezzoMaxText.setText(String.valueOf(prezzoMax));
    }

    private void checkClasseEnergetica(ClasseEnergetica classeEnergetica) {
        String value = String.valueOf(classeEnergetica);
        if (value.equals("null")) classeMenu.setText("Classe energetica");
        else classeMenu.setText(String.valueOf(classeEnergetica));
    }

    private void checkNumStanze(Integer numStanze) {
        if (numStanze == null) textNumeroLocali.setPromptText("Inserisci numero locali");
        else textNumeroLocali.setText(String.valueOf(numStanze));
    }

    private void addListenerTipologia(ArrayList<TipoVendita> options) {
        for (TipoVendita option : options) {
            MenuItem menuItem = new MenuItem(option.toString());
            menuItem.setOnAction(event -> statoMenu.setText(option.toString()));
            statoMenu.getItems().add(menuItem);
        }
    }

    private void addListenerClasseEn(ArrayList<ClasseEnergetica> options) {
        for (ClasseEnergetica option : options) {
            MenuItem menuItem = new MenuItem(option.toString());
            menuItem.setOnAction(event -> classeMenu.setText(option.toString()));
            classeMenu.getItems().add(menuItem);
        }
    }

    private void aggiungiOpzioniArrayClasse(ArrayList<ClasseEnergetica> options) {
        classeMenu.getItems().clear();
        CreaAnnuncioController.setOptions(options);
    }

    private void aggiungiOpzioniArrayTipi(ArrayList<TipoVendita> options) {
        statoMenu.getItems().clear();
        options.add(TipoVendita.VENDITA);
        options.add(TipoVendita.AFFITTO);
    }

    // =========================================================================
    // NAVIGAZIONE (Dashboard e Home)
    // =========================================================================
    public void tornaHome() {
        linkHome.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1) {
                try {
                    HomePageController.setUtente(utenteCheCerca);
                    HomePage.initializeHomePage((Stage) linkHome.getScene().getWindow(), utenteCheCerca);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void apriDashboard() {
        switch (utenteCheCerca) {
            case Cliente cliente -> apriDashboardCliente();
            case Agente agente -> apriDashboardAgente();
            case GestoreAgenziaImmobiliare gestoreAgenziaImmobiliare -> apriDashboardGestore();
            default -> AlertFactory.creaAlertErroreInterno();
        }
    }

    private void apriDashboardGestore() {
        try { DashboardAmministratore.initializePageDashboardAmministratore((Stage) btnUserDashboard.getScene().getWindow()); } 
        catch (IOException e) { AlertFactory.creaAlertErroreInterno(); }
    }

    private void apriDashboardCliente() {
        try { DashBoardCliente.initializePageDashboardCliente(btnUserDashboard.getScene().getWindow(), utenteCheCerca); } 
        catch (InterruptedException e) { Thread.currentThread().interrupt(); AlertFactory.creaAlertErroreCaricamentoPagina(); } 
        catch (IOException e) { AlertFactory.creaAlertErroreInterno(); }
    }

    private void apriDashboardAgente() {
        try {
            Agente agenteDashboard = new Agente();
            agenteDashboard.setAccountAgente(new Account(utenteCheCerca.getAccountAgente().getEmail(), utenteCheCerca.getAccountAgente().getPassword()));
            agenteDashboard.setToken(utenteCheCerca.getToken());
            DashboardAgenteController.setAgente(agenteDashboard);
            DashboardAgente.initializePageDashboardAgente(btnUserDashboard.getScene().getWindow());
        } catch (IOException e) {
            e.printStackTrace(); AlertFactory.creaAlertErroreCaricamentoPagina();
        } catch (InterruptedException e) {
            e.printStackTrace(); Thread.currentThread().interrupt(); AlertFactory.creaAlertErroreCaricamentoPagina();
        }
    }
}