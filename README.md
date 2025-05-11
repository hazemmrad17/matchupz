# Gestion MatchUpz

![Logo](logo_matchupz.svg)

---

## 1. Description du Projet

**Gestion MatchUpz** est un système web complet destiné à la gestion logistique et fonctionnelle d'une plateforme sportive. Conçu pour simplifier les interactions entre divers acteurs (administrateurs, sponsors, fournisseurs, utilisateurs), il offre une architecture robuste et modulaire.

### Objectif

Permettre une gestion fluide et centralisée des opérations liées aux infrastructures sportives, aux réservations et à la gestion des partenaires (sponsors, fournisseurs).

### Problématique Résolue

Automatiser et structurer les processus de gestion d’un environnement sportif complexe avec des rôles différenciés et des données interconnectées.

### Fonctionnalités Clés

- Authentification et inscription utilisateur
- Gestion des rôles (Admin, Manager, Utilisateur)
- Administration des sponsors et fournisseurs
- Réservation et gestion d’espaces sportifs
- Gestion centralisée avec une structure modulaire

---

## 2. Diagrammes du Système

### Diagramme des Cas d'Utilisation

Représentation des interactions entre les différents rôles du système :

![Diagramme des Cas d'Utilisation](./usecase.png)

### Diagramme des Entités / Classes

Visualisation des entités du système et de leurs relations :

![Diagramme des Entités / Classes](./umldiagram1.png)

---

## 3. Table des Matières

- [Description du Projet](#1-description-du-projet)
- [Diagrammes du Système](#2-diagrammes-du-système)
- [Entités du Projet](#4-entités-du-projet)
- [Installation](#5-installation)
- [Utilisation](#6-utilisation)
- [Contribution](#7-contribution)
- [Licence](#8-licence)

---

## 4. Entités du Projet

Liste des entités principales gérées dans le système (les liens pointent vers les fichiers sources) :

- [User](src/Entity/User.php)
- [Sponsor](src/Entity/Sponsor.php)
- [Fournisseur](src/Entity/Fournisseur.php)
- [EspaceSportif](src/Entity/EspaceSportif.php)
- [Reservation](src/Entity/Reservation.php)
- [Contrat](src/Entity/Contrat.php)
- [Transaction](src/Entity/Transaction.php)
- [Club](src/Entity/Club.php)
- [Sport](src/Entity/Sport.php)

---

## 5. Installation

### Prérequis

- PHP ≥ 8.1
- Composer
- Symfony CLI (recommandé)
- Serveur de base de données (MySQL/PostgreSQL)
- Node.js et npm (si assets front-end)

### Étapes d’Installation

1. **Cloner le dépôt :**
   ```bash
   git clone https://github.com/yourusername/matchupz.git
   cd matchupz
   ```

2. **Installer les dépendances PHP :**
   ```bash
   composer install
   ```

3. **Configurer la base de données :**
   - Modifier le fichier `.env` avec vos paramètres de connexion :
     ```
     DATABASE_URL="mysql://user:password@127.0.0.1:3306/matchupz"
     ```

   - Créer et migrer la base :
     ```bash
     php bin/console doctrine:database:create
     php bin/console doctrine:migrations:migrate
     ```

4. **Démarrer le serveur Symfony :**
   ```bash
   symfony server:start
   ```

   Accédez à l'application à l'adresse : [http://localhost:8000](http://localhost:8000)

---

## 6. Utilisation

- Accédez aux interfaces en fonction de votre rôle (admin, manager, utilisateur).
- Utilisez l'interface de gestion pour créer, modifier ou supprimer les entités.
- Exécutez les commandes suivantes pour la maintenance :

  ```bash
  # Exécuter les tests (si configurés)
  php bin/phpunit

  # Vider le cache
  php bin/console cache:clear
  ```

---

## 7. Contribution

### Comment contribuer ?

1. **Forkez** ce dépôt.
2. Créez une **branche dédiée** à vos modifications :
   ```bash
   git checkout -b feature/ma-fonctionnalite
   ```
3. **Commitez** vos changements :
   ```bash
   git commit -m "Ajout d'une nouvelle fonctionnalité"
   ```
4. **Poussez** sur votre dépôt forké :
   ```bash
   git push origin feature/ma-fonctionnalite
   ```
5. Ouvrez une **pull request** avec une description claire de vos modifications.

---

## 8. Licence

Ce projet est sous licence **MIT**.  
Consultez le fichier [LICENSE](./LICENSE) pour plus de détails.
