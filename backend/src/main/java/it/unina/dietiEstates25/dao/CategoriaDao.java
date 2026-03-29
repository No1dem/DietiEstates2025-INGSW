package it.unina.dietiEstates25.dao;

import it.unina.dietiEstates25.dto.request.UpdateCategoriaNotificaDTO;
import it.unina.dietiEstates25.dto.response.UpdateCategoriaResponse;
import it.unina.dietiEstates25.model.CategoriaNotifica;

import java.util.List;

public interface CategoriaDao {
    UpdateCategoriaResponse setInviaCategoriaCliente(String email, UpdateCategoriaNotificaDTO categoriaDaAttivare);

    UpdateCategoriaResponse setNonInviareCategoriaCliente(String cf, UpdateCategoriaNotificaDTO categoriaDaDisattivare);

    UpdateCategoriaResponse setNonInviareCategoriaAgente(String cf, UpdateCategoriaNotificaDTO categoriaDaDisattivare);

    UpdateCategoriaResponse setInviareCategoriaAgente(String cf, UpdateCategoriaNotificaDTO categoriaDaattivare);

    List<CategoriaNotifica> getCategorieDisattivateByAgenteCF(String cf);

    List<CategoriaNotifica> getCategorieDisattivateByClienteEmail(String email);

    List<CategoriaNotifica> getAllCategorieByEmail(String email);

    List<CategoriaNotifica> getAllCategorieByAgenteCF(String cf);

}
