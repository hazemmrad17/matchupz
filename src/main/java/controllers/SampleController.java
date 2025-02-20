/*package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import models.User;
import services.UserService;

import java.io.IOException;

public class SampleController {

    @FXML
    private Button button_login;

    @FXML
    private Button button_signup;

    @FXML
    private TextField tf_email;

    @FXML
    private TextField tf_mot_de_passe;

    private final UserService userService = new UserService();

    @FXML
    void seconnecter(ActionEvent event) {
        String email = tf_email.getText();
        String password = tf_mot_de_passe.getText();

        if (email.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Attention", "Veuillez remplir tous les champs !");
            return;
        }

        User user = userService.verifierUtilisateur(email, password);

        if (user != null) {
            showAlert(Alert.AlertType.INFORMATION, "Connexion réussie", "Bienvenue " + user.getNom() + " !");
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/log-in.fxml"));
                Stage stage = (Stage) button_login.getScene().getWindow();
                Scene scene = new Scene(loader.load());

                // Récupérer le contrôleur et lui passer l'utilisateur connecté
              //  LogInController logInController = loader.getController();
              //  logInController.setUser(user);
               // LogInController logInController = loader.getController();
               // logInController.initUserData(user);

                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de charger la page de connexion.");
            }
        } else {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Utilisateur non trouvé. Vérifiez vos identifiants !");
        }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.show();
    }
}
*/





/*

package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import models.SessionManager;
import models.User;
import services.UserService;
import java.io.IOException;

public class SampleController {



    @FXML
    private Button button_login;
    @FXML
    private Button button_signup;
    @FXML
    private TextField tf_email;
    @FXML
    private TextField tf_mot_de_passe;

    private final UserService userService = new UserService(); // Service pour interagir avec la BD

    @FXML
    void seconnecter(ActionEvent event) {
        String email = tf_email.getText();
        String password = tf_mot_de_passe.getText();

        // Vérifier si les champs sont remplis
        if (email.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Attention", "Veuillez remplir tous les champs !");
            return;
        }

        // Vérifier si l'utilisateur existe dans la base
        User user = userService.verifierUtilisateur(email, password);
      //  SessionManager.setCurrentUser(user);
        if (user != null) {

            // Succès : Rediriger vers log-in.fxml
            showAlert(Alert.AlertType.INFORMATION, "Connexion réussie", "Bienvenue " + user.getNom() + " !");
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/log-in.fxml"));
                Stage stage = (Stage) button_login.getScene().getWindow();


                // Charger la scène d'abord
                Scene scene = new Scene(loader.load());
                LogInController logInController = loader.getController();
                //logInController.affichage(tf_email.getText());
                System.out.println(tf_email.getText());
                //LogInController logInController = loader.getController();
                //logInController.setUser(user);

                // Récupérer le contrôleur après avoir chargé la scène
              /*  LogInController logInController = loader.getController();
                logInController.initialize(user);
                logInController.tfnom.setText(user.getNom());*/
                // Passer l'objet User au contrôleur
/*
                stage.setScene(scene);
                stage.show();
            }
            /*try {

                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/log-in.fxml"));
                Stage stage = (Stage) button_login.getScene().getWindow();
                LogInController logInController = loader.getController();
                logInController.affichage(user);
                Scene scene = new Scene(loader.load());

                // Pass the user object to the next page
             //  LogInController logInController = loader.getController();
             //  logInController.setUser(user);

                stage.setScene(scene);
                stage.show();
            }*/ /*catch (IOException e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de charger la page de connexion.");
            }
        } else {
            // Échec : Afficher une erreur
            showAlert(Alert.AlertType.ERROR, "Erreur", "Utilisateur non trouvé. Vérifiez vos identifiants !");
        }
    }

*/
/*
    @FXML
    void seconnecter(ActionEvent event) {
        String email = tf_email.getText();
        String password = tf_mot_de_passe.getText();

        // Vérifier si les champs sont remplis
        if (email.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Attention", "Veuillez remplir tous les champs !");
            return;
        }

        // Vérifier si l'utilisateur existe dans la base
        User user = userService.verifierUtilisateur(email, password);

        if (user != null) {
            // Succès : Rediriger vers log-in.fxml
            showAlert(Alert.AlertType.INFORMATION, "Connexion réussie", "Bienvenue " + user.getNom() + " !");

            redirigerVersLogin();
        } else {
            // Échec : Afficher une erreur
            showAlert(Alert.AlertType.ERROR, "Erreur", "Utilisateur non trouvé. Vérifiez vos identifiants !");
        }
    }

    private void redirigerVersLogin() {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/log-in.fxml"));
            Stage stage = (Stage) button_login.getScene().getWindow();
            stage.setScene(new Scene(loader.load()));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de charger la page de connexion.");
        }
    }
*/
  /* // Méthode pour afficher des alertes
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.show();
    }
}
*/