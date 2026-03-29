package com.example.prova2.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UpdateDatiResponseDTO {

    // =========================================================================
    // CAMPI (Proprietà)
    // =========================================================================
    private boolean duplicato;
    private boolean erroreInterno;

    // =========================================================================
    // COSTRUTTORI
    // =========================================================================
    public UpdateDatiResponseDTO() {}

    public UpdateDatiResponseDTO(boolean emailDuplicate, boolean error) {
        this.duplicato = emailDuplicate;
        this.erroreInterno = error;
    }

    // =========================================================================
    // GETTER
    // =========================================================================
    public boolean getDuplicato() { return duplicato; }
    public boolean getErroreInterno() { return erroreInterno; }

    // =========================================================================
    // SETTER
    // =========================================================================
    public void setDuplicato(boolean duplicato) { this.duplicato = duplicato; }
    public void setErroreInterno(boolean erroreInterno) { this.erroreInterno = erroreInterno; }

}