package isi.nour.microbank.dao;

import isi.nour.microbank.config.JpaUtil;
import isi.nour.microbank.model.Operation;
import jakarta.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;

public class OperationDAO {

    public Operation findById(int id) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            return em.find(Operation.class, id);
        } finally {
            em.close();
        }
    }

    // Récupère toutes les opérations d'un compte — utilisé pour l'historique
    public List<Operation> findByAccount(int accountId) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            return em.createQuery(
                            "SELECT o FROM Operation o WHERE o.account.id = :accountId " +
                                    "ORDER BY o.dateOperation DESC", Operation.class)
                    .setParameter("accountId", accountId)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    // Compte le total des opérations d'un compte — utilisé pour la pagination
    public long countByAccount(int accountId, String type,
                               LocalDateTime debut, LocalDateTime fin) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            StringBuilder jpql = new StringBuilder(
                    "SELECT COUNT(o) FROM Operation o WHERE o.account.id = :accountId"
            );
            if (type != null && !type.isEmpty()) {
                jpql.append(" AND o.type = :type");
            }
            if (debut != null) {
                jpql.append(" AND o.dateOperation >= :debut");
            }
            if (fin != null) {
                jpql.append(" AND o.dateOperation <= :fin");
            }
            var query = em.createQuery(jpql.toString(), Long.class)
                    .setParameter("accountId", accountId);
            if (type != null && !type.isEmpty()) {
                query.setParameter("type", type);
            }
            if (debut != null) {
                query.setParameter("debut", debut);
            }
            if (fin != null) {
                query.setParameter("fin", fin);
            }
            return query.getSingleResult();
        } finally {
            em.close();
        }
    }

    // Recherche paginée avec filtres — utilisé pour l'historique
    public List<Operation> findByAccountPaged(int accountId, String type,
                                              LocalDateTime debut, LocalDateTime fin,
                                              int pageSize, int offset) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            StringBuilder jpql = new StringBuilder(
                    "SELECT o FROM Operation o WHERE o.account.id = :accountId"
            );
            if (type != null && !type.isEmpty()) {
                jpql.append(" AND o.type = :type");
            }
            if (debut != null) {
                jpql.append(" AND o.dateOperation >= :debut");
            }
            if (fin != null) {
                jpql.append(" AND o.dateOperation <= :fin");
            }
            jpql.append(" ORDER BY o.dateOperation DESC");

            var query = em.createQuery(jpql.toString(), Operation.class)
                    .setParameter("accountId", accountId)
                    .setFirstResult(offset)
                    .setMaxResults(pageSize);
            if (type != null && !type.isEmpty()) {
                query.setParameter("type", type);
            }
            if (debut != null) {
                query.setParameter("debut", debut);
            }
            if (fin != null) {
                query.setParameter("fin", fin);
            }
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    // Récupère les opérations du jour — utilisé pour le dashboard
    public long countToday() {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            LocalDateTime debutJour = LocalDateTime.now().toLocalDate().atStartOfDay();
            LocalDateTime finJour   = debutJour.plusDays(1);
            return em.createQuery(
                            "SELECT COUNT(o) FROM Operation o WHERE " +
                                    "o.dateOperation >= :debut AND o.dateOperation < :fin", Long.class)
                    .setParameter("debut", debutJour)
                    .setParameter("fin",   finJour)
                    .getSingleResult();
        } finally {
            em.close();
        }
    }

    // Sauvegarde une opération — appelé uniquement depuis OperationService dans une transaction existante
    public void save(EntityManager em, Operation operation) {
        em.persist(operation);
    }
}