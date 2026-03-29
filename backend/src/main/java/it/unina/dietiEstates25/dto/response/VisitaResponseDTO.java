package it.unina.dietiEstates25.dto.response;

public class VisitaResponseDTO {
    private boolean success;

    private boolean fail;

    private boolean immobileNonDiAgente;

    private boolean agenteOccupato;

    private boolean orarioFineNonValido;

    public VisitaResponseDTO(boolean success,boolean fail,boolean immobileNonDiAgente,boolean agenteOccupato,boolean orarioFineNonValido) {
        this.success = success;
        this.fail = fail;
        this.immobileNonDiAgente = immobileNonDiAgente;
        this.agenteOccupato = agenteOccupato;
        this.orarioFineNonValido = orarioFineNonValido;
    }

    public void setAgenteOccupato(boolean agenteOccupato) {
        this.agenteOccupato = agenteOccupato;
    }

    public void setOrarioFineNonValido(boolean orarioFineNonValido) {
        this.orarioFineNonValido = orarioFineNonValido;
    }

    public boolean isAgenteOccupato() {
        return agenteOccupato;
    }

    public boolean isOrarioFineNonValido() {
        return orarioFineNonValido;
    }

    public VisitaResponseDTO(boolean success, boolean fail, boolean immobileNonDiAgente) {
        this.success = success;
        this.fail = fail;
        this.immobileNonDiAgente = immobileNonDiAgente;
    }

    public boolean isImmobileNonDiAgente() {
        return immobileNonDiAgente;
    }

    public void setImmobileNonDiAgente(boolean immobileNonDiAgente) {
        this.immobileNonDiAgente = immobileNonDiAgente;
    }

    public VisitaResponseDTO(boolean success, boolean fail) {
        this.success = success;
        this.fail = fail;
    }

    public boolean getFail() {
        return fail;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setFail(boolean fail) {
        this.fail = fail;
    }
}
