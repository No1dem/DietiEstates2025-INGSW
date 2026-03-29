package it.unina.dietiEstates25.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class LoginRequestDTO {
    @NotEmpty(message = "L'email che inserisci non deve essere vuota!")
    @NotNull(message = "Devi inserire una email")
    private String email;

    @NotEmpty(message = "La password che inserisci non deve essere vuota!")
    @NotNull(message = "Devi inserire una password")
    private String password;

    public @NotEmpty(message = "La password che inserisci non deve essere vuota!") @NotNull(message = "Devi inserire una password") String getPassword() {
        return password;
    }

    public @NotEmpty(message = "L'email che inserisci non deve essere vuota!") @NotNull(message = "Devi inserire una email") String getEmail() {
        return email;
    }

    public void setPassword(@NotEmpty(message = "La password che inserisci non deve essere vuota!") @NotNull(message = "Devi inserire una password") String password) {
        this.password = password;
    }

    public void setEmail(@NotEmpty(message = "L'email che inserisci non deve essere vuota!") @NotNull(message = "Devi inserire una email") String email) {
        this.email = email;
    }
}
