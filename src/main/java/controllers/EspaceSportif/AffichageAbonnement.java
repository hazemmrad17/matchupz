package controllers.EspaceSportif;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import models.EspaceSportif.Abonnement;
import services.EspaceSportif.AbonnementService;
import utils.MyDatabase; // Import your MyDatabase class
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.io.IOException;
import java.util.List;

public class AffichageAbonnement {

    @FXML
    private TableView<Abonnement> tableView;

    @FXML
    private TableColumn<Abonnement, Integer> colId;

    @FXML
    private TableColumn<Abonnement, String> colSport;

    @FXML
    private TableColumn<Abonnement, String> colType;

    @FXML
    private TableColumn<Abonnement, java.sql.Date> colDateDebut;

    @FXML
    private TableColumn<Abonnement, java.sql.Date> colDateFin;

    @FXML
    private TableColumn<Abonnement, Double> colTarif;

    @FXML
    private TableColumn<Abonnement, String> colEtat;

    @FXML
    private TableColumn<Abonnement, Void> colActions;

    @FXML
    private TextField searchField;

    private AbonnementService abonnementService;
    private ObservableList<Abonnement> abonnementList;

    public AffichageAbonnement() {
        this.abonnementService = new AbonnementService(MyDatabase.getInstance().getConnection());
        this.abonnementList = FXCollections.observableArrayList();
    }

    @FXML
    public void initialize() {
        // Configuration des colonnes du TableView
        colId.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getIdAbonnement()).asObject());
        colSport.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNomSport()));
        colType.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTypeAbonnement()));
        colDateDebut.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getDateDebut()));
        colDateFin.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getDateFin()));
        colTarif.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getTarif()));
        colEtat.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEtat()));

        // Ajout de la colonne actions
        addActionsColumn();

        // Chargement initial des données
        loadAbonnements();

        // Listener pour la recherche en temps réel
        searchField.textProperty().addListener((observable, oldValue, newValue) -> searchAbonnement(newValue));
    }

    private void loadAbonnements() {
        try {
            List<Abonnement> abonnements = abonnementService.rechercher();
            if (abonnements == null || abonnements.isEmpty()) {
                System.err.println("⚠ Aucune abonnements trouvée !");
                return;
            }
            abonnementList.setAll(abonnements);
            tableView.setItems(abonnementList);
        } catch (RuntimeException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur de chargement", "Impossible de charger les abonnements : " + e.getMessage());
        }
    }

    private void addActionsColumn() {
        colActions.setCellFactory(param -> new TableCell<>() {
            final Button editButton = new Button("Modifier");
            final Button deleteButton = new Button("Supprimer");
            final HBox hBox = new HBox(10, editButton, deleteButton);

            {
                editButton.setOnAction(event -> handleEdit(getTableRow().getItem()));
                deleteButton.setOnAction(event -> handleDelete(getTableRow().getItem()));
            }

            @Override
            public void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : hBox);
            }
        });
    }

    private void handleEdit(Abonnement abonnement) {
        if (abonnement == null) return;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModifierAbonnement.fxml"));
            Parent modifLayout = loader.load();

            ModifierAbonnement controller = loader.getController();
            controller.setAbonnementToEdit(abonnement);

            Stage currentStage = (Stage) tableView.getScene().getWindow();
            currentStage.close();

            Stage newStage = new Stage();
            newStage.setScene(new Scene(modifLayout));
            newStage.setTitle("Modifier Abonnement");
            newStage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible d'ouvrir la fenêtre de modification : " + e.getMessage());
        }
    }

    private void handleDelete(Abonnement abonnement) {
        if (abonnement == null) return;

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation de suppression");
        alert.setHeaderText("Supprimer l'abonnement");
        alert.setContentText("Êtes-vous sûr de vouloir supprimer cet abonnement ?");

        ButtonType buttonYes = new ButtonType("Oui", ButtonBar.ButtonData.YES);
        ButtonType buttonNo = new ButtonType("Non", ButtonBar.ButtonData.NO);
        alert.getButtonTypes().setAll(buttonYes, buttonNo);

        alert.showAndWait().ifPresent(response -> {
            if (response == buttonYes) {
                try {
                    abonnementService.supprimer(abonnement);
                    loadAbonnements();
                } catch (RuntimeException e) {
                    showAlert(Alert.AlertType.ERROR, "Erreur de suppression", "Impossible de supprimer l'abonnement : " + e.getMessage());
                }
            }
        });
    }

    private void searchAbonnement(String keyword) {
        List<Abonnement> searchResults = abonnementService.rechercher().stream()
                .filter(abn -> abn.getNomSport().toLowerCase().contains(keyword.toLowerCase()) ||
                        abn.getTypeAbonnement().toLowerCase().contains(keyword.toLowerCase()) ||
                        abn.getEtat().toLowerCase().contains(keyword.toLowerCase()))
                .toList();

        ObservableList<Abonnement> data = FXCollections.observableArrayList(searchResults);
        tableView.setItems(data);
    }

    @FXML
    private void refreshList() {
        loadAbonnements();
    }

    @FXML
    private void addAbonnement(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterAbonnement.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Ajouter un Abonnement");
            stage.show();
            stage.setOnHidden(e -> refreshList()); // Rafraîchit la liste quand la fenêtre se ferme
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible d'ouvrir la fenêtre d'ajout : " + e.getMessage());
        }
    }

    @FXML
    private void goToEspaceSportif(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AffichageEspace.fxml"));
            AnchorPane espaceSportifLayout = loader.load();
            Scene scene = new Scene(espaceSportifLayout);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de charger AffichageEspace.fxml : " + e.getMessage());
        }
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    private void goToReservation(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AffichageReservation.fxml"));
            AnchorPane reservationLayout = loader.load();
            Scene scene = new Scene(reservationLayout);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de charger AffichageReservation.fxml : " + e.getMessage());
        }
    }
}