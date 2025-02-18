package controllers.EspaceSportif;

import models.EspaceSportif.EspaceSportif;
import services.EspaceSportif.EspaceSportifService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleFloatProperty;

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
    private Button btnSupprimer;

    private final EspaceSportifService espaceSportifService = new EspaceSportifService();
    private final ObservableList<EspaceSportif> espaceList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Set up column bindings
        colNom.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNomEspace()));
        colAdresse.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAdresse()));
        colCategorie.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCategorie()));
        colCapacite.setCellValueFactory(cellData -> new SimpleFloatProperty(cellData.getValue().getCapacite()).asObject());

        tableViewEspace.setItems(espaceList);
        loadEspaceSportifs();

        btnSupprimer.setOnAction(event -> handleSupprimer());
    }

    private void loadEspaceSportifs() {
        espaceList.clear();  // Prevent duplication
        List<EspaceSportif> espaces = espaceSportifService.rechercher();
        espaceList.addAll(espaces);
    }

    private void handleSupprimer() {
        EspaceSportif selectedEspace = tableViewEspace.getSelectionModel().getSelectedItem();
        if (selectedEspace != null) {
            // Confirmation dialog before deleting
            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmAlert.setTitle("Confirmation de suppression");
            confirmAlert.setHeaderText(null);
            confirmAlert.setContentText("Voulez-vous vraiment supprimer cet espace sportif ?");
            Optional<ButtonType> result = confirmAlert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                try {
                    espaceSportifService.supprimer(selectedEspace);
                    espaceList.remove(selectedEspace);
                    showAlert(Alert.AlertType.INFORMATION, "Suppression réussie", "L'espace sportif a été supprimé.");
                } catch (Exception e) {
                    showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de supprimer cet espace sportif.");
                    e.printStackTrace();
                }
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Aucune sélection", "Veuillez sélectionner un espace sportif à supprimer.");
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
