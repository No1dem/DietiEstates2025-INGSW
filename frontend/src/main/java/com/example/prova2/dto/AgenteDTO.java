package com.example.prova2.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AgenteDTO {

    // =========================================================================
    // CAMPI (Proprietà)
    // =========================================================================
    
    private boolean credenzialiSbagliate;
    private boolean erroreInterno;
    private String token;
    private String role;
    private String email;
    private boolean admin;

    // =========================================================================
    // COSTRUTTORI
    // =========================================================================
    
    public AgenteDTO() {}

    public AgenteDTO(boolean erroreInterno) {
        this.erroreInterno = erroreInterno;
    }

    public AgenteDTO(boolean credenzialiSbagliate, boolean erroreInterno) {
        this.credenzialiSbagliate = credenzialiSbagliate;
        this.erroreInterno = erroreInterno;
    }

    // =========================================================================
    // GETTER
    // =========================================================================

    public boolean isCredenzialiSbagliate() { return credenzialiSbagliate; }
    public boolean isErroreInterno() { return erroreInterno; }
    public String getToken() { return token; }
    public String getRole() { return role; }
    public String getEmail() { return email; }
    public boolean isAdmin() { return admin; }

    // =========================================================================
    // SETTER
    // =========================================================================

    public void setCredenzialiSbagliate(boolean credenzialiSbagliate) { this.credenzialiSbagliate = credenzialiSbagliate; }
    public void setErroreInterno(boolean erroreInterno) { this.erroreInterno = erroreInterno; }
    public void setToken(String token) { this.token = token; }
    public void setRole(String role) { this.role = role; }
    public void setEmail(String email) { this.email = email; }
    public void setAdmin(boolean admin) { this.admin = admin; }

}