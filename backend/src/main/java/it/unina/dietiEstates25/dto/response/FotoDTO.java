package it.unina.dietiEstates25.dto.response;

public class FotoDTO {
    private boolean immagineAggiuntaConSuccesso;

    private boolean errore;



    public FotoDTO(boolean immagineAggiuntaConSuccesso, boolean errore) {
        this.immagineAggiuntaConSuccesso=immagineAggiuntaConSuccesso;
        this.errore=errore;
    }




    public void setErrore(boolean errore) {
        this.errore = errore;
    }

    public void setImmagineAggiuntaConSuccesso(boolean immagineAggiuntaConSuccesso) {
        this.immagineAggiuntaConSuccesso = immagineAggiuntaConSuccesso;
    }

    public boolean isImmagineAggiuntaConSuccesso() {
        return immagineAggiuntaConSuccesso;
    }



    public boolean isErrore() {
        return errore;
    }
}
