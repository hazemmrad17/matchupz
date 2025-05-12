package controllers.joueur;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import models.joueur.Club;
import models.match.SessionManager;
import models.utilisateur.Role;
import models.utilisateur.User;
import services.joueur.ClubService;

import java.io.File;
import java.io.IOException;

public class ModifyClub {
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

    @FXML private Button homeButton;
    @FXML private TextField nomField;
    @FXML private TextField photoField;
    @FXML private Button selectPhotoButton;
    @FXML private Button modifierButton;
    @FXML private Button annulerButton;

    private Club clubToModify;
    private ClubService clubService = new ClubService();



    private void loadScene(String fxmlPath, Button button) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            Stage stage = (Stage) button.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            showAlert("Erreur", "Échec du chargement", e.getMessage());
        }
    }

    @FXML
    private void handleAnnulerButton() {
        loadScene("/joueur/DisplayClub.fxml", annulerButton);
    }

    @FXML
    private void handleHome() {
        loadScene("/Home.fxml", homeButton);
    }

    public void setClubToModify(Club club) {
        this.clubToModify = club;
        nomField.setText(club.getNomClub());
        photoField.setText(club.getPhotoUrl());
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

    @FXML
    private void modifier() {
        String nom = nomField.getText().trim();
        String photoUrl = photoField.getText().trim();

        if (nom.isEmpty()) {
            showAlert("Erreur", "Nom requis", "Le nom du club ne peut pas être vide.");
            return;
        }

        clubToModify.setNomClub(nom);
        clubToModify.setPhotoUrl(photoUrl.isEmpty() ? null : photoUrl);
        clubService.modifier(clubToModify);
        handleAnnulerButton();
    }




    private void showAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}