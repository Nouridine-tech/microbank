package isi.nour.microbank.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDateTime;

@MappedSuperclass
@Getter
@Setter
public class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected int id;

    @CreationTimestamp
    @Column(updatable = false, name = "created_at", nullable = false)
    protected LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    protected LocalDateTime updatedAt;

    @Column(name = "user_created", nullable = false, updatable = false, length = 100)
    protected String userCreated;

    @Column(name = "user_updated", nullable = false, length = 100)
    protected String userUpdated;

    // Alimenté automatiquement avant chaque insertion en base
    @PrePersist
    public void prePersist() {
        this.userCreated = "system";
        this.userUpdated = "system";
    }

    // Alimenté automatiquement avant chaque mise à jour en base
    @PreUpdate
    public void preUpdate() {
        this.userUpdated = "system";
    }
}