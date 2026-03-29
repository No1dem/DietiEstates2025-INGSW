package com.example.prova2.view;

import java.time.LocalDate;
import java.time.LocalTime;

import com.calendarfx.model.Calendar;
import com.calendarfx.model.CalendarSource;
import com.calendarfx.view.CalendarView;
import com.example.prova2.controller.CalendarioAgenteController;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class CalendarioAgente {

    public static void initCalendario(Stage stagePrincipale) {

        // 1. Inizializzazione della vista base
        CalendarView vistaCalendario = new CalendarView();
        vistaCalendario.setRequestedTime(LocalTime.now());

        // 2. Creazione e stile del calendario degli appuntamenti
        Calendar calendarioAppuntamenti = new Calendar("Appuntamenti");
        calendarioAppuntamenti.setStyle(Calendar.Style.STYLE1);

        // 3. Creazione della sorgente dati e collegamento alla vista
        CalendarSource sorgenteCalendari = new CalendarSource("I Miei Calendari");
        sorgenteCalendari.getCalendars().addAll(calendarioAppuntamenti);
        vistaCalendario.getCalendarSources().addAll(sorgenteCalendari);

        // 4. Avvio del thread in background per aggiornare la linea del tempo
        avviaThreadAggiornamentoOrario(vistaCalendario);

        // 5. Configurazione della nuova finestra (Stage)
        Stage stageCalendario = new Stage();
        Scene scenaCalendario = new Scene(vistaCalendario, 1540, 800);
        
        stageCalendario.setScene(scenaCalendario);
        stageCalendario.setTitle("I miei appuntamenti");

        // 6. Gestione degli eventi di apertura/chiusura
        stageCalendario.setOnCloseRequest(event -> {
            stagePrincipale.show();
        });

        stageCalendario.show();
        stagePrincipale.hide();

        // 7. Passaggio dei dati al controller
        CalendarioAgenteController.initEventiAgente(calendarioAppuntamenti);
    }

    /**
     * Metodo di supporto per isolare la logica del thread in background
     */
    private static void avviaThreadAggiornamentoOrario(CalendarView vistaCalendario) {
        Thread threadAggiornamentoOrario = new Thread(() -> {
            while (true) {
                Platform.runLater(() -> {
                    vistaCalendario.setToday(LocalDate.now());
                    vistaCalendario.setTime(LocalTime.now());
                });
                try {
                    // Aggiorna ogni 10 secondi (10000 millisecondi)
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    Thread.currentThread().interrupt(); // Buona pratica in caso di stop forzato
                    break;
                }
            }
        }, "Calendar: Update Time Thread");

        threadAggiornamentoOrario.setPriority(Thread.MIN_PRIORITY);
        threadAggiornamentoOrario.setDaemon(true);
        threadAggiornamentoOrario.start();
    }
}