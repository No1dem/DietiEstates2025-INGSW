package it.unina.dietiEstates25.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class BaseImmobileDTO {
    protected int idImmobile;
    protected String via;
    protected String comune;
    protected String numeroCivico;
}
