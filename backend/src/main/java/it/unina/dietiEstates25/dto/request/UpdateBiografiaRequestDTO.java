package it.unina.dietiEstates25.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class UpdateBiografiaRequestDTO {
    @NotNull(message = "Inserisci una biografia")
    @Size(max = 3000,message = "Inserisci una bio valida!")
    private String biografia;

    public @NotNull(message = "Inserisci una biografia") @Size(max = 3000, message = "Inserisci una bio valida!") String getBiografia() {
        return biografia;
    }

    public void setBiografia(@NotNull(message = "Inserisci una biografia") @Size(max = 3000, message = "Inserisci una bio valida!") String biografia) {
        this.biografia = biografia;
    }
}
