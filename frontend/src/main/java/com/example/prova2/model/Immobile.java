package com.example.prova2.model;

import com.example.prova2.dto.DatiImmobileDTO;
import com.example.prova2.dto.GetNotificheDTO;
import com.example.prova2.dto.ImmobileResponseRicercaDTO;

public class Immobile {
    private int id_immobile;

    private Double longitudine;

    private Double latitudine;

    private String comune;

    private String citta;

    private String numeroCivico;

    private String titolo;

    private String agenteProprietario;

    private String nomeAgente;

    private String cognomeAgente;

    private String tipologiaImmobile;

    private String viaImmobile;

    public String getViaImmobile() {
        return viaImmobile;
    }

    public void setViaImmobile(String viaImmobile) {
        this.viaImmobile = viaImmobile;
    }

    public Immobile() {}

    public Immobile(String titolo, String tipologia, String citta) {
        this.titolo = titolo;
        this.citta = citta;
        this.tipologiaImmobile = tipologia;
    }

    public String getCitta() {
        return citta;
    }
    public void setCitta(String citta) {
        this.citta = citta;
    }
    public String getTipologiaImmobile() {
        return tipologiaImmobile;
    }
    public void setTipologiaImmobile(String tipologiaImmobile) {
        this.tipologiaImmobile = tipologiaImmobile;

    }

    public String getTitolo() {
        return titolo;
    }
    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }

    public Double getLongitudine() {
        return longitudine;
    }

    public Double getLatitudine() {
        return latitudine;
    }

    public void setLongitudine(Double longitudine) {
        this.longitudine = longitudine;
    }

    public void setLatitudine(Double latitudine) {
        this.latitudine = latitudine;
    }

    public Immobile(ImmobileResponseRicercaDTO dto){
        this.id_immobile=dto.getIdImmobile();
        this.agenteProprietario=dto.getCfAgente();
        this.latitudine=dto.getLatitudine();
        this.longitudine=dto.getLongitudine();
        this.comune=dto.getComune();
        this.viaImmobile=dto.getVia();
        this.numeroCivico=dto.getNumeroCivico();
        this.nomeAgente=dto.getNomeAgente();
        this.cognomeAgente=dto.getCognomeAgente();
    }

    public Immobile(DatiImmobileDTO dto, GetNotificheDTO notifica, Double lat, Double lon){
        this.id_immobile=notifica.getIdImmobile();
        this.viaImmobile=dto.getIndirizzo();
        this.numeroCivico=dto.getNumeroCivico();
        this.latitudine=lat;
        this.longitudine=lon;
        this.comune=dto.getComune();
        this.agenteProprietario=dto.getAgente();

    }

    public String getNomeAgente() {
        return nomeAgente;
    }

    public String getCognomeAgente() {
        return cognomeAgente;
    }

    public String getComune() {
        return comune;
    }

    public String getNumeroCivico() {
        return numeroCivico;
    }

    public void setNumeroCivico(String numeroCivico) {
        this.numeroCivico = numeroCivico;
    }

    public void setComune(String comune) {
        this.comune = comune;
    }

    public int getId_immobile() {
        return id_immobile;
    }

    public String getAgenteProprietario() {
        return agenteProprietario;
    }
}
