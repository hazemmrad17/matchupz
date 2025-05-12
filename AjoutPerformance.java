package controllers.joueur;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import models.joueur.PerformanceJoueur;
import models.match.SessionManager;
import models.utilisateur.Role;
import models.utilisateur.User;
import services.joueur.PerformanceJoueurService;

import java.io.IOException;

public class AjoutPerformance {

    @FXML
    private Button bt_user;
    @FXML
    private Button teams;
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
    void user(javafx.event.ActionEvent event) {
        User user = SessionManager.getCurrentUser();
        if (user != null && user.getRole() == Role.ADMIN) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/user/adminpage.fxml"));
                Stage stage = (Stage) bt_user.getScene().getWindow();
                stage.setScene(new Scene(loader.load()));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de charger la page d'inscription.");
            }
        } else {
            System.out.println("Aucun utilisateur connecté ou pas ADMIN !");
        }
    }

    @FXML
    void dashboard(javafx.event.ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Dashboard.fxml"));
            Stage stage = (Stage) dashboard.getScene().getWindow();
            stage.setScene(new Scene(loader.load()));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de charger la page d'inscription.");
        }

    }

    @FXML
    void espace(javafx.event.ActionEvent event) {
        User user = SessionManager.getCurrentUser();
        if (user != null && user.getRole() == Role.ADMIN) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/EspaceSportif/AffichageEspace.fxml"));
                Stage stage = (Stage) espace.getScene().getWindow();
                stage.setScene(new Scene(loader.load()));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de charger la page d'inscription.");
            }
        } else {
            System.out.println("Aucun utilisateur connecté ou pas ADMIN !");
        }
    }

    @FXML
    void logistique(javafx.event.ActionEvent event) {

        User user = SessionManager.getCurrentUser();
        if (user != null && user.getRole() == Role.ADMIN) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fournisseur/AfficherFournisseur.fxml"));
                Stage stage = (Stage) bt_user.getScene().getWindow();
                stage.setScene(new Scene(loader.load()));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de charger la page d'inscription.");
            }
        } else {
            System.out.println("Aucun utilisateur connecté ou pas ADMIN !");
        }
    }

    @FXML
    void sponsor(javafx.event.ActionEvent event) {


        User user = SessionManager.getCurrentUser();
        if (user != null && user.getRole() == Role.ADMIN) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/sponsoring/AfficherSponsor.fxml"));
                Stage stage = (Stage) dashboard.getScene().getWindow();
                stage.setScene(new Scene(loader.load()));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de charger la page d'inscription.");
            }
        } else {
            System.out.println("Aucun utilisateur connecté ou pas ADMIN !");
        }

    }

    @FXML
    private Button log_out;

    @FXML
    void log_out(javafx.event.ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/user/interfaceA.fxml"));
            Stage stage = (Stage) log_out.getScene().getWindow();
            stage.setScene(new Scene(loader.load()));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de charger la page d'inscription.");
        }
    }

    @FXML
    void match(javafx.event.ActionEvent event) {
        User user = SessionManager.getCurrentUser();
        if (user != null && user.getRole() == Role.ADMIN) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/MatchSchedule/AffichageMatch.fxml"));
                Stage stage = (Stage) bt_user.getScene().getWindow();
                stage.setScene(new Scene(loader.load()));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de charger la page d'inscription.");
            }
        } else {
            System.out.println("Aucun utilisateur connecté ou pas ADMIN !");
        }

    }

    @FXML
    void teams(javafx.event.ActionEvent event) {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/joueur/MainController.fxml"));
            Stage stage = (Stage) dashboard.getScene().getWindow();
            stage.setScene(new Scene(loader.load()));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de charger la page d'inscription.");
        }


    }
    private void showAlert(Alert.AlertType alertType, String erreur, String content) {
    }

    private void loadScene(String fxmlPath, Button button) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            Stage stage = (Stage) button.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.valueOf("Erreur"), "Échec du chargement", e.getMessage());
        }
    }

    @FXML
    private Button Home; // Matches fx:id="Home" in FXML

    @FXML
    private Button annulerButton;

    @FXML
    private Button AjoutPerformance; // Matches fx:id="AjoutPerformance" in FXML

    @FXML
    private TextField idJoueurField;

    @FXML
    private TextField saisonField;

    @FXML
    private TextField nombreMatchesField;

    @FXML
    private TextField minutesJoueesField;

    @FXML
    private TextField butsMarquesField;

    @FXML
    private TextField passesDecisivesField;

    @FXML
    private TextField cartonsJaunesField;

    @FXML
    private TextField cartonsRougesField;

    private PerformanceJoueurService performanceService = new PerformanceJoueurService();

    @FXML
    private void handleHome() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/joueur/MainController.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) Home.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            showErrorAlert("Failed to load the Main page", e.getMessage());
        }
    }

    @FXML
    private void handleAnnulerButton() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/joueur/DisplayPerformance.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) annulerButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            showErrorAlert("Failed to load the Display Performance page", e.getMessage());
        }
    }

    @FXML
    private void ajouter() { // Matches onAction="#ajouter" in FXML
        if (idJoueurField.getText().trim().isEmpty() || saisonField.getText().trim().isEmpty() ||
                nombreMatchesField.getText().trim().isEmpty() || minutesJoueesField.getText().trim().isEmpty() ||
                butsMarquesField.getText().trim().isEmpty() || passesDecisivesField.getText().trim().isEmpty() ||
                cartonsJaunesField.getText().trim().isEmpty() || cartonsRougesField.getText().trim().isEmpty()) {

            showErrorAlert("Missing Required Fields", "Please fill in all fields before adding a performance.");
            return;
        }

        try {
            // Retrieve and parse values from fields
            int idJoueur = Integer.parseInt(idJoueurField.getText().trim());
            String saison = saisonField.getText().trim();
            int nombreMatches = Integer.parseInt(nombreMatchesField.getText().trim());
            int minutesJouees = Integer.parseInt(minutesJoueesField.getText().trim());
            int butsMarques = Integer.parseInt(butsMarquesField.getText().trim());
            int passesDecisives = Integer.parseInt(passesDecisivesField.getText().trim());
            int cartonsJaunes = Integer.parseInt(cartonsJaunesField.getText().trim());
            int cartonsRouges = Integer.parseInt(cartonsRougesField.getText().trim());

            // Validation
            if (idJoueur <= 0) {
                showErrorAlert("Invalid Input", "Player ID must be a positive number.");
                return;
            }

            if (!saison.matches("^\\d{4}-\\d{4}$")) {
                showErrorAlert("Invalid Season Format", "Season must be in YYYY-YYYY format (e.g., 2023-2024).");
                return;
            }

            if (nombreMatches < 0 || minutesJouees < 0 || butsMarques < 0 || passesDecisives < 0 ||
                    cartonsJaunes < 0 || cartonsRouges < 0) {
                showErrorAlert("Invalid Input", "All performance metrics must be non-negative.");
                return;
            }

            // Create PerformanceJoueur object
            PerformanceJoueur performance = new PerformanceJoueur(
                    idJoueur, saison, nombreMatches, minutesJouees,
                    butsMarques, passesDecisives, cartonsJaunes, cartonsRouges
            );

            // Add to database
            performanceService.ajouter(performance);

            // Show success message
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText("Performance Added Successfully!");
            alert.setContentText("The performance for player ID " + idJoueur + " has been added for season " + saison + " with ID " + performance.getIdPerformance() + ".");
            alert.showAndWait();

            // Navigate to DisplayPerformance after success
            handleAnnulerButton();

        } catch (NumberFormatException e) {
            showErrorAlert("Invalid Number Format", "Please enter valid numbers for all numeric fields.");
        } catch (Exception e) {
            showErrorAlert("An Error Occurred", e.getMessage());
        }
    }

    private void showErrorAlert(String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}