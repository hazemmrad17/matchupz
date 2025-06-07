package controllers.sponsor;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import models.sponsor.Sponsor;
import models.match.SessionManager;
import models.utilisateur.Role;
import models.utilisateur.User;
import services.sponsor.SponsorService;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ModifierSponsor {

    @FXML
    private Button bt_user;

    @FXML
    private Button dashboard, goTosponsor;

    @FXML
    private Button espace;

    @FXML
    private Button logistique;

    @FXML
    private Label nom_user;

    @FXML
    private Button annulerButton;

    @FXML
    private TextField nomField;

    @FXML
    private TextField contactField;

    @FXML
    private ComboBox<String> packField;

    @FXML
    private Button modifierButton;

    @FXML
    private Button uploadImageButton;

    @FXML
    private ImageView sponsorImageView;

    private Sponsor sponsorToModify;
    private String imagePath; // Store new image path temporarily

    private final SponsorService sponsorService = new SponsorService();

    private void afficherProfil(User user) {
        if (user.getImage() != null && !user.getImage().isEmpty()) {
            Image image = new Image(user.getImage());
            String name = user.getPrenom();
            nom_user.setText(name);
        }
    }

    public void setSponsorToModify(Sponsor sponsor) {
        this.sponsorToModify = sponsor;
        nomField.setText(sponsor.getNom());
        contactField.setText(sponsor.getContact());
        packField.setValue(sponsor.getPack());
        imagePath = sponsor.getSponsor_picture(); // Initialize with existing image
        // Load existing image into ImageView
        if (imagePath != null && !imagePath.isEmpty()) {
            File imageFile = new File("Uploads/sponsors/" + imagePath);
            if (imageFile.exists()) {
                sponsorImageView.setImage(new Image(imageFile.toURI().toString()));
            } else {
                sponsorImageView.setImage(null); // Clear if image file is missing
            }
        } else {
            sponsorImageView.setImage(null);
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
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fournisseur/AfficherFournisseur.fxml"));
            Stage stage = (Stage) logistique.getScene().getWindow();
            stage.setScene(new Scene(loader.load()));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de charger la page d'inscription.");
        }
    }

    @FXML
    void goTosponsor(javafx.event.ActionEvent event) {
        User user = SessionManager.getCurrentUser();
        if (user != null && user.getRole() == Role.ADMIN) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/sponsoring/AfficherSponsor.fxml"));
                Stage stage = (Stage) goTosponsor.getScene().getWindow();
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
    void pageuser(javafx.event.ActionEvent event) {
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
    void teams(javafx.event.ActionEvent event) {
        User user = SessionManager.getCurrentUser();
        if (user != null && user.getRole() == Role.ADMIN) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Team/AfficherTeam.fxml"));
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
    private void handleAnnulerButton() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/sponsoring/AfficherSponsor.fxml"));
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
    void modifier(javafx.event.ActionEvent event) {
        // Validate only nom and contact fields
        if (nomField.getText().trim().isEmpty() || contactField.getText().trim().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Veuillez remplir les champs nom et contact.");
            return;
        }

        try {
            String nom = nomField.getText().trim();
            String contact = contactField.getText().trim();
            String pack = packField.getValue(); // Pack is optional

            // Validate nom (letters only)
            if (!nom.matches("[a-zA-Z]+")) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Le nom doit contenir uniquement des lettres.");
                return;
            }

            // Validate contact (8 digits)
            if (!contact.matches("\\d{8}")) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Le contact doit contenir exactement 8 chiffres.");
                return;
            }

            // Check if sponsor name already exists (excluding current sponsor)
            if (!nom.equals(sponsorToModify.getNom()) && sponsorService.isSponsorNameExists(nom)) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Ce nom de sponsor existe déjà.");
                return;
            }

            // Update sponsor details
            sponsorToModify.setNom(nom);
            sponsorToModify.setContact(contact);
            sponsorToModify.setPack(pack);
            sponsorToModify.setSponsor_picture(imagePath);

            sponsorService.modifier(sponsorToModify);

            showAlert(Alert.AlertType.INFORMATION, "Succès", "Les informations du sponsor ont été modifiées avec succès.");
            handleAnnulerButton();
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Une erreur est survenue : " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleUploadImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choisir une image pour le sponsor");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg")
        );
        Stage stage = (Stage) uploadImageButton.getScene().getWindow();
        File imageFile = fileChooser.showOpenDialog(stage);
        if (imageFile != null) {
            try {
                // Define the directory to save images
                String uploadDir = "Uploads/sponsors/";
                Path uploadPath = Paths.get(uploadDir);
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                // Resize and save the image
                BufferedImage bufferedImage = ImageIO.read(imageFile);
                BufferedImage resizedImage = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
                java.awt.Graphics2D g = resizedImage.createGraphics();
                g.drawImage(bufferedImage, 0, 0, 100, 100, null);
                g.dispose();

                // Generate a unique filename
                String fileName = System.currentTimeMillis() + "_" + imageFile.getName();
                File destinationFile = new File(uploadDir + fileName);
                ImageIO.write(resizedImage, "jpg", destinationFile);

                // Delete the old image if it exists
                if (imagePath != null && !imagePath.isEmpty()) {
                    File oldImage = new File(uploadDir + imagePath);
                    if (oldImage.exists()) {
                        oldImage.delete();
                    }
                }

                // Update imagePath and ImageView
                imagePath = fileName;
                sponsorImageView.setImage(new Image(destinationFile.toURI().toString()));
                showAlert(Alert.AlertType.INFORMATION, "Succès", "Image du sponsor téléchargée avec succès.");
            } catch (IOException e) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Échec du téléchargement de l'image.");
                e.printStackTrace();
            }
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

        packField.setItems(FXCollections.observableArrayList("Bronze", "Silver", "Gold"));


        // Add border to ImageView for placeholder
        sponsorImageView.setStyle("-fx-border-color: #ccc; -fx-border-width: 1px;");
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}