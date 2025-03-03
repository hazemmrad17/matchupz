package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class StripePaymentController {

    @FXML
    private WebView webView;
    @FXML
    private Button btnBack;

    private String titre;
    private float montant;

    static {
        Stripe.apiKey = "sk_test_51Ib2kWSEw6FAIm6XCdgACg4POrw5qG0wocT1K59WHb47SM4Djd1WNiOh8SXlrsbgGdpiE99iG0sfi2YVikcO0sfE00Sf3YPqGJ"; // Secret key
    }

    public void setContractDetails(String titre, float montant) {
        this.titre = titre;
        this.montant = montant;
        initializeWebView();
    }

    @FXML
    void initialize() {
        // Ensure WebView is initialized
        if (webView != null) {
            initializeWebView();
        }
    }

    private void initializeWebView() {
        WebEngine webEngine = webView.getEngine();
        webEngine.load("https://buy.stripe.com/test_28o15r1fbduB44o8ww");

        // JavaScript to Java bridge (if needed for custom handling)
        webEngine.getLoadWorker().stateProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue == javafx.concurrent.Worker.State.SUCCEEDED) {
                // Optionally set contract details if the page supports it
                webEngine.executeScript("window.JavaFX = { handlePayment: function(paymentData) { " +
                        "javafx.application.Platform.runLater(() -> handlePayment(paymentData)); } };");
            }
        });
    }

    private void handlePayment(Object paymentData) {
        // Handle the payment data returned from Stripe (if the webpage supports callbacks)
        showAlert(Alert.AlertType.INFORMATION, "Succès", "Test payment completed in Stripe Checkout.");
        Stage stage = (Stage) webView.getScene().getWindow();
        stage.close(); // Close the payment window
    }

    private void handleToken(String token, String title, float amount) {
        try {
            // Use the token to create a customer
            Map<String, Object> customerParams = new HashMap<>();
            customerParams.put("name", title);
            customerParams.put("email", "customer@example.com"); // Replace with actual email if available
            customerParams.put("source", token); // Use the token from Stripe.js
            Customer customer = Customer.create(customerParams);

            showAlert(Alert.AlertType.INFORMATION, "Succès", "Paiement traité avec succès. Customer ID: " + customer.getId());
            Stage stage = (Stage) webView.getScene().getWindow();
            stage.close(); // Close the payment window
        } catch (StripeException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Échec du paiement Stripe: " + e.getMessage());
        }
    }

    @FXML
    private void handleBackToAfficherContrat(ActionEvent event) {
        Stage stage = (Stage) btnBack.getScene().getWindow();
        loadAfficherContrat(stage);
    }

    private void loadAfficherContrat(Stage stage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherContrat.fxml"));
            Parent root = loader.load();
            stage.setScene(new Scene(root));
            stage.setTitle("Afficher Contrats");
            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de charger AfficherContrat: " + e.getMessage());
            e.printStackTrace();
        }
    }


    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}