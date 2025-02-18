package controllers.EspaceSportif;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import models.EspaceSportif.EspaceSportif;
import services.EspaceSportif.EspaceSportifService;

import java.util.List;
import java.util.Optional;

public class AjouterEspace {

    @FXML
    private TableColumn<EspaceSportif, Integer> colId;

    @FXML
    private TextField textFieldNom, textFieldAdresse, textFieldCapacite;

    @FXML
    private ComboBox<String> comboBoxCategorie;

    @FXML
    private Button btnAjout, btnAnnuler, btnActualiser;

    @FXML
    private TableView<EspaceSportif> tableViewEspaces;

    @FXML
    private TableColumn<EspaceSportif, String> colNom, colAdresse, colCategorie;

    @FXML
    private TableColumn<EspaceSportif, Float> colCapacite;

    @FXML
    private TableColumn<EspaceSportif, Void> colActions;

    private final EspaceSportifService espaceSportifService = new EspaceSportifService();
    private ObservableList<EspaceSportif> espaceList = FXCollections.observableArrayList();
    private EspaceSportif selectedEspace = null;

    @FXML
    public void initialize() {
        // Set up column bindings
        colId.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getIdLieu()).asObject());
        colNom.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNomEspace()));
        colAdresse.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAdresse()));
        colCapacite.setCellValueFactory(cellData -> new SimpleFloatProperty(cellData.getValue().getCapacite()).asObject());
        colCategorie.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCategorie()));

        // Ajouter une colonne d'actions
        colActions.setCellFactory(param -> new TableCell<>() {
            private final Button btnModifier = new Button("✏");
            private final Button btnSupprimer = new Button("🗑");
            private final HBox container = new HBox(10, btnModifier, btnSupprimer);

            {
                btnModifier.getStyleClass().add("edit-button");
                btnSupprimer.getStyleClass().add("delete-button");

                btnModifier.setOnAction(event -> {
                    EspaceSportif espace = getTableView().getItems().get(getIndex());
                    remplirChampsPourModification(espace);
                });

                btnSupprimer.setOnAction(event -> {
                    EspaceSportif espace = getTableView().getItems().get(getIndex());
                    handleSupprimer(espace);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(container);
                }
            }
        });

        // Charger les catégories depuis la base de données
        List<String> categories = espaceSportifService.getCategories();
        comboBoxCategorie.setItems(FXCollections.observableArrayList(categories));

        tableViewEspaces.setItems(espaceList);
        loadEspaceSportifs();

        // Event Listeners
        btnAjout.setOnAction(event -> handleAjouterOuModifier());
        btnAnnuler.setOnAction(event -> clearFields());
        btnActualiser.setOnAction(event -> loadEspaceSportifs());
    }

    private void loadEspaceSportifs() {
        espaceList.clear();
        List<EspaceSportif> espaces = espaceSportifService.rechercher();
        espaceList.addAll(espaces);
    }

    private void handleAjouterOuModifier() {
        if (!validerChamps()) {
            return;
        }

        String nom = textFieldNom.getText().trim();
        String adresse = textFieldAdresse.getText().trim();
        String categorie = comboBoxCategorie.getValue();
        float capacite = Float.parseFloat(textFieldCapacite.getText());

        if (selectedEspace == null) {
            // Ajout d'un nouvel espace
            EspaceSportif espace = new EspaceSportif(nom, adresse, categorie, capacite);
            espaceSportifService.ajouter(espace);
            espaceList.add(espace);
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Espace sportif ajouté avec succès !");
        } else {
            // Modification d'un espace existant
            selectedEspace.setNomEspace(nom);
            selectedEspace.setAdresse(adresse);
            selectedEspace.setCategorie(categorie);
            selectedEspace.setCapacite(capacite);
            espaceSportifService.modifier(selectedEspace);
            loadEspaceSportifs();
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Espace sportif modifié avec succès !");
        }

        clearFields();
    }

    private void handleSupprimer(EspaceSportif espace) {
        // Demande de confirmation
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirmation");
        confirmation.setHeaderText(null);
        confirmation.setContentText("Voulez-vous vraiment supprimer cet espace ?");
        Optional<ButtonType> result = confirmation.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            espaceSportifService.supprimer(espace);
            espaceList.remove(espace);
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Espace supprimé !");
        }
    }

    private void remplirChampsPourModification(EspaceSportif espace) {
        selectedEspace = espace;
        textFieldNom.setText(espace.getNomEspace());
        textFieldAdresse.setText(espace.getAdresse());
        comboBoxCategorie.setValue(espace.getCategorie());
        textFieldCapacite.setText(String.valueOf(espace.getCapacite()));
    }

    private void clearFields() {
        textFieldNom.clear();
        textFieldAdresse.clear();
        comboBoxCategorie.setValue(null);
        textFieldCapacite.clear();
        selectedEspace = null;
    }

    /**
     * 🔥 Contrôle de saisie des champs
     */
    private boolean validerChamps() {
        String nom = textFieldNom.getText().trim();
        String adresse = textFieldAdresse.getText().trim();
        String categorie = comboBoxCategorie.getValue();
        String capaciteText = textFieldCapacite.getText().trim();

        if (nom.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Erreur de saisie", "Le champ 'Nom' est obligatoire !");
            return false;
        }

        if (adresse.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Erreur de saisie", "Le champ 'Adresse' est obligatoire !");
            return false;
        }

        if (categorie == null || categorie.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Erreur de saisie", "Veuillez sélectionner une catégorie !");
            return false;
        }

        try {
            float capacite = Float.parseFloat(capaciteText);
            if (capacite <= 0) {
                showAlert(Alert.AlertType.ERROR, "Erreur de saisie", "La capacité doit être un nombre positif !");
                return false;
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur de saisie", "La capacité doit être un nombre valide !");
            return false;
        }

        return true;
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
