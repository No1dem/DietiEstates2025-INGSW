package it.unina.dietiEstates25.dao;
import it.unina.dietiEstates25.dto.response.NotificaDTO;
import it.unina.dietiEstates25.model.Notifica;

import java.util.List;

public interface NotificaDao {


    NotificaDTO setNotificaAccepted(int idNotifica);

    NotificaDTO setNotificaRejected(int idNotifica);

    List<Notifica> getNotificaAgente(String cf);

    List<Notifica> getNotofAdmin(String partitaIva);

    List<Notifica> getNotificaCliente(String email);

    boolean annullaInvioNotifica(int id);
}
