package com.example.prova2.dto;

public class GestoreDTO {

    // =========================================================================
    // CAMPI (Proprietà)
    // =========================================================================
    
    private boolean duplicatoCF;
    private boolean duplicatoEmail;
    private boolean erroreInterno;

    // =========================================================================
    // COSTRUTTORI
    // =========================================================================
    
    public GestoreDTO() {}

    public GestoreDTO(boolean duplicatoCF, boolean duplicatoEmail) {
        this.duplicatoCF = duplicatoCF;
        this.duplicatoEmail = duplicatoEmail;
    }

    public GestoreDTO(boolean duplicatoCF, boolean duplicatoEmail, boolean erroreInterno) {
        this.duplicatoCF = duplicatoCF;
        this.duplicatoEmail = duplicatoEmail;
        this.erroreInterno = erroreInterno;
    }

    // =========================================================================
    // GETTER
    // =========================================================================

    public boolean isDuplicatoCF() { return duplicatoCF; }
    public boolean isDuplicatoEmail() { return duplicatoEmail; }
    public boolean isErroreInterno() { return erroreInterno; }

    // =========================================================================
    // SETTER
    // =========================================================================

    public void setDuplicatoCF(boolean duplicatoCF) { this.duplicatoCF = duplicatoCF; }
    public void setDuplicatoEmail(boolean duplicatoEmail) { this.duplicatoEmail = duplicatoEmail; }
    public void setErroreInterno(boolean erroreInterno) { this.erroreInterno = erroreInterno; }

}