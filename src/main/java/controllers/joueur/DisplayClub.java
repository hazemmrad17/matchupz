package controllers.joueur;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import models.joueur.Club;
import models.match.SessionManager;
import models.utilisateur.Role;
import models.utilisateur.User;
import services.joueur.ClubService;

import java.io.IOException;

public class DisplayClub {
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

    @FXML private Button homeButton;
    @FXML private Button addClubButton;
    @FXML private Button joueurButton;
    @FXML private TextField searchField; // Added for search functionality
    @FXML private TableView<Club> tableView;
    @FXML private TableColumn<Club, Integer> idColumn;
    @FXML private TableColumn<Club, String> nomColumn;
    @FXML private TableColumn<Club, String> photoColumn;
    @FXML private TableColumn<Club, Void> modifierColumn;
    @FXML private TableColumn<Club, Void> deleteColumn;

    private ObservableList<Club> clubList = FXCollections.observableArrayList();
    private ClubService clubService = new ClubService();

    @FXML
    private void HandleJoueur() {
        loadScene("/joueur/MainController.fxml", joueurButton);
    }

    @FXML
    private void handleHome() {
        loadScene("/Home.fxml", homeButton);
    }

    @FXML
    private void handleAddClubButton() {
        loadScene("/joueur/AjoutClub.fxml", addClubButton);
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

    private void openModifyWindow(Club club, Stage stage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/joueur/ModifierClub.fxml"));
            Parent root = loader.load();
            ModifyClub controller = loader.getController();
            controller.setClubToModify(club);
            stage.setScene(new Scene(root));
            stage.show();
            stage.setOnHidden(event -> loadClubs());
        } catch (IOException e) {
            showAlert("Erreur", "Échec du chargement", e.getMessage());
        }
    }

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("idClub"));
        nomColumn.setCellValueFactory(new PropertyValueFactory<>("nomClub"));
        photoColumn.setCellFactory(param -> new TableCell<>() {
            private final ImageView imageView = new ImageView();
            {
                imageView.setFitHeight(30);
                imageView.setFitWidth(30);
            }
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    try {
                        imageView.setImage(new javafx.scene.image.Image(item));
                        setGraphic(imageView);
                    } catch (Exception e) {
                        setGraphic(null);
                    }
                }
            }
        });
        photoColumn.setCellValueFactory(new PropertyValueFactory<>("photoUrl"));

        modifierColumn.setCellFactory(param -> new TableCell<>() {
            private final Button btn = new Button("Modifier");
            {
                btn.setId("btn-modify");
                btn.setOnAction(event -> {
                    Club selectedClub = getTableView().getItems().get(getIndex());
                    showConfirmation("Confirmation", "Modifier ce club ?", "Club: " + selectedClub.getNomClub(), () -> {
                        Stage stage = (Stage) btn.getScene().getWindow();
                        openModifyWindow(selectedClub, stage);
                    });
                });
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btn);
            }
        });

        deleteColumn.setCellFactory(param -> new TableCell<>() {
            private final Button btn = new Button("Supprimer");
            {
                btn.setId("btn-delete");
                btn.setOnAction(event -> {
                    Club selectedClub = getTableView().getItems().get(getIndex());
                    showConfirmation("Confirmation", "Supprimer ce club ?", "Club: " + selectedClub.getNomClub(), () -> {
                        clubService.supprimer(selectedClub);
                        loadClubs();
                    });
                });
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btn);
            }
        });

        // Optional: Real-time filtering as user types
        searchField.textProperty().addListener((observable, oldValue, newValue) -> filterClubs(newValue));

        loadClubs();
    }

    public void loadClubs() {
        clubList.clear();
        clubList.addAll(clubService.recherche());
        tableView.setItems(clubList);
    }

    @FXML
    private void handleSearch() {
        filterClubs(searchField.getText());
    }

    private void filterClubs(String searchText) {
        if (searchText == null || searchText.trim().isEmpty()) {
            tableView.setItems(clubList);
        } else {
            ObservableList<Club> filteredList = FXCollections.observableArrayList();
            String lowerCaseFilter = searchText.toLowerCase().trim();
            for (Club club : clubList) {
                if (String.valueOf(club.getIdClub()).contains(lowerCaseFilter) ||
                        club.getNomClub().toLowerCase().contains(lowerCaseFilter)) {
                    filteredList.add(club);
                }
            }
            tableView.setItems(filteredList);
        }
    }

    private void showAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void showConfirmation(String title, String header, String content, Runnable onConfirm) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                onConfirm.run();
            }
        });
    }
}