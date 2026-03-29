package it.unina.dietiEstates25;

import it.unina.dietiEstates25.dto.request.UpdateCategoriaNotificaDTO;
import it.unina.dietiEstates25.dto.response.NotificaDTO;
import it.unina.dietiEstates25.dto.response.UpdateCategoriaResponse;
import it.unina.dietiEstates25.model.CategoriaNotifica;
import it.unina.dietiEstates25.model.Notifica;
import it.unina.dietiEstates25.service.CategoriaService;
import it.unina.dietiEstates25.service.NotificaService;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class NotificaControllerTest {

    @InjectMocks
    private NotificaController controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // =========================================================================
    // TEST GET ALL NOTIFICATIONS GESTORE
    // =========================================================================

    @Test
    void testGetAllNotificationsOfGestore_Success() {
        String validCf = "RSSMRA80A01H501Z";
        List<Notifica> mockList = new ArrayList<>();
        mockList.add(new Notifica()); 

        try (MockedStatic<NotificaService> mockedService = Mockito.mockStatic(NotificaService.class)) {
            mockedService.when(() -> NotificaService.getNotificationOfAdminService(validCf)).thenReturn(mockList);

            Response response = controller.getAllNotificationsOfGestore(validCf);

            assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
            assertEquals(mockList, response.getEntity());
        }
    }

    @Test
    void testGetAllNotificationsOfGestore_NotFound() {
        String validCf = "RSSMRA80A01H501Z";

        try (MockedStatic<NotificaService> mockedService = Mockito.mockStatic(NotificaService.class)) {
            mockedService.when(() -> NotificaService.getNotificationOfAdminService(validCf)).thenReturn(Collections.emptyList());

            Response response = controller.getAllNotificationsOfGestore(validCf);

            assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
        }
    }

    // =========================================================================
    // TEST GET ALL NOTIFICATIONS CLIENTE (Multi-Parametro)
    // =========================================================================

    /**
     * TEST PARAMETRIZZATO CON 2 PARAMETRI:
     */
    @ParameterizedTest
    @CsvSource({
            "'', 400",                       // Caso 1: email vuota -> 400 BAD REQUEST
            "'cliente@test.it', 200",        // Caso 2: email valida CON notifiche -> 200 OK
            "'nessunanotifica@test.it', 404" // Caso 3: email valida SENZA notifiche -> 404 NOT FOUND
    })
    void testGetAllNotificationsCliente(String email, int expectedStatus) {
        try (MockedStatic<NotificaService> mockedService = Mockito.mockStatic(NotificaService.class)) {

            // Configuriamo il mock in base alla mail ricevuta in input dal parametro
            if ("cliente@test.it".equals(email)) {
                mockedService.when(() -> NotificaService.getNotificaOfClienteService(email))
                        .thenReturn(List.of(new Notifica()));
            } else if ("nessunanotifica@test.it".equals(email)) {
                mockedService.when(() -> NotificaService.getNotificaOfClienteService(email))
                        .thenReturn(Collections.emptyList());
            }

            // Eseguiamo il metodo del controller
            Response response = controller.getAllNotificationsCliente(email);

            // Verifichiamo che lo status corrisponda a quello atteso nel CsvSource
            assertEquals(expectedStatus, response.getStatus(), "Errore con la mail: " + email);
        }
    }

    // =========================================================================
    // TEST AZIONI SULLE NOTIFICHE (PUT/DELETE)
    // =========================================================================

    @Test
    void testAccettaNotifica_Success() {
        int idNotifica = 1;
        NotificaDTO mockNotificaDTO = new NotificaDTO();

        try (MockedStatic<NotificaService> mockedService = Mockito.mockStatic(NotificaService.class)) {
            mockedService.when(() -> NotificaService.setNotificationAccepted(idNotifica)).thenReturn(mockNotificaDTO);

            NotificaDTO result = controller.accettaNotifica(idNotifica);

            assertEquals(mockNotificaDTO, result);
        }
    }

    /**
     * TEST PARAMETRIZZATO CON 3 PARAMETRI:
     * idNotifica, risultato del service simulato, status atteso.
     */
    @ParameterizedTest
    @CsvSource({
            "1, true, 200",   // ID 1 -> Il DB lo trova e lo annulla (true) -> Ci aspettiamo 200 OK
            "999, false, 404" // ID 999 -> Il DB non lo trova (false) -> Ci aspettiamo 404 NOT FOUND
    })
    void testAnnullaInvioNotifica(int idNotifica, boolean serviceMockResponse, int expectedStatus) {
        try (MockedStatic<NotificaService> mockedService = Mockito.mockStatic(NotificaService.class)) {
            // Usiamo il booleano fornito dai parametri per istruire il mock
            mockedService.when(() -> NotificaService.annullaInvioNotifica(idNotifica)).thenReturn(serviceMockResponse);

            Response response = controller.annullaInvioNotifica(idNotifica);

            assertEquals(expectedStatus, response.getStatus());
        }
    }

    // =========================================================================
    // TEST RECUPERO CATEGORIE (GET)
    // =========================================================================

    @ParameterizedTest
    @ValueSource(strings = {"", "CF_CORTO", "null_value"}) 
    void testGetCategorieDisattivateAgente_CfInvalido(String invalidCf) {
        String inputCf = invalidCf.equals("null_value") ? null : invalidCf;
        
        Response response = controller.getCategorieDisattivateAgente(inputCf);
        
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
    }

    @Test
    void testGetCategorieDisattivateAgente_Success() {
        String validCf = "RSSMRA80A01H501Z";
        
        List<CategoriaNotifica> mockList = List.of(CategoriaNotifica.VISITAIMMOBILE);

        try (MockedStatic<CategoriaService> mockedService = Mockito.mockStatic(CategoriaService.class)) {
            mockedService.when(() -> CategoriaService.getCategorieDisattivateByCfAgente(validCf)).thenReturn(mockList);

            Response response = controller.getCategorieDisattivateAgente(validCf);

            assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        }
    }
    
    // =========================================================================
    // TEST DISATTIVAZIONE CATEGORIA CLIENTE (POST con Validazione)
    // =========================================================================

    @Test
    void testDisattivaCategoriaCliente_Success() {
        String email = "mario.rossi@test.it";
        UpdateCategoriaNotificaDTO dto = new UpdateCategoriaNotificaDTO();
        
        UpdateCategoriaResponse serviceResponse = new UpdateCategoriaResponse();
        serviceResponse.setSuccess(true); 

        try (
            MockedStatic<GestoreController> mockGestore = Mockito.mockStatic(GestoreController.class);
            MockedStatic<ImmobileController> mockImmobile = Mockito.mockStatic(ImmobileController.class);
            MockedStatic<CategoriaService> mockCatService = Mockito.mockStatic(CategoriaService.class)
        ) {
            mockGestore.when(() -> GestoreController.validaInput(dto)).thenReturn(null);
            mockImmobile.when(() -> ImmobileController.inputNonValido(null)).thenReturn(false);
            
            mockCatService.when(() -> CategoriaService.setNonInviareCategoriaAdCliente(email, dto)).thenReturn(serviceResponse);

            Response response = controller.disattivaCategoriaCliente(email, dto);

            assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
            assertEquals(serviceResponse, response.getEntity());
        }
    }
}