package it.unina.dietiEstates25.dto.response;

public class GestoreDTO {
    private boolean duplicatoCF;

    private boolean duplicatoEmail;

    private boolean erroreInterno;

    public GestoreDTO(boolean duplicatoCF, boolean duplicatoEmail, boolean erroreInterno) {
        this.duplicatoCF = duplicatoCF;
        this.duplicatoEmail = duplicatoEmail;
        this.erroreInterno = erroreInterno;
    }

    public GestoreDTO(boolean duplicatoCF, boolean duplicatoEmail) {
        this.duplicatoCF = duplicatoCF;
        this.duplicatoEmail = duplicatoEmail;
    }

    public boolean isDuplicatoCF() {
        return duplicatoCF;
    }

    public boolean isDuplicatoEmail() {
        return duplicatoEmail;
    }

    public void setErroreInterno(boolean erroreInterno) {
        this.erroreInterno = erroreInterno;
    }

    public void setDuplicatoCF(boolean duplicatoCF) {
        this.duplicatoCF = duplicatoCF;
    }

    public void setDuplicatoEmail(boolean duplicatoEmail) {
        this.duplicatoEmail = duplicatoEmail;
    }

    public boolean isErroreInterno() {
        return erroreInterno;
    }
}
