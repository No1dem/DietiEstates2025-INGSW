package it.unina.dietiEstates25.dto.request;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class AddAdminRequestDTO {
    @NotNull
    @NotEmpty
    private String nome;

    @NotNull
    @NotEmpty
    private String cognome;

    @NotNull
    @Size(min = 16, max = 16)
    private String cf;

    @NotNull
    @Size(min = 10, max = 10)
    private String telefono;


    @NotNull
    @Size(min = 8, max = 100000)
    private String password;

    @NotNull
    @Size(min = 5, max = 400)
    private String email;

    @NotNull
    @Size(min = 2, max = 400)
    private String nomeAgenzia;



    public String getPassword() {
        return password;
    }

    public String getCf() {
        return cf;
    }

    public String getNome() {
        return nome;
    }

    public String getCognome() {
        return cognome;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getEmail() {
        return email;
    }

    public String getNomeAgenzia() {
        return nomeAgenzia;
    }

    public void setNomeAgenzia(String nomeAgenzia) {
        this.nomeAgenzia = nomeAgenzia;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setCf(String cf) {
        this.cf = cf;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
