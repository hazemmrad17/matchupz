package controllers.EspaceSportif;

import javafx.fxml.FXML;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

public class MapViewController {

    @FXML
    private WebView webView;

    private double latitude;
    private double longitude;

    public void initialize() {
        // Ensure webView is not null
        if (webView == null) {
            System.err.println("WebView is null in MapViewController");
            return;
        }

        WebEngine webEngine = webView.getEngine();
        String mapHtml = new services.EspaceSportif.EspaceSportifService().getMapUrl(longitude, latitude);
        System.out.println("Loading map HTML into WebView: First 200 chars: " + mapHtml.substring(0, Math.min(200, mapHtml.length()))); // Debug: Print part of HTML

        webEngine.loadContent(mapHtml);

        // Add a listener to debug loading issues
        webEngine.getLoadWorker().stateProperty().addListener((obs, oldState, newState) -> {
            if (newState == javafx.concurrent.Worker.State.FAILED) {
                System.err.println("WebView failed to load: " + webEngine.getLoadWorker().getException());
                System.err.println("Error details: " + webEngine.getLoadWorker().getException().getMessage());
            } else if (newState == javafx.concurrent.Worker.State.SUCCEEDED) {
                System.out.println("Map loaded successfully in WebView");
            } else if (newState == javafx.concurrent.Worker.State.RUNNING) {
                System.out.println("Map loading in progress...");
            }
        });
    }

    // Method to set the coordinates before initializing the map
    public void setCoordinates(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    // Method to close the stage (optional, for a close button if added later)
    public void closeStage() {
        Stage stage = (Stage) webView.getScene().getWindow();
        stage.close();
    }
}