package it.unina.dietiEstates25.model;

public enum CategoriaNotifica {
    SPAM("SPAM"),
    VISITAIMMOBILE("VISITAIMMOBILE"),
    ESITORICHIESTA("ESITO RICHIESTA"),
    CAMBIOPASSWORD("CAMBIO PASSWORD"),
    CREAZIONEACCOUNT("CREAZIONE ACCOUNT"),
    ESITOVISITA("ESITOVISITA"),
    CONSIGLIOIMMOBILE("CONSIGLIOIMMOBILE");

    private final String label;


    public static CategoriaNotifica fromString(String valore) {
        for (CategoriaNotifica classe : CategoriaNotifica.values()) {
            if (classe.label.equals(valore)) {
                return classe;
            }
        }
        throw new IllegalArgumentException("Valore non valido per ClasseEnergetica: " + valore);
    }


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

