package com.example.prova2.dto;

public class AgenteCreazioneDTO {

    // =========================================================================
    // CAMPI (Proprietà)
    // =========================================================================
    
    private boolean isEmailRegistrata;
    private boolean isCfRegistrato;
    private boolean errore;

    // =========================================================================
    // COSTRUTTORI
    // =========================================================================
    
    public AgenteCreazioneDTO() {}

    public AgenteCreazioneDTO(boolean email, boolean cf) {
        this.isEmailRegistrata = email;
        this.isCfRegistrato = cf;
    }

    public AgenteCreazioneDTO(boolean errore) {
        this.errore = errore;
    }

    // =========================================================================
    // GETTER
    // =========================================================================

    public boolean isEmailRegistrata() { return isEmailRegistrata; }
    public boolean isCfRegistrato() { return isCfRegistrato; }
    public boolean isErrore() { return errore; }

    // =========================================================================
    // SETTER
    // =========================================================================

    public void setEmailRegistrata(boolean emailRegistrata) { this.isEmailRegistrata = emailRegistrata; }
    public void setCfRegistrato(boolean cfRegistrato) { this.isCfRegistrato = cfRegistrato; }
    public void setErrore(boolean errore) { this.errore = errore; }

}