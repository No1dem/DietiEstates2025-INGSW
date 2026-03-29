package it.unina.dietiEstates25.dao;

import it.unina.dietiEstates25.dto.request.AddImmobileRequestDTO;
import it.unina.dietiEstates25.dto.request.RicercaImmobileDTORequest;
import it.unina.dietiEstates25.dto.response.DatiImmobileDTO;
import it.unina.dietiEstates25.dto.response.ImmobileDTO;
import it.unina.dietiEstates25.dto.response.ImmobileResponseRicercaDTO;
import it.unina.dietiEstates25.model.Immobile;
import java.util.List;

public interface ImmobileDao {
    ImmobileDTO addImmobile(AddImmobileRequestDTO immobile);

    boolean deleteImmobile(int idImmobile);

    List<Immobile> getAnnunciByEmail(String email);

    List<ImmobileResponseRicercaDTO> getImmobiliRicerca(RicercaImmobileDTORequest ricerca);

    DatiImmobileDTO getInfoAboutImmobile(int idImmobile);

    List<ImmobileResponseRicercaDTO> getImmobiliForAgente(String mail);

    ImmobileResponseRicercaDTO getInfoAboutImmobileById(int idImmobile);

}
