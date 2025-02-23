package controllers.Match;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import models.match.Match;
import services.match.MatchService;

import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.scene.control.*;

import java.io.IOException;

public class AjoutMatch {

    @FXML
    private TextField C1Field;

    @FXML
    private TextField C2Field;

    @FXML
    private ComboBox<String> sportTypeComboBox; // ComboBox for SportType

    @FXML
    private Button ajouterButton;
    @FXML
    private Button annulerButton;

    @FXML
    void initialize() {
        // Initialize ComboBox with the sport types
        sportTypeComboBox.getItems().addAll("Football", "Basketball", "Tennis", "Volleyball", "Handball");
    }

    @FXML
    void ajouter(ActionEvent event) {
        // Validate that all fields are filled
        if (C1Field.getText().trim().isEmpty() || C2Field.getText().trim().isEmpty() ||
                sportTypeComboBox.getSelectionModel().isEmpty()) {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText("Champs obligatoires manquants");
            alert.setContentText("Veuillez remplir tous les champs avant d'ajouter un match.");
            alert.showAndWait();
            return;
        }

        try {
            // Retrieve values from the fields
            String C1 = C1Field.getText().trim();
            String C2 = C2Field.getText().trim();
            String sportType = sportTypeComboBox.getSelectionModel().getSelectedItem(); // Get selected sport type

            // Validate the SportType from ComboBox
            if (sportType == null || sportType.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur de saisie");
                alert.setHeaderText("Sport invalide");
                alert.setContentText("Veuillez sélectionner un type de sport.");
                alert.showAndWait();
                return;
            }

            // Create and save the Match object
            // Assuming idMatch is auto-generated or managed by the database
            Match match = new Match(C1, C2, sportType); // idMatch will be auto-generated
            MatchService matchService = new MatchService();
            matchService.ajouter(match);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Succès");
            alert.setHeaderText("Match ajouté avec succès !");
            alert.setContentText("Le match entre " + C1 + " et " + C2 + " a été ajouté.");
            alert.showAndWait();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText("Une erreur est survenue");
            alert.setContentText("Détails : " + e.getMessage());
            alert.showAndWait();
        }
    }
    @FXML
    private void handleAnnulerButton() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Match/DisplayMatch.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) annulerButton.getScene().getWindow();

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
}
