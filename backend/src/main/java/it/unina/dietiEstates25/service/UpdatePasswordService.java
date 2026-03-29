package it.unina.dietiEstates25.service;
import it.unina.dietiEstates25.dao.*;
import it.unina.dietiEstates25.dto.request.UpdatePasswordRequestDTO;

import java.sql.SQLException;

public class UpdatePasswordService {

    private UpdatePasswordService() {}

    public static boolean updatePassword(UpdatePasswordRequestDTO request) {
        try{
            AdminDao dao = new AdminDaoImpl();
            request.setPassword(SecurityPasswordService.hashPassword(request.getPassword()));
            return dao.updatePassword(request);
        } catch (SQLException e) {
            return false;
        }
    }

    public static boolean updatePasswordAgente(UpdatePasswordRequestDTO request) {
        try{
            AgenteDao dao = new AgenteDaoImpl();
            request.setPassword(SecurityPasswordService.hashPassword(request.getPassword()));
            return dao.updatePassword(request);
        } catch (SQLException e) {
            return false;
        }
    }

    public static boolean updatePasswordCliente(UpdatePasswordRequestDTO request) {
        try{
            ClienteDao dao = new ClienteDaoImpl();
            request.setPassword(SecurityPasswordService.hashPassword(request.getPassword()));
            return dao.updatePassword(request);
        } catch (SQLException e) {
            return false;
        }
    }
}
