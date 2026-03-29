package it.unina.dietiEstates25.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
public class AddAgenteRequestDTO extends AddBaseDTO {

    @NotEmpty
    @Size(min = 16, max = 16)
    private String cfGestore;

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        AddAgenteRequestDTO other = (AddAgenteRequestDTO) obj;
        return Objects.equals(getNome(), other.getNome()) &&
                Objects.equals(getCognome(), other.getCognome()) &&
                Objects.equals(getCf(), other.getCf()) &&
                Objects.equals(getTelefono(), other.getTelefono()) &&
                Objects.equals(getPassword(), other.getPassword()) &&
                Objects.equals(getEmail(), other.getEmail()) &&
                Objects.equals(cfGestore, other.cfGestore);
    }
    @Override
    public int hashCode() {
        return Objects.hash(getNome(), getCognome(), getCf(), getTelefono(), getPassword(), getEmail(), cfGestore);
    }
}
