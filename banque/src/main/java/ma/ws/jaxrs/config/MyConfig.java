package ma.ws.jaxrs.config;

import ma.ws.jaxrs.controllers.CompteRestJaxRSAPI;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MyConfig {

    @Bean
    public ResourceConfig resourceConfig() {
        ResourceConfig jerseyServlet = new ResourceConfig();

        // Enregistrement du contrôleur JAX-RS
        jerseyServlet.register(CompteRestJaxRSAPI.class);

        // Jersey détectera automatiquement Jackson pour JSON/XML
        // grâce aux dépendances Spring Boot

        return jerseyServlet;
    }
}