package isi.nour.microbank.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User extends BaseEntity {

    @Column(nullable = false, unique = true, length = 50)
    private String login;

    // Stocké haché en SHA-256 via Tools.hashPassword()
    @Column(nullable = false)
    private String password;

    @Column(nullable = false, length = 100)
    private String nom;

    @Column(nullable = false, length = 100)
    private String prenom;

    // Rôle : "AGENT" ou "ADMIN"
    @Column(nullable = false, length = 20)
    private String role;

    @Column(nullable = false)
    private boolean actif = true;
}