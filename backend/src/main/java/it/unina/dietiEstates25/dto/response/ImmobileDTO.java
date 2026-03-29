package it.unina.dietiEstates25.dto.response;

public class ImmobileDTO {
    private boolean immobileNonInserito;

    private boolean immobileInserito;

    private boolean erroreInterno;



    public ImmobileDTO(boolean immobileInserito,boolean erroreInterno,boolean immobileNonInserito) {
        this.immobileInserito = immobileInserito;
        this.erroreInterno = erroreInterno;
        this.immobileNonInserito = immobileNonInserito;
    }


    public void setErroreInterno(boolean erroreInterno) {
        this.erroreInterno = erroreInterno;
    }

    public boolean isErroreInterno() {
        return erroreInterno;
    }

    public void setImmobileInserito(boolean immobileInserito) {
        this.immobileInserito = immobileInserito;
    }

    public void setImmobileNonInserito(boolean immobileNonInserito) {
        this.immobileNonInserito = immobileNonInserito;
    }

    public boolean isImmobileInserito() {
        return immobileInserito;
    }

    public boolean isImmobileNonInserito() {
        return immobileNonInserito;
    }
}
