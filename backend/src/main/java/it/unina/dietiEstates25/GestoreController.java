package it.unina.dietiEstates25;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.unina.dietiEstates25.dto.request.AddGestoreRequestDTO;
import it.unina.dietiEstates25.dto.request.CheckPasswordRequestDTO;
import it.unina.dietiEstates25.dto.request.UpdateDatiRequestDTO;
import it.unina.dietiEstates25.dto.response.*;
import it.unina.dietiEstates25.filter.RequireJWTAuthentication;
import it.unina.dietiEstates25.service.*;
import jakarta.validation.*;
import jakarta.ws.rs.*;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.*;

import static it.unina.dietiEstates25.AgenteController.isValidCF;
import static it.unina.dietiEstates25.ClienteController.responseConflict;
import static it.unina.dietiEstates25.ClienteController.validaEmail;
import static it.unina.dietiEstates25.ImmobileController.inputNonValido;

@Path("gestore")
public class GestoreController {

    // =========================================================================
    // COSTANTI E VARIABILI DI ISTANZA
    // =========================================================================
    private static final Logger logger = LoggerFactory.getLogger(GestoreController.class);

    // =========================================================================
    // ENDPOINT PUBBLICI - CREAZIONE (Create)
    // =========================================================================
    @RequireJWTAuthentication
    @Path("add")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addGestore(AddGestoreRequestDTO addGestoreReq) {
        String validationJson = validaInput(addGestoreReq);
        if (validationJson != null) {
            return Response.status(Response.Status.BAD_REQUEST).entity(validationJson).build();
        }
        
        GestoreDTO creationResult = AddGestoreService.addGestore(addGestoreReq);
        
        if (isEmailOCfDuplicato(creationResult)) return Response.status(Response.Status.CONFLICT).entity(creationResult).build();
        if (erroreInterno(creationResult)) return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        
        return Response.ok(creationResult).build();
    }

    // =========================================================================
    // ENDPOINT PUBBLICI - LETTURA E RICERCA (Read)
    // =========================================================================
    @Path("cf")
    @GET
    @RequireJWTAuthentication
    public List<String> getCf() throws SQLException {
        List<String> cfList = GestoreService.getAllCfService();
        if (cfList.isEmpty()) {
            return new ArrayList<>();
        } else {
            return cfList;
        }
    }

    @Path("email")
    @GET
    @RequireJWTAuthentication
    public List<String> getMail() throws SQLException {
        List<String> mailList = GestoreService.getAllEmailService();
        if (mailList.isEmpty()) {
            return new ArrayList<>();
        } else {
            return mailList;
        }
    }

    @Path("ricerche/{cf}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRicerhce(@PathParam("cf") String fiscalCode) {
        if (isValidCF(fiscalCode)) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        
        List<GetAllRicercheResponse> searchesList = GestoreService.getRicerche(fiscalCode);
        
        if (searchesList == null || searchesList.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.status(Response.Status.OK).entity(searchesList).build();
    }

    @Path("dati")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @RequireJWTAuthentication
    public Response fetchDatiOfGestore(@QueryParam("email") String targetEmail) {
        if (targetEmail == null || targetEmail.isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Email is required")
                    .build();
        }
        try {
            GestoreAgenziaImmobiliareDatiDTO fetchedDto = GestoreService.getGestoreByEmail(targetEmail);
            if (fetchedDto == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Gestore not found")
                        .build();
            }
            return Response
                    .status(Response.Status.OK)
                    .entity(fetchedDto)
                    .build();
        } catch (SQLException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error fetching data from database")
                    .build();
        }
    }

    @Path("getAccountGestori")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAccount(@QueryParam("cf") String managerCf) {
        List<DatiDTO> managersList = GestoreService.getAccountGestori(managerCf);
        
        if (managersList.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).entity(managersList).build();
        }
        return Response.status(Response.Status.OK).entity(managersList).build();
    }

    // =========================================================================
    // ENDPOINT PUBBLICI - AGGIORNAMENTO E CONTROLLO (Update)
    // =========================================================================
    @Path("update")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateGestore(UpdateDatiRequestDTO updateDataReq, @QueryParam("emailAttuale") String currentEmail) {
        if (validaEmail(currentEmail)) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Devi inserire l'email attuale!").build();
        }
        
        String validationStr = validaInput(updateDataReq);
        if (inputNonValido(validationStr)) {
            return Response.status(Response.Status.BAD_REQUEST).entity(validationStr).build();
        }
        
        UpdateDatiResponseDTO updateResult = GestoreService.updateDati(updateDataReq, currentEmail);
        return getResponse(updateResult);
    }

    @RequireJWTAuthentication
    @Path("checkPassword")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public static Response checkPassword(CheckPasswordRequestDTO pwdCheckReq) {
        String validationJson = validaInput(pwdCheckReq);
        if (validationJson != null) {
            return Response.status(Response.Status.BAD_REQUEST).entity(validationJson).build();
        }
        
        if (GestorePasswordCheckService.checkPassword(pwdCheckReq)) {
            return Response.status(Response.Status.OK).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    // =========================================================================
    // ENDPOINT PUBBLICI - CANCELLAZIONE (Delete)
    // =========================================================================
    @Path("deleteGestore")
    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deleteGestoreByEmail(@QueryParam("email") String emailToDelete) {
        boolean isDeleted = GestoreService.deleteGestoreByEmail(emailToDelete);
        if (isDeleted) {
            return Response.status(Response.Status.OK).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    // =========================================================================
    // METODI HELPER E DI VALIDAZIONE GLOBALE (Statici e Privati)
    // =========================================================================
    public static <T> String validaInput(T objectToValidate) {
        ValidatorFactory vFactory = Validation.buildDefaultValidatorFactory();
        Validator validatorInstance = vFactory.getValidator();
        String jsonErrorResult = null;
        
        Set<ConstraintViolation<T>> constraintViolations = validatorInstance.validate(objectToValidate);
        
        if (!constraintViolations.isEmpty()) {
            List<String> errorsList = new ArrayList<>();
            for (ConstraintViolation<T> violation : constraintViolations) {
                errorsList.add(violation.getMessage());
            }
            jsonErrorResult = creaJsonErrori(errorsList, jsonErrorResult);
        }
        return jsonErrorResult;
    }

    private static String creaJsonErrori(List<String> errorMsgList, String currentJsonOutput) {
        Map<String, Object> errorResponseMap = new HashMap<>();
        errorResponseMap.put("status", "error");
        errorResponseMap.put("errors", errorMsgList);
        try {
            ObjectMapper jsonMapper = new ObjectMapper();
            currentJsonOutput = jsonMapper.writeValueAsString(errorResponseMap);
        } catch (Exception ex) {
            logger.error("Errore nella verifica dell'input", ex);
        }
        return currentJsonOutput;
    }

    static Response getResponse(UpdateDatiResponseDTO responseDto) {
        if (responseDto == null) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
        if (responseDto.getEmailDuplicate()) {
            return responseConflict();
        }
        if (responseDto.getError()) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(responseDto).build();
        }
        return Response.status(Response.Status.OK).entity(responseDto).build();
    }

    private static boolean erroreInterno(GestoreDTO gestoreDto) {
        return gestoreDto.isErroreInterno();
    }

    private static boolean isEmailOCfDuplicato(GestoreDTO gestoreDto) {
        return gestoreDto.isDuplicatoCF() || gestoreDto.isDuplicatoEmail();
    }
}