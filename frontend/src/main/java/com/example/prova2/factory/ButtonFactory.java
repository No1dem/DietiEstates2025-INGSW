
package com.example.prova2.factory;

import java.util.Objects;

import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ButtonFactory {

    private ButtonFactory(){}

    public static final String STILEBOTTONE1= "-fx-background-color : rgba(255, 255, 255, 0.2); " +
            "-fx-backgrounds-radius : 10; " +
            "-fx-border-width: 2; " +
            "-fx-text-fill: #f3f6f4; ";

    public static final String STILEBOTTONE2= "-fx-background-color : rgba(255, 255, 255, 0.4); " +
            "-fx-backgrounds-radius : 10; " +
            "-fx-border-width: 2; " +
            "-fx-text-fill: #f3f6f4; ";


    public static Button createButton() {
        Button btn = new Button();
        btn.setPrefWidth(175);
        btn.setPrefHeight(93.6);

        btn.setStyle(STILEBOTTONE2);
        return btn;
    }

    public static Button createLettaImageButton() {

        Image img = new Image(Objects.requireNonNull(ButtonFactory.class.getResourceAsStream("/com/example/prova2/images/icons8-visto-48.png"))); 
        ImageView imgView = new ImageView(img);

        // Configura l'ImageView (opzionale: dimensioni dell'immagine)
        imgView.setFitWidth(20); // Larghezza dell'immagine
        imgView.setFitHeight(20); // Altezza dell'immagine

        // Crea il pulsante e aggiungi l'ImageView
        Button buttonLetta = new Button();
        buttonLetta.setStyle(STILEBOTTONE1);
        buttonLetta.setGraphic(imgView); // Imposta l'immagine come contenuto del pulsante

        return buttonLetta;
    }

    public static Button createRifiutaImageButton() {
        // Carica l'immagine
        Image img = new Image(Objects.requireNonNull(ButtonFactory.class.getResourceAsStream("/com/example/prova2/images/icons8-no-48.png"))); 
        ImageView imgView = new ImageView(img);

        // Configura l'ImageView (opzionale: dimensioni dell'immagine)
        imgView.setFitWidth(20); // Larghezza dell'immagine
        imgView.setFitHeight(20); // Altezza dell'immagine

        // Crea il pulsante e aggiungi l'ImageView
        Button buttonRifiuta = new Button();
        buttonRifiuta.setStyle(STILEBOTTONE1);
        buttonRifiuta.setGraphic(imgView); // Imposta l'immagine come contenuto del pulsante

        return buttonRifiuta;
    }
}
