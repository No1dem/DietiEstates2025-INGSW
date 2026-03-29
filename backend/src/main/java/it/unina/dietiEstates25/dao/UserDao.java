package it.unina.dietiEstates25.dao;
import it.unina.dietiEstates25.dto.request.UpdateDatiRequestDTO;
import it.unina.dietiEstates25.dto.response.AgenteDTO;
import it.unina.dietiEstates25.dto.response.LoginUtenteResponse;
import it.unina.dietiEstates25.dto.response.UpdateDatiResponseDTO;

import java.sql.SQLException;

public interface UserDao {
    UpdateDatiResponseDTO updateDatiGestore(UpdateDatiRequestDTO datiNuovi, String emailAttuale);

    UpdateDatiResponseDTO updateDatiAgente(UpdateDatiRequestDTO datiAgenteNuovi, String cf);

    UpdateDatiResponseDTO updateDatiCliente(UpdateDatiRequestDTO datiNuovi,String emailAttuale);

    AgenteDTO add(String email, String password) throws SQLException;

    LoginUtenteResponse login(String email, String password) throws SQLException;

    String getPassword(String email) throws SQLException; //* */
}
