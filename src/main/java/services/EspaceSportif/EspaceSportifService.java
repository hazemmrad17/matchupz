package services.EspaceSportif;

import models.EspaceSportif.EspaceSportif;
import utils.MyDataSource;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EspaceSportifService implements EspaceService<EspaceSportif> {

    private final Connection connection = MyDataSource.getInstance().getConn();

    @Override
    public void ajouter(EspaceSportif espaceSportif) {
        String req = "INSERT INTO `espacesportif` (`nom_espace`, `adresse`, `categorie`, `capacite`) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(req)) {
            ps.setString(1, espaceSportif.getNomEspace());
            ps.setString(2, espaceSportif.getAdresse());
            ps.setString(3, espaceSportif.getCategorie());
            ps.setFloat(4, espaceSportif.getCapacite());
            ps.executeUpdate();
            System.out.println("✅ Espace sportif ajouté !");
        } catch (SQLException e) {
            System.err.println("❌ Erreur lors de l'ajout : " + e.getMessage());
        }
    }

    @Override
    public void modifier(EspaceSportif espaceSportif) {
        String req = "UPDATE `espacesportif` SET nom_espace=?, adresse=?, categorie=?, capacite=? WHERE id_lieu=?";
        try (PreparedStatement ps = connection.prepareStatement(req)) {
            ps.setString(1, espaceSportif.getNomEspace());
            ps.setString(2, espaceSportif.getAdresse());
            ps.setString(3, espaceSportif.getCategorie());
            ps.setFloat(4, espaceSportif.getCapacite());
            ps.setInt(5, espaceSportif.getIdLieu());
            ps.executeUpdate();
            System.out.println("✅ Espace sportif modifié !");
        } catch (SQLException e) {
            System.err.println("❌ Erreur lors de la modification : " + e.getMessage());
        }
    }

    @Override
    public void supprimer(EspaceSportif espaceSportif) {
        String req = "DELETE FROM `espacesportif` WHERE id_lieu=?";
        try (PreparedStatement ps = connection.prepareStatement(req)) {
            ps.setInt(1, espaceSportif.getIdLieu());
            ps.executeUpdate();
            System.out.println("✅ Espace sportif supprimé !");
        } catch (SQLException e) {
            System.err.println("❌ Erreur lors de la suppression : " + e.getMessage());
        }
    }

    @Override
    public List<EspaceSportif> rechercher() {
        String req = "SELECT * FROM `espacesportif`";
        List<EspaceSportif> espacesSportifs = new ArrayList<>();

        try (PreparedStatement ps = connection.prepareStatement(req);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                EspaceSportif espaceSportif = new EspaceSportif();
                espaceSportif.setIdLieu(rs.getInt("id_lieu"));
                espaceSportif.setNomEspace(rs.getString("nom_espace"));
                espaceSportif.setAdresse(rs.getString("adresse"));
                espaceSportif.setCategorie(rs.getString("categorie"));
                espaceSportif.setCapacite(rs.getFloat("capacite"));
                espacesSportifs.add(espaceSportif);
            }
        } catch (SQLException e) {
            System.err.println("❌ Erreur lors de la récupération : " + e.getMessage());
        }

        return espacesSportifs;
    }


    public List<String> getCategories() {
        List<String> categories = new ArrayList<>();
        String query = "SHOW COLUMNS FROM `espacesportif` WHERE Field = 'categorie'";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            if (rs.next()) {
                String enumValues = rs.getString("Type");
                enumValues = enumValues.substring(enumValues.indexOf("(") + 1, enumValues.lastIndexOf(")"));
                String[] values = enumValues.replace("'", "").split(",");
                categories.addAll(List.of(values));
            }
        } catch (SQLException e) {
            System.err.println("❌ Erreur lors de la récupération des catégories : " + e.getMessage());
        }

        return categories;
    }

    public List<EspaceSportif> rechercherParMotCle(String motCle) {
        String req = "SELECT id_lieu, nom_espace, adresse, categorie, capacite FROM espacesportif " +
                "WHERE nom_espace LIKE ? OR adresse LIKE ? OR categorie LIKE ?";
        List<EspaceSportif> espacesSportifs = new ArrayList<>();

        try (PreparedStatement ps = connection.prepareStatement(req)) {
            ps.setString(1, "%" + motCle + "%");
            ps.setString(2, "%" + motCle + "%");
            ps.setString(3, "%" + motCle + "%");

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    EspaceSportif espaceSportif = new EspaceSportif();
                    espaceSportif.setIdLieu(rs.getInt("id_lieu"));
                    espaceSportif.setNomEspace(rs.getString("nom_espace"));
                    espaceSportif.setAdresse(rs.getString("adresse"));
                    espaceSportif.setCategorie(rs.getString("categorie"));
                    espaceSportif.setCapacite(rs.getFloat("capacite"));
                    espacesSportifs.add(espaceSportif);
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ Erreur lors de la récupération des espaces sportifs par mot-clé : " + e.getMessage());
        }

        return espacesSportifs;
    }

    public int getIdLieuByName(String nomLieu) {
        String req = "SELECT id_lieu FROM espacesportif WHERE nom_espace = ?";
        try (PreparedStatement ps = connection.prepareStatement(req)) {
            ps.setString(1, nomLieu);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id_lieu");
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ Erreur lors de la récupération de l'ID du lieu : " + e.getMessage());
        }
        return -1; // Retourne -1 si aucun ID trouvé
    }
    public List<String> getLieux() {
        List<String> lieux = new ArrayList<>();
        String req = "SELECT nom_espace FROM espacesportif";

        try (PreparedStatement ps = connection.prepareStatement(req);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lieux.add(rs.getString("nom_espace"));
            }
        } catch (SQLException e) {
            System.err.println("❌ Erreur lors de la récupération des lieux : " + e.getMessage());
        }

        return lieux;
    }
    public String getNomLieuById(int idLieu) {
        String query = "SELECT nom_espace FROM espacesportif WHERE id_lieu = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, idLieu);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("nom");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
