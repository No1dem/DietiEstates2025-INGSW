package it.unina.dietiEstates25.dao;
import it.unina.dietiEstates25.dto.request.AddClienteRequestDTO;
import it.unina.dietiEstates25.dto.request.LasciaRecensioneRequestDTO;
import it.unina.dietiEstates25.dto.request.UpdatePasswordRequestDTO;
import it.unina.dietiEstates25.dto.response.ClienteDatiDTO;
import it.unina.dietiEstates25.dto.response.ClienteDTO;
import it.unina.dietiEstates25.dto.response.GetAllRicercheResponse;

import java.util.List;

public interface ClienteDao {
    ClienteDTO insert(AddClienteRequestDTO cliente);

    boolean exists(AddClienteRequestDTO cliente);

    ClienteDatiDTO getClienteByEmail(String email);

    boolean lasciaRecensione(LasciaRecensioneRequestDTO request);

    List<GetAllRicercheResponse> getAllRicerche(String email);

    boolean updatePassword(UpdatePasswordRequestDTO request);
}
