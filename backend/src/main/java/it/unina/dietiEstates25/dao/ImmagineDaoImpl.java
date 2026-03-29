package it.unina.dietiEstates25.dao;

import it.unina.dietiEstates25.db.DatabaseConnection;
import it.unina.dietiEstates25.dto.response.FotoDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ImmagineDaoImpl implements ImmagineDao {

    // =========================================================================
    // COSTANTI E VARIABILI DI ISTANZA
    // =========================================================================
    private static final Logger logger = LoggerFactory.getLogger(ImmagineDaoImpl.class);
    private static final String ERRORE = "Si è verificato un errore nel db";

    private Connection connection;

    // =========================================================================
    // COSTRUTTORE
    // =========================================================================
    public ImmagineDaoImpl() throws SQLException {
        try {
            this.connection = DatabaseConnection.getConnection();
        } catch (SQLException dbException) {
            logger.error(ERRORE, dbException);
        }
    }

    // =========================================================================
    // METODI PUBBLICI / INTERFACCIA (CRUD e Lettura)
    // =========================================================================
    @Override
    public FotoDTO addImage(String imageId, String agentCf) {
        List<String> existingPhotos = getPublicIdByCf(agentCf);
        String sqlQuery;
        
        if (agenteNonHaFoto(existingPhotos)) {
            sqlQuery = "INSERT INTO FOTO(assert_id,agente) values(?,?)";
        } else {
            sqlQuery = "UPDATE FOTO SET assert_id = ? where agente= ?";
        }
        
        try (PreparedStatement insertOrUpdateStmt = connection.prepareStatement(sqlQuery)) {
            insertOrUpdateStmt.setString(1, imageId);
            insertOrUpdateStmt.setString(2, agentCf);
            
            int affectedRows = insertOrUpdateStmt.executeUpdate();
            if (affectedRows > 0) {
                return new FotoDTO(true, false);
            }
            return new FotoDTO(false, true);
        } catch (SQLException sqlException) {
            logger.error(ERRORE, sqlException);
            return new FotoDTO(false, true);
        }
    }

    @Override
    public List<String> getPublicIdByCf(String fiscalCode) {
        String fetchAgentPhotoQuery = "select assert_id from foto where agente = ?";
        return getFoto(fiscalCode, fetchAgentPhotoQuery, connection);
    }

    @Override
    public List<String> getImageOfImmobile(int propertyId) {
        return getFotoById(propertyId);
    }

    public List<String> getFotoById(int propertyIdParam) {
        String selectPropertyPhotosQuery = "select assert_id from foto where immobileRiferimento = ?";
        List<String> propertyPhotos = new ArrayList<>();
        
        try (PreparedStatement queryStmt = connection.prepareStatement(selectPropertyPhotosQuery)) {
            queryStmt.setInt(1, propertyIdParam);
            ResultSet resultSet = queryStmt.executeQuery();
            
            while (resultSet.next()) {
                propertyPhotos.add(resultSet.getString("assert_id"));
            }
        } catch (SQLException dbEx) {
            logger.error(ERRORE, dbEx);
        }
        return propertyPhotos;
    }

    // =========================================================================
    // METODI PRIVATI / PACKAGE-PRIVATE (Helper Utilities)
    // =========================================================================
    private static boolean agenteNonHaFoto(List<String> photoList) {
        return photoList.isEmpty();
    }

    static List<String> getFoto(String identifier, String queryStr, Connection dbConn) {
        List<String> fetchedPhotos = new ArrayList<>();
        
        try (PreparedStatement statement = dbConn.prepareStatement(queryStr)) {
            statement.setString(1, identifier);
            ResultSet results = statement.executeQuery();
            
            while (results.next()) {
                fetchedPhotos.add(results.getString("assert_id"));
            }
        } catch (SQLException dbError) {
            logger.error(ERRORE, dbError);
        }
        return fetchedPhotos;
    }
}