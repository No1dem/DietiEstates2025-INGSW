package com.example.prova2.model;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Arredamento {
    ARREDATO("Arredato", "ARREDATO"),
    PARZIALMENTEARREDATO("Parzialmente Arredato", "PARZIALMENTEARREDATO"),
    NONARREDATO("Non Arredato", "NONARREDATO");

    private final String label; // Etichetta leggibile
    private final String value; // Valore usato nel JSON

    // Costruttore
    Arredamento(String label, String value) {
        this.label = label;
        this.value = value;
    }

    // Metodo per ottenere l'etichetta leggibile
    public String getLabel() {
        return label;
    }

    @Override
    public String toString() {
        return label;
    }

    // Questo serve per serializzare l'enum nel JSON con il valore corretto
    @JsonValue
    public String getValue() {
        return value;
    }

    // Questo permette di deserializzare il valore dal JSON
    @JsonCreator
    public static Arredamento fromValue(String value) {
        if (value == null || value.isEmpty()) {
            throw new IllegalArgumentException("Valore arredamento nullo o vuoto");
        }

        // Normalizza il valore rimuovendo spazi e forzando maiuscole
        String normalizedValue = value.replace(" ", "").toUpperCase();

        for (Arredamento arredamento : Arredamento.values()) {
            if (arredamento.value.replace(" ", "").equalsIgnoreCase(normalizedValue)) {
                return arredamento;
            }
        }

        throw new IllegalArgumentException("Valore arredamento non valido: " + value);
    }

    public static Arredamento fromString(String valore) {
        // Gestisce i possibili spazi tra le parole nel database
        for (Arredamento arredamento : Arredamento.values()) {
            if (arredamento.label.equalsIgnoreCase(valore.trim())) {
                return arredamento;
            }
        }
        throw new IllegalArgumentException("Valore non valido per Arredamento: " + valore);
    }
}

