package it.unina.dietiEstates25.dao;

import it.unina.dietiEstates25.db.DatabaseConnection;
import it.unina.dietiEstates25.dto.request.UpdateCategoriaNotificaDTO;
import it.unina.dietiEstates25.dto.response.UpdateCategoriaResponse;
import it.unina.dietiEstates25.model.CategoriaNotifica;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CategoriaDaoImpl implements CategoriaDao {

    private Connection connection;

    private static final Logger logger = LoggerFactory.getLogger(CategoriaDaoImpl.class);

    private static final String ERRORE = "Si è verificato un errore nel db";

    public CategoriaDaoImpl() {
        try {
            this.connection = DatabaseConnection.getConnection();
        } catch (SQLException sqlEx) {
            logger.error(ERRORE, sqlEx);
        }
    }

    public UpdateCategoriaResponse setInviaCategoriaCliente(String clientEmail, UpdateCategoriaNotificaDTO categoryToActivate) {
        String updateQuery = "UPDATE NOTIFICA SET da_inviare=true WHERE clientenotificato=? and categoria=?";
        return updateCategoria(clientEmail, categoryToActivate, updateQuery);
    }

    public UpdateCategoriaResponse setNonInviareCategoriaCliente(String clientEmail, UpdateCategoriaNotificaDTO targetCategory) {
        String deactivateQuery = "UPDATE NOTIFICA SET da_inviare=false WHERE clientenotificato=? and categoria=?";
        return updateCategoria(clientEmail, targetCategory, deactivateQuery);
    }

    public UpdateCategoriaResponse setNonInviareCategoriaAgente(String agentCf, UpdateCategoriaNotificaDTO categoryDto) {
        String query = "UPDATE NOTIFICA SET da_inviare=false WHERE agentenotificato=? and categoria=?";
        return updateCategoria(agentCf, categoryDto, query);
    }

    public UpdateCategoriaResponse setInviareCategoriaAgente(String fiscalCode, UpdateCategoriaNotificaDTO categoryToUpdate) {
        String activateSql = "UPDATE NOTIFICA SET da_inviare=true WHERE agentenotificato=? and categoria=?";
        return updateCategoria(fiscalCode, categoryToUpdate, activateSql);
    }

    private UpdateCategoriaResponse updateCategoria(String userId, UpdateCategoriaNotificaDTO categoryDto, String executedQuery) {
        try (PreparedStatement prepStmt = connection.prepareStatement(executedQuery)) {
            prepStmt.setString(1, userId);
            prepStmt.setObject(2, categoryDto.getCategoriaNotifica(), Types.OTHER);
            
            int affectedRows = prepStmt.executeUpdate();
            
            if (noUpdateFatti(affectedRows)) {
                return new UpdateCategoriaResponse(true);
            }
            if (updateAvvenuto(affectedRows)) {
                return new UpdateCategoriaResponse(true, true);
            }
            return new UpdateCategoriaResponse(false, false);
        } catch (SQLException dbError) {
            logger.error(ERRORE, dbError);
            return new UpdateCategoriaResponse(false, false);
        }
    }

    @Override
    public List<CategoriaNotifica> getCategorieDisattivateByAgenteCF(String agentId) {
        List<CategoriaNotifica> deactivatedCategories = new ArrayList<>();
        String selectQuery = "SELECT distinct categoria FROM notifica where agentenotificato = ? and da_inviare = false";
        return getCategoriaNotifiche(agentId, deactivatedCategories, selectQuery);
    }

    @Override
    public List<CategoriaNotifica> getCategorieDisattivateByClienteEmail(String userMail) {
        List<CategoriaNotifica> disabledCategories = new ArrayList<>();
        String sqlString = "SELECT distinct categoria FROM notifica where clientenotificato = ? and da_inviare = false";
        return getCategoriaNotifiche(userMail, disabledCategories, sqlString);
    }

    @Override
    public List<CategoriaNotifica> getAllCategorieByEmail(String mailAddress) {
        List<CategoriaNotifica> allCategories = new ArrayList<>();
        String fetchAllSql = "SELECT distinct categoria FROM notifica where clientenotificato = ?";
        return getCategoriaNotifiche(mailAddress, allCategories, fetchAllSql);
    }

    @Override
    public List<CategoriaNotifica> getAllCategorieByAgenteCF(String agentFiscalCode) {
        List<CategoriaNotifica> categoriesList = new ArrayList<>();
        String fetchAgentSql = "SELECT distinct categoria FROM notifica where agentenotificato = ?";
        return getCategoriaNotifiche(agentFiscalCode, categoriesList, fetchAgentSql);
    }

    private List<CategoriaNotifica> getCategoriaNotifiche(String identifier, List<CategoriaNotifica> categoryList, String queryToRun) {
        try (PreparedStatement stmt = connection.prepareStatement(queryToRun)) {
            stmt.setString(1, identifier);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                String categoryString = rs.getString("categoria");
                CategoriaNotifica enumCategory = CategoriaNotifica.fromString(categoryString);
                categoryList.add(enumCategory);
            }
        } catch (SQLException sqlException) {
            logger.error(ERRORE, sqlException);
            return Collections.emptyList();
        }
        return categoryList;
    }

    private static boolean updateAvvenuto(int rowCount) {
        return rowCount > 0;
    }

    private static boolean noUpdateFatti(int rowsModified) {
        return rowsModified == 0;
    }

}