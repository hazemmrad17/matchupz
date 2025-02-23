package controllers.Match;

import controllers.Match.ModifyMatch;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import models.match.Match;
import services.match.MatchService;
import services.match.ScheduleService;

import java.io.IOException;
import java.text.SimpleDateFormat;

public class DisplayMatch {

    @FXML
    private Button addMatchButton;

    @FXML
    private TableView<Match> tableView;

    @FXML
    private TableColumn<Match, Integer> idMatchColumn;

    @FXML
    private TableColumn<Match, String> team1Column;

    @FXML
    private TableColumn<Match, String> team2Column;

    @FXML
    private TableColumn<Match, String> sportTypeColumn;

    @FXML
    private TableColumn<Match, Void> modifyColumn;

    @FXML
    private TableColumn<Match, Void> deleteColumn;

    private ObservableList<Match> matchList = FXCollections.observableArrayList();
    private MatchService matchService = new MatchService();

    @FXML
    public void initialize() {
        // Set up cell value factories using wrapper methods
        idMatchColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getIdMatch()).asObject());
        team1Column.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getC1()));
        team2Column.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getC2()));
        sportTypeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getSportType()));

        // Set up action for the Modify column
        modifyColumn.setCellFactory(param -> new TableCell<Match, Void>() {
            private final Button btn = new Button("Modifier");

            {
                btn.setId("btn-modify"); // Assign the CSS ID for styling
                btn.setOnAction(event -> {
                    Match selectedMatch = getTableView().getItems().get(getIndex());
                    openModifyWindow(selectedMatch); // Open the ModifierMatch.fxml in a new window
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btn);
            }
        });

        // Set up action for the Delete column
        deleteColumn.setCellFactory(param -> new TableCell<Match, Void>() {
            private final Button btn = new Button("Delete");

            {
                btn.setId("btn-delete"); // Assign the CSS ID for styling
                btn.setOnAction(event -> {
                    Match selectedMatch = getTableView().getItems().get(getIndex());
                    matchService.supprimer(selectedMatch);  // Delete the match using the service
                    loadMatches();  // Reload the table after deletion
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btn);
            }
        });

        loadMatches();  // Load matches into the table view
    }
    @FXML
    private void handleAddMatchButton() {
        try {
            // Load the FXML file for adding a match
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Match/AjoutMatch.fxml"));  // Change path
            Parent root = loader.load();

            // Get the current stage
            Stage stage = (Stage) addMatchButton.getScene().getWindow();

            // Set the new scene
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the error (e.g., show an alert)
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Failed to load the FXML file");
            alert.setContentText("Details: " + e.getMessage());
            alert.showAndWait();
        }
    }


    private void openModifyWindow(Match match) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/match/ModifyMatch.fxml"));
            Parent root = loader.load();

            ModifyMatch controller = loader.getController();
            controller.setMatchToModify(match);

            Stage modifyStage = new Stage();
            modifyStage.setTitle("Modifier Match");
            modifyStage.setScene(new Scene(root));

            // Add a listener to reload the table when the modify window is closed
            modifyStage.setOnHidden(event -> loadMatches());

            modifyStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private Button log_out;

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

    private void showAlert(Alert.AlertType alertType, String erreur, String s) {
    }

    public void loadMatches() {
        matchList.clear();
        matchList.addAll(matchService.recherche());  // Fetch list of matches from the service
        tableView.setItems(matchList);
    }
}
