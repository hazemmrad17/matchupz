package controllers.joueur;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import models.joueur.EvaluationPhysique;
import models.match.SessionManager;
import models.utilisateur.Role;
import models.utilisateur.User;
import services.joueur.EvaluationPhysiqueService;

import java.io.IOException;
import java.util.Date;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

public class ModifyEvaluation {
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
// Corrected class name to match FXML

    @FXML private Button Home;
    @FXML private Button annulerButton;
    @FXML private Button modifierButton;
    @FXML private Button joueurButton;
    @FXML private Button logout;
    @FXML private TextField idJoueurField;
    @FXML private DatePicker dateEvaluationField;
    @FXML private TextField niveauEnduranceField;
    @FXML private TextField forcePhysiqueField;
    @FXML private TextField vitesseField;
    @FXML private TextField etatBlessureField;

    private EvaluationPhysiqueService evaluationService = new EvaluationPhysiqueService();
    private EvaluationPhysique evaluationToModify;

    @FXML
    private void initialize() {
        // Debug to check injection
        System.out.println("Home: " + (Home != null ? "OK" : "NULL"));
        System.out.println("annulerButton: " + (annulerButton != null ? "OK" : "NULL"));
        System.out.println("modifierButton: " + (modifierButton != null ? "OK" : "NULL"));
        System.out.println("joueurButton: " + (joueurButton != null ? "OK" : "NULL"));
        System.out.println("logout: " + (logout != null ? "OK" : "NULL"));
    }

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
    private void handleLogout() {
        try {
            Stage stage = (Stage) logout.getScene().getWindow();
            stage.close();
        } catch (Exception e) {
            showErrorAlert("Logout Failed", e.getMessage());
        }
    }

    public void setEvaluationToModify(EvaluationPhysique evaluation) {
        this.evaluationToModify = evaluation;
        idJoueurField.setText(String.valueOf(evaluation.getIdJoueur()));
        Date utilDate = evaluation.getDateEvaluation();
        LocalDate localDate = Instant.ofEpochMilli(utilDate.getTime())
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
        dateEvaluationField.setValue(localDate);
        niveauEnduranceField.setText(String.valueOf(evaluation.getNiveauEndurance()));
        forcePhysiqueField.setText(String.valueOf(evaluation.getForcePhysique()));
        vitesseField.setText(String.valueOf(evaluation.getVitesse()));
        etatBlessureField.setText(evaluation.getEtatBlessure());
    }

    @FXML
    private void modifier() {
        if (idJoueurField.getText().trim().isEmpty() || dateEvaluationField.getValue() == null ||
                niveauEnduranceField.getText().trim().isEmpty() || forcePhysiqueField.getText().trim().isEmpty() ||
                vitesseField.getText().trim().isEmpty() || etatBlessureField.getText().trim().isEmpty()) {

            showErrorAlert("Missing Required Fields", "Please fill in all fields before modifying the evaluation.");
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
            if (niveauEndurance < 0 || niveauEndurance > 10 || forcePhysique < 0 || forcePhysique > 10 || vitesse < 0 || vitesse > 10) {
                showErrorAlert("Invalid Input", "Endurance, Strength, and Speed must be between 0 and 10.");
                return;
            }

            Date utilDate = Date.from(dateEvaluation.atStartOfDay(ZoneId.systemDefault()).toInstant());

            evaluationToModify.setIdJoueur(idJoueur);
            evaluationToModify.setDateEvaluation(utilDate);
            evaluationToModify.setNiveauEndurance(niveauEndurance);
            evaluationToModify.setForcePhysique(forcePhysique);
            evaluationToModify.setVitesse(vitesse);
            evaluationToModify.setEtatBlessure(etatBlessure);

            evaluationService.modifier(evaluationToModify);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText("Evaluation Modified Successfully!");
            alert.setContentText("The evaluation with ID " + evaluationToModify.getIdEvaluation() + " has been updated.");
            alert.showAndWait();

        } catch (NumberFormatException e) {
            showErrorAlert("Invalid Number Format", "Please enter valid numbers for ID, Endurance, Strength, and Speed.");
        } catch (Exception e) {
            showErrorAlert("An Error Occurred", e.getMessage());
        }
    }

    private void loadFXML(String fxmlPath, String errorMessage, Button sourceButton) {
        try {
            if (sourceButton == null) {
                showErrorAlert("Button Error", "Source button is null for FXML: " + fxmlPath);
                return;
            }
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

    private void showErrorAlert(String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}