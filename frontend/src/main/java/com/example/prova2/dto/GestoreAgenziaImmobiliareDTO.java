package com.example.prova2.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GestoreAgenziaImmobiliareDTO {

    // =========================================================================
    // CAMPI 
    // =========================================================================

    @JsonProperty("additionalInfo")
    private boolean additionalInfo;

    @JsonProperty("credSbagliate")
    private boolean credSbagliate;

    @JsonProperty("token")
    private String token;

    @JsonProperty("cf")
    private String cf;

    @JsonProperty("agenzia")
    private String agenzia;

    // =========================================================================
    // COSTRUTTORE
    // =========================================================================

    public GestoreAgenziaImmobiliareDTO() {}

    // =========================================================================
    // GETTER
    // =========================================================================

    public boolean isAdditionalInfo() { return additionalInfo; }
    public boolean isCredSbagliate() { return credSbagliate; }
    public String getToken() { return token; }
    public String getCf() { return cf; }
    public String getAgenzia() { return agenzia; }

    // =========================================================================
    // SETTER
    // =========================================================================

    public void setAdditionalInfo(boolean additionalInfo) { this.additionalInfo = additionalInfo; }
    public void setCredSbagliate(boolean credSbagliate) { this.credSbagliate = credSbagliate; }
    public void setToken(String token) { this.token = token; }
    public void setCf(String cf) { this.cf = cf; }
    public void setAgenzia(String agenzia) { this.agenzia = agenzia; }

}