package controllers.joueur;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import models.joueur.Joueur;
import models.match.SessionManager;
import models.utilisateur.Role;
import models.utilisateur.User;
import services.joueur.JoueurService;
import services.joueur.SportService;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class ModifyJoueur {
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
    @FXML private Button annulerButton;
    @FXML private Button modifierButton;
    @FXML private Button selectPhotoButton;
    @FXML private TextField nomField;
    @FXML private TextField prenomField;
    @FXML private DatePicker dateNaissancePicker;
    @FXML private ComboBox<String> posteComboBox;
    @FXML private TextField tailleField;
    @FXML private TextField poidsField;
    @FXML private ComboBox<String> statutComboBox;
    @FXML private TextField emailField;
    @FXML private TextField telephoneField;
    @FXML private ComboBox<String> sportComboBox;
    @FXML private TextField profilePictureField;

    private JoueurService joueurService = new JoueurService();
    private SportService sportService = new SportService();
    private Joueur joueurToModify;

    @FXML
    private void initialize() {
        sportComboBox.setItems(FXCollections.observableArrayList(
                sportService.recherche().stream().map(s -> s.getIdSport() + " - " + s.getNomSport()).toList()
        ));
        posteComboBox.setItems(FXCollections.observableArrayList(
                "GK", "RB", "LB", "RWB", "LWB", "SW", "DM", "CM", "AM", "RM", "LM", "RW", "LW", "CF", "ST", "SS"
        ));
        statutComboBox.setItems(FXCollections.observableArrayList("Actif", "Blessé", "Suspendu", ""));
    }

    public void setJoueurToModify(Joueur joueur) {
        this.joueurToModify = joueur;
        nomField.setText(joueur.getNom());
        prenomField.setText(joueur.getPrenom());
        // Convert java.util.Date to LocalDate safely
        LocalDate localDate = new java.sql.Date(joueur.getDateNaissance().getTime()).toLocalDate();
        dateNaissancePicker.setValue(localDate);
        posteComboBox.setValue(joueur.getPoste());
        tailleField.setText(String.valueOf(joueur.getTaille()));
        poidsField.setText(String.valueOf(joueur.getPoids()));
        statutComboBox.setValue(joueur.getStatut());
        emailField.setText(joueur.getEmail());
        telephoneField.setText(joueur.getTelephone());
        sportComboBox.setValue(joueur.getIdSport() + " - " + joueur.getNomSport());
        profilePictureField.setText(joueur.getProfilePictureUrl() != null ? joueur.getProfilePictureUrl() : "");
    }

    @FXML
    private void handleHome() {
        loadScene("/joueur/MainController.fxml", homeButton);
    }

    @FXML
    private void handleAnnulerButton() {
        loadScene("/joueur/DisplayJoueur.fxml", annulerButton);
    }

    @FXML
    private void selectPhoto() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choisir une Photo de Profil");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg")
        );
        File selectedFile = fileChooser.showOpenDialog(modifierButton.getScene().getWindow());
        if (selectedFile != null) {
            profilePictureField.setText(selectedFile.getAbsolutePath());
        }
    }

    @FXML
    private void modifier() {
        if (nomField.getText().trim().isEmpty() || prenomField.getText().trim().isEmpty() || dateNaissancePicker.getValue() == null ||
                sportComboBox.getValue() == null || posteComboBox.getValue() == null || tailleField.getText().trim().isEmpty() ||
                poidsField.getText().trim().isEmpty() || statutComboBox.getValue() == null || emailField.getText().trim().isEmpty() ||
                telephoneField.getText().trim().isEmpty()) {

            showAlert("Erreur", "Champs obligatoires manquants", "Veuillez remplir tous les champs.");
            return;
        }

        try {
            String nom = nomField.getText().trim();
            String prenom = prenomField.getText().trim();
            LocalDate dateNaissanceLocal = dateNaissancePicker.getValue();
            java.util.Date dateNaissance = java.util.Date.from(dateNaissanceLocal.atStartOfDay(ZoneId.systemDefault()).toInstant());
            String poste = posteComboBox.getValue();
            float taille = Float.parseFloat(tailleField.getText().trim());
            float poids = Float.parseFloat(poidsField.getText().trim());
            String statut = statutComboBox.getValue();
            String email = emailField.getText().trim();
            String telephone = telephoneField.getText().trim();
            int idSport = Integer.parseInt(sportComboBox.getValue().split(" - ")[0]);
            String nomSport = sportComboBox.getValue().split(" - ")[1];
            String profilePicturePath = profilePictureField.getText().trim().isEmpty() ? null : profilePictureField.getText().trim();

            if (taille < 1.50 || taille > 2.50) throw new NumberFormatException("Taille must be between 1.50m and 2.50m");
            if (poids < 60 || poids > 110) throw new NumberFormatException("Poids must be between 60kg and 110kg");
            if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) throw new IllegalArgumentException("Invalid email format");
            if (!telephone.matches("^\\+?[1-9][0-9]{1,14}$")) throw new IllegalArgumentException("Invalid phone format");

            joueurToModify.setIdSport(idSport);
            joueurToModify.setNomSport(nomSport);
            joueurToModify.setNom(nom);
            joueurToModify.setPrenom(prenom);
            joueurToModify.setDateNaissance(dateNaissance);
            joueurToModify.setPoste(poste);
            joueurToModify.setTaille(taille);
            joueurToModify.setPoids(poids);
            joueurToModify.setStatut(statut);
            joueurToModify.setEmail(email);
            joueurToModify.setTelephone(telephone);
            joueurToModify.setProfilePictureUrl(profilePicturePath);

            joueurService.modifier(joueurToModify);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Succès");
            alert.setHeaderText("Joueur modifié avec succès !");
            alert.setContentText("Le joueur " + nom + " " + prenom + " a été modifié.");
            alert.showAndWait();

            handleAnnulerButton();
        } catch (NumberFormatException e) {
            showAlert("Erreur", "Valeur incorrecte", e.getMessage() != null ? e.getMessage() : "Veuillez entrer des valeurs numériques valides.");
        } catch (IllegalArgumentException e) {
            showAlert("Erreur", "Entrée invalide", e.getMessage());
        } catch (Exception e) {
            showAlert("Erreur", "Une erreur est survenue", e.getMessage());
        }
    }

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

    private void showAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}