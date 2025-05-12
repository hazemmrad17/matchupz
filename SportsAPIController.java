package controllers.joueur;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

import javafx.stage.Stage;
import models.joueur.Joueur;
import models.match.SessionManager;
import models.utilisateur.Role;
import models.utilisateur.User;
import org.json.JSONArray;
import org.json.JSONObject;
import services.joueur.JoueurService;

public class SportsAPIController {

    @FXML private Button bt_user;
    @FXML private Button teams;
    @FXML private Button dashboard;
    @FXML private Button espace;
    @FXML private Button logistique;
    @FXML private Label nom_user;
    @FXML private BarChart<String, Number> weightChart; // Changed from performanceChart to match FXML
    @FXML private PieChart positionChart;
    @FXML private Label totalPlayersLabel; // Changed from totalPlayers to match FXML
    @FXML private Label avgHeightLabel;
    @FXML private Label avgWeightLabel;
    @FXML private Label activePlayersLabel; // Changed from topPerformer to match FXML
    @FXML private Button filterButton;
    @FXML private Button backToDashboard;

    private JoueurService joueurService = new JoueurService();
    private List<Joueur> joueurs;

    private void afficherProfil(User user) {
        if (user != null && user.getImage() != null && !user.getImage().isEmpty()) {
            javafx.scene.image.Image image = new javafx.scene.image.Image(user.getImage());
            String name = user.getPrenom();
            nom_user.setText(name);
            // profileImageView.setImage(image); // Uncomment if you add an ImageView
        }
    }

    @FXML
    void match(ActionEvent event) {
        // Implement match navigation if needed
    }

