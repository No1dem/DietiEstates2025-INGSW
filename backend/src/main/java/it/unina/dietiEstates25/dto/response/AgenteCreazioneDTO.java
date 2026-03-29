package it.unina.dietiEstates25.dto.response;

import lombok.Getter;
import lombok.Setter;

public class AgenteCreazioneDTO {
    private boolean isEmailRegistrata;

    private boolean isCfRegistrato;

    @Setter
    @Getter
    private  boolean errore;
    public AgenteCreazioneDTO(boolean email,boolean cf){
        this.isEmailRegistrata = email;this.isCfRegistrato =cf;
    }

    public AgenteCreazioneDTO(boolean errore){
        this.errore = errore;
    }

    public AgenteCreazioneDTO(){}

    public boolean isCfRegistrato() {
        return isCfRegistrato;
    }

    public boolean isEmailRegistrata() {
        return isEmailRegistrata;
    }

    public void setCfRegistrato(boolean cfRegistrato) {
        isCfRegistrato = cfRegistrato;
    }

    public void setEmailRegistrata(boolean emailRegistrata) {
        isEmailRegistrata = emailRegistrata;
    }

}
