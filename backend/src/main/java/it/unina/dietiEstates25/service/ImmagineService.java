package it.unina.dietiEstates25.service;
import it.unina.dietiEstates25.dao.ImmagineDao;
import it.unina.dietiEstates25.dao.ImmagineDaoImpl;
import it.unina.dietiEstates25.dto.response.FotoDTO;
import java.sql.SQLException;
import java.util.*;

public class ImmagineService {

    private ImmagineService(){}

    public static FotoDTO addImageAlDB(String path, String cf) {
        try {
            ImmagineDao dao = new ImmagineDaoImpl();
            return dao.addImage(path,cf);
        } catch (SQLException e) {
            return errorDto();
        }
    }

    public static List<String> getPublicIdByCF(String cf) {
        try {
            ImmagineDao dao = new ImmagineDaoImpl();
            return dao.getPublicIdByCf(cf);
        } catch (SQLException e) {
            return Collections.emptyList();
        }
    }

    public static List<String> getUriOfImmobile(int idImmobile) {
        try {
            ImmagineDao dao = new ImmagineDaoImpl();
            return dao.getImageOfImmobile(idImmobile);
        } catch (SQLException e) {
            return Collections.emptyList();
        }
    }


    private static FotoDTO errorDto() {
        return new FotoDTO(false,true);
    }






}
