package it.unina.dietiEstates25.dao;

import it.unina.dietiEstates25.dto.response.FotoDTO;
import java.util.List;

public interface ImmagineDao {
    FotoDTO addImage(String assertId, String cf);

    List<String> getPublicIdByCf(String cf);

    List<String> getImageOfImmobile(int idImmobile);
}
