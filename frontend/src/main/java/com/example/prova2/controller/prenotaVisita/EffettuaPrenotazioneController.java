package com.example.prova2.controller.prenotaVisita;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import com.example.prova2.controller.AnnullaVisita;
import com.example.prova2.controller.HomePageController;
import com.example.prova2.dto.AddVisitaRequestDTO;
import com.example.prova2.dto.VisitaResponseDTO;
import com.example.prova2.dto.WeatherResponseDTO;
import com.example.prova2.facade.ImmobileFacade;
import com.example.prova2.factory.AlertFactory;
import com.example.prova2.model.Cliente;
import com.example.prova2.model.Immobile;
import com.example.prova2.service.MeteoService;
import com.example.prova2.view.EffettuaPrenotazione;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public class EffettuaPrenotazioneController {

    // =========================================================================
    // COMPONENTI UI (FXML) - PANNELLI E CONTENITORI
    // =========================================================================
    @FXML private HBox hboxContenitore;
    @FXML private ScrollPane scrollPane;
    @FXML private Pane primaFasciaOrari;
    @FXML private Pane secondaFasciaOrari;
    @FXML private Pane terzaFasciaOrari;
    @FXML private Pane paneMeteo;

    // =========================================================================
    // COMPONENTI UI (FXML) - ETICHETTE E TESTI
    // =========================================================================
    @FXML private Label primaFasciaLabel;
    @FXML private Label secondaFasciaLabel;
    @FXML private Label terzaFasciaLabel;
    @FXML private Label labelGradi;
    @FXML private Label scrittaData;
    @FXML private Label scrittaPosizione;
    @FXML private Label percentualePioggia;
    @FXML private Label scrittaMeteo;

    // =========================================================================
    // COMPONENTI UI (FXML) - IMMAGINI E BOTTONI
    // =========================================================================
    @FXML private ImageView sunImg;
    @FXML private ImageView temporaleImg;
    @FXML private Button bottoneInvia;
    @FXML private Button btnAvantiDate;
    @FXML private Button btnIndietroDate;

    // =========================================================================
    // VARIABILI DI STATO E DATI
    // =========================================================================
    private Cliente cliente;
    private Immobile immobile;
    private AnnullaVisita controllerPrec;
    private AddVisitaRequestDTO requestDTOIn;

    private Pane panePrecedentePerDate = null;
    private Pane panePrecedentePerOrari = null;
    private final HashMap<Pane, Label> paneEOrari = new HashMap<>();

    // =========================================================================
    // INIZIALIZZAZIONE E SETUP UI
    // =========================================================================
    public void initalize(String email, Immobile immobile, AnnullaVisita controllerPrec) {
        this.controllerPrec = controllerPrec;
        this.cliente = new Cliente(email);
        this.immobile = immobile;
        
        btnAvantiDate.setOnAction(event -> scrollHorizontally(100));
        btnIndietroDate.setOnAction(event -> scrollHorizontally(-100));
        
        List<Pane> orari = new ArrayList<>();
        LocalDate oggi = LocalDate.now();
        hboxContenitore.setSpacing(20);
        
        new Thread(() -> {
            Platform.runLater(() -> {
                for (int i = 0; i <= 14; i++) {
                    Pane pane = creaPaneGiorni(oggi.plusDays(i));
                    hboxContenitore.getChildren().add(pane);
                }
                initOrari(orari);
                gestisciUnClickOrari(orari);
            });
        }).start();
    }

    private void scrollHorizontally(double amount) {
        Platform.runLater(() -> {
            btnIndietroDate.setVisible(true);
            double current = scrollPane.getHvalue();
            double scrollAmount = amount / (hboxContenitore.getBoundsInLocal().getWidth() - scrollPane.getWidth());
            double newValue = Math.max(0, Math.min(1, current + scrollAmount));
            scrollPane.setHvalue(newValue);
        });
    }

    private void initOrari(List<Pane> orari) {
        orari.add(primaFasciaOrari);
        orari.add(secondaFasciaOrari);
        orari.add(terzaFasciaOrari);
        paneEOrari.put(primaFasciaOrari, primaFasciaLabel);
        paneEOrari.put(secondaFasciaOrari, secondaFasciaLabel);
        paneEOrari.put(terzaFasciaOrari, terzaFasciaLabel);
    }

    // =========================================================================
    // GENERAZIONE DINAMICA E GESTIONE CLICK PANNELLI
    // =========================================================================
    public Pane creaPaneGiorni(LocalDate date) {
        VBox pane = new VBox();
        pane.setPrefSize(100, 95); 
        pane.setAlignment(Pos.CENTER);
        pane.setStyle("-fx-background-color: white; -fx-background-radius: 20; -fx-cursor: hand; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 5, 0, 0, 3);");

        Label giornoLbl = new Label(date.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.ITALIAN));
        giornoLbl.setStyle("-fx-text-fill: #011638;");
        giornoLbl.setFont(new Font("System Bold", 14));

        Label numeroLbl = new Label(date.getDayOfMonth() + "");
        numeroLbl.setStyle("-fx-text-fill: #011638;");
        numeroLbl.setFont(new Font("System Bold", 32));

        Label meseLbl = new Label(date.getMonth().getDisplayName(TextStyle.SHORT, Locale.ITALIAN));
        meseLbl.setStyle("-fx-text-fill: #011638;");
        meseLbl.setFont(new Font("System Bold", 14));
        
        pane.setId(numeroLbl.getText() + " " + meseLbl.getText());
        
        pane.setOnMouseEntered(event ->
                pane.setStyle("-fx-background-color: #E0E0E0; -fx-background-radius: 20; -fx-cursor: hand; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 5, 0, 0, 3);")
        );
        pane.setOnMouseExited(event -> {
            if (pane != panePrecedentePerDate) {
                pane.setStyle("-fx-background-color: white; -fx-background-radius: 20; -fx-cursor: hand; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 5, 0, 0, 3);");
            }
        });
        pane.setOnMouseClicked(event -> {
            if (panePrecedentePerDate != null && !panePrecedentePerDate.getId().equals(pane.getId())) {
                panePrecedentePerDate.setStyle("-fx-background-color: white; -fx-background-radius: 20; -fx-cursor: hand; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 5, 0, 0, 3);");
            }
            pane.setStyle("-fx-background-color: #B8B8B8; -fx-background-radius: 20; -fx-cursor: hand; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 5, 0, 0, 3);");
            panePrecedentePerDate = pane;
            vediMeteo();
            attivaBottone(panePrecedentePerOrari);
        });
        
        pane.getChildren().addAll(giornoLbl, numeroLbl, meseLbl);
        return pane;
    }

    private void gestisciUnClickOrari(List<Pane> fasceOrarie) {
        for (Pane orari : fasceOrarie) {
            orari.setOnMouseEntered(event ->
                orari.setStyle("-fx-background-color: #E0E0E0; -fx-background-radius: 25; -fx-cursor: hand; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 5, 0, 0, 3);")
            );
            orari.setOnMouseExited(event -> {
                if (orari != panePrecedentePerOrari) {
                    orari.setStyle("-fx-background-color: white; -fx-background-radius: 25; -fx-cursor: hand; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 5, 0, 0, 3);");
                }
            });
            orari.setOnMouseClicked(event -> {
                if (panePrecedentePerOrari != null && !panePrecedentePerOrari.getId().equals(orari.getId())) {
                    panePrecedentePerOrari.setStyle("-fx-background-color: white; -fx-background-radius: 25; -fx-cursor: hand; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 5, 0, 0, 3);");
                }
                orari.setStyle("-fx-background-color: #B8B8B8; -fx-background-radius: 25; -fx-cursor: hand; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 5, 0, 0, 3);");
                panePrecedentePerOrari = orari;
                attivaBottone(panePrecedentePerDate);
            });
        }
    }

    private void attivaBottone(Pane pane) {
        bottoneInvia.setVisible(pane != null);
    }

    // =========================================================================
    // LOGICA DI INVIO (Prenotazione)
    // =========================================================================
    @FXML
    public void inviaRichiesta() {
        String datiFormattati = getDatiFormattati();
        Label fasciaOrariaSelezionata = paneEOrari.get(panePrecedentePerOrari);
        
        String testoFascia = fasciaOrariaSelezionata.getText();
        String[] parts = testoFascia.split("-");
        
        String startHourStr = parts[0].split(":")[0].trim(); 
        String endHourStr = parts[1].split(":")[0].trim();
        
        String startTime = String.format("%02d:00", Integer.parseInt(startHourStr));
        String endTime = String.format("%02d:00", Integer.parseInt(endHourStr));
        
        AddVisitaRequestDTO requestDTO = new AddVisitaRequestDTO(
                LocalDate.parse(datiFormattati),
                LocalTime.parse(startTime),
                LocalTime.parse(endTime),
                "ciao",
                cliente.getAccountAgente().getEmail(),
                immobile.getAgenteProprietario(),
                immobile.getId_immobile(), 
                HomePageController.getUtente().getToken()
        );
        
        VisitaResponseDTO response = ImmobileFacade.addVisita(requestDTO);
        this.requestDTOIn = requestDTO;
        
        if (controllerPrec != null) {
            this.controllerPrec.mostraPaneAnnullaVisita(requestDTOIn);
        }
        
        Platform.runLater(() -> mostraPopUp(response));
    }

    private void mostraPopUp(VisitaResponseDTO response) {
        if (response == null) {
            AlertFactory.creaAlertErroreInterno();
            return;
        }
        if (response.isSuccess()) {
            Alert alert = AlertFactory.creaAlertSuccessInvioPrenotazione();
            alert.showAndWait().ifPresentOrElse(responseUtente -> {
                chiudiFinestra();
            }, this::chiudiFinestra);
        }
    }

    private void chiudiFinestra() {
        EffettuaPrenotazione.getStagePrenotazione().close();
    }

    // =========================================================================
    // METODI ASINCRONI E API (Meteo)
    // =========================================================================
    private void vediMeteo() {
        fetchWeatherDate();
    }

    private void fetchWeatherDate() {
        new Thread(() -> {
            String datiFormattati = getDatiFormattati();
            String date = getDatiDaScrivereInLabel(panePrecedentePerDate.getId() + " " + LocalDate.now().getYear());
            WeatherResponseDTO dto = MeteoService.getWeather(immobile.getLatitudine(), immobile.getLongitudine(), datiFormattati, datiFormattati);
            
            Platform.runLater(() -> {
                if (dto != null) {
                    labelGradi.setText(dto.getDaily().getTemperature_2m_max().toString().replace("[", "").replace("]", "") + "°");
                    scrittaData.setText(date);
                    scrittaPosizione.setText(immobile.getComune() + "," + immobile.getViaImmobile() + " n°" + immobile.getNumeroCivico());
                    percentualePioggia.setText("Probabilità di pioggia : " + dto.getDaily().getPrecipitation_sum().toString().replace("[", "").replace("]", "") + "%");
                    scegliImgPrevisione(dto.getDaily().getPrecipitation_sum().getFirst());
                    scrittaMeteo.setVisible(false);
                    paneMeteo.setVisible(true);
                }
            });
        }).start();
    }

    private void scegliImgPrevisione(Integer percentualePioggia) {
        Platform.runLater(() -> {
            if (percentualePioggia <= 30) {
                sunImg.setVisible(true);
                temporaleImg.setVisible(false);
            } else if (percentualePioggia >= 70) {
                sunImg.setVisible(false);
                temporaleImg.setVisible(true);
                paneMeteo.setStyle(paneMeteo.getStyle() + "-fx-background-color: #BAB2CD;");
            }
        });
    }

    // =========================================================================
    // METODI HELPER E FORMATTAZIONE
    // =========================================================================
    @FXML
    public void tornaAllaDash() {
        // Implementazione non presente nel codice originale
    }

    private String getDatiFormattati() {
        String dataConAnno = panePrecedentePerDate.getId() + " " + LocalDate.now().getYear();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMM yyyy", Locale.ITALIAN);
        LocalDate dataParsed = LocalDate.parse(dataConAnno, formatter);
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return dataParsed.format(outputFormatter);
    }

    private String getDatiDaScrivereInLabel(String dataInput) {
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("d MMM yyyy", Locale.ITALIAN);
        LocalDate data = LocalDate.parse(dataInput, inputFormatter);
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("EEEE d MMMM yyyy", Locale.ITALIAN);
        String dataFormattata = data.format(outputFormatter);
        return rendiMaiuscoloPrimoCarattere(dataFormattata);
    }

    private static String rendiMaiuscoloPrimoCarattere(String dataFormattata) {
        if (!dataFormattata.isEmpty()) {
            dataFormattata = dataFormattata.substring(0, 1).toUpperCase() + dataFormattata.substring(1);
        }
        return dataFormattata;
    }
}