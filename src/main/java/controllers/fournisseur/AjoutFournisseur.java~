package controllers.fournisseur;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import models.logistics.Fournisseur;
import models.match.SessionManager;
import models.utilisateur.Role;
import models.utilisateur.User;
import services.logistics.FournisseurService;

import java.io.IOException;

public class AjoutFournisseur {

    @FXML
    private TextField nomField, emailField, adresseField;

    @FXML
    private ComboBox<String> comboBoxCategorie;

    @FXML
    private Button btnAjout;


    @FXML
    private Button annulerButton;

    private FournisseurService fournisseurService = new FournisseurService();




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
    public void initialize() {
        User user = SessionManager.getCurrentUser();

        if (nom_user == null) {
            System.out.println("Erreur : nom_user est null !");
        } else if (user != null) {
            afficherProfil(user);
        } else {
            System.out.println("Aucun utilisateur connecté !");
        }
        comboBoxCategorie.getItems().addAll(
                "EQUIPEMENT_SPORTIF",
                "ACCESSOIRE_ENTRAINEMENT",
                "MATERIEL_JEU",
                "TENUE_SPORTIVE",
                "EQUIPEMENT_PROTECTION",
                "INFRASTRUCTURE"
        );
        comboBoxCategorie.setValue("MATERIEL_JEU");
    }

    @FXML
    void ajouter(ActionEvent event) {
        if (nomField.getText().trim().isEmpty() || emailField.getText().trim().isEmpty() ||
                adresseField.getText().trim().isEmpty() || comboBoxCategorie.getValue() == null) {
            showAlert(Alert.AlertType.WARNING, "Champs obligatoires", "Veuillez remplir tous les champs !");
            return;
        }

        String nom = nomField.getText().trim();

        // Check if Fournisseur already exists
        if (fournisseurService.exists(nom)) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Fournisseur déjà existant"
            );
            return;
        }

        Fournisseur newFournisseur = new Fournisseur();
        newFournisseur.setNom(nom);
        newFournisseur.setEmail(emailField.getText().trim());
        newFournisseur.setAdresse(adresseField.getText().trim());
        newFournisseur.setCategorie_produit(comboBoxCategorie.getValue());

        fournisseurService.ajouter(newFournisseur); // Assuming this method exists

        showAlert(Alert.AlertType.CONFIRMATION, "Succès", "Fournisseur ajouté avec succès !");
        clearFields();
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
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Échec du chargement de DisplayFournisseur.fxml : " + e.getMessage());
        }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.show();
    }

    private void clearFields() {
        nomField.clear();
        emailField.clear();
        adresseField.clear();
        comboBoxCategorie.setValue("MATERIEL_JEU");
    }
}