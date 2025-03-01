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
import services.joueur.PerformanceJoueurService; // Assuming this service exists

import java.io.IOException;

public class ModifyPerformance {
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

    private void showAlert(Alert.AlertType alertType, String erreur, String content) {
    }


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

    @FXML
    private Button annulerButton;

    @FXML
    private Button homeButton;

    @FXML
    private Button modifierButton;

    @FXML
    private void handleHome() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/joueur/MainController.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) homeButton.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showErrorAlert("Failed to load the Main page", e.getMessage());
        }
    }

    @FXML
    private void handleAnnulerButton() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/joueur/DisplayPerformance.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) annulerButton.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showErrorAlert("Failed to load the Display Performance page", e.getMessage());
        }
    }

    private PerformanceJoueur performanceToModify;

    public void setPerformanceToModify(PerformanceJoueur performance) {
        this.performanceToModify = performance;
        idJoueurField.setText(String.valueOf(performance.getIdJoueur()));
        saisonField.setText(performance.getSaison());
        nombreMatchesField.setText(String.valueOf(performance.getNombreMatches()));
        minutesJoueesField.setText(String.valueOf(performance.getMinutesJouees()));
        butsMarquesField.setText(String.valueOf(performance.getButsMarques()));
        passesDecisivesField.setText(String.valueOf(performance.getPassesDecisives()));
        cartonsJaunesField.setText(String.valueOf(performance.getCartonsJaunes()));
        cartonsRougesField.setText(String.valueOf(performance.getCartonsRouges()));
    }

    @FXML
    void modifier(ActionEvent event) {
        if (idJoueurField.getText().trim().isEmpty() || saisonField.getText().trim().isEmpty() ||
                nombreMatchesField.getText().trim().isEmpty() || minutesJoueesField.getText().trim().isEmpty() ||
                butsMarquesField.getText().trim().isEmpty() || passesDecisivesField.getText().trim().isEmpty() ||
                cartonsJaunesField.getText().trim().isEmpty() || cartonsRougesField.getText().trim().isEmpty()) {

            showErrorAlert("Missing Required Fields", "Please fill in all fields before modifying the performance.");
            return;
        }

        try {
            int idJoueur = Integer.parseInt(idJoueurField.getText().trim());
            String saison = saisonField.getText().trim();
            int nombreMatches = Integer.parseInt(nombreMatchesField.getText().trim());
            int minutesJouees = Integer.parseInt(minutesJoueesField.getText().trim());
            int butsMarques = Integer.parseInt(butsMarquesField.getText().trim());
            int passesDecisives = Integer.parseInt(passesDecisivesField.getText().trim());
            int cartonsJaunes = Integer.parseInt(cartonsJaunesField.getText().trim());
            int cartonsRouges = Integer.parseInt(cartonsRougesField.getText().trim());

            // Basic validation
            if (nombreMatches < 0 || minutesJouees < 0 || butsMarques < 0 || passesDecisives < 0 ||
                    cartonsJaunes < 0 || cartonsRouges < 0) {
                showErrorAlert("Invalid Input", "All numeric fields must be non-negative.");
                return;
            }

            String saisonRegex = "^\\d{4}-\\d{4}$";
            if (!saison.matches(saisonRegex)) {
                showErrorAlert("Invalid Season Format", "Season must be in YYYY-YYYY format (e.g., 2023-2024).");
                return;
            }

            // Update the performance object
            performanceToModify.setIdJoueur(idJoueur);
            performanceToModify.setSaison(saison);
            performanceToModify.setNombreMatches(nombreMatches);
            performanceToModify.setMinutesJouees(minutesJouees);
            performanceToModify.setButsMarques(butsMarques);
            performanceToModify.setPassesDecisives(passesDecisives);
            performanceToModify.setCartonsJaunes(cartonsJaunes);
            performanceToModify.setCartonsRouges(cartonsRouges);

            PerformanceJoueurService performanceService = new PerformanceJoueurService();
            performanceService.modifier(performanceToModify);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText("Modification Successful!");
            alert.setContentText("The performance data has been updated successfully.");
            alert.showAndWait();

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