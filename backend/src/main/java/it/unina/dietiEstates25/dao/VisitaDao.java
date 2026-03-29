package it.unina.dietiEstates25.dao;


import it.unina.dietiEstates25.dto.request.AddVisitaRequestDTO;
import it.unina.dietiEstates25.dto.response.*;

import java.util.List;

public interface VisitaDao {
    VisitaResponseDTO addVisita(AddVisitaRequestDTO visita);

    CheckVisitaPerClienteDTO controllaSeClienteHaGiaVisitaPerImmobile(String email, int idImmobile);

    InformazioniVisitaDTO getInformazioniVisita(int idNotifica);

    List<GetVisiteResponse> getAllVisiteAccettateByCF(String cf);

    List<Integer> getAllImmobileByCf(String cf);

    List<DateEOreOccupateAgente> getDateEOreOccupateAgente(String cf);

    boolean eliminaVisita(AddVisitaRequestDTO visita);

}
