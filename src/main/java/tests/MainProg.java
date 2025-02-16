package tests;

import models.EspaceSportif.Abonnement;
import services.EspaceSportif.AbonnementService;
import services.EspaceSportif.EspaceSportifService;
import services.EspaceSportif.ReservationService;
import utils.MyDatabase;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;

public class MainProg {
    public static void main(String[] args) {
        Connection connection = MyDatabase.getInstance().getConnection();
        AbonnementService as = new AbonnementService(connection);
        EspaceSportifService ess = new EspaceSportifService();
        ReservationService rs = new ReservationService();

        try {
            /*
            System.out.println("\n=== AJOUT D'ESPACES SPORTIFS ===");
            EspaceSportif espaceSportif1 = new EspaceSportif("Stade Municipal", "123 Rue du Sport, Paris", "terrain foot", 500);
            EspaceSportif espaceSportif2 = new EspaceSportif("Stade Club", "123 Rue du Kaki, Tunis", "terrain foot", 1200);
            ess.ajouter(espaceSportif1);
            ess.ajouter(espaceSportif2);
            System.out.println("\n=== LISTE DES ESPACES SPORTIFS APRES AJOUT ===");
            ess.rechercher();

            System.out.println("\n=== MODIFICATION D'UN ESPACE SPORTIF ===");
            EspaceSportif espaceModifie = new EspaceSportif(15, "Arena Olympique", "789 Boulevard du Sport, Lyon", "salle gym", 800);
            ess.modifier(espaceModifie);
            System.out.println("\n=== LISTE DES ESPACES SPORTIFS APRES MODIFICATION ===");
            ess.rechercher();

            System.out.println("\n=== SUPPRESSION D'UN ESPACE SPORTIF ===");
            ess.supprimer(espaceModifie);
            System.out.println("\n=== LISTE DES ESPACES SPORTIFS APRES SUPPRESSION ===");
            ess.rechercher();
*/
            /*
            System.out.println("\n=== AJOUT D'UNE RÉSERVATION ===");
            rs.ajouter(new Reservation(18, Timestamp.valueOf("2025-02-15 14:30:00"), "match", "confirmée"));

            System.out.println("\n=== LISTE DES RÉSERVATIONS ===");
            rs.rechercher().forEach(System.out::println);

            System.out.println("\n=== MODIFICATION D'UNE RÉSERVATION ===");
            rs.modifier(new Reservation(2, 18, Timestamp.valueOf("2025-02-16 15:00:00"), "tournoi", "en attente"));

            System.out.println("\n=== SUPPRESSION D'UNE RÉSERVATION ===");
            rs.supprimer(new Reservation(1, 0, null, "", ""));

            System.out.println("\n=== LISTE DES RÉSERVATIONS APRES SUPPRESSION ===");
            rs.rechercher().forEach(System.out::println);

*/

            System.out.println("\n=== AJOUT D'ABONNEMENTS ===");
            Abonnement abonnement1 = new Abonnement(1, "Mensuel", new Date(), new Date(System.currentTimeMillis() + 2592000000L), 50.0, "Actif");
            Abonnement abonnement2 = new Abonnement(2, "Annuel", new Date(), new Date(System.currentTimeMillis() + 31536000000L), 500.0, "Actif");
            as.ajouter(abonnement1);
            as.ajouter(abonnement2);

            System.out.println("\n=== LISTE DES ABONNEMENTS APRES AJOUT ===");
            as.lister().forEach(System.out::println);

            System.out.println("\n=== MODIFICATION D'UN ABONNEMENT ===");
            abonnement1.setTypeAbonnement("Trimestriel");
            abonnement1.setTarif(120.0);
            as.modifier(abonnement1);

            System.out.println("\n=== LISTE DES ABONNEMENTS APRES MODIFICATION ===");
            as.lister().forEach(System.out::println);

            System.out.println("\n=== SUPPRESSION D'UN ABONNEMENT ===");
            as.supprimer(abonnement1.getIdAbonnement());

            System.out.println("\n=== LISTE DES ABONNEMENTS APRES SUPPRESSION ===");
            as.lister().forEach(System.out::println);
        } catch (SQLException e) {
            System.out.println("Erreur SQL : " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Erreur : " + e.getMessage());
        }
    }
}
