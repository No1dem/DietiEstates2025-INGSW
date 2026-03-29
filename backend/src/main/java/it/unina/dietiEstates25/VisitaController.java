package it.unina.dietiEstates25;

import it.unina.dietiEstates25.dto.request.AddVisitaRequestDTO;
import it.unina.dietiEstates25.dto.response.*;
import it.unina.dietiEstates25.filter.RequireJWTAuthentication;
import it.unina.dietiEstates25.service.VisitaService;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import static it.unina.dietiEstates25.GestoreController.validaInput;
import static it.unina.dietiEstates25.ImmobileController.inputNonValido;

@Path("visita")
public class VisitaController {

    // =========================================================================
    // COSTANTI E VARIABILI DI ISTANZA
    // =========================================================================
    private static final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";

    // =========================================================================
    // ENDPOINT PUBBLICI - CREAZIONE (Create)
    // =========================================================================
    @Path("addVisita")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @RequireJWTAuthentication
    public Response addVisita(AddVisitaRequestDTO visitCreationReq) {
        String validationJson = validaInput(visitCreationReq);
        if (inputNonValido(validationJson)) {
            return Response.status(Response.Status.BAD_REQUEST).entity(validationJson).build();
        }
        
        VisitaResponseDTO creationResult = VisitaService.addVisita(visitCreationReq);
        
        if (creationResult.getFail()) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(creationResult).build();
        }
        return Response.status(Response.Status.OK).entity(creationResult).build();
    }

    // =========================================================================
    // ENDPOINT PUBBLICI - LETTURA E RICERCA (Read)
    // =========================================================================
    @Path("getVisiteOf")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @RequireJWTAuthentication
    public Response getVisiteOf(@QueryParam("cf") String agentCf) {
        if (agentCf.length() != 16) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        
        List<GetVisiteResponse> agentVisitsList = VisitaService.getAllVisiteByCf(agentCf);
        
        return Response.status(Response.Status.OK).entity(agentVisitsList).build();
    }

    @Path("getInfoAboutVisita")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @RequireJWTAuthentication
    public Response getInfoAboutVisita(@QueryParam("idNotifica") int notificationId) {
        InformazioniVisitaDTO visitDetailsDto = VisitaService.getInfoAboutVisita(notificationId);
        
        if (visitDetailsDto == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.status(Response.Status.OK).entity(visitDetailsDto).build();
    }

    @Path("dateEOreOccupate")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @RequireJWTAuthentication
    public Response dateEOreOccupate(@QueryParam("cf") String targetAgentCf) {
        if (targetAgentCf == null || targetAgentCf.length() != 16) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        
        List<DateEOreOccupateAgente> occupiedSlots = VisitaService.getDateEOreOccupateAgente(targetAgentCf);
        
        if (occupiedSlots == null) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
        return Response.status(Response.Status.OK).entity(occupiedSlots).build();
    }

    /**
     * Ritorna true se il cliente NON ha già una visita per quell'immobile.
     */
    @Path("checkVisita/{idImmobile}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @RequireJWTAuthentication
    public Response checkVisitaPerCliente(@PathParam("idImmobile") Integer propertyId,
                                          @QueryParam("email") String clientEmail) {
        Response validationResponse = validateInputsForCheckVisita(clientEmail, propertyId);
        if (validationResponse != null) {
            return validationResponse;
        }
        
        CheckVisitaPerClienteDTO visitCheckDto = VisitaService.checkUtenteHaVisitaPerImmobile(clientEmail, propertyId);
        
        if (visitCheckDto.isVisitGiaPrenotata()) {
            return Response.status(Response.Status.NOT_FOUND).entity("Il cliente ha già una visita a carico").build();
        } else if (visitCheckDto.isInternalError()) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
        return Response.status(Response.Status.OK).entity(true).build();
    }

    // =========================================================================
    // ENDPOINT PUBBLICI - CANCELLAZIONE (Delete / Action)
    // =========================================================================
    @Path("delete/visita")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public static Response deleteVisita(AddVisitaRequestDTO visitToDeleteReq) {
        String validationString = validaInput(visitToDeleteReq);
        if (inputNonValido(validationString)) {
            return Response.status(Response.Status.BAD_REQUEST).entity(validationString).build();
        }
        
        boolean isDeletedSuccessfully = VisitaService.deleteVisita(visitToDeleteReq);
        
        if (isDeletedSuccessfully) {
            return Response.status(Response.Status.OK).build();
        }
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
    }

    // =========================================================================
    // METODI HELPER E DI VALIDAZIONE (Private)
    // =========================================================================
    private Response validateInputsForCheckVisita(String mailToCheck, Integer propIdToCheck) {
        if (mailToCheck == null || !mailToCheck.matches(EMAIL_REGEX) || propIdToCheck == null || propIdToCheck <= 0) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        return null;
    }
}