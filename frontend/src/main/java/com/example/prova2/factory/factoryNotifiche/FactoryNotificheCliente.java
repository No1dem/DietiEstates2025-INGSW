package com.example.prova2.factory.factoryNotifiche;

import java.io.IOException;

import com.example.prova2.controller.notifiche.VisualizzaNotificheClienteController;
import com.example.prova2.dto.GetNotificheDTO;
import com.example.prova2.dto.ImmobileResponseRicercaDTO;
import com.example.prova2.facade.ImmobileFacade;
import com.example.prova2.factory.AlertFactory;
import static com.example.prova2.model.CategoriaNotifica.CONSIGLIOIMMOBILE;
import static com.example.prova2.model.CategoriaNotifica.ESITOVISITA;
import com.example.prova2.model.Utente;
import com.example.prova2.view.InfoAboutPrenotazioneCliente;
import com.example.prova2.view.SchermataAnnuncio;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

public class FactoryNotificheCliente {

    public static Label createScrittaEsitoImmobile(boolean esito) {
        String contenutoLabel;
        if (esito) {
            contenutoLabel = "Visita accettata";
        }else{
            contenutoLabel = "Visita rifiutata";
        }
        Label label = new Label(contenutoLabel);
        label.setLayoutX(47);
        label.setLayoutY(4);
        label.setStyle("-fx-text-fill: white; -fx-font-size: 15px; -fx-font-weight: bold; -fx-font-style: italic;");
        return label;
    }

    public static Pane createPaneEsitoNotifica(boolean esito) {
        if(esito){
            return createPaneForVisitaAccettata();
        }
        else{
            return createPaneForVisitaRifiutata();
        }
    }

    public static Pane createPaneConsiglioImmobile(){
        Pane titoloPane = new Pane();
        titoloPane.setLayoutX(122);
        titoloPane.setLayoutY(14);
        titoloPane.setPrefSize(210, 35);
        // Changed to a transparent/glass style instead of solid color
        titoloPane.setStyle("-fx-background-color: rgba(105, 79, 93, 0.5); -fx-background-radius: 10; -fx-border-color: rgba(255,255,255,0.2); -fx-border-radius: 10;");
        return titoloPane;
    }

    public static Pane creaPaneForCategoria(String nomeCategoria){
        if(nomeCategoria.equals("CREAZIONE ACCOUNT")){
            return createPaneCreazioneAccount();
        }
        else if(nomeCategoria.equals("CAMBIO PASSWORD")){
            return createPaneForCambioPassword();
        }
        return null;
    }

    public static Pane createPaneCreazioneAccount(){
        Pane titoloPane = new Pane();
        titoloPane.setLayoutX(122);
        titoloPane.setLayoutY(14);
        titoloPane.setPrefSize(210, 35);
        titoloPane.setStyle("-fx-background-color: rgba(34, 42, 104, 0.5); -fx-background-radius: 10; -fx-border-color: rgba(255,255,255,0.2); -fx-border-radius: 10;");
        return titoloPane;
    }

    public static Pane createPaneForCambioPassword(){
        Pane titoloPane = new Pane();
        titoloPane.setLayoutX(122);
        titoloPane.setLayoutY(14);
        titoloPane.setPrefSize(210, 35);
        titoloPane.setStyle("-fx-background-color: rgba(16, 57, 0, 0.5); -fx-background-radius: 10; -fx-border-color: rgba(255,255,255,0.2); -fx-border-radius: 10;");
        return titoloPane;
    }

    public static Label createScrittaConsiglioImmobile(String scritta){
        Label label = new Label(scritta);
        label.setLayoutX(40);
        label.setLayoutY(4);
        // Changed text fill to a light blue to match the theme
        label.setStyle("-fx-text-fill: #b2d8d8; -fx-font-size: 15px; -fx-font-weight: bold; -fx-font-style: italic;");
        return label;
    }

    public static Label setContenuto(GetNotificheDTO notifica, int numeroNotifica){
        String contenutoNotifica = notifica.getNomeNotifica();
        if(contenutoNotifica.length()>=96 && contenutoNotifica.length()<=300) {
            contenutoNotifica = contenutoNotifica.substring(0, 96) + "\n" + contenutoNotifica.substring(96);
        }
        Label messaggio = new Label(numeroNotifica+") "+contenutoNotifica);
        messaggio.setLayoutX(124);
        messaggio.setLayoutY(60);
        // Made text white for dark background visibility
        messaggio.setStyle("-fx-text-fill: white; -fx-font-size: 15px; -fx-font-weight: bold;");
        return messaggio;
    }

