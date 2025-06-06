package services.sponsor;

import models.sponsor.Contrat;
import services.IService;
import utils.MyDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.text.ParseException;

public class ContratService implements IService<Contrat> {

    Connection connection = MyDataSource.getInstance().getConn();

    public ContratService() {}

    @Override
    public void ajouter(Contrat contrat) {
        String checkTitleQuery = "SELECT COUNT(*) FROM `contrats` WHERE `titre` = ?";
        String insertQuery = "INSERT INTO `contrats`( `id_sponsor`, `titre`, `DateDebut`, `DateFin`, `montant`, `signature` , `article`) VALUES (?,?,?,?,?,?,?)";

        try {
            // Check if the title is unique
            PreparedStatement checkPs = this.connection.prepareStatement(checkTitleQuery);
            checkPs.setString(1, contrat.getTitre());
            ResultSet rs = checkPs.executeQuery();
            rs.next();

            // Validate dates
            //String formattedDateDebut = formatDate(contrat.getDateDebut());
            //String formattedDateFin = formatDate(contrat.getDateFin());
            if (contrat.getDateFin() == null || contrat.getDateFin() == null) {
                System.out.println("Error: Les dates doivent être au format yyyy/MM/dd!");
                return;
            }

            // Validate montant
            if (contrat.getMontant() <= 0) {
                System.out.println("Error: Le montant doit être un nombre positif!");
                return;
            }

            // Insert the contract
            PreparedStatement ps = this.connection.prepareStatement(insertQuery);
            ps.setInt(1, contrat.getId_sponsor());
            ps.setString(2, contrat.getTitre());
            ps.setString(3, contrat.getDateDebut());
            ps.setString(4, contrat.getDateFin());
            ps.setFloat(5, contrat.getMontant());
            ps.setString(6, contrat.getSignaturePath());
            ps.setString(7, contrat.getArticle());
            ps.executeUpdate();
            System.out.println("Contrat ajouté!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void modifier(Contrat contrat) {
        String checkTitleQuery = "SELECT COUNT(*) FROM `contrats` WHERE `titre` = ? AND `id_contrat` != ?";
        String updateQuery = "UPDATE `contrats` SET id_sponsor=?, titre=?, DateDebut=?, DateFin=?, montant=?, signature=?, article=? WHERE id_contrat=?";

        try {
            // Check if the title is unique
            PreparedStatement checkPs = this.connection.prepareStatement(checkTitleQuery);
            checkPs.setString(1, contrat.getTitre());
            checkPs.setInt(2, contrat.getId_contrat());
            ResultSet rs = checkPs.executeQuery();
            rs.next();
            // Validate dates
            //String formattedDateDebut = formatDate(contrat.getDateDebut());
            //String formattedDateFin = formatDate(contrat.getDateFin());
            if (contrat.getDateDebut() == null || contrat.getDateFin() == null) {
                System.out.println("Error: Les dates doivent être au format DD/MM/YYYY!");
                return;
            }

            // Validate montant
            if (contrat.getMontant() <= 0) {
                System.out.println("Error: Le montant doit être un nombre positif!");
                return;
            }

            // Update the contract
            PreparedStatement ps = this.connection.prepareStatement(updateQuery);
            ps.setInt(1, contrat.getId_sponsor());
            ps.setString(2, contrat.getTitre());
            ps.setString(3, contrat.getDateDebut());
            ps.setString(4, contrat.getDateFin());
            ps.setFloat(5, contrat.getMontant());
            ps.setString(6, contrat.getSignaturePath());
            ps.setString(7, contrat.getArticle());
            ps.setInt(8, contrat.getId_contrat());
            ps.executeUpdate();
            System.out.println("Contrat modifié!");
            System.out.println("Updating contract with id: " + contrat.getId_contrat());
            System.out.println("New id_sponsor: " + contrat.getId_sponsor());
            System.out.println("New title: " + contrat.getTitre());
            System.out.println("New DateDebut: " + contrat.getDateDebut());
            System.out.println("New DateFin: " + contrat.getDateFin());
            System.out.println("New montant: " + contrat.getMontant());
            System.out.println("New signature: " + contrat.getSignaturePath());
            System.out.println("New article: " + contrat.getArticle());
            int rowsAffected = ps.executeUpdate();
            System.out.println("Rows affected: " + rowsAffected);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void supprimer(Contrat contrat) {
        String req = "DELETE FROM `contrats` WHERE id_contrat = ?";

        try {
            PreparedStatement ps = this.connection.prepareStatement(req);
            ps.setInt(1, contrat.getId_contrat());
            ps.executeUpdate();
            System.out.println("Contrat supprimé!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Contrat> recherche() {
        String req = "SELECT * FROM `contrats`";
        List<Contrat> contrats = new ArrayList<>();

        try {
            PreparedStatement ps = this.connection.prepareStatement(req);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Contrat contrat = new Contrat();
                contrat.setId_contrat(rs.getInt("id_contrat"));
                contrat.setId_sponsor(rs.getInt("id_sponsor"));
                contrat.setTitre(rs.getString("titre"));
                contrat.setDateDebut(rs.getString("DateDebut"));
                contrat.setDateFin(rs.getString("DateFin"));
                contrat.setMontant(rs.getFloat("montant"));
                contrat.setSignaturePath(rs.getString("signature"));
                contrat.setArticle(rs.getString("article"));
                contrats.add(contrat);
            }

            System.out.println(contrats);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return contrats;
    }



    public boolean isContratTitreExists(String nom) {
        String checkNameQuery = "SELECT COUNT(*) FROM contrats WHERE titre = ?";

        try (PreparedStatement checkPs = this.connection.prepareStatement(checkNameQuery)) {
            checkPs.setString(1, nom);
            ResultSet rs = checkPs.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la vérification du titre du contrat : " + e.getMessage());
        }

        return false;
    }
}
