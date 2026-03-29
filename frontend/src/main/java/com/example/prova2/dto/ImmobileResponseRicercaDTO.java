package com.example.prova2.dto;

import java.text.DecimalFormat;
import java.util.List;

import com.example.prova2.model.Arredamento;
import com.example.prova2.model.ClasseEnergetica;
import com.example.prova2.model.TipoImmobile;
import com.example.prova2.model.TipoVendita;

public class ImmobileResponseRicercaDTO {

    // =========================================================================
    // CAMPI (Proprietà)
    // =========================================================================
    private int idImmobile;
    private TipoImmobile tipoimmobile;
    private TipoVendita tipovendita;
    private String descrizione;
    private String via;
    private int numeroStanze;
    private int piano;
    private ClasseEnergetica classeEnergetica;
    private int superficie;
    private Arredamento arredamento;
    private String prezzo;
    private String speseCondominiali;
    private int numeroBagni;
    private int numeroCucine;
    private int numeroSoggiorni;
    private String cfAgente;
    private double latitudine;
    private double longitudine;
    private String numeroCivico;
    private String comune;
    private String citta;
    private String titolo;
    private List<String> uriFotoImmobile;
    private String nomeAgente;
    private String cognomeAgente;
    private boolean ascensore;

    // =========================================================================
    // COSTRUTTORI
    // =========================================================================
    public ImmobileResponseRicercaDTO() {}

    private ImmobileResponseRicercaDTO(Builder builder) {
        this.idImmobile = builder.idImmobile;
        this.tipoimmobile = builder.tipoimmobile;
        this.tipovendita = builder.tipovendita;
        this.descrizione = builder.descrizione;
        this.via = builder.via;
        this.numeroStanze = builder.numeroStanze;
        this.piano = builder.piano;
        this.classeEnergetica = builder.classeEnergetica;
        this.superficie = builder.superficie;
        this.arredamento = builder.arredamento;
        this.prezzo = builder.prezzo;
        this.speseCondominiali = builder.speseCondominiali;
        this.numeroBagni = builder.numeroBagni;
        this.numeroCucine = builder.numeroCucine;
        this.numeroSoggiorni = builder.numeroSoggiorni;
        this.cfAgente = builder.cfAgente;
        this.latitudine = builder.latitudine;
        this.longitudine = builder.longitudine;
        this.numeroCivico = builder.numeroCivico;
        this.comune = builder.comune;
        this.citta = builder.citta;
        this.titolo = builder.titolo;
        this.nomeAgente = builder.nomeAgente;
        this.cognomeAgente = builder.cognomeAgente;
        // uriFotoImmobile e ascensore non sono popolati dal builder in base al tuo design
    }

    // =========================================================================
    // BUILDER
    // =========================================================================
    public static class Builder {
        private int idImmobile;
        private TipoImmobile tipoimmobile;
        private TipoVendita tipovendita;
        private String descrizione;
        private String via;
        private int numeroStanze;
        private int piano;
        private ClasseEnergetica classeEnergetica;
        private int superficie;
        private Arredamento arredamento;
        private String prezzo;
        private String speseCondominiali;
        private int numeroBagni;
        private int numeroCucine;
        private int numeroSoggiorni;
        private String cfAgente;
        private double latitudine;
        private double longitudine;
        private String numeroCivico;
        private String comune;
        private String citta;
        private String titolo;
        private String cognomeAgente;
        private String nomeAgente;

        public Builder idImmobile(int idImmobile) { this.idImmobile = idImmobile; return this; }
        public Builder nomeAgente(String nomeAgente) { this.nomeAgente = nomeAgente; return this; }
        public Builder cognomeAgente(String cognomeAgente) { this.cognomeAgente = cognomeAgente; return this; }
        public Builder tipoimmobile(TipoImmobile tipoimmobile) { this.tipoimmobile = tipoimmobile; return this; }
        public Builder tipovendita(TipoVendita tipovendita) { this.tipovendita = tipovendita; return this; }
        public Builder descrizione(String descrizione) { this.descrizione = descrizione; return this; }
        public Builder via(String via) { this.via = via; return this; }
        public Builder numeroStanze(int numeroStanze) { this.numeroStanze = numeroStanze; return this; }
        public Builder piano(int piano) { this.piano = piano; return this; }
        public Builder classeEnergetica(ClasseEnergetica classeEnergetica) { this.classeEnergetica = classeEnergetica; return this; }
        public Builder superficie(int superficie) { this.superficie = superficie; return this; }
        public Builder arredamento(Arredamento arredamento) { this.arredamento = arredamento; return this; }
        public Builder prezzo(String prezzo) { this.prezzo = prezzo; return this; }
        public Builder speseCondominiali(String speseCondominiali) { this.speseCondominiali = speseCondominiali; return this; }
        public Builder numeroBagni(int numeroBagni) { this.numeroBagni = numeroBagni; return this; }
        public Builder numeroCucine(int numeroCucine) { this.numeroCucine = numeroCucine; return this; }
        public Builder numeroSoggiorni(int numeroSoggiorni) { this.numeroSoggiorni = numeroSoggiorni; return this; }
        public Builder cfAgente(String cfAgente) { this.cfAgente = cfAgente; return this; }
        public Builder latitudine(double latitudine) { this.latitudine = latitudine; return this; }
        public Builder longitudine(double longitudine) { this.longitudine = longitudine; return this; }
        public Builder numeroCivico(String numeroCivico) { this.numeroCivico = numeroCivico; return this; }
        public Builder comune(String comune) { this.comune = comune; return this; }
        public Builder citta(String citta) { this.citta = citta; return this; }
        public Builder titolo(String titolo) { this.titolo = titolo; return this; }

