package com.example.prova2.model;

import com.example.prova2.dto.GestoreAgenziaImmobiliareDatiDTO;

public class GestoreAgenziaImmobiliare extends Utente {
    public String partitaIva;

    public Agenzia agenziaAppartenente;

    private GestoreAgenziaImmobiliare adminAppartenente;

    private Account accountAgente;

    private boolean isAdmin;


    public void setAgenziaAppartenente(Agenzia agenziaAppartenente) {
        this.agenziaAppartenente = agenziaAppartenente;
    }

    public GestoreAgenziaImmobiliare(String username, String password){
        this.getAccountAgente().setUsername(username);
        this.getAccountAgente().setPassword(password);
    }


    public GestoreAgenziaImmobiliare(boolean isAdmin){
        this.isAdmin = isAdmin;
    }

    public GestoreAgenziaImmobiliare(String nome, String cognome,String cf,String numeroTelefono){
        this.nome=nome;
        this.cognome=cognome;
        this.setCf(cf);
        this.setTelefono(numeroTelefono);
    }

    public GestoreAgenziaImmobiliare(String nome, String cognome,String numeroTelefono){
        this.nome=nome;
        this.cognome=cognome;
        this.setTelefono(numeroTelefono);
    }




    public GestoreAgenziaImmobiliare(String nome, String cognome, String cf, String telefono,String via,String numeroCivico) {
        super(nome, cognome, cf, telefono, via, numeroCivico);
    }

    public GestoreAgenziaImmobiliare(Agenzia societa, String cf) {
        this.agenziaAppartenente = societa;
        this.setCf(cf);
    }

    public void setAdminAppartenente(GestoreAgenziaImmobiliare adminAppartenente) {
        this.adminAppartenente = adminAppartenente;
    }

    public GestoreAgenziaImmobiliare getAdminAppartenente() {
        return adminAppartenente;
    }

    public GestoreAgenziaImmobiliare() {
    }

    public Agenzia getAgenziaAppartenente() {
        return agenziaAppartenente;
    }

    public String getPartitaIva() {
        return partitaIva;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAccountAgente(Account accountAgente) {
        this.accountAgente = accountAgente;
    }

    public Account getAccountAgente() {
        return accountAgente;
    }

    public void setDto(GestoreAgenziaImmobiliareDatiDTO gestoreData) {
        Account account = new Account(gestoreData.getEmail());
        this.setAccountAgente(account);
        this.nome = gestoreData.getNome();
        this.cognome = gestoreData.getCognome();
        this.setTelefono(gestoreData.getTelefono());
        this.setCf(gestoreData.getCf());
    }
}
