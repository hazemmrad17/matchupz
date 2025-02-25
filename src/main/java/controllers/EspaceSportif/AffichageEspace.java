package controllers.EspaceSportif;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Parent;
import models.EspaceSportif.EspaceSportif;
import services.EspaceSportif.EspaceSportifService;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.util.List;

public class AffichageEspace {

    @FXML
    private TableView<EspaceSportif> tableView;

    @FXML
    private TableColumn<EspaceSportif, Integer> colId;

    @FXML
    private TableColumn<EspaceSportif, String> colNom;

    @FXML
    private TableColumn<EspaceSportif, String> colAdresse;

    @FXML
    private TableColumn<EspaceSportif, String> colCategorie;

    @FXML
    private TableColumn<EspaceSportif, Float> colCapacite;

    @FXML
    private TableColumn<EspaceSportif, String> colActions;

    @FXML
    private TextField searchField;

    private EspaceSportifService espaceService;

    public AffichageEspace() {
        this.espaceService = new EspaceSportifService();
    }

    @FXML
    public void initialize() {
        if (colNom == null || tableView == null) {
            System.err.println("⚠ Erreur : Vérifie ton fichier FXML !");
            return;
        }

        colId.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getIdLieu()).asObject());
        colNom.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNomEspace()));
        colAdresse.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAdresse()));
        colCategorie.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCategorie()));
        colCapacite.setCellValueFactory(cellData -> new SimpleFloatProperty(cellData.getValue().getCapacite()).asObject());

        addActionsColumn();
        loadEspaces();

        // Ajout d'un listener pour la recherche dynamique
        searchField.textProperty().addListener((observable, oldValue, newValue) -> searchEspaceSportif(newValue));
    }

    private void loadEspaces() {
        List<EspaceSportif> espaces = espaceService.rechercher();
        if (espaces == null) {
            System.err.println("⚠ Aucun espace sportif trouvé !");
            return;
        }
        tableView.getItems().setAll(espaces);
    }

    private void addActionsColumn() {
        colActions.setCellValueFactory(cellData -> new SimpleStringProperty("Actions"));

        colActions.setCellFactory(new Callback<>() {
            @Override
            public TableCell<EspaceSportif, String> call(TableColumn<EspaceSportif, String> param) {
                return new TableCell<>() {
                    final Button editButton = new Button("Modifier");
                    final Button deleteButton = new Button("Supprimer");
                    final HBox hBox = new HBox(10, editButton, deleteButton);

                    {
                        editButton.setOnAction(event -> handleEdit(getTableRow().getItem()));
                        deleteButton.setOnAction(event -> handleDelete(getTableRow().getItem()));
                    }

                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        setGraphic(empty ? null : hBox);
                    }
                };
            }
        });
    }

    private void handleEdit(EspaceSportif espace) {
        try {
            // Charger la fenêtre de modification de l'espace sportif
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModifierEspace.fxml"));
            AnchorPane modifLayout = loader.load();

            // Obtenir le contrôleur de la fenêtre de modification
            ModifierEspace controller = loader.getController();
            controller.setEspaceToEdit(espace);

            // Obtenir le stage actuel (fenêtre précédente) et le fermer
            Stage currentStage = (Stage) tableView.getScene().getWindow();
            currentStage.close();  // Fermer la fenêtre précédente

            // Créer une nouvelle scène avec la fenêtre de modification et l'afficher
            Stage newStage = new Stage();
            newStage.setScene(new Scene(modifLayout));
            newStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Erreur lors du chargement de ModifierEspace.fxml");
        }
    }


    private void handleDelete(EspaceSportif espace) {
        if (espace == null) return;

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation de suppression");
        alert.setHeaderText("Supprimer l'espace sportif");
        alert.setContentText("Êtes-vous sûr de vouloir supprimer \"" + espace.getNomEspace() + "\" ?");

        ButtonType buttonYes = new ButtonType("Oui", ButtonBar.ButtonData.YES);
        ButtonType buttonNo = new ButtonType("Non", ButtonBar.ButtonData.NO);
        alert.getButtonTypes().setAll(buttonYes, buttonNo);

        alert.showAndWait().ifPresent(response -> {
            if (response == buttonYes) {
                espaceService.supprimer(espace);
                loadEspaces();
            }
        });
    }

    @FXML
    private void addEspace() {
        try {
            // Charger la fenêtre d'ajout d'un espace sportif
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterEspace.fxml"));
            AnchorPane ajoutLayout = loader.load();

            // Obtenir le stage actuel et le fermer
            Stage currentStage = (Stage) tableView.getScene().getWindow();
            currentStage.close();  // Fermer la fenêtre précédente

            // Créer une nouvelle scène et l'afficher
            Stage newStage = new Stage();
            newStage.setScene(new Scene(ajoutLayout));
            newStage.setTitle("Ajouter un espace sportif");
            newStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Erreur lors du chargement de AjouterEspace.fxml");
        }
    }


    @FXML
    private void refreshList() {
        loadEspaces();
    }

    private void searchEspaceSportif(String motCle) {
        List<EspaceSportif> resultatRecherche = espaceService.rechercherParMotCle(motCle.toLowerCase());

        ObservableList<EspaceSportif> data = FXCollections.observableArrayList(resultatRecherche);
        tableView.setItems(data);
    }



    @FXML
    private void goToEspaceSportif(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/AffichageEspace.fxml"));
        AnchorPane espaceSportifLayout = loader.load();
        Scene scene = new Scene(espaceSportifLayout);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void goToReservation(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AffichageReservation.fxml"));
            AnchorPane reservationLayout = loader.load();
            Scene reservationScene = new Scene(reservationLayout);
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.setScene(reservationScene);
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Erreur lors du chargement de Reservation.fxml");
        }
    }

    @FXML
    public void goToAbonnement(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AffichageAbonnement.fxml")); // Ou une autre vue
            Parent root = loader.load();
            Stage stage = (Stage) tableView.getScene().getWindow(); // Récupère la fenêtre actuelle
            stage.setScene(new Scene(root));
            stage.setTitle("Liste des Abonnements");
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Erreur lors du chargement des Abonnements.fxml");
        }
    }


}
