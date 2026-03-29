package com.example.prova2.service;

public class JavaScriptBridge {
    // Questo metodo permette di convertire l'oggetto in una forma che può essere chiamata da JavaScript
    @Override
    public String toString() {
        return "{sendCoordinates: function(lat, lng) { window.javaApp.sendCoordinates(lat, lng); }}";
    }
}
