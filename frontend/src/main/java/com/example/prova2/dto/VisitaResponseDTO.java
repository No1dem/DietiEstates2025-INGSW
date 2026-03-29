package com.example.prova2.dto;

public class VisitaResponseDTO {

    // =========================================================================
    // CAMPI (Proprietà)
    // =========================================================================
    private boolean success;
    private boolean fail;
    private boolean immobileNonDiAgente;
    private boolean agenteOccupato;
    private boolean orarioFineNonValido;

    // =========================================================================
    // COSTRUTTORI
    // =========================================================================
    public VisitaResponseDTO() {}

    public VisitaResponseDTO(boolean success, boolean fail) {
        this.success = success;
        this.fail = fail;
    }

    public VisitaResponseDTO(boolean success, boolean fail, boolean immobileNonDiAgente) {
        this.success = success;
        this.fail = fail;
        this.immobileNonDiAgente = immobileNonDiAgente;
    }

    public VisitaResponseDTO(boolean success, boolean fail, boolean immobileNonDiAgente, boolean agenteOccupato, boolean orarioFineNonValido) {
        this.success = success;
        this.fail = fail;
        this.immobileNonDiAgente = immobileNonDiAgente;
        this.agenteOccupato = agenteOccupato;
        this.orarioFineNonValido = orarioFineNonValido;
    }

    // =========================================================================
    // GETTER
    // =========================================================================
    public boolean isSuccess() { return success; }
    public boolean getFail() { return fail; }
    public boolean isImmobileNonDiAgente() { return immobileNonDiAgente; }
    public boolean isAgenteOccupato() { return agenteOccupato; }
    public boolean isOrarioFineNonValido() { return orarioFineNonValido; }

    // =========================================================================
    // SETTER
    // =========================================================================
    public void setSuccess(boolean success) { this.success = success; }
    public void setFail(boolean fail) { this.fail = fail; }
    public void setImmobileNonDiAgente(boolean immobileNonDiAgente) { this.immobileNonDiAgente = immobileNonDiAgente; }
    public void setAgenteOccupato(boolean agenteOccupato) { this.agenteOccupato = agenteOccupato; }
    public void setOrarioFineNonValido(boolean orarioFineNonValido) { this.orarioFineNonValido = orarioFineNonValido; }

}