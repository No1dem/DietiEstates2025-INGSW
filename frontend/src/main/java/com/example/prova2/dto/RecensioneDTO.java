package com.example.prova2.dto;

public class RecensioneDTO {

    // =========================================================================
    // CAMPI (Proprietà)
    // =========================================================================
    private String agenteDaRecensire;
    private int valutazione;

    // =========================================================================
    // COSTRUTTORI
    // =========================================================================
    public RecensioneDTO() {}

    public RecensioneDTO(String agenteDaRecensire, int valutazione) {
        this.agenteDaRecensire = agenteDaRecensire;
        this.valutazione = valutazione;
    }

    // =========================================================================
    // GETTER
    // =========================================================================
    public String getAgenteDaRecensire() { return agenteDaRecensire; }
    public int getValutazione() { return valutazione; }

    // =========================================================================
    // SETTER
    // =========================================================================
    public void setAgenteDaRecensire(String agenteDaRecensire) { this.agenteDaRecensire = agenteDaRecensire; }
    public void setValutazione(int valutazione) { this.valutazione = valutazione; }

}