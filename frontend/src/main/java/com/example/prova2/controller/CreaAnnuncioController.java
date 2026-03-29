package com.example.prova2.controller;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.function.UnaryOperator;
import java.util.logging.Logger;

import com.example.prova2.controller.dashBoard.DashboardAgenteController;
import com.example.prova2.dto.AddImmobileRequestDTO;
import com.example.prova2.facade.CreaAnnuncioFacade;
import com.example.prova2.factory.AlertFactory;
import com.example.prova2.factory.AnnunciFactory;
import com.example.prova2.model.Arredamento;
import com.example.prova2.model.ClasseEnergetica;
import com.example.prova2.model.TipoImmobile;
import com.example.prova2.model.TipoVendita;
import com.example.prova2.view.CreaAnnuncioAgente;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import netscape.javascript.JSObject;

public class CreaAnnuncioController {

    // =========================================================================
    // COSTANTI E LOGGER
    // =========================================================================
    private static final Logger logger = Logger.getLogger(CreaAnnuncioController.class.getName());
    private static final String ERROR_MESSAGE = "Errore";
    private static final String ERROR_MESSAGE2 = "Non hai completato i passaggi rivedi";
    private static final String ERROR_MESSAGE_SCHERMATA = "Errore di navigazione";
    private static final String ERROR_MESSAGE_CARICAMENTOSCHERMATA = "Si è verificato un problema durante il caricamento della dashboard.";
    private static final String LOGGER_MESSAGE = "Nessun tipo selezionato";
    private static final String PATTERN_NUMEROCIVICO = "^(\\d+|N\\/A)$";

    private static final Random random = new Random();

    // =========================================================================
    // COMPONENTI UI (FXML) - PANNELLI E WIDGET COMPLESSI
    // =========================================================================
    @FXML private Pane paneCategoria;
    @FXML private Pane paneLocalita;
    @FXML private Pane paneCaratteristiche;
    @FXML private Pane panePrezzi;
    @FXML private Pane paneFoto;
    @FXML private Pane paneDescrizione;
    
    @FXML private WebView webViewMap;
    @FXML private ScrollPane scrollImage;

    // =========================================================================
    // COMPONENTI UI (FXML) - BOTTONI
    // =========================================================================
    @FXML private Button btnAvantiCategoria;
    @FXML private Button btnAvantiLocalita;
    @FXML private Button btnAvantiCarat;
    @FXML private Button btnAvantiPrezzi;
    @FXML private Button btnAvantiFoto;
    @FXML private Button btnIndietro;
    @FXML private Button btnFoto;
    @FXML private Button btnCarica;
    
    @FXML private Button btnMenoBagni;
    @FXML private Button btnPiuBagni;
    @FXML private Button btnMenoSoggiorno;
    @FXML private Button btnPiuSoggiorno;
    @FXML private Button btnMenoCucine;
    @FXML private Button btnPiuCucine;

    // =========================================================================
    // COMPONENTI UI (FXML) - CAMPI DI TESTO E MENU
    // =========================================================================
    @FXML private TextField comuneTextField;
    @FXML private TextField indirizzoTextField;
    @FXML private TextField numeroCivicoTextField;
    @FXML private TextField superficieTextField;
    @FXML private TextField pianiTextField;
    @FXML private TextField numeroStanzeTextField;
    @FXML private TextField prezziTextField;
    @FXML private TextField speseCondominioTextField;
    @FXML private TextField titoloTextField;
    @FXML private TextField descrizioneTextField;
    
    @FXML private MenuButton menuClasseEnergetica;
    @FXML private MenuButton menuAscensore;

    // =========================================================================
    // COMPONENTI UI (FXML) - RADIO BUTTONS
    // =========================================================================
    @FXML private RadioButton rbApp, rbLoft, rbRes, rbVilla, rbBaita, rbMans, rbAtt, rbOpn;
    @FXML private RadioButton rbArr, rbParzArr, rbNonArr;
    @FXML private RadioButton rbVendi, rbAffitti;

    // =========================================================================
    // COMPONENTI UI (FXML) - ETICHETTE DI ERRORE E INFO
    // =========================================================================
    @FXML private Label erroreComune, erroreIndirizzo, erroreCivico;
    @FXML private Label errorTipologia;
    @FXML private Label errorSuperficie, errorPiano, errorStanze, errorArredamento, errorClasse, errorAscensore;
    @FXML private Label prezzoErrore, speseErrore, venditaErrore;
    @FXML private Label errorFoto, errorTitolo, errorDes;
    @FXML private Label bagniLbl, soggiornoLbl, cucineLbl;

    @FXML private ImageView imgSpese, imgPrezzo, imgStanze, imgSuperficie, imgPiano;

    // =========================================================================
    // VARIABILI DI STATO E DATI
    // =========================================================================
    public Stage stagePrecedente;
    public Stage stageOra;
    
    private DashboardAgenteController controllerPrec;
    private String agenteCf = null;
    
