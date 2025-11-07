package ma.ws.jaxrs.controllers;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import ma.ws.jaxrs.entities.Compte;
import ma.ws.jaxrs.model.TypeCompte;
import ma.ws.jaxrs.repositories.CompteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@Path("/banque")
@Slf4j
public class CompteRestJaxRSAPI {

    @Autowired
    private CompteRepository compteRepository;

    /**
     * Endpoint de santé - Vérifie que l'API fonctionne
     */
    @Path("/health")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String health() {
        long count = compteRepository.count();
        return String.format("✅ API Banque JAX-RS is running! Total comptes: %d", count);
    }

    /**
     * Récupère tous les comptes
     */
    @Path("/comptes")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Compte> getComptes() {
        try {
            List<Compte> comptes = compteRepository.findAll();
            log.info("Récupération de {} comptes", comptes.size());
            return comptes;
        } catch (Exception e) {
            log.error("Erreur lors de la récupération des comptes", e);
            throw new WebApplicationException("Erreur interne du serveur", Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Récupère un compte par son ID
     */
    @Path("/comptes/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Compte getCompte(@PathParam("id") Long id) {
        try {
            Optional<Compte> compte = compteRepository.findById(id);
            if (compte.isPresent()) {
                log.info("Compte trouvé avec ID: {}", id);
                return compte.get();
            } else {
                log.warn("Compte non trouvé avec ID: {}", id);
                throw new WebApplicationException("Compte non trouvé avec ID: " + id, Response.Status.NOT_FOUND);
            }
        } catch (WebApplicationException e) {
            throw e;
        } catch (Exception e) {
            log.error("Erreur lors de la récupération du compte ID: {}", id, e);
            throw new WebApplicationException("Erreur interne du serveur", Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Crée un nouveau compte
     */
    @Path("/comptes")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Compte addCompte(Compte compte) {
        try {
            if (compte == null) {
                throw new WebApplicationException("Données du compte manquantes", Response.Status.BAD_REQUEST);
            }

            // S'assurer que l'ID est null pour la création
            compte.setId(null);
            Compte savedCompte = compteRepository.save(compte);
            log.info("Compte créé avec ID: {}", savedCompte.getId());
            return savedCompte;
        } catch (Exception e) {
            log.error("Erreur lors de la création du compte", e);
            throw new WebApplicationException("Erreur lors de la création du compte", Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Met à jour un compte existant
     */
    @Path("/comptes/{id}")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Compte updateCompte(@PathParam("id") Long id, Compte compte) {
        try {
            Optional<Compte> existing = compteRepository.findById(id);
            if (existing.isPresent()) {
                Compte toUpdate = existing.get();
                toUpdate.setSolde(compte.getSolde());
                toUpdate.setDateCreation(compte.getDateCreation());
                toUpdate.setType(compte.getType());

                Compte updated = compteRepository.save(toUpdate);
                log.info("Compte mis à jour avec ID: {}", id);
                return updated;
            } else {
                log.warn("Tentative de mise à jour d'un compte inexistant ID: {}", id);
                throw new WebApplicationException("Compte non trouvé avec ID: " + id, Response.Status.NOT_FOUND);
            }
        } catch (WebApplicationException e) {
            throw e;
        } catch (Exception e) {
            log.error("Erreur lors de la mise à jour du compte ID: {}", id, e);
            throw new WebApplicationException("Erreur lors de la mise à jour du compte", Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Supprime un compte
     */
    @Path("/comptes/{id}")
    @DELETE
    public Response deleteCompte(@PathParam("id") Long id) {
        try {
            if (compteRepository.existsById(id)) {
                compteRepository.deleteById(id);
                log.info("Compte supprimé avec ID: {}", id);
                return Response.noContent().build();
            } else {
                log.warn("Tentative de suppression d'un compte inexistant ID: {}", id);
                throw new WebApplicationException("Compte non trouvé avec ID: " + id, Response.Status.NOT_FOUND);
            }
        } catch (WebApplicationException e) {
            throw e;
        } catch (Exception e) {
            log.error("Erreur lors de la suppression du compte ID: {}", id, e);
            throw new WebApplicationException("Erreur lors de la suppression du compte", Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Recherche les comptes par type (COURANT ou EPARGNE)
     */
    @Path("/comptes/type/{type}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Compte> getComptesByType(@PathParam("type") String type) {
        try {
            TypeCompte typeCompte = TypeCompte.valueOf(type.toUpperCase());
            List<Compte> comptes = compteRepository.findByType(typeCompte);
            log.info("Récupération de {} comptes de type: {}", comptes.size(), type);
            return comptes;
        } catch (IllegalArgumentException e) {
            log.warn("Type de compte invalide: {}", type);
            throw new WebApplicationException(
                    "Type de compte invalide: " + type + ". Types valides: COURANT, EPARGNE",
                    Response.Status.BAD_REQUEST
            );
        } catch (Exception e) {
            log.error("Erreur lors de la recherche par type: {}", type, e);
            throw new WebApplicationException("Erreur interne du serveur", Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Recherche les comptes avec un solde minimum
     */
    @Path("/comptes/solde-min/{soldeMin}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Compte> getComptesBySoldeMin(@PathParam("soldeMin") double soldeMin) {
        try {
            List<Compte> comptes = compteRepository.findBySoldeGreaterThan(soldeMin);
            log.info("Récupération de {} comptes avec solde > {}", comptes.size(), soldeMin);
            return comptes;
        } catch (Exception e) {
            log.error("Erreur lors de la recherche par solde minimum: {}", soldeMin, e);
            throw new WebApplicationException("Erreur interne du serveur", Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Statistiques des comptes
     */
    @Path("/statistiques")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getStatistiques() {
        try {
            long totalComptes = compteRepository.count();
            long comptesCourant = compteRepository.findByType(TypeCompte.COURANT).size();
            long comptesEpargne = compteRepository.findByType(TypeCompte.EPARGNE).size();

            String stats = String.format(
                    "{\"totalComptes\": %d, \"comptesCourant\": %d, \"comptesEpargne\": %d}",
                    totalComptes, comptesCourant, comptesEpargne
            );

            log.info("Statistiques générées: {}", stats);
            return stats;
        } catch (Exception e) {
            log.error("Erreur lors de la génération des statistiques", e);
            throw new WebApplicationException("Erreur lors de la génération des statistiques", Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Vérifie si un compte existe
     */
    @Path("/comptes/{id}/exists")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String checkCompteExists(@PathParam("id") Long id) {
        boolean exists = compteRepository.existsById(id);
        log.info("Vérification existence compte ID {}: {}", id, exists);
        return String.format("{\"id\": %d, \"exists\": %s}", id, exists);
    }
}