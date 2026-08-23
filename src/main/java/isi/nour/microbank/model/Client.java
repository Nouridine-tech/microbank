package isi.nour.microbank.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "clients")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = "accounts")
public class Client extends BaseEntity {

    @Column(nullable = false, length = 100)
    private String nom;

    @Column(nullable = false, length = 100)
    private String prenom;

    @Column(name = "date_naissance")
    private LocalDate dateNaissance;

    @Column(nullable = false, length = 20)
    private String telephone;

    @Column(length = 150)
    private String email;

    @Column(length = 255)
    private String adresse;

    @Column(name = "numero_piece", length = 50)
    private String numeroPiece;

    // Alimenté automatiquement à la création du client
    @Column(name = "date_creation")
    private LocalDateTime dateCreation;

    // "ACTIF" ou "INACTIF"
    @Column(nullable = false, length = 20)
    private String statut = "ACTIF";

    // Un client peut avoir plusieurs comptes
    @OneToMany(mappedBy = "client", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private List<Account> accounts;
}