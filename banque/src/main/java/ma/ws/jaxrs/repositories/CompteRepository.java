package ma.ws.jaxrs.repositories;

import ma.ws.jaxrs.entities.Compte;
import ma.ws.jaxrs.model.TypeCompte;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompteRepository extends JpaRepository<Compte, Long> {

    // Trouve les comptes par type
    List<Compte> findByType(TypeCompte type);

    // Trouve les comptes avec un solde supérieur à une valeur donnée
    @Query("SELECT c FROM Compte c WHERE c.solde > :soldeMin")
    List<Compte> findBySoldeGreaterThan(@Param("soldeMin") double soldeMin);
}