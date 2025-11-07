package ma.ws.jaxrs;

import ma.ws.jaxrs.entities.Compte;
import ma.ws.jaxrs.model.TypeCompte;
import ma.ws.jaxrs.repositories.CompteRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Date;

@SpringBootApplication
public class JaxrsApplication {

    public static void main(String[] args) {
        SpringApplication.run(JaxrsApplication.class, args);
    }

    @Bean
    CommandLineRunner start(CompteRepository compteRepository) {
        return args -> {
            // Création de comptes initiaux pour les tests
            compteRepository.save(new Compte(null, 7600.0, new Date(), TypeCompte.EPARGNE));
            compteRepository.save(new Compte(null, 1200.0, new Date(), TypeCompte.COURANT));
            compteRepository.save(new Compte(null, 18500.0, new Date(), TypeCompte.EPARGNE));
            compteRepository.save(new Compte(null, 450.0, new Date(), TypeCompte.COURANT));
            compteRepository.save(new Compte(null, 9200.0, new Date(), TypeCompte.EPARGNE));

            // Affichage des comptes créés
            System.out.println("=== COMPTES INITIALISÉS ===");
            compteRepository.findAll().forEach(compte -> {
                System.out.printf("Compte [id=%d, solde=%.2f, type=%s]%n",
                        compte.getId(), compte.getSolde(), compte.getType());
            });
            System.out.println("============================");
        };
    }
}