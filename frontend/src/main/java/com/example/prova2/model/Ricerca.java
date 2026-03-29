package com.example.prova2.model;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;

public class Ricerca {

    private String titoloRicerca;

    private Date dataRicerca;

    private Time horaRicerca;

    private BigDecimal prezzoMinimo;

    private BigDecimal prezzoMaximo;

    private Integer numeroStanze;

    private String localitaRicercata;

    private String viaRicercata;

    private ArrayList<Utente> utentiCheHannoFattoRicerca;

    private String comune;

    private ClasseEnergetica classeEnergetica;

    private TipoVendita tipoVendita;

    private int sessioneUtente;

    public int getSessioneUtente() {
        return sessioneUtente;
    }

    public void setSessioneUtente(int sessioneUtente) {
        this.sessioneUtente = sessioneUtente;
    }

    public Ricerca(ClasseEnergetica classeEnergetica, TipoVendita tipoVendita, String comune, Integer numeroStanze, BigDecimal prezzoMinimo, BigDecimal prezzoMaximo) {
        this.classeEnergetica = classeEnergetica;
        this.tipoVendita = tipoVendita;
        this.comune = comune;
        this.numeroStanze = numeroStanze;
        this.prezzoMinimo = prezzoMinimo;
        this.prezzoMaximo = prezzoMaximo;
    }

    public String getComune() {
        return comune;
    }

    public ClasseEnergetica getClasseEnergetica() {
        return classeEnergetica;
    }

    public TipoVendita getTipoVendita() {
        return tipoVendita;
    }

    public Integer getNumeroStanze() {
        return numeroStanze;
    }

    public BigDecimal getPrezzoMaximo() {
        return prezzoMaximo;
    }

    public BigDecimal getPrezzoMinimo() {
        return prezzoMinimo;
    }


}
