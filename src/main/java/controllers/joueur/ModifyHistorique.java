package controllers.joueur;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import models.joueur.HistoriqueClub;
import models.joueur.Joueur;
import models.match.SessionManager;
import models.utilisateur.Role;
import models.utilisateur.User;
import services.joueur.HistoriqueClubService;
import services.joueur.JoueurService;

import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;

public class ModifyHistorique {
    @FXML private Button bt_user;
    @FXML private Button dashboard;
    @FXML private Button espace;
    @FXML private Button logistique;
    @FXML private Button sponsor;
    @FXML private Button match;
    @FXML private Button teams;
    @FXML private Button log_out;
    @FXML private Label nom_user;
    @FXML private Button home;
    @FXML private Button modifierHistoriqueButton;
    @FXML private Button annulerButton;
    @FXML private ComboBox<String> idJoueurComboBox;
    @FXML private TextField nomClubField;
    @FXML private DatePicker saisonDebutPicker;
    @FXML private DatePicker saisonFinPicker;

    private HistoriqueClubService historiqueService = new HistoriqueClubService();
    private JoueurService joueurService = new JoueurService();
    private HistoriqueClub historiqueToModify;

    @FXML
    private void initialize() {
        User user = SessionManager.getCurrentUser();
        if (user != null) {
            afficherProfil(user);
        } else {
            System.out.println("Aucun utilisateur connecté !");
        }

        idJoueurComboBox.setItems(FXCollections.observableArrayList(
                joueurService.recherche().stream()
                        .map(j -> j.getIdJoueur() + " - " + j.getNom() + " " + j.getPrenom())
                        .toList()
        ));
    }

    private void afficherProfil(User user) {
        if (user.getImage() != null && !user.getImage().isEmpty()) {
            String name = user.getPrenom();
            nom_user.setText(name);
        }
    }

    public void setHistoriqueToModify(HistoriqueClub historique) {
        this.historiqueToModify = historique;
        idJoueurComboBox.setValue(historique.getIdJoueur() + " - " + joueurService.recherche().stream()
                .filter(j -> j.getIdJoueur() == historique.getIdJoueur())
                .map(j -> j.getNom() + " " + j.getPrenom())
                .findFirst().orElse(""));
        nomClubField.setText(historique.getNomClub());
        saisonDebutPicker.setValue(historique.getSaisonDebut().toLocalDate());
        saisonFinPicker.setValue(historique.getSaisonFin() != null ? historique.getSaisonFin().toLocalDate() : null);
    }

    @FXML
    private void handleHome(ActionEvent event) {
        loadScene("/joueur/MainController.fxml", home);
    }

    @FXML
    private void handleAnnulerButton(ActionEvent event) {
        loadScene("/joueur/DisplayHistorique.fxml", annulerButton);
    }

    @FXML
    private void modifier(ActionEvent event) {
        if (idJoueurComboBox.getValue() == null || nomClubField.getText().trim().isEmpty() || saisonDebutPicker.getValue() == null) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Veuillez remplir tous les champs obligatoires.");
            return;
        }

        try {
            int idJoueur = Integer.parseInt(idJoueurComboBox.getValue().split(" - ")[0]);
            String nomClub = nomClubField.getText().trim();
            LocalDate saisonDebutLocal = saisonDebutPicker.getValue();
            Date saisonDebut = Date.valueOf(saisonDebutLocal);
            LocalDate saisonFinLocal = saisonFinPicker.getValue();
            Date saisonFin = saisonFinLocal != null ? Date.valueOf(saisonFinLocal) : null;

            historiqueToModify.setIdJoueur(idJoueur);
            historiqueToModify.setNomClub(nomClub);
            historiqueToModify.setSaisonDebut(saisonDebut);
            historiqueToModify.setSaisonFin(saisonFin);

            historiqueService.modifier(historiqueToModify);

            showAlert(Alert.AlertType.INFORMATION, "Succès", "Historique modifié avec succès pour le joueur " + idJoueur + " et le club " + nomClub + ".");
            handleAnnulerButton(null);
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Veuillez sélectionner un joueur valide.");
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Une erreur est survenue : " + e.getMessage());
        }
    }

    @FXML
    void user(ActionEvent event) {
        User user = SessionManager.getCurrentUser();
        if (user != null && user.getRole() == Role.ADMIN) {
            loadScene("/user/adminpage.fxml", bt_user);
        } else {
            System.out.println("Aucun utilisateur connecté ou pas ADMIN !");
        }
    }

    @FXML
    void dashboard(ActionEvent event) {
        loadScene("/Dashboard.fxml", dashboard);
    }

    @FXML
    void espace(ActionEvent event) {
        User user = SessionManager.getCurrentUser();
        if (user != null && user.getRole() == Role.ADMIN) {
            loadScene("/EspaceSportif/AffichageEspace.fxml", espace);
        } else {
            System.out.println("Aucun utilisateur connecté ou pas ADMIN !");
        }
    }

    @FXML
    void logistique(ActionEvent event) {
        User user = SessionManager.getCurrentUser();
        if (user != null && user.getRole() == Role.ADMIN) {
            loadScene("/fournisseur/AfficherFournisseur.fxml", logistique);
        } else {
            System.out.println("Aucun utilisateur connecté ou pas ADMIN !");
        }
    }

    @FXML
    void sponsor(ActionEvent event) {
        User user = SessionManager.getCurrentUser();
        if (user != null && user.getRole() == Role.ADMIN) {
            loadScene("/sponsoring/AfficherSponsor.fxml", sponsor);
        } else {
            System.out.println("Aucun utilisateur connecté ou pas ADMIN !");
        }
    }

    @FXML
    void match(ActionEvent event) {
        User user = SessionManager.getCurrentUser();
        if (user != null && user.getRole() == Role.ADMIN) {
            loadScene("/MatchSchedule/AffichageMatch.fxml", match);
        } else {
            System.out.println("Aucun utilisateur connecté ou pas ADMIN !");
        }
    }

    @FXML
    void teams(ActionEvent event) {
        loadScene("/joueur/MainController.fxml", teams);
    }

    @FXML
    void log_out(ActionEvent event) {
        loadScene("/user/interfaceA.fxml", log_out);
    }

    private void loadScene(String fxmlPath, Button button) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            Stage stage = (Stage) button.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors du chargement : " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}