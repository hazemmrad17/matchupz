package controllers.EspaceSportif;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import models.EspaceSportif.Reservation;
import services.EspaceSportif.ReservationService;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class ModifierReservation {

    private final ReservationService reservationService = new ReservationService();
    private Reservation reservationActuelle;

    @FXML private ComboBox<String> idLieuField;
    @FXML private DatePicker dateField;
    @FXML private ComboBox<String> heureField;
    @FXML private ComboBox<String> motifField;
    @FXML private ComboBox<String> statusField;
    @FXML private Button btnModifier;

    public void setReservation(Reservation reservation) {
        this.reservationActuelle = reservation;
        idLieuField.setValue(String.valueOf(reservation.getIdLieu()));
        dateField.setValue(reservation.getDateReservee().toLocalDateTime().toLocalDate());
        motifField.setValue(reservation.getMotif());
        statusField.setValue(reservation.getStatus());
    }

    @FXML
    private void modifierReservation(ActionEvent event) {
        try {
            if (reservationActuelle == null) {
                showAlert("Erreur", "Aucune réservation sélectionnée.");
                return;
            }

            String idLieu = idLieuField.getValue();
            if (idLieu == null || idLieu.isEmpty()) {
                showAlert("Erreur", "Veuillez sélectionner un lieu valide.");
                return;
            }

            LocalDate date = dateField.getValue();
            if (date == null) {
                showAlert("Erreur", "Veuillez sélectionner une date valide.");
                return;
            }

            String heure = heureField.getValue();
            if (heure == null) {
                showAlert("Erreur", "Veuillez sélectionner une heure.");
                return;
            }

            String motif = motifField.getValue();
            String status = statusField.getValue();

            Timestamp dateReservee = Timestamp.valueOf(LocalDateTime.of(date, LocalDateTime.now().toLocalTime()));
            reservationActuelle.setIdLieu(Integer.parseInt(idLieu));
            reservationActuelle.setDateReservee(dateReservee);
            reservationActuelle.setMotif(motif);
            reservationActuelle.setStatus(status);

            reservationService.modifier(reservationActuelle);
            showAlert("Succès", "Réservation modifiée avec succès !");
            goToAfficherReservations(event);

        } catch (NumberFormatException e) {
            showAlert("Erreur", "Veuillez entrer des données valides.");
        }
    }

    @FXML
    private void goToAfficherReservations(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/AffichageEspace.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void annulerModification(ActionEvent event) {
        clearFields();
    }

    private void clearFields() {
        idLieuField.setValue(null);
        dateField.setValue(null);
        heureField.setValue(null);
        motifField.setValue(null);
        statusField.setValue(null);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
