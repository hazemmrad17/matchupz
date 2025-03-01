package controllers.sponsor;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import models.Sponsor;
import models.match.SessionManager;
import models.utilisateur.Role;
import models.utilisateur.User;
import services.SponsorService;

import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.IOException;

public class AjouterSponsor {

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



    @FXML
    private Button annulerButton;

    @FXML
    private TextField nomField;

    @FXML
    private TextField contactField;

    @FXML
    private ComboBox<String> packField;

    @FXML
    private Button ajouterButton;

    private final SponsorService sponsorService = new SponsorService();

    @FXML
    private void handleAnnulerButton() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherSponsor.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) annulerButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de charger la page des sponsors.");
            e.printStackTrace();
        }
    }

    @FXML
    void ajouterSponsor(ActionEvent event) {
        String nom = nomField.getText().trim();
        String contact = contactField.getText().trim();
        String pack = packField.getValue();

        if (nom.isEmpty() || contact.isEmpty() || pack == null) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Veuillez remplir tous les champs.");
            return;
        }

        if (!nom.matches("[a-zA-Z]+")) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Le nom doit contenir uniquement des lettres.");
            return;
        }

        if (!contact.matches("\\d{8}")) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Le contact doit contenir exactement 8 chiffres.");
            return;
        }

        if (sponsorService.isSponsorNameExists(nom)) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Ce sponsor existe déjà.");
            return;
        }

        Sponsor sponsor = new Sponsor(nom, contact, pack);
        sponsorService.ajouter(sponsor);
        showAlert(Alert.AlertType.INFORMATION, "Succès", "Le sponsor a été ajouté avec succès.");
        clearFields();
        handleAnnulerButton();
    }

    @FXML
    public void initialize() {
        packField.setItems(FXCollections.observableArrayList("Bronze", "Silver", "Gold"));
    }

    private void clearFields() {
        nomField.clear();
        contactField.clear();
        packField.setValue(null);
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    public void initializer() {
        ObservableList<String> options = FXCollections.observableArrayList(
                "Bronze", "Silver", "Gold"
        );
        packField.setItems(options);
    }
}
