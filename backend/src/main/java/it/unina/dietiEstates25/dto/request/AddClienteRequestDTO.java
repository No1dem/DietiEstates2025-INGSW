package it.unina.dietiEstates25.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor
@Getter
@Setter
public class AddClienteRequestDTO {
    @NotNull(message = "Inserire un nome!")
    @NotEmpty(message = "Il nome non può essere vuoto!")
    private String nome;

    @NotNull(message = "Inserire un cognome!")
    @NotEmpty(message = "Il cognome non può essere vuoto!")
    private String cognome;


    private String telefono;

    @NotNull(message = "la password deve essere inserita")
    @NotEmpty(message = "la password non può essere vuota")
    private String password;

    @NotNull(message = "l'email deve essere inserita")
    @NotEmpty(message = "l'email non può essere vuota!")
    private String email;


    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
}
