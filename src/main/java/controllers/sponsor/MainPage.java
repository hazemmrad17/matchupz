package controllers.sponsor;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import models.match.SessionManager;
import models.utilisateur.User;

import java.io.IOException;

public class MainPage {

    @FXML
    private Button bt_user;

    @FXML
    private Button dashboard;

    @FXML
    private Button espace;

    @FXML
    private AnchorPane home_form;

    @FXML
    private Button logistique;

    @FXML
    private Button logout;

    @FXML
    private Button logout2;

    @FXML
    private AnchorPane main_form;

    @FXML
    private Button match;

    @FXML
    private Button sponsor;

    @FXML
    private Button teams;

    @FXML
    void dashboard(ActionEvent event) {
        // Add dashboard logic here if needed
    }

    @FXML
    void espace(ActionEvent event) {
        // Add espace logic here if needed
    }

    @FXML
    void loadAfficherContrat(ActionEvent event) {
        loadScene("/sponsoring/AfficherContrat.fxml", (Button) event.getSource());
    }

    @FXML
    void loadAfficherSponsor(ActionEvent event) {
        loadScene("/sponsoring/AfficherSponsor.fxml", (Button) event.getSource());
    }


    @FXML
    void logistique(ActionEvent event) {
        loadScene("/fournisseur/DisplayFournisseur.fxml", logistique);
    }

    @FXML
    void match(ActionEvent event) {
        // Add match logic here if needed
    }

    @FXML
    void pageuser(ActionEvent event) {
        loadScene("/user/adminpage.fxml", bt_user);
    }

    @FXML
    void sponsor(ActionEvent event) {
        // Add sponsor logic here if needed
    }

    @FXML
    void teams(ActionEvent event) {
        loadScene("/joueur/MainController.fxml", teams);
    }


    // Helper method to load scenes
    private void loadScene(String fxmlPath, Button button) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            Stage stage = (Stage) button.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de charger la page: " + e.getMessage());
        }
    }
    @FXML
    private Label nom_user;
    // Helper method to show alerts
    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
    private void afficherProfil(User user) {
        if (user.getImage() != null && !user.getImage().isEmpty()) {
            Image image = new Image(user.getImage());
            String name = user.getPrenom();
            nom_user.setText(name);
        }
    }

    @FXML
    public void initialize() {

        User user = SessionManager.getCurrentUser();
        if (nom_user == null) {
            System.out.println("Erreur : nom_user est null !");
        } else if (user != null) {
            afficherProfil(user);
        } else {
            System.out.println("Aucun utilisateur connecté !");
        }}
}