package controllers.user;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import models.match.SessionManager;
import models.utilisateur.Role;
import models.utilisateur.User;
import services.user.UserService;

import java.io.File;
import java.io.IOException;
import java.util.regex.Pattern;

public class ModifyUserController {

    @FXML
    private Button log_out;
    @FXML
    private Button button_annuler_user;

    @FXML
    private Button button_modifier_user;

    @FXML
    private ComboBox<String> combo_role;

    @FXML
    private DatePicker date_date_naiss;

    @FXML
    private AnchorPane home_form;
    @FXML
    private Button bt_user;
    @FXML
    private Button logout;

    @FXML
    private AnchorPane main_form;

    @FXML
    private RadioButton rb_femme;

    @FXML
    private RadioButton rb_homme;

    @FXML
    private TextField tf_email;

    @FXML
    private TextField tf_image;

    @FXML
    private TextField tf_mot_de_passe;

    @FXML
    private TextField tf_nom;

    @FXML
    private TextField tf_prenom;

    @FXML
    private TextField tf_tel;

    @FXML
    private Button sponsor;

    @FXML
    private Button teams;
    @FXML
    private Button match;

    @FXML
    private ImageView icon4;

    @FXML
    private ImageView icon5;

    @FXML
    private ImageView icon6;

    @FXML
    private ImageView icon7;

    @FXML
    private Button logistique;

    @FXML
    private ImageView icon2;

    @FXML
    private ImageView back;

    @FXML
    private ImageView icon3;


    @FXML
    private Button dashboard;

    @FXML
    private ImageView Home;
    @FXML
    private Label nom_user;

    @FXML
    private Button espace;


    private User currentUser;
    private ToggleGroup toggleGroup;

    public void setUserToModify(User user) {
        this.currentUser = user;


        tf_nom.setText(user.getNom());
        tf_prenom.setText(user.getPrenom());
        tf_email.setText(user.getEmail());
        tf_mot_de_passe.setText(user.getPassword());
        tf_tel.setText(String.valueOf(user.getNum_telephone()));
        tf_image.setText(user.getImage());
        date_date_naiss.setValue(user.getDate_de_naissance());


        if ("Homme".equals(user.getGenre())) {
            rb_homme.setSelected(true);
        } else {
            rb_femme.setSelected(true);
        }


        combo_role.setValue(user.getRole().getValue());
    }


    @FXML
    void log_out(ActionEvent event) {
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
    public void initData(User user) {
        this.currentUser = user;
        tf_nom.setText(user.getNom());
        tf_prenom.setText(user.getPrenom());
        tf_email.setText(user.getEmail());
        tf_mot_de_passe.setText(user.getPassword());
        tf_tel.setText(String.valueOf(user.getNum_telephone()));
        tf_image.setText(user.getImage());
        date_date_naiss.setValue(user.getDate_de_naissance());
        if ("Homme".equals(user.getGenre())) rb_homme.setSelected(true);
        else rb_femme.setSelected(true);
        combo_role.setValue(user.getRole().getValue());
    }

    @FXML
    private void initialize() {

        User user = SessionManager.getCurrentUser();
        if (user != null) {

            String name = user.getPrenom();
            nom_user.setText(name);

        } else {
            System.out.println("Aucun utilisateur connecté !");
        }
        toggleGroup = new ToggleGroup();
        rb_homme.setToggleGroup(toggleGroup);
        rb_femme.setToggleGroup(toggleGroup);

        for (Role role : Role.values()) {
            combo_role.getItems().add(role.getValue());
        }
        tf_image.setEditable(false);
        tf_image.setOnMouseClicked(event -> choisirImage());
    }

    private void choisirImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choisir une image");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg")
        );
        File file = fileChooser.showOpenDialog(new Stage());
        if (file != null) {
            tf_image.setText(file.toURI().toString());
        }
    }


    @FXML
    void annuler(ActionEvent event) {
        tf_nom.clear();
        tf_prenom.clear();
        tf_email.clear();
        tf_mot_de_passe.clear();
        tf_tel.clear();
        tf_image.clear();
        date_date_naiss.setValue(null);
        combo_role.setValue(null);
        toggleGroup.selectToggle(null);

    }

    @FXML
    void logout(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/user/interfaceA.fxml"));
            Stage stage = (Stage) logout.getScene().getWindow();
            stage.setScene(new Scene(loader.load()));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de charger la page d'inscription.");
        }


    }

    @FXML
    void pageuser(ActionEvent event) {
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


    @FXML
    void modifier(ActionEvent event) {

        if (!validerSaisie()) return;

        currentUser.setNom(tf_nom.getText());
        currentUser.setPrenom(tf_prenom.getText());
        currentUser.setEmail(tf_email.getText());
        currentUser.setPassword(tf_mot_de_passe.getText());
        currentUser.setNum_telephone(Integer.parseInt(tf_tel.getText()));
        currentUser.setImage(tf_image.getText());
        currentUser.setDate_de_naissance(date_date_naiss.getValue());
        currentUser.setGenre(rb_homme.isSelected() ? "Homme" : "Femme");
        currentUser.setRole(Role.fromString(combo_role.getValue()));

        UserService userService = new UserService();
        userService.modifier(currentUser);
        showAlert(Alert.AlertType.INFORMATION, "Succès", "Utilisateur mis à jour avec succès !");
        ((Button) event.getSource()).getScene().getWindow().hide();
    }

    private boolean validerSaisie() {
        if (tf_nom.getText().trim().isEmpty() || tf_prenom.getText().trim().isEmpty()
                || tf_email.getText().trim().isEmpty() || tf_mot_de_passe.getText().isEmpty()
                || tf_tel.getText().trim().isEmpty() || date_date_naiss.getValue() == null
                || combo_role.getValue() == null || toggleGroup.getSelectedToggle() == null
                || tf_image.getText().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Attention", "Veuillez remplir tous les champs !");
            return false;
        }
        if (!Pattern.matches("^[A-Z][a-zA-Z ]*$", tf_nom.getText())) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Le nom doit commencer par une majuscule et contenir uniquement des lettres et des espaces !");
            return false;
        }
        if (!Pattern.matches("^[A-Z][a-zA-Z ]*$", tf_prenom.getText())) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Le prénom doit commencer par une majuscule et contenir uniquement des lettres et des espaces !");
            return false;
        }
        if (!Pattern.matches("^[a-zA-Z0-9._%+-]+@gmail\\.com$", tf_email.getText())) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Adresse e-mail invalide !");
            return false;
        }
        if (!Pattern.matches("^\\d{8}$", tf_tel.getText())) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Le numéro de téléphone doit contenir exactement 8 chiffres !");
            return false;
        }
        return true;
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.show();
    }

    @FXML
    void dashboard(ActionEvent event) {

    }

    @FXML
    void espace(ActionEvent event) {

    }



    @FXML
    void logistique(ActionEvent event) {

    }

    @FXML
    void match(ActionEvent event) {

    }



    @FXML
    void sponsor(ActionEvent event) {

    }

    @FXML
    void teams(ActionEvent event) {

    }



    @FXML
    void Home(MouseEvent event) {

    }

}
