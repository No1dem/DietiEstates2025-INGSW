package it.unina.dietiEstates25.model;

import jakarta.json.bind.annotation.JsonbTypeAdapter;

@JsonbTypeAdapter(ClasseEnergeticaAdapter.class)
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

    public static ClasseEnergetica fromString(String valore) {
        if(valore == null) return null;
        for (ClasseEnergetica classe : ClasseEnergetica.values()) {
            if (classe.valore.equals(valore)) {
                return classe;
            }
        }
        throw new IllegalArgumentException("Valore non valido per ClasseEnergetica: " + valore);
    }

    public String getValore() {
        return valore;
    }

    @Override
    public String toString() {
        return valore;
    }
}

