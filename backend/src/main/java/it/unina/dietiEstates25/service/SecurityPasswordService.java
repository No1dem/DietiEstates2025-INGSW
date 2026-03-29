package it.unina.dietiEstates25.service;
import org.mindrot.jbcrypt.BCrypt;

public class SecurityPasswordService {

    private SecurityPasswordService(){}

    public static String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }


    public static boolean checkPassword(String passwordInseritaUtente, String storedPassword) {
        boolean esito;
        esito=BCrypt.checkpw(passwordInseritaUtente, storedPassword);
        return esito;
    }
}
