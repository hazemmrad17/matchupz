package controllers.joueur;

import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import models.match.SessionManager;
import models.utilisateur.Role;
import models.utilisateur.User;
import models.joueur.Joueur;
import services.joueur.JoueurService;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AnalyticsJoueurController {

    @FXML private Button bt_user;
    @FXML private Button teams;
    @FXML private Button dashboard;
    @FXML private Button espace;
    @FXML private Button logistique;
    @FXML private Label nom_user;
    @FXML private Label log_out;
    @FXML private Label match;
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
    void dashboard(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Dashboard.fxml"));
            Stage stage = (Stage) dashboard.getScene().getWindow();
            stage.setScene(new Scene(loader.load()));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de charger la page d'inscription.");
        }
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
    void teams(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/joueur/MainController.fxml"));
            Stage stage = (Stage) teams.getScene().getWindow();
            stage.setScene(new Scene(loader.load()));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de charger la page d'inscription.");
        }
    }

    @FXML
    void espace(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/EspaceSportif/AffichageEspace.fxml"));
            Stage stage = (Stage) logistique.getScene().getWindow();
            stage.setScene(new Scene(loader.load()));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de charger la page d'inscription.");
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

    @FXML
    void match(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/MatchSchedule/AffichageMatch.fxml"));
            Stage stage = (Stage) match.getScene().getWindow();
            stage.setScene(new Scene(loader.load()));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de charger la page d'inscription.");
        }
    }

    @FXML
    void sponsor(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/sponsoring/MainPage.fxml"));
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

    private void loadScene(String fxmlPath, Button button) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            Stage stage = (Stage) button.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Échec du chargement", e.getMessage());
        }
    }

    @FXML
    public void initialize() {
        User currentUser = SessionManager.getCurrentUser();
        if (currentUser != null) {
            afficherProfil(currentUser);
        }
        loadPlayerAnalytics();
    }

    private void loadPlayerAnalytics() {
        joueurs = joueurService.recherche(); // Fetch all players
        if (joueurs == null || joueurs.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "No Data", "No players found for analytics.");
            return;
        }

        // Total Players
        totalPlayersLabel.setText("Total Players: " + joueurs.size());

        // Weight Chart
        XYChart.Series<String, Number> weightSeries = new XYChart.Series<>();
        weightSeries.setName("Player Weight");
        for (Joueur joueur : joueurs) {
            weightSeries.getData().add(new XYChart.Data<>(joueur.getNom() + " " + joueur.getPrenom(), joueur.getPoids()));
        }
        weightChart.getData().clear();
        weightChart.getData().add(weightSeries);

        // Position Distribution
        Map<String, Long> positionCounts = joueurs.stream()
                .collect(Collectors.groupingBy(Joueur::getPoste, Collectors.counting()));
        positionChart.getData().clear();
        positionCounts.forEach((poste, count) ->
                positionChart.getData().add(new PieChart.Data(poste, count)));

        // Additional Stats
        double avgHeight = joueurs.stream().mapToDouble(Joueur::getTaille).average().orElse(0.0);
        double avgWeight = joueurs.stream().mapToDouble(Joueur::getPoids).average().orElse(0.0);
        long activeCount = joueurs.stream().filter(j -> "Active".equalsIgnoreCase(j.getStatut())).count();

        avgHeightLabel.setText(String.format("Avg Height: %.1f cm", avgHeight));
        avgWeightLabel.setText(String.format("Avg Weight: %.1f kg", avgWeight));
        activePlayersLabel.setText("Active Players: " + activeCount); // Updated to set the label
    }

    @FXML
    private void handleFilter(ActionEvent event) {
        // Example: Filter active players only
        XYChart.Series<String, Number> filteredWeightSeries = new XYChart.Series<>();
        filteredWeightSeries.setName("Active Players Weight");
        List<Joueur> activeJoueurs = joueurs.stream()
                .filter(j -> "Active".equalsIgnoreCase(j.getStatut()))
                .collect(Collectors.toList());
        for (Joueur joueur : activeJoueurs) {
            filteredWeightSeries.getData().add(new XYChart.Data<>(joueur.getNom() + " " + joueur.getPrenom(), joueur.getPoids()));
        }
        weightChart.getData().clear();
        weightChart.getData().add(filteredWeightSeries);

        Map<String, Long> filteredPositionCounts = activeJoueurs.stream()
                .collect(Collectors.groupingBy(Joueur::getPoste, Collectors.counting()));
        positionChart.getData().clear();
        filteredPositionCounts.forEach((poste, count) ->
                positionChart.getData().add(new PieChart.Data(poste, count)));
    }

    @FXML
    private void handleBackToDashboard(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/joueur/MainController.fxml")); // Updated path
            Stage stage = (Stage) backToDashboard.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de charger le tableau de bord.");
        }
    }
}