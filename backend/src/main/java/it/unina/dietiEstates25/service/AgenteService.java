package it.unina.dietiEstates25.service;

import it.unina.dietiEstates25.dao.*;
import it.unina.dietiEstates25.dto.request.AddAgenteRequestDTO;
import it.unina.dietiEstates25.dto.request.UpdateDatiRequestDTO;
import it.unina.dietiEstates25.dto.response.AgenteCreazioneDTO;
import it.unina.dietiEstates25.dto.response.DatiDTO;
import it.unina.dietiEstates25.dto.response.GetAllRicercheResponse;
import it.unina.dietiEstates25.dto.response.UpdateDatiResponseDTO;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AgenteService {
    public AgenteCreazioneDTO addAgenteService(AddAgenteRequestDTO agente){
        try {
            AgenteDao dao = new AgenteDaoImpl();
            agente.setPassword(SecurityPasswordService.hashPassword(agente.getPassword()));
            return dao.addAgente(agente);
        }
        catch (SQLException e) {
            return new AgenteCreazioneDTO(true);
        }
    }

    public static DatiDTO getAgente(String email) {
        try {
            AgenteDao dao = new AgenteDaoImpl();
            return dao.getAgenteByEmail(email);
        }catch (SQLException e) {
            return null;
        }
    }

    public static UpdateDatiResponseDTO updateService(UpdateDatiRequestDTO datiAgente, String email){
        try {
            UserDao dao = new UserDaoImpl();
            return dao.updateDatiAgente(datiAgente,email);
        } catch (SQLException e) {
            return new UpdateDatiResponseDTO(false,true);
        }
    }


    public static boolean updateBiografia(String newBio,String cf){
        try {
            AgenteDao dao = new AgenteDaoImpl();
            return dao.updateBiografia(newBio, cf);
        } catch (SQLException e) {
            return false;
        }
    }

    public static Integer getValutazione(String cf){
        try {
            AgenteDao dao = new AgenteDaoImpl();
            return dao.getValutazione(cf);
        }catch (SQLException e) {
            return null;
        }
    }

    public static Integer getValutazioneByEmail(String email){
        try {
            AgenteDao dao = new AgenteDaoImpl();
            return dao.getValutazionebyEmail(email);
        }catch (SQLException e) {
            return null;
        }
    }

    public static List<GetAllRicercheResponse> getRicerche(String cf){
        try {
            AgenteDao dao = new AgenteDaoImpl();
            return dao.getAllRicerche(cf);
        } catch (SQLException e) {
            return Collections.emptyList();
        }
    }

    public static List<DatiDTO> getAccountAgenti(String cf){
        try{
            GestoreDao dao = new GestoreDaoImpl();
            return dao.getAllAccountAgenti(cf);
        }catch (SQLException e){
            return new ArrayList<>();
        }
    }



}
