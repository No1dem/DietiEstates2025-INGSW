package it.unina.dietiEstates25;

import it.unina.dietiEstates25.dto.response.ImmobileResponseRicercaDTO;
import it.unina.dietiEstates25.service.ImmobileService;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ImmobileControllerTest {

    private ImmobileController immobileController;

    @BeforeEach
    void setUp() {
        // Inizializziamo il controller prima di ogni test
        immobileController = new ImmobileController();
    }

    /**
     * TEST PARAMETRIZZATO CON 2 PARAMETRI:
     * 1. agentMail: L'input che simuliamo arrivi dal frontend.
     * 2. expectedStatus: Il codice HTTP che ci aspettiamo che il controller restituisca.
     */
    @ParameterizedTest
    @CsvSource({
            ", 400",                    // Caso 1: mail è null -> Si aspetta 400 (BAD REQUEST)
            "'', 400",                  // Caso 2: mail è vuota -> Si aspetta 400 (BAD REQUEST)
            "'agente@test.it', 200",    // Caso 3: mail valida CON risultati -> Si aspetta 200 (OK)
            "'vuoto@test.it', 404"      // Caso 4: mail valida SENZA risultati -> Si aspetta 404 (NOT FOUND)
    })
    void testGetAllAnnunciAgente(String agentMail, int expectedStatus) {
        
        // Apriamo un contesto per mockare i metodi statici di ImmobileService
        try (MockedStatic<ImmobileService> mockedService = Mockito.mockStatic(ImmobileService.class)) {

            // ISTRUZIONI PER IL MOCK: Diciamo al finto Database come comportarsi
            if ("agente@test.it".equals(agentMail)) {
                // Se la mail è giusta, fingiamo che il DB restituisca una lista con 1 immobile 
                mockedService.when(() -> ImmobileService.getImmobileForAgente(agentMail))
                        .thenReturn(List.of(ImmobileResponseRicercaDTO.builder().build())); 
            } else if ("vuoto@test.it".equals(agentMail)) {
                // Se la mail è "vuoto", fingiamo che l'agente non abbia immobili (lista vuota)
                mockedService.when(() -> ImmobileService.getImmobileForAgente(agentMail))
                        .thenReturn(new ArrayList<>()); 
            }

            // ESECUZIONE: Chiamiamo il metodo del controller esattamente come farebbe l'API
            Response response = immobileController.getAllAnnunciAgente(agentMail);

            // ASSERZIONE: Verifichiamo che il codice HTTP restituito sia uguale a quello atteso (expectedStatus)
            assertEquals(expectedStatus, response.getStatus(), 
                    "Il codice di stato HTTP restituito non combacia con quello atteso per l'input: " + agentMail);
        }
    }
}