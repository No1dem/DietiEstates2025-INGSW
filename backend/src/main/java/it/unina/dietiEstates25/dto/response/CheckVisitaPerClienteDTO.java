package it.unina.dietiEstates25.dto.response;

public class CheckVisitaPerClienteDTO {
    private boolean internalError;

    private boolean success;

    private boolean visitGiaPrenotata;


    public CheckVisitaPerClienteDTO(boolean internalError, boolean success, boolean visitGiaPrenotata) {
        this.internalError = internalError;
        this.success = success;
        this.visitGiaPrenotata = visitGiaPrenotata;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setInternalError(boolean internalError) {
        this.internalError = internalError;
    }

    public void setVisitGiaPrenotata(boolean visitGiaPrenotata) {
        this.visitGiaPrenotata = visitGiaPrenotata;
    }

    public boolean isSuccess() {
        return success;
    }

    public boolean isInternalError() {
        return internalError;
    }

    public boolean isVisitGiaPrenotata() {
        return visitGiaPrenotata;
    }
}
