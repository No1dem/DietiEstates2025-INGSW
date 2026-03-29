package it.unina.dietiEstates25.dto.request;
import it.unina.dietiEstates25.model.CategoriaNotifica;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UpdateCategoriaNotificaDTO {



    @NotNull(message = "Inserisci una categoria da disattivare")
    private CategoriaNotifica categoriaNotifica;


    @Override
    public String toString() {
        return "CategoriaNotificaDTO{" +
                "categoriaNotifica=" + categoriaNotifica +
                '}';
    }


}
