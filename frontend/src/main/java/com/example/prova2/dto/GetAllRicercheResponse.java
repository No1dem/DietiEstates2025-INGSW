package com.example.prova2.dto;

import java.math.BigDecimal;

import com.example.prova2.model.ClasseEnergetica;
import com.example.prova2.model.TipoVendita;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class GetAllRicercheResponse {

    // =========================================================================
    // CAMPI (Proprietà)
    // =========================================================================
    private int idRicerca;
    private String utente;
    private String agente;
    private String gestore;
    private Integer numeroStanze;
    private ClasseEnergetica classeEnergetica;
    private BigDecimal prezzoMin;
    private BigDecimal prezzoMax;
    private String comune;
    private String citta;
    private TipoVendita tipoVendita;
    private String immagineRicerca;

    // =========================================================================
    // COSTRUTTORI
    // =========================================================================
    public GetAllRicercheResponse() {}

    private GetAllRicercheResponse(Builder builder) {
        this.idRicerca = builder.idRicerca;
        this.utente = builder.cliente;
        this.tipoVendita = builder.tipovendita;
        this.prezzoMin = builder.prezzoMin;
        this.comune = builder.comune;
        this.citta = builder.citta;
        this.numeroStanze = builder.numeroStanze;
        this.classeEnergetica = builder.classeEnergetica;
        this.prezzoMax = builder.prezzoMax;
    }

    // =========================================================================
    // BUILDER 
    // =========================================================================
    public static class Builder {
        private int idRicerca;
        private String cliente;
        private TipoVendita tipovendita;
        private String citta;
        private Integer numeroStanze;
        private ClasseEnergetica classeEnergetica;
        private BigDecimal prezzoMin;
        private String comune;
        private BigDecimal prezzoMax;

        public Builder idRicerca(int idRicerca) { this.idRicerca = idRicerca; return this; }
        public Builder cliente(String cliente) { this.cliente = cliente; return this; }
        public Builder tipovendita(TipoVendita tipovendita) { this.tipovendita = tipovendita; return this; }
        public Builder citta(String citta) { this.citta = citta; return this; }
        public Builder numeroStanze(Integer numeroStanze) { this.numeroStanze = numeroStanze; return this; }
        public Builder classeEnergetica(ClasseEnergetica classeEnergetica) { this.classeEnergetica = classeEnergetica; return this; }
        public Builder prezzo(BigDecimal prezzo) { this.prezzoMin = prezzo; return this; } 
        public Builder comune(String comune) { this.comune = comune; return this; }
        public Builder prezzoMassimo(BigDecimal prezzoMassimo) { this.prezzoMax = prezzoMassimo; return this; } 

        public GetAllRicercheResponse build() {
            return new GetAllRicercheResponse(this);
        }
    }

    // =========================================================================
    // GETTER
    // =========================================================================
    public int getIdRicerca() { return idRicerca; }
    public String getUtente() { return utente; }
    public String getAgente() { return agente; }
    public String getGestore() { return gestore; }
    public Integer getNumeroStanze() { return numeroStanze; }
    public ClasseEnergetica getClasseEnergetica() { return classeEnergetica; }
    public BigDecimal getPrezzoMin() { return prezzoMin; }
    public BigDecimal getPrezzoMax() { return prezzoMax; }
    public String getComune() { return comune; }
    public String getCitta() { return citta; }
    public TipoVendita getTipoVendita() { return tipoVendita; }
    public String getImmagineRicerca() { return immagineRicerca; }

    // =========================================================================
    // SETTER
    // =========================================================================
    public void setIdRicerca(int idRicerca) { this.idRicerca = idRicerca; }
    public void setUtente(String utente) { this.utente = utente; }
    public void setAgente(String agente) { this.agente = agente; }
    public void setGestore(String gestore) { this.gestore = gestore; }
    public void setComune(String comune) { this.comune = comune; }
    public void setNumeroStanze(Integer numeroStanze) { this.numeroStanze = numeroStanze; }
    public void setCitta(String citta) { this.citta = citta; }
    public void setClasseEnergetica(ClasseEnergetica classeEnergetica) { this.classeEnergetica = classeEnergetica; }
    public void setPrezzoMin(BigDecimal prezzoMin) { this.prezzoMin = prezzoMin; }
    public void setPrezzoMax(BigDecimal prezzoMax) { this.prezzoMax = prezzoMax; }
    public void setTipoVendita(TipoVendita tipoVendita) { this.tipoVendita = tipoVendita; }
    public void setImmagineRicerca(String immagineRicerca) { this.immagineRicerca = immagineRicerca; }
    
    public void setIdRicerca(Integer idRicerca) { this.idRicerca = idRicerca; }
}