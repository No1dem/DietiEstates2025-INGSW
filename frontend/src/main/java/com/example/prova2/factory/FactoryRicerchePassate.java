package com.example.prova2.factory;

import javafx.scene.layout.Pane;

public class FactoryRicerchePassate {
    
    public static Pane createPane(){
        Pane pane = new Pane();
        pane.setPrefSize(358, 478);
        pane.setMinSize(358, 478);
        pane.setMaxSize(358, 478);
        
        
        pane.setStyle(
            "-fx-background-color: rgba(255, 255, 255, 0.1); " +     // Sfondo bianco semi-trasparente
            "-fx-background-radius: 25; " +                           // Angoli arrotondati morbidi
            "-fx-border-color: rgba(255, 255, 255, 0.2); " +          // Bordino luminoso sottile
            "-fx-border-radius: 25; " +                               // Arrotondamento del bordo
            "-fx-border-width: 1.5; " +                               // Spessore del bordo
            "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 15, 0, 0, 5);" // Ombra per dare profondità
        );
        
        return pane;
    }
}
