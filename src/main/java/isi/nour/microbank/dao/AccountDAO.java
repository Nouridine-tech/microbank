package isi.nour.microbank.dao;

import isi.nour.microbank.config.JpaUtil;
import isi.nour.microbank.model.Account;
import jakarta.persistence.EntityManager;
import java.util.List;

public class AccountDAO {

    // JOIN FETCH charge le client en même temps que le compte — évite la LazyInitializationException
    public List<Account> findAll() {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            return em.createQuery(
                            "SELECT a FROM Account a JOIN FETCH a.client ORDER BY a.numeroCompte",
                            Account.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    // JOIN FETCH charge le client avec le compte
    public Account findById(int id) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            return em.createQuery(
                            "SELECT a FROM Account a JOIN FETCH a.client WHERE a.id = :id",
                            Account.class)
                    .setParameter("id", id)
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        } finally {
            em.close();
        }
    }

    // Récupère tous les comptes d'un client
    public List<Account> findByClient(int clientId) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            return em.createQuery(
                            "SELECT a FROM Account a WHERE a.client.id = :clientId ORDER BY a.numeroCompte",
                            Account.class)
                    .setParameter("clientId", clientId)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    // Vérifie qu'un numéro de compte n'existe pas déjà en base
    public boolean existsByNumeroCompte(String numeroCompte) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            Long count = em.createQuery(
                            "SELECT COUNT(a) FROM Account a WHERE a.numeroCompte = :num",
                            Long.class)
                    .setParameter("num", numeroCompte)
                    .getSingleResult();
            return count > 0;
        } finally {
            em.close();
        }
    }

    // Compte le nombre total de comptes
    public long countAll() {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            return em.createQuery(
                            "SELECT COUNT(a) FROM Account a", Long.class)
                    .getSingleResult();
        } finally {
            em.close();
        }
    }

    // Calcule le solde total de tous les comptes actifs
    public java.math.BigDecimal sumSoldes() {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            java.math.BigDecimal result = em.createQuery(
                            "SELECT SUM(a.solde) FROM Account a WHERE a.statut = 'ACTIF'",
                            java.math.BigDecimal.class)
                    .getSingleResult();
            return result != null ? result : java.math.BigDecimal.ZERO;
        } finally {
            em.close();
        }
    }

    // Charge les comptes avec leurs clients — utilisé pour le dropdown du virement
    public List<Account> findAllWithClient() {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            return em.createQuery(
                            "SELECT a FROM Account a JOIN FETCH a.client ORDER BY a.numeroCompte",
                            Account.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public void save(Account account) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            account.setDateOuverture(java.time.LocalDateTime.now());
            em.persist(account);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    public void update(Account account) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(account);
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
            Account account = em.find(Account.class, id);
            if (account != null) {
                em.remove(account);
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