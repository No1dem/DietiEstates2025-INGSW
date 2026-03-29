package it.unina.dietiEstates25.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DatiDTO {
        @JsonProperty("nome")
        private String nome;

        @JsonProperty("cognome")
        private String cognome;

        @JsonProperty("telefono")
        private String telefono;

        @JsonProperty("email")
        private String email;

        @JsonProperty("bio")
        private String bio;

        @JsonProperty("cf")
        private String cf;

        // Costruttore
        public DatiDTO(String nome, String cognome, String email, String telefono, String bio, String cf) {
            this.nome = nome;
            this.cognome = cognome;
            this.email = email;
            this.telefono = telefono;
            this.bio = bio;
            this.cf = cf;
        }

        public DatiDTO() {}

    public DatiDTO(String nome, String cognome, String email, String telefono) {
            this.nome = nome;
            this.cognome = cognome;
            this.email = email;
            this.telefono = telefono;
    }


    public String getNome() {
            return nome;
        }
        public void setNome(String nome) {
            this.nome = nome;
        }
        public String getCognome() {
            return cognome;
        }
        public void setCognome(String cognome) {
            this.cognome = cognome;
        }
        public String getTelefono() {
            return telefono;
        }
        public void setTelefono(String telefono) {
            this.telefono = telefono;
        }
        public String getEmail() {
            return email;
        }
        public void setEmail(String email) {
            this.email = email;
        }

        public String getBio() {
            return bio;
        }
        public void setBio(String bio) {
            this.bio = bio;
        }

    public String getCf() {
        return cf;
    }

    public void setCf(String cf) {
        this.cf = cf;
    }
}


