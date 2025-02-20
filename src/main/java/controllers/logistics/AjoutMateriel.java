package controllers.logistics;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import models.logistics.EtatMateriel;
import models.logistics.Materiel;
import models.logistics.TypeMateriel;
import services.logistics.MaterielService;

import java.io.IOException;

public class AjoutMateriel {

    @FXML
    private Button btnAjout;

    @FXML
    private TextField textFieldNom, textFieldQuantite, textFieldPrix;

    @FXML
    private ComboBox<TypeMateriel> comboBoxType;

    @FXML
    private ComboBox<EtatMateriel> comboBoxEtat;

    @FXML
    private TableView<Materiel> materielTable;

    @FXML
    private TableColumn<Materiel, String> colNom, colType, colEtat;

    @FXML
    private TableColumn<Materiel, Integer> colQuantite;

    @FXML
    private TableColumn<Materiel, Float> colPrix;

    @FXML
    private TableColumn<Materiel, Void> colModifier, colSupprimer;

    private ObservableList<Materiel> materielList = FXCollections.observableArrayList();

    private MaterielService materielService = new MaterielService();

    @FXML
    private AnchorPane mainContainer; // This should be the root container in the FXML file

    @FXML
    void switchToFournisseur() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjoutFournisseur.fxml"));
            AnchorPane newView = loader.load();

            // Replace the content inside mainContainer with the new FXML view
            mainContainer.getChildren().setAll(newView);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    void switchToMateriel() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjoutMateriel.fxml"));
            AnchorPane newView = loader.load();

