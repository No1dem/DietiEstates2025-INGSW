package it.unina.dietiEstates25.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AccountSemplice {
    private String username;

    private String password;

    private String email;

    public AccountSemplice(String email) {
        this.email = email;
    }

}
