package it.unina.dietiEstates25.dto.response;

public class AgenteDTO {

    private boolean credenzialiSbagliate;

    private boolean erroreInterno;

    private String token;

    private String role;

    private String email;

    private boolean admin;

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public AgenteDTO() {}

    public AgenteDTO(boolean credenzialiSbagliate,boolean erroreInterno) {
        this.credenzialiSbagliate = credenzialiSbagliate;
        this.erroreInterno = erroreInterno;
    }


    public AgenteDTO(boolean erroreInterno){
        this.erroreInterno = erroreInterno;
    }



    public boolean isCredenzialiSbagliate() {
        return credenzialiSbagliate;
    }

    public boolean isErroreInterno() {
        return erroreInterno;
    }

    public void setErroreInterno(boolean erroreInterno) {
        this.erroreInterno = erroreInterno;
    }

    public void setCredenzialiSbagliate(boolean credenzialiSbagliate) {
        this.credenzialiSbagliate = credenzialiSbagliate;
    }


    public void setToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}
