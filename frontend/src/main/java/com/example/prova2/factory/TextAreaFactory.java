
package com.example.prova2.factory;

import javafx.scene.control.TextArea;

public class TextAreaFactory {

    public static final String STILETEXTAREA= "-fx-background-color : rgba(255, 255, 255, 0.2); " +
            "-fx-backgrounds-radius : 10; " +
            "-fx-border-width: 2; " +
            "-fx-text-fill: #011638; " +
            "-fx-control-inner-background: transparent;";

    private TextAreaFactory() {}

    // Metodo per creare un TextArea con contenuto
    public static TextArea createTextArea(String content) {
        TextArea textArea = new TextArea();
        textArea.setText(content);
        textArea.setEditable(false);  // Imposta il TextArea come non modificabile
        textArea.setWrapText(true);    // Abilita il ritorno a capo automatico
        textArea.setPrefHeight(402);   // Imposta l'altezza preferita
        textArea.setPrefWidth(416);

        textArea.setStyle(STILETEXTAREA);
        // Imposta la larghezza preferita
        return textArea;
    }
}
