package com.example.prova2.model;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Notifica {

    private int idNotifica;

    private String nomeNotifica;

    private String descrizioneNotifica;

    private Agente agenteNotificato;

    private GestoreAgenziaImmobiliare gestoreNotificato;

    private Utente utenteNotificato;

    private Utente utenteCheHaTriggeratoNotifica;

    private CategoriaNotifica categoria;

    private int idImmobile;

    public int getIdImmobile() {
        return idImmobile;
    }

    public void setIdImmobile(int idImmobile) {
        this.idImmobile = idImmobile;
    }

    public Notifica(int idNotifica){
        this.idNotifica = idNotifica;
    }

    public String getCategoria() {
        return categoria.toString();
    }

    public void setCategoria(CategoriaNotifica categoria) {
        this.categoria = categoria;
    }

    public Notifica(String titolo, String contenuto, int idNotifica) {
        this.nomeNotifica=titolo;
        this.descrizioneNotifica=contenuto;
        this.idNotifica=idNotifica;
    }

    public Notifica(String titolo,String contenuto, int idNotifica, CategoriaNotifica categoria) {
        this.nomeNotifica=titolo;
        this.descrizioneNotifica=contenuto;
        this.idNotifica=idNotifica;
        this.categoria=categoria;
    }

    public Notifica(){}


    public int getIdNotifica() {
        return idNotifica;
    }

    public void setIdNotifica(int idNotifica) {
        this.idNotifica = idNotifica;
    }

    public void setUtenteCheHaTriggeratoNotifica(Utente utenteCheHaTriggeratoNotifica) {
        this.utenteCheHaTriggeratoNotifica = utenteCheHaTriggeratoNotifica;
    }

    public Agente getAgenteNotificato() {
        return agenteNotificato;
    }

    public String getDescrizioneNotifica() {
        return descrizioneNotifica;
    }

    public String getNomeNotifica() {
        return nomeNotifica;
    }

    public void setNomeNotifica(String nomeNotifica) {
        this.nomeNotifica = nomeNotifica;
    }

    public void setDescrizioneNotifica(String descrizioneNotifica) {
        this.descrizioneNotifica = descrizioneNotifica;
    }

    public Utente getUtenteCheHaTriggeratoNotifica() {
        return utenteCheHaTriggeratoNotifica;
    }

    public void setAgenteNotificato(Agente agenteNotificato) {
        this.agenteNotificato = agenteNotificato;
    }
}
