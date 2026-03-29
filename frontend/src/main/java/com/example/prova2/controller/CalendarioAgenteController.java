package com.example.prova2.controller;

import java.time.LocalDateTime;
import java.util.List;

import com.calendarfx.model.Calendar;
import com.calendarfx.model.Entry;
import com.example.prova2.controller.dashBoard.DashboardAgenteController;
import com.example.prova2.dto.GetVisiteResponse;
import com.example.prova2.facade.VisitaServiceFacade;

public final class CalendarioAgenteController {

    
    private CalendarioAgenteController() {
        throw new UnsupportedOperationException("Questa è una classe di utilità e non può essere istanziata");
    }

    public static void initEventiAgente(Calendar calendar) {
        
      
        String cfAgente = DashboardAgenteController.getAgente().getCf();
        String token = DashboardAgenteController.getAgente().getToken();

        List<GetVisiteResponse> visite = VisitaServiceFacade.getVisiteOfAgente(cfAgente, token);

        
        if (!visite.isEmpty()) {
            
            for (GetVisiteResponse visita : visite) {
                Entry<String> visitaCalendario = new Entry<>(visita.getDescrizione());

                
                LocalDateTime start = LocalDateTime.of(visita.getDataVisita(), visita.getHoraInizioVisita());
                LocalDateTime end = LocalDateTime.of(visita.getDataVisita(), visita.getHoraFineVisita());

                visitaCalendario.setInterval(start, end);
                calendar.addEntry(visitaCalendario);
            }
            
        }
    }
}