package tests;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class FXMain extends Application {

    private static Stage primaryStage;
    private static Scene mainScene;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        primaryStage = stage;
        try {
            // Charger l'interface AfficherSponsor.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherSponsor.fxml"));
            Parent root = loader.load();

            // Définir la scène principale
            mainScene = new Scene(root);
            primaryStage.setScene(mainScene);
            primaryStage.setTitle("Gestion des Sponsors");
            primaryStage.setResizable(false); // Empêche le redimensionnement de la fenêtre
            primaryStage.show();


        } catch (IOException e) {
            System.err.println("⚠ Erreur lors du chargement du fichier FXML !");
            System.err.println("Message: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Méthode pour changer la vue sans recharger toute la fenêtre.
     * @param fxmlFile le fichier FXML à charger
     */
    public static void switchScene(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(FXMain.class.getResource(fxmlFile));
            Parent newRoot = loader.load();
            mainScene.setRoot(newRoot);
        } catch (IOException e) {
            System.err.println("⚠ Erreur lors du changement de scène !");
            System.err.println("Message: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
