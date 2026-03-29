package it.unina.dietiEstates25;

import it.unina.dietiEstates25.dto.request.*;
import it.unina.dietiEstates25.dto.response.AgenteCreazioneDTO;
import it.unina.dietiEstates25.dto.response.DatiDTO;
import it.unina.dietiEstates25.dto.response.GetAllRicercheResponse;
import it.unina.dietiEstates25.dto.response.UpdateDatiResponseDTO;
import it.unina.dietiEstates25.filter.RequireJWTAuthentication;
import it.unina.dietiEstates25.service.AgenteService;
import it.unina.dietiEstates25.service.GestoreService;
import it.unina.dietiEstates25.service.UpdatePasswordService;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

import static it.unina.dietiEstates25.GestoreController.getResponse;
import static it.unina.dietiEstates25.GestoreController.validaInput;

@Path("agente")
public class AgenteController {

    // =========================================================================
    // ENDPOINT: CREAZIONE (Create)
    // =========================================================================
    @Path("add")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @RequireJWTAuthentication
    public Response addAgente(AddAgenteRequestDTO agentReq) {
        String validationErrorJson = validaInput(agentReq);
        if (inputNonValido(validationErrorJson)) {
            return Response.status(Response.Status.BAD_REQUEST).entity(validationErrorJson).build();
        }
        
        AgenteService agentSvc = new AgenteService();
        AgenteCreazioneDTO creationResult = agentSvc.addAgenteService(agentReq);
        
        if (isAgenteDuplicato(creationResult)) {
            return Response.status(Response.Status.CONFLICT).entity(creationResult).build();
        }
        if (isVerificatoErroreInterno(creationResult)) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(creationResult).build();
        }
        return Response.status(Response.Status.OK).entity(creationResult).build();
    }

    // =========================================================================
    // ENDPOINT: LETTURA DATI (Read)
    // =========================================================================
    @Path("dati")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @RequireJWTAuthentication
    public Response getDatiAgente(@QueryParam("email") String targetEmail) {
        if (targetEmail == null || targetEmail.isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Email is required")
                    .build();
        }
        
        DatiDTO fetchedAgentDto = AgenteService.getAgente(targetEmail);
        
        if (fetchedAgentDto == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Agente not found")
                    .build();
        }
        return Response.status(Response.Status.OK).entity(fetchedAgentDto).build();
    }

    @Path("valutazione")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @RequireJWTAuthentication
    public Response getValutazione(@QueryParam("cf") String agentCf) {
        if (agentCf == null || agentCf.length() < 16) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        
        Integer ratingValue = AgenteService.getValutazione(agentCf);
        
        if (ratingValue == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.status(Response.Status.OK).entity(ratingValue).build();
    }

    @Path("valutazioneByEmail")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @RequireJWTAuthentication
    public Response getValutazioneByEmail(@QueryParam("email") String mailAddress) {
        if (mailAddress == null || mailAddress.isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        
        Integer ratingVal = AgenteService.getValutazioneByEmail(mailAddress);
        
        if (ratingVal == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.status(Response.Status.OK).entity(ratingVal).build();
    }

    @Path("ricerche/{cf}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRicerhce(@PathParam("cf") String fiscalCode) {
        if (isValidCF(fiscalCode)) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        
        List<GetAllRicercheResponse> searchesList = AgenteService.getRicerche(fiscalCode);
        
        if (searchesList == null || searchesList.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.status(Response.Status.OK).entity(searchesList).build();
    }

    @Path("getAccountAgenti")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAccount(@QueryParam("cf") String managerCf) {
        List<DatiDTO> agentsList = AgenteService.getAccountAgenti(managerCf);
        
        if (agentsList.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).entity(agentsList).build();
        }
        return Response.status(Response.Status.OK).entity(agentsList).build();
    }

    // =========================================================================
    // ENDPOINT: AGGIORNAMENTO (Update)
    // =========================================================================
    @Path("updatedati")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @RequireJWTAuthentication
    public Response updateDati(@QueryParam("email") String currentMail, UpdateDatiRequestDTO updateReqDto) {
        String validationJson = validaInput(updateReqDto);
        if (inputNonValido(validationJson)) {
            return Response.status(Response.Status.BAD_REQUEST).entity(validationJson).build();
        }
        
        UpdateDatiResponseDTO updateResponse = AgenteService.updateService(updateReqDto, currentMail);
        return getResponse(updateResponse);
    }

    @Path("updateBio")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @RequireJWTAuthentication
    public Response updateBio(@QueryParam("cf") String agentCode, UpdateBiografiaRequestDTO bioReq) {
        String valJson = validaInput(bioReq);
        if (inputNonValido(valJson)) {
            return Response.status(Response.Status.BAD_REQUEST).entity(valJson).build();
        }
        if (isValidCF(agentCode)) return Response.status(Response.Status.BAD_REQUEST).build();
        
        boolean isUpdated = AgenteService.updateBiografia(bioReq.getBiografia(), agentCode);
        return gestioneResponse(isUpdated);
    }

    @Path("updatePassword/agente")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public boolean updatePassword(UpdatePasswordRequestDTO pwdRequest) {
        return UpdatePasswordService.updatePasswordAgente(pwdRequest);
    }

    // =========================================================================
    // ENDPOINT: ELIMINAZIONE (Delete)
    // =========================================================================
    @Path("deleteAgente")
    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deleteAgenteByEmail(@QueryParam("email") String emailToDelete) {
        boolean isDeleted = GestoreService.deleteAgenteByEmail(emailToDelete);
        
        if (isDeleted) {
            return Response.status(Response.Status.OK).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    // =========================================================================
    // METODI HELPER E DI VALIDAZIONE (Private)
    // =========================================================================
    private static boolean isVerificatoErroreInterno(AgenteCreazioneDTO creationDto) {
        return creationDto.isErrore();
    }

    private static boolean isAgenteDuplicato(AgenteCreazioneDTO creationDto) {
        return creationDto.isCfRegistrato() || creationDto.isEmailRegistrata();
    }

    private static boolean inputNonValido(String jsonValidationStr) {
        return jsonValidationStr != null;
    }

    static boolean isValidCF(String cfToCheck) {
        return cfToCheck == null || cfToCheck.isEmpty();
    }

    private static Response gestioneResponse(boolean operationResult) {
        if (operationResult) {
            return Response.status(Response.Status.OK).build();
        } else {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }
}