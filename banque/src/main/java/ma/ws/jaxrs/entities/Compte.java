package ma.ws.jaxrs.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.ws.jaxrs.model.TypeCompte;

import java.util.Date;

@Entity
@Table(name = "COMPTES")
@Data
@NoArgsConstructor
@AllArgsConstructor
@JacksonXmlRootElement(localName = "compte")
public class Compte {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "SOLDE", nullable = false)
    private double solde;

    @Temporal(TemporalType.DATE)
    @Column(name = "DATE_CREATION", nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date dateCreation;

    @Enumerated(EnumType.STRING)
    @Column(name = "TYPE_COMPTE", nullable = false, length = 10)
    private TypeCompte type;

    // Constructeur sans l'ID pour faciliter la création
    public Compte(double solde, Date dateCreation, TypeCompte type) {
        this.solde = solde;
        this.dateCreation = dateCreation;
        this.type = type;
    }

    @Override
    public String toString() {
        return String.format("Compte{id=%d, solde=%.2f, dateCreation=%s, type=%s}",
                id, solde, dateCreation, type);
    }
}