package com.example.prova2.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public class GetVisiteResponse {

    // =========================================================================
    // CAMPI (Proprietà)
    // =========================================================================
    private String emailCliente;
    private LocalDate dataVisita;
    private LocalTime horaInizioVisita;
    private LocalTime horaFineVisita;
    private String descrizione;

    // =========================================================================
    // COSTRUTTORI
    // =========================================================================
    public GetVisiteResponse() {
    }

    public GetVisiteResponse(String emailCliente, LocalDate dataVisita, LocalTime horaVisita, String descrizione, LocalTime horaFineVisita) {
        this.emailCliente = emailCliente;
        this.dataVisita = dataVisita;
        this.horaInizioVisita = horaVisita;
        this.descrizione = descrizione;
        this.horaFineVisita = horaFineVisita;
    }

    // =========================================================================
    // GETTER
    // =========================================================================
    public String getEmailCliente() { return emailCliente; }
    public LocalDate getDataVisita() { return dataVisita; }
    public LocalTime getHoraInizioVisita() { return horaInizioVisita; }
    public LocalTime getHoraFineVisita() { return horaFineVisita; }
    public String getDescrizione() { return descrizione; }

    // =========================================================================
    // SETTER
    // =========================================================================
    public void setEmailCliente(String emailCliente) { this.emailCliente = emailCliente; }
    public void setDataVisita(LocalDate dataVisita) { this.dataVisita = dataVisita; }
    public void setHoraInizioVisita(LocalTime horaInizioVisita) { this.horaInizioVisita = horaInizioVisita; }
    public void setHoraFineVisita(LocalTime horaFineVisita) { this.horaFineVisita = horaFineVisita; }
    public void setDescrizione(String descrizione) { this.descrizione = descrizione; }

}