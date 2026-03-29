package com.example.prova2.model;

import com.example.prova2.dto.DatiDTO;

import java.util.ArrayList;

public class Agente extends Utente {
    private String partitaIva;

    private ArrayList<Recensione> recensioniAgente;

    private ArrayList<Notifica> notificheAgente;

    private ArrayList<Immobile> immobiliCaricati;

    private ArrayList<Visita> visiteAgente;

    private Account accountAgente;

    private GestoreAgenziaImmobiliare gestoreRiferimento;

    private Foto fotoProfilo;

    private String bio;

    public Agente() {}

    public Agente(String email,String token){
        accountAgente = new Account(email);
        this.setToken(token);
    }


    public Foto getFotoProfilo() {
        return fotoProfilo;
    }


    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public Agente(String nome,String cognome,String telefono){
        this.nome = nome;
        this.cognome = cognome;
        this.setTelefono(telefono);
    }

    public Agente(String nome, String cognome, String cf, String telefono, String indirizzo, String numeroCivico) {
        super(nome, cognome, cf, telefono, indirizzo,numeroCivico);
    }

    public Agente(Account a){
        this.accountAgente = a;
    }

    public Agente(DatiDTO dto){
        this.setCf(dto.getCf());
        this.nome=dto.getNome();
        this.cognome=dto.getCognome();
    }

    public void setDto(DatiDTO dto){
        this.setCf(dto.getCf());
        this.nome=dto.getNome();
        this.cognome=dto.getCognome();
        this.setTelefono(dto.getTelefono());
        this.bio=dto.getBio();
    }

    public Agente(String nome, String cognome, String cf, String telefono) {
        this.nome = nome;
        this.cognome = cognome;
        this.setCf(cf);
        this.setTelefono(telefono);
    }

    public Account getAccountAgente() {
        return accountAgente;
    }

    public void setAccountAgente(Account accountAgente) {
        this.accountAgente = accountAgente;
    }

    public String getPartitaIva() {
        return partitaIva;
    }

    public void setPartitaIva(String partitaIva) {
        this.partitaIva = partitaIva;
    }

    public GestoreAgenziaImmobiliare getGestoreRiferimento() {
        return gestoreRiferimento;
    }

    public void setGestoreRiferimento(GestoreAgenziaImmobiliare gestoreRiferimento) {
        this.gestoreRiferimento = gestoreRiferimento;
    }

    public void setFotoProfilo(String s) {
        Foto foto = new Foto(s);
        this.fotoProfilo = foto;
    }
}