        public String getNomeAgente() { return nomeAgente; }
        public void setNomeAgente(String nomeAgente) { this.nomeAgente = nomeAgente; }

        public ImmobileResponseRicercaDTO build() {
            return new ImmobileResponseRicercaDTO(this);
        }
    }

    // =========================================================================
    // GETTER
    // =========================================================================
    public int getIdImmobile() { return idImmobile; }
    public TipoImmobile getTipoimmobile() { return tipoimmobile; }
    public TipoVendita getTipovendita() { return tipovendita; }
    public String getDescrizione() { return descrizione; }
    public String getVia() { return via; }
    public int getNumeroStanze() { return numeroStanze; }
    public int getPiano() { return piano; }
    public ClasseEnergetica getClasseEnergetica() { return classeEnergetica; }
    public int getSuperficie() { return superficie; }
    public Arredamento getArredamento() { return arredamento; }
    public String getPrezzo() { return prezzo; }
    public String getSpeseCondominiali() { return speseCondominiali; }
    public int getNumeroBagni() { return numeroBagni; }
    public int getNumeroCucine() { return numeroCucine; }
    public int getNumeroSoggiorni() { return numeroSoggiorni; }
    public String getCfAgente() { return cfAgente; }
    public double getLatitudine() { return latitudine; }
    public double getLongitudine() { return longitudine; }
    public String getNumeroCivico() { return numeroCivico; }
    public String getComune() { return comune; }
    public String getCitta() { return citta; }
    public String getTitolo() { return titolo; }
    public List<String> getUriFotoImmobile() { return uriFotoImmobile; }
    public String getNomeAgente() { return nomeAgente; }
    public String getCognomeAgente() { return cognomeAgente; }
    public boolean isAscensore() { return ascensore; }

    // =========================================================================
    // SETTER
    // =========================================================================
    public void setIdImmobile(int idImmobile) { this.idImmobile = idImmobile; }
    public void setTipoimmobile(TipoImmobile tipoimmobile) { this.tipoimmobile = tipoimmobile; }
    public void setTipovendita(TipoVendita tipovendita) { this.tipovendita = tipovendita; }
    public void setDescrizione(String descrizione) { this.descrizione = descrizione; }
    public void setVia(String via) { this.via = via; }
    public void setNumeroStanze(int numeroStanze) { this.numeroStanze = numeroStanze; }
    public void setPiano(int piano) { this.piano = piano; }
    public void setClasseEnergetica(ClasseEnergetica classeEnergetica) { this.classeEnergetica = classeEnergetica; }
    public void setSuperficie(int superficie) { this.superficie = superficie; }
    public void setArredamento(Arredamento arredamento) { this.arredamento = arredamento; }
    public void setPrezzo(String prezzo) { this.prezzo = prezzo; }
    public void setSpeseCondominiali(String speseCondominiali) { this.speseCondominiali = speseCondominiali; }
    public void setNumeroBagni(int numeroBagni) { this.numeroBagni = numeroBagni; }
    public void setNumeroCucine(int numeroCucine) { this.numeroCucine = numeroCucine; }
    public void setNumeroSoggiorni(int numeroSoggiorni) { this.numeroSoggiorni = numeroSoggiorni; }
    public void setCfAgente(String cfAgente) { this.cfAgente = cfAgente; }
    public void setLatitudine(double latitudine) { this.latitudine = latitudine; }
    public void setLongitudine(double longitudine) { this.longitudine = longitudine; }
    public void setNumeroCivico(String numeroCivico) { this.numeroCivico = numeroCivico; }
    public void setComune(String comune) { this.comune = comune; }
    public void setCitta(String citta) { this.citta = citta; }
    public void setTitolo(String titolo) { this.titolo = titolo; }
    public void setUriFotoImmobile(List<String> uriFotoImmobile) { this.uriFotoImmobile = uriFotoImmobile; }
    public void setNomeAgente(String nomeAgente) { this.nomeAgente = nomeAgente; }
    public void setCognomeAgente(String cognomeAgente) { this.cognomeAgente = cognomeAgente; }
    public void setAscensore(boolean ascensore) { this.ascensore = ascensore; }

    // =========================================================================
    // UTILITY METHODS
    // =========================================================================
    public String formattaStringa(String input) {
        long number = Long.parseLong(input);
        DecimalFormat formatter = new DecimalFormat("#,###");
        return formatter.format(number);
    }
}