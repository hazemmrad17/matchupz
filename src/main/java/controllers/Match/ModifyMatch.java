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
import java.sql.Date;
import java.time.LocalDate;

public class ModifyMatch {

    @FXML
    private TextField team1Field;

    @FXML
    private TextField team2Field;

    @FXML
    private ComboBox<String> sportTypeField;  // Changed to ComboBox

    @FXML
    private DatePicker matchDateField; // Changed to DatePicker for better date handling

    @FXML
    private Button modifierButton;

    @FXML
    private Button annulerButton;

    private Match matchToModify; // The Match object you want to modify

    @FXML
    public void initialize() {
        // Populate the ComboBox with static sport types
        sportTypeField.getItems().addAll("Football", "Basketball", "Tennis", "Volleyball", "Handball");
    }

    public void setMatchToModify(Match match) {
        this.matchToModify = match;
        // Pre-fill the fields with the current match information
        team1Field.setText(match.getC1());
        team2Field.setText(match.getC2());
        sportTypeField.setValue(match.getSportType()); // Set ComboBox value
    }

    @FXML
    void modifier(ActionEvent event) {
        // Validate that all fields are filled
        if (team1Field.getText().trim().isEmpty() || team2Field.getText().trim().isEmpty() ||
                sportTypeField.getValue() == null) { // Check if ComboBox is selected

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText("Champs obligatoires manquants");
            alert.setContentText("Veuillez remplir tous les champs avant de modifier un match.");
            alert.showAndWait();
            return;
        }

        try {
            // Retrieve values from the fields
            String team1 = team1Field.getText().trim();
            String team2 = team2Field.getText().trim();
            String sportType = sportTypeField.getValue(); // Get the selected sport type from ComboBox
            // Update the Match object
            matchToModify.setC1(team1);
            matchToModify.setC2(team2);
            matchToModify.setSportType(sportType);

            // Save the changes using the MatchService
            MatchService matchService = new MatchService();
            matchService.modifier(matchToModify);  // Assuming this method correctly handles database persistence

            // After modifying, update the ComboBox to show the selected value (although it should automatically update)
            sportTypeField.setValue(sportType);  // Ensure ComboBox reflects the modified value

            // Show success alert
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Succès");
            alert.setHeaderText("Modification réussie !");
            alert.setContentText("Les informations du match ont été modifiées avec succès.");
            alert.showAndWait();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText("Une erreur est survenue");
            alert.setContentText("Détails : " + e.getMessage());
            System.out.println(e.getMessage());
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
