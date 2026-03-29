package it.unina.dietiEstates25.dto.response;

import it.unina.dietiEstates25.model.*;
import java.time.LocalDate;
import java.time.LocalTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class BaseVisitaDTO {
    protected String emailClientePrenotatoVisita;
    protected TipoImmobile tipoImmobile;
    protected String viaImmobile;
    protected String comune;
    protected String numeroCivico;
    protected int idImmobile;
    protected LocalDate dataVisita;
    protected LocalTime oraInizioVisita;
    protected LocalTime oraFineVisita;
    protected String agente;
    protected String fotoProfiloAgente;
}
