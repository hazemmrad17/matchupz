package controllers.MatchSchedule;

import javafx.application.HostServices;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import models.Match;
import models.Schedule;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

public class NewsWindowController {
    @FXML
    private Label titleLabel;

    @FXML
    private Label sourceLabel;

    @FXML
    private TextArea descriptionArea;

    @FXML
    private Button urlButton;
 
    private Schedule schedule;
    private ObservableList<Match> matchFKList;
    private HostServices hostServices; // Add HostServices reference
    private static final String NEWS_API_KEY = "2a65b6315ab7460f97ac49687599263d"; // Replace with your actual NewsAPI key
    private static final String NEWS_API_URL = "https://newsapi.org/v2/everything";
    private final OkHttpClient httpClient = new OkHttpClient();
    private String articleUrl;

    public void setSchedule(Schedule schedule, ObservableList<Match> matchFKList, HostServices hostServices) {
        this.schedule = schedule;
        this.matchFKList = matchFKList;
        this.hostServices = hostServices; // Set HostServices from ScheduleController
        fetchAndDisplayNews();
    }

    private void fetchAndDisplayNews() {
        Match match = matchFKList.stream()
                .filter(m -> m.getIdMatch() == schedule.getIdMatchFK())
                .findFirst()
                .orElse(null);
        if (match == null) {
            titleLabel.setText("No match data available");
            return;
        }

        String query = match.getC1() + " " + match.getC2() + " " + match.getSportType();
        Request request = new Request.Builder()
                .url(NEWS_API_URL + "?q=" + query.replace(" ", "+") + "&apiKey=" + NEWS_API_KEY + "&language=en&sortBy=relevancy")
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                titleLabel.setText("Error fetching news: " + response.code());
                return;
            }
            String jsonData = response.body().string();
            JSONObject json = new JSONObject(jsonData);
            JSONArray articles = json.getJSONArray("articles");
            if (articles.length() > 0) {
                JSONObject article = articles.getJSONObject(0); // Most relevant article
                titleLabel.setText(article.getString("title"));
                sourceLabel.setText("Source: " + article.getJSONObject("source").getString("name"));
                descriptionArea.setText(article.optString("description", "No description available"));
                articleUrl = article.getString("url");
                urlButton.setDisable(false);
            } else {
                titleLabel.setText("No recent news found");
                sourceLabel.setText("");
                descriptionArea.setText("");
                urlButton.setDisable(true);
            }
        } catch (IOException e) {
            e.printStackTrace();
            titleLabel.setText("Error: " + e.getMessage());
            sourceLabel.setText("");
            descriptionArea.setText("");
            urlButton.setDisable(true);
        }
    }

    @FXML
    private void openArticleUrl() {
        if (articleUrl != null && !articleUrl.isEmpty() && hostServices != null) {
            hostServices.showDocument(articleUrl);
        } else {
            titleLabel.setText("Error: Unable to open article (URL or HostServices missing)");
        }
    }
}