package it.unina.dietiEstates25.dto.response;
import com.fasterxml.jackson.annotation.JsonInclude;
import it.unina.dietiEstates25.model.ClasseEnergetica;
import it.unina.dietiEstates25.model.TipoVendita;
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GetAllRicercheResponse {
    private int idRicerca;

    private String utente;

    private String agente;

    private String gestore;

    private Integer numeroStanze;

    private ClasseEnergetica classeEnergetica;

    private Double prezzoMin;

    private Double prezzoMax;

    private String comune;

    private String citta;

    private TipoVendita tipoVendita;

    private String immagineRicerca;

    private GetAllRicercheResponse(Builder builder) {
        this.idRicerca = builder.idRicerca;
        this.utente = builder.cliente;
        this.tipoVendita=builder.tipovendita;
        this.prezzoMin =builder.prezzoMin;
        this.comune=builder.comune;
        this.citta=builder.citta;
        this.numeroStanze=builder.numeroStanze;
        this.classeEnergetica=builder.classeEnergetica;
        this.prezzoMax = builder.prezzoMax;
    }

    public static class Builder {
        private int idRicerca;
        private String cliente;
        private TipoVendita tipovendita;
        private String citta;
        private Integer numeroStanze;
        private ClasseEnergetica classeEnergetica;
        private Double prezzoMin;
        private String comune;
        private Double prezzoMax;

        public Builder idRicerca(int idRicerca) {
            this.idRicerca = idRicerca;
            return this;
        }

        public Builder cliente(String cliente) {
            this.cliente = cliente;
            return this;
        }

        public Builder tipovendita(TipoVendita tipovendita) {
            this.tipovendita = tipovendita;
            return this;
        }

        public Builder citta(String citta) {
            this.citta = citta;
            return this;
        }
        public Builder numeroStanze(Integer numeroStanze) {
            this.numeroStanze = numeroStanze;
            return this;
        }
        public Builder classeEnergetica(ClasseEnergetica classeEnergetica) {
            this.classeEnergetica = classeEnergetica;
            return this;
        }
        public Builder prezzo(Double prezzo) {
            this.prezzoMin = prezzo;
            return this;
        }
        public Builder comune(String comune) {
            this.comune = comune;
            return this;
        }
        public Builder prezzoMassimo(Double prezzoMassimo) {
            this.prezzoMax=prezzoMassimo;
            return this;
        }

        public GetAllRicercheResponse build(){
            return new GetAllRicercheResponse(this);
        }
    }

    public ClasseEnergetica getClasseEnergetica() {
        return classeEnergetica;
    }

    public TipoVendita getTipoVendita() {
        return tipoVendita;
    }


    public String getComune() {
        return comune;
    }

    public Double getPrezzoMax() {
        return prezzoMax;
    }

    public void setPrezzoMax(Double prezzoMax) {
        this.prezzoMax = prezzoMax;
    }

    public String getAgente() {
        return agente;
    }

    public Integer getNumeroStanze() {
        return numeroStanze;
    }

    public String getCitta() {
        return citta;
    }

    public Double getPrezzoMin() {
        return prezzoMin;
    }

    public Integer getIdRicerca() {
        return idRicerca;
    }

    public String getUtente() {
        return utente;
    }

    public String getGestore() {
        return gestore;
    }

    public void setUtente(String utente) {
        this.utente = utente;
    }

    public void setImmagineRicerca(String immagineRicerca) {
        this.immagineRicerca = immagineRicerca;
    }

    public void setIdRicerca(int idRicerca) {
        this.idRicerca = idRicerca;
    }

    public String getImmagineRicerca() {
        return immagineRicerca;
    }

    public void setAgente(String agente) {
        this.agente = agente;
    }

    public void setGestore(String gestore) {
        this.gestore = gestore;
    }

    public void setComune(String comune) {
        this.comune = comune;
    }

    public void setNumeroStanze(Integer numeroStanze) {
        this.numeroStanze = numeroStanze;
    }

    public void setCitta(String citta) {
        this.citta = citta;
    }

    public void setClasseEnergetica(ClasseEnergetica classeEnergetica) {
        this.classeEnergetica = classeEnergetica;
    }

    public void setPrezzoMin(Double prezzoMin) {
        this.prezzoMin = prezzoMin;
    }

    public void setTipoVendita(TipoVendita tipoVendita) {
        this.tipoVendita = tipoVendita;
    }

    public void setIdRicerca(Integer idRicerca) {
        this.idRicerca = idRicerca;
    }
}
