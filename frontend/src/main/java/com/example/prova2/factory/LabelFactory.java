package com.example.prova2.factory;

import javafx.scene.control.Label;

public class LabelFactory {

    private LabelFactory() {}


    public static Label createLabel(String labelName) {
        Label label = new Label(labelName);
        label.setLayoutX(165.0);
        label.setLayoutY(31.0);
        return label;
    }

    public static Label createLabelBianco(String labelName) {
        Label label = createLabel(labelName);
        label.setStyle("-fx-text-fill: #f3f6f4");
        return label;

    }
}
