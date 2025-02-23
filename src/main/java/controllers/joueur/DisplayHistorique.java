package controllers.joueur;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import models.HistoriqueClub;
import services.joueur.HistoriqueClubService;

import java.io.IOException;

public class DisplayHistorique {
    @FXML
    private Button joueurButton;
    @FXML
    private Button Home;
    @FXML
    private Button addHistoriqueButton; // Changed to reflect adding historical data
    @FXML
    private TableView<HistoriqueClub> tableView;
    @FXML
    private TableColumn<HistoriqueClub, Integer> idHistoriqueColumn;
    @FXML
    private TableColumn<HistoriqueClub, Integer> idJoueurColumn;
    @FXML
    private TableColumn<HistoriqueClub, String> nomClubColumn;
    @FXML
    private TableColumn<HistoriqueClub, String> saisonDebutColumn;
    @FXML
    private TableColumn<HistoriqueClub, String> saisonFinColumn;
    @FXML
    private TableColumn<HistoriqueClub, Void> modifierColumn;
    @FXML
    private TableColumn<HistoriqueClub, Void> deleteColumn;

    private ObservableList<HistoriqueClub> historiqueList = FXCollections.observableArrayList();
    private HistoriqueClubService historiqueClubService = new HistoriqueClubService();

    @FXML
    private void handleHome() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/joueur/MainController.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) Home.getScene().getWindow();

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Failed to load the FXML file");
            alert.setContentText("Details: " + e.getMessage());
            alert.showAndWait();
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
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Failed to load the FXML file");
            alert.setContentText("Details: " + e.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    private void handleAddHistoriqueButton() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/joueur/AjoutHistorique.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) addHistoriqueButton.getScene().getWindow();

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Failed to load the FXML file");
            alert.setContentText("Details: " + e.getMessage());
            alert.showAndWait();
        }
    }

    private void openModifyWindow(HistoriqueClub historique, Stage stage) {
        try {
            // Load ModifierHistorique.fxml for editing
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/joueur/ModifierHistorique.fxml"));
            Parent root = loader.load();

            ModifyHistorique controller = loader.getController();
            controller.setHistoriqueToModify(historique);

            // Set the scene for the modification window
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

            // After modification, load DisplayHistorique.fxml
            stage.setOnHidden(event -> {
                try {
                    FXMLLoader displayLoader = new FXMLLoader(getClass().getResource("/joueur/DisplayHistorique.fxml"));
                    Parent displayRoot = displayLoader.load();

                    Scene displayScene = new Scene(displayRoot);
                    stage.setScene(displayScene);
                    stage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Failed to load DisplayHistorique.fxml");
                    alert.setContentText("Details: " + e.getMessage());
                    alert.showAndWait();
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Failed to load the FXML file");
            alert.setContentText("Details: " + e.getMessage());
            alert.showAndWait();
        }
    }

    public void initialize() {
        // Initialize columns with appropriate data
        idHistoriqueColumn.setCellValueFactory(cellData -> cellData.getValue().idHistoriqueProperty().asObject());
        idJoueurColumn.setCellValueFactory(cellData -> cellData.getValue().idJoueurProperty().asObject());
        nomClubColumn.setCellValueFactory(cellData -> cellData.getValue().nomClubProperty());
        saisonDebutColumn.setCellValueFactory(cellData -> cellData.getValue().saisonDebutProperty().asString());
        saisonFinColumn.setCellValueFactory(cellData -> cellData.getValue().saisonFinProperty().asString());
        loadHistorique();
        modifierColumn.setCellFactory(param -> new TableCell<HistoriqueClub, Void>() {
            private final Button btn = new Button("Modifier");

            {
                btn.setId("btn-modify");
                btn.setOnAction(event -> {
                    HistoriqueClub selectedHistorique = getTableView().getItems().get(getIndex());

                    // Confirmation dialog before modification
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Confirmation");
                    alert.setHeaderText("Are you sure you want to modify this historique?");
                    alert.setContentText("Historique Club: " + selectedHistorique.getNomClub());

                    alert.showAndWait().ifPresent(response -> {
                        if (response == ButtonType.OK) {
                            Stage stage = (Stage) btn.getScene().getWindow();
                            openModifyWindow(selectedHistorique, stage);
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
        deleteColumn.setCellFactory(param -> new TableCell<HistoriqueClub, Void>() {
            private final Button btn = new Button("Delete");

            {
                btn.setId("btn-delete");
                btn.setOnAction(event -> {
                    HistoriqueClub selectedHistorique = getTableView().getItems().get(getIndex());

                    // Confirmation dialog before deletion
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Confirmation");
                    alert.setHeaderText("Are you sure you want to delete this historique?");
                    alert.setContentText("Historique Club: " + selectedHistorique.getNomClub());

                    alert.showAndWait().ifPresent(response -> {
                        if (response == ButtonType.OK) {
                            historiqueClubService.supprimer(selectedHistorique);  // Delete the historique using the service
                            loadHistorique();  // Reload the table after deletion
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

        loadHistorique();  // Load historical data into the table view
    }

    public void loadHistorique() {
        historiqueList.clear();
        historiqueList.addAll(historiqueClubService.recherche());  // Fetch list of historical data from the service
        tableView.setItems(historiqueList);
    }
}
