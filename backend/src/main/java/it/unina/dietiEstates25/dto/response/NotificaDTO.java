package it.unina.dietiEstates25.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class NotificaDTO {
    @JsonProperty("id")
    private int id;

    @JsonProperty("numeroNotAcc")
    private int numeroNotificheAccettate;


    @JsonProperty("numeroNotRif")
    private int numeroNotificheRifiutate;


    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setNumeroNotificheAccettate(int numeroNotificheAccettate) {
        this.numeroNotificheAccettate = numeroNotificheAccettate;
    }

    public int getNumeroNotificheAccettate() {
        return numeroNotificheAccettate;
    }

    public void setNumeroNotificheRifiutate(int numeroNotificheRifiutate) {
        this.numeroNotificheRifiutate = numeroNotificheRifiutate;
    }

    public int getNumeroNotificheRifiutate() {
        return numeroNotificheRifiutate;
    }
}
