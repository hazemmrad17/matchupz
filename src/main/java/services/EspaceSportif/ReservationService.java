package services.EspaceSportif;

import models.EspaceSportif.Reservation;
import utils.MyDatabase;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReservationService implements EspaceService<Reservation> {
    Connection connection = MyDatabase.getInstance().getConnection();

    @Override
    public void ajouter(Reservation reservation) {
        String req = "INSERT INTO `reservation` (`id_lieu`, `date_reservee`, `motif`, `status`) VALUES (?, ?, ?, ?)";
        try {
            PreparedStatement ps = connection.prepareStatement(req);
            ps.setInt(1, reservation.getIdLieu());
            ps.setTimestamp(2, reservation.getDateReservee());
            ps.setString(3, reservation.getMotif());
            ps.setString(4, reservation.getStatus());
            ps.executeUpdate();
            System.out.println("Réservation ajoutée !");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void modifier(Reservation reservation) {
        String req = "UPDATE `reservation` SET `id_lieu`=?, `date_reservee`=?, `motif`=?, `status`=? WHERE `id_reservation`=?";
        try {
            PreparedStatement ps = connection.prepareStatement(req);
            ps.setInt(1, reservation.getIdLieu());
            ps.setTimestamp(2, reservation.getDateReservee());
            ps.setString(3, reservation.getMotif());
            ps.setString(4, reservation.getStatus());
            ps.setInt(5, reservation.getIdReservation());
            ps.executeUpdate();
            System.out.println("Réservation modifiée !");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void supprimer(Reservation reservation) {
        String req = "DELETE FROM `reservation` WHERE `id_reservation`=?";
        try {
            PreparedStatement ps = connection.prepareStatement(req);
            ps.setInt(1, reservation.getIdReservation());
            ps.executeUpdate();
            System.out.println("Réservation supprimée !");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Reservation> rechercher() {
        String req = "SELECT * FROM `reservation`";
        List<Reservation> reservations = new ArrayList<>();
        try {
            PreparedStatement ps = connection.prepareStatement(req);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Reservation reservation = new Reservation(
                        rs.getInt("id_reservation"),
                        rs.getInt("id_lieu"),
                        rs.getTimestamp("date_reservee"),
                        rs.getString("motif"),
                        rs.getString("status")
                );
                reservations.add(reservation);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reservations;
    }
}
