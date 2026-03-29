package it.unina.dietiEstates25.dao;

import it.unina.dietiEstates25.dto.response.GestoreAgenziaImmobiliareDTO;
import it.unina.dietiEstates25.model.AccountSemplice;
import it.unina.dietiEstates25.model.GestoreAgenziaImmobiliare;

import java.sql.SQLException;

public interface AutenticazioneDao {
    boolean checkIfExists(AccountSemplice account) throws SQLException;
    GestoreAgenziaImmobiliareDTO checkGestoreExists(GestoreAgenziaImmobiliare g, String psw) throws SQLException;
}
