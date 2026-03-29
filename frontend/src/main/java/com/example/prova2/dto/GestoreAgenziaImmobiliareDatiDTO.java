package com.example.prova2.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GestoreAgenziaImmobiliareDatiDTO {

    // =========================================================================
    // CAMPI 
    // =========================================================================

    @JsonProperty("token")
    private String token;

    @JsonProperty("nome")
    private String nome;

    @JsonProperty("cognome")
    private String cognome;

    @JsonProperty("telefono")
    private String telefono;

    @JsonProperty("email")
    private String email;

    @JsonProperty("partitaIva")
    private String partitaIva;

    @JsonProperty("username")
    private String username;

    @JsonProperty("cf")
    private String cf;

    // =========================================================================
    // COSTRUTTORE
    // =========================================================================

    public GestoreAgenziaImmobiliareDatiDTO() {}

    // =========================================================================
    // GETTER
    // =========================================================================

    public String getToken() { return token; }
    public String getNome() { return nome; }
    public String getCognome() { return cognome; }
    public String getTelefono() { return telefono; }
    public String getEmail() { return email; }
    public String getPartitaIva() { return partitaIva; }
    public String getUsername() { return username; }
    public String getCf() { return cf; }

    // =========================================================================
    // SETTER
    // =========================================================================

    public void setToken(String token) { this.token = token; }
    public void setNome(String nome) { this.nome = nome; }
    public void setCognome(String cognome) { this.cognome = cognome; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public void setEmail(String email) { this.email = email; }
    public void setPartitaIva(String partitaIva) { this.partitaIva = partitaIva; }
    public void setUsername(String username) { this.username = username; }
    public void setCf(String cf) { this.cf = cf; }

}