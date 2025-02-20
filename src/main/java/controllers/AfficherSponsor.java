package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import models.Sponsor;
import services.SponsorService;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class AfficherSponsor {

    @FXML
    private TableView<Sponsor> tableViewSponsors;

    @FXML
    private TableColumn<Sponsor, String> colNom;

    @FXML
    private TableColumn<Sponsor, String> colContact;

    @FXML
    private TableColumn<Sponsor, String> colPack;

    @FXML
    private TableColumn<Sponsor, Void> colActions; // Changed to Void since buttons don't need values

    private final SponsorService sponsorService = new SponsorService();
    private final ObservableList<Sponsor> sponsorList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colNom.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getNom()));
        colContact.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getContact()));
        colPack.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getPack()));

        addActionsColumn();
        loadSponsors();
    }

    private void addActionsColumn() {
        colActions.setCellFactory(new Callback<>() {
            @Override
            public TableCell<Sponsor, Void> call(TableColumn<Sponsor, Void> param) {
                return new TableCell<>() {
                    final Button btnModifier = new Button("Modifier");
                    final Button btnSupprimer = new Button("Supprimer");
                    final HBox pane = new HBox(10, btnModifier, btnSupprimer);

                    {
                        btnModifier.setOnAction(event -> handleEdit(getTableRow().getItem()));
                        btnSupprimer.setOnAction(event -> handleDelete(getTableRow().getItem()));
                    }

                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        setGraphic(empty ? null : pane);
                    }
                };
            }
        });
    }

    private void loadSponsors() {
        sponsorList.clear(); // Prevent duplication
        List<Sponsor> sponsors = sponsorService.rechercher();
        sponsorList.addAll(sponsors);
        tableViewSponsors.setItems(sponsorList);
    }

    @FXML
    private void addSponso() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterSponsor.fxml"));
        Scene scene = new Scene(loader.load());
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Ajouter un sponsor");
        stage.show();
    }

    @FXML
    private void refreshList() {
        loadSponsors();
    }

    private void handleEdit(Sponsor sponsor) {
        if (sponsor == null) return;

        // Add your navigation logic here
        System.out.println("Modifier " + sponsor.getNom());
    }

    private void handleDelete(Sponsor sponsor) {
        if (sponsor == null) return;

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation de suppression");
        alert.setHeaderText("Supprimer le sponsor");
        alert.setContentText("Êtes-vous sûr de vouloir supprimer \"" + sponsor.getNom() + "\" ?");

        ButtonType buttonYes = new ButtonType("Oui", ButtonBar.ButtonData.YES);
        ButtonType buttonNo = new ButtonType("Non", ButtonBar.ButtonData.NO);
        alert.getButtonTypes().setAll(buttonYes, buttonNo);

        alert.showAndWait().ifPresent(response -> {
            if (response == buttonYes) {
                sponsorService.supprimer(sponsor);
                loadSponsors();
            }
        });
    }
}
