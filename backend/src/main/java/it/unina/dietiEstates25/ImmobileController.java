package it.unina.dietiEstates25;

import it.unina.dietiEstates25.dto.request.AddImmobileRequestDTO;
import it.unina.dietiEstates25.dto.request.RicercaImmobileDTORequest;
import it.unina.dietiEstates25.dto.response.DatiImmobileDTO;
import it.unina.dietiEstates25.dto.response.ImmobileDTO;
import it.unina.dietiEstates25.dto.response.ImmobileResponseRicercaDTO;
import it.unina.dietiEstates25.filter.RequireJWTAuthentication;
import it.unina.dietiEstates25.model.Immobile;
import it.unina.dietiEstates25.service.ImmobileService;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.ArrayList;
import java.util.List;
import static it.unina.dietiEstates25.GestoreController.validaInput;

@Path("immobile")
public class ImmobileController {

    // =========================================================================
    // ENDPOINT PUBBLICI - CREAZIONE (Create)
    // =========================================================================
    @Path("add")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @RequireJWTAuthentication
    public static Response addImmobile(AddImmobileRequestDTO addPropertyReq) {
        String validationErrorJson = validaInput(addPropertyReq);
        if (inputNonValido(validationErrorJson)) {
            return Response.status(Response.Status.BAD_REQUEST).entity(validationErrorJson).build();
        }
        
        ImmobileDTO creationStatus = ImmobileService.addImmobile(addPropertyReq);
        
        if (isImmobileNonInserito(creationStatus)) {
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(new ImmobileDTO(false, true, true))
                    .build();
        }
        return Response
                .status(Response.Status.OK)
                .entity(new ImmobileDTO(true, false, false))
                .build();
    }

    // =========================================================================
    // ENDPOINT PUBBLICI - LETTURA E RICERCA (Read)
    // =========================================================================
    @Path("annunciRecenti")
    @GET
    @RequireJWTAuthentication
    public List<Immobile> getAllAnnunci(@QueryParam("email") String agentEmail) {
        List<Immobile> recentProperties = ImmobileService.getAllAnnunci(agentEmail);
        if (recentProperties.isEmpty()) {
            return new ArrayList<>();
        }
        return recentProperties;
    }

    @Path("searchImmobile")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @RequireJWTAuthentication
    public Response getImmobili(RicercaImmobileDTORequest searchCriteria) {
        List<ImmobileResponseRicercaDTO> searchResults = ImmobileService.getImmobile(searchCriteria);
        if (searchResults.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(searchResults).build();
    }

    @Path("getInfoAboutImmobile")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @RequireJWTAuthentication
    public Response getInfoAbout(@QueryParam("idImmobile") int propertyId) {
        DatiImmobileDTO propertyDetails = ImmobileService.getInfoAboutImmobile(propertyId);
        if (propertyDetails == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(propertyDetails).build();
    }

    @Path("AnnunciAgente")
    @GET
    @RequireJWTAuthentication
    public Response getAllAnnunciAgente(@QueryParam("mail") String agentMail) {
        if (agentMail == null || agentMail.isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        List<ImmobileResponseRicercaDTO> agentProperties = ImmobileService.getImmobileForAgente(agentMail);
        if (agentProperties.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(agentProperties).build();
    }

    @Path("getInfoAboutImmobileById")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @RequireJWTAuthentication
    public Response getInfoAboutAnnuncio(@QueryParam("idImmobile") int propertyId) {
        ImmobileResponseRicercaDTO propertyData = ImmobileService.getInfoAboutImmobileById(propertyId);
        if (propertyData == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(propertyData).build();
    }

    // =========================================================================
    // ENDPOINT PUBBLICI - CANCELLAZIONE (Delete)
    // =========================================================================
    @Path("deleteImmobile")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteImmobileById(@QueryParam("id") int targetPropertyId) {
        boolean isDeleted = ImmobileService.deleteImmobileById(targetPropertyId);
        if (!isDeleted) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok().build();
    }

    // =========================================================================
    // METODI HELPER E DI VALIDAZIONE (Private / Utility)
    // =========================================================================
    public static boolean inputNonValido(String validationString) {
        return validationString != null;
    }

    private static boolean isImmobileNonInserito(ImmobileDTO statusDto) {
        return !statusDto.isImmobileInserito();
    }
}