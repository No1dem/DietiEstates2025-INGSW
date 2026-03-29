package it.unina.dietiEstates25.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
public class DatiImmobileDTO {
    @Setter
    private String indirizzo;

    private String comune;

    private String citta;

    private String agente;

    private String numeroCivico;

    @Setter
    private String via;

    public DatiImmobileDTO(String indirizzo,String comune,String agente,String citta,String numeroCivico) {
        this.indirizzo = indirizzo;
        this.comune = comune;
        this.agente = agente;
        this.citta =citta;
        this.numeroCivico = numeroCivico;
    }

}
