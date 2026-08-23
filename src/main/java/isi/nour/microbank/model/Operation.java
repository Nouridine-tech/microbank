package isi.nour.microbank.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "operations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"account", "user"})
public class Operation extends BaseEntity {

    // Généré automatiquement via Tools.generateOperationReference()
    @Column(nullable = false, unique = true, length = 30)
    private String reference;

    // Type : "DEPOT", "RETRAIT", "VIREMENT"
    @Column(nullable = false, length = 20)
    private String type;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal montant;

    @Column(name = "date_operation")
    private LocalDateTime dateOperation;

    @Column(length = 255)
    private String description;

    // Solde du compte après cette opération — utile pour l'historique
    @Column(name = "solde_apres", precision = 15, scale = 2)
    private BigDecimal soldeApres;

    // Le compte sur lequel l'opération a été effectuée
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    // L'agent qui a effectué l'opération
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

}