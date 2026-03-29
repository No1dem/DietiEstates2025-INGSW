package it.unina.dietiEstates25.model;

import lombok.Getter;

@Getter
public class Agenzia {
    private String nomeAgenzia;

    public Agenzia(String societa) {
        this.nomeAgenzia = societa;
    }

}
