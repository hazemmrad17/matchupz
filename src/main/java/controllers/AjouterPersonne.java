package controllers;

import models.Personne;
import services.PersonneService;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class AjouterPersonne {

    @FXML
    private TextField textFieldNom;

    @FXML
    private TextField textFieldPrenom;

    @FXML
    private TextField textFieldAge; // Ajout du champ Age

    @FXML
    private Button btnAjout;

    @FXML
    private Button btnAnnuler;

    private PersonneService personneService = new PersonneService();

    @FXML
    public void initialize() {
        // Initialisation des actions des boutons
        btnAjout.setOnAction(event -> handleSubmit());
        btnAnnuler.setOnAction(event -> handleCancel());
    }

    private void handleSubmit() {
        String nom = textFieldNom.getText().trim();
        String prenom = textFieldPrenom.getText().trim();
        String ageStr = textFieldAge.getText().trim();

        // Vérification que les champs ne sont pas vides
        if (nom.isEmpty() || prenom.isEmpty() || ageStr.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Champs vides", "Veuillez remplir tous les champs.");
            return;
        }

        int age;
        try {
            age = Integer.parseInt(ageStr);
            if (age <= 0) {
                showAlert(Alert.AlertType.ERROR, "Âge invalide", "L'âge doit être un nombre positif.");
                return;
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur de format", "L'âge doit être un nombre valide.");
            return;
        }

        // Création et insertion de la personne
        Personne personne = new Personne(nom, prenom, age);
        personneService.ajouter(personne);

        // Affichage de la confirmation
        showAlert(Alert.AlertType.INFORMATION, "Succès", "Personne ajoutée avec succès !");
        System.out.println("Personne ajoutée: " + nom + " " + prenom + ", Âge: " + age);

        // Réinitialisation des champs après soumission
        textFieldNom.clear();
        textFieldPrenom.clear();
        textFieldAge.clear();
    }

    private void handleCancel() {
        // Réinitialisation des champs en cas d'annulation
        textFieldNom.clear();
        textFieldPrenom.clear();
        textFieldAge.clear();
        System.out.println("Opération annulée.");
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
