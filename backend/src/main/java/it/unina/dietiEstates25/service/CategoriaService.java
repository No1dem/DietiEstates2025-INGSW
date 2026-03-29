package it.unina.dietiEstates25.service;
import it.unina.dietiEstates25.dao.CategoriaDao;
import it.unina.dietiEstates25.dao.CategoriaDaoImpl;
import it.unina.dietiEstates25.dto.request.UpdateCategoriaNotificaDTO;
import it.unina.dietiEstates25.dto.response.UpdateCategoriaResponse;
import it.unina.dietiEstates25.model.CategoriaNotifica;

import java.util.List;

public class CategoriaService {

    private CategoriaService(){}

    public static UpdateCategoriaResponse setInviaCategoriaAdCliente(String email, UpdateCategoriaNotificaDTO categoriaNotifica){
        CategoriaDao notDao = new CategoriaDaoImpl();
        return notDao.setInviaCategoriaCliente(email, categoriaNotifica);
    }


    public static UpdateCategoriaResponse setNonInviareCategoriaAdCliente(String email, UpdateCategoriaNotificaDTO categoriaNotifica){
        CategoriaDao notDao = new CategoriaDaoImpl();
        return notDao.setNonInviareCategoriaCliente(email, categoriaNotifica);
    }

    public static UpdateCategoriaResponse setNonInviareCategoriaAdAgente(String cf, UpdateCategoriaNotificaDTO categoriaNotifica){
        CategoriaDao notDao = new CategoriaDaoImpl();
        return notDao.setNonInviareCategoriaAgente(cf, categoriaNotifica);
    }

    public static UpdateCategoriaResponse setInviareCategoriaAdAgente(String cf, UpdateCategoriaNotificaDTO categoriaNotifica){
        CategoriaDao notDao = new CategoriaDaoImpl();
        return notDao.setInviareCategoriaAgente(cf, categoriaNotifica);
    }

    public static List<CategoriaNotifica> getCategorieDisattivateByCfAgente(String cf){
        CategoriaDao dao = new CategoriaDaoImpl();
        return dao.getCategorieDisattivateByAgenteCF(cf);
    }

    public static List<CategoriaNotifica> getCategorieDisattivateByClienteEmail(String email){
        CategoriaDao dao = new CategoriaDaoImpl();
        return dao.getCategorieDisattivateByClienteEmail(email);
    }

    public static List<CategoriaNotifica> getAllCategorieByEmailCliente(String email){
        CategoriaDao dao = new CategoriaDaoImpl();
        return dao.getAllCategorieByEmail(email);
    }

    public static List<CategoriaNotifica> getAllCategorieByCFAgente(String cf){
        CategoriaDao dao = new CategoriaDaoImpl();
        return dao.getAllCategorieByAgenteCF(cf);
    }





}
