package it.unina.dietiEstates25.dto.response;
import com.fasterxml.jackson.annotation.JsonProperty;

public class GestoreAgenziaImmobiliareDTO {

    @JsonProperty("token")
    private String token;

    @JsonProperty("additionalInfo")
    private boolean isAdmin;

    @JsonProperty("credSbagliate")
    private boolean credSbagliate;

    @JsonProperty("cf")
    private String cf;

    @JsonProperty("agenzia")
    private String agenzia;


    public GestoreAgenziaImmobiliareDTO() {}


    public String getCf() {
        return cf;
    }

    public void setCf(String cf) {
        this.cf = cf;
    }

    public String getAgenzia() {
        return agenzia;
    }

    public void setAgenzia(String agenzia) {
        this.agenzia = agenzia;
    }

    // Costruttori
    public GestoreAgenziaImmobiliareDTO(String token, boolean additionalInfo,boolean credSbagliate,String cf,String agenzia) {
        this.token = token;
        this.isAdmin = additionalInfo;
        this.credSbagliate = credSbagliate;
        this.cf = cf;
        this.agenzia = agenzia;
    }

    // Getter e Setter
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean getAdditionalInfo() {
        return isAdmin;
    }

    public void setAdditionalInfo(boolean additionalInfo) {
        this.isAdmin = additionalInfo;
    }

    public void setCredSbagliate(boolean credSbagliate) {
        this.credSbagliate = credSbagliate;
    }

    public boolean isCredSbagliate() {
        return credSbagliate;
    }


}


