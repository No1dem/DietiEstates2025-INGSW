package com.example.prova2.factory;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;

public final class AlertFactory {

    // Costruttore privato blindato
    private AlertFactory() {
        throw new UnsupportedOperationException("Questa è una classe di utilità e non può essere istanziata");
    }

    // =========================================================================
    // METODO HELPER (Per eliminare la duplicazione del codice)
    // =========================================================================
    
    private static Alert buildAlert(Alert.AlertType type, String title, String header, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        return alert;
    }

    // =========================================================================
    // ALLERT GENERICI / PERSONALIZZATI
    // =========================================================================

    public static Alert creaAlertSuccess(String scrittaBottone1, String scrittaBottone2, String title, String head, String contenuto) {
        Alert alert = buildAlert(Alert.AlertType.INFORMATION, title, head, contenuto);
        ButtonType customYesButton = new ButtonType(scrittaBottone1, ButtonBar.ButtonData.NO);
        ButtonType customNoButton = new ButtonType(scrittaBottone2, ButtonBar.ButtonData.NO);
        alert.getButtonTypes().setAll(customYesButton, customNoButton);
        alert.getDialogPane().setMinWidth(500);
        alert.getDialogPane().setMinHeight(170);
        return alert;
    }

    public static Alert creaAlertSuccessPasswordCambiata(String scrittaBottone1, String title, String head, String contenuto) {
        Alert alert = buildAlert(Alert.AlertType.INFORMATION, title, head, contenuto);
        ButtonType customYesButton = new ButtonType(scrittaBottone1, ButtonBar.ButtonData.NO);
        alert.getButtonTypes().setAll(customYesButton);
        alert.getDialogPane().setMinWidth(500);
        alert.getDialogPane().setMinHeight(170);
        return alert;
    }

    public static Alert creaAlertErrore(String titolo, String messaggio) {
        Alert errorAlert = buildAlert(Alert.AlertType.ERROR, titolo, null, messaggio);
        errorAlert.showAndWait();
        return errorAlert;
    }

    // =========================================================================
    // SUCCESSO E INFORMAZIONI
    // =========================================================================

    public static Alert creaAlertSuccessValutazione() {
        Alert alert = buildAlert(Alert.AlertType.INFORMATION, 
                "Operazione completata!", 
                "Valutazione inviata con successo", 
                "Il tuo feedback è stato salvato correttamente sul profilo dell'agente. Grazie per aver condiviso la tua opinione!");
        alert.getButtonTypes().setAll(ButtonType.OK);
        return alert;
    }

    public static Alert creaAlertSuccessInvioPrenotazione() {
        Alert alert = buildAlert(Alert.AlertType.INFORMATION, 
                "Richiesta inviata", 
                "Prenotazione inoltrata correttamente", 
                """
                La tua richiesta per visitare l'immobile è stata inviata all'agente di riferimento.
                Ti invitiamo a tenere d'occhio la sezione notifiche per scoprire quando la tua visita verrà confermata o riprogrammata.
                """);
        alert.getButtonTypes().setAll(ButtonType.OK);
        return alert;
    }

    public static void creaAlertSuccessModificaBio() {
        Alert alert = buildAlert(Alert.AlertType.INFORMATION, 
                "Profilo aggiornato", 
                "Biografia modificata con successo", 
                """
                Le modifiche apportate alla tua biografia sono state salvate.
                Potrebbero essere necessari alcuni istanti prima che il nuovo testo sia visibile pubblicamente.
                """);
        alert.getButtonTypes().setAll(ButtonType.OK);
        alert.showAndWait();
    }

    public static Alert creaAlertVisitaAccettata() {
        Alert alert = buildAlert(Alert.AlertType.INFORMATION, 
                "Visita confermata", 
                "Appuntamento fissato con successo", 
                """
                L'appuntamento è stato registrato a sistema.
                Puoi monitorare tutti i tuoi impegni accedendo al calendario nella sezione "Gestione Appuntamenti".
                """);
        alert.getButtonTypes().setAll(ButtonType.OK);
        return alert;
    }

    public static Alert creaAlertVisitaRifiutata() {
        Alert alert = buildAlert(Alert.AlertType.INFORMATION, 
                "Visita declinata", 
                "Operazione completata", 
                """
                Hai rifiutato la richiesta di visita. Il cliente è stato notificato.
                """);
        alert.getButtonTypes().setAll(ButtonType.OK);
        return alert;
    }

    // =========================================================================
    // CONFERME E AVVISI (WARNING / CONFIRMATION)
    // =========================================================================

    public static Alert creaAlertAggiornamentoCategoria() {
        Alert alert = buildAlert(Alert.AlertType.CONFIRMATION, 
                "Aggiornamento completato", 
                "Preferenze salvate con successo", 
                """
                Le tue nuove categorie di interesse sono state memorizzate.
                Ricorda che puoi tornare in questa sezione e modificarle in qualsiasi momento.
                """);
        alert.getButtonTypes().setAll(ButtonType.OK);
        return alert;
    }

    public static Alert creaAlertAccettaNotifica() {
        Alert alert = buildAlert(Alert.AlertType.INFORMATION, 
                "Accettazione appuntamento", 
                "Confermi l'appuntamento?", 
                """
                Procedendo, la visita verrà ufficialmente fissata.
                Il cliente riceverà una notifica di conferma con i dettagli dell'incontro.
                """);
        alert.getButtonTypes().setAll(ButtonType.OK, ButtonType.CANCEL);
        return alert;
    }

