package it.unina.dietiEstates25.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AddBaseDTO {
    @NotEmpty
    private String nome;

    @NotEmpty
    private String cognome;

    @NotEmpty
    @Size(min = 16, max = 16)
    private String cf;

    @NotEmpty
    private String telefono;

    @NotEmpty
    private String password;

    @NotEmpty
    private String email;
}
