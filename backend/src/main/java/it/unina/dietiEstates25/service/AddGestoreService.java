package it.unina.dietiEstates25.service;

import it.unina.dietiEstates25.dao.GestoreDao;
import it.unina.dietiEstates25.dao.GestoreDaoImpl;
import it.unina.dietiEstates25.dto.request.AddGestoreRequestDTO;
import it.unina.dietiEstates25.dto.response.GestoreDTO;
import java.sql.SQLException;

public class AddGestoreService {

    private AddGestoreService(){}

    public static GestoreDTO addGestore(AddGestoreRequestDTO gestore) {
        try {
            GestoreDao dao = new GestoreDaoImpl();
            gestore.setPassword(SecurityPasswordService.hashPassword(gestore.getPassword()));
            return dao.addGestore(gestore);
        } catch (SQLException e) {
            return new GestoreDTO(false,false,true);
        }
    }
}
