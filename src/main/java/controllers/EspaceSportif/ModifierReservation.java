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
import models.match.SessionManager;
import models.utilisateur.Role;
import models.utilisateur.User;
import services.EspaceSportif.ReservationService;
import services.EspaceSportif.EspaceSportifService;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class ModifierReservation {

    @FXML
    private Button bt_user;

    @FXML
    private Button dashboard;
    @FXML
    private Button espace;
    @FXML
    private Button logistique;
    @FXML
    private Label nom_user;
    private void afficherProfil(User user) {

        if (user.getImage() != null && !user.getImage().isEmpty()) {
            javafx.scene.image.Image image = new javafx.scene.image.Image(user.getImage());
            String name = user.getPrenom();
            nom_user.setText(name);
            // profileImageView.setImage(image);

        }
    }
    @FXML
    void match(ActionEvent event) {

    }


    @FXML
    void pageuser(ActionEvent event) {
        User user = SessionManager.getCurrentUser();
        if (user != null) {

            String role = user.getRole().getValue();
            if (user.getRole() == Role.ADMIN)
            {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/user/adminpage.fxml"));
                    Stage stage = (Stage) bt_user.getScene().getWindow();
                    stage.setScene(new Scene(loader.load()));
                    stage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                    showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de charger la page d'inscription.");
                }
            }

        } else {
            System.out.println("Aucun utilisateur connecté !");
        }


    }
    @FXML
    void sponsor(ActionEvent event) {

    }

    @FXML
    void teams(ActionEvent event) {

    }

    @FXML
    void dashboard(ActionEvent event) {

    }

    @FXML
    void espace(ActionEvent event) {
        User user = SessionManager.getCurrentUser();
        if (user != null) {

            String role = user.getRole().getValue();
            if (user.getRole() == Role.ADMIN)
            {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/AffichageEspace.fxml"));
                    Stage stage = (Stage) espace.getScene().getWindow();
                    stage.setScene(new Scene(loader.load()));
                    stage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                    showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de charger la page d'inscription.");
                }
            }

        } else {
            System.out.println("Aucun utilisateur connecté !");
        }


    }

    @FXML
    void logistique(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fournisseur/DisplayFournisseur.fxml"));
            Stage stage = (Stage) logistique.getScene().getWindow();
            stage.setScene(new Scene(loader.load()));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de charger la page d'inscription.");
        }


    }





    @FXML
    private ComboBox<String> lieuField;

    @FXML
    private DatePicker dateField;

    @FXML
    private ComboBox<String> motifField;

    @FXML
    private ComboBox<String> statusField;

    @FXML
    private Button updateButton;

    @FXML
    private Button cancelButton;

    private final ReservationService reservationService = new ReservationService();
    private final EspaceSportifService espaceService = new EspaceSportifService();

    private Reservation reservationToEdit;

    @FXML
    public void initialize() {
        loadLieux();
        loadMotifs();
        loadStatus();
    }

    private void loadLieux() {
        lieuField.getItems().setAll(espaceService.getLieux());
    }

    private void loadMotifs() {
        motifField.getItems().setAll("match", "entraînement", "tournoi", "autre");
    }

    private void loadStatus() {
        statusField.getItems().setAll("confirmée", "annulée", "en attente");
    }

    public void setReservationToEdit(Reservation reservation) {
        this.reservationToEdit = reservation;
        lieuField.setValue(espaceService.getNomLieuById(reservation.getIdLieu()));
        dateField.setValue(reservation.getDateReservee().toLocalDateTime().toLocalDate());
        motifField.setValue(reservation.getMotif());
        statusField.setValue(reservation.getStatus());
    }

    @FXML
    private void updateReservation(ActionEvent event) {
        if (reservationToEdit == null) return;

        String lieu = lieuField.getValue();
        LocalDateTime date = dateField.getValue().atStartOfDay();
        String motif = motifField.getValue();
        String status = statusField.getValue();

        if (lieu == null || date == null || motif == null || status == null) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Veuillez remplir tous les champs !");
            return;
        }

        reservationToEdit.setIdLieu(espaceService.getIdLieuByName(lieu));
        reservationToEdit.setDateReservee(Timestamp.valueOf(date));
        reservationToEdit.setMotif(motif);
        reservationToEdit.setStatus(status);

        reservationService.modifier(reservationToEdit);

        showAlert(Alert.AlertType.INFORMATION, "Succès", "Réservation modifiée avec succès !");
        goToAfficherReservations(event);
    }

    @FXML
    private void goToAfficherReservations(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AffichageReservation.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Liste des Réservations");
            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de charger la page AffichageReservations.");
            e.printStackTrace();
        }
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
    public void annuler(ActionEvent actionEvent) {
        lieuField.setValue(null);
        dateField.setValue(null);
        motifField.setValue(null);
        statusField.setValue(null);
    }

}
