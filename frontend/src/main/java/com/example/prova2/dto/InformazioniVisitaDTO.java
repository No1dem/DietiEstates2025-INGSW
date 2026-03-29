package com.example.prova2.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import com.example.prova2.model.TipoImmobile;

public class InformazioniVisitaDTO {

    // =========================================================================
    // CAMPI (Proprietà)
    // =========================================================================
    private String emailClientePrenotatoVisita;
    private TipoImmobile tipoImmobile;
    private String viaImmobile;
    private String comune;
    private String numeroCivico;
    private int idImmobile;
    private LocalDate dataVisita;
    private LocalTime oraInizioVisita;
    private LocalTime oraFineVisita;
    private String agente;
    private String fotoProfiloAgente;

    // =========================================================================
    // COSTRUTTORI
    // =========================================================================
    public InformazioniVisitaDTO() {}

    public InformazioniVisitaDTO(Builder builder) {
        this.emailClientePrenotatoVisita = builder.emailClientePrenotatoVisita;
        this.tipoImmobile = builder.tipoImmobile;
        this.viaImmobile = builder.viaImmobile;
        this.comune = builder.comune;
        this.numeroCivico = builder.numeroCivico;
        this.idImmobile = builder.idImmobile;
        this.dataVisita = builder.dataVisita;
        this.oraInizioVisita = builder.oraInizioVisita;
        this.oraFineVisita = builder.oraFineVisita;
        this.agente = builder.agente;
        this.fotoProfiloAgente = builder.fotoProfiloAgente;
    }

    // =========================================================================
    // BUILDER
    // =========================================================================
    public static class Builder {
        private String emailClientePrenotatoVisita;
        private TipoImmobile tipoImmobile;
        private String viaImmobile;
        private String comune;
        private String numeroCivico;
        private int idImmobile;
        private LocalDate dataVisita;
        private LocalTime oraInizioVisita;
        private LocalTime oraFineVisita;
        private String agente;
        private String fotoProfiloAgente;

        public Builder emailClientePrenotatoVisita(String emailClientePrenotatoVisita) { this.emailClientePrenotatoVisita = emailClientePrenotatoVisita; return this; }
        public Builder tipoImmobile(TipoImmobile tipoImmobile) { this.tipoImmobile = tipoImmobile; return this; }
        public Builder viaImmobile(String viaImmobile) { this.viaImmobile = viaImmobile; return this; }
        public Builder comune(String comune) { this.comune = comune; return this; }
        public Builder numeroCivico(String numeroCivico) { this.numeroCivico = numeroCivico; return this; }
        public Builder idImmobile(int idImmobile) { this.idImmobile = idImmobile; return this; }
        public Builder dataVisita(LocalDate dataVisita) { this.dataVisita = dataVisita; return this; }
        public Builder oraVisita(LocalTime oraVisita) { this.oraInizioVisita = oraVisita; return this; } // Mantenuto il nome originale 'oraVisita'
        public Builder oraFineVisita(LocalTime oraFineVisita) { this.oraFineVisita = oraFineVisita; return this; }
        public Builder agente(String agente) { this.agente = agente; return this; }
        public Builder fotoProfiloAgente(String fotoProfiloAgente) { this.fotoProfiloAgente = fotoProfiloAgente; return this; }

        public InformazioniVisitaDTO build() {
            return new InformazioniVisitaDTO(this);
        }
    }

    // =========================================================================
    // GETTER
    // =========================================================================
    public String getEmailClientePrenotatoVisita() { return emailClientePrenotatoVisita; }
    public TipoImmobile getTipoImmobile() { return tipoImmobile; }
    public String getViaImmobile() { return viaImmobile; }
    public String getComune() { return comune; }
    public String getNumeroCivico() { return numeroCivico; }
    public int getIdImmobile() { return idImmobile; }
    public LocalDate getDataVisita() { return dataVisita; }
    public LocalTime getOraInizioVisita() { return oraInizioVisita; }
    public LocalTime getOraFineVisita() { return oraFineVisita; }
    public String getAgente() { return agente; }
    public String getFotoProfiloAgente() { return fotoProfiloAgente; }

    // =========================================================================
    // SETTER
    // =========================================================================
    public void setEmailClientePrenotatoVisita(String emailClientePrenotatoVisita) { this.emailClientePrenotatoVisita = emailClientePrenotatoVisita; }
    public void setTipoImmobile(TipoImmobile tipoImmobile) { this.tipoImmobile = tipoImmobile; }
    public void setViaImmobile(String viaImmobile) { this.viaImmobile = viaImmobile; }
    public void setComune(String comune) { this.comune = comune; }
    public void setNumeroCivico(String numeroCivico) { this.numeroCivico = numeroCivico; }
    public void setIdImmobile(int idImmobile) { this.idImmobile = idImmobile; }
    public void setDataVisita(LocalDate dataVisita) { this.dataVisita = dataVisita; }
    public void setOraInizioVisita(LocalTime oraInizioVisita) { this.oraInizioVisita = oraInizioVisita; }
    public void setOraFineVisita(LocalTime oraFineVisita) { this.oraFineVisita = oraFineVisita; }
    public void setAgente(String agente) { this.agente = agente; }
    public void setFotoProfiloAgente(String fotoProfiloAgente) { this.fotoProfiloAgente = fotoProfiloAgente; }

}