    private final AddImmobileRequestDTO annuncioDTO = new AddImmobileRequestDTO();
    private final ToggleGroup group = new ToggleGroup();
    private final ToggleGroup group2 = new ToggleGroup();
    private final ToggleGroup group3 = new ToggleGroup();
    private final HBox imageContainer = new HBox(15);
    
    private ObservableList<String> selectedFiles = FXCollections.observableArrayList();
    private ObservableList<File> absPath = FXCollections.observableArrayList();
    
    private int numeroBagni = 0;
    private int numeroSoggiorno = 0;
    private int numeroCucine = 0;

    // =========================================================================
    // SETTER E GETTER
    // =========================================================================
    public void setControllerPrec(DashboardAgenteController controllerPrec) { this.controllerPrec = controllerPrec; }
    public void setCf(String cf) { agenteCf = cf; }
    public void setAgenteCfForAnnuncio() { annuncioDTO.setCfAgente(agenteCf); }
    private void setRandomNumber() {
        int numeroCasuale = 100 + random.nextInt(900);
        annuncioDTO.setIdImmobile(numeroCasuale);
    }

    // =========================================================================
    // INIZIALIZZAZIONE (Setup UI, Binding e Listener)
    // =========================================================================
    public void initialize() {
        btnAvantiCategoria.setDefaultButton(true);
        btnAvantiFoto.setDefaultButton(true);
        btnCarica.setDefaultButton(true);

        CompletableFuture.runAsync(() -> {
            Platform.runLater(() -> {
                WebEngine webEngine = webViewMap.getEngine();
                comunicationJavaJavascript(webEngine);
                CreaAnnuncioFacade.initMappa(webEngine);
            });
        });

        CompletableFuture.runAsync(() -> {
            ArrayList<ClasseEnergetica> options2 = new ArrayList<>();
            Platform.runLater(() -> {
                setPaneCategoriaVisible();
                setRadioButtonCategoria();
                setRadioButtonCaratteristiche();
                setRadioButtonPrezzi();
                aggiungiOpzioniArrayClasse(options2);
                addListenerClasseEn(options2);
                addListenerAscensore("Si", "No");
                initOnClick();
                initPage();
            });
        });
    }

    private void initPage() {
        initRadioCategoria();
        initButtonAvantiCategoria();
        initBottoneAvantiLocalita();
        initBottoneCaratteristiche();
        initBottoneAvantiPrezzi();
        initListenerForPosizione();
        initBottoneFoto();
        initBottoneCarica();
        aggiungiListenerSoloNumeriEPunti(prezziTextField);
        aggiungiListenerSoloNumeriEPunti(speseCondominioTextField);
        addListenerNumeri(superficieTextField);
        aggiungiListenerSoloNumeriEMeno(pianiTextField);
        addListenerNumeri(numeroStanzeTextField);
        aggiungiListenerMaiuscole(comuneTextField);
        aggiungiListenerMaiuscole(indirizzoTextField);
    }

    private void initOnClick() {
        onClickBagni();
        onClickSoggiorno();
        onClickCucine();
    }

