# MicroBank Manager

MicroBank Manager est une application web de gestion d'une institution de microfinance. Elle permet aux agents de gérer les clients, les comptes bancaires et les opérations financières (dépôts, retraits et virements).

---

## Technologies utilisées

| Technologie | Rôle |
|---|---|
| Java 21 | Langage principal |
| Jakarta Servlet 6.1 | Traitement des requêtes HTTP |
| JSP / JSTL 3.0 | Affichage des vues |
| Hibernate / JPA 7.0 | Persistance des données |
| PostgreSQL | Base de données relationnelle |
| Bootstrap 5.3.3 | Interface utilisateur responsive |
| Font Awesome 6.5.1 | Icônes |
| iText 7.2.5 | Génération des relevés PDF |
| Maven | Gestion des dépendances |
| Apache Tomcat 10.1.x | Serveur d'application |

---

## Prérequis

| Outil | Requis |
|---|---|
| JDK | Version 21 ou supérieure |
| Apache Tomcat | Version 10.1.x |
| PostgreSQL | Version 14 ou supérieure |
| Maven | Version 3.x |
| IntelliJ IDEA | Recommandé |

---

## Configuration avant lancement

### Base de données

Créer une base de données PostgreSQL nommée `microbank_db`.

Ouvrir le fichier `src/main/resources/META-INF/persistence.xml` et modifier le nom d'utilisateur et le mot de passe selon votre configuration PostgreSQL.


### Serveur

Déployer le projet sur Apache Tomcat et accéder à l'application via :

```
http://localhost:8080/microbank
```

---

## Compte administrateur

Un compte administrateur est créé automatiquement au premier démarrage.

- **Identifiant** : admin
- **Mot de passe** : admin123
- **Rôle** : ADMIN

---

## Structure du projet

```
src/main/java/isi/nour/microbank/
├── config/        (configuration JPA et connexion à la base de données)
├── model/         (entités JPA : User, Client, Account, Operation)
├── dao/           (accès à la base de données : requêtes et opérations CRUD)
├── service/       (logique métier : opérations bancaires, génération PDF et CSV)
├── controller/    (Servlets : traitement des requêtes HTTP)
├── filter/        (filtre d'authentification : protection de toutes les pages)
└── utils/         (utilitaires : génération de numéros et hachage des mots de passe)

src/main/webapp/
├── WEB-INF/views/
│   ├── tools/     (navbar partagée incluse dans toutes les pages)
│   ├── auth/      (page de connexion)
│   ├── clients/   (liste, formulaire et détail d'un client)
│   ├── accounts/  (liste, formulaire et détail d'un compte)
│   ├── operations/(dépôt, retrait, virement et historique des opérations)
│   └── users/     (gestion des utilisateurs, accessible à l'ADMIN uniquement)
└── index.jsp      (redirection automatique vers le tableau de bord)
```

---

## Fonctionnalités

### Authentification
- L'agent se connecte avec son identifiant et son mot de passe.
- La session est maintenue jusqu'à la déconnexion.
- Toutes les pages sont protégées — un utilisateur non connecté est redirigé vers le login.

### Gestion des clients
- L'agent peut ajouter, modifier et supprimer un client.
- Le numéro de téléphone et le numéro de pièce d'identité sont uniques — le système empêche les doublons.
- L'agent peut rechercher un client par nom, prénom, téléphone ou numéro de pièce.
- La liste est paginée (10 clients par page).
- La page détail affiche les informations du client ainsi que ses comptes.

### Gestion des comptes
- L'agent ouvre un compte (courant ou épargne) pour un client existant.
- Le numéro de compte est généré automatiquement.
- L'agent peut définir un dépôt initial à l'ouverture.
- L'agent peut modifier le statut d'un compte : actif, bloqué ou clôturé.

### Opérations bancaires
- **Dépôt** : l'agent crédite un montant sur un compte.
- **Retrait** : l'agent débite un montant — le système vérifie que le solde est suffisant.
- **Virement** : l'agent transfère un montant d'un compte vers un autre.
- Chaque opération vérifie que le client et le compte sont actifs avant d'exécuter.
- Un client inactif ne peut effectuer aucune transaction.
- Le dépôt, le retrait et le virement sont chacun exécutés dans une transaction atomique — en cas d'erreur, aucune modification n'est conservée.

### Historique des opérations
- L'agent consulte toutes les opérations d'un compte.
- Il peut filtrer par type (dépôt, retrait, virement) et par période.
- L'historique est paginé (10 opérations par page).
- Le solde après chaque opération est affiché.

### Relevé PDF
- L'agent génère le relevé d'un compte sur une période choisie.
- Un bouton permet de générer le relevé complet depuis la date d'ouverture du compte.
- Le PDF contient les informations du client, le numéro de compte, la liste des opérations, le total des dépôts, le total des retraits et le solde final.
- Si aucune opération n'existe sur la période, un message avertit l'agent.

### Export CSV
- L'agent exporte l'historique des opérations au format CSV sur une période choisie.
- Un bouton permet d'exporter l'historique complet depuis la date d'ouverture.
- Le fichier peut être ouvert dans Excel ou tout tableur.

### Gestion des utilisateurs (ADMIN uniquement)
- L'administrateur peut créer, modifier et supprimer des comptes utilisateurs.
- Il peut activer ou désactiver un compte.
- Un administrateur ne peut pas supprimer son propre compte.
- Le mot de passe est optionnel à la modification — s'il est laissé vide, l'ancien est conservé.

### Tableau de bord
- Le tableau de bord affiche le nombre total de clients, le nombre de comptes, le solde total de tous les comptes actifs et le nombre d'opérations effectuées dans la journée.

---

## Règles métier importantes

- Un client inactif bloque toutes les transactions sur ses comptes.
- Un compte bloqué ou clôturé ne peut pas recevoir ni envoyer d'argent.
- Le solde ne peut jamais devenir négatif.
- Un virement ne peut pas être effectué d'un compte vers lui-même.
- Le numéro de téléphone et le numéro de pièce d'un client sont uniques dans le système.