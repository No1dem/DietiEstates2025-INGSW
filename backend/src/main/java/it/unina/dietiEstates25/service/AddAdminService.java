package it.unina.dietiEstates25.service;

import it.unina.dietiEstates25.dao.AdminDao;
import it.unina.dietiEstates25.dao.AdminDaoImpl;
import it.unina.dietiEstates25.dto.request.AddAdminRequestDTO;
import java.sql.SQLException;

public class AddAdminService {

    private AddAdminService(){}

    public static boolean addAdmin(AddAdminRequestDTO admin) {
        try{
            admin.setPassword(SecurityPasswordService.hashPassword(admin.getPassword()));
            AdminDao adminDao = new AdminDaoImpl();
            return adminDao.addAdmin(admin);
        } catch (SQLException e) {
            return false;
        }
    }
}
