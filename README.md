#  API Bancaire JAX-RS/Jersey

##  Description
Microservice bancaire RESTful développé avec **Spring Boot 3.2.0** et **JAX-RS/Jersey**. Cette API permet la gestion complète de comptes bancaires avec persistance des données en base H2.

##  Technologies Utilisées

- **Java 17**
- **Spring Boot 3.2.0**
- **JAX-RS/Jersey** (Implémentation REST)
- **Spring Data JPA** (Persistence)
- **H2 Database** (Base en mémoire)
- **Lombok** (Réduction du code boilerplate)
- **Maven** (Gestion des dépendances)

##  Structure du Projet

```
banque/
├── src/
│   └── main/
│       └── java/
│           └── ma/
│               └── ws/
│                   └── jaxrs/
│                       ├── JaxrsApplication.java          # Classe principale
│                       ├── config/
│                       │   └── MyConfig.java              # Configuration Jersey
│                       ├── controllers/
│                       │   └── CompteRestJaxRSAPI.java    # Contrôleur JAX-RS
│                       ├── entities/
│                       │   └── Compte.java                # Entité JPA
│                       ├── model/
│                       │   └── TypeCompte.java            # Enumération
│                       └── repositories/
│                           └── CompteRepository.java      # Repository Spring Data
└── src/main/resources/
    └── application.properties                             # Configuration
```

##  Installation et Démarrage

### Prérequis
- Java 17 ou supérieur
- Maven 3.6+

### Compilation et Lancement
```bash
# Cloner le projet
git clone [url-du-projet]
cd banque

# Compiler
mvn clean compile

# Lancer l'application
mvn spring-boot:run
```

### Accès
- **API REST** : http://localhost:8083/api/banque
- **Console H2** : http://localhost:8083/h2-console
- **Port** : 8083 (configurable dans `application.properties`)

##  Endpoints de l'API

### 🔍 Lecture
| Méthode | Endpoint | Description |
|---------|----------|-------------|
| `GET` | `/banque/health` | Statut de l'API |
| `GET` | `/banque/comptes` | Liste tous les comptes |
| `GET` | `/banque/comptes/{id}` | Récupère un compte par ID |
| `GET` | `/banque/comptes/type/{type}` | Filtre par type (COURANT/EPARGNE) |
| `GET` | `/banque/comptes/solde-min/{min}` | Filtre par solde minimum |
| `GET` | `/banque/comptes/{id}/exists` | Vérifie l'existence d'un compte |
| `GET` | `/banque/statistiques` | Statistiques des comptes |

###  Écriture
| Méthode | Endpoint | Description |
|---------|----------|-------------|
| `POST` | `/banque/comptes` | Crée un nouveau compte |
| `PUT` | `/banque/comptes/{id}` | Met à jour un compte existant |
| `DELETE` | `/banque/comptes/{id}` | Supprime un compte |

## Modèle de Données

### Entité Compte
```java
@Entity
@Table(name = "COMPTES")
public class Compte {
    private Long id;
    private double solde;
    private Date dateCreation;
    private TypeCompte type; // COURANT ou EPARGNE
}
```

### Enumération TypeCompte
```java
public enum TypeCompte {
    COURANT,    // Compte courant
    EPARGNE     // Compte épargne
}
```

##  Exemples d'Utilisation

### Récupérer tous les comptes
```bash
curl -X GET "http://localhost:8083/api/banque/comptes" -H "Accept: application/json"
```

### Créer un nouveau compte
```bash
curl -X POST "http://localhost:8083/api/banque/comptes" \
  -H "Content-Type: application/json" \
  -d '{
    "solde": 2500.0,
    "dateCreation": "2024-01-07",
    "type": "COURANT"
  }'
```

### Récupérer les comptes épargne
```bash
curl -X GET "http://localhost:8083/api/banque/comptes/type/EPARGNE"
```

### Statistiques
```bash
curl -X GET "http://localhost:8083/api/banque/statistiques"
```

## Configuration

### Fichier application.properties
```properties
server.port=8083
spring.datasource.url=jdbc:h2:mem:banquedb
spring.jpa.hibernate.ddl-auto=create-drop
spring.h2.console.enabled=true
spring.jersey.application-path=/api
```

### Configuration Jersey
```java
@Configuration
public class MyConfig {
    @Bean
    public ResourceConfig resourceConfig() {
        ResourceConfig jerseyServlet = new ResourceConfig();
        jerseyServlet.register(CompteRestJaxRSAPI.class);
        return jerseyServlet;
    }
}
```

##  Base de Données H2

### Connexion à la console H2
- **URL** : http://localhost:8083/h2-console
- **JDBC URL** : `jdbc:h2:mem:banquedb`
- **Username** : `sa`
- **Password** : _(vide)_

### Requête SQL exemple
```sql
SELECT * FROM COMPTES;
```

## 🎯 Fonctionnalités Implémentées

-  **CRUD Complet** sur les comptes bancaires
-  **Filtrage avancé** (par type, par solde)
-  **Gestion d'erreurs** HTTP appropriées
-  **Validation** des données
-  **Logging** complet
-  **Statistiques** en temps réel
-  **Base de données** H2 intégrée
-  **API RESTful** conforme aux standards

## Tests 



https://github.com/user-attachments/assets/23e9a11a-192f-4862-90cf-a15e36f29fa8



https://github.com/user-attachments/assets/22306c80-75cd-4554-9687-148f8347bd84



<img width="946" height="406" alt="en3" src="https://github.com/user-attachments/assets/ea6edea9-a76d-43ba-8f28-8fd8eff41655" />



<img width="959" height="472" alt="en2" src="https://github.com/user-attachments/assets/08857868-d67f-4762-8e77-2dcdce95117d" />
