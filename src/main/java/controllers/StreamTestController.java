package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.web.WebView;
import javafx.scene.web.WebEngine;
import javafx.concurrent.Worker;

public class StreamTestController {
    @FXML private WebView streamView;
    @FXML private Button toggleButton;

    // The simplified YouTube stream URL without autoplay and autohide
    private final String STREAM_URL = "https://www.youtube.com/embed/A6g94uyEYcs";
    private boolean isStreamActive = false;

    @FXML
    private void initialize() {
        WebEngine engine = streamView.getEngine();
        engine.setJavaScriptEnabled(true); // Enable JavaScript for interactive content
        engine.setUserAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36"); // Set a typical user-agent string

        // Add detailed debugging for loading state
        engine.getLoadWorker().stateProperty().addListener((obs, oldState, newState) -> {
            System.out.println("WebEngine State: " + newState);
            if (newState == Worker.State.FAILED) {
                System.out.println("Error loading URL: " + engine.getLoadWorker().getException());
                System.out.println("Current URL: " + engine.getLocation());
            } else if (newState == Worker.State.SUCCEEDED) {
                System.out.println("Stream loaded successfully at: " + engine.getLocation());
            }
        });
    }

    @FXML
    private void handleToggleStream() {
        WebEngine engine = streamView.getEngine();
        if (!isStreamActive) {
            engine.load(STREAM_URL);
            toggleButton.setText("Arrêter le Stream");
            isStreamActive = true;
        } else {
            engine.loadContent(""); // Clear the WebView if the stream is stopped
            toggleButton.setText("Démarrer le Stream");
            isStreamActive = false;
        }
    }
}
