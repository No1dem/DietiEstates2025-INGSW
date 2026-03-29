package com.example.prova2.factory;

import javafx.scene.layout.Pane;

public class PaneFactory {

    private PaneFactory() {}
    public static Pane createPane() {
        Pane pane = new Pane();
        pane.setPrefHeight(200.0);
        pane.setPrefWidth(200.0);
        return pane;
    }
}
