package it.unina.dietiEstates25.dto.request;
import it.unina.dietiEstates25.model.Arredamento;
import it.unina.dietiEstates25.model.ClasseEnergetica;
import it.unina.dietiEstates25.model.TipoImmobile;
import it.unina.dietiEstates25.model.TipoVendita;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.Locale;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor
@Setter
@Getter
public class AddImmobileRequestDTO {

    @NotNull(message = "Devi inserire il tipo dell'immobile")
    private TipoImmobile tipoimmobile;

    @NotNull(message = "Devi inserire il tipo di vendita che si desidera effettuare!")
    private TipoVendita tipovendita;

    @NotNull(message = "Devi inserire una descrizione!")
    @NotEmpty(message = "Non puoi inserire una descrizione vuota")
    @Size(min = 1,message = "Inserisci una descrizione minima")
    @Size(max = 500)
    private String descrizione;

    @NotNull(message = "La deve essere inserita!")
    @NotEmpty(message = "Devi inserire una via")

    private String via;

    @NotNull(message = "Inserisci il numero di stanze")
    @Min(value = 1,message = "Inserisci un numero minimo di stanze")
    @Max(value = 10000)
    private int numeroStanze;

    @NotNull(message = "Inserisci il piano")
    @Min(value = 0,message = "Inserire almeno un piano")
    @Max(value = 10000)
    private int piano;

    @NotNull(message = "Inserire la classe energetica")
    private ClasseEnergetica classeEnergetica;

    @NotNull(message = "Inserire la superficie")
    @Min(value = 1,message = "Inserire una superficie valida")
    @Max(value = 201324)
    private int superficie;

    @NotNull(message = "Inserire arredamento")
    private Arredamento arredamento;

    private @NotNull(message = "Inserire il prezzo dell'immobile")
    @Min(value = 1, message = "Inserire un prezzo minimo ")
    @Max(value = 2029923930) BigDecimal prezzo;

    private @NotNull(message = "Inserire le spese condominiali")
    @Min(value = 1, message = "Inserire un numero minimo di spese condominiali")
    @Max(value = 2029923930) BigDecimal speseCondominiali;



    @NotNull(message = "Inserire il numero di bagni")
    @Min(value = 0,message = "Inserire un numero minimo di bagni")
    @Max(value = 202992393)
    private int numeroBagni;

    @NotNull(message = "Inserire il numero di cucine")
    @Min(value = 0,message = "Inserire un numero minimo di cucine")
    @Max(value = 202992393)
    private int numeroCucine;

    @NotNull(message = "Inserire il numero di soggiorni")
    @Min(value = 0,message = "Inserire un numero minimo di soggiorni")
    @Max(value = 202992393)
    private int numeroSoggiorni;

    @NotNull(message = "Inserisci un agente di riferimento")
    @NotEmpty(message = "L'agente di riferimento non può essere vuoto")
    @Size(min = 16,max = 16,message = "Inserisci un codice fiscale valido")
    private String cfAgente;

    @NotNull(message = "Inserisci un titolo")
    @NotEmpty(message = "Inserisci un titolo non vuoto")
    private String titolo;

    @DecimalMin(value = "-180.0",message = "la longitudine deve essere maggiore di '-180.0' ")
    @DecimalMax(value = "180.0",message = "la longitudine deve essere minore di '180.0' ")
    private double longitudine;

    @DecimalMin(value = "-90.0",message = "La latitudine deve essere maggiore di '-90.0' ")
    @DecimalMax(value = "90.0",message = "La latitudine deve essere minore di '90.0' ")
    private double latitudine;

    @NotNull(message = "Inserisci la citta")
    @NotEmpty(message = "Inserisci una citta non vuota!")
    @Size(min = 2,max = 50,message = "Inserisci una citta valida!")
    private String citta;


    @Setter
    @Getter
    @NotNull(message = "Inserire il comune!")
    @NotEmpty(message = "Inserire un comune non vuoto!")
    @Size(min = 1,max = 134,message = "Inserire un comune valido!")
    private String comune;


    @NotNull(message = "Inserisci un numero Civico")
    @NotEmpty(message = "Inserisci un numero civico non vuoto")
    @Size(min = 1,max = 50,message = "Inserisci un numero civico valido!")
    private String numeroCivico;


    private List<String> fotoDelImmobile;

    @NotNull
    private boolean ascensore;


    public String formattaPrezzo(BigDecimal prezzo) {
        DecimalFormatSymbols simboli = new DecimalFormatSymbols(Locale.ITALY);
        DecimalFormat formato = new DecimalFormat("#,###.##", simboli);
        return formato.format(prezzo);
    }


}
