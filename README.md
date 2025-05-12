# Gestion MatchupZ

Guide détaillé pour rédiger un bon README sur GitHub pour notre projet MatchupZ

# Nom du Projet

![Alt Text](umldiagram.png)

## Dev Push

---

## 1. Description du Projet

**MatchupZ** est une application Java conçue pour la gestion d'un club sportif. Développée avec JavaFX, elle propose des fonctionnalités telles que :

- **Son objectif** : Fournir une plateforme complète pour la gestion des clubs sportifs, des joueurs et des installations.
- **Le problème qu'il résout** : Simplifier la gestion administrative et sportive des clubs.
- **Ses principales fonctionnalités** :
  - Gestion des clubs et des joueurs
  - Suivi des performances et évaluations
  - Gestion des espaces sportifs et réservations
  - Système d'abonnement
  - Interface utilisateur moderne avec JavaFX
  - Analyses statistiques et rapports

---

## 2. Entités du Projet

Retrouvez ci-dessous la liste des entités principales du projet, chacune pointant vers son fichier source :

### Gestion des Utilisateurs 
- [User](User.java) - [Service](UserService.java)
- [Role](Role.java)
- [SessionManager](SessionManager.java)

### Gestion des Clubs et Joueurs
- [Club](Club.java) - [Service](ClubService.java)
- [Joueur](Joueur.java) - [Service](JoueurService.java)
- [HistoriqueClub](HistoriqueClub.java) - [Service](HistoriqueClubService.java)
- [PerformanceJoueur](PerformanceJoueur.java) - [Service](PerformanceJoueurService.java)
- [EvaluationPhysique](EvaluationPhysique.java) - [Service](EvaluationPhysiqueService.java)

### Gestion des Espaces Sportifs
- [EspaceSportif](EspaceSportif.java) - [Service](EspaceSportifService.java)
- [Materiel](Materiel.java) - [Service](MaterielService.java)
- [Reservation](Reservation.java) - [Service](ReservationService.java)

### Gestion des Matches 
- [Match](Match.java) - [Service](MatchService.java)
- [Sport](Sport.java) - [Service](SportService.java)
- [StatistiquesPostMatch](StatistiquesPostMatch.java) - [Service](StatistiquesPostMatchService.java)
- [Schedule](Schedule.java) - [Service](ScheduleService.java)

### Gestion Sponsoring
- [Abonnement](Abonnement.java) - [Service](AbonnementService.java)
- [Contrat](Contrat.java) - [Service](ContratService.java)
- [Sponsor](Sponsor.java) - [Service](SponsorService.java)
- [Transaction](Transaction.java) - [Service](TransactionService.java)

### Gestion Logistique
 - [Fournisseur](Fournisseur.java) - [Service](FournisseurService.java)

## 3. Table des Matières

- [Description du Projet](#description-du-projet)
- [Entités du Projet](#entités-du-projet)
- [Installation](#installation)
- [Utilisation](#utilisation)
- [Contribution](#contribution)
- [Licence](#licence)

---

## 4. Installation

Pour installer et exécuter le projet localement :

1. **Cloner le dépôt :**
   ```bash
   git clone https://github.com/your-username/dev-push.git
   cd dev-push
   ```

2. **Installer les dépendances avec Maven :**
   ```bash
   mvn clean install
   ```

3. **Compiler le projet :**
   ```bash
   mvn compile
   ```

4. **Exécuter l'application :**
   ```bash
   mvn exec:java
   ```

L'application sera accessible via son interface JavaFX.

---

## 5. Utilisation

- L'application offre une interface utilisateur JavaFX intuitive avec plusieurs vues :
  - [Interface Administrateur](adminpage.fxml)
  - [Gestion des Clubs](AjoutClub.fxml)
  - [Gestion des Joueurs](AjoutJoueur.fxml)
  - [Statistiques des Joueurs](PlayerStats.fxml)
  - [Gestion des Sports](AjoutSport.fxml)

- Utilisez les commandes Maven pour gérer l'application :

  ```bash
  # Lancer les tests
  mvn test

  # Créer un package
  mvn package

  # Nettoyer le projet
  mvn clean
  ```

---

## 6. Contribution

Nous remercions tous ceux qui ont contribué à ce projet !

### Comment contribuer ?

1. Forkez le dépôt.
2. Créez une nouvelle branche pour votre fonctionnalité ou correction de bug.
3. Commitez vos modifications et poussez-les sur votre fork.
4. Soumettez une pull request avec une description détaillée de vos changements.

---

## 7. Licence

Ce projet est sous licence **MIT**.  
Pour plus de détails, consultez le fichier [LICENSE](./LICENSE).

---

## 8. Ressources

- [Diagramme UML](umldiagram.png)
- [Cas d'utilisation](usecase.png)
- [Style CSS Principal](dashboardDesign.css)

---

## 9. Contact

Pour toute question ou suggestion, n'hésitez pas à ouvrir une issue sur GitHub.
