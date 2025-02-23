package controllers.fournisseur;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import models.logistics.Fournisseur;
import services.logistics.FournisseurService;

import java.io.IOException;

public class DisplayFournisseur {

    @FXML
    private Button Home;

    @FXML
    private Button fournisseurButton;

    @FXML
    private Button addFournisseurButton;

    @FXML
    private TableView<Fournisseur> tableView;

    @FXML
    private TableColumn<Fournisseur, Integer> idColumn;

    @FXML
    private TableColumn<Fournisseur, String> nomColumn;

    @FXML
    private TableColumn<Fournisseur, String> emailColumn;

    @FXML
    private TableColumn<Fournisseur, String> adresseColumn;

    @FXML
    private TableColumn<Fournisseur, String> categorieProduitColumn;

    @FXML
    private TableColumn<Fournisseur, Void> modifierColumn;

    @FXML
    private TableColumn<Fournisseur, Void> deleteColumn;

    private ObservableList<Fournisseur> fournisseurList = FXCollections.observableArrayList();
    private FournisseurService fournisseurService = new FournisseurService();

    @FXML
    private void handleHome() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Home.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) Home.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            showError("Erreur de chargement", "Impossible de charger Home.fxml", e);
        }
    }

    @FXML
    private void handleFournisseur() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fournisseur/DisplayFournisseur.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) fournisseurButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            showError("Erreur de chargement", "Impossible de charger DisplayFournisseur.fxml", e);
        }
    }

    @FXML
    private void handleAddFournisseurButton() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fournisseur/AjoutFournisseur.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) addFournisseurButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            showError("Erreur de chargement", "Impossible de charger AjoutFournisseur.fxml", e);
        }
    }

    private void openModifyWindow(Fournisseur fournisseur, Stage stage) {
        try {
            String fxmlPath = "/fournisseur/ModifierFournisseur.fxml";
            System.out.println("➡️ Tentative de chargement du fichier FXML : " + fxmlPath);

            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            if (loader.getLocation() == null) {
                throw new IOException("❌ ERREUR : Fichier FXML non trouvé -> " + fxmlPath);
            }

            Parent root = loader.load();
            System.out.println("✅ FXML chargé avec succès !");

            ModifyFournisseur controller = loader.getController();
            controller.setFournisseurToModify(fournisseur);

            stage.setScene(new Scene(root));
            stage.show();

            stage.setOnHidden(event -> loadFournisseurs());
        } catch (IOException e) {
            System.err.println("❌ Erreur lors du chargement de ModifyFournisseur.fxml : " + e.getMessage());
            e.printStackTrace();
        }
    }


    @FXML
    public void initialize() {

        nomColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNom()));
        emailColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEmail()));
        adresseColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAdresse()));
        categorieProduitColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCategorie_produit()));

        modifierColumn.setCellFactory(param -> new TableCell<Fournisseur, Void>() {
            private final Button btn = new Button("Modifier");

            {
                btn.setOnAction(event -> {
                    Fournisseur selectedFournisseur = getTableView().getItems().get(getIndex());

                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Confirmation");
                    alert.setHeaderText("Modifier ce fournisseur ?");
                    alert.setContentText("Fournisseur : " + selectedFournisseur.getNom());

                    alert.showAndWait().ifPresent(response -> {
                        if (response == ButtonType.OK) {
                            Stage stage = (Stage) btn.getScene().getWindow();
                            openModifyWindow(selectedFournisseur, stage);
                        }
                    });
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btn);
            }
        });

        deleteColumn.setCellFactory(param -> new TableCell<Fournisseur, Void>() {
            private final Button btn = new Button("Supprimer");

            {
                btn.setOnAction(event -> {
                    Fournisseur selectedFournisseur = getTableView().getItems().get(getIndex());

                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Confirmation");
                    alert.setHeaderText("Supprimer ce fournisseur ?");
                    alert.setContentText("Fournisseur : " + selectedFournisseur.getNom());

                    alert.showAndWait().ifPresent(response -> {
                        if (response == ButtonType.OK) {
                            fournisseurService.supprimer(selectedFournisseur);
                            loadFournisseurs();
                        }
                    });
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btn);
            }
        });

        loadFournisseurs();
    }

    public void loadFournisseurs() {
        fournisseurList.clear();
        fournisseurList.addAll(fournisseurService.recherche());
        tableView.setItems(fournisseurList);
    }

    @FXML
    private Button log_out;

    @FXML
    void log_out(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/user/interfaceA.fxml"));
            Stage stage = (Stage) log_out.getScene().getWindow();
            stage.setScene(new Scene(loader.load()));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de charger la page d'inscription.");
        }

    }

    private void showAlert(Alert.AlertType alertType, String erreur, String s) {
    }

    private void showError(String title, String header, Exception e) {
        e.printStackTrace();
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText("Détails : " + e.getMessage());
        alert.showAndWait();
    }
}
