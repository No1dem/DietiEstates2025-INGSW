package com.example.prova2.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public class AddVisitaRequestDTO {

    // =========================================================================
    // CAMPI (Proprietà)
    // =========================================================================
    
    private LocalDate dataVisita;
    private LocalTime horaInizioVisita;
    private LocalTime horaFineVisita;
    private String descrizione;
    private String emailClienteChePrenotaVisita;
    private String token;
    private String cfAgente;
    private int immobileDaVisitare;

    // =========================================================================
    // COSTRUTTORE
    // =========================================================================

    public AddVisitaRequestDTO(LocalDate dataVisita, LocalTime horaInizioVisita, LocalTime horaFineVisita, String des,
                               String emailCliente, String cfAgente, int idImmobile, String token) {
        this.dataVisita = dataVisita;
        this.horaInizioVisita = horaInizioVisita;
        this.horaFineVisita = horaFineVisita;
        this.descrizione = des;
        this.emailClienteChePrenotaVisita = emailCliente;
        this.cfAgente = cfAgente;
        this.immobileDaVisitare = idImmobile;
        this.token = token;
    }

    // =========================================================================
    // GETTER
    // =========================================================================

    public LocalDate getDataVisita() { return dataVisita; }
    public LocalTime getHoraInizioVisita() { return horaInizioVisita; }
    public LocalTime getHoraFineVisita() { return horaFineVisita; }
    public String getDescrizione() { return descrizione; }
    public String getEmailClienteChePrenotaVisita() { return emailClienteChePrenotaVisita; }
    public String getToken() { return token; }
    public String getCfAgente() { return cfAgente; }
    public int getImmobileDaVisitare() { return immobileDaVisitare; }

    // =========================================================================
    // SETTER
    // =========================================================================

    public void setDataVisita(LocalDate dataVisita) { this.dataVisita = dataVisita; }
    public void setHoraInizioVisita(LocalTime horaInizioVisita) { this.horaInizioVisita = horaInizioVisita; }
    public void setHoraFineVisita(LocalTime horaFineVisita) { this.horaFineVisita = horaFineVisita; }
    public void setDescrizione(String descrizione) { this.descrizione = descrizione; }
    public void setEmailClienteChePrenotaVisita(String emailClienteChePrenotaVisita) { this.emailClienteChePrenotaVisita = emailClienteChePrenotaVisita; }
    public void setToken(String token) { this.token = token; }
    public void setCfAgente(String cfAgente) { this.cfAgente = cfAgente; }
    public void setImmobileDaVisitare(int immobileDaVisitare) { this.immobileDaVisitare = immobileDaVisitare; }

}