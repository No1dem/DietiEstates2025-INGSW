package it.unina.dietiEstates25.dao;

import it.unina.dietiEstates25.db.DatabaseConnection;
import it.unina.dietiEstates25.dto.request.UpdateDatiRequestDTO;
import it.unina.dietiEstates25.dto.response.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static it.unina.dietiEstates25.dao.NotificaDaoImpl.esiste;

public class UserDaoImpl implements UserDao {

    // =========================================================================
    // COSTANTI E VARIABILI DI ISTANZA
    // =========================================================================
    private static final Logger logger = LoggerFactory.getLogger(UserDaoImpl.class);
    private static final String ERRORE = "Si è verificato un errore nel db";
    private static final String ADMIN = "admin";

    private Connection connection;

    // =========================================================================
    // COSTRUTTORE
    // =========================================================================
    public UserDaoImpl() throws SQLException {
        try {
            this.connection = DatabaseConnection.getConnection();
        } catch (SQLException dbException) {
            logger.error(ERRORE, dbException);
        }
    }

    // =========================================================================
    // METODI PUBBLICI E INTERFACCIA (CRUD e Autenticazione)
    // =========================================================================
    @Override
    public UpdateDatiResponseDTO updateDatiAgente(UpdateDatiRequestDTO newAgentData, String agentEmail) {
        String updateAgentSql = "UPDATE AGENTE SET nome=?,cognome=?,telefono=?,email=? where email = ?";
        return aggiornaDati(newAgentData, agentEmail, updateAgentSql);
    }

    @Override
    public UpdateDatiResponseDTO updateDatiCliente(UpdateDatiRequestDTO newClientData, String currentEmail) {
        String updateClientSql = "UPDATE CLIENTE SET nome=?,cognome=?,telefono=?,email=? where email = ?";
        return aggiornaDati(newClientData, currentEmail, updateClientSql);
    }

    @Override
    public UpdateDatiResponseDTO updateDatiGestore(UpdateDatiRequestDTO newGestoreData, String currentEmail) {
        String updateGestoreSql = "UPDATE GESTOREAGENZIAIMMOBILIARE SET nome=?,cognome=?,telefono=?,email=? where email = ?";
        return aggiornaDati(newGestoreData, currentEmail, updateGestoreSql);
    }

    @Override
    public AgenteDTO add(String newUserEmail, String newUserPwd) throws SQLException {
        String checkUserQuery = """
        
                SELECT email, ruolo, admin
                        FROM (
                            SELECT email, ruolo, false AS admin FROM cliente
                            UNION
                            SELECT email, ruolo, admin FROM gestoreagenziaimmobiliare
                            UNION
                            SELECT email, ruolo, false AS admin FROM agente
                        ) AS combined
                        WHERE email = ?;
                        
        """;
        String insertUserQuery = "INSERT INTO cliente (email, password) VALUES (?, ?)";
        try (PreparedStatement queryStmt = connection.prepareStatement(checkUserQuery)) {
            queryStmt.setString(1, newUserEmail);
            ResultSet rsData = queryStmt.executeQuery();
            if (utenteEsiste(rsData)) {
                AgenteDTO agentResponse = new AgenteDTO(false, false);
                agentResponse.setRole(rsData.getString("ruolo"));
                agentResponse.setEmail(newUserEmail);
                agentResponse.setAdmin(rsData.getBoolean(ADMIN));
                return agentResponse;
            } else {
                return aggiungiUtente(newUserEmail, newUserPwd, insertUserQuery);
            }
        } catch (SQLException sqlError) {
            logger.error(ERRORE, sqlError);
        }
        return null;
    }

    @Override
    public LoginUtenteResponse login(String loginEmail, String loginPwd) {
        String loginCheckQuery = """
                SELECT email, ruolo, admin
                        FROM (
                            SELECT email, ruolo, false AS admin FROM cliente
                                    UNION
                            SELECT email, ruolo, admin FROM gestoreagenziaimmobiliare
                                    UNION
                            SELECT email, ruolo, false AS admin FROM agente
                        ) AS combined
                        WHERE email = ?
                        
        """;
        try (PreparedStatement loginStmt = connection.prepareStatement(loginCheckQuery)) {
            loginStmt.setString(1, loginEmail);
            ResultSet loginRs = loginStmt.executeQuery();
            if (utenteEsiste(loginRs)) {
                LoginUtenteResponse responseData = new LoginUtenteResponse();
                responseData.setRole(loginRs.getString("ruolo"));
                responseData.setAdmin(loginRs.getBoolean(ADMIN));
                return responseData;
            }
        } catch (SQLException ex) {
            logger.error(ERRORE, ex);
        }
        return null;
    }

