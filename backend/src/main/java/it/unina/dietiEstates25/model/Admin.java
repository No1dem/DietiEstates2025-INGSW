package it.unina.dietiEstates25.model;

import lombok.Getter;

@Getter
public class Admin extends GestoreAgenziaImmobiliare {
    private final AccountAmministratore accountAmministratore = new AccountAmministratore();

}
