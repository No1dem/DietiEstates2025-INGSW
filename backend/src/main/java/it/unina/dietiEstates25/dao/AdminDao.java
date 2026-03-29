package it.unina.dietiEstates25.dao;
import it.unina.dietiEstates25.dto.request.AddAdminRequestDTO;
import it.unina.dietiEstates25.dto.request.UpdatePasswordRequestDTO;

import java.sql.SQLException;

public interface AdminDao {
    boolean addAdmin(AddAdminRequestDTO admin) throws SQLException;

    boolean updatePassword(UpdatePasswordRequestDTO request) throws SQLException;
}
