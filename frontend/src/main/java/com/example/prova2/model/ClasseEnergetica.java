package com.example.prova2.model;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum ClasseEnergetica {
    A_PLUS_PLUS("A++"),
    A_PLUS("A+"),
    A("A"),
    B("B"),
    C("C"),
    D("D"),
    E("E"),
    F("F"),
    G("G");

    private final String valore;

    ClasseEnergetica(String valore) {
        this.valore = valore;
    }


    @JsonCreator
    public static ClasseEnergetica fromString(String valore) {
        for (ClasseEnergetica classe : ClasseEnergetica.values()) {
            if (classe.valore.equals(valore)) {
                return classe;
            }
        }
        throw new IllegalArgumentException("Valore non valido per ClasseEnergetica: " + valore);
    }

    @JsonValue
    public String getValore() {
        return valore;
    }

    @Override
    public String toString() {
        return valore;
    }
}

