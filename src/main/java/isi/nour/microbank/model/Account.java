package isi.nour.microbank.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "accounts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"client", "operations"})
public class Account extends BaseEntity {

    // Généré automatiquement via Tools.generateAccountNumber()
    @Column(name = "numero_compte", nullable = false, unique = true, length = 30)
    private String numeroCompte;

    // Type : "COURANT" ou "EPARGNE"
    @Column(nullable = false, length = 20)
    private String type;

    // BigDecimal recommandé pour les montants financiers — évite les erreurs d'arrondi
    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal solde = BigDecimal.ZERO;

    // Statut : "ACTIF", "BLOQUE", "CLOTURE"
    @Column(nullable = false, length = 20)
    private String statut = "ACTIF";

    // Date d'ouverture du compte — initialisée automatiquement à la création
    @Column(name = "date_ouverture")
    private java.time.LocalDateTime dateOuverture;

    // Côté propriétaire de la relation : accounts contient la colonne client_id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    // Un compte a plusieurs opérations
    @OneToMany(mappedBy = "account", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private List<Operation> operations;
}