    public static Label createLabelVisualizzaDettagli(VisualizzaNotificheClienteController controller, GetNotificheDTO notifica, Utente utente) throws IOException, InterruptedException {
        Label visualizzaDettagli = new Label("Visualizza dettagli aggiuntivi");
        visualizzaDettagli.setLayoutX(125);
        visualizzaDettagli.setLayoutY(130);
        visualizzaDettagli.setUnderline(true);

        // --- GESTIONE DEL CLICK SUL LINK ---
        visualizzaDettagli.setOnMouseClicked(event -> {
            
            // 1. Recuperiamo il popup delle notifiche
            Stage notificaStage = (Stage) visualizzaDettagli.getScene().getWindow();
            
            // 2. Recuperiamo la finestra principale (Dashboard) che sta "sotto" al popup
            Stage mainStage = (Stage) notificaStage.getOwner();
            if (mainStage == null) {
                mainStage = new Stage(); // Sicurezza nel caso non ci sia un owner
            }

            // 3. Chiudiamo il popup delle notifiche
            notificaStage.close();

            // Salviamo il riferimento alla finestra principale per usarlo nel Thread
            final Stage targetStage = mainStage;

            if (notifica.getCategoria() == ESITOVISITA) {
                try {
                    InfoAboutPrenotazioneCliente.initPage(new Stage(), controller, notifica.getIdNotifica());
                } catch (Exception e) {
                    AlertFactory.creaAlertErroreInterno();
                }
                
            } else if (notifica.getCategoria() == CONSIGLIOIMMOBILE) {
                
                new Thread(() -> {
                    try {
                        // SCARICA I DATI (fuori dal runLater per non bloccare l'interfaccia)
                        ImmobileResponseRicercaDTO immobile = ImmobileFacade.getInfoById(notifica.getIdImmobile(), utente.getToken());
                        
                        Platform.runLater(() -> {
                            try {
                                // APRE L'ANNUNCIO NELLA FINESTRA PRINCIPALE DELLA DASHBOARD
                                SchermataAnnuncio.initializeSchermataAnnuncioClienteNotifiche(targetStage, immobile, utente);
                            } catch (Exception e) {
                                System.err.println("Errore apertura annuncio: " + e.getMessage());
                                e.printStackTrace();
                                AlertFactory.creaAlertErroreInterno();
                            }
                        });
                    } catch (Exception e) {
                        System.err.println("Errore scaricamento dati immobile: " + e.getMessage());
                        e.printStackTrace();
                    }
                }).start();
            }
        });

        visualizzaDettagli.setStyle("-fx-text-fill: #b2d8d8; -fx-cursor: hand;  -fx-font-size: 15px; -fx-font-weight: bold;");
        visualizzaDettagli.setVisible(true);
        return visualizzaDettagli;
    }

    private static Pane createPaneForVisitaAccettata(){
        Pane titoloPane = new Pane();
        titoloPane.setLayoutX(122);
        titoloPane.setLayoutY(14);
        titoloPane.setPrefSize(210, 35);
        titoloPane.setStyle("-fx-background-color: rgba(0, 128, 0, 0.5); -fx-background-radius: 10; -fx-border-color: rgba(255,255,255,0.2); -fx-border-radius: 10;");
        return titoloPane;
    }

    private static Pane createPaneForVisitaRifiutata(){
        Pane titoloPane = new Pane();
        titoloPane.setLayoutX(122);
        titoloPane.setLayoutY(14);
        titoloPane.setPrefSize(210, 35);
        titoloPane.setStyle("-fx-background-color: rgba(255, 0, 0, 0.5); -fx-background-radius: 10; -fx-border-color: rgba(255,255,255,0.2); -fx-border-radius: 10;");
        return titoloPane;
    }

    public static ImageView aggiustaImmagineProfilo(ImageView closeIcon){
        closeIcon.setFitWidth(72);
        closeIcon.setFitHeight(74);
        closeIcon.setLayoutX(25);
        closeIcon.setLayoutY(30);
        closeIcon.setStyle("-fx-cursor: hand;");
        Circle clip = new Circle(36, 36, 36);
        closeIcon.setClip(clip);
        return closeIcon;
    }

    public static Label createScrittaLabel(String string) {
        String formattedString = string.substring(0, 1).toUpperCase() + string.substring(1).toLowerCase();
        return createScrittaConsiglioImmobile(formattedString);
    }
}