    private void setRadioButtonCategoria() {
        rbApp.setToggleGroup(group); rbApp.setUserData(TipoImmobile.APPARTAMENTO);
        rbLoft.setToggleGroup(group); rbLoft.setUserData(TipoImmobile.LOFT);
        rbRes.setToggleGroup(group); rbRes.setUserData(TipoImmobile.RESIDENZIALE);
        rbVilla.setToggleGroup(group); rbVilla.setUserData(TipoImmobile.VILLA);
        rbBaita.setToggleGroup(group); rbBaita.setUserData(TipoImmobile.BAITA);
        rbMans.setToggleGroup(group); rbMans.setUserData(TipoImmobile.MANSARDA);
        rbAtt.setToggleGroup(group); rbAtt.setUserData(TipoImmobile.ATTICO);
        rbOpn.setToggleGroup(group); rbOpn.setUserData(TipoImmobile.OPENSPACE);
        
        group.selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
            if (newToggle != null) {
                TipoImmobile selectedCategoria = (TipoImmobile) newToggle.getUserData();
                logger.info("Categoria selezionata: " + selectedCategoria);
            }
        });
    }

    private void setRadioButtonCaratteristiche() {
        rbArr.setToggleGroup(group2); rbArr.setUserData(Arredamento.ARREDATO);
        rbNonArr.setToggleGroup(group2); rbNonArr.setUserData(Arredamento.NONARREDATO);
        rbParzArr.setToggleGroup(group2); rbParzArr.setUserData(Arredamento.PARZIALMENTEARREDATO);
    }

    private void setRadioButtonPrezzi() {
        rbVendi.setToggleGroup(group3); rbVendi.setUserData(TipoVendita.VENDITA);
        rbAffitti.setToggleGroup(group3); rbAffitti.setUserData(TipoVendita.AFFITTO);
    }

    private void initRadioCategoria() {
        rbBaita.setOnAction(event -> errorTipologia.setVisible(false));
        rbLoft.setOnAction(event -> errorTipologia.setVisible(false));
        rbMans.setOnAction(event -> errorTipologia.setVisible(false));
        rbApp.setOnAction(event -> errorTipologia.setVisible(false));
        rbAtt.setOnAction(event -> errorTipologia.setVisible(false));
        rbOpn.setOnAction(event -> errorTipologia.setVisible(false));
        rbRes.setOnAction(event -> errorTipologia.setVisible(false));
        rbVilla.setOnAction(event -> errorTipologia.setVisible(false));
    }

    private void initButtonAvantiCategoria() {
        btnAvantiCategoria.disableProperty().bind(
                rbAffitti.selectedProperty().
                        or(rbApp.selectedProperty()
                                .or(rbBaita.selectedProperty())
                                .or(rbMans.selectedProperty())
                                .or(rbRes.selectedProperty())
                                .or(rbAtt.selectedProperty())
                                .or(rbOpn.selectedProperty())
                                .or(rbLoft.selectedProperty())
                                .or(rbVilla.selectedProperty())
                                .and((rbVendi.selectedProperty()).or(rbAffitti.selectedProperty()))).not());
    }

    private void initBottoneAvantiLocalita() {
        btnAvantiLocalita.disableProperty().bind(
                Bindings.createBooleanBinding(
                        () -> {
                            boolean isComuneValid = comuneTextField.getText().length() >= 4 || comuneTextField.getText().trim().equals("N/A");
                            boolean isIndirizzoValid = indirizzoTextField.getText().length() >= 4;
                            boolean isCivicoValid = numeroCivicoTextField.getText().matches(PATTERN_NUMEROCIVICO);
                            return !(isComuneValid && isIndirizzoValid && isCivicoValid);
                        },
                        comuneTextField.textProperty(), indirizzoTextField.textProperty(), numeroCivicoTextField.textProperty()
                )
        );
    }

    private void initBottoneCaratteristiche() {
        btnAvantiCarat.disableProperty().bind(
                Bindings.createBooleanBinding(
                        () -> !(superficieValida() && numeroStanzeValido() && numeroPianoValido() && classeValida() && !arredamentoValido() && ascensoreValida()),
                        superficieTextField.textProperty(), numeroStanzeTextField.textProperty(), pianiTextField.textProperty(),
                        menuClasseEnergetica.textProperty(), rbArr.selectedProperty(), rbNonArr.selectedProperty(),
                        rbParzArr.selectedProperty(), menuAscensore.textProperty()
                )
        );
    }

    private void initBottoneAvantiPrezzi() {
        btnAvantiPrezzi.disableProperty().bind(
                Bindings.createBooleanBinding(
                        () -> !(isPrezzoValido() && isSpeseCondominialiValide()),
                        prezziTextField.textProperty(), speseCondominioTextField.textProperty(),
                        rbVendi.selectedProperty(), rbAffitti.selectedProperty()
                )
        );
    }

    private void initBottoneFoto() {
        btnAvantiFoto.setVisible(true);
        btnAvantiFoto.disableProperty().bind(
                Bindings.createBooleanBinding(() -> selectedFiles.isEmpty(), selectedFiles)
        );
    }

    private void initBottoneCarica() {
        btnCarica.disableProperty().bind(
                Bindings.createBooleanBinding(
                        () -> (descrizioneTextField.getText().isEmpty() || titoloTextField.getText().isEmpty()),
                        descrizioneTextField.textProperty(), titoloTextField.textProperty()
                )
        );
    }

    private void initListenerForPosizione() {
        comuneTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty()) rimuoviMessaggioComune();
        });
        indirizzoTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty()) rimuoviMessaggioIndirizzo();
        });
        numeroCivicoTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty()) rimuoviMessaggioCivico();
        });
    }

    private void comunicationJavaJavascript(WebEngine webEngine) {
        webEngine.setUserDataDirectory(new File("user-data"));
        webEngine.getLoadWorker().stateProperty().addListener((obs, oldState, newState) -> {
            if (newState == Worker.State.SUCCEEDED) {
                JSObject window = (JSObject) webEngine.executeScript("window");
                window.setMember("java", this); 
            }
        });
    }

    public void addListenerClasseEn(ArrayList<ClasseEnergetica> classiEnergetiche) {
        for (ClasseEnergetica classeEnergetica : classiEnergetiche) {
            MenuItem menuItem = new MenuItem(classeEnergetica.toString());
            menuItem.setOnAction(event -> {
                menuClasseEnergetica.setText(classeEnergetica.toString());
                rimuoviMessaggioClasseEnergetica();
            });
            menuClasseEnergetica.getItems().add(menuItem);
        }
    }

    public void addListenerAscensore(String si, String no) {
        MenuItem menuItem1 = new MenuItem(si);
        MenuItem menuItem2 = new MenuItem(no);
        menuItem1.setOnAction(event -> { menuAscensore.setText(si); rimuoviMessaggioAscensore(); });
        menuItem2.setOnAction(event -> { menuAscensore.setText(no); rimuoviMessaggioAscensore(); });
        menuAscensore.getItems().addAll(menuItem1, menuItem2);
    }

    public void aggiungiOpzioniArrayClasse(ArrayList<ClasseEnergetica> classiEnergetiche) { addClasseMenu(classiEnergetiche); }
    public static void addClasseMenu(ArrayList<ClasseEnergetica> classiEnergetiche) { setOptions(classiEnergetiche); }

    public static void setOptions(ArrayList<ClasseEnergetica> classiEnergetiche) {
        classiEnergetiche.add(ClasseEnergetica.A_PLUS_PLUS);
        classiEnergetiche.add(ClasseEnergetica.A_PLUS);
        classiEnergetiche.add(ClasseEnergetica.A);
        classiEnergetiche.add(ClasseEnergetica.B);
        classiEnergetiche.add(ClasseEnergetica.C);
        classiEnergetiche.add(ClasseEnergetica.D);
        classiEnergetiche.add(ClasseEnergetica.E);
        classiEnergetiche.add(ClasseEnergetica.F);
        classiEnergetiche.add(ClasseEnergetica.G);
    }

    // =========================================================================
    // NAVIGAZIONE TRA PANNELLI
    // =========================================================================
    private void setPaneCategoriaVisible() {
        paneCaratteristiche.setVisible(false); paneLocalita.setVisible(false);
        paneDescrizione.setVisible(false); paneFoto.setVisible(false);
        panePrezzi.setVisible(false); paneCategoria.setVisible(true);
    }

    public void attivaCategoria() {
        paneCategoria.setVisible(true); paneCaratteristiche.setVisible(false);
        paneLocalita.setVisible(false); paneDescrizione.setVisible(false);
        paneFoto.setVisible(false); panePrezzi.setVisible(false);
    }

    public void attivaLocalita() {
        if (checkCategoria()) {
            paneCategoria.setVisible(false); paneLocalita.setVisible(true);
            paneCaratteristiche.setVisible(false); paneDescrizione.setVisible(false);
            paneFoto.setVisible(false); panePrezzi.setVisible(false);
        } else {
            AlertFactory.creaAlertErrore("Attenzione", "Mancano dei dati nelle schermate precedenti, controlla!!");
        }
    }

    public void attivaCaratteristiche() {
        if (checkCategoria() && checkLocalita()) {
            paneLocalita.setVisible(false); paneCategoria.setVisible(false);
            paneCaratteristiche.setVisible(true); paneDescrizione.setVisible(false);
            paneFoto.setVisible(false); panePrezzi.setVisible(false);
        } else {
            AlertFactory.creaAlertErrore("Attenzione", "Mancano dei dati nelle schermate precedenti, controlla!!");
        }
    }

    public void attivaPrezzi() {
        if (checkCaratteristiche()) {
            paneLocalita.setVisible(false); paneCaratteristiche.setVisible(false);
            paneCategoria.setVisible(false); paneDescrizione.setVisible(false);
            paneFoto.setVisible(false); panePrezzi.setVisible(true);
        } else {
            AlertFactory.creaAlertErrore(ERROR_MESSAGE, ERROR_MESSAGE2);
        }
    }

    public void attivaDocumenti() {
        if (checkPrezzi()) {
            paneLocalita.setVisible(false); paneCaratteristiche.setVisible(false);
            paneCategoria.setVisible(false); paneDescrizione.setVisible(false);
            panePrezzi.setVisible(false); paneFoto.setVisible(true);
        } else {
            AlertFactory.creaAlertErrore(ERROR_MESSAGE, ERROR_MESSAGE2);
        }
    }

    public void attivaDescrizione() {
        if (checkNumeroFoto()) {
            paneLocalita.setVisible(false); paneCaratteristiche.setVisible(false);
            paneCategoria.setVisible(false); panePrezzi.setVisible(false);
            paneFoto.setVisible(false); paneDescrizione.setVisible(true);
        } else {
            AlertFactory.creaAlertErrore(ERROR_MESSAGE, ERROR_MESSAGE2);
        }
    }

    // =========================================================================
    // EVENTI INTERFACCIA (Bottoni e Callback)
    // =========================================================================
    @FXML
    public void caricaImmagine() {
        Window currentWindow = btnFoto.getScene().getWindow();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleziona uno o più file");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Immagini", "*.png", "*.jpg", "*.jpeg"),
                new FileChooser.ExtensionFilter("Tutti i file", "*.*")
        );
        List<File> files = fileChooser.showOpenMultipleDialog(currentWindow);
        
        if (files != null && !files.isEmpty()) {
            if (scrollImage.getContent() == null) {
                imageContainer.setPadding(new Insets(10));
                scrollImage.setContent(imageContainer);
            }
            for (File fileUtente : files) {
                selectedFiles.add("images/" + fileUtente.getName());
                absPath.add(new File(fileUtente.getAbsolutePath()));
                Pane pane = AnnunciFactory.createImageViewForFoto(fileUtente, selectedFiles, absPath);
                imageContainer.getChildren().add(pane);
            }
            if (errorFoto != null) errorFoto.setVisible(false);
            annuncioDTO.setUriFotoImmobile(selectedFiles);
        }
    }

    @FXML
    public void onSearchAddress() {
        String address = comuneTextField.getText().trim() + "," + indirizzoTextField.getText().trim() + "," + numeroCivicoTextField.getText().trim();
        String coordinates = CreaAnnuncioFacade.getCoordinateFromAddress(address);
        if (coordinates != null) {
            String[] latLng = coordinates.split(",");
            CreaAnnuncioFacade.updateMarker(Double.parseDouble(latLng[0]), Double.parseDouble(latLng[1]));
        } else {
            AlertFactory.creaAlertErrore(ERROR_MESSAGE, "Impossibile trovare le coordinate per l'indirizzo fornito.");
        }
    }

    public void updateAddress(String address) { divideAddress(address); } // Callback Javascript

    private void onClickBagni() {
        btnPiuBagni.setOnAction(event -> { numeroBagni++; updateBagniLabel(); });
        btnMenoBagni.setOnAction(event -> { if (numeroBagni > 0) { numeroBagni--; updateBagniLabel(); } });
        updateBagniLabel();
    }

    private void onClickSoggiorno() {
        btnPiuSoggiorno.setOnAction(event -> { numeroSoggiorno++; updateSoggiornoLabel(); });
        btnMenoSoggiorno.setOnAction(event -> { if (numeroSoggiorno > 0) { numeroSoggiorno--; updateSoggiornoLabel(); } });
        updateSoggiornoLabel();
    }

    private void onClickCucine() {
        btnPiuCucine.setOnAction(event -> { numeroCucine++; updateCucineLabel(); });
        btnMenoCucine.setOnAction(event -> { if (numeroCucine > 0) { numeroCucine--; updateCucineLabel(); } });
        updateCucineLabel();
    }

    public void tornaIndietro() throws IOException, InterruptedException {
        Alert alert = AlertFactory.creaAlertUscitaCreazioneAnnuncio();
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            stagePrecedente.show();
            stageOra.close();
        }
    }

    public void closeWindows() { ((Stage) btnIndietro.getScene().getWindow()).close(); }

    // =========================================================================
    // LOGICA DI VALIDAZIONE
    // =========================================================================
    private boolean checkCategoria() { return checkTipo() && checkVendita(); }
    private boolean checkLocalita() { return checkComune() && checkIndirizzo() && checkNumeroCivico(); }
    private boolean checkPrezzi() { return checkPrezzo() && checkSpeseCondominiali(); }
    private boolean checkCaratteristiche() { return checkSupeficie() && checkPiano() && checkNumeroStanze() && checkArredamento() && checkClasseEnergetica() && checkAscensore(); }
    private boolean checkDescrizione() { return checkDesInseritaUtente() && checkTitolo(); }

    private boolean checkTipo() {
        List<RadioButton> tipi = Arrays.asList(rbBaita, rbLoft, rbMans, rbApp, rbAtt, rbOpn, rbRes, rbVilla);
        boolean tipoSelezionato = tipi.stream().anyMatch(RadioButton::isSelected);
        errorTipologia.setVisible(!tipoSelezionato);
        return tipoSelezionato;
    }

    private boolean checkVendita() {
        if (isVenditaValida()) { venditaErrore.setVisible(false); return true; }
        venditaErrore.setVisible(true); return false;
    }

    private boolean isVenditaValida() { return rbVendi.isSelected() || rbAffitti.isSelected(); }

    private boolean checkSpeseCondominiali() {
        try {
            String input = speseCondominioTextField.getText().replace(".", "").trim();
            if (input.isEmpty()) { speseErrore.setText("Il campo non può essere vuoto."); speseErrore.setVisible(true); return false; }
            BigDecimal prezzo = new BigDecimal(input);
            BigDecimal maxPrezzo = BigDecimal.valueOf(100000);
            if (prezzo.compareTo(BigDecimal.ZERO) <= 0) { speseErrore.setText("Il prezzo deve essere maggiore di zero."); speseErrore.setVisible(true); return false; }
            if (prezzo.compareTo(maxPrezzo) > 0) { speseErrore.setText("Il prezzo non può superare questa soglia."); speseErrore.setVisible(true); return false; }
            speseErrore.setVisible(false); return true;
        } catch (NumberFormatException e) {
            speseErrore.setText("Prezzo non valido."); speseErrore.setVisible(true); return false;
        }
    }

    private boolean isSpeseCondominialiValide() {
        try {
            String input = speseCondominioTextField.getText().replace(".", "").trim();
            if (input.isEmpty()) return false;
            BigDecimal prezzo = new BigDecimal(input);
            return (prezzo.compareTo(BigDecimal.ZERO) > 0 && prezzo.compareTo(BigDecimal.valueOf(100000)) <= 0);
        } catch (NumberFormatException e) { return false; }
    }

    private boolean checkPrezzo() {
        try {
            String input = prezziTextField.getText().replace(".", "").trim();
            if (input.isEmpty()) { prezzoErrore.setText("Il campo non può essere vuoto."); prezzoErrore.setVisible(true); return false; }
            BigDecimal prezzo = new BigDecimal(input);
            BigDecimal maxPrezzo = BigDecimal.valueOf(1000000000);
            if (prezzo.compareTo(BigDecimal.ZERO) <= 0) { prezzoErrore.setText("Il prezzo deve essere maggiore di zero."); prezzoErrore.setVisible(true); return false; }
            if (prezzo.compareTo(maxPrezzo) > 0) { prezzoErrore.setText("Il prezzo non può superare questa soglia."); prezzoErrore.setVisible(true); return false; }
            prezzoErrore.setVisible(false); return true;
        } catch (NumberFormatException e) {
            prezzoErrore.setText("Prezzo non valido."); prezzoErrore.setVisible(true); return false;
        }
    }

    private boolean isPrezzoValido() {
        try {
            String input = prezziTextField.getText().replace(".", "").trim();
            if (input.isEmpty()) return false;
            BigDecimal prezzo = new BigDecimal(input);
            return (prezzo.compareTo(BigDecimal.ZERO) > 0 && prezzo.compareTo(BigDecimal.valueOf(1000000000)) <= 0);
        } catch (NumberFormatException e) { return false; }
    }

    private boolean checkNumeroCivico() {
        if (numeroCivicoTextField.getText().matches(PATTERN_NUMEROCIVICO)) { erroreCivico.setVisible(false); return true; }
        erroreCivico.setVisible(true); return false;
    }

    private boolean checkIndirizzo() {
        if (indirizzoTextField.getText().trim().length() >= 4) { erroreIndirizzo.setVisible(false); return true; }
        erroreIndirizzo.setVisible(true); return false;
    }

    private boolean checkComune() {
        if (comuneTextField.getText().trim().length() >= 4) { erroreComune.setVisible(false); return true; }
        erroreComune.setVisible(true); return false;
    }

    private boolean checkClasseEnergetica() {
        if (menuClasseEnergetica.getText().equals("Seleziona la classe energetica")) { errorClasse.setVisible(true); return false; }
        errorClasse.setVisible(false); return true;
    }

    private boolean checkAscensore() {
        if (menuAscensore.getText().equals("Seleziona la presenza dell'ascensore")) { errorAscensore.setVisible(true); return false; }
        errorAscensore.setVisible(false); return true;
    }

    private boolean classeValida() { return !menuClasseEnergetica.getText().trim().equals("Seleziona la classe energetica"); }
    private boolean ascensoreValida() { return !menuAscensore.getText().trim().equals("Seleziona la presenza dell'ascensore"); }
    private boolean arredamentoValido() { return !rbArr.isSelected() && !rbNonArr.isSelected() && !rbParzArr.isSelected(); }

    private boolean checkArredamento() {
        if (arredamentoValido()) { errorArredamento.setVisible(true); return false; }
        errorArredamento.setVisible(false); return true;
    }

    private boolean checkPiano() {
        try {
            int piano = Integer.parseInt(pianiTextField.getText());
            if (piano > 150) { errorPiano.setText("Il numero di piani inserito è troppo grande. Inserisci un valore inferiore a 150."); errorPiano.setVisible(true); return false; }
            if (pianiTextField.getText().isEmpty()) { errorPiano.setText("Inserisci un numero di piani."); errorPiano.setVisible(true); return false; }
            errorPiano.setVisible(false); return true;
        } catch (NumberFormatException e) { errorPiano.setVisible(true); return false; }
    }

    private boolean checkSupeficie() {
        try {
            int superficie = Integer.parseInt(superficieTextField.getText().trim());
            if (superficie > 0 && superficie <= 100000) { errorSuperficie.setVisible(false); return true; }
            else if (superficieTextField.getText().isEmpty()) { errorSuperficie.setVisible(true); errorSuperficie.setText("Inserisci un valore numerico valido per la superficie."); return false; }
            else if (superficie == 0) { errorSuperficie.setText("La superficie non può essere 0."); errorSuperficie.setVisible(true); return false; }
            else { errorSuperficie.setText("La superficie inserita è troppo grande.Inserire una superficie con valore minore."); errorSuperficie.setVisible(true); return false; }
        } catch (NumberFormatException e) { errorSuperficie.setVisible(true); return false; }
    }

    private boolean checkNumeroStanze() {
        try {
            int numeroStanze = Integer.parseInt(numeroStanzeTextField.getText());
            if (numeroStanze > 50) { errorStanze.setText("Il numero di stanze inserite è troppo grande.Inserire un numero minore."); errorStanze.setVisible(true); return false; }
            if (numeroStanzeTextField.getText().isEmpty()) { errorStanze.setText("Inserire un numero di stanze."); errorStanze.setVisible(true); return false; }
            errorStanze.setVisible(false); return true;
        } catch (NumberFormatException e) { errorStanze.setVisible(true); return false; }
    }

    private boolean checkNumeroFoto() {
        if (selectedFiles.isEmpty()) { errorFoto.setVisible(true); return false; }
        errorFoto.setVisible(false); return true;
    }

    private boolean checkTitolo() {
        if (titoloTextField.getText().isEmpty()) { errorTitolo.setVisible(true); return false; }
        errorTitolo.setVisible(false); return true;
    }

    private boolean checkDesInseritaUtente() {
        if (descrizioneTextField.getText().isEmpty()) { errorDes.setVisible(true); return false; }
        errorDes.setVisible(false); return true;
    }

    private boolean numeroPianoValido() { try { Integer.parseInt(pianiTextField.getText()); return true; } catch (NumberFormatException e) { return false; } }
    private boolean superficieValida() { try { Integer.parseInt(superficieTextField.getText()); return true; } catch (NumberFormatException e) { return false; } }
    private boolean numeroStanzeValido() { try { Integer.parseInt(numeroStanzeTextField.getText()); return true; } catch (NumberFormatException e) { return false; } }

    // =========================================================================
    // METODI HELPER E PULIZIA UI
    // =========================================================================
    private void updateBagniLabel() { bagniLbl.setText(String.valueOf(numeroBagni)); }
    private void updateSoggiornoLabel() { soggiornoLbl.setText(String.valueOf(numeroSoggiorno)); }
    private void updateCucineLabel() { cucineLbl.setText(String.valueOf(numeroCucine)); }

    private void divideAddress(String address) {
        if (address == null || address.trim().isEmpty()) { logger.warning("L'indirizzo fornito è nullo o vuoto."); return; }
        try {
            TrasformaIndirizzo result = getTrasformaIndirizzo(address);
            indirizzoTextField.setText(result.street() + ", " + result.zipCode() + ", " + result.province() + ", " + result.country());
            numeroCivicoTextField.setText(result.number() != null ? result.number() : "N/A");
            comuneTextField.setText(result.city());
        } catch (Exception e) { logger.info("Errore durante il parsing dell'indirizzo: " + e.getMessage()); }
    }

    private static TrasformaIndirizzo getTrasformaIndirizzo(String address) {
        String[] parts = address.split(", ");
        String street = parts.length > 0 ? parts[0] : "N/A";
        String number = (parts.length > 1 && parts[1].matches("\\d+")) ? parts[1] : null;
        int cityIndex = (number != null) ? 2 : 1;
        int countryIndex = (number != null) ? 3 : 2;

        String[] cityDetails = parts.length > cityIndex ? parts[cityIndex].split(" ") : new String[]{"N/A", "N/A", "N/A"};
        String zipCode = cityDetails.length > 0 ? cityDetails[0] : "N/A";
        String city = cityDetails.length > 1 ? cityDetails[1] : "N/A";
        String province = cityDetails.length > 2 ? cityDetails[2] : "N/A";
        String country = parts.length > countryIndex ? parts[countryIndex] : "N/A";

        return new TrasformaIndirizzo(street, number, zipCode, city, province, country);
    }

    private record TrasformaIndirizzo(String street, String number, String zipCode, String city, String province, String country) {}

    private void aggiungiListenerSoloNumeriEPunti(TextField textField) { HomePageController.aggiungiListener(textField); }
    private void addListenerNumeri(TextField textField) { CreaGestoreController.aggiungi(textField); }
    private void aggiungiListenerSoloNumeriEMeno(TextField textField) {
        UnaryOperator<TextFormatter.Change> filter = change -> {
            String newText = change.getControlNewText();
            if (newText.matches("-?\\d*")) return change;
            return null;
        };
        textField.setTextFormatter(new TextFormatter<>(filter));
    }
    private void aggiungiListenerMaiuscole(TextField text) {
        text.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty()) text.setText(newValue.substring(0, 1).toUpperCase() + newValue.substring(1));
        });
    }

    public void rimuoviMessaggioAscensore() { errorAscensore.setVisible(false); }
    public void rimuoviMessaggioIndirizzo() { erroreIndirizzo.setVisible(false); }
    public void rimuoviMessaggioCivico() { erroreCivico.setVisible(false); }
    public void rimuoviMessaggioComune() { erroreComune.setVisible(false); }
    public void rimuoviMessaggioClasseEnergetica() { errorClasse.setVisible(false); }
    public void rimuoviMessaggioArredamenti() { errorArredamento.setVisible(false); }
    public void rimuoviMessaggioStanze() { errorStanze.setVisible(false); }
    public void rimuoviMessaggioPiano() { errorPiano.setVisible(false); }
    public void rimuoviMessaggioSuperficie() { errorSuperficie.setVisible(false); }
    public void rimuoviMessaggioVendita() { venditaErrore.setVisible(false); }
    public void rimuoviMessaggioSpesa() { speseErrore.setVisible(false); }
    public void rimuoviMessaggioPrezzo() { prezzoErrore.setVisible(false); }
    public void rimuoviMessaggioTitolo() { errorTitolo.setVisible(false); }
    public void rimuoviMessaggioDescrizione() { errorDes.setVisible(false); }

    // =========================================================================
    // IMPACCHETTAMENTO DATI E SALVATAGGIO (Submit Logic)
    // =========================================================================
    public void generaAnnuncio() {
        if (checkDescrizione()) {
            Alert alert = AlertFactory.creaAlertConfermaCaricamentoImmobile();
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                inviaDati();
            }
        }
    }

    private void inviaDati() {
        new Thread(() -> Platform.runLater(() -> inviaDatiAnnuncio(DashboardAgenteController.getAgente().getToken()))).start();
    }

    private void inviaDatiAnnuncio(String token) {
        if (aggiungiAnnuncio(token)) {
            mostraPopUpConferma();
        } else {
            AlertFactory.creaAlertErrore("Errore nella creazione dell'annuncio !", "Siamo spiacenti si è verificato un errore interno nel sistema si prega di riprovare!");
        }
    }

    private boolean aggiungiAnnuncio(String token) {
        setAgenteCfForAnnuncio();
        setRandomNumber();
        setTypeEstates();
        setIndirizzoAnnuncio();
        setCaratteristiche();
        setSpeseEPrezzi();
        setScritture();
        
        new Thread(() -> {
            for (File immagine : absPath) {
                CreaAnnuncioFacade.caricaImmagine(immagine.getAbsolutePath(), "images/" + immagine.getName());
            }
        }).start();
        
        return CreaAnnuncioFacade.addAnnuncio(annuncioDTO, token);
    }

    private void mostraPopUpConferma() {
        Alert alert = creaSuccessMsg();
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent()) {
            Stage currentStage = (Stage) btnCarica.getScene().getWindow();
            if (result.get().getText().equals("Torna alla dashboard")) {
                controllerPrec.initialize();
                currentStage.close();
            } else if (result.get().getText().equals("Continua a creare altri annunci")) {
                currentStage.close();
                navigaCreaAnnunci();
            }
        }
    }

    public static Alert creaSuccessMsg() {
        return AlertFactory.creaAlertSuccess("Torna alla dashboard", "Continua a creare altri annunci", "Operazione completata!", "Hai creato con successo il tuo annuncio!", "Complimenti l'operazione è andata a buon fine, vedrai il tuo annuncio nella sezione Annunci recenti sulla tua dashboard");
    }
    
    private void navigaCreaAnnunci() {
        try {
            CreaAnnuncioAgente.initializePageCreaAnnuncioFromCreateAnnunci(agenteCf);
        } catch (IOException e) {
            AlertFactory.creaAlertErrore(ERROR_MESSAGE_SCHERMATA, ERROR_MESSAGE_CARICAMENTOSCHERMATA);
        }
    }

    private void setScritture() {
        annuncioDTO.setTitolo(titoloTextField.getText());
        annuncioDTO.setDescrizione(descrizioneTextField.getText());
    }

    private void setSpeseEPrezzi() {
        BigDecimal prezzo = new BigDecimal(prezziTextField.getText().replace(".", "").trim());
        BigDecimal spese = new BigDecimal(speseCondominioTextField.getText().replace(".", "").trim());

        RadioButton selectedTypeSell = (RadioButton) group3.getSelectedToggle();
        if (selectedTypeSell != null) {
            annuncioDTO.setTipovendita((TipoVendita) selectedTypeSell.getUserData());
        } else {
            logger.warning(LOGGER_MESSAGE);
        }
        annuncioDTO.setPrezzo(prezzo);
        annuncioDTO.setSpeseCondominiali(spese);
    }

    private void setCaratteristiche() {
        int superfice = Integer.parseInt(superficieTextField.getText());
        int piani = Integer.parseInt(pianiTextField.getText());
        int numStanze = Integer.parseInt(numeroStanzeTextField.getText());
        
        String ascensore = menuAscensore.getText();
        switch (ascensore) {
            case "Si": annuncioDTO.setAscensore(true); break;
            case "No":
            default: annuncioDTO.setAscensore(false); break;
        }

        RadioButton selectedTypeArr = (RadioButton) group2.getSelectedToggle();
        if (selectedTypeArr != null) {
            annuncioDTO.setArredamento((Arredamento) selectedTypeArr.getUserData());
        } else {
            logger.warning(LOGGER_MESSAGE);
        }

        annuncioDTO.setSuperficie(superfice);
        annuncioDTO.setPiano(piani);
        annuncioDTO.setNumeroStanze(numStanze);
        annuncioDTO.setNumeroBagni(numeroBagni);
        annuncioDTO.setNumeroSoggiorni(numeroSoggiorno);
        annuncioDTO.setNumeroCucine(numeroCucine);
        annuncioDTO.setClasseEnergetica(ClasseEnergetica.fromString(menuClasseEnergetica.getText()));
    }

    private void setTypeEstates() {
        RadioButton selectedTypeEstates = (RadioButton) group.getSelectedToggle();
        if (selectedTypeEstates != null) {
            annuncioDTO.setTipoimmobile((TipoImmobile) selectedTypeEstates.getUserData());
        } else {
            logger.warning(LOGGER_MESSAGE);
        }
    }

    private void setIndirizzoAnnuncio() {
        annuncioDTO.setComune(comuneTextField.getText());
        annuncioDTO.setVia(indirizzoTextField.getText());
        annuncioDTO.setNumeroCivico(numeroCivicoTextField.getText());
    }
}