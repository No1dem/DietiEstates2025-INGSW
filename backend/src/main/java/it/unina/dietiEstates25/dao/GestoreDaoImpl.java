package it.unina.dietiEstates25.dao;

import it.unina.dietiEstates25.db.DatabaseConnection;
import it.unina.dietiEstates25.dto.request.AddGestoreRequestDTO;
import it.unina.dietiEstates25.dto.response.DatiDTO;
import it.unina.dietiEstates25.dto.response.GestoreAgenziaImmobiliareDatiDTO;
import it.unina.dietiEstates25.dto.response.GestoreDTO;
import it.unina.dietiEstates25.dto.response.GetAllRicercheResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static it.unina.dietiEstates25.dao.AgenteDaoImpl.getRicerche;

public class GestoreDaoImpl implements GestoreDao {

    // =========================================================================
    // COSTANTI E VARIABILI DI ISTANZA
    // =========================================================================
    private static final Logger logger = LoggerFactory.getLogger(GestoreDaoImpl.class);
    private static final String ERRORE = "Si è verificato un errore nel db";
    
    private Connection connection;

    // =========================================================================
    // COSTRUTTORE
    // =========================================================================
    public GestoreDaoImpl() throws SQLException {
        try {
            this.connection = DatabaseConnection.getConnection();
        } catch (SQLException dbException) {
            logger.error(ERRORE, dbException);
        }
    }

