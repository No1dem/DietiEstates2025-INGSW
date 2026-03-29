package it.unina.dietiEstates25;

import it.unina.dietiEstates25.dto.request.UpdateCategoriaNotificaDTO;
import it.unina.dietiEstates25.dto.response.NotificaDTO;
import it.unina.dietiEstates25.dto.response.UpdateCategoriaResponse;
import it.unina.dietiEstates25.filter.RequireJWTAuthentication;
import it.unina.dietiEstates25.model.CategoriaNotifica;
import it.unina.dietiEstates25.service.CategoriaService;
import it.unina.dietiEstates25.service.NotificaService;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import static it.unina.dietiEstates25.GestoreController.validaInput;
import static it.unina.dietiEstates25.ImmobileController.inputNonValido;

@Path("not")
public class NotificaController {

    // =========================================================================
    // ENDPOINT PUBBLICI - LETTURA NOTIFICHE (GET)
    // =========================================================================
    @GET
    @RequireJWTAuthentication
    public Response getAllNotificationsOfGestore(@QueryParam("cf") String managerCf) {
        List<it.unina.dietiEstates25.model.Notifica> managerNotifications = NotificaService.getNotificationOfAdminService(managerCf);
        if (managerNotifications.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(managerNotifications).build();
    }

    @GET
    @Path("/agente")
    @RequireJWTAuthentication
    public Response getAllNotificationsAgente(@QueryParam("cf") String agentCf) {
        List<it.unina.dietiEstates25.model.Notifica> agentNotifications = NotificaService.getNotificaOfAgenteService(agentCf);
        if (agentNotifications.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(agentNotifications).build();
    }

    @GET
    @Path("/cliente")
    @Produces(MediaType.APPLICATION_JSON)
    @RequireJWTAuthentication
    public Response getAllNotificationsCliente(@QueryParam("email") String clientEmail) {
        if (clientEmail.isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        List<it.unina.dietiEstates25.model.Notifica> clientNotifications = NotificaService.getNotificaOfClienteService(clientEmail);
        if (clientNotifications.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(clientNotifications).build();
    }

    // =========================================================================
    // ENDPOINT PUBBLICI - LETTURA CATEGORIE (GET)
    // =========================================================================
    @Path("categorieDisattivateAgente")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @RequireJWTAuthentication
    public Response getCategorieDisattivateAgente(@QueryParam("cf") String agentId) {
        if (isValidCF(agentId)) return Response.status(Response.Status.BAD_REQUEST).build();
        List<CategoriaNotifica> disabledAgentCategories = CategoriaService.getCategorieDisattivateByCfAgente(agentId);
        if (disabledAgentCategories.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(disabledAgentCategories).build();
    }

    @Path("categorieCliente")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @RequireJWTAuthentication
    public Response getAllCategorieCliente(@QueryParam("email") String userMail) {
        if (userMail == null || userMail.isEmpty()) return Response.status(Response.Status.BAD_REQUEST).build();
        List<CategoriaNotifica> allClientCategories = CategoriaService.getAllCategorieByEmailCliente(userMail);
        if (allClientCategories.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(allClientCategories).build();
    }

    @Path("categorieAgente")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @RequireJWTAuthentication
    public Response getAllCategorieAgente(@QueryParam("cf") String agentFiscalCode) {
        if (isValidCF(agentFiscalCode)) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        List<CategoriaNotifica> allAgentCategories = CategoriaService.getAllCategorieByCFAgente(agentFiscalCode);
        if (allAgentCategories.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(allAgentCategories).build();
    }

    @Path("categorieDisattivateCliente")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @RequireJWTAuthentication
    public Response getCategorieDisattivateCliente(@QueryParam("email") String clientAddress) {
        if (clientAddress == null || clientAddress.isEmpty()) return Response.status(Response.Status.BAD_REQUEST).build();
        List<CategoriaNotifica> disabledClientCategories = CategoriaService.getCategorieDisattivateByClienteEmail(clientAddress);
        if (disabledClientCategories.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(disabledClientCategories).build();
    }

    // =========================================================================
    // ENDPOINT PUBBLICI - AZIONI SU NOTIFICHE (PUT / DELETE)
    // =========================================================================
    @PUT
    @Path("/{id}/accetta")
    @Produces(MediaType.APPLICATION_JSON)
    @RequireJWTAuthentication
    public NotificaDTO accettaNotifica(@PathParam("id") int notificationId) {
        return NotificaService.setNotificationAccepted(notificationId);
    }

    @DELETE
    @Path("/{id}/elimina")
    @Produces(MediaType.APPLICATION_JSON)
    @RequireJWTAuthentication
    public NotificaDTO eliminaNotifica(@PathParam("id") int notificationIdToDelete) {
        return NotificaService.setNotificationRejected(notificationIdToDelete);
    }

    @PUT
    @Path("/{id}/annullaInvioNotifica")
    @Produces(MediaType.APPLICATION_JSON)
    @RequireJWTAuthentication
    public Response annullaInvioNotifica(@PathParam("id") int notificationIdToCancel) {
        boolean isCanceled = NotificaService.annullaInvioNotifica(notificationIdToCancel);
        if (!isCanceled) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok().build();
    }

    // =========================================================================
    // ENDPOINT PUBBLICI - AZIONI SU CATEGORIE (POST)
    // =========================================================================
    @POST
    @Path("/disattivaCategoria/agente")
    @Produces(MediaType.APPLICATION_JSON)
    @RequireJWTAuthentication
    public Response disattivaCategoriaAgente(@QueryParam("cf") String targetAgentCf, UpdateCategoriaNotificaDTO categoryUpdateReq) {
        if (isValidCF(targetAgentCf)) return Response.status(Response.Status.BAD_REQUEST).build();
        String validationErrorStr = validaInput(categoryUpdateReq);
        if (inputNonValido(validationErrorStr)) {
            return Response.status(Response.Status.BAD_REQUEST).entity(validationErrorStr).build();
        }
        UpdateCategoriaResponse updateResult = CategoriaService.setNonInviareCategoriaAdAgente(targetAgentCf, categoryUpdateReq);
        return getResponse(updateResult);
    }

    @POST
    @Path("/attivaCategoria/agente")
    @Produces(MediaType.APPLICATION_JSON)
    @RequireJWTAuthentication
    public Response attivaCategoriaAgente(@QueryParam("cf") String activatingAgentCf, UpdateCategoriaNotificaDTO categoryDto) {
        if (isValidCF(activatingAgentCf)) return Response.status(Response.Status.BAD_REQUEST).build();
        String valJson = validaInput(categoryDto);
        if (inputNonValido(valJson)) {
            return Response.status(Response.Status.BAD_REQUEST).entity(valJson).build();
        }
        UpdateCategoriaResponse activationResult = CategoriaService.setInviareCategoriaAdAgente(activatingAgentCf, categoryDto);
        return getResponse(activationResult);
    }

    @POST
    @Path("/attivaCategoria/cliente")
    @Produces(MediaType.APPLICATION_JSON)
    @RequireJWTAuthentication
    public Response attivaCategoriaCliente(@QueryParam("email") String clientTargetEmail, UpdateCategoriaNotificaDTO activeCategoryReq) {
        if (validaEmail(clientTargetEmail)) return Response.status(Response.Status.BAD_REQUEST).build();
        String jsonErrors = validaInput(activeCategoryReq);
        if (inputNonValido(jsonErrors)) {
            return Response.status(Response.Status.BAD_REQUEST).entity(jsonErrors).build();
        }
        UpdateCategoriaResponse clientActivationRes = CategoriaService.setInviaCategoriaAdCliente(clientTargetEmail, activeCategoryReq);
        return getResponse(clientActivationRes);
    }

    @POST
    @Path("/disattivaCategoria/cliente")
    @Produces(MediaType.APPLICATION_JSON)
    @RequireJWTAuthentication
    public Response disattivaCategoriaCliente(@QueryParam("email") String deactivatingClientEmail, UpdateCategoriaNotificaDTO disabledCategoryDto) {
        if (validaEmail(deactivatingClientEmail)) return Response.status(Response.Status.BAD_REQUEST).build();
        String validationResultJson = validaInput(disabledCategoryDto);
        if (inputNonValido(validationResultJson)) {
            return Response.status(Response.Status.BAD_REQUEST).entity(validationResultJson).build();
        }
        UpdateCategoriaResponse clientDeactivationRes = CategoriaService.setNonInviareCategoriaAdCliente(deactivatingClientEmail, disabledCategoryDto);
        return getResponse(clientDeactivationRes);
    }

    // =========================================================================
    // METODI HELPER E DI VALIDAZIONE (Private)
    // =========================================================================
    private static boolean isValidCF(String cfToCheck) {
        return (cfToCheck == null || cfToCheck.length() < 16);
    }

    private static boolean validaEmail(String emailToCheck) {
        return (emailToCheck == null || emailToCheck.isEmpty());
    }

    private Response getResponse(UpdateCategoriaResponse categoryResponseData) {
        if (categoryResponseData.isSuccess()) {
            return Response.ok(categoryResponseData).build();
        } else {
            if (categoryResponseData.isNessunaNotificaTrovata()) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
            if (categoryResponseData.isCfExists()) {
                return Response.status(Response.Status.CONFLICT).build();
            }
        }
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
    }
}