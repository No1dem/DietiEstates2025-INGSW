package it.unina.dietiEstates25.dto.response;

import it.unina.dietiEstates25.model.Arredamento;
import it.unina.dietiEstates25.model.ClasseEnergetica;
import it.unina.dietiEstates25.model.TipoImmobile;
import it.unina.dietiEstates25.model.TipoVendita;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.text.DecimalFormat;
import java.util.List;

@Setter
@Getter
@Builder
public class ImmobileResponseRicercaDTO {

    private int idImmobile;
    private TipoImmobile tipoimmobile;
    private TipoVendita tipovendita;
    private String descrizione;
    private String via;
    private int numeroStanze;
    private int piano;
    private ClasseEnergetica classeEnergetica;
    private int superficie;
    private Arredamento arredamento;
    private String prezzo;
    private String speseCondominiali;
    private int numeroBagni;
    private int numeroCucine;
    private int numeroSoggiorni;
    private String cfAgente;
    private double latitudine;
    private double longitudine;
    private String numeroCivico;
    private String comune;
    private String citta;
    private String titolo;
    private List<String> uriFotoImmobile;
    private String nomeAgente;
    private String cognomeAgente;
    private boolean ascensore;

    public static String formattaStringa(String input) {
        try {
            double number = Double.parseDouble(input);
            long roundedNumber = Math.round(number);
            DecimalFormat formatter = new DecimalFormat("#,###");
            return formatter.format(roundedNumber);
        } catch (NumberFormatException e) {
            return input;
        }
    }
}
