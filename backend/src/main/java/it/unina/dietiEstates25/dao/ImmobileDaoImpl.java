package it.unina.dietiEstates25.dao;

import it.unina.dietiEstates25.db.DatabaseConnection;
import it.unina.dietiEstates25.dto.request.AddImmobileRequestDTO;
import it.unina.dietiEstates25.dto.request.RicercaImmobileDTORequest;
import it.unina.dietiEstates25.dto.response.DatiImmobileDTO;
import it.unina.dietiEstates25.dto.response.ImmobileDTO;
import it.unina.dietiEstates25.dto.response.ImmobileResponseRicercaDTO;
import it.unina.dietiEstates25.model.*;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ImmobileDaoImpl implements ImmobileDao {

    // =========================================================================
    // COSTANTI E VARIABILI DI ISTANZA
    // =========================================================================
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(ImmobileDaoImpl.class);
    private static final String ERRORE = "Si è verificato un errore nel db";
    
    private Connection connection;

    // =========================================================================
    // COSTRUTTORE
    // =========================================================================
    public ImmobileDaoImpl() throws SQLException {
        try {
            this.connection = DatabaseConnection.getConnection();
        } catch (SQLException dbEx) {
            logger.error(ERRORE, dbEx);
        }
    }

    // =========================================================================
    // METODI PUBBLICI - SCRITTURA E CANCELLAZIONE (Write)
    // =========================================================================
    @Override
    public ImmobileDTO addImmobile(AddImmobileRequestDTO propertyReq) {
        String insertQuery = "insert into immobile(tipoImmobile,tipoVendita,descrizione,indirizzo," +
                "n°stanze,piano,classeenergetica,superficie,arredamento,prezzo,spesecondominiali,n°bagni," +
                "n°cucine,n°soggiorni,agenteproprietario,città,lat,long,titolo,comune,numeroCivico,presenza_ascensore) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        try (PreparedStatement insertStmt = connection.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS)) {
            setSQL(propertyReq, insertStmt);
            int affectedRows = insertStmt.executeUpdate();
            if (affectedRows > 0) {
                aggiungiFoto(insertStmt, propertyReq.getFotoDelImmobile());
                return new ImmobileDTO(true, false, false);
            } else {
                return new ImmobileDTO(false, true, true);
            }
        } catch (SQLException sqlError) {
            logger.error(ERRORE, sqlError);
            return new ImmobileDTO(false, true, true);
        }
    }

    @Override
    public boolean deleteImmobile(int propertyId) {
        String deleteQuery =
                """
                        delete
                        from immobile
                        where id_immobile = ?
                """;
        return update(propertyId, deleteQuery, connection, logger, ERRORE);
    }

    // =========================================================================
    // METODI PUBBLICI - LETTURA DATI E RICERCA (Read)
    // =========================================================================
    @Override
    public List<Immobile> getAnnunciByEmail(String agentEmail) {
        List<Immobile> propertyList = new ArrayList<>();
        String selectSql = "SELECT immobile.titolo,immobile.tipoimmobile,immobile.città FROM immobile INNER JOIN agente ON immobile.agenteproprietario = agente.cf where agente.email = ?";
        try (PreparedStatement pStmt = connection.prepareStatement(selectSql)) {
            pStmt.setString(1, agentEmail);
            ResultSet rs = pStmt.executeQuery();
            aggiungiAnnunci(propertyList, rs);
        } catch (SQLException dbException) {
            logger.error(ERRORE, dbException);
        }
        return propertyList;
    }

    @Override
    public List<ImmobileResponseRicercaDTO> getImmobiliRicerca(RicercaImmobileDTORequest searchReq) {
        List<ImmobileResponseRicercaDTO> searchResults = new ArrayList<>();
        String searchQuery = """
                SELECT i.id_immobile,i.tipoImmobile,i.tipoVendita,i.descrizione,i.indirizzo,i.comune,i.n°stanze,i.piano,i.classeEnergetica,
           i.superficie,i.arredamento,i.prezzo,i.speseCondominiali,i.n°bagni,i.n°cucine,i.n°soggiorni,i.agenteProprietario,i.città,i.lat,i.long,i.titolo,
            i.numerocivico,a.nome,a.cognome,i.presenza_ascensore
                FROM immobile i
                JOIN agente a ON i.agenteProprietario = a.cf
               WHERE tipovendita = COALESCE(?, tipovendita)
               AND "n°stanze" = COALESCE(?, "n°stanze")
               AND classeenergetica = COALESCE(?, CLASSEENERGETICA)
              AND CAST(prezzo AS DECIMAL(15, 0)) >= CAST(? AS DECIMAL(15, 0))
             AND CAST(prezzo AS DECIMAL(15, 0)) <= CAST(? AS DECIMAL(15, 0))
             AND LOWER(comune) LIKE LOWER(COALESCE(CONCAT('%', ?, '%'), '%'))
            AND LOWER(città) = LOWER(COALESCE(?, città))
           """;
        try (PreparedStatement queryStmt = connection.prepareStatement(searchQuery)) {
            queryStmt.setObject(1, (searchReq.getTipologiaVendita() != null) ? searchReq.getTipologiaVendita() : null, Types.OTHER);
            queryStmt.setObject(2, (searchReq.getNumeroStanze() != null) ? searchReq.getNumeroStanze() : null, Types.INTEGER);
            queryStmt.setObject(3, (searchReq.getClasseEnergetica() != null) ? searchReq.getClasseEnergetica() : null, Types.OTHER);
            queryStmt.setObject(4, (searchReq.getPrezzoMinimo() != null) ? searchReq.getPrezzoMinimo() : 0, Types.DECIMAL);
            queryStmt.setObject(5, (searchReq.getPrezzoMaximo() != null) ? searchReq.getPrezzoMaximo() : 999999999, Types.DECIMAL);
            queryStmt.setString(6,
                    (searchReq.getComune() != null && !searchReq.getComune().trim().isEmpty()) ? searchReq.getComune() : null);
            queryStmt.setObject(7,
                    (searchReq.getCitta() != null && !searchReq.getCitta().trim().isEmpty()) ? searchReq.getCitta() : null,
                    Types.OTHER);
            ResultSet queryRes = queryStmt.executeQuery();
            aggiungiImmobili(queryRes, searchResults);
            aggiungiImmobileAllaRicercaDellUtente(searchReq);
        } catch (SQLException ex) {
            ex.printStackTrace();
            logger.error(ERRORE, ex);
            return Collections.emptyList();
        }
        return searchResults;
    }

    @Override
    public DatiImmobileDTO getInfoAboutImmobile(int targetId) {
        String infoQuery = """
             SELECT indirizzo,comune,città,agenteProprietario,numeroCivico
              from immobile where id_immobile=?
         """;
        try (PreparedStatement stmt = connection.prepareStatement(infoQuery)) {
            stmt.setInt(1, targetId);
            ResultSet rs = stmt.executeQuery();
            if (!rs.next()) {
                return null;
            }
            return new DatiImmobileDTO(rs.getString("indirizzo"),
                    rs.getString("comune"), rs.getString("agenteProprietario"),
                    rs.getString("città"), rs.getString("numeroCivico"));
        } catch (SQLException sqlErr) {
            logger.error(ERRORE, sqlErr);
            return null;
        }
    }

    public List<ImmobileResponseRicercaDTO> getImmobiliForAgente(String agentEmail) {
        List<ImmobileResponseRicercaDTO> agentProps = new ArrayList<>();
        String fetchAgentPropsQuery = """
        SELECT i.id_immobile,i.tipoImmobile,i.tipoVendita,i.descrizione,i.indirizzo,i.comune,i.n°stanze,i.piano,i.classeEnergetica,
           i.superficie,i.arredamento,i.prezzo,i.speseCondominiali,i.n°bagni,i.n°cucine,i.n°soggiorni,i.agenteProprietario,i.città,i.lat,i.long,i.titolo,
            i.numerocivico,a.nome,a.cognome,i.presenza_ascensore
        FROM immobile i INNER JOIN agente a ON i.agenteproprietario = a.cf WHERE a.email = ?
       """;
        try (PreparedStatement stmt = connection.prepareStatement(fetchAgentPropsQuery)) {
            stmt.setString(1, agentEmail);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                ImmobileResponseRicercaDTO parsedDto = makeImmobile(rs);
                agentProps.add(parsedDto);
            }
        } catch (SQLException e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Errore SQL", e);
        }

        return agentProps;
    }

    @Override
    public ImmobileResponseRicercaDTO getInfoAboutImmobileById(int propId) {
        ImmobileResponseRicercaDTO resultDto = null;
        String fetchByIdQuery =
                """
                SELECT i.id_immobile,i.tipoImmobile,i.tipoVendita,i.descrizione,i.indirizzo,i.comune,i.n°stanze,i.piano,i.classeEnergetica,
                i.superficie,i.arredamento,i.prezzo,i.speseCondominiali,i.n°bagni,i.n°cucine,i.n°soggiorni,i.agenteProprietario,i.città,i.lat,i.long,i.titolo,
                i.numerocivico,a.nome,a.cognome,i.presenza_ascensore FROM immobile i JOIN agente a ON i.agenteProprietario = a.cf where id_immobile=?
                """;
        try (PreparedStatement stmt = connection.prepareStatement(fetchByIdQuery)) {
            stmt.setInt(1, propId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                resultDto = makeImmobile(rs);
            }
        } catch (SQLException e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Errore SQL", e);
        }
        return resultDto;
    }

    // =========================================================================
    // METODI PRIVATI E HELPER (Utility e Metodi Statici)
    // =========================================================================
    static boolean update(int propertyIdTarget, String queryToRun, Connection dbConn, org.slf4j.Logger sysLogger, String errorMsg) {
        try (PreparedStatement pstmt = dbConn.prepareStatement(queryToRun)) {
            pstmt.setInt(1, propertyIdTarget);
            int updatedRows = pstmt.executeUpdate();
            return updatedRows > 0;
        } catch (SQLException sqlE) {
            sysLogger.error(errorMsg, sqlE);
            return false;
        }
    }

    private void aggiungiFoto(PreparedStatement statement, List<String> photoPaths) throws SQLException {
        ResultSet generatedKeys = statement.getGeneratedKeys();
        if (generatedKeys.next()) {
            int newPropertyId = generatedKeys.getInt(1);
            if (!photoPaths.isEmpty()) {
                aggiungiFotoAdImmobile(newPropertyId, photoPaths);
            }
        }
    }

    private void aggiungiFotoAdImmobile(int propId, List<String> images) {
        String insertPhotoSql = "INSERT INTO foto (assert_id, immobileriferimento) VALUES (?, ?)";
        try (PreparedStatement batchStmt = connection.prepareStatement(insertPhotoSql)) {
            batchStmt.setInt(2, propId);
            for (String path : images) {
                batchStmt.setString(1, path);
                batchStmt.addBatch();
            }
            batchStmt.executeBatch();
        } catch (SQLException error) {
            logger.error(ERRORE, error);
        }
    }

    private static void setSQL(AddImmobileRequestDTO reqDto, PreparedStatement stmt) throws SQLException {
        stmt.setObject(1, reqDto.getTipoimmobile(), Types.OTHER);
        stmt.setObject(2, reqDto.getTipovendita(), Types.OTHER);
        stmt.setString(3, reqDto.getDescrizione());
        stmt.setString(4, reqDto.getVia());
        stmt.setInt(5, reqDto.getNumeroStanze());
        stmt.setInt(6, reqDto.getPiano());
        stmt.setObject(7, reqDto.getClasseEnergetica(), Types.OTHER);
        stmt.setInt(8, reqDto.getSuperficie());
        stmt.setObject(9, reqDto.getArredamento(), Types.OTHER);
        stmt.setBigDecimal(10, reqDto.getPrezzo());
        stmt.setBigDecimal(11, reqDto.getSpeseCondominiali());
        stmt.setInt(12, reqDto.getNumeroBagni());
        stmt.setInt(13, reqDto.getNumeroCucine());
        stmt.setInt(14, reqDto.getNumeroSoggiorni());
        stmt.setString(15, reqDto.getCfAgente());
        stmt.setString(16, reqDto.getCitta());
        stmt.setDouble(17, reqDto.getLatitudine());
        stmt.setDouble(18, reqDto.getLongitudine());
        stmt.setString(19, reqDto.getTitolo());
        stmt.setString(20, reqDto.getComune());
        stmt.setString(21, reqDto.getNumeroCivico());
        stmt.setBoolean(22, reqDto.isAscensore());
    }

    private void aggiungiImmobili(ResultSet resultSet, List<ImmobileResponseRicercaDTO> targetList) throws SQLException {
        while (resultSet.next()) {
            ImmobileResponseRicercaDTO parsedProperty = makeImmobile(resultSet);
            parsedProperty.setPrezzo(ImmobileResponseRicercaDTO.formattaStringa(parsedProperty.getPrezzo()));
            parsedProperty.setSpeseCondominiali(ImmobileResponseRicercaDTO.formattaStringa(parsedProperty.getSpeseCondominiali()));
            targetList.add(parsedProperty);
        }
    }

    private void aggiungiImmobileAllaRicercaDellUtente(RicercaImmobileDTORequest searchParams) {
        if (searchParams.getSessioneUtente() != null && !searchParams.getUtenteCheRicerca().isEmpty()) {
            CompletableFuture.runAsync(
                    () -> {
                        try {
                            insertInRicerca(searchParams, connection);
                        } catch (SQLException asyncErr) {
                            logger.error(ERRORE, asyncErr);
                        }
                    }
            );
        }
    }

    private void insertInRicerca(RicercaImmobileDTORequest searchData, Connection dbConnection) throws SQLException {
        String insertSearchSql = "INSERT INTO ricerca (clientechecerca,agentechecerca,gestorechecerca,n°stanze,classe_energetica,prezzo,comune,città,tipovendita,prezzo_max) VALUES (?,?,?,?,?,?,?,?,?,?)";
        try (PreparedStatement insertStmt = dbConnection.prepareStatement(insertSearchSql)) {
            if (isUtenteAGestore(searchData.getSessioneUtente())) {
                insertStmt.setNull(1, Types.VARCHAR);
                insertStmt.setNull(2, Types.VARCHAR);
                insertStmt.setString(3, searchData.getUtenteCheRicerca());
            } else if (isUtenteAgente(searchData.getSessioneUtente())) {
                insertStmt.setNull(1, Types.VARCHAR);
                insertStmt.setString(2, searchData.getUtenteCheRicerca());
                insertStmt.setNull(3, Types.VARCHAR);
            } else {
                insertStmt.setString(1, searchData.getUtenteCheRicerca());
                insertStmt.setNull(2, Types.VARCHAR);
                insertStmt.setNull(3, Types.VARCHAR);
            }
            settaGliAltriParametri(searchData, insertStmt);
            insertStmt.executeUpdate();
        } catch (SQLException logError) {
            logger.error(ERRORE, logError);
        }
    }

    private static void settaGliAltriParametri(RicercaImmobileDTORequest dto, PreparedStatement statement) throws SQLException {
        setParametro(statement, 4, dto.getNumeroStanze(), Types.INTEGER);
        setParametro(statement, 5, dto.getClasseEnergetica() != null ? dto.getClasseEnergetica().toString() : null, Types.VARCHAR);
        setParametro(statement, 6, dto.getPrezzoMinimo(), Types.DOUBLE);
        setParametro(statement, 7, dto.getComune(), Types.VARCHAR);
        setParametro(statement, 8, dto.getCitta(), Types.VARCHAR);
        setParametro(statement, 9, dto.getTipologiaVendita(), Types.OTHER);
        setParametro(statement, 10, dto.getPrezzoMaximo(), Types.DOUBLE);
    }

    private static <T> void setParametro(PreparedStatement stmt, int idx, T val, int type) throws SQLException {
        if (val != null) {
            stmt.setObject(idx, val, type);
        } else {
            stmt.setNull(idx, type);
        }
    }

    private boolean isUtenteAgente(Integer sessionLevel) {
        return sessionLevel == 1;
    }

    private boolean isUtenteAGestore(Integer sessionLevel) {
        return sessionLevel == 0;
    }

    private static ImmobileResponseRicercaDTO makeImmobile(ResultSet resultSet) throws SQLException {
        return ImmobileResponseRicercaDTO.builder()
                .idImmobile(resultSet.getInt(1))
                .tipoimmobile(TipoImmobile.valueOf(resultSet.getString(2)))
                .tipovendita(TipoVendita.valueOf(resultSet.getString(3)))
                .descrizione(resultSet.getString(4))
                .via(resultSet.getString(5))
                .comune(resultSet.getString(6))
                .numeroStanze(resultSet.getInt(7))
                .piano(resultSet.getInt(8))
                .classeEnergetica(ClasseEnergetica.fromString(resultSet.getString(9)))
                .superficie(resultSet.getInt(10))
                .arredamento(Arredamento.fromString(resultSet.getString(11)))
                .prezzo(resultSet.getString(12).replaceAll("\\.000$", ""))
                .speseCondominiali(resultSet.getString(13).replaceAll("\\.000$", ""))
                .numeroBagni(resultSet.getInt(14))
                .numeroCucine(resultSet.getInt(15))
                .numeroSoggiorni(resultSet.getInt(16))
                .cfAgente(resultSet.getString(17))
                .citta(resultSet.getString(18))
                .latitudine(resultSet.getDouble(19))
                .longitudine(resultSet.getDouble(20))
                .titolo(resultSet.getString(21))
                .numeroCivico(resultSet.getString(22))
                .nomeAgente(resultSet.getString("nome"))
                .cognomeAgente(resultSet.getString("cognome"))
                .ascensore(resultSet.getBoolean("presenza_ascensore"))
                .build();
    }

    private void aggiungiAnnunci(List<Immobile> listToFill, ResultSet rs) throws SQLException {
        while (rs.next()) {
            String title = rs.getString("titolo");
            String city = rs.getString("città");
            String typeStr = rs.getString("TipoImmobile");
            Immobile newProp = new Immobile(title, convertiStringConEnum(typeStr), city);
            listToFill.add(newProp);
        }
    }

    private static TipoImmobile convertiStringConEnum(String typeString) {
        TipoImmobile enumValue = null;
        if (typeString != null) {
            try {
                enumValue = TipoImmobile.valueOf(typeString.toUpperCase());
            } catch (IllegalArgumentException enumEx) {
                logger.error(ERRORE, enumEx);
            }
        }
        return enumValue;
    }
}