            // Replace the content inside mainContainer with the new FXML view
            mainContainer.getChildren().setAll(newView);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void initialize() {
        // Remplir les ComboBox
        comboBoxType.getItems().addAll(TypeMateriel.values());
        comboBoxEtat.getItems().addAll(EtatMateriel.values());

        // Lier les colonnes aux données
        colNom.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNom()));
        colType.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getType().name()));
        colEtat.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEtat().name()));
        colQuantite.setCellValueFactory(new PropertyValueFactory<>("quantite"));
        colPrix.setCellValueFactory(new PropertyValueFactory<>("prix_unitaire"));

        // Ajouter boutons Modifier et Supprimer
        addActionButtons();

        // Charger les matériels depuis la base de données
        materielList.addAll(materielService.recherche());
        materielTable.setItems(materielList);
    }

    // Ajouter Materiel
    @FXML
    void ajouterMateriel(ActionEvent event) {
        // Vérifier si les champs sont vides
        if (textFieldNom.getText().trim().isEmpty() || textFieldQuantite.getText().trim().isEmpty() ||
                textFieldPrix.getText().trim().isEmpty() || comboBoxType.getValue() == null ||
                comboBoxEtat.getValue() == null) {

            showAlert(Alert.AlertType.WARNING, "Champs obligatoires", "Veuillez remplir tous les champs !");
            return;
        }

        // Vérifier que le nom contient uniquement des lettres et des espaces
        if (!textFieldNom.getText().matches("^[a-zA-Z\\s]+$")) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Le nom doit contenir uniquement des lettres !");
            return;
        }

        try {
            int quantite = Integer.parseInt(textFieldQuantite.getText().trim());
            if (quantite <= 0) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "La quantité doit être un nombre entier positif !");
                return;
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Quantité invalide ! Entrez un nombre entier.");
            return;
        }

        try {
            float prix = Float.parseFloat(textFieldPrix.getText().trim());
            if (prix <= 0) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Le prix doit être un nombre positif !");
                return;
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Prix invalide ! Entrez un nombre décimal.");
            return;
        }

        // Création du matériel si toutes les validations sont réussies
        Materiel newMateriel = new Materiel(
                1, // ID fournisseur temporaire
                textFieldNom.getText().trim(),
                comboBoxType.getValue(),
                Integer.parseInt(textFieldQuantite.getText().trim()),
                comboBoxEtat.getValue(),
                Float.parseFloat(textFieldPrix.getText().trim())
        );

        // Ajouter le matériel à la liste et à la base de données
        materielList.add(newMateriel);
        materielService.ajouter(newMateriel);

        // Afficher un message de succès et réinitialiser les champs
        showAlert(Alert.AlertType.CONFIRMATION, "Succès", "Matériel ajouté avec succès !");
        clearFields();
    }

    // Ajouter boutons Modifier et Supprimer
    private void addActionButtons() {
        colModifier.setCellFactory(param -> new TableCell<>() {
            private final Button btnModify = new Button("✏️");

            {
                btnModify.setOnAction(event -> {
                    Materiel materiel = getTableView().getItems().get(getIndex());
                    modifyMateriel(materiel);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(btnModify);
                }
            }
        });

        colSupprimer.setCellFactory(param -> new TableCell<>() {
            private final Button btnDelete = new Button("🗑️");

            {
                btnDelete.setOnAction(event -> {
                    Materiel materiel = getTableView().getItems().get(getIndex());
                    deleteMateriel(materiel);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(btnDelete);
                }
            }
        });
    }

    private void modifyMateriel(Materiel materiel) {
        // Pré-remplir les champs avec les données du matériel sélectionné
        textFieldNom.setText(materiel.getNom());
        textFieldQuantite.setText(String.valueOf(materiel.getQuantite()));
        textFieldPrix.setText(String.valueOf(materiel.getPrix_unitaire()));
        comboBoxType.setValue(materiel.getType());
        comboBoxEtat.setValue(materiel.getEtat());

        btnAjout.setText("Modifier");

        btnAjout.setOnAction(event -> {
            // Vérifier si les champs sont vides
            if (textFieldNom.getText().trim().isEmpty() || textFieldQuantite.getText().trim().isEmpty() ||
                    textFieldPrix.getText().trim().isEmpty() || comboBoxType.getValue() == null ||
                    comboBoxEtat.getValue() == null) {

                showAlert(Alert.AlertType.WARNING, "Champs obligatoires", "Veuillez remplir tous les champs !");
                return;
            }

            // Vérifier que le nom contient uniquement des lettres et des espaces
            if (!textFieldNom.getText().matches("^[a-zA-Z\\s]+$")) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Le nom doit contenir uniquement des lettres !");
                return;
            }

            // Vérifier que la quantité est un nombre entier positif
            int quantite;
            try {
                quantite = Integer.parseInt(textFieldQuantite.getText().trim());
                if (quantite <= 0) {
                    showAlert(Alert.AlertType.ERROR, "Erreur", "La quantité doit être un nombre entier positif !");
                    return;
                }
            } catch (NumberFormatException e) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Quantité invalide ! Entrez un nombre entier.");
                return;
            }

            // Vérifier que le prix est un nombre décimal positif
            float prix;
            try {
                prix = Float.parseFloat(textFieldPrix.getText().trim());
                if (prix <= 0) {
                    showAlert(Alert.AlertType.ERROR, "Erreur", "Le prix doit être un nombre positif !");
                    return;
                }
            } catch (NumberFormatException e) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Prix invalide ! Entrez un nombre décimal.");
                return;
            }

            // Appliquer les modifications si tout est valide
            materiel.setNom(textFieldNom.getText().trim());
            materiel.setQuantite(quantite);
            materiel.setPrix_unitaire(prix);
            materiel.setType(comboBoxType.getValue());
            materiel.setEtat(comboBoxEtat.getValue());

            // Mettre à jour dans la base de données
            materielService.modifier(materiel);
            materielTable.refresh();

            // Afficher un message de confirmation et réinitialiser les champs
            showAlert(Alert.AlertType.INFORMATION, "Modification", "Matériel modifié avec succès !");
            btnAjout.setText("Ajouter");
            clearFields();
            btnAjout.setOnAction(this::ajouterMateriel);

        });
    }

    // Supprimer un matériel
    private void deleteMateriel(Materiel materiel) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Voulez-vous vraiment supprimer ce matériel ?", ButtonType.YES, ButtonType.NO);
        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                materielService.supprimer(materiel);
                materielList.remove(materiel);
                showAlert(Alert.AlertType.INFORMATION, "Suppression", "Matériel supprimé avec succès !");
            }
        });
    }

    // Afficher une alerte
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.show();
    }

    // Réinitialiser les champs
    private void clearFields() {
        textFieldNom.clear();
        textFieldQuantite.clear();
        textFieldPrix.clear();
        comboBoxType.setValue(null);
        comboBoxEtat.setValue(null);
    }
}
