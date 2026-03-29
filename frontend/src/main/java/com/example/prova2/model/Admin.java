package com.example.prova2.model;

import java.util.ArrayList;

public class Admin extends GestoreAgenziaImmobiliare {
    private ArrayList<GestoreAgenziaImmobiliare> gestoriCreati;



    public Admin(String nome, String cognome, String cf){
        this.nome=nome;
        this.cognome=cognome;
        this.setCf(cf);
    }


    @Override
    public String getCf() {
        return super.getCf();
    }

    @Override
    public void setCf(String cf) {
        super.setCf(cf);
    }
}
