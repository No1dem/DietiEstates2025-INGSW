package it.unina.dietiEstates25.service;
import it.unina.dietiEstates25.dao.NotificaDao;
import it.unina.dietiEstates25.dao.NotificaDaoImpl;
import it.unina.dietiEstates25.dto.response.NotificaDTO;
import it.unina.dietiEstates25.model.Notifica;

import java.util.List;

public class NotificaService {

    private NotificaService(){}

    public static List<Notifica> getNotificationOfAdminService(String cf) {
            NotificaDaoImpl dao = new NotificaDaoImpl();
            return dao.getNotofAdmin(cf);
    }

    public static NotificaDTO setNotificationAccepted(int idNotifica){
        NotificaDao notDao = new NotificaDaoImpl();
        return notDao.setNotificaAccepted(idNotifica);
    }

    public static NotificaDTO setNotificationRejected(int idNotifica){
        NotificaDao notDao = new NotificaDaoImpl();
        return notDao.setNotificaRejected(idNotifica);
    }

    public static List<Notifica> getNotificaOfAgenteService(String cf){
        NotificaDao notDao = new NotificaDaoImpl();
        return notDao.getNotificaAgente(cf);
    }

    public static List<Notifica> getNotificaOfClienteService(String email){
        NotificaDao notDao = new NotificaDaoImpl();
        return notDao.getNotificaCliente(email);
    }


    public static boolean annullaInvioNotifica(int id){
            NotificaDao dao = new NotificaDaoImpl();
            return dao.annullaInvioNotifica(id);
    }
}
