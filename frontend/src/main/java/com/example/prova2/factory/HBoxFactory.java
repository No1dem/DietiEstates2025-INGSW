package com.example.prova2.factory;

import javafx.geometry.Pos;
import javafx.scene.layout.HBox;

public class HBoxFactory {
    public static final String STILEHBOX= "-fx-background-color : rgba(255, 255, 255, 0.2); " +
            "-fx-backgrounds-radius : 10; " +
            "-fx-border-width: 2; " +
            "-fx-text-fill: #f3f6f4; " +
            "-fx-control-inner-background: transparent;";

    public static final String STILEHBOXANNUNCI="-fx-background-color : rgba(255, 255, 255, 0.2);" +
            " -fx-border-color:white;" +
            " -fx-border-width: 1;" +
            "    -fx-border-radius: 5 5 5 5;" +
            "    -fx-background-radius: 5 5 5 5;";

    private HBoxFactory() {}

    public static HBox createHbox(){
        HBox hBox = new HBox(160); 
        hBox.setAlignment(Pos.CENTER_LEFT);// Allinea gli elementi a sinistra
        hBox.setStyle(STILEHBOX);
        return hBox;
    }

    public static HBox createHboxAnnunci(){
        HBox hBox = new HBox();
        hBox.prefHeight(520);
        hBox.prefWidth(30);
        hBox.setAlignment(Pos.CENTER_LEFT);
        hBox.setStyle(STILEHBOXANNUNCI);
        return hBox;
    }
}
