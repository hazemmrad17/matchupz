package controllers.EspaceSportif;

import models.EspaceSportif.Reservation;
import services.EspaceSportif.ReservationService;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;

public class AffichageReservation {

    @FXML
    private TableView<Reservation> tableView;

    @FXML
    private TableColumn<Reservation, Integer> colIdReservation;

    @FXML
    private TableColumn<Reservation, Integer> colIdLieu;

    @FXML
    private TableColumn<Reservation, Timestamp> colDateReservee;

    @FXML
    private TableColumn<Reservation, String> colMotif;

    @FXML
    private TableColumn<Reservation, String> colStatus;

    @FXML
    private TableColumn<Reservation, String> colActions;

    private ReservationService reservationService;

    public AffichageReservation() {
        this.reservationService = new ReservationService();
    }

    @FXML
    public void initialize() {
        if (tableView == null || colIdReservation == null) {
            System.err.println("⚠ Erreur : Vérifie ton fichier FXML !");
            return;
        }

        colIdReservation.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getIdReservation()).asObject());
        colIdLieu.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getIdLieu()).asObject());
        colDateReservee.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getDateReservee()));
        colMotif.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getMotif()));
        colStatus.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStatus()));

        addActionsColumn();
        loadReservations();
    }

    private void loadReservations() {
        List<Reservation> reservations = reservationService.rechercher();
        if (reservations == null || reservations.isEmpty()) {
            System.err.println("⚠ Aucune réservation trouvée !");
            return;
        }
        tableView.getItems().setAll(reservations);
    }

    private void addActionsColumn() {
        colActions.setCellValueFactory(cellData -> new SimpleStringProperty("Actions"));

        colActions.setCellFactory(new Callback<>() {
            @Override
            public TableCell<Reservation, String> call(TableColumn<Reservation, String> param) {
                return new TableCell<>() {
                    final Button editButton = new Button("Modifier");
                    final Button deleteButton = new Button("Supprimer");
                    final HBox hBox = new HBox(10, editButton, deleteButton);

                    {
                        editButton.setOnAction(event -> handleEdit(getTableRow().getItem()));
                        deleteButton.setOnAction(event -> handleDelete(getTableRow().getItem()));
                    }

                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        setGraphic(empty ? null : hBox);
                    }
                };
            }
        });
    }

    private void handleEdit(Reservation reservation) {
        if (reservation == null) return;

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModifierReservation.fxml"));
            AnchorPane modifReservationLayout = loader.load();

            ModifierReservation controller = loader.getController();
           // controller.setReservationToEdit(reservation);

            Scene currentScene = tableView.getScene();
            currentScene.setRoot(modifReservationLayout);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Erreur lors du chargement de ModifierReservation.fxml");
        }
    }

    private void handleDelete(Reservation reservation) {
        if (reservation == null) return;

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation de suppression");
        alert.setHeaderText("Supprimer la réservation");
        alert.setContentText("Êtes-vous sûr de vouloir supprimer cette réservation ?");

        ButtonType buttonYes = new ButtonType("Oui", ButtonBar.ButtonData.YES);
        ButtonType buttonNo = new ButtonType("Non", ButtonBar.ButtonData.NO);
        alert.getButtonTypes().setAll(buttonYes, buttonNo);

        alert.showAndWait().ifPresent(response -> {
            if (response == buttonYes) {
                reservationService.supprimer(reservation);
                loadReservations();
            }
        });
    }

    @FXML
    private void addReservation() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterReservation.fxml"));
        Scene scene = new Scene(loader.load());
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Ajouter une réservation");
        stage.show();
    }

    @FXML
    private void refreshList() {
        loadReservations();
    }

    @FXML
    private void goToReservation(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Reservation.fxml"));
        AnchorPane reservationLayout = loader.load();
        Scene scene = new Scene(reservationLayout);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
}
