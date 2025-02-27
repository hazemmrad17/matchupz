package services;

import models.Club;
import services.IService;
import utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClubService implements IService<Club> {
    Connection connection = MyDatabase.getInstance().getConnection();

    @Override
    public void ajouter(Club club) {
        String req = "INSERT INTO Club (nom_club, photo_url) VALUES (?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(req, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, club.getNomClub());
            ps.setString(2, club.getPhotoUrl());
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                club.setIdClub(rs.getInt(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void modifier(Club club) {
        String req = "UPDATE Club SET nom_club=?, photo_url=? WHERE id_club=?";
        try (PreparedStatement ps = connection.prepareStatement(req)) {
            ps.setString(1, club.getNomClub());
            ps.setString(2, club.getPhotoUrl());
            ps.setInt(3, club.getIdClub());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void supprimer(Club club) {
        String req = "DELETE FROM Club WHERE id_club=?";
        try (PreparedStatement ps = connection.prepareStatement(req)) {
            ps.setInt(1, club.getIdClub());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Club> rechercher() {
        List<Club> clubs = new ArrayList<>();
        String req = "SELECT * FROM Club";
        try (PreparedStatement ps = connection.prepareStatement(req); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Club club = new Club(
                        rs.getInt("id_club"),
                        rs.getString("nom_club"),
                        rs.getString("photo_url")
                );
                clubs.add(club);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return clubs;
    }
}