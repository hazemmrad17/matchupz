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
import models.joueur.Sport;
import models.match.SessionManager;
import models.utilisateur.Role;
import models.utilisateur.User;
import services.joueur.SportService;

import java.io.IOException;

public class AjoutSport {

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
        loadScene("/joueur/MainController.fxml",teams);
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
    private Button Home;

    @FXML
    private Button ajoutSport;

    @FXML
    private Button annulerButton;

    @FXML
    private TextField nomSportField;

    @FXML
    private TextField descriptionSportField;

    @FXML
    private void handleHome() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/joueur/MainController.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) Home.getScene().getWindow();

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Failed to load the FXML file");
            alert.setContentText("Details: " + e.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    private void handleAnnulerButton() {
        try {
            // Load the FXML file for the sports display screen
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/joueur/DisplaySport.fxml"));
            Parent root = loader.load();

            // Get the current stage
            Stage stage = (Stage) annulerButton.getScene().getWindow();

            // Set the new scene
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the error (e.g., show an alert)
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Failed to load the FXML file");
            alert.setContentText("Details: " + e.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    void ajouter(ActionEvent event) {
        if (nomSportField.getText().trim().isEmpty() || descriptionSportField.getText().trim().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText("Champs obligatoires manquants");
            alert.setContentText("Veuillez remplir tous les champs avant d'ajouter un sport.");
            alert.showAndWait();
            return;
        }

        try {
            // Retrieve values from the fields
            String nomSport = nomSportField.getText().trim();
            String description = descriptionSportField.getText().trim();

            // Validate description length
            if (description.length() > 500) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur de saisie");
                alert.setHeaderText("Description trop longue");
                alert.setContentText("La description ne doit pas dépasser 500 caractères.");
                alert.showAndWait();
                return;
            }

            // Create Sport object
            Sport sport = new Sport(nomSport, description);

            // Call the service to add the sport
            SportService sportService = new SportService();
            sportService.ajouter(sport);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Succès");
            alert.setHeaderText("Sport ajouté avec succès !");
            alert.setContentText("Le sport " + nomSport + " a été ajouté.");
            alert.showAndWait();

            // Automatically return to DisplaySport after adding
            loadScene("/joueur/DisplaySport.fxml", ajoutSport);

        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText("Une erreur est survenue");
            alert.setContentText("Détails : " + e.getMessage());
            alert.showAndWait();
        }
    }
}
