package it.unina.dietiEstates25.dto.response;

public class ClienteDTO {
    private String token;

    private boolean duplicato;


    private boolean erroreInterno;

    public ClienteDTO() {}

    public ClienteDTO(boolean duplicato, boolean erroreInterno) {
        this.duplicato = duplicato;
        this.erroreInterno = erroreInterno;
    }

    public ClienteDTO(String token, boolean duplicato) {
        this.token = token;
        this.duplicato = duplicato;
    }

    public ClienteDTO(boolean duplicato) {
        this.duplicato = duplicato;
    }




    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean isDuplicato() {
        return duplicato;
    }

    public void setDuplicato(boolean duplicato) {
        this.duplicato = duplicato;
    }



    public void setErroreInterno(boolean erroreInterno) {
        this.erroreInterno = erroreInterno;
    }

    public boolean isErroreInterno() {
        return erroreInterno;
    }
}

