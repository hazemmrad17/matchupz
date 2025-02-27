package services.EspaceSportif;

import models.EspaceSportif.Abonnement;
import models.Sport; // Import the Sport model
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AbonnementService implements EspaceService<Abonnement> {
    private Connection connection;

    public AbonnementService(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void ajouter(Abonnement abonnement) {
        try {
            String sql = "INSERT INTO abonnement (id_sport, type_abonnement, date_debut, date_fin, tarif, etat) VALUES (?, ?, ?, ?, ?, ?)";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setInt(1, abonnement.getIdSport());
                stmt.setString(2, abonnement.getTypeAbonnement());
                stmt.setDate(3, new java.sql.Date(abonnement.getDateDebut().getTime()));
                stmt.setDate(4, new java.sql.Date(abonnement.getDateFin().getTime()));
                stmt.setDouble(5, abonnement.getTarif());
                stmt.setString(6, abonnement.getEtat());
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de l'ajout de l'abonnement : " + e.getMessage(), e);
        }
    }

    @Override
    public void modifier(Abonnement abonnement) {
        try {
            String sql = "UPDATE abonnement SET id_sport=?, type_abonnement=?, date_debut=?, date_fin=?, tarif=?, etat=? WHERE id_abonnement=?";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setInt(1, abonnement.getIdSport());
                stmt.setString(2, abonnement.getTypeAbonnement());
                stmt.setDate(3, new java.sql.Date(abonnement.getDateDebut().getTime()));
                stmt.setDate(4, new java.sql.Date(abonnement.getDateFin().getTime()));
                stmt.setDouble(5, abonnement.getTarif());
                stmt.setString(6, abonnement.getEtat());
                stmt.setInt(7, abonnement.getIdAbonnement());
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la modification de l'abonnement : " + e.getMessage(), e);
        }
    }

    @Override
    public void supprimer(Abonnement abonnement) {
        try {
            String sql = "DELETE FROM abonnement WHERE id_abonnement=?";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setInt(1, abonnement.getIdAbonnement());
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la suppression de l'abonnement : " + e.getMessage(), e);
        }
    }

    @Override
    public List<Abonnement> rechercher() {
        List<Abonnement> abonnements = new ArrayList<>();
        String query = "SELECT a.id_abonnement, a.id_sport, s.nom_sport AS nomSport, a.type_abonnement, a.date_debut, a.date_fin, a.tarif, a.etat " +
                "FROM abonnement a " +
                "JOIN sport s ON a.id_sport = s.id_sport"; // Changed to id_sport to match potential database schema

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Abonnement abonnement = new Abonnement(
                        rs.getInt("id_abonnement"),
                        rs.getInt("id_sport"), // Updated to match the query column name
                        rs.getString("nomSport"), // Use the alias for the sport name
                        rs.getString("type_abonnement"),
                        rs.getDate("date_debut"),
                        rs.getDate("date_fin"),
                        rs.getDouble("tarif"),
                        rs.getString("etat")
                );
                abonnements.add(abonnement);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la recherche des abonnements : " + e.getMessage(), e);
        }
        return abonnements;
    }

    // Optional: Keep the specific method if needed for other parts of your application
    public List<Abonnement> rechercherParSport(int idSport) {
        List<Abonnement> abonnements = new ArrayList<>();
        try {
            String sql = "SELECT a.id_abonnement, a.id_sport, s.nom_sport AS nomSport, a.type_abonnement, a.date_debut, a.date_fin, a.tarif, a.etat " +
                    "FROM abonnement a " +
                    "JOIN sport s ON a.id_sport = s.id_sport WHERE a.id_sport = ?";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setInt(1, idSport);
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        Abonnement abonnement = new Abonnement(
                                rs.getInt("id_abonnement"),
                                rs.getInt("id_sport"), // Updated to match the query column name
                                rs.getString("nomSport"), // Use the alias for the sport name
                                rs.getString("type_abonnement"),
                                rs.getDate("date_debut"),
                                rs.getDate("date_fin"),
                                rs.getDouble("tarif"),
                                rs.getString("etat")
                        );
                        abonnements.add(abonnement);
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la recherche des abonnements par sport : " + e.getMessage(), e);
        }
        return abonnements;
    }
}