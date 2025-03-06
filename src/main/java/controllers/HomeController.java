package controllers;

import javafx.beans.property.SimpleStringProperty;
import models.joueur.Joueur;
import services.joueur.JoueurService;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class HomeController implements Initializable {

    @FXML
    private TableView<Joueur> tableView;

    @FXML
    private TableColumn<Joueur, Integer> idJoueurColumn;
    @FXML
    private TableColumn<Joueur, String> nomColumn;
    @FXML
    private TableColumn<Joueur, String> prenomColumn;
    @FXML
    private TableColumn<Joueur, String> dateNaissanceColumn;
    @FXML
    private TableColumn<Joueur, String> posteColumn;
    @FXML
    private TableColumn<Joueur, Float> tailleColumn;
    @FXML
    private TableColumn<Joueur, Float> poidsColumn;
    @FXML
    private TableColumn<Joueur, String> statutColumn;
    @FXML
    private TableColumn<Joueur, String> emailColumn;
    @FXML
    private TableColumn<Joueur, String> telephoneColumn;
    @FXML
    private TableColumn<Joueur, Integer> idSportColumn;
    @FXML
    private TableColumn<Joueur, String> nomSportColumn;
    @FXML
    private TableColumn<Joueur, String> profilePictureColumn;

    private ObservableList<Joueur> playerData = FXCollections.observableArrayList();
    private JoueurService joueurService;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        joueurService = new JoueurService();
        loadPlayerData();
        setupTableView();
    }

    private void loadPlayerData() {
        try {
            List<Joueur> joueurs = joueurService.recherche();
            playerData.setAll(joueurs);
        } catch (Exception e) {
            e.printStackTrace();
            // Fallback to mock data if service fails
            playerData.setAll(
                    new Joueur(1, "Football", "John", "Doe", new java.util.Date(), "Forward", 1.8f, 75f, "Active", "john@example.com", "123456789", "/images/player1.png"),
                    new Joueur(2, "Basketball", "Jane", "Smith", new java.util.Date(), "Guard", 1.75f, 70f, "Active", "jane@example.com", "987654321", null)
            );
        }
    }

    private void setupTableView() {
        idJoueurColumn.setCellValueFactory(cellData -> cellData.getValue().idJoueurProperty().asObject());
        nomColumn.setCellValueFactory(cellData -> cellData.getValue().nomProperty());
        prenomColumn.setCellValueFactory(cellData -> cellData.getValue().prenomProperty());
        dateNaissanceColumn.setCellValueFactory(cellData -> {
            Date date = cellData.getValue().getDateNaissance();
            return new SimpleStringProperty(date != null ? new SimpleDateFormat("yyyy-MM-dd").format(date) : "");
        });
        posteColumn.setCellValueFactory(cellData -> cellData.getValue().posteProperty());
        tailleColumn.setCellValueFactory(cellData -> cellData.getValue().tailleProperty().asObject());
        poidsColumn.setCellValueFactory(cellData -> cellData.getValue().poidsProperty().asObject());
        statutColumn.setCellValueFactory(cellData -> cellData.getValue().statutProperty());
        emailColumn.setCellValueFactory(cellData -> cellData.getValue().emailProperty());
        telephoneColumn.setCellValueFactory(cellData -> cellData.getValue().telephoneProperty());
        idSportColumn.setCellValueFactory(cellData -> cellData.getValue().idSportProperty().asObject());
        nomSportColumn.setCellValueFactory(cellData -> cellData.getValue().nomSportProperty());
        profilePictureColumn.setCellValueFactory(cellData -> cellData.getValue().profilePictureUrlProperty());

        tableView.setItems(playerData);
    }


}