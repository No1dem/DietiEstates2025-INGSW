package it.unina.dietiEstates25.dao;

import it.unina.dietiEstates25.db.DatabaseConnection;
import it.unina.dietiEstates25.dto.request.AddVisitaRequestDTO;
import it.unina.dietiEstates25.dto.response.*;
import it.unina.dietiEstates25.model.TipoImmobile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class VisitaDaoImpl implements VisitaDao {

    // =========================================================================
    // COSTANTI E VARIABILI DI ISTANZA
    // =========================================================================
    private static final Logger logger = LoggerFactory.getLogger(VisitaDaoImpl.class);
    private static final String ERRORE = "Si è verificato un errore nel db";
    
    private Connection connection;

    // =========================================================================
    // COSTRUTTORE
    // =========================================================================
    public VisitaDaoImpl() throws SQLException {
        try {
            this.connection = DatabaseConnection.getConnection();
        } catch (SQLException dbException) {
            logger.error(ERRORE, dbException);
        }
    }

    // =========================================================================
    // METODI PUBBLICI - CREAZIONE ED ELIMINAZIONE (Write/Delete)
    // =========================================================================
    @Override
    public VisitaResponseDTO addVisita(AddVisitaRequestDTO visitReq) {
        String insertVisitQuery = "INSERT INTO visita (datavisita, orainiziovisita,accettata, descrizione, clienteprenotatovisita, agenteriferimento, immobilevisita,orafineVisita) " +
                "VALUES (?, ?,?, ?, ?, ?, ?, ?)";
        
        List<Integer> agentPropertiesList = getAllImmobileByCf(visitReq.getCfAgente());
        if (!controllaSeImmobileInseritoEDellagente(visitReq.getImmobileDaVisitare(), agentPropertiesList)) {
            return new VisitaResponseDTO(false, true, true);
        }
        
        try (PreparedStatement insertStmt = connection.prepareStatement(insertVisitQuery)) {
            insertStmt.setObject(1, visitReq.getDataVisita());
            insertStmt.setObject(2, visitReq.getHoraInizioVisita());
            insertStmt.setObject(3, null);
            insertStmt.setString(4, visitReq.getDescrizione());
            insertStmt.setString(5, visitReq.getEmailClienteChePrenotaVisita());
            insertStmt.setString(6, visitReq.getCfAgente());
            insertStmt.setInt(7, visitReq.getImmobileDaVisitare());
            insertStmt.setObject(8, visitReq.getHoraFineVisita());
            
            int affectedRows = insertStmt.executeUpdate();
            if (affectedRows > 0) {
                return new VisitaResponseDTO(true, false);
            }
        } catch (SQLException sqlEx) {
            logger.error(ERRORE, sqlEx);
            return new VisitaResponseDTO(false, true);
        }
        return new VisitaResponseDTO(false, true);
    }

    @Override
    public boolean eliminaVisita(AddVisitaRequestDTO visitToDelete) {
        String deleteNotificationQuery = """
        DELETE FROM notifica
        WHERE id_visita IN (
            SELECT id_visita FROM visita
            WHERE datavisita = ?
            AND oraInizioVisita = ?
            AND clientePrenotatoVisita = ?
            AND agenteRiferimento = ?
            AND immobileVisita = ?
            AND oraFineVisita = ?
        );
    """;

        String deleteVisitQuery = """
        DELETE FROM visita
        WHERE datavisita = ?
        AND oraInizioVisita = ?
        AND clientePrenotatoVisita = ?
        AND agenteRiferimento = ?
        AND immobileVisita = ?
        AND oraFineVisita = ?;
    """;

        try (PreparedStatement delNotifStmt = connection.prepareStatement(deleteNotificationQuery);
             PreparedStatement delVisitStmt = connection.prepareStatement(deleteVisitQuery)) {
            
            for (int i = 1; i <= 6; i++) {
                delNotifStmt.setObject(i, getParamByIndex(visitToDelete, i));
                delVisitStmt.setObject(i, getParamByIndex(visitToDelete, i));
            }
            delNotifStmt.executeUpdate();
            
            int deletedRowsCount = delVisitStmt.executeUpdate();
            return deletedRowsCount > 0;
            
        } catch (SQLException dbErr) {
            dbErr.printStackTrace();
            return false;
        }
    }

    // =========================================================================
    // METODI PUBBLICI - LETTURA E VERIFICA DATI (Read)
    // =========================================================================
    @Override
    public List<GetVisiteResponse> getAllVisiteAccettateByCF(String agentFiscalCode) {
        String fetchVisitsQuery = "select datavisita,orainiziovisita,orafineVisita,descrizione,clienteprenotatovisita from visita where agenteriferimento = ? and accettata=true";
        List<GetVisiteResponse> acceptedVisitsList = new ArrayList<>();
        
        try (PreparedStatement pStmt = connection.prepareStatement(fetchVisitsQuery)) {
            pStmt.setString(1, agentFiscalCode);
            ResultSet rsVisits = pStmt.executeQuery();
            
            while (rsVisits.next()) {
                LocalDate visitDate = rsVisits.getObject(1, LocalDate.class);
                LocalTime startTime = rsVisits.getObject(2, LocalTime.class);
                LocalTime endTime = rsVisits.getObject(3, LocalTime.class);
                String visitDescription = rsVisits.getString(4);
                String clientBookingEmail = rsVisits.getString(5);
                
                GetVisiteResponse parsedVisit = new GetVisiteResponse(clientBookingEmail, visitDate, startTime, visitDescription, endTime);
                acceptedVisitsList.add(parsedVisit);
            }
        } catch (SQLException ex) {
            logger.error(ERRORE, ex);
            return Collections.emptyList();
        }
        return acceptedVisitsList;
    }

    @Override
    public List<Integer> getAllImmobileByCf(String ownerCf) {
        String fetchPropIdsQuery = "select id_immobile from immobile where agenteproprietario = ?";
        List<Integer> propertyIdsList = new ArrayList<>();
        
        try (PreparedStatement queryStmt = connection.prepareStatement(fetchPropIdsQuery)) {
            queryStmt.setString(1, ownerCf);
            ResultSet rsProps = queryStmt.executeQuery();
            
            while (rsProps.next()) {
                propertyIdsList.add(rsProps.getInt(1));
            }
            return propertyIdsList;
        } catch (SQLException sqlError) {
            logger.error(ERRORE, sqlError);
            return Collections.emptyList();
        }
    }

    @Override
    public InformazioniVisitaDTO getInformazioniVisita(int targetNotificationId) {
        String fetchInfoQuery = """
                select f.assert_id, a.nome,a.cognome,clienteproprietarioNotifica, i.tipoimmobile,i.indirizzo,i.comune,i.numeroCivico,i.id_immobile, v.datavisita,orainiziovisita,orafinevisita from notifica n join visita v on n.id_visita=v.id_visita
                join immobile i on v.immobilevisita = i.id_immobile join agente a on i.agenteProprietario =a.cf join foto f on a.cf = f.agente
                where n.id_notifica = ?;
            """;
            
        try (PreparedStatement statement = connection.prepareStatement(fetchInfoQuery)) {
            statement.setInt(1, targetNotificationId);
            ResultSet infoRs = statement.executeQuery();
            
            if (infoRs.next()) {
                ArrayList<String> agentPhotoUrls = new ArrayList<>();
                recuperaFotoAgente(infoRs, agentPhotoUrls);
                
                return new InformazioniVisitaDTO.Builder()
                        .fotoProfiloAgente(agentPhotoUrls.get(0))
                        .agente(infoRs.getString(2) + " " + infoRs.getString(3))
                        .emailClientePrenotatoVisita(infoRs.getString((4)))
                        .tipoImmobile(TipoImmobile.valueOf(infoRs.getString(5)))
                        .viaImmobile(infoRs.getString(6))
                        .comune(infoRs.getString(7))
                        .numeroCivico(infoRs.getString(8))
                        .idImmobile(infoRs.getInt(9))
                        .dataVisita(infoRs.getDate(10).toLocalDate())
                        .oraVisita(infoRs.getTime(11).toLocalTime())
                        .oraFineVisita(infoRs.getTime(12).toLocalTime())
                        .build();
            }
        } catch (SQLException e) {
            logger.error(ERRORE, e);
            return null;
        }
        return null;
    }

    @Override
    public List<DateEOreOccupateAgente> getDateEOreOccupateAgente(String agentId) {
        String fetchDatesQuery = "select datavisita,orainiziovisita,orafinevisita from visita v where v.agenteriferimento=? and accettata=true";
        List<DateEOreOccupateAgente> occupiedSlotsList = new ArrayList<>();
        
        try (PreparedStatement stmt = connection.prepareStatement(fetchDatesQuery)) {
            stmt.setString(1, agentId);
            ResultSet rsSlots = stmt.executeQuery();
            
            while (rsSlots.next()) {
                DateEOreOccupateAgente slotData = new DateEOreOccupateAgente(
                        rsSlots.getDate(1).toLocalDate(), 
                        rsSlots.getTime(2).toLocalTime(), 
                        rsSlots.getTime(3).toLocalTime());
                occupiedSlotsList.add(slotData);
            }
            return occupiedSlotsList;
        } catch (SQLException exDb) {
            logger.error(ERRORE, exDb);
            return occupiedSlotsList;
        }
    }

    @Override
    public CheckVisitaPerClienteDTO controllaSeClienteHaGiaVisitaPerImmobile(String clientMail, int propertyId) {
        String checkExistingVisitSql = "select 1 from visita v where v.accettata is NULL and v.clienteprenotatovisita=? and v.immobilevisita=?";
        
        try (PreparedStatement checkStmt = connection.prepareStatement(checkExistingVisitSql)) {
            checkStmt.setString(1, clientMail);
            checkStmt.setInt(2, propertyId);
            ResultSet checkRs = checkStmt.executeQuery();
            
            if (checkRs.next()) {
                return new CheckVisitaPerClienteDTO(false, false, true);
            } else {
                return new CheckVisitaPerClienteDTO(false, true, false);
            }
        } catch (SQLException sqlE) {
            logger.error(ERRORE, sqlE);
            return new CheckVisitaPerClienteDTO(true, false, false);
        }
    }

    // =========================================================================
    // METODI PRIVATI E PACKAGE-PRIVATE (Helper e Utility)
    // =========================================================================
    private boolean controllaSeImmobileInseritoEDellagente(int targetPropertyId, List<Integer> agentPropertiesList) {
        for (int currentPropId : agentPropertiesList) {
            if (currentPropId == targetPropertyId) {
                return true;
            }
        }
        return false;
    }

    private Object getParamByIndex(AddVisitaRequestDTO visitDto, int paramIndex) {
        return switch (paramIndex) {
            case 1 -> visitDto.getDataVisita();
            case 2 -> visitDto.getHoraInizioVisita();
            case 3 -> visitDto.getEmailClienteChePrenotaVisita();
            case 4 -> visitDto.getCfAgente();
            case 5 -> visitDto.getImmobileDaVisitare();
            case 6 -> visitDto.getHoraFineVisita();
            default -> throw new IllegalArgumentException("Indice parametro non valido");
        };
    }

    private static void recuperaFotoAgente(ResultSet resultSetData, ArrayList<String> urlListToFill) throws SQLException {
        String photoAssetId = resultSetData.getString(1);
        urlListToFill.add(photoAssetId);
    }
}