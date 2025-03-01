package controllers.joueur;

import javafx.beans.property.SimpleStringProperty;
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
import javafx.stage.Stage;
import models.joueur.Joueur;
import models.match.SessionManager;
import models.utilisateur.Role;
import models.utilisateur.User;
import services.joueur.JoueurService;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.stream.Collectors;

public class DisplayJoueur {
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

    @FXML private Button joueurButton;
    @FXML private Button homeButton;
    @FXML private Button addJoueurButton;
    @FXML private Button trackPlayersButton;
    @FXML private TableView<Joueur> tableView;
    @FXML private TableColumn<Joueur, Integer> idColumn;
    @FXML private TableColumn<Joueur, Integer> idSportColumn;
    @FXML private TableColumn<Joueur, String> nomSportColumn;
    @FXML private TableColumn<Joueur, String> nomColumn;
    @FXML private TableColumn<Joueur, String> prenomColumn;
    @FXML private TableColumn<Joueur, String> dateNaissanceColumn;
    @FXML private TableColumn<Joueur, String> posteColumn;
    @FXML private TableColumn<Joueur, Float> tailleColumn;
    @FXML private TableColumn<Joueur, Float> poidsColumn;
    @FXML private TableColumn<Joueur, String> statutColumn;
    @FXML private TableColumn<Joueur, String> emailColumn;
    @FXML private TableColumn<Joueur, String> telephoneColumn;
    @FXML private TableColumn<Joueur, String> profilePictureColumn;
    @FXML private TableColumn<Joueur, Void> modifierColumn;
    @FXML private TableColumn<Joueur, Void> deleteColumn;
    @FXML private TextField searchField; // Added for the search bar

    private ObservableList<Joueur> joueurList = FXCollections.observableArrayList();
    private ObservableList<Joueur> allJoueurs = FXCollections.observableArrayList(); // Store all joueurs for filtering
    private JoueurService joueurService = new JoueurService();
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    @FXML
    private void handleHome() {
        loadScene("/Home.fxml", homeButton);
    }

    @FXML
    private void HandleJoueur() {
        loadScene("/joueur/MainController.fxml", joueurButton);
    }

    @FXML
    private void handleAddJoueurButton() {
        loadScene("/joueur/AjoutJoueur.fxml", addJoueurButton);
    }

    @FXML
    private void handleTrackPlayers() {
        loadScene("/joueur/TrackPlayers.fxml", trackPlayersButton);
    }


    private void openModifyWindow(Joueur joueur, Stage stage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/joueur/ModifierJoueur.fxml"));
            Parent root = loader.load();
            ModifyJoueur controller = loader.getController();
            controller.setJoueurToModify(joueur);
            stage.setScene(new Scene(root));
            stage.show();
            stage.setOnHidden(event -> loadJoueurs());
        } catch (IOException e) {
            showAlert("Erreur", "Échec du chargement", e.getMessage());
        }
    }

    @FXML
    public void initialize() {
        // Initialize table columns
        idColumn.setCellValueFactory(new PropertyValueFactory<>("idJoueur"));
        idSportColumn.setCellValueFactory(new PropertyValueFactory<>("idSport"));
        nomSportColumn.setCellValueFactory(new PropertyValueFactory<>("nomSport"));
        nomColumn.setCellValueFactory(new PropertyValueFactory<>("nom"));
        prenomColumn.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        dateNaissanceColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(dateFormat.format(cellData.getValue().getDateNaissance())));
        posteColumn.setCellValueFactory(new PropertyValueFactory<>("poste"));
        tailleColumn.setCellValueFactory(new PropertyValueFactory<>("taille"));
        poidsColumn.setCellValueFactory(new PropertyValueFactory<>("poids"));
        statutColumn.setCellValueFactory(new PropertyValueFactory<>("statut"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        telephoneColumn.setCellValueFactory(new PropertyValueFactory<>("telephone"));
        profilePictureColumn.setCellFactory(param -> new TableCell<>() {
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
        profilePictureColumn.setCellValueFactory(new PropertyValueFactory<>("profilePictureUrl"));

        modifierColumn.setCellFactory(param -> new TableCell<>() {
            private final Button btn = new Button("Modifier");
            {
                btn.setId("btn-modify");
                btn.setOnAction(event -> {
                    Joueur selectedJoueur = getTableView().getItems().get(getIndex());
                    showConfirmation("Confirmation", "Modifier ce joueur ?", "Joueur: " + selectedJoueur.getNom() + " " + selectedJoueur.getPrenom(), () -> {
                        Stage stage = (Stage) btn.getScene().getWindow();
                        openModifyWindow(selectedJoueur, stage);
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
                    Joueur selectedJoueur = getTableView().getItems().get(getIndex());
                    showConfirmation("Confirmation", "Supprimer ce joueur ?", "Joueur: " + selectedJoueur.getNom() + " " + selectedJoueur.getPrenom(), () -> {
                        joueurService.supprimer(selectedJoueur);
                        loadJoueurs();
                    });
                });
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btn);
            }
        });

        // Load all players initially
        loadJoueurs();

        // Add listener to search field for real-time filtering
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filterJoueurs(newValue.trim());
        });
    }

    public void loadJoueurs() {
        allJoueurs.clear(); // Clear all players list
        allJoueurs.addAll(joueurService.recherche()); // Load all players from service
        joueurList.clear(); // Clear current list
        joueurList.addAll(allJoueurs); // Set initial list to all players
        tableView.setItems(joueurList); // Update table
    }

    private void filterJoueurs(String searchText) {
        if (searchText == null || searchText.isEmpty()) {
            joueurList.setAll(allJoueurs); // Show all players if search is empty
        } else {
            // Filter players based on nom, prenom, or email (case-insensitive)
            joueurList.setAll(allJoueurs.stream()
                    .filter(joueur ->
                            (joueur.getNom() != null && joueur.getNom().toLowerCase().contains(searchText.toLowerCase())) ||
                                    (joueur.getPrenom() != null && joueur.getPrenom().toLowerCase().contains(searchText.toLowerCase())) ||
                                    (joueur.getEmail() != null && joueur.getEmail().toLowerCase().contains(searchText.toLowerCase()))
                    )
                    .collect(Collectors.toList()));
        }
        tableView.setItems(joueurList); // Update table with filtered results
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