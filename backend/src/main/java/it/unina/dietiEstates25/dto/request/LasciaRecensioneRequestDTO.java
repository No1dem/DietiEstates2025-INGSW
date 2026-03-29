package it.unina.dietiEstates25.dto.request;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LasciaRecensioneRequestDTO {
    @NotNull(message = "Inserisci un agente")
    @NotEmpty(message = "Inserisci un agente")
    @Size(min = 16,max = 16,message = "Inserisci il codice fiscale dell'agente")
    private String agenteDaRecensire;


    @Min(value = 1,message = "La valutazione deve essere compresa fra 1 e 5")
    @Max(value = 5,message = "La valutazione deve essere compresa fra 1 e 5")
    private int valutazione;

}
