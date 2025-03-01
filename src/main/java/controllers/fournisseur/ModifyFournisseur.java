package controllers.fournisseur;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import models.logistics.Fournisseur;
import models.match.SessionManager;
import models.utilisateur.Role;
import models.utilisateur.User;
import services.logistics.FournisseurService;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.scene.control.*;

import java.io.IOException;

public class ModifyFournisseur {


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

    private void showAlert(Alert.AlertType alertType, String erreur, String s) {
    }


    @FXML
    private TextField nomField;

    @FXML
    private TextField emailField;

    @FXML
    private TextField adresseField;

    @FXML
    private ComboBox<String> comboBoxCategorie;

    @FXML
    private Button modifierButton;

    @FXML
    private Button annulerButton;

    @FXML
    private Button fournisseurButton;

    @FXML
    private Button Home;

    private Fournisseur fournisseurToModify;
    @FXML
    public void initialize() {
        User user = SessionManager.getCurrentUser();

        if (nom_user == null) {
            System.out.println("Erreur : nom_user est null !");
        } else if (user != null) {
            afficherProfil(user);
        } else {
            System.out.println("Aucun utilisateur connecté !");
        }
        System.out.println("✅ ModifyFournisseur : initialize() exécuté !");
        System.out.println("🔍 comboBoxCategorie = " + comboBoxCategorie);
    }
    public void setFournisseurToModify(Fournisseur fournisseur) {
        if (comboBoxCategorie == null) {
            System.out.println("⚠️ ERREUR : setFournisseurToModify() appelé AVANT initialize() !");
            return;
        }
        this.fournisseurToModify = fournisseur;
        nomField.setText(fournisseur.getNom());
        emailField.setText(fournisseur.getEmail());
        adresseField.setText(fournisseur.getAdresse());
        comboBoxCategorie.setValue(fournisseur.getCategorie_produit());
        System.out.println("✅ Fournisseur chargé : " + fournisseur.getNom());
    }

    @FXML
    private void handleHome() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Home.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) Home.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            showError("Erreur de chargement", "Impossible de charger Home.fxml", e);
        }
    }

    @FXML
    private void handleFournisseur() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fournisseur/DisplayFournisseur.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) fournisseurButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            showError("Erreur de chargement", "Impossible de charger DisplayFournisseur.fxml", e);
        }
    }

    @FXML
    private void handleAnnulerButton() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fournisseur/DisplayFournisseur.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) annulerButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            showError("Erreur de chargement", "Impossible de charger DisplayFournisseur.fxml", e);
        }
    }

    @FXML
    void modifier(ActionEvent event) {
        if (nomField.getText().trim().isEmpty() ||
                emailField.getText().trim().isEmpty() ||
                adresseField.getText().trim().isEmpty() ||
                comboBoxCategorie.getValue().trim().isEmpty()) {

            showAlert(Alert.AlertType.ERROR, "Erreur", "Champs obligatoires manquants",
                    "Veuillez remplir tous les champs avant de modifier un fournisseur.");
            return;
        }

        try {
            String nom = nomField.getText().trim();
            String email = emailField.getText().trim();
            String adresse = adresseField.getText().trim();
            String categorieProduit = comboBoxCategorie.getValue().trim();

            fournisseurToModify.setNom(nom);
            fournisseurToModify.setEmail(email);
            fournisseurToModify.setAdresse(adresse);
            fournisseurToModify.setCategorie_produit(categorieProduit);

            FournisseurService fournisseurService = new FournisseurService();
            fournisseurService.modifier(fournisseurToModify);

            showAlert(Alert.AlertType.INFORMATION, "Succès", "Modification réussie !",
                    "Les informations du fournisseur ont été modifiées avec succès.");

        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Une erreur est survenue", "Détails : " + e.getMessage());
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String header, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void showError(String title, String header, Exception e) {
        e.printStackTrace();
        showAlert(Alert.AlertType.ERROR, title, header, "Détails : " + e.getMessage());
    }
}
