package controllers.joueur;

import controllers.joueur.ModifySport;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import models.joueur.Sport;
import models.match.SessionManager;
import models.utilisateur.Role;
import models.utilisateur.User;
import services.joueur.SportService;

import java.io.IOException;

public class DisplaySport {
    @FXML
    private Button bt_user;
    @FXML
    private Button teams;
    @FXML
    private Button dashboard;
    @FXML
    private Button espace;
    @FXML
    private Button logistique;
    @FXML
    private Label nom_user;

    private boolean isGreenTheme = false; // Track current theme

    @FXML
    private AnchorPane main_form; // Add fx:id="main_form" to AnchorPane in FXML

    @FXML
    private Button button_theme_switch;

    @FXML
    private void switchTheme() {
        main_form.getStylesheets().clear(); // Remove current stylesheet

        if (isGreenTheme) {
            // Switch to blue
            main_form.getStylesheets().add(getClass().getResource("../style_file_blue.css").toExternalForm());
            isGreenTheme = false;
        } else {
            // Switch to green
            main_form.getStylesheets().add(getClass().getResource("../style_file_green.css").toExternalForm());
            isGreenTheme = true;
        }
    }

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
        loadScene("/joueur/MainController.fxml",teams);
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

    private void showAlert(Alert.AlertType alertType, String erreur, String content) {
    }

    private void loadScene(String fxmlPath, Button button) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            Stage stage = (Stage) button.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.valueOf("Erreur"), "Échec du chargement", e.getMessage());
        }
    }

    @FXML private Button joueurButton;
    @FXML private Button homeButton;
    @FXML private Button addSportButton;
    @FXML private TextField searchField; // Existing search field
    @FXML private TableView<Sport> tableView;
    @FXML private TableColumn<Sport, Integer> idSportColumn;
    @FXML private TableColumn<Sport, String> nomSportColumn;
    @FXML private TableColumn<Sport, String> descriptionColumn;
    @FXML private TableColumn<Sport, Void> modifierColumn;
    @FXML private TableColumn<Sport, Void> deleteColumn;

    private ObservableList<Sport> sportList = FXCollections.observableArrayList();
    private SportService sportService = new SportService();

    @FXML
    private void handleHome() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/joueur/MainController.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) homeButton.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            showError("Failed to load the MainController page", e);
        }
    }

    @FXML
    private void HandleJoueur() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/joueur/MainController.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) joueurButton.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            showError("Failed to load the MainController page", e);
        }
    }

    @FXML
    private void handleAddSportButton() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/joueur/AjoutSport.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) addSportButton.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            showError("Failed to load the Add Sport page", e);
        }
    }

    private void openModifyWindow(Sport sport, Stage stage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/joueur/ModifierSport.fxml"));
            Parent root = loader.load();
            ModifySport controller = loader.getController();
            controller.setSportToModify(sport);
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

            stage.setOnHidden(event -> loadSports()); // Simplified reload
        } catch (IOException e) {
            showError("Failed to load the Modify Sport page", e);
        }
    }

    @FXML
    public void initialize() {
        // Initialize columns with appropriate data
        idSportColumn.setCellValueFactory(cellData -> cellData.getValue().idSportProperty().asObject());
        nomSportColumn.setCellValueFactory(cellData -> cellData.getValue().nomSportProperty());
        descriptionColumn.setCellValueFactory(cellData -> cellData.getValue().descriptionProperty());
        main_form.getStylesheets().add(getClass().getResource("../style_file_blue.css").toExternalForm());
        // Modifier column with confirmation dialog
        modifierColumn.setCellFactory(param -> new TableCell<Sport, Void>() {
            private final Button btn = new Button("Modifier");
            {
                btn.setId("btn-modify");
                btn.setOnAction(event -> {
                    Sport selectedSport = getTableView().getItems().get(getIndex());
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Confirmation");
                    alert.setHeaderText("Are you sure you want to modify this sport?");
                    alert.setContentText("Sport: " + selectedSport.getNomSport());
                    alert.showAndWait().ifPresent(response -> {
                        if (response == ButtonType.OK) {
                            Stage stage = (Stage) btn.getScene().getWindow();
                            openModifyWindow(selectedSport, stage);
                        }
                    });
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btn);
            }
        });

        // Delete column with confirmation dialog
        deleteColumn.setCellFactory(param -> new TableCell<Sport, Void>() {
            private final Button btn = new Button("Supprimer");
            {
                btn.setId("btn-delete");
                btn.setOnAction(event -> {
                    Sport selectedSport = getTableView().getItems().get(getIndex());
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Confirmation");
                    alert.setHeaderText("Are you sure you want to delete this sport?");
                    alert.setContentText("Sport: " + selectedSport.getNomSport());
                    alert.showAndWait().ifPresent(response -> {
                        if (response == ButtonType.OK) {
                            sportService.supprimer(selectedSport);
                            loadSports();
                        }
                    });
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btn);
            }
        });

        // Real-time filtering as user types
        searchField.textProperty().addListener((observable, oldValue, newValue) -> filterSports(newValue));

        loadSports();
    }

    public void loadSports() {
        sportList.clear();
        sportList.addAll(sportService.recherche());
        tableView.setItems(sportList);
    }

    @FXML
    private void handleSearch() {
        filterSports(searchField.getText());
    }

    private void filterSports(String searchText) {
        if (searchText == null || searchText.trim().isEmpty()) {
            tableView.setItems(sportList);
        } else {
            ObservableList<Sport> filteredList = FXCollections.observableArrayList();
            String lowerCaseFilter = searchText.toLowerCase().trim();
            for (Sport sport : sportList) {
                if (String.valueOf(sport.getIdSport()).contains(lowerCaseFilter) ||
                        sport.getNomSport().toLowerCase().contains(lowerCaseFilter) ||
                        sport.getDescription().toLowerCase().contains(lowerCaseFilter)) {
                    filteredList.add(sport);
                }
            }
            tableView.setItems(filteredList);
        }
    }

    private void showError(String header, IOException e) {
        e.printStackTrace();
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(header);
        alert.setContentText("Details: " + e.getMessage());
        alert.showAndWait();
    }
}