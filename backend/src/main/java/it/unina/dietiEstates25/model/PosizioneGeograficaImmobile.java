package it.unina.dietiEstates25.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PosizioneGeograficaImmobile {
    private String via;
    private String comune;
    private String numeroCivico;
    private String citta;
    private int longitudine;
    private int latitudine;


}
