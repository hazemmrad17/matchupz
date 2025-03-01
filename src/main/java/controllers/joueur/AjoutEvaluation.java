package controllers.joueur;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import models.joueur.EvaluationPhysique;
import models.match.SessionManager;
import models.utilisateur.Role;
import models.utilisateur.User;
import services.joueur.EvaluationPhysiqueService;

import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;

public class AjoutEvaluation {

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


    @FXML private Button joueurButton;
    @FXML private Button Home;
    @FXML private Button annulerButton;
    @FXML private Button ajouterButton;
    @FXML private Button selectPhotoButton;
    @FXML private TextField photoField;
    @FXML private Button handleEvaluationButton; // Note: This should be renamed or mapped in FXML
    @FXML private Button logout;
    @FXML private TextField idJoueurField;
    @FXML private DatePicker dateEvaluationField;
    @FXML private TextField niveauEnduranceField;
    @FXML private TextField forcePhysiqueField;
    @FXML private TextField vitesseField;
    @FXML private TextField etatBlessureField;

    private EvaluationPhysiqueService evaluationService = new EvaluationPhysiqueService();

    @FXML
    private void handleHome() {
        loadFXML("/joueur/MainController.fxml", "Failed to load the Main page", Home);
    }

    @FXML
    private void handleAnnulerButton() {
        loadFXML("/joueur/DisplayEvaluation.fxml", "Failed to load the Display Evaluations page", annulerButton);
    }

    @FXML
    private void HandleJoueur() {
        loadFXML("/joueur/MainController.fxml", "Failed to load the Main Joueur page", joueurButton);
    }

    @FXML
    private void ajouter() {
        if (idJoueurField.getText().trim().isEmpty() || dateEvaluationField.getValue() == null ||
                niveauEnduranceField.getText().trim().isEmpty() || forcePhysiqueField.getText().trim().isEmpty() ||
                vitesseField.getText().trim().isEmpty() || etatBlessureField.getText().trim().isEmpty()) {

            showErrorAlert("Missing Required Fields", "Please fill in all fields before adding an evaluation.");
            return;
        }

        try {
            int idJoueur = Integer.parseInt(idJoueurField.getText().trim());
            LocalDate dateEvaluation = dateEvaluationField.getValue();
            float niveauEndurance = Float.parseFloat(niveauEnduranceField.getText().trim());
            float forcePhysique = Float.parseFloat(forcePhysiqueField.getText().trim());
            float vitesse = Float.parseFloat(vitesseField.getText().trim());
            String etatBlessure = etatBlessureField.getText().trim();

            if (idJoueur <= 0) {
                showErrorAlert("Invalid Input", "Player ID must be a positive number.");
                return;
            }

            if (niveauEndurance < 0 || forcePhysique < 0 || vitesse < 0) {
                showErrorAlert("Invalid Input", "Endurance, Strength, and Speed must be non-negative.");
                return;
            }

            Date sqlDateEvaluation = Date.valueOf(dateEvaluation);
            EvaluationPhysique evaluation = new EvaluationPhysique(
                    idJoueur, sqlDateEvaluation, niveauEndurance, forcePhysique, vitesse, etatBlessure
            );

            evaluationService.ajouter(evaluation);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText("Evaluation Added Successfully!");
            alert.setContentText("The evaluation for player ID " + idJoueur + " has been added with ID " + evaluation.getIdEvaluation() + ".");
            alert.showAndWait();

            clearFields();
            handleAnnulerButton();

        } catch (NumberFormatException e) {
            showErrorAlert("Invalid Number Format", "Please enter valid numbers for ID, Endurance, Strength, and Speed.");
        } catch (Exception e) {
            showErrorAlert("An Error Occurred", e.getMessage());
        }
    }

    @FXML
    private void handleLogout() {
        try {
            Stage stage = (Stage) logout.getScene().getWindow();
            stage.close();
        } catch (Exception e) {
            showErrorAlert("Logout Failed", e.getMessage());
        }
    }

    // Temporary handler for unmapped button (remove or map in FXML)
    @FXML
    private void handleEvaluation() {
        System.out.println("handleEvaluationButton clicked - not implemented yet");
    }

    private void loadFXML(String fxmlPath, String errorMessage, Button sourceButton) {
        try {
            java.net.URL location = getClass().getResource(fxmlPath);
            if (location == null) {
                showErrorAlert("Resource Not Found", "FXML file not found: " + fxmlPath);
                return;
            }
            FXMLLoader loader = new FXMLLoader(location);
            Parent root = loader.load();
            Stage stage = (Stage) sourceButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            showErrorAlert(errorMessage, e.getMessage());
        }
    }

    private void clearFields() {
        idJoueurField.clear();
        dateEvaluationField.setValue(null);
        niveauEnduranceField.clear();
        forcePhysiqueField.clear();
        vitesseField.clear();
        etatBlessureField.clear();
    }

    private void showErrorAlert(String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    private void selectPhoto() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choisir une photo de club");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );
        File file = fileChooser.showOpenDialog(selectPhotoButton.getScene().getWindow());
        if (file != null) {
            photoField.setText(file.toURI().toString());
        }
    }
}