package it.unina.dietiEstates25.dto.request;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UpdatePasswordRequestDTO {
    @Size(min = 1,max = 5000,message = "Inserisci una password valida!")
    private String password;

    @Size(min = 5,max = 50000,message = "Inserisci una mail valida!")
    private String email;

}
