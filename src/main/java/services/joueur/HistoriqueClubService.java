package services.joueur;

import models.joueur.HistoriqueClub;
import services.IService;
import utils.MyDataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HistoriqueClubService implements IService<HistoriqueClub> {
    private final Connection connection = MyDataSource.getInstance().getConn();

    @Override
    public void ajouter(HistoriqueClub historique) {
        String req = "INSERT INTO historique_club (id_joueur, nom_club, saison_debut, saison_fin) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(req, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, historique.getIdJoueur());
            ps.setString(2, historique.getNomClub());
            ps.setDate(3, historique.getSaisonDebut());
            ps.setDate(4, historique.getSaisonFin());
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                historique.setIdHistorique(rs.getInt(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de l'ajout de l'historique: " + e.getMessage(), e);
        }
    }

    @Override
    public void modifier(HistoriqueClub historique) {
        String req = "UPDATE historique_club SET id_joueur = ?, nom_club = ?, saison_debut = ?, saison_fin = ? WHERE id_historique = ?";
        try (PreparedStatement ps = connection.prepareStatement(req)) {
            ps.setInt(1, historique.getIdJoueur());
            ps.setString(2, historique.getNomClub());
            ps.setDate(3, historique.getSaisonDebut());
            ps.setDate(4, historique.getSaisonFin());
            ps.setInt(5, historique.getIdHistorique());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la modification de l'historique: " + e.getMessage(), e);
        }
    }

    @Override
    public void supprimer(HistoriqueClub historique) {
        String req = "DELETE FROM historique_club WHERE id_historique = ?";
        try (PreparedStatement ps = connection.prepareStatement(req)) {
            ps.setInt(1, historique.getIdHistorique());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la suppression de l'historique: " + e.getMessage(), e);
        }
    }

    @Override
    public List<HistoriqueClub> recherche() {
        List<HistoriqueClub> historiques = new ArrayList<>();
        String req = "SELECT id_historique, id_joueur, nom_club, saison_debut, saison_fin FROM historiqueclub";
        try (PreparedStatement ps = connection.prepareStatement(req); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                HistoriqueClub historique = new HistoriqueClub(
                        rs.getInt("id_historique"),
                        rs.getInt("id_joueur"),
                        rs.getString("nom_club"),
                        rs.getDate("saison_debut"),
                        rs.getDate("saison_fin")
                );
                historiques.add(historique);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la recherche des historiques: " + e.getMessage(), e);
        }
        return historiques;
    }
}