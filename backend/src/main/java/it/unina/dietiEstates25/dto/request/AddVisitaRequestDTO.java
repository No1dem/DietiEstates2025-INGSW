package it.unina.dietiEstates25.dto.request;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;


@Getter
@Setter
@Data
@NoArgsConstructor
public class AddVisitaRequestDTO {
    @NotNull(message = "Devi inserire una data per visitare immobile")
    private LocalDate dataVisita;

    @NotNull(message = "Devi inserire una ora per la visita dell'immobile")
    private LocalTime horaInizioVisita;

    @NotNull(message = "Devi inserire un'ora per la fine della visita")
    private LocalTime horaFineVisita;

    @NotNull(message = "Inserisci una descrizione per l'agente")
    @NotEmpty(message = "Non inserire una descrizione!")
    private String descrizione;

    @NotNull(message = "Inserisci un cliente")
    @NotEmpty(message = "Non inserire un cliente vuoto")
    @Size(min = 5,message = "Inserisci un cliente(email) valido!")
    private String emailClienteChePrenotaVisita;

    @NotNull(message = "Inserisci un agente")
    @NotEmpty(message = "Inserisci un agente valido!")
    @Size(min = 16,max = 16,message = "Inserisci un agente(cf) valido")
    private String cfAgente;

    @NotNull(message = "Inserisci un immobile valido(intero che identifica immobile)")

    private int immobileDaVisitare;
}