    @FXML
    void pageuser(ActionEvent event) {
        User user = SessionManager.getCurrentUser();
        if (user != null && user.getRole() == Role.ADMIN) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/user/adminpage.fxml"));
                Stage stage = (Stage) bt_user.getScene().getWindow();
                stage.setScene(new Scene(loader.load()));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de charger la page d'inscription.");
            }
        } else {
            System.out.println("Aucun utilisateur connecté ou pas admin !");
        }
    }

    @FXML
    void sponsor(ActionEvent event) {
        // Implement sponsor navigation if needed
    }

    @FXML
    void teams(ActionEvent event) {
        loadScene("/joueur/MainController.fxml", teams);
    }

    @FXML
    void dashboard(ActionEvent event) {
        loadScene("/joueur/MainController.fxml", dashboard);
    }

    @FXML
    void espace(ActionEvent event) {
        User user = SessionManager.getCurrentUser();
        if (user != null && user.getRole() == Role.ADMIN) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/AffichageEspace.fxml"));
                Stage stage = (Stage) espace.getScene().getWindow();
                stage.setScene(new Scene(loader.load()));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de charger la page d'inscription.");
            }
        } else {
            System.out.println("Aucun utilisateur connecté ou pas admin !");
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

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML private Button backButton;
    @FXML private Button homeButton;

    @FXML private AnchorPane main_form;

    @FXML private TableView<Standing> standingsTable;

    @FXML private TableColumn<Standing, Integer> rankColumn;
    @FXML private TableColumn<Standing, String> teamColumn;
    @FXML private TableColumn<Standing, Integer> pointsColumn;
    @FXML private TableColumn<Standing, Integer> playedColumn;
    @FXML private TableColumn<Standing, Integer> wonColumn;
    @FXML private TableColumn<Standing, Integer> drawnColumn;
    @FXML private TableColumn<Standing, Integer> lostColumn;
    @FXML private TableColumn<Standing, Integer> goalsForColumn;
    @FXML private TableColumn<Standing, Integer> goalsAgainstColumn;

    private ObservableList<Standing> standingsData = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        // Set up TableView columns
        rankColumn.setCellValueFactory(cellData -> cellData.getValue().rankProperty().asObject());
        teamColumn.setCellValueFactory(cellData -> cellData.getValue().teamProperty());
        pointsColumn.setCellValueFactory(cellData -> cellData.getValue().pointsProperty().asObject());
        playedColumn.setCellValueFactory(cellData -> cellData.getValue().playedProperty().asObject());
        wonColumn.setCellValueFactory(cellData -> cellData.getValue().wonProperty().asObject());
        drawnColumn.setCellValueFactory(cellData -> cellData.getValue().drawnProperty().asObject());
        lostColumn.setCellValueFactory(cellData -> cellData.getValue().lostProperty().asObject());
        goalsForColumn.setCellValueFactory(cellData -> cellData.getValue().goalsForProperty().asObject());
        goalsAgainstColumn.setCellValueFactory(cellData -> cellData.getValue().goalsAgainstProperty().asObject());

        // Fetch data and populate table
        fetchStandingsData();
        standingsTable.setItems(standingsData);
    }

    private void fetchStandingsData() {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://api.football-data.org/v4/competitions/PL/standings")) // Premier League
                    .header("X-Auth-Token", "d7ae883b954c4c1687706f0d370422fb") // Your API token
                    .method("GET", HttpRequest.BodyPublishers.noBody())
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("HTTP Status Code: " + response.statusCode());
            System.out.println("Raw API Response: " + response.body());

            if (response.statusCode() != 200) {
                JSONObject jsonObject = new JSONObject(response.body());
                String errorMsg = jsonObject.optString("message", "Unknown error occurred");
                standingsTable.setPlaceholder(new Label("API Error (HTTP " + response.statusCode() + "): " + errorMsg));
                return;
            }

            JSONObject jsonObject = new JSONObject(response.body());
            JSONArray standingsArray = jsonObject.getJSONArray("standings") // Get standings array
                    .getJSONObject(0) // First standings object (TOTAL)
                    .getJSONArray("table"); // Table array

            for (int i = 0; i < standingsArray.length(); i++) {
                JSONObject teamData = standingsArray.getJSONObject(i);
                int rank = teamData.getInt("position");
                String teamName = teamData.getJSONObject("team").getString("name");
                int points = teamData.getInt("points");
                int played = teamData.getInt("playedGames");
                int won = teamData.getInt("won");
                int drawn = teamData.getInt("draw");
                int lost = teamData.getInt("lost");
                int goalsFor = teamData.getInt("goalsFor");
                int goalsAgainst = teamData.getInt("goalsAgainst");

                standingsData.add(new Standing(rank, teamName, points, played, won, drawn, lost, goalsFor, goalsAgainst));
            }
        } catch (Exception e) {
            e.printStackTrace();
            standingsTable.setPlaceholder(new Label("Error fetching standings data: " + e.getMessage()));
        }
    }

    @FXML
    private void handleBack() {
        loadScene("/joueur/DisplayJoueur.fxml", backButton);
    }

    @FXML
    private void handleHome() {
        loadScene("/Home.fxml", homeButton);
    }

    private void loadScene(String fxmlPath, Button button) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            Stage stage = (Stage) button.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            showAlert("Error", "Failed to Load Scene", e.getMessage());
        }
    }

    private void showAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        if ("Success".equals(title)) {
            alert.setAlertType(Alert.AlertType.INFORMATION);
        }
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    // Standing class to hold table data
    public static class Standing {
        private final SimpleIntegerProperty rank;
        private final SimpleStringProperty team;
        private final SimpleIntegerProperty points;
        private final SimpleIntegerProperty played;
        private final SimpleIntegerProperty won;
        private final SimpleIntegerProperty drawn;
        private final SimpleIntegerProperty lost;
        private final SimpleIntegerProperty goalsFor;
        private final SimpleIntegerProperty goalsAgainst;

        public Standing(int rank, String team, int points, int played, int won, int drawn, int lost, int goalsFor, int goalsAgainst) {
            this.rank = new SimpleIntegerProperty(rank);
            this.team = new SimpleStringProperty(team);
            this.points = new SimpleIntegerProperty(points);
            this.played = new SimpleIntegerProperty(played);
            this.won = new SimpleIntegerProperty(won);
            this.drawn = new SimpleIntegerProperty(drawn);
            this.lost = new SimpleIntegerProperty(lost);
            this.goalsFor = new SimpleIntegerProperty(goalsFor);
            this.goalsAgainst = new SimpleIntegerProperty(goalsAgainst);
        }

        public SimpleIntegerProperty rankProperty() { return rank; }
        public SimpleStringProperty teamProperty() { return team; }
        public SimpleIntegerProperty pointsProperty() { return points; }
        public SimpleIntegerProperty playedProperty() { return played; }
        public SimpleIntegerProperty wonProperty() { return won; }
        public SimpleIntegerProperty drawnProperty() { return drawn; }
        public SimpleIntegerProperty lostProperty() { return lost; }
        public SimpleIntegerProperty goalsForProperty() { return goalsFor; }
        public SimpleIntegerProperty goalsAgainstProperty() { return goalsAgainst; }
    }
}



