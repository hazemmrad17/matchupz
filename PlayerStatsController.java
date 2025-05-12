

package controllers.joueur;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

import models.joueur.Joueur;
import models.match.SessionManager;
import models.utilisateur.Role;
import models.utilisateur.User;
import org.json.JSONArray;
import org.json.JSONObject;
import services.joueur.JoueurService;

public class PlayerStatsController {

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

    @FXML private AnchorPane main_form;
    @FXML private Button backButton;
    @FXML private Button homeButton;
    @FXML private TableView<PlayerStat> playerStatsTable;
    @FXML private TableColumn<PlayerStat, String> playerNameColumn;
    @FXML private TableColumn<PlayerStat, Integer> ageColumn;
    @FXML private TableColumn<PlayerStat, String> nationalityColumn;
    @FXML private TableColumn<PlayerStat, String> heightColumn;
    @FXML private TableColumn<PlayerStat, String> weightColumn;
    @FXML private TableColumn<PlayerStat, String> positionColumn;
    @FXML private TableColumn<PlayerStat, Integer> appearancesColumn;
    @FXML private TableColumn<PlayerStat, Integer> goalsColumn;
    @FXML private TableColumn<PlayerStat, Integer> assistsColumn;
    @FXML private TableColumn<PlayerStat, Integer> shotsColumn;
    @FXML private TableColumn<PlayerStat, Integer> passesColumn;
    @FXML private TableColumn<PlayerStat, Integer> dribblesColumn;
    @FXML private TableColumn<PlayerStat, Integer> minutesColumn;
    @FXML private TableColumn<PlayerStat, Boolean> injuredColumn;
    @FXML private Canvas heatmapCanvas;
    @FXML private BarChart<String, Number> performanceChart;
    @FXML private ComboBox<String> playerSelector;
    @FXML private ComboBox<String> seasonSelector;

    private ObservableList<PlayerStat> playerStatsData = FXCollections.observableArrayList();
    private ObservableList<String> playerOptions = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        setupTableColumns();
        setupSelectors();
        fetchTeamPlayers(64, "2023"); // Hull City team ID, default to 2023
        playerStatsTable.setItems(playerStatsData);

