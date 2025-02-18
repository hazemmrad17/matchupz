package controllers.EspaceSportif;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleFloatProperty;
import models.EspaceSportif.EspaceSportif;
import services.EspaceSportif.EspaceSportifService;

import java.util.List;
import java.util.Optional;

public class AjouterEspace {

    @FXML
    private TextField textFieldNom, textFieldAdresse, textFieldCapacite;

    @FXML
    private ComboBox<String> comboBoxCategorie;

    @FXML
    private Button btnAjout, btnAnnuler, btnSupprimer, btnModifier, btnActualiser;

    @FXML
    private TableView<EspaceSportif> tableViewEspaces;

    @FXML
    private TableColumn<EspaceSportif, String> colNom, colAdresse, colCategorie;

    @FXML
    private TableColumn<EspaceSportif, Float> colCapacite;

    private final EspaceSportifService espaceSportifService = new EspaceSportifService();
    private ObservableList<EspaceSportif> espaceList = FXCollections.observableArrayList();
    private EspaceSportif selectedEspace = null;

    @FXML
    public void initialize() {
        // Set up column bindings
        colNom.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNomEspace()));
        colAdresse.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAdresse()));
        colCategorie.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCategorie()));
        colCapacite.setCellValueFactory(cellData -> new SimpleFloatProperty(cellData.getValue().getCapacite()).asObject());


        tableViewEspaces.setItems(espaceList);
        loadEspaceSportifs();

        // Event Listeners
        btnAjout.setOnAction(event -> handleAjouterOuModifier());
        btnAnnuler.setOnAction(event -> clearFields());
        btnSupprimer.setOnAction(event -> handleSupprimer());
        btnActualiser.setOnAction(event -> loadEspaceSportifs());

        // Selection Event
        tableViewEspaces.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                selectedEspace = newSelection;
                textFieldNom.setText(selectedEspace.getNomEspace());
                textFieldAdresse.setText(selectedEspace.getAdresse());
                comboBoxCategorie.setValue(selectedEspace.getCategorie());
                textFieldCapacite.setText(String.valueOf(selectedEspace.getCapacite()));
            }
        });
    }

    private void loadEspaceSportifs() {
        espaceList.clear();
        List<EspaceSportif> espaces = espaceSportifService.rechercher();
        espaceList.addAll(espaces);
    }

    private void handleAjouterOuModifier() {
        String nom = textFieldNom.getText();
        String adresse = textFieldAdresse.getText();
        String categorie = comboBoxCategorie.getValue();
        float capacite;

        try {
            capacite = Float.parseFloat(textFieldCapacite.getText());
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Capacité invalide !");
            return;
        }

        if (selectedEspace == null) {
            EspaceSportif espace = new EspaceSportif(nom, adresse, categorie, capacite);
            espaceSportifService.ajouter(espace);
            espaceList.add(espace);
        } else {
            selectedEspace.setNomEspace(nom);
            selectedEspace.setAdresse(adresse);
            selectedEspace.setCategorie(categorie);
            selectedEspace.setCapacite(capacite);
            espaceSportifService.modifier(selectedEspace);
            loadEspaceSportifs();
        }
        clearFields();
    }

    private void handleSupprimer() {
        EspaceSportif selected = tableViewEspaces.getSelectionModel().getSelectedItem();
        if (selected != null) {
            espaceSportifService.supprimer(selected);
            espaceList.remove(selected);
        }
    }

    private void clearFields() {
        textFieldNom.clear();
        textFieldAdresse.clear();
        comboBoxCategorie.setValue(null);
        textFieldCapacite.clear();
        selectedEspace = null;
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
