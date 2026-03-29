package it.unina.dietiEstates25.dao;
import it.unina.dietiEstates25.dto.request.AddAgenteRequestDTO;
import it.unina.dietiEstates25.dto.request.UpdatePasswordRequestDTO;
import it.unina.dietiEstates25.dto.response.AgenteCreazioneDTO;
import it.unina.dietiEstates25.dto.response.DatiDTO;
import it.unina.dietiEstates25.dto.response.GetAllRicercheResponse;
import java.sql.SQLException;
import java.util.List;

public interface AgenteDao {

    AgenteCreazioneDTO addAgente(AddAgenteRequestDTO agente) throws SQLException;

    DatiDTO getAgenteByEmail(String cf);

    boolean updateBiografia(String newBio, String cf);

    Integer getValutazione(String cf);

    Integer getValutazionebyEmail(String email);

    List<GetAllRicercheResponse> getAllRicerche(String cf);

    boolean updatePassword(UpdatePasswordRequestDTO request);


}
