package it.unina.dietiEstates25;
import it.unina.dietiEstates25.filter.ValidationExceptionMapper;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.grizzly.http.server.HttpServer;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;

public class Main {

    // Base URI the Grizzly HTTP server will listen on
    public static final String BASE_URI = "http://0.0.0.0:9094/";

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(Main.class);


    /**
     * Starts Grizzly HTTP server exposing JAX-RS resources defined in this application.
     * @return Grizzly HTTP server.
     */
    public static HttpServer startServer() {
        // Create a resource config that scans for JAX-RS resources and providers
        // in the it.unina.dietiEstates25 package, including the validation exception mapper
        final ResourceConfig rc = new ResourceConfig()
                .packages("it.unina.dietiEstates25")   // Registrazione delle risorse
                .register(ValidationExceptionMapper.class);

        // Create and start a new instance of Grizzly HTTP server exposing the Jersey application at BASE_URI
        return GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), rc);
    }

    public static void main(String[] args) throws IOException {
        // Start the server
        final HttpServer server = startServer();

        // Output server information
        logger.info("Jersey app started with endpoints available at {}%nHit Ctrl-C to stop it...", BASE_URI);


        System.in.read();
        server.shutdown();
    }
}