        if (!playerStatsData.isEmpty()) {
            PlayerStat firstPlayer = playerStatsData.get(0);
            drawHeatmap(firstPlayer);
            updatePerformanceChart(firstPlayer);
        }
    }

    private void setupTableColumns() {
        playerNameColumn.setCellValueFactory(cellData -> cellData.getValue().playerNameProperty());
        ageColumn.setCellValueFactory(cellData -> cellData.getValue().ageProperty().asObject());
        nationalityColumn.setCellValueFactory(cellData -> cellData.getValue().nationalityProperty());
        heightColumn.setCellValueFactory(cellData -> cellData.getValue().heightProperty());
        weightColumn.setCellValueFactory(cellData -> cellData.getValue().weightProperty());
        positionColumn.setCellValueFactory(cellData -> cellData.getValue().positionProperty());
        appearancesColumn.setCellValueFactory(cellData -> cellData.getValue().appearancesProperty().asObject());
        goalsColumn.setCellValueFactory(cellData -> cellData.getValue().goalsProperty().asObject());
        assistsColumn.setCellValueFactory(cellData -> cellData.getValue().assistsProperty().asObject());
        shotsColumn.setCellValueFactory(cellData -> cellData.getValue().shotsProperty().asObject());
        passesColumn.setCellValueFactory(cellData -> cellData.getValue().passesProperty().asObject());
        dribblesColumn.setCellValueFactory(cellData -> cellData.getValue().dribblesProperty().asObject());
        minutesColumn.setCellValueFactory(cellData -> cellData.getValue().minutesProperty().asObject());
        injuredColumn.setCellValueFactory(cellData -> cellData.getValue().injuredProperty());
    }

    private void setupSelectors() {
        playerSelector.setItems(playerOptions);
        playerSelector.setOnAction(this::onPlayerSelect);

        seasonSelector.setOnAction(this::onSeasonSelect);
    }

    private void fetchTeamPlayers(int teamId, String season) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://v3.football.api-sports.io/players?season=" + season + "&team=" + teamId))
                    .header("x-apisports-key", "184633b125801511d4969b1afb399010") // Your API key
                    .method("GET", HttpRequest.BodyPublishers.noBody())
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("HTTP Status Code (Players): " + response.statusCode());
            System.out.println("Raw API Response (Players): " + response.body());

            if (response.statusCode() != 200) {
                handleApiError("API Error", "Failed to fetch player data", response.body());
                return;
            }

            JSONObject jsonObject = new JSONObject(response.body());
            if (jsonObject.has("errors")) {
                Object errors = jsonObject.get("errors");
                if (errors instanceof JSONArray && ((JSONArray) errors).length() > 0) {
                    String errorMsg = ((JSONArray) errors).optString(0, "Unknown error");
                    handleApiError("API Error", "Plan Limitation", errorMsg);
                    return;
                } else if (errors instanceof JSONObject && ((JSONObject) errors).length() > 0) {
                    String errorMsg = ((JSONObject) errors).optString("plan", "Unknown error");
                    handleApiError("API Error", "Plan Limitation", errorMsg);
                    return;
                }
            }

            JSONArray playersArray = jsonObject.getJSONArray("response");

            playerStatsData.clear();
            playerOptions.clear();

            for (int i = 0; i < playersArray.length(); i++) {
                JSONObject playerData = playersArray.getJSONObject(i);
                String playerName = getPlayerName(playerData);
                JSONObject stats = getPlayerStats(playerData);
                JSONObject playerInfo = playerData.getJSONObject("player");

                PlayerStat stat = extractPlayerStats(playerName, playerInfo, stats);
                if (stat != null) {
                    playerStatsData.add(stat);
                    playerOptions.add(playerName);
                }
            }

            if (playerStatsData.isEmpty()) {
                playerStatsTable.setPlaceholder(new Label("No player data available for this team and season."));
            }
        } catch (Exception e) {
            e.printStackTrace();
            handleApiError("Error", "Failed to fetch player stats", e.getMessage());
        }
    }

    private String getPlayerName(JSONObject playerData) {
        try {
            return playerData.getJSONObject("player").getString("name");
        } catch (Exception e) {
            return "Unknown Player";
        }
    }

    private JSONObject getPlayerStats(JSONObject playerData) {
        try {
            return playerData.getJSONArray("statistics").getJSONObject(0);
        } catch (Exception e) {
            return new JSONObject();
        }
    }

    private PlayerStat extractPlayerStats(String playerName, JSONObject playerInfo, JSONObject stats) {
        try {
            int age = playerInfo.optInt("age", 0);
            String nationality = playerInfo.optString("nationality", "Unknown");
            String height = playerInfo.optString("height", "N/A");
            String weight = playerInfo.optString("weight", "N/A");
            String position = stats.optJSONObject("games").optString("position", "N/A");
            boolean injured = playerInfo.optBoolean("injured", false);
            int appearances = stats.optJSONObject("games").optInt("appearances", 0);
            int goals = stats.optJSONObject("goals").optInt("total", 0);
            int assists = stats.optJSONObject("goals").optInt("assists", 0);
            int shots = stats.optJSONObject("shots").optInt("total", 0);
            int passes = stats.optJSONObject("passes").optInt("total", 0);
            int dribbles = stats.optJSONObject("dribbles").optInt("attempts", 0);
            int minutes = stats.optJSONObject("games").optInt("minutes", 0);

            return new PlayerStat(playerName, age, nationality, height, weight, position, appearances, goals, assists, shots, passes, dribbles, minutes, injured);
        } catch (Exception e) {
            System.err.println("Error parsing stats for " + playerName + ": " + e.getMessage());
            return null;
        }
    }

    @FXML
    private void onPlayerSelect(javafx.event.ActionEvent event) {
        String selectedPlayer = playerSelector.getValue();
        if (selectedPlayer != null) {
            PlayerStat player = playerStatsData.stream()
                    .filter(p -> p.getPlayerName().equals(selectedPlayer))
                    .findFirst()
                    .orElse(null);
            if (player != null) {
                playerStatsTable.getItems().clear();
                playerStatsTable.getItems().add(player);
                drawHeatmap(player);
                updatePerformanceChart(player);
            } else {
                showAlert("Error", "Player Not Found", "Selected player data is unavailable.");
            }
        }
    }

    @FXML
    private void onSeasonSelect(javafx.event.ActionEvent event) {
        String selectedSeason = seasonSelector.getValue();
        if (selectedSeason != null) {
            fetchTeamPlayers(64, selectedSeason); // Hull City team ID
        }
    }

    @FXML
    private void handleRefresh(javafx.event.ActionEvent event) {
        String selectedSeason = seasonSelector.getValue();
        if (selectedSeason != null) {
            fetchTeamPlayers(64, selectedSeason); // Refresh with current season
        } else {
            fetchTeamPlayers(64, "2023"); // Default to 2023 if no season selected
        }
    }

    private void drawHeatmap(PlayerStat player) {
        GraphicsContext gc = heatmapCanvas.getGraphicsContext2D();
        gc.clearRect(0, 0, heatmapCanvas.getWidth(), heatmapCanvas.getHeight());

        double width = heatmapCanvas.getWidth();
        double height = heatmapCanvas.getHeight();
        gc.setStroke(Color.WHITE);
        gc.strokeRect(0, 0, width, height); // Pitch outline
        gc.setFill(Color.LIGHTGREEN);
        gc.fillRect(0, 0, width, height); // Green pitch

        int totalActivity = player.getShots() + player.getPasses() + player.getDribbles();
        if (totalActivity > 0) {
            double attackingIntensity = (player.getShots() * 0.7 + player.getGoals() * 0.3) / totalActivity;
            double midfieldIntensity = (player.getPasses() * 0.6 + player.getDribbles() * 0.4) / totalActivity;
            double defenseIntensity = (player.getPasses() * 0.4) / totalActivity;

            drawHeatZone(gc, 0, 0, width * 0.3, height * 0.3, defenseIntensity, Color.BLUE); // Defense
            drawHeatZone(gc, width * 0.3, 0, width * 0.4, height * 0.4, midfieldIntensity, Color.YELLOW); // Midfield
            drawHeatZone(gc, width * 0.7, 0, width * 0.3, height * 0.7, attackingIntensity, Color.RED); // Attack
        }

        gc.setFill(Color.WHITE);
        gc.fillText(player.getPlayerName() + " Heatmap", 10, 20);
        gc.fillText("Position: " + player.getPosition(), 10, 40);
    }

    private void drawHeatZone(GraphicsContext gc, double x, double y, double w, double h, double intensity, Color color) {
        gc.setFill(color.deriveColor(0, 1, 1, Math.min(0.8, intensity)));
        gc.fillRect(x, y, w, h);
    }

    private void updatePerformanceChart(PlayerStat player) {
        performanceChart.getData().clear();
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName(player.getPlayerName());
        series.getData().add(new XYChart.Data<>("Goals", player.getGoals()));
        series.getData().add(new XYChart.Data<>("Assists", player.getAssists()));
        series.getData().add(new XYChart.Data<>("Shots", player.getShots()));
        series.getData().add(new XYChart.Data<>("Passes", player.getPasses()));
        series.getData().add(new XYChart.Data<>("Dribbles", player.getDribbles()));
        performanceChart.getData().add(series);

        performanceChart.setTitle("Performance Metrics");
        performanceChart.setLegendVisible(true);
        performanceChart.setStyle("-fx-background-color: #16213e; -fx-text-fill: white;");
    }

    @FXML
    private void handleBack() {
        loadScene("/joueur/DisplayEvaluation.fxml", backButton);
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

    private void handleApiError(String title, String header, String content) {
        showAlert(title, header, content);
        playerStatsTable.setPlaceholder(new Label(header + ": " + content));
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

    public static class PlayerStat {
        private final SimpleStringProperty playerName;
        private final SimpleIntegerProperty age;
        private final SimpleStringProperty nationality;
        private final SimpleStringProperty height;
        private final SimpleStringProperty weight;
        private final SimpleStringProperty position;
        private final SimpleIntegerProperty appearances;
        private final SimpleIntegerProperty goals;
        private final SimpleIntegerProperty assists;
        private final SimpleIntegerProperty shots;
        private final SimpleIntegerProperty passes;
        private final SimpleIntegerProperty dribbles;
        private final SimpleIntegerProperty minutes;
        private final SimpleBooleanProperty injured;

        public PlayerStat(String playerName, int age, String nationality, String height, String weight, String position, int appearances, int goals, int assists, int shots, int passes, int dribbles, int minutes, boolean injured) {
            this.playerName = new SimpleStringProperty(playerName);
            this.age = new SimpleIntegerProperty(age);
            this.nationality = new SimpleStringProperty(nationality);
            this.height = new SimpleStringProperty(height);
            this.weight = new SimpleStringProperty(weight);
            this.position = new SimpleStringProperty(position);
            this.appearances = new SimpleIntegerProperty(appearances);
            this.goals = new SimpleIntegerProperty(goals);
            this.assists = new SimpleIntegerProperty(assists);
            this.shots = new SimpleIntegerProperty(shots);
            this.passes = new SimpleIntegerProperty(passes);
            this.dribbles = new SimpleIntegerProperty(dribbles);
            this.minutes = new SimpleIntegerProperty(minutes);
            this.injured = new SimpleBooleanProperty(injured);
        }

        public SimpleStringProperty playerNameProperty() { return playerName; }
        public SimpleIntegerProperty ageProperty() { return age; }
        public SimpleStringProperty nationalityProperty() { return nationality; }
        public SimpleStringProperty heightProperty() { return height; }
        public SimpleStringProperty weightProperty() { return weight; }
        public SimpleStringProperty positionProperty() { return position; }
        public SimpleIntegerProperty appearancesProperty() { return appearances; }
        public SimpleIntegerProperty goalsProperty() { return goals; }
        public SimpleIntegerProperty assistsProperty() { return assists; }
        public SimpleIntegerProperty shotsProperty() { return shots; }
        public SimpleIntegerProperty passesProperty() { return passes; }
        public SimpleIntegerProperty dribblesProperty() { return dribbles; }
        public SimpleIntegerProperty minutesProperty() { return minutes; }
        public SimpleBooleanProperty injuredProperty() { return injured; }

        public String getPlayerName() { return playerName.get(); }
        public int getAge() { return age.get(); }
        public String getNationality() { return nationality.get(); }
        public String getHeight() { return height.get(); }
        public String getWeight() { return weight.get(); }
        public String getPosition() { return position.get(); }
        public int getAppearances() { return appearances.get(); }
        public int getGoals() { return goals.get(); }
        public int getAssists() { return assists.get(); }
        public int getShots() { return shots.get(); }
        public int getPasses() { return passes.get(); }
        public int getDribbles() { return dribbles.get(); }
        public int getMinutes() { return minutes.get(); }
        public boolean isInjured() { return injured.get(); }
    }
}