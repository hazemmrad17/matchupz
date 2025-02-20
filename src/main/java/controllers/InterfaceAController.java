package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import models.Role;
import models.SessionManager;
import models.User;
import services.UserService;
import java.io.IOException;

public class InterfaceAController {
    @FXML
    private ImageView image;

    @FXML
    private Button button_login;
    @FXML
    private TextField tf_email;
    @FXML
    private Button button_signup;
    @FXML
    private TextField tf_mot_de_passe;

    private final UserService userService = new UserService();

    @FXML
    private void initialize() {
        image.setImage(new Image("file:/C:/Users/MSI/Desktop/image.png"));
        
    }
    @FXML
    void sinscrire(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/sign-up.fxml"));
            Stage stage = (Stage) button_signup.getScene().getWindow();
            stage.setScene(new Scene(loader.load()));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de charger la page d'inscription.");
        }
    }
    @FXML
    void seconnecter(ActionEvent event) {
        String email = tf_email.getText();
        String password = tf_mot_de_passe.getText();
        User user1 = userService.verifierUtilisateur(email, password);
        System.out.println("Résultat de la vérification : " + user1);

        // Vérifier si les champs sont remplis
        if (email.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Attention", "Veuillez remplir tous les champs !");
            return;
        }

        // Vérifier si l'utilisateur existe dans la base
        User user = userService.verifierUtilisateur(email, password);

        if (user != null) {
            SessionManager.setCurrentUser(user);  // Sauvegarde de l'utilisateur connecté
            showAlert(Alert.AlertType.INFORMATION, "Connexion réussie", "Bienvenue " + user.getNom() + " !");
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/log-in.fxml"));
                Stage stage = (Stage) button_login.getScene().getWindow();
                stage.setScene(new Scene(loader.load()));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de charger la page de connexion.");
            }
        } else {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Utilisateur non trouvé. Vérifiez vos identifiants !");
        }
       /* if (user != null) {
            // Succès : Rediriger vers log-in.fxml
            showAlert(Alert.AlertType.INFORMATION, "Connexion réussie", "Bienvenue " + user.getNom() + " !");

            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/log-in.fxml"));
                Stage stage = (Stage) button_login.getScene().getWindow();
                stage.setScene(new Scene(loader.load()));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de charger la page de connexion.");
            }

        } else {
            // Échec : Afficher une erreur
            showAlert(Alert.AlertType.ERROR, "Erreur", "Utilisateur non trouvé. Vérifiez vos identifiants !");
        }*/
    }
    //redirigerVersLogin();
   /* private void redirigerVersLogin() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/log-in.fxml"));
            Stage stage = (Stage) button_login.getScene().getWindow();
            stage.setScene(new Scene(loader.load()));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de charger la page de connexion.");
        }
    }*/


    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.show();
    }
}

