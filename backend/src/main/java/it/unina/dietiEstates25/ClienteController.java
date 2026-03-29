package it.unina.dietiEstates25;

import it.unina.dietiEstates25.dto.request.AddClienteRequestDTO;
import it.unina.dietiEstates25.dto.request.LasciaRecensioneRequestDTO;
import it.unina.dietiEstates25.dto.request.UpdateDatiRequestDTO;
import it.unina.dietiEstates25.dto.request.UpdatePasswordRequestDTO;
import it.unina.dietiEstates25.dto.response.ClienteDTO;
import it.unina.dietiEstates25.dto.response.ClienteDatiDTO;
import it.unina.dietiEstates25.dto.response.GetAllRicercheResponse;
import it.unina.dietiEstates25.dto.response.UpdateDatiResponseDTO;
import it.unina.dietiEstates25.filter.RequireJWTAuthentication;
import it.unina.dietiEstates25.service.ClienteService;
import it.unina.dietiEstates25.service.UpdatePasswordService;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static it.unina.dietiEstates25.GestoreController.getResponse;
import static it.unina.dietiEstates25.GestoreController.validaInput;

@Path("cliente")
public class ClienteController {

    // =========================================================================
    // VARIABILI DI ISTANZA
    // =========================================================================
    private ClienteService clienteService = new ClienteService();

    // =========================================================================
    // COSTRUTTORI
    // =========================================================================
    public ClienteController() {}

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    // =========================================================================
    // ENDPOINT PUBBLICI - CREAZIONE E REGISTRAZIONE (Create)
    // =========================================================================
    @Path("add")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addCliente(AddClienteRequestDTO clientReqDto) {
        String validationJson = validaInput(clientReqDto);
        if (inputNonValido(validationJson)) {
            return Response.status(Response.Status.BAD_REQUEST).entity(validationJson).build();
        }
        
        ClienteDTO creationResult = ClienteService.addCliente(clientReqDto);
        
        if (isVerificatoErroreInterno(creationResult)) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
        if (isClienteEsiste(creationResult)) {
            return responseConflict();
        } else {
            String jwtToken = AutenticazioneController.creazioneJWT(clientReqDto.getEmail(), TimeUnit.DAYS.toMillis(365));
            return responseWithToken(jwtToken);
        }
    }

    // =========================================================================
    // ENDPOINT PUBBLICI - LETTURA DATI E RICERCHE (Read)
    // =========================================================================
    @Path("getUtente")
    @GET
    @RequireJWTAuthentication
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getUtenteByEmail(@QueryParam("email") String targetEmail) {
        if (validaEmail(targetEmail)) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Inserisci una email valida!").build();
        }
        
        ClienteDatiDTO clientData = ClienteService.getCliente(targetEmail);
        
        if (clientData == null) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
        return Response.status(Response.Status.OK).entity(clientData).build();
    }

    @Path("getRicerche")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @RequireJWTAuthentication
    public Response getAllRicerche(@QueryParam("email") String clientEmail) {
        if (validaEmail(clientEmail)) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        
        List<GetAllRicercheResponse> searchesList = ClienteService.getAllRicerche(clientEmail);
        
        if (searchesList == null) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
        if (searchesList.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.status(Response.Status.OK).entity(searchesList).build();
    }

    // =========================================================================
    // ENDPOINT PUBBLICI - AGGIORNAMENTO E AZIONI (Update)
    // =========================================================================
    @Path("update")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateCliente(UpdateDatiRequestDTO updateReqDto, @QueryParam("emailAttuale") String currentEmail) {
        if (validaEmail(currentEmail)) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Devi inserire l'email attuale!").build();
        }
        
        String validationStr = validaInput(updateReqDto);
        if (inputNonValido(validationStr)) {
            return Response.status(Response.Status.BAD_REQUEST).entity(validationStr).build();
        }
        
        UpdateDatiResponseDTO updateResult = ClienteService.updateDati(updateReqDto, currentEmail);
        return getResponse(updateResult);
    }

    @Path("lasciaRecensione")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @RequireJWTAuthentication
    public Response lasciaRecensione(LasciaRecensioneRequestDTO reviewReq) {
        String valJson = validaInput(reviewReq);
        if (inputNonValido(valJson)) {
            return Response.status(Response.Status.BAD_REQUEST).entity(valJson).build();
        }
        
        boolean isReviewSaved = clienteService.lasciaRecensione(reviewReq);
        
        if (!isReviewSaved) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
        return Response.status(Response.Status.OK).build();
    }

    @Path("updatePassword")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public boolean updatePassword(UpdatePasswordRequestDTO pwdReqDto) {
        return UpdatePasswordService.updatePasswordCliente(pwdReqDto);
    }

    // =========================================================================
    // METODI HELPER E DI VALIDAZIONE (Private / Package-Private)
    // =========================================================================
    private static boolean inputNonValido(String jsonErrorStr) {
        return jsonErrorStr != null;
    }

    private static Response responseWithToken(String generatedToken) {
        return Response
                .status(Response.Status.OK)
                .entity(new ClienteDTO(generatedToken, false))
                .build();
    }

    static Response responseConflict() {
        return Response
                .status(Response.Status.CONFLICT)
                .entity(new ClienteDTO(null, true))
                .build();
    }

    private static boolean isVerificatoErroreInterno(ClienteDTO responseDto) {
        return responseDto.isErroreInterno();
    }

    private static boolean isClienteEsiste(ClienteDTO responseDto) {
        return responseDto.isDuplicato();
    }

    static boolean validaEmail(String emailToCheck) {
        return emailToCheck == null || emailToCheck.isEmpty();
    }
}