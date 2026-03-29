package com.example.prova2.factory.factoryNotifiche;

import java.io.IOException;

import com.example.prova2.controller.notifiche.VisualizzaNotificheAgenteController;
import com.example.prova2.dto.GetNotificheDTO;
import com.example.prova2.factory.AlertFactory;
import com.example.prova2.view.InfoAboutPrenotazioneAgente;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class FactoryForPageNotifica {

    public static Label createLabel(String categoria) {
        if(categoria.equals("CREAZIONE ACCOUNT")){
            return createLabelCreaAccount(categoria);
        }
        else if(categoria.equals("VISITA IMMOBILE")){
            return createLabelVisitaImmobile(categoria);
        }
        return createLabelVisitaImmobile(categoria);
    }

    public static Pane createPane(String categoria) {
        if(categoria.equals("CREAZIONE ACCOUNT")){
            return createPaneCreaAccount();
        }
        else if(categoria.equals("VISITA IMMOBILE")){
            return createPaneVisitaImmobile();
        }else{
            return createPaneVisitaImmobile();
        }
    }

    public static Label createLabelVisitaImmobile(String labelName) {
        Label label = new Label(labelName);
        label.setLayoutX(47);
        label.setLayoutY(7);
        label.setStyle("-fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold;");
        return label;
    }

    public static Label createLabelCreaAccount(String labelName) {
        Label label = new Label(labelName);
        label.setLayoutX(29);
        label.setLayoutY(7);
        label.setStyle("-fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold;");
        return label;
    }

    public static Pane createPaneVisitaImmobile(){
        Pane titoloPane = new Pane();
        titoloPane.setLayoutX(122);
        titoloPane.setLayoutY(14);
        titoloPane.setPrefSize(210, 35);
        // Stile "Badge" moderno semi-trasparente verde con bordino
        titoloPane.setStyle("-fx-background-color: rgba(52, 208, 88, 0.2); -fx-background-radius: 20; -fx-border-color: #34D058; -fx-border-radius: 20; -fx-border-width: 1.5;");
        return titoloPane;
    }

    public static Pane createPaneCreaAccount(){
        Pane titoloPane = new Pane();
        titoloPane.setLayoutX(122);
        titoloPane.setLayoutY(14);
        titoloPane.setPrefSize(210, 35);
        // Stile "Badge" moderno semi-trasparente oro/arancione con bordino
        titoloPane.setStyle("-fx-background-color: rgba(245, 166, 35, 0.2); -fx-background-radius: 20; -fx-border-color: #F5A623; -fx-border-radius: 20; -fx-border-width: 1.5;");
        return titoloPane;
    }

    public static Label createContenuto(GetNotificheDTO notifica, int numeroNotifica){
        Label msg = new Label(numeroNotifica + ") " + notifica.getNomeNotifica());
        msg.setLayoutX(124);
        msg.setLayoutY(60);
        // Testo chiaro, in grassetto e abilitato per andare a capo se troppo lungo
        msg.setStyle("-fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold;");
        msg.setWrapText(true);
        msg.setMaxWidth(600); // Evita che il testo sbordi dal pannello
        return msg;
    }

    public static Label createLabelVisualizzaDettagli(VisualizzaNotificheAgenteController controller, GetNotificheDTO notifica, Pane pane){
        Label visualizzaDettagli = new Label("Visualizza dettagli aggiuntivi");
        visualizzaDettagli.setLayoutX(125);
        visualizzaDettagli.setLayoutY(130);
        visualizzaDettagli.setOnMouseClicked(event -> {
            try {
                InfoAboutPrenotazioneAgente.initPage(new Stage(), controller, notifica.getIdNotifica(), pane);
            } catch (IOException e) {
                e.printStackTrace();
                AlertFactory.creaAlertErroreInterno();
            }
        });
        
        visualizzaDettagli.setStyle("-fx-text-fill: #b2d8d8; -fx-cursor: hand; -fx-font-size: 14px; -fx-font-weight: bold; -fx-underline: true;");
        visualizzaDettagli.setVisible(true);
        return visualizzaDettagli;
    }

    public static Button createButtonConferma(String nomeBottone){
        Button accetta = new Button(nomeBottone);
        accetta.setLayoutX(719);
        accetta.setLayoutY(125);
        accetta.setPrefSize(130, 32);
        // Bottone a pillola verde con ombra
        accetta.setStyle("-fx-background-color: #34D058; -fx-text-fill: white; -fx-background-radius: 25; -fx-cursor: hand; -fx-font-weight: bold; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 5, 0, 0, 2);");
        return accetta;
    }

    public static Button createButtonOk(String nomeBottone){
        Button accetta = new Button(nomeBottone);
        accetta.setLayoutX(719);
        accetta.setLayoutY(125);
        accetta.setPrefSize(90, 32);
        // Bottone a pillola verde con ombra
        accetta.setStyle("-fx-background-color: #34D058; -fx-text-fill: white; -fx-background-radius: 25; -fx-cursor: hand; -fx-font-weight: bold; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 5, 0, 0, 2);");
        return accetta;
    }

    public static Button createButtonSelezioneNuovaOra(String nomeBottone){
        Button accetta = new Button(nomeBottone);
        accetta.setLayoutX(500);
        accetta.setLayoutY(125);
        accetta.setPrefSize(160, 32);
        // Bottone "fantasma" a pillola con bordo bianco
        accetta.setStyle("-fx-background-color: transparent; -fx-border-color: white; -fx-border-radius: 25; -fx-text-fill: white; -fx-cursor: hand; -fx-font-weight: bold;");
        return accetta;
    }

    public static Button createButtonRifiuta(String nomeBottone){
        Button rifiuta = new Button(nomeBottone);
        rifiuta.setLayoutX(615);
        rifiuta.setLayoutY(125);
        rifiuta.setPrefSize(90, 32);
        // Bottone a pillola rosso pieno con ombra per massima coerenza
        rifiuta.setStyle("-fx-background-color: #E63946; -fx-text-fill: white; -fx-background-radius: 25; -fx-cursor: hand; -fx-font-weight: bold; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 5, 0, 0, 2);");
        return rifiuta;
    }

    public static ImageView createLogo(ImageView closeIcon){
        closeIcon.setFitWidth(110); 
        closeIcon.setFitHeight(90);
        closeIcon.setLayoutX(10);
        closeIcon.setLayoutY(20);
        closeIcon.setStyle("-fx-cursor: hand; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.3), 10, 0, 0, 3);");
        return closeIcon;
    }
}