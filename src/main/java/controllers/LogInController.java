

package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import models.SessionManager;
import models.User;

import java.io.IOException;

public class LogInController {
    @FXML
    private Button Current_User;

    @FXML
    private Button button_logout;
    @FXML
    private ImageView profileImageView;




    @FXML
    public void initialize() {
        User user = SessionManager.getCurrentUser(); // Récupérer l'utilisateur connecté
        if (user != null) {
            afficherProfil(user);
        } else {
            System.out.println("Aucun utilisateur connecté !");
        }
    }

    @FXML
    private void CurrentUser(ActionEvent event) {
        // Code à exécuter
    }
    @FXML
    void Update(MouseEvent event) {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/updateUtilisateur.fxml"));
            Parent parent = loader.load();


            UpdateUtilisateurController controller = loader.getController();


            User user = SessionManager.getCurrentUser();
            if (user != null) {
                controller.initData(user);
            }

            Stage stage = new Stage();
            stage.setScene(new Scene(parent));
            stage.initStyle(StageStyle.UTILITY);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de charger la page de modification du profil.");
        }
    }


   /* @FXML
    void Update(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/updateUtilisateur.fxml"));
            Scene newScene = new Scene(loader.load());

            // Récupérer le contrôleur de la nouvelle scène
            UpdateUtilisateurController controller = loader.getController();

            // Passer les informations de l'utilisateur connecté
            User user = SessionManager.getCurrentUser();
            if (user != null) {
                controller.setUserData(user);
            }

            // Changer la scène
            Stage stage = (Stage) profileImageView.getScene().getWindow();
            stage.setScene(newScene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de charger la page de modification du profil.");
        }
        /*try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/updateUtilisateur.fxml"));
            Stage stage = (Stage) profileImageView.getScene().getWindow();
            stage.setScene(new Scene(loader.load()));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de charger la page de modification du profil.");
        }*/


    private void afficherProfil(User user) {
     //   tfnom.setText(user.getNom());  // Affiche le nom de l'utilisateur
        if (user.getImage() != null && !user.getImage().isEmpty()) {
            Image image = new Image(user.getImage());
            profileImageView.setImage(image);
        }
    }

   /* @FXML
    public void initialize() {
        User user = SessionManager.getCurrentUser(); // Récupérer l'utilisateur connecté
        if (user != null) {
            affichage(user);
        } else {
            System.out.println("Aucun utilisateur connecté !");
        }
    }*/

   /* @FXML
    public void initialize(User user) {
        System.out.println(user.getNom());
        // Cette méthode est appelée après le chargement du FXML
        System.out.println("LogInController initialisé");
    }*/
 /*   @FXML
    private Label label_welcome;
    public void affichage(String nom) {

        tfnom.setText(nom);
       // label_welcome.setText("Bienvenue, " + user.getNom() + " !");
    }
    private User user;
    public void setUser(User user) {
        this.user = user;
    }*/
/*
    @FXML
    void CurrentUser(ActionEvent event) {
        User user = SessionManager.getCurrentUser();
        if (user != null) {
            tfnom.setText(user.getNom()); // Afficher le nom de l'utilisateur connecté
        } else {
            showAlert(Alert.AlertType.WARNING, "Attention", "Aucun utilisateur connecté !");
        }

    }*/

    /*public void affichage(User user) {

        tfnom.setText(user.getNom());
        label_welcome.setText("Bienvenue, " + user.getNom());


    }
*/
    @FXML
    void deconnection(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/interfaceA.fxml"));
            Stage stage = (Stage) button_logout.getScene().getWindow();
            stage.setScene(new Scene(loader.load()));
            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de charger la page d'inscription.");
        }

    }

    private void showAlert(Alert.AlertType alertType, String erreur, String s) {
    }

}
