package it.unina.dietiEstates25.dto.request;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class CheckPasswordRequestDTO {
    @NotNull(message = "email non può essere nullo")
    @NotEmpty(message = "email non può essere vuoto")
    @Size(min = 1,max = 300,message = "email deve essere valida!")
    private String email;

    @NotNull(message = "La password non può essere nulla")
    @NotEmpty(message = "la password non può essere vuota")
    private String password;




    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }



    public String getPassword() {
        return password;
    }


    public void setPassword(String password) {
        this.password = password;
    }
}
