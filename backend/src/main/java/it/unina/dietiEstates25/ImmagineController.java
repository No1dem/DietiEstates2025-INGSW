package it.unina.dietiEstates25;

import it.unina.dietiEstates25.dto.request.ImageUploadRequest;
import it.unina.dietiEstates25.dto.response.FotoDTO;
import it.unina.dietiEstates25.service.ImmagineService;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("image")
public class ImmagineController {

    // =========================================================================
    // ENDPOINT PUBBLICI - CARICAMENTO (Upload / POST)
    // =========================================================================
    @Path("upload")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response uploadImmagineUtente(@QueryParam("codicefiscale") String agentCf, ImageUploadRequest imageRequest) {
        if (isNotValidCF(agentCf)) {
            return badRequest();
        }
        
        FotoDTO dbUploadResult = ImmagineService.addImageAlDB(imageRequest.getKey(), agentCf);
        
        if (dbUploadResult.isErrore()) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(dbUploadResult).build();
        }
        return Response.ok().build();
    }

    // =========================================================================
    // ENDPOINT PUBBLICI - LETTURA DATI (Fetch / GET)
    // =========================================================================
    @GET
    @Path("fetchByCf")
    public Response getImmagineByCf(@QueryParam("codicefiscale") String fiscalCode) {
        if (isNotValidCF(fiscalCode)) {
            return badRequest();
        }
        
        List<String> assetIds = ImmagineService.getPublicIdByCF(fiscalCode);
        
        if (assetIds.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(assetIds).build();
    }

    @GET
    @Path("getImmagineByIdImmobile")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getImmagineByIdImmobile(@QueryParam("idImmobile") int propertyId) {
        List<String> propertyImages = ImmagineService.getUriOfImmobile(propertyId);
        
        if (propertyImages.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(propertyImages).build();
    }

    // =========================================================================
    // METODI HELPER E DI VALIDAZIONE (Private)
    // =========================================================================
    private static boolean isNotValidCF(String cfToCheck) {
        return cfToCheck.length() != 16;
    }

    private static Response badRequest() {
        return Response.status(Response.Status.BAD_REQUEST).build();
    }
}