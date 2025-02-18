package controllers.EspaceSportif;

import models.EspaceSportif.EspaceSportif;
import services.EspaceSportif.EspaceSportifService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.shape.SVGPath;
import javafx.geometry.Insets;

import java.util.List;
import java.util.Optional;

public class AffichageEspace {

    @FXML
    private TableView<EspaceSportif> tableViewEspace;

    @FXML
    private TableColumn<EspaceSportif, String> colNom;

    @FXML
    private TableColumn<EspaceSportif, String> colAdresse;

    @FXML
    private TableColumn<EspaceSportif, String> colCategorie;

    @FXML
    private TableColumn<EspaceSportif, Float> colCapacite;

    @FXML
    private TableColumn<EspaceSportif, Void> colActions;

    private final EspaceSportifService espaceSportifService = new EspaceSportifService();
    private final ObservableList<EspaceSportif> espaceList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colNom.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNomEspace()));
        colAdresse.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAdresse()));
        colCategorie.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCategorie()));
        colCapacite.setCellValueFactory(cellData -> new SimpleFloatProperty(cellData.getValue().getCapacite()).asObject());

        addActionButtons();

        tableViewEspace.setItems(espaceList);
        loadEspaceSportifs();
    }

    private void loadEspaceSportifs() {
        espaceList.clear();
        List<EspaceSportif> espaces = espaceSportifService.rechercher();
        espaceList.addAll(espaces);
    }

    private void addActionButtons() {
        colActions.setCellFactory(param -> new TableCell<>() {
            private final Button btnEdit = new Button();
            private final Button btnDelete = new Button();
            private final HBox actionBox = new HBox(10);

            {
                // Icône de modification (✏️)
                SVGPath editIcon = new SVGPath();
                editIcon.setContent("M3 17.25V21h3.75l11.06-11.06-3.75-3.75L3 17.25zM20.71 5.63a1 1 0 0 0 0-1.41l-2.83-2.83a1 1 0 0 0-1.41 0L14 2.46l3.75 3.75 2.96-2.58z");
                editIcon.setStyle("-fx-fill: #2a9df4; -fx-scale-x: 1.2; -fx-scale-y: 1.2;");

                btnEdit.setGraphic(editIcon);
                btnEdit.setStyle("-fx-background-color: transparent;");

                // Icône de suppression (❌)
                SVGPath deleteIcon = new SVGPath();
                deleteIcon.setContent("M6 19c0 1.1.9 2 2 2h8c1.1 0 2-.9 2-2V7H6v12zM19 4h-3.5l-1-1h-5l-1 1H5v2h14V4z");
                deleteIcon.setStyle("-fx-fill: #d9534f; -fx-scale-x: 1.2; -fx-scale-y: 1.2;");

                btnDelete.setGraphic(deleteIcon);
                btnDelete.setStyle("-fx-background-color: transparent;");

                actionBox.getChildren().addAll(btnEdit, btnDelete);
                actionBox.setPadding(new Insets(5));

                btnEdit.setOnAction(event -> {
                    EspaceSportif espace = getTableView().getItems().get(getIndex());
                    modifierEspace(espace);
                });

                btnDelete.setOnAction(event -> {
                    EspaceSportif espace = getTableView().getItems().get(getIndex());
                    handleSupprimer(espace);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : actionBox);
            }
        });
    }

    private void modifierEspace(EspaceSportif espace) {
        showAlert(Alert.AlertType.INFORMATION, "Modification", "Modifier l'espace : " + espace.getNomEspace());
    }

    private void handleSupprimer(EspaceSportif espace) {
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirmation de suppression");
        confirmAlert.setHeaderText(null);
        confirmAlert.setContentText("Voulez-vous vraiment supprimer cet espace sportif ?");
        Optional<ButtonType> result = confirmAlert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            espaceSportifService.supprimer(espace);
            espaceList.remove(espace);
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Espace supprimé.");
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
