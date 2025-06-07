package controllers.sponsor;

import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import models.sponsor.Gemini;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GeminiController {

    @FXML
    private VBox chatArea;

    @FXML
    private TextField inputField;

    @FXML
    private Button sendButton;

    // Persistent context about Matchupz
    private static final String MATCHUPZ_CONTEXT = "Matchupz est une plateforme innovante disponible à la fois sous forme d'une application desktop et d'un site web, dédiée à la gestion d'événements sportifs, d'équipes et des joueurs, ainsi que la gestion logistique des stades et des sponsors qui financent les transactions. Elle offre une interface utilisateur intuitive pour gérer les sponsors, les joueurs, les matchs, les espaces sportifs, la logistique, les statistiques, les contrats, les paiements via Stripe, et bien plus encore. L'objectif est de simplifier l'organisation des événements sportifs, y compris pour des sports comme le football, le handball, le tennis, le padel et le basketball, en centralisant toutes les fonctionnalités clés dans une seule plateforme.";

    // List of keywords related to Matchupz and supported sports
    private List<String> keywords = Arrays.asList(
            "Matchupz", "matchupz", "matchupz desktop application", "matchupz website", "gestion des sponsors", "gestion des joueurs",
            "gestion des espaces sportifs", "gestion des matches", "gestion du logistique", "statistique",
            "gestion des fournisseurs", "contrat", "profil utilisateur", "interface utilisateur", "macth",
            "stade", "paiement", "stripe", "players tracker", "crud", "api",
            "Hazem Mrad", "Amine Mokhtar", "Ismail Ismail", "Louay Tlili", "Ibtissem Ben Amara", "Feriel Khamlia",
            "football", "handball", "tennis", "padel", "basketball"
    );

    // In-memory chat history
    private List<ChatMessage> chatHistory = new ArrayList<>();

    @FXML
    public void initialize() {
        // Initialize chat history with an introductory message
        if (chatHistory.isEmpty()) {
            String introMessage = "Gemini : Bonjour je suis l'assistant Matchupz, comment je pourrais vous aider aujourd'hui ?";
            chatHistory.add(new ChatMessage("Gemini", introMessage));
            Label introLabel = new Label(introMessage);
            introLabel.setStyle("-fx-background-color: #E0E0E0; -fx-padding: 10; -fx-background-radius: 10; -fx-font-size: 14;");
            introLabel.setWrapText(true);
            chatArea.getChildren().add(introLabel);
            animateMessage(introLabel);
        }

        sendButton.setOnAction(event -> sendMessage());
    }

    private void sendMessage() {
        String userInput = inputField.getText().trim();
        if (!userInput.isEmpty()) {
            // Add user message to chat history and display
            ChatMessage userMessage = new ChatMessage("Moi", userInput);
            chatHistory.add(userMessage);
            Label userLabel = new Label("Moi : " + userInput);
            userLabel.setStyle("-fx-background-color: #DCF8C6; -fx-padding: 10; -fx-background-radius: 10; -fx-font-size: 14;");
            userLabel.setWrapText(true);
            chatArea.getChildren().add(userLabel);
            animateMessage(userLabel);

            // Prepare system prompt
            String systemPrompt = """
                    Tu es Gemini, l'assistant intelligent de l'application Matchupz. Ton rôle principal est d'aider les utilisateurs avec des questions liées à Matchupz, une plateforme de gestion d'événements sportifs, ainsi que des questions sur les sports suivants : football, handball, tennis, padel et basketball. Voici le contexte de Matchupz : 

                    %s

                    Priorise les réponses aux questions contenant ces mots-clés : %s. Pour ces questions, fournis des réponses détaillées, précises et utiles basées sur le contexte de Matchupz ou sur les sports mentionnés.

                    Instructions pour les questions :
                    1. Si la question concerne Matchupz ou contient ses mots-clés, réponds en te basant sur le contexte de Matchupz, en mettant en avant ses fonctionnalités (gestion des sponsors, joueurs, matchs, etc.).
                    2. Si la question concerne le football, le handball, le tennis, le padel ou le basketball, réponds de manière informative et, si possible, fais un lien avec Matchupz. Par exemple, pour une question sur l'organisation d'un tournoi de football, explique comment Matchupz peut faciliter la gestion de l'événement.
                    3. Si la question est liée au domaine sportif ou à l'organisation d'événements en général, réponds de manière intelligente en faisant un lien avec Matchupz si possible.
                    4. Si la question est hors sujet (ne concerne ni Matchupz ni les sports mentionnés), réponds poliment : "Désolé, je suis spécialisé dans Matchupz et les sports comme le football, le handball, le tennis, le padel et le basketball. Pouvez-vous poser une question sur ces sujets ?"

                    Instructions supplémentaires :
                    - Adopte un ton professionnel, amical et engageant.
                    - Fournis des réponses claires, concises et structurées.
                    - Si la question est vague, pose une question de clarification pour mieux répondre.
                    - Évite de partager des informations sensibles ou hors contexte.

                    Question de l'utilisateur : %s
                    """.formatted(MATCHUPZ_CONTEXT, String.join(", ", keywords), userInput);

            // Call Gemini API
            String response = Gemini.getResponseFromGemini(systemPrompt);
            String responseText;

            try {
                // Extract response text
                responseText = extractResponseText(response);
                // Check if response is relevant
                if (!isResponseRelevant(responseText, keywords) && !isMatchupzOrSportsQuestion(userInput)) {
                    responseText = "Désolé, je suis spécialisé dans Matchupz et les sports comme le football, le handball, le tennis, le padel et le basketball. Pouvez-vous poser une question sur ces sujets ?";
                }
            } catch (Exception e) {
                responseText = "Erreur lors de la communication avec l'API. Veuillez réessayer plus tard.";
            }

            // Add Gemini response to chat history and display
            ChatMessage geminiMessage = new ChatMessage("Gemini", responseText);
            chatHistory.add(geminiMessage);
            Label geminiLabel = new Label("Gemini : " + responseText);
            geminiLabel.setStyle("-fx-background-color: #E0E0E0; -fx-padding: 10; -fx-background-radius: 10; -fx-font-size: 14;");
            geminiLabel.setWrapText(true);
            chatArea.getChildren().add(geminiLabel);
            animateMessage(geminiLabel);

            // Clear input field
            inputField.clear();

            // Auto-scroll to bottom
            chatArea.requestLayout();
        }
    }

    // Check if the question contains Matchupz or sports-related keywords
    private boolean isMatchupzOrSportsQuestion(String userInput) {
        for (String keyword : keywords) {
            if (userInput.toLowerCase().contains(keyword.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    // Check if the response is relevant to Matchupz or sports keywords
    private boolean isResponseRelevant(String response, List<String> keywords) {
        String responseLower = response.toLowerCase();
        for (String keyword : keywords) {
            if (responseLower.contains(keyword.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    // Animation for messages
    private void animateMessage(Label message) {
        FadeTransition fadeIn = new FadeTransition();
        fadeIn.setNode(message);
        fadeIn.setDuration(new javafx.util.Duration(500)); // 500 milliseconds
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        fadeIn.play();
    }

    // Extract response text from Gemini API response
    private String extractResponseText(String jsonResponse) {
        JsonObject responseObj = JsonParser.parseString(jsonResponse).getAsJsonObject();
        return responseObj.getAsJsonArray("candidates")
                .get(0)
                .getAsJsonObject()
                .getAsJsonObject("content")
                .getAsJsonArray("parts")
                .get(0)
                .getAsJsonObject()
                .get("text")
                .getAsString();
    }

    // Inner class to store chat messages
    private static class ChatMessage {
        private final String sender;
        private final String message;

        public ChatMessage(String sender, String message) {
            this.sender = sender;
            this.message = message;
        }

        public String getSender() {
            return sender;
        }

        public String getMessage() {
            return message;
        }
    }
}