package it.unina.dietiEstates25;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

class VisitaControllerTest {
    private VisitaController controller;

    @BeforeEach
    void setUp() {
        controller = new VisitaController();
    }

    private Response callCheckVisita(Integer idImmobile, String email) {
        return controller.checkVisitaPerCliente(idImmobile, email);
    }


    @ParameterizedTest
    @CsvSource({
            "0, ''",               // Email vuota
            "1, 'invalidEmail'",   // Email non valida
            "2, null"              // Email nulla
    })
    void testCheckVisita_EmailInvalid(Integer idImmobile, String email) {
        // Act
        Response response = callCheckVisita(idImmobile, email);

        // Assert
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
    }


    @Test
    void testCheckVisita_EmailValidIdNull(){
        //Arrange
        String email = "email123@gmail.com";
        //Act
        Response response = callCheckVisita(null, email);
        //Assert
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
    }

    @Test
    void testCheckVisita_EmailValidIdNegativ(){
        //Arrange
        String email = "email123@gmail.com";
        int idImmobile = -1;
        //Act
        Response response = callCheckVisita(idImmobile, email);
        //Assert
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
    }

    @Test
    void testCheckVisita_EmailValidIdZero(){
        //Arrange
        String email = "email123@gmail.com";
        int idImmobile = 0;
        //Act
        Response response = callCheckVisita(idImmobile, email);
        //Assert
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
    }


    @Test
    void testCheckVisita_EmailValidIdPositive(){
        //Arrange
        String email = "email123@gmail.com";
        int idImmobile = 1;
        //Act
        Response response = callCheckVisita(idImmobile, email);
        //Assert
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }


}
