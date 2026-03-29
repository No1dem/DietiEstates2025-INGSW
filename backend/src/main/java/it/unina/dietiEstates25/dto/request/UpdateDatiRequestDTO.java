package it.unina.dietiEstates25.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class UpdateDatiRequestDTO {

    @NotNull(message = "Inserire un nome!")
    @NotEmpty(message = "Il nome non può essere vuoto!")
    @Size(min = 2,max = 1000,message = "Inserisci un nome valido (minimo 2 caratteri)")
    private String nome;

    @NotNull(message = "Inserire un cognome!")
    @NotEmpty(message = "Il cognome non può essere vuoto")
    @Size(min = 2,max = 1000,message = "Inserisci un cognome valido (minimo 2 caratteri)")
    private String cognome;

    @NotNull
    private String telefono;


    @NotNull(message = "La email deve essere inserita")
    @NotEmpty(message = "la email non può essere vuota")
    @Size(min=2,message = "L'email deve essere valida!")
    private String email;

}
