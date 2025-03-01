package controllers.joueur;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import models.joueur.Club;
import models.joueur.HistoriqueClub; // Assuming this is the correct class name
import models.joueur.Joueur;
import models.match.SessionManager;
import models.utilisateur.Role;
import models.utilisateur.User;
import services.joueur.ClubService;
import services.joueur.HistoriqueClubService;
import services.joueur.JoueurService;

import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneId;

public class AjoutHistorique {
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

    @FXML private Button Home;
    @FXML private Button ajoutHistoriqueButton;
    @FXML private Button annulerButton;
    @FXML private ComboBox<String> idJoueurComboBox;
    @FXML private ComboBox<String> idClubComboBox;
    @FXML private DatePicker saisonDebutPicker;
    @FXML private DatePicker saisonFinPicker;

    private HistoriqueClubService historiqueService = new HistoriqueClubService();
    private JoueurService joueurService = new JoueurService();
    private ClubService clubService = new ClubService();

    @FXML
    private void initialize() {
        idJoueurComboBox.setItems(FXCollections.observableArrayList(
                joueurService.recherche().stream().map(j -> j.getIdJoueur() + " - " + j.getNom() + " " + j.getPrenom()).toList()
        ));
        idClubComboBox.setItems(FXCollections.observableArrayList(
                clubService.recherche().stream().map(c -> c.getIdClub() + " - " + c.getNomClub()).toList()
        ));
    }

    @FXML
    private void handleHome() {
        loadScene("/joueur/MainController.fxml", Home);
    }

    @FXML
    private void handleAnnulerButton() {
        loadScene("/joueur/DisplayHistorique.fxml", annulerButton);
    }

    @FXML
    private void ajouter() {
        if (idJoueurComboBox.getValue() == null || idClubComboBox.getValue() == null || saisonDebutPicker.getValue() == null) {
            showAlert("Erreur", "Champs obligatoires manquants", "Veuillez remplir tous les champs obligatoires.");
            return;
        }

        try {
            int idJoueur = Integer.parseInt(idJoueurComboBox.getValue().split(" - ")[0]);
            int idClub = Integer.parseInt(idClubComboBox.getValue().split(" - ")[0]);
            LocalDate saisonDebutLocal = saisonDebutPicker.getValue();
            Date saisonDebut = Date.valueOf(saisonDebutLocal); // Convert to java.sql.Date
            LocalDate saisonFinLocal = saisonFinPicker.getValue();
            Date saisonFin = saisonFinLocal != null ? Date.valueOf(saisonFinLocal) : null; // Convert to java.sql.Date

            HistoriqueClub historique = new HistoriqueClub(idJoueur, idClub, saisonDebut, saisonFin);
            historiqueService.ajouter(historique);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Succès");
            alert.setHeaderText("Historique ajouté avec succès !");
            alert.setContentText("L'historique pour le joueur " + idJoueur + " et le club " + idClub + " a été ajouté.");
            alert.showAndWait();

            handleAnnulerButton();
        } catch (NumberFormatException e) {
            showAlert("Erreur", "Valeur incorrecte", "Veuillez sélectionner des valeurs valides pour le joueur et le club.");
        } catch (Exception e) {
            showAlert("Erreur", "Une erreur est survenue", "Détails : " + e.getMessage());
        }
    }

    private void showAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}