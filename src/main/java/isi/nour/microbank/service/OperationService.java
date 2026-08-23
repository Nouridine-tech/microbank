package isi.nour.microbank.service;

import isi.nour.microbank.config.JpaUtil;
import isi.nour.microbank.model.Account;
import isi.nour.microbank.model.Operation;
import isi.nour.microbank.model.User;
import isi.nour.microbank.dao.AccountDAO;
import isi.nour.microbank.dao.OperationDAO;
import isi.nour.microbank.utils.Tools;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class OperationService {

    private final AccountDAO accountDAO = new AccountDAO();
    private final OperationDAO operationDAO = new OperationDAO();

    // ----- DÉPÔT -----
    // Ajoute un montant sur un compte dans une seule transaction
    public void depot(int accountId, BigDecimal montant,
                      String description, User agent) {

        // Vérification des règles métier AVANT d'ouvrir la transaction
        if (montant == null || montant.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Le montant doit être supérieur à 0.");
        }

        EntityManager em = JpaUtil.getEntityManager();
        try {
            em.getTransaction().begin();

            Account account = em.find(Account.class, accountId);

            // Vérification que le compte existe
            if (account == null) {
                throw new IllegalArgumentException("Compte introuvable.");
            }

            // Vérification que le client du compte est actif
            if (!"ACTIF".equals(account.getClient().getStatut())) {
                throw new IllegalArgumentException("Le client de ce compte est inactif.");
            }

            // Vérification que le compte est actif
            if (!"ACTIF".equals(account.getStatut())) {
                throw new IllegalArgumentException("Le compte n'est pas actif.");
            }

            // Mise à jour du solde
            BigDecimal nouveauSolde = account.getSolde().add(montant);
            account.setSolde(nouveauSolde);

            // Enregistrement de l'opération
            Operation operation = new Operation();
            operation.setReference(Tools.generateOperationReference());
            operation.setType("DEPOT");
            operation.setMontant(montant);
            operation.setDateOperation(LocalDateTime.now());
            operation.setDescription(description);
            operation.setSoldeApres(nouveauSolde);
            operation.setAccount(account);
            operation.setUser(em.find(User.class, agent.getId()));

            // Les deux actions dans la même transaction
            operationDAO.save(em, operation);

            em.getTransaction().commit();

        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    // ----- RETRAIT -----
    // Retire un montant d'un compte dans une seule transaction
    public void retrait(int accountId, BigDecimal montant,
                        String description, User agent) {

        if (montant == null || montant.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Le montant doit être supérieur à 0.");
        }

        EntityManager em = JpaUtil.getEntityManager();
        try {
            em.getTransaction().begin();

            Account account = em.find(Account.class, accountId);

            // Vérification que le compte existe
            if (account == null) {
                throw new IllegalArgumentException("Compte introuvable.");
            }

            // Vérification que le client du compte est actif
            if (!"ACTIF".equals(account.getClient().getStatut())) {
                throw new IllegalArgumentException("Le client de ce compte est inactif.");
            }

            // Vérification que le compte est actif
            if (!"ACTIF".equals(account.getStatut())) {
                throw new IllegalArgumentException("Le compte n'est pas actif.");
            }

            // Vérification que le solde du compte est suffisant
            if (account.getSolde().compareTo(montant) < 0) {
                throw new IllegalArgumentException("Solde insuffisant.");
            }

            BigDecimal nouveauSolde = account.getSolde().subtract(montant);
            account.setSolde(nouveauSolde);

            Operation operation = new Operation();
            operation.setReference(Tools.generateOperationReference());
            operation.setType("RETRAIT");
            operation.setMontant(montant);
            operation.setDateOperation(LocalDateTime.now());
            operation.setDescription(description);
            operation.setSoldeApres(nouveauSolde);
            operation.setAccount(account);
            operation.setUser(em.find(User.class, agent.getId()));

            operationDAO.save(em, operation);

            em.getTransaction().commit();

        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    // ----- VIREMENT -----
    // Transfère un montant d'un compte vers un autre dans une seule transaction.
    // Les 3 actions (débit source + crédit destination + enregistrement)
    // sont atomiques : si l'une échoue, tout est annulé.
    public void virement(int accountSourceId, int accountDestId,
                         BigDecimal montant, String description, User agent) {

        // Vérification que le montant n'est pas null
        if (montant == null || montant.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Le montant doit être supérieur à 0.");
        }

        // Vérification que les deux comptes sont differents
        if (accountSourceId == accountDestId) {
            throw new IllegalArgumentException("Les comptes source et destination doivent être différents.");
        }

        EntityManager em = JpaUtil.getEntityManager();
        try {
            em.getTransaction().begin();

            Account source = em.find(Account.class, accountSourceId);
            Account dest   = em.find(Account.class, accountDestId);

            // Vérification que les deux comptes existent
            if (source == null || dest == null) {
                throw new IllegalArgumentException("Un des comptes est introuvable.");
            }

            // Vérification que les clients des deux comptes sont actifs
            if (!"ACTIF".equals(source.getClient().getStatut())) {
                throw new IllegalArgumentException("Le client du compte source est inactif.");
            }

            if (!"ACTIF".equals(dest.getClient().getStatut())) {
                throw new IllegalArgumentException("Le client du compte destination est inactif.");
            }

            // Vérification que les deux comptes sont actifs
            if (!"ACTIF".equals(source.getStatut()) || !"ACTIF".equals(dest.getStatut())) {
                throw new IllegalArgumentException("Les deux comptes doivent être actifs.");
            }

            User userRef = em.find(User.class, agent.getId());

            // Débit du compte source
            BigDecimal soldeSource = source.getSolde().subtract(montant);
            source.setSolde(soldeSource);
            Operation opSource = new Operation();
            opSource.setReference(Tools.generateOperationReference());
            opSource.setType("VIREMENT");
            opSource.setMontant(montant);
            opSource.setDateOperation(LocalDateTime.now());
            opSource.setDescription("Virement vers " + dest.getNumeroCompte() + " — " + description);
            opSource.setSoldeApres(soldeSource);
            opSource.setAccount(source);
            opSource.setUser(userRef);

            // Crédit du compte destination
            BigDecimal soldeDest = dest.getSolde().add(montant);
            dest.setSolde(soldeDest);
            Operation opDest = new Operation();
            opDest.setReference(Tools.generateOperationReference());
            opDest.setType("VIREMENT");
            opDest.setMontant(montant);
            opDest.setDateOperation(LocalDateTime.now());
            opDest.setDescription("Virement depuis " + source.getNumeroCompte() + " — " + description);
            opDest.setSoldeApres(soldeDest);
            opDest.setAccount(dest);
            opDest.setUser(userRef);

            // Les 4 actions dans la même transaction
            operationDAO.save(em, opSource);
            operationDAO.save(em, opDest);

            em.getTransaction().commit();

        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }
}