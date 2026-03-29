package it.unina.dietiEstates25.service;

import it.unina.dietiEstates25.dao.ClienteDao;
import it.unina.dietiEstates25.dao.ClienteDaoImpl;
import it.unina.dietiEstates25.dao.UserDao;
import it.unina.dietiEstates25.dao.UserDaoImpl;
import it.unina.dietiEstates25.dto.request.AddClienteRequestDTO;
import it.unina.dietiEstates25.dto.request.LasciaRecensioneRequestDTO;
import it.unina.dietiEstates25.dto.request.UpdateDatiRequestDTO;
import it.unina.dietiEstates25.dto.response.*;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

public class ClienteService {

    public ClienteService(){}

    public ClienteService(ClienteDao dao){
        clienteDao=dao;
    }

    private ClienteDao clienteDao;

    public static ClienteDTO addCliente(AddClienteRequestDTO cliente) {
        try {
            cliente.setPassword(SecurityPasswordService.hashPassword(cliente.getPassword()));
            ClienteDao dao = new ClienteDaoImpl();
            return dao.insert(cliente);
        } catch (SQLException e) {
            ClienteDTO clienteDTO = new ClienteDTO();
            clienteDTO.setErroreInterno(true);
            return clienteDTO;
        }
    }

    public static UpdateDatiResponseDTO updateDati(UpdateDatiRequestDTO nuoviDati,String emailAttuale) {
        try {
            UserDao dao = new UserDaoImpl();
            return dao.updateDatiCliente(nuoviDati,emailAttuale);
        } catch (SQLException e) {
            return null;
        }
    }

    public static ClienteDatiDTO getCliente(String email) {
        try {
            ClienteDao dao = new ClienteDaoImpl();
            return dao.getClienteByEmail(email);
        }catch (SQLException e){
            return null;
        }
    }

    public boolean lasciaRecensione(LasciaRecensioneRequestDTO recensione){
        try {
            ClienteDao dao = new ClienteDaoImpl();
            return dao.lasciaRecensione(recensione);
        }catch (SQLException e){
            return false;
        }
    }

    public static List<GetAllRicercheResponse> getAllRicerche(String email){
        try {
            ClienteDao dao = new ClienteDaoImpl();
            return dao.getAllRicerche(email);
        }catch (SQLException e){
            return Collections.emptyList();
        }
    }

    public static AgenteDTO addCliente(String email, String password){
        try {
            UserDao dao = new UserDaoImpl();
            return dao.add(email, password);
        }catch (SQLException e){
            return null;
        }
    }
}
