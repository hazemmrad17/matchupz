package tests;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainFX extends Application {

    @Override
    public void start(Stage stage) {
        try {
            // Charger le fichier FXML à partir des ressources
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AffichageEspace.fxml"));
            if (loader.getLocation() == null) {
                throw new IllegalStateException("Fichier AffichageEspace.fxml non trouvé dans les ressources ! Vérifiez le chemin : /AffichageEspace.fxml");
            }
            Parent root = loader.load();

            // Définition de la scène
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Gestion des Espaces Sportifs");
            stage.setResizable(true); // Permet le redimensionnement de la fenêtre (corrigé de false à true si intentionnel)
            stage.show();
        } catch (Exception e) {
            System.err.println("⚠ Erreur lors du chargement du fichier FXML !");
            System.err.println("Détails : " + e.getMessage());
            e.printStackTrace(); // Affiche la trace complète pour diagnostiquer
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}