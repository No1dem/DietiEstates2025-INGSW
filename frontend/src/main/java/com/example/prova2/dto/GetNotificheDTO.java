package com.example.prova2.dto;

import com.example.prova2.model.CategoriaNotifica;

public class GetNotificheDTO {

    // =========================================================================
    // CAMPI (Proprietà)
    // =========================================================================
    private int idImmobile;
    private int idNotifica;
    private String nomeNotifica;
    private CategoriaNotifica categoria;
    private String descrizioneNotifica;

    // =========================================================================
    // COSTRUTTORI
    // =========================================================================
    public GetNotificheDTO() {}

    public GetNotificheDTO(int idNotifica) {
        this.idNotifica = idNotifica;
    }

    // =========================================================================
    // GETTER
    // =========================================================================
    public int getIdImmobile() { return idImmobile; }
    public int getIdNotifica() { return idNotifica; }
    public String getNomeNotifica() { return nomeNotifica; }
    public CategoriaNotifica getCategoria() { return categoria; }
    public String getDescrizioneNotifica() { return descrizioneNotifica; }

    // =========================================================================
    // SETTER
    // =========================================================================
    public void setIdImmobile(int idImmobile) { this.idImmobile = idImmobile; }
    public void setIdNotifica(int idNotifica) { this.idNotifica = idNotifica; }
    public void setNomeNotifica(String nomeNotifica) { this.nomeNotifica = nomeNotifica; }
    public void setCategoria(CategoriaNotifica categoria) { this.categoria = categoria; }
    public void setDescrizioneNotifica(String descrizioneNotifica) { this.descrizioneNotifica = descrizioneNotifica; }

}