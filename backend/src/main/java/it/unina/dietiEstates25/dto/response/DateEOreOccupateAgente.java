package it.unina.dietiEstates25.dto.response;

import java.time.LocalDate;
import java.time.LocalTime;

public class DateEOreOccupateAgente {
    private LocalDate data;

    private LocalTime oraInizio;

    private LocalTime oraFine;

    public DateEOreOccupateAgente(LocalDate data, LocalTime oraInizio, LocalTime oraFine) {
        this.data = data;
        this.oraInizio = oraInizio;
        this.oraFine = oraFine;
    }

    public LocalDate getData() {
        return data;
    }

    public LocalTime getOraFine() {
        return oraFine;
    }

    public LocalTime getOraInizio() {
        return oraInizio;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public void setOraFine(LocalTime oraFine) {
        this.oraFine = oraFine;
    }

    public void setOraInizio(LocalTime oraInizio) {
        this.oraInizio = oraInizio;
    }
}
