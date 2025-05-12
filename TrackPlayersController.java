

package controllers.joueur;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressIndicator;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

public class TrackPlayersController {
    @FXML private Button backButton;
    @FXML private Button homeButton;
    @FXML private Button showTrackingButton;
    @FXML private ProgressIndicator trackingProgress;

    private static final String ANALYSIS_DIR = Paths.get("src", "main", "football_analysis-master").toString();
    private static final String MAIN_SCRIPT = Paths.get(ANALYSIS_DIR, "main.py").toString();
    private static final String OUTPUT_VIDEO_DIR = Paths.get(ANALYSIS_DIR, "output_videos").toString();

    @FXML
    private void handleStartTracking() {
        try {
            String projectRoot = System.getProperty("user.dir");
            String pythonPath = "C:\\Users\\MSI\\AppData\\Local\\Programs\\Python\\Python310\\python.exe"; // Updated Python path
            String scriptPath = Paths.get(projectRoot, MAIN_SCRIPT).toString();
            String workingDir = Paths.get(projectRoot, ANALYSIS_DIR).toString();

            // Verify script exists
            File scriptFile = new File(scriptPath);
            if (!scriptFile.exists()) {
                showAlert("Error", "Script Not Found", "Python script not found at: " + scriptPath);
                return;
            }

            // Show progress indicator
            trackingProgress.setVisible(true);

            // Execute the Python script (no video path needed since it's handled in Python GUI)
            ProcessBuilder pb = new ProcessBuilder(pythonPath, scriptPath);
            pb.directory(new File(workingDir));
            pb.redirectErrorStream(true);
            Process process = pb.start();

            // Capture output for debugging
            java.util.Scanner scanner = new java.util.Scanner(process.getInputStream()).useDelimiter("\\A");
            String output = scanner.hasNext() ? scanner.next() : "";
            scanner.close();
            System.out.println("Python script output: " + output);

            // Wait for the script to complete
            int exitCode = process.waitFor();
            trackingProgress.setVisible(false);

            if (exitCode == 0) {
                showAlert("Success", "Tracking Complete", "Video tracking completed successfully");
            } else {
                showAlert("Error", "Tracking Failed", "Python script exited with code: " + exitCode + "\nOutput: " + output);
            }
        } catch (IOException e) {
            trackingProgress.setVisible(false);
            showAlert("Error", "Failed to Run Tracking", "IO Exception: " + e.getMessage());
        } catch (InterruptedException e) {
            trackingProgress.setVisible(false);
            showAlert("Error", "Tracking Interrupted", "Interrupted Exception: " + e.getMessage());
            Thread.currentThread().interrupt();
        }
    }

    @FXML
    private void openVideoWithExternalPlayer() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Video to Play");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Video Files", "*.mp4", "*.avi"));
        fileChooser.setInitialDirectory(new File(Paths.get(System.getProperty("user.dir"), OUTPUT_VIDEO_DIR).toString()));
        Stage stage = (Stage) showTrackingButton.getScene().getWindow();
        File selectedFile = fileChooser.showOpenDialog(stage);

        if (selectedFile != null) {
            try {
                if (!selectedFile.exists()) {
                    showAlert("Error", "Video Not Found", "Selected video not found at: " + selectedFile.getAbsolutePath());
                    return;
                }
                if (selectedFile.length() == 0) {
                    showAlert("Error", "Video Empty", "Selected video is empty at: " + selectedFile.getAbsolutePath());
                    return;
                }
                if (!selectedFile.canRead()) {
                    showAlert("Error", "Permission Denied", "Cannot read video file at: " + selectedFile.getAbsolutePath());
                    return;
                }

                if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.OPEN)) {
                    Desktop.getDesktop().open(selectedFile);
                } else {
                    ProcessBuilder pb = new ProcessBuilder("vlc", selectedFile.getAbsolutePath());
                    pb.start();
                }
            } catch (IOException e) {
                showAlert("Error", "Failed to Open Video", "Exception: " + e.getMessage());
            }
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
}

