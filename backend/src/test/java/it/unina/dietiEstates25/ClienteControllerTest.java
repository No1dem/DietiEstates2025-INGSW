package it.unina.dietiEstates25;
import it.unina.dietiEstates25.dao.ClienteDao;
import it.unina.dietiEstates25.dto.request.LasciaRecensioneRequestDTO;
import it.unina.dietiEstates25.service.ClienteService;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

class ClienteControllerTest {

    @InjectMocks
    private ClienteController controller;

    @Mock
    private ClienteService clienteService;

    @Mock
    private ClienteDao clienteDao;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private LasciaRecensioneRequestDTO creaRecensione(String agenteDaRecensire, int valutazione) {
        LasciaRecensioneRequestDTO recensioneDTO = new LasciaRecensioneRequestDTO();
        recensioneDTO.setAgenteDaRecensire(agenteDaRecensire);
        recensioneDTO.setValutazione(valutazione);
        return recensioneDTO;
    }

    @ParameterizedTest
    @CsvSource({
            "null, 1",       // Codice fiscale null
            "'', 2",         // Codice fiscale vuoto
            "'SVN', 5",      // Codice fiscale non valido
            "'SVNGLN03T05H892R', -1",  // Valutazione negativa
            "'SVNGLN03T05H892R', 6"    // Valutazione superiore a 5
    })
    void testLasciaRecensione_InputNonValido(String agenteDaRecensire, int valutazione) {
        // Arrange
        LasciaRecensioneRequestDTO recensioneDTO = creaRecensione(agenteDaRecensire.equals("null") ? null : agenteDaRecensire
                , valutazione);
        // Configurazione mock
        Mockito.when(clienteDao.lasciaRecensione(Mockito.any(LasciaRecensioneRequestDTO.class))).thenReturn(false);
        Mockito.when(clienteService.lasciaRecensione(Mockito.any(LasciaRecensioneRequestDTO.class))).thenReturn(false);
        // Act
        Response response = controller.lasciaRecensione(recensioneDTO);
        // Assert
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
        //Nessun interazione perchè il controller lo impedisce, tramite dei controlli
        verifyNoInteractions(clienteService);
    }

    @ParameterizedTest
    @CsvSource({
            "'SSKTGH03T05H892R', 4", // Codice fiscale valido e valutazione valida
            "'LCMGLN03T05H892R', 3"  // Altro caso valido
    })
    void testLasciaRecensione_InputValido(String agenteDaRecensire, int valutazione) {
        // Arrange: Creiamo la richiesta DTO
        LasciaRecensioneRequestDTO recensioneDTO = creaRecensione(agenteDaRecensire.equals("null") ? null : agenteDaRecensire
                , valutazione);

        // Mock dei metodi
        Mockito.when(clienteDao.lasciaRecensione(Mockito.any(LasciaRecensioneRequestDTO.class))).thenReturn(true);
        Mockito.when(clienteService.lasciaRecensione(Mockito.any(LasciaRecensioneRequestDTO.class))).thenReturn(true);

        // Act Chiamata al controller
        Response response = controller.lasciaRecensione(recensioneDTO);

        // Assert Verifica che il controller restituisce status code esatto
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());

        //Verifico che c'è stata almeno un interazione
        verify(clienteService).lasciaRecensione(Mockito.any(LasciaRecensioneRequestDTO.class));
    }

}
