package com.example.prova2.model;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;

public class Cliente extends Utente {

    public Cliente(String nome, String cognome, String cf, String telefono, String indirizzo) {
        super(nome, cognome, cf, telefono, indirizzo);
    }

    public Cliente(String token,String email) {
        this.setToken(token);
        this.setAccountAgente(new Account(email));
    }

    private Date dataRegistrazione;

    private Time horaRegistrazione;

    private ArrayList<Recensione> recensioniLasciate;

    private ArrayList<Visita> visitePrenotate;

    private Account accountAgente;

    public Cliente() {}

    public Cliente(String text, String text1, String text2, String text3) {
        this.nome = text;
        this.cognome = text1;
        this.accountAgente = new Account(text2);
        this.setTelefono(text3);
    }

    @Override
    public Account getAccountAgente() {
        return accountAgente;
    }

    @Override
    public void setAccountAgente(Account accountAgente) {
        this.accountAgente = accountAgente;
    }


    public Cliente(String email){
        accountAgente = new Account(email);
    }

    public Cliente(String text, String text1, Account accountSemplice) {
        this.nome = text;
        this.cognome = text1;
        this.accountAgente = accountSemplice;
    }

    public Cliente(String trim, String trim1, Account accountSemplice, String text) {
        this.nome=trim;
        this.cognome=trim1;
        this.accountAgente = accountSemplice;
        this.setTelefono(text);
    }

    public Cliente(String nome,String cognome,String email){
        this.nome=nome;
        this.cognome=cognome;
        this.setAccountAgente(new Account(email));
    }

}
