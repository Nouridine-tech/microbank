package isi.nour.microbank.dao;

import isi.nour.microbank.config.JpaUtil;
import isi.nour.microbank.model.Client;
import jakarta.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;

public class ClientDAO {

    public List<Client> findAll() {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            return em.createQuery(
                            "SELECT c FROM Client c ORDER BY c.nom", Client.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public Client findById(int id) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            return em.find(Client.class, id);
        } finally {
            em.close();
        }
    }

    // Recherche par nom, prénom, téléphone ou numéro de pièce
    public List<Client> search(String keyword) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            String kw = "%" + keyword.toLowerCase() + "%";
            return em.createQuery(
                            "SELECT c FROM Client c WHERE " +
                                    "LOWER(c.nom) LIKE :kw OR " +
                                    "LOWER(c.prenom) LIKE :kw OR " +
                                    "LOWER(c.telephone) LIKE :kw OR " +
                                    "LOWER(c.numeroPiece) LIKE :kw " +
                                    "ORDER BY c.nom", Client.class)
                    .setParameter("kw", kw)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    // Compte le total pour la pagination
    public long count(String keyword) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            if (keyword != null && !keyword.trim().isEmpty()) {
                String kw = "%" + keyword.toLowerCase() + "%";
                return em.createQuery(
                                "SELECT COUNT(c) FROM Client c WHERE " +
                                        "LOWER(c.nom) LIKE :kw OR " +
                                        "LOWER(c.prenom) LIKE :kw OR " +
                                        "LOWER(c.telephone) LIKE :kw OR " +
                                        "LOWER(c.numeroPiece) LIKE :kw", Long.class)
                        .setParameter("kw", kw)
                        .getSingleResult();
            }
            return em.createQuery(
                            "SELECT COUNT(c) FROM Client c", Long.class)
                    .getSingleResult();
        } finally {
            em.close();
        }
    }

    // Récupère une page de clients avec pagination
    public List<Client> findPaged(String keyword, int pageSize, int offset) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            if (keyword != null && !keyword.trim().isEmpty()) {
                String kw = "%" + keyword.toLowerCase() + "%";
                return em.createQuery(
                                "SELECT c FROM Client c WHERE " +
                                        "LOWER(c.nom) LIKE :kw OR " +
                                        "LOWER(c.prenom) LIKE :kw OR " +
                                        "LOWER(c.telephone) LIKE :kw OR " +
                                        "LOWER(c.numeroPiece) LIKE :kw " +
                                        "ORDER BY c.nom", Client.class)
                        .setParameter("kw", kw)
                        .setFirstResult(offset)
                        .setMaxResults(pageSize)
                        .getResultList();
            }
            return em.createQuery(
                            "SELECT c FROM Client c ORDER BY c.nom", Client.class)
                    .setFirstResult(offset)
                    .setMaxResults(pageSize)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    // Charge le client ET ses comptes dans la même session JPA — évite la LazyInitializationException
    public Client findByIdWithAccounts(int id) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            return em.createQuery(
                            "SELECT c FROM Client c LEFT JOIN FETCH c.accounts WHERE c.id = :id",
                            Client.class)
                    .setParameter("id", id)
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        } finally {
            em.close();
        }
    }

    // Vérifie si un numéro de téléphone existe déjà en base
    public boolean existsByTelephone(String telephone, int excludeId) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            Long count = em.createQuery(
                            "SELECT COUNT(c) FROM Client c WHERE c.telephone = :tel AND c.id != :excludeId",
                            Long.class)
                    .setParameter("tel", telephone)
                    .setParameter("excludeId", excludeId)
                    .getSingleResult();
            return count > 0;
        } finally {
            em.close();
        }
    }

    // Vérifie si un numéro de pièce existe déjà en base
    public boolean existsByNumeroPiece(String numeroPiece, int excludeId) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            Long count = em.createQuery(
                            "SELECT COUNT(c) FROM Client c WHERE c.numeroPiece = :num AND c.id != :excludeId",
                            Long.class)
                    .setParameter("num", numeroPiece)
                    .setParameter("excludeId", excludeId)
                    .getSingleResult();
            return count > 0;
        } finally {
            em.close();
        }
    }

    public void save(Client client) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            client.setDateCreation(LocalDateTime.now());
            em.persist(client);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    public void update(Client client) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(client);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    public void delete(int id) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            Client client = em.find(Client.class, id);
            if (client != null) {
                em.remove(client);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }
}