package com.example.prova2.facade;

import com.example.prova2.controller.dashBoard.DashBoardClienteController;
import com.example.prova2.controller.dashBoard.DashboardAgenteController;
import com.example.prova2.controller.dashBoard.DashboardAmministratoreController;
import com.example.prova2.dto.GetNotificheDTO;
import com.example.prova2.dto.InformazioniVisitaDTO;
import com.example.prova2.model.Agente;
import com.example.prova2.model.Cliente;
import com.example.prova2.model.GestoreAgenziaImmobiliare;
import com.example.prova2.model.Utente;
import com.example.prova2.service.AmazonS3Service;
import com.example.prova2.service.UpdatePasswordService;

import javafx.concurrent.Task;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public final class UtenteFacade {


    private UtenteFacade() {
        throw new UnsupportedOperationException("Questa è una classe di utilità e non può essere istanziata");
    }

    // =========================================================================
    // GESTIONE ASINCRONA IMMAGINI
    // =========================================================================

    public static void caricaImmagineProfiloAsync(GetNotificheDTO notifica, ImageView imageView, String token) {
        Task<String> task = new Task<>() {
            @Override
            protected String call() {
                InformazioniVisitaDTO visita = VisitaServiceFacade.getInfoAboutVisita(notifica.getIdNotifica(), token);
                return AmazonS3Service.getImageFromS3(visita.getFotoProfiloAgente());
            }
        };

        task.setOnSucceeded(event -> imageView.setImage(new Image(task.getValue())));
        task.setOnFailed(event -> System.err.println("Errore nel caricamento dell'immagine: " + task.getException()));

        Thread backgroundThread = new Thread(task);
        
        backgroundThread.setDaemon(true); 
        backgroundThread.start();
    }

    // =========================================================================
    // GESTIONE CREDENZIALI
    // =========================================================================

    public static boolean updatePassword(String nuovaPass, Utente utente) {
          
        return switch (utente) {
            case Cliente c -> 
                UpdatePasswordService.updatePasswordCliente(nuovaPass, DashBoardClienteController.getCliente());
                
            case Agente a -> 
                UpdatePasswordService.updatePasswordAgente(nuovaPass, DashboardAgenteController.getAgente());
                
            case GestoreAgenziaImmobiliare g -> 
                UpdatePasswordService.updatePasswordGestore(nuovaPass, DashboardAmministratoreController.getAdmin());
                
            default -> false;
        };
    }
}