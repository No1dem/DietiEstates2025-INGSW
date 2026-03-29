package com.example.prova2.controller;

import com.example.prova2.controller.dashBoard.DashboardAgenteController;
import com.example.prova2.dto.GetAllRicercheResponse;
import com.example.prova2.facade.ClienteServiceFacade;
import com.example.prova2.factory.AlertFactory;
import com.example.prova2.factory.FactoryRicerchePassate;
import com.example.prova2.model.*;
import com.example.prova2.service.GeopifyService;
import com.example.prova2.view.*;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HomePageController {

    // =========================================================================
    // COMPONENTI UI (FXML) - PANNELLI E CONTENITORI
    // =========================================================================
    @FXML private ScrollPane principalScrollPane;
    @FXML private ScrollPane scrollPaneHome;
    @FXML private ScrollPane scrollPaneHome2;
    @FXML private Pane principalPane;
    @FXML private Pane annunciPane;
    @FXML private Pane ricerchePane;
    @FXML private Pane newsPane;
    @FXML private HBox hBoxPerPane;

    // =========================================================================
    // COMPONENTI UI (FXML) - INPUT E RICERCA
    // =========================================================================
    @FXML private TextField comuneTextField;
    @FXML private TextField numeroStanzeTextField;
    @FXML private ComboBox<String> prezzoMinText;
    @FXML private ComboBox<String> prezzoMaxText;
    @FXML private MenuButton classeMenu;
    @FXML private MenuButton menuTipologia;
    @FXML private ListView<String> listMaps;

    // =========================================================================
    // COMPONENTI UI (FXML) - BOTTONI E LINK
    // =========================================================================
    @FXML private Button ricercaBtn;
    @FXML private Button btnIndietro;
    @FXML private Button btnAvanti;
    @FXML private Button btnUserDashboard;
    @FXML private Hyperlink FAQLink;

    // =========================================================================
    // COMPONENTI UI (FXML) - ELEMENTI VISIVI E LABELS
    // =========================================================================
    @FXML private ImageView fotoProfilo;
    @FXML private ImageView imgNoRicercheEffettuate;
    @FXML private Text txtNoRicercheEffettuate;
    @FXML private Label labelErrorComune;
    @FXML private Label labelErrorNumeroStanze;

    // =========================================================================
    // VARIABILI DI STATO
    // =========================================================================
    private static Utente utente;
    private boolean flagNonAutoComplete = true;

    // =========================================================================
    // GETTER, SETTER E INITIALIZE
    // =========================================================================
    public static void setUtente(Utente utente) {
        HomePageController.utente = utente;
        System.out.println("setUtente" + utente.getNome());
    }

    public static Utente getUtente() {
        return utente;
    }

    public void initialize(Utente utente) {
        aggiungiListenerPrezzi();
        ricercaBtn.setDefaultButton(true);
        
        principalScrollPane.addEventFilter(ScrollEvent.SCROLL, event -> {
            if (event.getDeltaY() != 0) {
                double delta = event.getDeltaY();
                principalScrollPane.setVvalue(principalScrollPane.getVvalue() - delta / 700);
                event.consume();
            }
        });
        
        listMaps.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (Boolean.FALSE.equals(newVal)) {
                listMaps.setVisible(false);
            }
        });

        listMaps.setOnMouseClicked(event -> {
            String selected = listMaps.getSelectionModel().getSelectedItem();
            if (selected != null) {
                flagNonAutoComplete = true;
                comuneTextField.setText(selected);
                flagNonAutoComplete = false;
                Platform.runLater(() -> {
                    listMaps.getItems().clear();
                    listMaps.setVisible(false);
                });
            }
        });
        
        ArrayList<ClasseEnergetica> list = new ArrayList<>();
        ArrayList<TipoVendita> list2 = new ArrayList<>();
        aggiungiOpzioniArrayClasse(list);
        aggiungiOpzioniArrayTipi(list2);
        addListenerClasseEn(list);
        addListenerTipologia(list2);
        setUtente(utente);

        new Thread(() -> {
            List<GetAllRicercheResponse> ricerchePassate = ClienteServiceFacade.getAllRicerche(utente);
            if (ricerchePassate.isEmpty()) {
                txtNoRicercheEffettuate.setVisible(true);
                imgNoRicercheEffettuate.setVisible(true);
                btnAvanti.setVisible(false);
                btnIndietro.setVisible(false);
            } else {
                if (ricerchePassate.size() > 3) {
                    btnAvanti.setVisible(true);
                    btnIndietro.setVisible(true);
                }
                txtNoRicercheEffettuate.setVisible(false);
                imgNoRicercheEffettuate.setVisible(false);
            }
            makeRicerchePassate(ricerchePassate);
        }).start();
        
        addTextFormatter(comuneTextField);
    }

    // =========================================================================
    // AZIONI DI NAVIGAZIONE E SCROLLING (UI Events)
    // =========================================================================
    @FXML
    private void handleAvanti2() {
        double currentHvalue = scrollPaneHome2.getHvalue();
        scrollPaneHome2.setHvalue(Math.min(1, currentHvalue + 0.2));
    }

    @FXML
    private void handleIndietro2() {
        double currentHvalue = scrollPaneHome2.getHvalue();
        scrollPaneHome2.setHvalue(Math.max(0, currentHvalue - 0.2));
    }

    @FXML
    private void handlerTastoAvanti() {
        double currentHvalue = scrollPaneHome.getHvalue();
        scrollPaneHome.setHvalue(Math.min(1, currentHvalue + 0.2));
    }

    @FXML
    private void handlerTastoIndietro() {
        double currentHvalue = scrollPaneHome.getHvalue();
        scrollPaneHome.setHvalue(Math.max(0, currentHvalue - 0.2));
    }

    @FXML
    private void scrollToPane(ActionEvent event) {
        double targetPosition = annunciPane.getLayoutY() / principalPane.getHeight();
        principalScrollPane.setVvalue(targetPosition);
    }

    @FXML
    private void scrollToRicercheRecenti(ActionEvent event) {
        System.out.println("ciao");
        double offset = 0.0001;
        double targetPosition = (ricerchePane.getLayoutY() / principalPane.getHeight()) + offset;
        principalScrollPane.setVvalue(targetPosition);
    }

    @FXML
    private void scrollToPaneNews(ActionEvent event) {
        double offset = 0.1;
        double targetPosition = (newsPane.getLayoutY() / principalPane.getHeight()) + offset;
        principalScrollPane.setVvalue(targetPosition);
    }

    @FXML
    public void openFAQ() {
        try { InfoPage.faqPageInit(); } 
        catch (IOException e) { AlertFactory.creaAlertErroreCaricamentoPagina(); }
    }

    @FXML
    public void openPrivacyPage() {
        try { InfoPage.privacyPageInit(); } 
        catch (IOException e) { AlertFactory.creaAlertErroreCaricamentoPagina(); }
    }

    @FXML
    public void openChiSiamoPage() {
        try { InfoPage.chiSiamoPageInit(); } 
        catch (IOException e) { AlertFactory.creaAlertErroreCaricamentoPagina(); }
    }

    // =========================================================================
    // GESTIONE DASHBOARD (Accesso aree riservate)
    // =========================================================================
    public void apriDashboard() {
        switch (utente) {
            case Cliente cliente -> apriDashboardCliente();
            case Agente agente -> apriDashboardAgente();
            case GestoreAgenziaImmobiliare gestoreAgenziaImmobiliare -> apriDashboardGestore();
            default -> AlertFactory.creaAlertErroreInterno();
        }
    }

    private void apriDashboardGestore() {
        try {
            DashboardAmministratore.initializePageDashboardAmministratore((Stage) btnAvanti.getScene().getWindow());
        } catch (IOException e) {
            AlertFactory.creaAlertErroreInterno();
        }
    }

    private void apriDashboardCliente() {
        try {
            System.out.println("apriDashboardCliente" + utente.getNome());
            DashBoardCliente.initializePageDashboardCliente(btnAvanti.getScene().getWindow(), utente);
        } catch (InterruptedException e) {
            AlertFactory.creaAlertErroreCaricamentoPagina();
            Thread.currentThread().interrupt();
        } catch (IOException e) {
            AlertFactory.creaAlertErroreInterno();
        }
    }

    private void apriDashboardAgente() {
        try {
            Agente agente = new Agente();
            agente.setAccountAgente(new Account(utente.getAccountAgente().getEmail(), utente.getAccountAgente().getPassword()));
            agente.setToken(utente.getToken());
            DashboardAgenteController.setAgente(agente);
            DashboardAgente.initializePageDashboardAgente(btnAvanti.getScene().getWindow());
        } catch (IOException e) {
            e.printStackTrace();
            AlertFactory.creaAlertErroreCaricamentoPagina();
        } catch (InterruptedException e) {
            AlertFactory.creaAlertErroreCaricamentoPagina();
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }
    }

    // =========================================================================
    // MOTORE DI RICERCA ED AUTOCOMPLETE
    // =========================================================================
    public void suggerimento() {
        fetchAsync(comuneTextField.getText());
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

    public void gestioneRicercaImmobile() {
        String comune = comuneTextField.getText().trim();
        Integer numStanze = (numeroStanzeTextField.getText().trim().isEmpty())
                ? null : Integer.parseInt(numeroStanzeTextField.getText());

        BigDecimal prezzoMin = (prezzoMinText.getValue() == null || prezzoMinText.getValue().trim().isEmpty())
                ? null : new BigDecimal(prezzoMinText.getValue().replace(".", ""));

        BigDecimal prezzoMax = (prezzoMaxText.getValue() == null || prezzoMaxText.getValue().trim().isEmpty())
                ? null : new BigDecimal(prezzoMaxText.getValue().replace(".", ""));

        TipoVendita tipoVendita = (Objects.equals(menuTipologia.getText(), "Qualsiasi") || menuTipologia.getText().trim().isEmpty())
                ? null : TipoVendita.valueOf(menuTipologia.getText().trim());

        ClasseEnergetica classeEnergetica = (Objects.equals(classeMenu.getText(), "Qualsiasi") || classeMenu.getText().trim().isEmpty())
                ? null : ClasseEnergetica.fromString(classeMenu.getText().trim());

        faiRicerca(comune, tipoVendita, prezzoMin, prezzoMax, numStanze, classeEnergetica, ricercaBtn);
    }

    private static void faiRicerca(String comune, TipoVendita stato, BigDecimal prezzoMin, BigDecimal prezzoMax, Integer numStanze, ClasseEnergetica classeEnergetica, Button ricercaBtn) {
        try {
            SchermataRicercaController.setUtenteCheCerca(utente);
            RicercaPage.caricamento((Stage) ricercaBtn.getScene().getWindow(), comune, stato, prezzoMin, prezzoMax, numStanze, classeEnergetica);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    // =========================================================================
    // METODI HELPER PER UI, FORMATTAZIONE E LISTENER
    // =========================================================================
    private void addListenerTipologia(ArrayList<TipoVendita> list) {
        for (TipoVendita item : list) {
            MenuItem menuItem = new MenuItem(item.toString());
            menuItem.setOnAction(event -> menuTipologia.setText(item.toString()));
            menuTipologia.getItems().add(menuItem);
        }
    }

    private void addListenerClasseEn(ArrayList<ClasseEnergetica> list) {
        for (ClasseEnergetica item : list) {
            MenuItem menuItem = new MenuItem(item.toString());
            menuItem.setOnAction(event -> classeMenu.setText(item.toString()));
            classeMenu.getItems().add(menuItem);
        }
    }

    private void aggiungiOpzioniArrayClasse(ArrayList<ClasseEnergetica> list) {
        CreaAnnuncioController.setOptions(list);
    }

    private void aggiungiOpzioniArrayTipi(ArrayList<TipoVendita> list) {
        list.add(TipoVendita.VENDITA);
        list.add(TipoVendita.AFFITTO);
    }

    private void aggiungiListenerPrezzi() {
        prezzoMinText.getEditor().addEventFilter(KeyEvent.KEY_TYPED, event -> {
            if (!event.getCharacter().matches("[0-9]")) event.consume();
        });
        prezzoMaxText.getEditor().addEventFilter(KeyEvent.KEY_TYPED, event -> {
            if (!event.getCharacter().matches("[0-9]")) event.consume();
        });
        
        prezzoMinText.getItems().addAll("10.000", "20.000", "30.000", "40.000", "50.000", "60.000", "70.000");
        prezzoMaxText.getItems().addAll("10.000", "20.000", "30.000", "40.000", "50.000", "60.000", "70.000");
        prezzoMaxText.setEditable(true);
        prezzoMinText.setEditable(true);
        
        prezzoMinText.getEditor().textProperty().addListener((observable, oldValue, newValue) -> {
            String cleanText = newValue.replaceAll("[^\\d]", "");
            if (!cleanText.isEmpty()) {
                StringBuilder formattedText = aggiungiSoloNumeriEPunti(cleanText);
                prezzoMinText.setValue(formattedText.reverse().toString());
            } else {
                prezzoMinText.setValue("");
            }
        });
        
        prezzoMaxText.getEditor().textProperty().addListener((observable, oldValue, newValue) -> {
            String cleanText = newValue.replaceAll("[^\\d]", "");
            if (!cleanText.isEmpty()) {
                StringBuilder formattedText = aggiungiSoloNumeriEPunti(cleanText);
                prezzoMaxText.setValue(formattedText.reverse().toString());
            } else {
                prezzoMaxText.setValue("");
            }
        });
    }

    private static StringBuilder aggiungiSoloNumeriEPunti(String cleanText) {
        StringBuilder formattedText = new StringBuilder();
        int count = 0;
        for (int i = cleanText.length() - 1; i >= 0; i--) {
            formattedText.append(cleanText.charAt(i));
            count++;
            if (count % 3 == 0 && i != 0) {
                formattedText.append(".");
            }
        }
        return formattedText;
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

    static void aggiungiListener(TextField textSpese) {
        textSpese.textProperty().addListener((observable, oldValue, newValue) -> {
            String cleanText = newValue.replaceAll("[^\\d]", "");
            if (!cleanText.isEmpty()) {
                StringBuilder formattedText = aggiungiSoloNumeriEPunti(cleanText);
                textSpese.setText(formattedText.reverse().toString());
            } else {
                textSpese.setText("");
            }
        });
    }

    // =========================================================================
    // GENERAZIONE DINAMICA DELLA UI (Ricerche Passate)
    // =========================================================================
    private void makeRicerchePassate(List<GetAllRicercheResponse> ricerchePassate) {
        Platform.runLater(() -> {
            for (GetAllRicercheResponse ricerca : ricerchePassate) {
                Pane pane = createPane(
                        ricerca.getComune(), ricerca.getTipoVendita(),
                        ricerca.getPrezzoMin(), ricerca.getPrezzoMax(), 
                        ricerca.getNumeroStanze(), ricerca.getClasseEnergetica()
                );
                hBoxPerPane.getChildren().add(pane);
            }
        });
    }

    private Pane createPane(String comune, TipoVendita stato, BigDecimal prezzoMin, BigDecimal prezzoMax, Integer numStanze, ClasseEnergetica classeEnergetica) {
        Pane pane = FactoryRicerchePassate.createPane();
        pane.setPrefSize(300, 440);
        pane.setStyle("""
            -fx-background-color: linear-gradient(to bottom, #E0F2F1, #bfe3e1);
            -fx-background-radius: 18;
            -fx-border-radius: 18;
            -fx-border-color: #145858;
            -fx-border-width: 1.5;
            -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.18), 12, 0.2, 0, 4);
        """);

        Label title = new Label("RICERCA SALVATA");
        title.setLayoutX(20);
        title.setLayoutY(18);
        title.setStyle("""
            -fx-text-fill: #145858;
            -fx-font-size: 18px;
            -fx-font-weight: bold;
            -fx-letter-spacing: 1px;
        """);

        Separator separator = new Separator();
        separator.setLayoutX(15);
        separator.setLayoutY(50);
        separator.setPrefWidth(270);

        Label labelComune = new Label("🌍 Comune: " + (!comune.isEmpty() ? comune : "Qualsiasi"));
        labelComune.setLayoutX(20); labelComune.setLayoutY(70);

        Label labelStato = new Label("🏷 Vendita: " + (stato != null ? stato : "Qualsiasi"));
        labelStato.setLayoutX(20); labelStato.setLayoutY(110);

        Label labelPrezzoMin = new Label("💰 Prezzo min: " + (prezzoMin != null ? prezzoMin : "Qualsiasi"));
        labelPrezzoMin.setLayoutX(20); labelPrezzoMin.setLayoutY(150);

        Label labelPrezzoMax = new Label("💰 Prezzo max: " + (prezzoMax != null ? prezzoMax : "Qualsiasi"));
        labelPrezzoMax.setLayoutX(20); labelPrezzoMax.setLayoutY(190);

        Label numeroStanzeLbl = new Label("🛏 Stanze: " + (numStanze != null ? numStanze : "Qualsiasi"));
        numeroStanzeLbl.setLayoutX(20); numeroStanzeLbl.setLayoutY(230);

        Label classeEnergeticaLbl = new Label("⚡ Classe: " + (classeEnergetica != null ? classeEnergetica : "Qualsiasi"));
        classeEnergeticaLbl.setLayoutX(20); classeEnergeticaLbl.setLayoutY(270);

        String labelStyle = """
            -fx-text-fill: #294c71;
            -fx-font-size: 15px;
            -fx-font-weight: 500;
        """;

        labelComune.setStyle(labelStyle);
        labelStato.setStyle(labelStyle);
        labelPrezzoMin.setStyle(labelStyle);
        labelPrezzoMax.setStyle(labelStyle);
        numeroStanzeLbl.setStyle(labelStyle);
        classeEnergeticaLbl.setStyle(labelStyle);

        Button btnCercaPassati = new Button("RIPETI RICERCA");
        btnCercaPassati.setLayoutX(60);
        btnCercaPassati.setLayoutY(340);
        btnCercaPassati.setPrefSize(180, 42);
        btnCercaPassati.setStyle("""
            -fx-cursor: hand;
            -fx-background-radius: 10;
            -fx-background-color: linear-gradient(to bottom, #3e9994, #296c71);
            -fx-text-fill: white;
            -fx-font-weight: bold;
            -fx-font-size: 13px;
            -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.25), 8, 0.2, 0, 2);
        """);

        btnCercaPassati.setOnMouseEntered(e -> btnCercaPassati.setStyle("""
            -fx-cursor: hand;
            -fx-background-radius: 10;
            -fx-background-color: #20B2AA;
            -fx-text-fill: white;
            -fx-font-weight: bold;
            -fx-font-size: 13px;
        """));

        btnCercaPassati.setOnMouseExited(e -> btnCercaPassati.setStyle("""
            -fx-cursor: hand;
            -fx-background-radius: 10;
            -fx-background-color: linear-gradient(to bottom, #3e9994, #296c71);
            -fx-text-fill: white;
            -fx-font-weight: bold;
            -fx-font-size: 13px;
        """));

        btnCercaPassati.setOnAction(e -> faiRicerca(comune, stato, prezzoMin, prezzoMax, numStanze, classeEnergetica, btnCercaPassati));

        pane.getChildren().addAll(
                title, separator, labelComune, labelStato, labelPrezzoMin, 
                labelPrezzoMax, numeroStanzeLbl, classeEnergeticaLbl, btnCercaPassati
        );

        return pane;
    }
}