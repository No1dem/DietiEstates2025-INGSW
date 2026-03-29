package it.unina.dietiEstates25.dao;

import it.unina.dietiEstates25.dto.response.GestoreAgenziaImmobiliareDTO;
import it.unina.dietiEstates25.model.AccountSemplice;
import it.unina.dietiEstates25.model.GestoreAgenziaImmobiliare;
import it.unina.dietiEstates25.service.SecurityPasswordService;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AutenticazioneDaoImpl implements AutenticazioneDao {

    private Connection connection;

    @Override
    public boolean checkIfExists(AccountSemplice simpleAccount) throws SQLException {
        String checkClientQuery = "SELECT 1 FROM Cliente c WHERE c.email = ? AND c.password = ?";
        try (PreparedStatement prepStmt = connection.prepareStatement(checkClientQuery)) {
            prepStmt.setString(1, simpleAccount.getEmail());
            prepStmt.setString(2, simpleAccount.getPassword());
            try (ResultSet resultSet = prepStmt.executeQuery()) {
                return resultSet.next();
            }
        } catch (SQLException sqlException) {
            return false;
        } finally {
            connection.close();
        }
    }

    @Override
    public GestoreAgenziaImmobiliareDTO checkGestoreExists(GestoreAgenziaImmobiliare manager, String passwordToVerify) throws SQLException {
        String verifyGestoreQuery = "SELECT g.password FROM GestoreAgenziaImmobiliare g WHERE g.username = ? AND g.nomeAgenzia = ? AND g.cf = ?  AND g.admin = ?";
        try (PreparedStatement statement = connection.prepareStatement(verifyGestoreQuery)) {
            statement.setString(1, manager.getAccountGestore().getUsername());
            statement.setString(2, manager.getAgenziaAppartenente().getNomeAgenzia());
            statement.setString(3, manager.getCf());
            statement.setBoolean(4, manager.getisAdmin());
            
            try (ResultSet results = statement.executeQuery()) {
                if (isRisultatoPresente(results)) {
                    GestoreAgenziaImmobiliareDTO responseDto = new GestoreAgenziaImmobiliareDTO();
                    responseDto.setCredSbagliate(!isPasswordCorretta(passwordToVerify, results));
                    return responseDto;
                } else {
                    GestoreAgenziaImmobiliareDTO responseDto = new GestoreAgenziaImmobiliareDTO();
                    responseDto.setCredSbagliate(true);
                    return responseDto;
                }
            }
        } catch (SQLException dbError) {
            return null;
        } finally {
            connection.close();
        }
    }

    private static boolean isRisultatoPresente(ResultSet queryResult) throws SQLException {
        return queryResult.next();
    }

    private static boolean isPasswordCorretta(String rawPassword, ResultSet resultSetData) throws SQLException {
        return SecurityPasswordService.checkPassword(rawPassword, resultSetData.getString("password"));
    }
}