    @Override
    public String getPassword(String targetEmail) throws SQLException {
        String pwdQuery = """
                SELECT password
                        FROM (
                            SELECT email,password FROM cliente
                                    UNION
                            SELECT email,password FROM gestoreagenziaimmobiliare
                                    UNION
                            SELECT email,password FROM agente
                        ) AS combined
                        WHERE email = ?;
                        
        """;
        try (PreparedStatement pwdStmt = connection.prepareStatement(pwdQuery)) {
            pwdStmt.setString(1, targetEmail);
            ResultSet pwdRs = pwdStmt.executeQuery();
            if (utenteEsiste(pwdRs)) {
                return pwdRs.getString("password");
            }
        } catch (SQLException sqlEx) {
            logger.error(ERRORE, sqlEx);
        }
        return "";
    }

    public UpdateDatiResponseDTO aggiornaDati(UpdateDatiRequestDTO newData, String identifierValue, String updateSql) {
        if (isEmailRegistrata(newData.getEmail(), identifierValue)) {
            return new UpdateDatiResponseDTO(true, true);
        }
        try (PreparedStatement updateStmt = connection.prepareStatement(updateSql)) {
            updateStmt.setString(1, newData.getNome());
            updateStmt.setString(2, newData.getCognome());
            updateStmt.setString(3, newData.getTelefono());
            updateStmt.setString(4, newData.getEmail());
            updateStmt.setString(5, identifierValue);
            
            int updatedRows = updateStmt.executeUpdate();
            System.out.println(updatedRows);
            return new UpdateDatiResponseDTO(false, updatedRows <= 0);
        } catch (SQLException dbError) {
            dbError.printStackTrace();
            logger.error(ERRORE, dbError);
            return new UpdateDatiResponseDTO(false, true);
        }
    }

    // =========================================================================
    // METODI PRIVATI E PACKAGE-PRIVATE (Helper e Utility)
    // =========================================================================
    static DatiDTO getDatiDTO(String userCf, String fetchQuery, Connection dbConn) {
        try (PreparedStatement fetchStmt = dbConn.prepareStatement(fetchQuery)) {
            fetchStmt.setString(1, userCf);
            try (ResultSet fetchRs = fetchStmt.executeQuery()) {
                if (fetchRs.next()) {
                    return new DatiDTO(
                            fetchRs.getString("nome"),
                            fetchRs.getString("cognome"),
                            fetchRs.getString("email"),
                            fetchRs.getString("telefono"),
                            fetchRs.getString("Bio"),
                            fetchRs.getString("cf")
                    );
                } else {
                    return null;
                }
            }
        } catch (SQLException eSQL) {
            return null;
        }
    }

    private AgenteDTO aggiungiUtente(String mail, String pwd, String insertSql) throws SQLException {
        try (PreparedStatement insertionStmt = connection.prepareStatement(insertSql)) {
            insertionStmt.setString(1, mail);
            insertionStmt.setString(2, pwd);
            insertionStmt.executeUpdate();
            
            AgenteDTO newAgent = new AgenteDTO(false, false);
            newAgent.setRole("cliente");
            newAgent.setEmail(mail);
            return newAgent;
        }
    }

    private static boolean utenteEsiste(ResultSet resultSet) throws SQLException {
        return resultSet.next();
    }

    private boolean isEmailRegistrata(String newEmail, String oldEmail) {
        if (newEmail.equals(oldEmail)) {
            return false;
        }
        String checkEmailSql = "select email from gestoreagenziaimmobiliare union select email from cliente union select email from agente";
        return esistenzaCampo(newEmail, checkEmailSql);
    }

    private boolean esistenzaCampo(String valueToFind, String searchQuery) {
        return esiste(valueToFind, searchQuery, connection);
    }
}