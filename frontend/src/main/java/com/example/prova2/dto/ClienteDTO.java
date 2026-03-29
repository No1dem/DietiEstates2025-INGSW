package com.example.prova2.dto;

public class ClienteDTO {

    // =========================================================================
    // CAMPI (Proprietà)
    // =========================================================================
    
    private String token;
    private boolean duplicato;
    private boolean erroreInterno;

    // =========================================================================
    // COSTRUTTORE
    // =========================================================================
    
    public ClienteDTO() {}

    // =========================================================================
    // GETTER
    // =========================================================================

    public String getToken() { return token; }
    public boolean isDuplicato() { return duplicato; }
    public boolean isErroreInterno() { return erroreInterno; }

    // =========================================================================
    // SETTER
    // =========================================================================

    public void setToken(String token) { this.token = token; }
    public void setDuplicato(boolean duplicato) { this.duplicato = duplicato; }
    public void setErroreInterno(boolean erroreInterno) { this.erroreInterno = erroreInterno; }

}