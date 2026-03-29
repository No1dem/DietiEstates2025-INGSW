package it.unina.dietiEstates25.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.unina.dietiEstates25.db.DatabaseConnection;
import it.unina.dietiEstates25.dto.response.NotificaDTO;
import it.unina.dietiEstates25.model.CategoriaNotifica;
import it.unina.dietiEstates25.model.Notifica;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static it.unina.dietiEstates25.dao.ImmobileDaoImpl.update;

public class NotificaDaoImpl implements NotificaDao {

    // =========================================================================
    // COSTANTI E VARIABILI DI ISTANZA
    // =========================================================================
    private static final Logger logger = LoggerFactory.getLogger(NotificaDaoImpl.class);
    private static final String ERRORE = "Si è verificato un errore nel db";
    
    private static final String CLIENTE = "clienteNotificato";
    private static final String AGENTE = "agenteNotificato";
    private static final String GESTORE = "gestoreNotificato";

    private static final Map<String, String> COLUMN_MAP = Map.of(
            CLIENTE, CLIENTE,
            AGENTE, AGENTE,
            GESTORE, GESTORE
    );

    private Connection connection;

    // =========================================================================
    // COSTRUTTORE
    // =========================================================================
    public NotificaDaoImpl() {
        try {
            this.connection = DatabaseConnection.getConnection();
        } catch (SQLException dbException) {
            logger.error(ERRORE, dbException);
        }
    }

    // =========================================================================
    // METODI PUBBLICI - LETTURA (Read)
    // =========================================================================
    @Override
    public List<Notifica> getNotofAdmin(String adminCf) {
        return getNotifiche(GESTORE, adminCf);
    }

    @Override
    public List<Notifica> getNotificaAgente(String agentCf) {
        return getNotifiche(AGENTE, agentCf);
    }

    @Override
    public List<Notifica> getNotificaCliente(String clientEmail) {
        return getNotifiche(CLIENTE, clientEmail);
    }

    // =========================================================================
    // METODI PUBBLICI - SCRITTURA / AGGIORNAMENTO (Update)
    // =========================================================================
    @Override
    public boolean annullaInvioNotifica(int notificationId) {
        String cancelQuery = "UPDATE NOTIFICA SET accettata = null where id_notifica = ?";
        return update(notificationId, cancelQuery, connection, logger, ERRORE);
    }

    @Override
    public NotificaDTO setNotificaAccepted(int targetNotificationId) {
        return setNotificaStato(targetNotificationId, true);
    }

    @Override
    public NotificaDTO setNotificaRejected(int targetNotificationId) {
        return setNotificaStato(targetNotificationId, false);
    }

    // =========================================================================
    // METODI PRIVATI E HELPER (Utility)
    // =========================================================================
    private List<Notifica> getNotifiche(String columnType, String searchParam) {
        List<Notifica> notificationsList = new ArrayList<>();
        String safeColumn = COLUMN_MAP.get(columnType);
        
        if (safeColumn == null) {
            logger.error("Colonna non valida: {}", columnType);
            return notificationsList;
        }
        
        String fetchQuery = "SELECT titolo, contenuto, id_notifica, categoria, id_immobile FROM notifica WHERE "
                + safeColumn + " = ? AND accettata IS NULL AND da_inviare = true";
                
        try (PreparedStatement pStmt = connection.prepareStatement(fetchQuery)) {
            pStmt.setString(1, searchParam); 
            ResultSet results = pStmt.executeQuery();
            aggiungiNotifiche(notificationsList, results);
        } catch (SQLException sqlEx) {
            logger.error(ERRORE, sqlEx);
        }
        return notificationsList;
    }

    private NotificaDTO setNotificaStato(int notifId, boolean isAccepted) {
        String updateStatusSql = "UPDATE notifica SET accettata = ? WHERE id_notifica = ?";
        try (PreparedStatement updateStmt = connection.prepareStatement(updateStatusSql)) {
            updateStmt.setBoolean(1, isAccepted);
            updateStmt.setInt(2, notifId);
            
            int updatedRowsCount = updateStmt.executeUpdate();
            if (updatedRowsCount > 0) {
                NotificaDTO responseDto = new NotificaDTO();
                if (isAccepted) {
                    responseDto.setNumeroNotificheAccettate(updatedRowsCount);
                } else {
                    responseDto.setNumeroNotificheRifiutate(updatedRowsCount);
                }
                return responseDto;
            }
            return null;
        } catch (SQLException sqlErr) {
            logger.error(ERRORE, sqlErr);
            return null;
        }
    }

    private void aggiungiNotifiche(List<Notifica> listToPopulate, ResultSet rsData) throws SQLException {
        while (rsData.next()) {
            String notifTitle = rsData.getString("titolo");
            String notifBody = rsData.getString("contenuto");
            int notifId = rsData.getInt("id_notifica");
            String catStr = rsData.getString("categoria");
            int propId = rsData.getInt("id_immobile");
            
            CategoriaNotifica enumCategory = CategoriaNotifica.fromString(catStr);
            Notifica newNotification = new Notifica(notifTitle, notifBody, notifId, enumCategory, propId);
            listToPopulate.add(newNotification);
        }
    }

    static boolean esiste(String fieldValue, String queryStr, Connection dbConn) {
        try (PreparedStatement testStmt = dbConn.prepareStatement(queryStr)) {
            ResultSet rsCheck = testStmt.executeQuery();
            while (rsCheck.next()) {
                if (rsCheck.getString(1).equals(fieldValue)) {
                    return true;
                }
            }
            return false;
        } catch (SQLException eSQL) {
            return false;
        }
    }
}