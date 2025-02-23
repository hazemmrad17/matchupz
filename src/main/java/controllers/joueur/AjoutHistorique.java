package controllers.joueur;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import models.HistoriqueClub;
import services.joueur.HistoriqueClubService;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AjoutHistorique {

    @FXML
    private TextField idJoueurField;

    @FXML
    private TextField nomSportField;

    @FXML
    private TextField saisonDebutField;

    @FXML
    private TextField saisonFinField;

    @FXML
    private Button ajoutHistoriqueButton;

    @FXML
    private Button annulerButton;

    @FXML
    private void handleAnnulerButton() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/joueur/DisplayHistorique.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) annulerButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void ajouter(ActionEvent event) {
        try {
            String idJoueurString = idJoueurField.getText().trim();
            String nomClub = nomSportField.getText().trim();
            String saisonDebutString = saisonDebutField.getText().trim();
            String saisonFinString = saisonFinField.getText().trim();

            // Validation
            if (idJoueurString.isEmpty() || nomClub.isEmpty() || saisonDebutString.isEmpty()) {
                showAlert("Erreur", "Veuillez remplir tous les champs obligatoires.", Alert.AlertType.ERROR);
                return;
            }

            int idJoueur = Integer.parseInt(idJoueurString);
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date saisonDebut = dateFormat.parse(saisonDebutString);
            Date saisonFin = saisonFinString.isEmpty() ? null : dateFormat.parse(saisonFinString);

            // Create HistoriqueClub object
            HistoriqueClub historique = new HistoriqueClub(idJoueur, nomClub, saisonDebut, saisonFin);
            HistoriqueClubService historiqueService = new HistoriqueClubService();
            historiqueService.ajouter(historique);

            showAlert("Succès", "Historique ajouté avec succès !", Alert.AlertType.INFORMATION);
        } catch (NumberFormatException e) {
            showAlert("Erreur de format", "L'ID du joueur doit être un nombre.", Alert.AlertType.ERROR);
        } catch (Exception e) {
            showAlert("Erreur", "Une erreur s'est produite : " + e.getMessage(), Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
