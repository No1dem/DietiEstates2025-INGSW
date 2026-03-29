package com.example.prova2.model;

public enum CategoriaNotifica {
    SPAM("SPAM"),
    VISITAIMMOBILE("VISITA IMMOBILE"),
    ESITORICHIESTA("ESITO RICHIESTA"),
    CAMBIOPASSWORD("CAMBIO PASSWORD"),
    CREAZIONEACCOUNT("CREAZIONE ACCOUNT"),
    ESITOVISITA("ESITOVISITA"),
    CONSIGLIOIMMOBILE("CONSIGLIOIMMOBILE");


    private final String label;

    // Costruttore per associare un'etichetta
    CategoriaNotifica(String label) {
        this.label = label;
    }

    // Getter per ottenere l'etichetta
    public String getLabel() {
        return label;
    }

    @Override
    public String toString() {
        return label;
    }
}

