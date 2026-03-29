package com.example.prova2.facade;


import com.example.prova2.factory.AlertFactory;
import com.example.prova2.service.RecensioneService;

import java.io.IOException;

public class RecensioneFacade {

    private RecensioneFacade() {}

    public static boolean updateRecensione(String agenteDaRecensire,int valutazione,String token) throws IOException, InterruptedException {
        boolean response = RecensioneService.updateRecensione(agenteDaRecensire,valutazione,token);
        if(!response){
            AlertFactory.creaAlertErroreInterno();
        }

        return response;
    }

}
