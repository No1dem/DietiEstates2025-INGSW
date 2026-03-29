package it.unina.dietiEstates25.service;
import it.unina.dietiEstates25.dao.UserDao;
import it.unina.dietiEstates25.dao.UserDaoImpl;
import it.unina.dietiEstates25.dto.response.LoginUtenteResponse;

import java.sql.SQLException;

public class AutenticazioneService {

    private AutenticazioneService(){}


    public static LoginUtenteResponse login(String email, String password){
        try {
            UserDao dao = new UserDaoImpl();
            String passwordStored=dao.getPassword(email);
            if(SecurityPasswordService.checkPassword(password,passwordStored)) {
                return dao.login(email, password);
            }
            return null;
        }catch (SQLException e){
            return null;
        }
    }
}
