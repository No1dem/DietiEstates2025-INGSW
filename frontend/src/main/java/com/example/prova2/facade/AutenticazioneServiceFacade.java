package com.example.prova2.facade;
import java.io.IOException;

import com.example.prova2.dto.AgenteDTO;
import com.example.prova2.factory.AlertFactory;
import com.example.prova2.service.AutenticazioneService;
import com.example.prova2.service.GoogleService;

public class AutenticazioneServiceFacade {


    public static AgenteDTO login(String email,String password){
        try {
            return AutenticazioneService.requestLogin(email, password);
        }catch (IOException e){
            e.printStackTrace();
            return null;
        } catch (InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
            return null;
        }
    }


    public static AgenteDTO authWithGoogle(String code){
        AgenteDTO response=GoogleService.exchangeCodeForToken(code);
        if(response!=null){
            return response;
        } else {
            AlertFactory.creaAlertErroreInterno();
            return null;
        }
    }
}
