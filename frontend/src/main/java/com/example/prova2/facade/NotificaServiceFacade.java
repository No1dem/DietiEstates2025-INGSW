package com.example.prova2.facade;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.example.prova2.dto.GetNotificheDTO;
import com.example.prova2.factory.AlertFactory;
import com.example.prova2.model.Agente;
import com.example.prova2.model.CategoriaNotifica;
import com.example.prova2.model.Cliente;
import com.example.prova2.service.NotificaService;

public final class NotificaServiceFacade {

   
    private NotificaServiceFacade() {
        throw new UnsupportedOperationException("Questa è una classe di utilità e non può essere istanziata");
    }

    // =========================================================================
    // RECUPERO NOTIFICHE
    // =========================================================================

    public static List<GetNotificheDTO> getNotificaCliente(Cliente cliente) {
        try {
            return NotificaService.getNotificationsForCliente(cliente);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return new ArrayList<>();
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    // =========================================================================
    // RECUPERO CATEGORIE (Disponibili e Disattivate)
    // =========================================================================

    public static List<CategoriaNotifica> getCategorieDispCliente(String email, String token) {
        return NotificaService.getCategorieDisponibiliCliente(email, token);
    }

    public static List<CategoriaNotifica> getCategorieDispAgente(String cf, String token) {
        return NotificaService.getCategorieDisponibiliAgente(cf, token);
    }

    public static List<CategoriaNotifica> getCategorieDisattivate(Agente agente) {
        List<CategoriaNotifica> categorie = NotificaService.getCategorieDisattivate(agente);
        if (!categorie.isEmpty()) {
            ordinaInModoDecrescenteAlfabetico(categorie);
            return categorie;
        }
        return Collections.emptyList();
    }

    public static List<CategoriaNotifica> getCategorieDisattivateCliente(Cliente cliente) {
        List<CategoriaNotifica> categorie = NotificaService.getCategorieDisattivateCliente(cliente);
        if (!categorie.isEmpty()) {
            ordinaInModoDecrescenteAlfabetico(categorie);
            return categorie;
        }
        return Collections.emptyList();
    }

    // =========================================================================
    // GESTIONE PREFERENZE NOTIFICHE - CLIENTE
    // =========================================================================

    public static boolean attivaCategoriaPerCliente(String categoriaNotifica, String email, String token) {
        String categoriaFormattata = formattaCategoria(categoriaNotifica);
        return NotificaService.attivaCategoriaNotificaPerCliente(categoriaFormattata, email, token);
    }

    public static boolean disattivaCategoriaPerCliente(String categoriaNotifica, String email, String token) {
        String categoriaFormattata = formattaCategoria(categoriaNotifica);
        return NotificaService.disattivaCategoriaNotificaPerCliente(categoriaFormattata, email, token);
    }

    // =========================================================================
    // GESTIONE PREFERENZE NOTIFICHE - AGENTE
    // =========================================================================

    public static boolean attivaCategoriaPerAgente(String categoriaNotifica, String cf, String token) {
        String categoriaFormattata = formattaCategoria(categoriaNotifica);
        boolean res = NotificaService.attivaCategoriaNotificaPerAgente(categoriaFormattata, cf, token);
        
        if (!res) {
            AlertFactory.creaAlertErroreInterno();
            return false;
        }
        return true;
    }

    public static boolean disattivaCategoriaPerAgente(String categoriaNotifica, String cf, String token) {
        String categoriaFormattata = formattaCategoria(categoriaNotifica);
        boolean res = NotificaService.disattivaCategoriaNotificaPerAgente(categoriaFormattata, cf, token);
        
        if (!res) {
            AlertFactory.creaAlertErroreInterno();
            return false;
        }
        return true;
    }

    // =========================================================================
    // METODI PRIVATI DI SUPPORTO (Helpers)
    // =========================================================================

    private static String formattaCategoria(String categoriaNotifica) {
        return categoriaNotifica.replace(" ", "").toUpperCase();
    }

    private static void ordinaInModoDecrescenteAlfabetico(List<CategoriaNotifica> categorie) {
        categorie.sort(Comparator.comparing(CategoriaNotifica::getLabel).reversed());
    }
}