package com.example.prova2.dto;

public class LoginUtenteResponse {

    // =========================================================================
    // CAMPI (Proprietà)
    // =========================================================================
    private String role;
    private String token;
    private boolean admin;

    // =========================================================================
    // COSTRUTTORI
    // =========================================================================
    public LoginUtenteResponse() {}

    // =========================================================================
    // GETTER
    // =========================================================================
    public String getRole() { return role; }
    public String getToken() { return token; }
    public boolean getAdmin() { return admin; }

    // =========================================================================
    // SETTER
    // =========================================================================
    public void setRole(String role) { this.role = role; }
    public void setToken(String token) { this.token = token; }
    public void setAdmin(boolean admin) { this.admin = admin; }

}