    public static Alert creaAlertRifiutaNotifica() {
        Alert alert = buildAlert(Alert.AlertType.INFORMATION, 
                "Rifiuto appuntamento", 
                "Vuoi davvero declinare la visita?", 
                """
                Rifiutando l'incontro, il cliente riceverà una notifica e gli verrà chiesto di proporre un nuovo orario per l'appuntamento.
                """);
        alert.getButtonTypes().setAll(ButtonType.OK, ButtonType.CANCEL);
        return alert;
    }

    public static Alert creaAlertLogout() {
        Alert alert = buildAlert(Alert.AlertType.INFORMATION, 
                "Uscita dall'account", 
                "Conferma disconnessione", 
                """
                Stai per uscire dalla tua area riservata.
                Per accedere nuovamente dovrai reinserire le tue credenziali.
                Vuoi procedere?
                """);
        alert.getButtonTypes().setAll(ButtonType.OK, ButtonType.CANCEL);
        return alert;
    }

    public static Alert creaAlertConfermaEliminazioneImmobile() {
        Alert alert = buildAlert(Alert.AlertType.INFORMATION, 
                "Eliminazione annuncio", 
                "Vuoi davvero rimuovere questo immobile?", 
                """
                L'immobile verrà cancellato in modo permanente e non apparirà più nei risultati di ricerca.
                Tutte le richieste di visita collegate verranno annullate.
                """);
        alert.getButtonTypes().setAll(ButtonType.OK, ButtonType.CANCEL);
        return alert;
    }

    public static Alert creaAlertConfermaCaricamentoImmobile() {
        Alert alert = buildAlert(Alert.AlertType.CONFIRMATION, 
                "Pubblicazione annuncio", 
                "Sei pronto a pubblicare l'immobile?", 
                """
                Confermando, l'immobile sarà online e visibile a tutti gli utenti della piattaforma.
                I clienti registrati su DietiEstates25 potranno iniziare a richiedere appuntamenti.
                Vuoi procedere con la pubblicazione?
                """);
        alert.getButtonTypes().setAll(ButtonType.OK, ButtonType.CANCEL);
        return alert;
    }

    public static Alert creaAlertUscitaCreazioneAnnuncio() {
        Alert alert = buildAlert(Alert.AlertType.WARNING, 
                "Dati non salvati", 
                "Vuoi abbandonare la creazione dell'annuncio?", 
                """
                Se esci ora, tutte le informazioni e le foto che hai caricato andranno perse.
                Sei sicuro di voler interrompere l'operazione?
                """);
        alert.getButtonTypes().setAll(ButtonType.OK, ButtonType.CANCEL);
        return alert;
    }

    // =========================================================================
    // ERRORI
    // =========================================================================

    public static void creaAlertErroreCredenziali() {
        Alert alert = buildAlert(Alert.AlertType.ERROR, 
                "Accesso negato", 
                "Credenziali errate", 
                """
                L'indirizzo email o la password inseriti non sono corretti.
                Verifica i dati e riprova l'accesso.
                
                Se hai dimenticato la password o continui a riscontrare problemi, contatta il supporto tecnico.
                """);
        alert.showAndWait();
    }

    public static void creaAlertErroreEmailRegistrata() {
        Alert alert = buildAlert(Alert.AlertType.ERROR, 
                "Errore di registrazione", 
                "Indirizzo email già in uso", 
                """
                L'email che stai cercando di utilizzare è già associata a un account esistente.
                Accedi con questa email o inseriscine una nuova per procedere.
                """);
        alert.showAndWait();
    }

    public static void creaAlertErroreCFRegistrato() {
        Alert alert = buildAlert(Alert.AlertType.ERROR, 
                "Errore di registrazione", 
                "Codice fiscale già registrato", 
                """
                Risulta già un account collegato a questo Codice Fiscale.
                Verifica la correttezza dei dati inseriti o contatta l'assistenza.
                """);
        alert.showAndWait();
    }

    public static Alert creaAlertErroreEliminazioneImmobile() {
        Alert alert = buildAlert(Alert.AlertType.INFORMATION, 
                "Operazione fallita", 
                "Impossibile eliminare l'immobile", 
                """
                Si è verificato un problema tecnico durante la cancellazione dell'annuncio.
                Ti preghiamo di riprovare più tardi.
                """);
        alert.getButtonTypes().setAll(ButtonType.OK, ButtonType.CANCEL);
        return alert;
    }

    public static void creaAlertErroreCaricamentoPagina() {
        Alert alert = buildAlert(Alert.AlertType.ERROR, 
                "Errore di caricamento", 
                "Impossibile caricare la schermata", 
                """
                Qualcosa è andato storto nel rendering della pagina.
                Assicurati di avere una connessione stabile e riprova.
                
                Se l'errore continua a presentarsi, contatta il nostro team di supporto.
                """);
        alert.showAndWait();
    }

    public static void creaAlertErroreConnessione() {
        Alert alert = buildAlert(Alert.AlertType.ERROR, 
                "Problema di rete", 
                "Server non raggiungibile", 
                """
                Impossibile stabilire una connessione con il sistema.
                
                - Controlla la tua connessione Wi-Fi o rete dati.
                - Verifica che l'applicazione non sia bloccata da un firewall.
                
                Riprova tra qualche istante.
                """);
        alert.showAndWait();
    }

    public static void creaAlertErroreInterno() {
        Alert alert = buildAlert(Alert.AlertType.ERROR, 
                "Errore di sistema", 
                "Si è verificato un errore inaspettato", 
                """
                L'operazione non è andata a buon fine a causa di un problema interno.
                
                Ti consigliamo di riavviare l'applicazione e tentare nuovamente.
                Se il guasto persiste, ti preghiamo di segnalarlo all'assistenza tecnica.
                """);
        alert.showAndWait();
    }
}