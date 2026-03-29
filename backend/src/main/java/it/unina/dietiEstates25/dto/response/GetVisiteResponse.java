package it.unina.dietiEstates25.dto.response;

import java.time.LocalDate;
import java.time.LocalTime;

public class GetVisiteResponse {

    private String emailCliente;

    private LocalDate dataVisita;

    private LocalTime horaInizioVisita;

    private LocalTime horaFineVisita;

    private String descrizione;

    public LocalTime getHoraFineVisita() {
        return horaFineVisita;
    }

    public void setHoraFineVisita(LocalTime horaFineVisita) {
        this.horaFineVisita = horaFineVisita;
    }

    public GetVisiteResponse() {
    }

    public GetVisiteResponse(String emailCliente, LocalDate dataVisita,LocalTime horaVisita, String descrizione,LocalTime horaFineVisita) {
        this.emailCliente = emailCliente;
        this.dataVisita = dataVisita;
        this.horaInizioVisita = horaVisita;
        this.descrizione = descrizione;
        this.horaFineVisita = horaFineVisita;
    }


    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public void setHoraInizioVisita(LocalTime horaInizioVisita) {
        this.horaInizioVisita = horaInizioVisita;
    }

    public void setDataVisita(LocalDate dataVisita) {
        this.dataVisita = dataVisita;
    }

    public void setEmailCliente(String emailCliente) {
        this.emailCliente = emailCliente;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public LocalDate getDataVisita() {
        return dataVisita;
    }

    public LocalTime getHoraInizioVisita() {
        return horaInizioVisita;
    }

    public String getEmailCliente() {
        return emailCliente;
    }
}
