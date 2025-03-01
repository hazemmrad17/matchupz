package tests;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class
FXMain extends Application {

    public static void main(String[] args) {
        launch(args);

    }

    @Override
    public void start(Stage primaryStage) {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/joueur/MainController.fxml"));
        try {
            Parent root = loader.load();


            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.setTitle("MatchhUPZ");
            primaryStage.setResizable(true);
            primaryStage.show();}
        catch (RuntimeException | IOException r){
            System.out.println(r.getMessage());
        }
    }
}
