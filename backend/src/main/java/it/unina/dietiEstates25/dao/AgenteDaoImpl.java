package it.unina.dietiEstates25.dao;

import it.unina.dietiEstates25.db.DatabaseConnection;
import it.unina.dietiEstates25.dto.request.AddAgenteRequestDTO;
import it.unina.dietiEstates25.dto.request.UpdatePasswordRequestDTO;
import it.unina.dietiEstates25.dto.response.AgenteCreazioneDTO;
import it.unina.dietiEstates25.dto.response.DatiDTO;
import it.unina.dietiEstates25.dto.response.GetAllRicercheResponse;
import it.unina.dietiEstates25.model.ClasseEnergetica;
import it.unina.dietiEstates25.model.TipoVendita;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static it.unina.dietiEstates25.dao.GestoreDaoImpl.getGetAllRicercheResponses;
import static it.unina.dietiEstates25.dao.NotificaDaoImpl.esiste;
import static it.unina.dietiEstates25.dao.UserDaoImpl.getDatiDTO;

public class AgenteDaoImpl implements AgenteDao {
    private Connection connection;

    private static final Logger logger = LoggerFactory.getLogger(AgenteDaoImpl.class);

    private static final String ERRORE = "Si è verificato un errore nel db";

    public AgenteDaoImpl() throws SQLException {
        try {
            connection = DatabaseConnection.getConnection();
        } catch (SQLException sqlEx) {
            logger.error(ERRORE, sqlEx);
        }
    }

    public AgenteCreazioneDTO addAgente(AddAgenteRequestDTO agentReq) {
        if (isEmailRegistrata(agentReq.getEmail())) {
           return new AgenteCreazioneDTO(true, false);
        }
        if (isCFRegistrato(agentReq.getCf())) {
            return new AgenteCreazioneDTO(false, true);
        }
        String insertAgentQuery = "insert into Agente (cf,nome,cognome,telefono,password,email,gestoreriferimento)" +
                     "values (?,?,?,?,?,?,?)";
        try (PreparedStatement prepStmt = connection.prepareStatement(insertAgentQuery)) {
            prepStmt.setString(1, agentReq.getCf());
            prepStmt.setString(2, agentReq.getNome());
            prepStmt.setString(3, agentReq.getCognome());
            prepStmt.setString(4, agentReq.getTelefono());
            prepStmt.setString(5, agentReq.getPassword());
            prepStmt.setString(6, agentReq.getEmail());
            prepStmt.setString(7, agentReq.getCfGestore());
            
            int affectedRows = prepStmt.executeUpdate();
            if (affectedRows > 0) {
                return new AgenteCreazioneDTO(false);
            }
        } catch (SQLException sqlException) {
            logger.error(ERRORE, sqlException);
        }
        return new AgenteCreazioneDTO(true);
    }

    private boolean isEmailRegistrata(String emailAddress) {
        String checkEmailQuery = "select email from gestoreagenziaimmobiliare union select email from cliente union select email from agente";
        return esistenzaCampo(emailAddress, checkEmailQuery);
    }

    private boolean isCFRegistrato(String fiscalCode) {
        String checkCfQuery = "select cf from gestoreagenziaimmobiliare UNION select cf from agente";
        return esistenzaCampo(fiscalCode, checkCfQuery);
    }

    private boolean esistenzaCampo(String fieldValue, String query) {
        return esiste(fieldValue, query, connection);
    }

    public DatiDTO getAgenteByEmail(String agentEmail) {
        String selectAgentQuery = "SELECT nome, cognome, telefono, email,password, \"Bio\",cf FROM agente WHERE email = ?";
        return getAgenteDatiDTO(agentEmail, selectAgentQuery);
    }

    private DatiDTO getAgenteDatiDTO(String identifier, String queryToExecute) {
        return getDatiDTO(identifier, queryToExecute, connection);
    }

    public boolean updateBiografia(String updatedBio, String agentCf) {
        String updateBioSql = "UPDATE AGENTE SET \"Bio\" = ? where cf = ?";
        try (PreparedStatement statement = connection.prepareStatement(updateBioSql)) {
            statement.setString(1, updatedBio);
            statement.setString(2, agentCf);
            int rowsUpdated = statement.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException dbEx) {
            logger.error(ERRORE, dbEx);
            return false;
        }
    }

    @Override
    public Integer getValutazionebyEmail(String agentMail) {
        String ratingQuery = "select valutazione from agente a where email = ?";
        return getInteger(agentMail, ratingQuery);
    }

    private Integer getInteger(String identifierParam, String selectQuery) {
        try (PreparedStatement stmt = connection.prepareStatement(selectQuery)) {
            stmt.setString(1, identifierParam);
            ResultSet resultSet = stmt.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
            return null;
        } catch (SQLException ex) {
            logger.error(ERRORE, ex);
            return null;
        }
    }

    public Integer getValutazione(String agentFiscalCode) {
        String valutazioneQuery = "select valutazione from agente a where cf = ?";
        return getInteger(agentFiscalCode, valutazioneQuery);
    }

    @Override
    public List<GetAllRicercheResponse> getAllRicerche(String agentCf) {
        String ricercheQuery = "select id_ricerca,agentechecerca,n°stanze,classe_energetica,prezzo,comune,città,tipovendita,prezzo_max from ricerca r where r.agentechecerca=?";
        return getGetAllRicercheResponses(agentCf, ricercheQuery, connection);
    }

    static void getRicerche(String agentId, List<GetAllRicercheResponse> resultsList, PreparedStatement preparedStmt) throws SQLException {
        preparedStmt.setString(1, agentId);
        ResultSet resultSet = preparedStmt.executeQuery();
        while (resultSet.next()) {
            Double priceVal = resultSet.getDouble(5);
            if (resultSet.wasNull()) {
                priceVal = null;
            }
            Double maxPriceVal = resultSet.getDouble(9);
            if (resultSet.wasNull()) {
                maxPriceVal = null;
            }
            Integer roomsCount = resultSet.getInt(3);
            if (resultSet.wasNull()) {
                roomsCount = null;
            }
            GetAllRicercheResponse researchItem = new GetAllRicercheResponse.Builder()
                    .idRicerca(resultSet.getInt(1))
                    .cliente(resultSet.getString(2))
                    .numeroStanze(roomsCount)
                    .classeEnergetica(ClasseEnergetica.fromString(resultSet.getString(4)))
                    .prezzo(priceVal)
                    .comune(resultSet.getString(6))
                    .citta(resultSet.getString(7))
                    .tipovendita(TipoVendita.fromString(resultSet.getString(8)))
                    .prezzoMassimo(maxPriceVal)
                    .build();
            resultsList.add(researchItem);
        }
    }

    public boolean updatePassword(UpdatePasswordRequestDTO updatePwdReq) {
        String updatePwdQuery = "Update agente set password=? where email=?";
        return updatePassDB(updatePwdReq, updatePwdQuery, connection);
    }

    static boolean updatePassDB(UpdatePasswordRequestDTO pwdReq, String updateQuery, Connection dbConn) {
        try (PreparedStatement pStmt = dbConn.prepareStatement(updateQuery)) {
            pStmt.setString(1, pwdReq.getPassword());
            pStmt.setString(2, pwdReq.getEmail());
            int affectedRowsCount = pStmt.executeUpdate();
            return affectedRowsCount > 0;
        } catch (SQLException sqlErr) {
            logger.error("Si è verificato un errore: nel db ", sqlErr);
            return false;
        }
    }
}