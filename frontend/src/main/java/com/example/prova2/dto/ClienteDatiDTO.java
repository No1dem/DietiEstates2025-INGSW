package com.example.prova2.dto;

public class ClienteDatiDTO {

    // =========================================================================
    // CAMPI (Proprietà)
    // =========================================================================
    
    private String nome;
    private String cognome;
    private String email;
    private String telefono;

    // =========================================================================
    // COSTRUTTORI
    // =========================================================================

    public ClienteDatiDTO() {}

    public ClienteDatiDTO(String nome, String cognome, String email, String telefono) {
        this.nome = nome;
        this.cognome = cognome;
        this.email = email;
        this.telefono = telefono;
    }

    // =========================================================================
    // GETTER
    // =========================================================================

    public String getNome() { return nome; }
    public String getCognome() { return cognome; }
    public String getEmail() { return email; }
    public String getTelefono() { return telefono; }

    // =========================================================================
    // SETTER
    // =========================================================================

    public void setNome(String nome) { this.nome = nome; }
    public void setCognome(String cognome) { this.cognome = cognome; }
    public void setEmail(String email) { this.email = email; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

}