    // =========================================================================
    // METODI PUBBLICI - CREAZIONE / INSERIMENTO (Write)
    // =========================================================================
    @Override
    public GestoreDTO addGestore(AddGestoreRequestDTO gestoreReq) {
        if (isCFRegistrato(gestoreReq.getCf())) {
            return new GestoreDTO(true, false, false);
        }
        if (isEmailRegistrata(gestoreReq.getEmail())) {
            return new GestoreDTO(false, true, false);
        }
        
        String insertGestoreQuery = """
            INSERT INTO GestoreAgenziaImmobiliare
            (nome, cognome, cf, telefono, password, email, admin, gestoricreati, nomeagenzia)
            VALUES
            (?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;
        
        try (PreparedStatement pStmt = connection.prepareStatement(insertGestoreQuery)) {
            AdminDaoImpl.setSql(pStmt, gestoreReq.getNome(), gestoreReq.getCognome(), gestoreReq.getCf(), gestoreReq.getTelefono(), gestoreReq.getPassword(), gestoreReq.getEmail());
            pStmt.setBoolean(7, false);
            pStmt.setString(8, gestoreReq.getCfGestoreAdmin());
            pStmt.setString(9, gestoreReq.getNomeAgenzia());
            
            int affectedRows = pStmt.executeUpdate();
            if (affectedRows > 0) {
                return new GestoreDTO(false, false, false);
            }
        } catch (SQLException exception) {
            logger.error(ERRORE, exception);
        }
        return new GestoreDTO(false, false, true);
    }

    // =========================================================================
    // METODI PUBBLICI - LETTURA DATI GLOBALI (Read)
    // =========================================================================
    @Override
    public List<String> getAllCF() {
        List<String> cfList = new ArrayList<>();
        String fetchCfQuery = "SELECT cf FROM GestoreAgenziaImmobiliare g union SELECT cf from agente UNION select email from cliente";
        try (PreparedStatement statement = connection.prepareStatement(fetchCfQuery);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                cfList.add(resultSet.getString("cf"));
            }
        } catch (SQLException sqlError) {
            return new ArrayList<>();
        }
        return cfList;
    }

    @Override
    public List<String> getAllEmail() {
        List<String> emailList = new ArrayList<>();
        String fetchEmailQuery = "SELECT email FROM GestoreAgenziaImmobiliare g union select email from agente union select email from cliente";
        try (PreparedStatement prepStmt = connection.prepareStatement(fetchEmailQuery);
             ResultSet results = prepStmt.executeQuery()) {
            while (results.next()) {
                emailList.add(results.getString("email"));
            }
        } catch (SQLException sqlEx) {
            return new ArrayList<>();
        }
        return emailList;
    }

    // =========================================================================
    // METODI PUBBLICI - RICERCA SPECIFICA E PROFILO 
    // =========================================================================
    @Override
    public String getSecurityPassword(String mailParam) {
        String searchPwdQuery = """
             SELECT password
             FROM (
             SELECT email, password FROM agente
             UNION
             SELECT email, password FROM gestoreagenziaimmobiliare
             UNION
             SELECT email,password from cliente
              ) AS subquery
             WHERE email = ?
           """;
        try (PreparedStatement queryStmt = connection.prepareStatement(searchPwdQuery)) {
            queryStmt.setString(1, mailParam);
            ResultSet pwdResult = queryStmt.executeQuery();
            if (pwdResult.next()) {
                return pwdResult.getString("password");
            }
            return null;
        } catch (SQLException err) {
            logger.error(ERRORE, err);
            return null;
        }
    }

    @Override
    public GestoreAgenziaImmobiliareDatiDTO getGestoreByEmail(String gestoreEmail) {
        String selectGestoreQuery = "SELECT nome, cognome, cf, telefono, email FROM gestoreagenziaimmobiliare WHERE email = ?";
        try (PreparedStatement stmt = connection.prepareStatement(selectGestoreQuery)) {
            stmt.setString(1, gestoreEmail);
            ResultSet dataResult = stmt.executeQuery();
            if (dataResult.next()) {
                GestoreAgenziaImmobiliareDatiDTO gestoreDati = new GestoreAgenziaImmobiliareDatiDTO();
                gestoreDati.setNome(dataResult.getString("nome"));
                gestoreDati.setCognome(dataResult.getString("cognome"));
                gestoreDati.setCf(dataResult.getString("cf"));
                gestoreDati.setTelefono(dataResult.getString("telefono"));
                gestoreDati.setEmail(dataResult.getString("email"));
                return gestoreDati;
            } else {
                return null;
            }
        } catch (SQLException ex) {
            return null;
        }
    }

    public List<GetAllRicercheResponse> getAllRicerche(String gestoreCf) {
        String ricercheSql = "select id_ricerca,gestorechecerca,n°stanze,classe_energetica,prezzo,comune,città,tipovendita,prezzo_max from ricerca r where r.gestorechecerca=?";
        return getGetAllRicercheResponses(gestoreCf, ricercheSql, connection);
    }

    @Override
    public List<DatiDTO> getAllAccountAgenti(String adminCf) {
        String agentiQuery = "select nome,cognome,email from agente where gestoreriferimento = ?";
        return getAccount(adminCf, agentiQuery);
    }

    @Override
    public List<DatiDTO> getAllAccountGestori(String creatorCf) {
        String gestoriQuery = "select nome,cognome,email from gestoreagenziaimmobiliare where gestoricreati = ?";
        return getAccount(creatorCf, gestoriQuery);
    }

    // =========================================================================
    // METODI PUBBLICI - ELIMINAZIONE (Delete)
    // =========================================================================
    @Override
    public boolean deleteGestoreByEmail(String targetEmail) {
        String deleteGestoreSql = "delete from gestoreagenziaimmobiliare where email = ?";
        return delete(targetEmail, deleteGestoreSql);
    }

    @Override
    public boolean deleteAgenteByEmail(String agentMail) {
        String deleteAgenteSql = "delete from agente where email = ?";
        return delete(agentMail, deleteAgenteSql);
    }

    // =========================================================================
    // METODI PRIVATI E HELPER (Utility)
    // =========================================================================
    private boolean isCFRegistrato(String fiscalCode) {
        List<String> allCfs = getAllCF();
        for (String currentCf : allCfs) {
            if (currentCf.equals(fiscalCode)) {
                return true;
            }
        }
        return false;
    }

    private boolean isEmailRegistrata(String emailAddress) {
        List<String> allEmails = getAllEmail();
        for (String currentEmail : allEmails) {
            if (currentEmail.equals(emailAddress)) {
                return true;
            }
        }
        return false;
    }

    private boolean delete(String identifierMail, String deleteQuery) {
        try (PreparedStatement delStmt = connection.prepareStatement(deleteQuery)) {
            delStmt.setString(1, identifierMail);
            int deletedRows = delStmt.executeUpdate();
            return deletedRows > 0;
        } catch (SQLException delError) {
            logger.error(ERRORE, delError);
        }
        return false;
    }

    private List<DatiDTO> getAccount(String referenceCf, String fetchAccountsQuery) {
        List<DatiDTO> accountsList = new ArrayList<>();
        try (PreparedStatement accountsStmt = connection.prepareStatement(fetchAccountsQuery)) {
            accountsStmt.setString(1, referenceCf);
            ResultSet accountsRs = accountsStmt.executeQuery();
            while (accountsRs.next()) {
                DatiDTO accountData = new DatiDTO();
                accountData.setNome(accountsRs.getString("nome"));
                accountData.setCognome(accountsRs.getString("cognome"));
                accountData.setEmail(accountsRs.getString("email"));
                accountsList.add(accountData);
            }
            return accountsList;
        } catch (SQLException accEx) {
            logger.error(ERRORE, accEx);
        }
        return accountsList;
    }

    static List<GetAllRicercheResponse> getGetAllRicercheResponses(String userId, String queryToRun, Connection dbConn) {
        List<GetAllRicercheResponse> searchResults = new ArrayList<>();
        try (PreparedStatement qStmt = dbConn.prepareStatement(queryToRun)) {
            getRicerche(userId, searchResults, qStmt);
        } catch (SQLException eSQL) {
            logger.error(ERRORE, eSQL);
            return Collections.emptyList();
        }
        return searchResults;
    }
}