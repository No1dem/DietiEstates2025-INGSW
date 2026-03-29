package it.unina.dietiEstates25.service;

import it.unina.dietiEstates25.dao.GestoreDao;
import it.unina.dietiEstates25.dao.GestoreDaoImpl;
import it.unina.dietiEstates25.dto.request.CheckPasswordRequestDTO;

import java.sql.SQLException;

public class GestorePasswordCheckService {

    private GestorePasswordCheckService(){}

    public static boolean checkPassword(CheckPasswordRequestDTO checkPasswordRequestDTO) {
        try {
            GestoreDao dao = new GestoreDaoImpl();
            String passwordhash=dao.getSecurityPassword(checkPasswordRequestDTO.getEmail());
            if(passwordhash == null){
                return false;
            }
            return SecurityPasswordService.checkPassword(checkPasswordRequestDTO.getPassword(),passwordhash);
        } catch (SQLException e) {
            return false;
        }

    }
}
