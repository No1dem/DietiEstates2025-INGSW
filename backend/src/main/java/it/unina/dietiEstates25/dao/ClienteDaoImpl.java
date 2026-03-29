package it.unina.dietiEstates25.dao;

import it.unina.dietiEstates25.db.DatabaseConnection;
import it.unina.dietiEstates25.dto.request.AddClienteRequestDTO;
import it.unina.dietiEstates25.dto.request.LasciaRecensioneRequestDTO;
import it.unina.dietiEstates25.dto.request.UpdatePasswordRequestDTO;
import it.unina.dietiEstates25.dto.response.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static it.unina.dietiEstates25.dao.AgenteDaoImpl.updatePassDB;

public class ClienteDaoImpl implements ClienteDao {

    private Connection connection;

    private static final Logger logger = LoggerFactory.getLogger(ClienteDaoImpl.class);

    private static final String ERRORE = "Si è verificato un errore nel db";

    public ClienteDaoImpl() throws SQLException {
        try {
            this.connection = DatabaseConnection.getConnection();
        } catch (SQLException sqlEx) {
            logger.error(ERRORE, sqlEx);
        }
    }

    @Override
    public ClienteDTO insert(AddClienteRequestDTO clientReq) {
        if (exists(clientReq)) {
            return new ClienteDTO(true);
        }
        

        clientReq.setTelefono("1234567890");
        String insertSql = "INSERT INTO cliente (nome,cognome,telefono,password,email) VALUES (?,?,?,?,?)";
        
        try (PreparedStatement prepStmt = connection.prepareStatement(insertSql)) {
            prepStmt.setString(1, clientReq.getNome());
            prepStmt.setString(2, clientReq.getCognome());
            prepStmt.setString(3, clientReq.getTelefono());
            prepStmt.setString(4, clientReq.getPassword());
            prepStmt.setString(5, clientReq.getEmail());
            
            int affectedRows = prepStmt.executeUpdate();
            if (affectedRows > 0) {
                return new ClienteDTO(false);
            }
            
            ClienteDTO errDto = new ClienteDTO();
            errDto.setErroreInterno(true);
            return errDto;
            
        } catch (SQLException dbError) {
            dbError.printStackTrace();
            logger.error(ERRORE, dbError);
            ClienteDTO errorResponse = new ClienteDTO();
            errorResponse.setErroreInterno(true);
            return errorResponse;
        }
    }

    @Override
    public boolean exists(AddClienteRequestDTO clientDto) {
        String checkEmailSql = "SELECT 1 FROM (SELECT email FROM cliente UNION SELECT email FROM agente UNION SELECT email from gestoreagenziaimmobiliare) WHERE email=?";
        try (PreparedStatement statement = connection.prepareStatement(checkEmailSql)) {
            statement.setString(1, clientDto.getEmail());
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next();
        } catch (SQLException sqlException) {
            return false;
        }
    }

    @Override
    public ClienteDatiDTO getClienteByEmail(String clientEmail) {
        String selectSql = "SELECT nome, cognome, telefono, email,password FROM cliente WHERE email = ?";
        try (PreparedStatement queryStmt = connection.prepareStatement(selectSql)) {
            queryStmt.setString(1, clientEmail);
            try (ResultSet results = queryStmt.executeQuery()) {
                if (results.next()) {
                    return new ClienteDatiDTO(
                            results.getString("nome"),
                            results.getString("cognome"),
                            results.getString("email"),
                            results.getString("telefono")
                    );
                } else {
                    return null;
                }
            }
        } catch (SQLException ex) {
            return null;
        }
    }

    @Override
    public boolean lasciaRecensione(LasciaRecensioneRequestDTO reviewReq) {
        String updateRatingSql = "UPDATE AGENTE SET VALUTAZIONE = ? WHERE cf =?";
        try (PreparedStatement updateStmt = connection.prepareStatement(updateRatingSql)) {
            updateStmt.setInt(1, reviewReq.getValutazione());
            updateStmt.setString(2, reviewReq.getAgenteDaRecensire());
            
            int rowsModified = updateStmt.executeUpdate();
            return rowsModified > 0;
            
        } catch (SQLException sqlErr) {
            logger.error(ERRORE, sqlErr);
        }
        return false;
    }

    @Override
    public List<GetAllRicercheResponse> getAllRicerche(String userEmail) {
        String fetchSearchesSql = "select id_ricerca,clientechecerca,n°stanze,classe_energetica,prezzo,comune,città,tipovendita,prezzo_max from ricerca where clientechecerca=? LIMIT 5";
        List<GetAllRicercheResponse> searchesList = new ArrayList<>();
        
        try (PreparedStatement pStmt = connection.prepareStatement(fetchSearchesSql)) {
            AgenteDaoImpl.getRicerche(userEmail, searchesList, pStmt);
            return searchesList;
        } catch (SQLException queryException) {
            return Collections.emptyList();
        }
    }

    @Override
    public boolean updatePassword(UpdatePasswordRequestDTO pwdReq) {
        String updatePwdSql = "Update cliente set password=? where email=?";
        return updatePassDB(pwdReq, updatePwdSql, connection);
    }
}