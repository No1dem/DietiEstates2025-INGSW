package it.unina.dietiEstates25.dao;
import it.unina.dietiEstates25.dto.request.AddGestoreRequestDTO;
import it.unina.dietiEstates25.dto.response.*;

import java.sql.SQLException;
import java.util.List;

public interface GestoreDao {
    GestoreDTO addGestore(AddGestoreRequestDTO g) throws SQLException;

    String getSecurityPassword(String cf) throws SQLException;

    GestoreAgenziaImmobiliareDatiDTO getGestoreByEmail(String username) throws SQLException;

    boolean deleteGestoreByEmail(String email);

    boolean deleteAgenteByEmail(String email);

    List<GetAllRicercheResponse> getAllRicerche(String cf);

    List<DatiDTO> getAllAccountAgenti(String cf);

    List<String> getAllCF() throws SQLException;

    List<String> getAllEmail() throws SQLException;

    List<DatiDTO> getAllAccountGestori(String cf);

    

}
