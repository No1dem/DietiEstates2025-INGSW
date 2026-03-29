package it.unina.dietiEstates25.dao;

import it.unina.dietiEstates25.db.DatabaseConnection;
import it.unina.dietiEstates25.dto.request.AddAdminRequestDTO;
import it.unina.dietiEstates25.dto.request.UpdatePasswordRequestDTO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static it.unina.dietiEstates25.dao.AgenteDaoImpl.updatePassDB;

public class AdminDaoImpl implements AdminDao {

    private Connection connection;

    private static final Logger logger = LoggerFactory.getLogger(AdminDaoImpl.class);

    public AdminDaoImpl() throws SQLException {
        try {
            connection = DatabaseConnection.getConnection();
        } catch (SQLException sqlEx) {
            logger.error("Si è verificato un errore nel db: ", sqlEx);
        }
    }

    @Override
    public boolean addAdmin(AddAdminRequestDTO adminRequest) {
        String insertQuery = "insert into gestoreagenziaimmobiliare" +
                "(nome,cognome,cf,telefono,password,email,admin,nomeagenzia,ruolo)"+
                " values(?,?,?,?,?,?,?,?,?)";
        
        try (PreparedStatement statement = connection.prepareStatement(insertQuery)) {
            
            setSql(statement, adminRequest.getNome(), adminRequest.getCognome(), adminRequest.getCf(), adminRequest.getTelefono(), adminRequest.getPassword(), adminRequest.getEmail());
            
            statement.setBoolean(7, true);
            statement.setString(8, adminRequest.getNomeAgenzia());
            statement.setString(9, "Admin");
            
            int affectedRows = statement.executeUpdate();
            return affectedRows > 0;
            
        } catch (SQLException dbException) {
            dbException.printStackTrace();
            logger.error("Si è verificato un errore nel db: ", dbException);
            return false;
        }
    }

    static void setSql(PreparedStatement prepStmt, String firstName, String lastName, String fiscalCode, String phone, String pwd, String mailAddress) throws SQLException {
        prepStmt.setString(1, firstName);
        prepStmt.setString(2, lastName);
        prepStmt.setString(3, fiscalCode);
        prepStmt.setString(4, phone);
        prepStmt.setString(5, pwd);
        prepStmt.setString(6, mailAddress);
    }

    @Override
    public boolean updatePassword(UpdatePasswordRequestDTO updateReq) {
        String updateQuery = "Update gestoreagenziaimmobiliare set password=? where email=?";
        return updatePassDB(updateReq, updateQuery, connection);
    }
}