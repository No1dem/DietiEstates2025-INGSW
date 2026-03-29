package com.example.prova2.dto;

public class NotificaDTO {

    // =========================================================================
    // CAMPI (Proprietà)
    // =========================================================================
    private int id;
    private int numeroNotificheAccettate;
    private int numeroNotificheRifiutate;

    // =========================================================================
    // COSTRUTTORI
    // =========================================================================
    public NotificaDTO() {}

    // =========================================================================
    // GETTER
    // =========================================================================
    public int getId() { return id; }
    public int getNumeroNotificheAccettate() { return numeroNotificheAccettate; }
    public int getNumeroNotificheRifiutate() { return numeroNotificheRifiutate; }

    // =========================================================================
    // SETTER
    // =========================================================================
    public void setId(int id) { this.id = id; }
    public void setNumeroNotificheAccettate(int numeroNotificheAccettate) { this.numeroNotificheAccettate = numeroNotificheAccettate; }
    public void setNumeroNotificheRifiutate(int numeroNotificheRifiutate) { this.numeroNotificheRifiutate = numeroNotificheRifiutate; }

}