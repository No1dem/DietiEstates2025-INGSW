package com.example.prova2.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DatiImmobileDTO {

    // =========================================================================
    // CAMPI (Proprietà)
    // =========================================================================
    
    private String indirizzo;
    private String comune;
    private String città;
    private String agente;
    private String numeroCivico;
    private String via;

    // =========================================================================
    // COSTRUTTORI
    // =========================================================================

    public DatiImmobileDTO() {}

    public DatiImmobileDTO(String indirizzo, String comune, String agente, String città, String numeroCivico) {
        this.indirizzo = indirizzo;
        this.comune = comune;
        this.agente = agente;
        this.città = città;
        this.numeroCivico = numeroCivico;
    }

    // =========================================================================
    // GETTER
    // =========================================================================

    public String getIndirizzo() { return indirizzo; }
    public String getComune() { return comune; }
    public String getCittà() { return città; }
    public String getAgente() { return agente; }
    public String getNumeroCivico() { return numeroCivico; }
    public String getVia() { return via; }

    // =========================================================================
    // SETTER
    // =========================================================================

    public void setIndirizzo(String indirizzo) { this.indirizzo = indirizzo; }
    public void setComune(String comune) { this.comune = comune; }
    public void setCittà(String città) { this.città = città; }
    public void setAgente(String agente) { this.agente = agente; }
    public void setNumeroCivico(String numeroCivico) { this.numeroCivico = numeroCivico; }
    public void setVia(String via) { this.via = via; }

}