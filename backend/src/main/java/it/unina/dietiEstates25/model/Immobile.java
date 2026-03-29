package it.unina.dietiEstates25.model;

import lombok.Setter;


@Setter
public class Immobile {
    private String descrizioneImmobile;


    private PosizioneGeograficaImmobile posizioneGeografica = new PosizioneGeograficaImmobile();
    private DettagliVenditaImmobile dettagliVendita = new DettagliVenditaImmobile();
    private String titolo;

    public Immobile(String titolo, TipoImmobile tipologia, String citta) {
        this.titolo = titolo;
        this.dettagliVendita.setTipoImmobile(tipologia);
        this.posizioneGeografica.setCitta(citta);
    }



}
