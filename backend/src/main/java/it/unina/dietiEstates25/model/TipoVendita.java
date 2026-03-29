package it.unina.dietiEstates25.model;

public enum TipoVendita {
    VENDITA("VENDITA"),
    AFFITTO("AFFITTO"),;

    private final String valore;

    TipoVendita(String valore) {
        this.valore = valore;
    }

    public static TipoVendita fromString(String valore) {
        if(valore == null) return null;
        for (TipoVendita classe : TipoVendita.values()) {
            if (classe.valore.equals(valore)) {
                return classe;
            }
        }
        throw new IllegalArgumentException("Valore non valido per ClasseEnergetica: " + valore);
    }
    @Override
    public String toString() {
        return valore;
    }
}
