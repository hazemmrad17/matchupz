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
import models.logistics.Materiel;
import models.match.SessionManager;
import models.utilisateur.Role;
import models.utilisateur.User;
import services.logistics.MaterielService;


import java.io.IOException;

public class DisplayMateriel {
    @FXML
    private Button bt_user;

    @FXML
    private Button dashboard;
    @FXML
    private Button espace;
    @FXML
    private Button logistique;

    @FXML
    private Button Home;

    @FXML
    private Button materielButton;

    @FXML
    private Button fournisseurButton;

    @FXML
    private Button addMaterielButton;

    @FXML
    private Button log_out; // Added field to bind the logout button

    @FXML
    private TableView<Materiel> tableView;

    @FXML
    private TableColumn<Materiel, Integer> idColumn;

    @FXML
    private TableColumn<Materiel, String> nomColumn;

    @FXML
    private TableColumn<Materiel, String> typeColumn;

    @FXML
    private TableColumn<Materiel, Integer> quantiteColumn;

    @FXML
    private TableColumn<Materiel, String> etatColumn;

    @FXML
    private TableColumn<Materiel, Float> prixUnitaireColumn;

    @FXML
    private TableColumn<Materiel, Void> modifierColumn;

    @FXML
    private TableColumn<Materiel, Void> deleteColumn;

    private ObservableList<Materiel> materielList = FXCollections.observableArrayList();
    private MaterielService materielService = new MaterielService();


    @FXML
    void user(javafx.event.ActionEvent event) {
        User user = SessionManager.getCurrentUser();
        if (user != null && user.getRole() == Role.ADMIN) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/user/adminpage.fxml"));
                Stage stage = (Stage) bt_user.getScene().getWindow();
                stage.setScene(new Scene(loader.load()));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de charger la page d'inscription.");
            }
        } else {
            System.out.println("Aucun utilisateur connecté ou pas ADMIN !");
        }
    }

    @FXML
    void dashboard(javafx.event.ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Dashboard.fxml"));
            Stage stage = (Stage) dashboard.getScene().getWindow();
            stage.setScene(new Scene(loader.load()));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de charger la page d'inscription.");
        }

    }

    @FXML
    void espace(javafx.event.ActionEvent event) {
        User user = SessionManager.getCurrentUser();
        if (user != null && user.getRole() == Role.ADMIN) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/EspaceSportif/AffichageEspace.fxml"));
                Stage stage = (Stage) espace.getScene().getWindow();
                stage.setScene(new Scene(loader.load()));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de charger la page d'inscription.");
            }
        } else {
            System.out.println("Aucun utilisateur connecté ou pas ADMIN !");
        }
    }

    @FXML
    void logistique(javafx.event.ActionEvent event) {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fournisseur/AfficherFournisseur.fxml"));
            Stage stage = (Stage) dashboard.getScene().getWindow();
            stage.setScene(new Scene(loader.load()));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de charger la page d'inscription.");
        }

    }

    @FXML
    void sponsor(javafx.event.ActionEvent event) {


        User user = SessionManager.getCurrentUser();
        if (user != null && user.getRole() == Role.ADMIN) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/sponsoring/AfficherSponsor.fxml"));
                Stage stage = (Stage) dashboard.getScene().getWindow();
                stage.setScene(new Scene(loader.load()));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de charger la page d'inscription.");
            }
        } else {
            System.out.println("Aucun utilisateur connecté ou pas ADMIN !");
        }

    }



    @FXML
    void log_out(javafx.event.ActionEvent event) {
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

    @FXML
    void match(javafx.event.ActionEvent event) {
        User user = SessionManager.getCurrentUser();
        if (user != null && user.getRole() == Role.ADMIN) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/MatchSchedule/AffichageMatch.fxml"));
                Stage stage = (Stage) bt_user.getScene().getWindow();
                stage.setScene(new Scene(loader.load()));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de charger la page d'inscription.");
            }
        } else {
            System.out.println("Aucun utilisateur connecté ou pas ADMIN !");
        }

    }

    private void showAlert(Alert.AlertType alertType, String erreur, String s) {
    }

    @FXML
    void teams(javafx.event.ActionEvent event) {


        User user = SessionManager.getCurrentUser();
        if (user != null && user.getRole() == Role.ADMIN) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/joueur/MainController.fxml"));
                Stage stage = (Stage) bt_user.getScene().getWindow();
                stage.setScene(new Scene(loader.load()));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de charger la page d'inscription.");
            }
        } else {
            System.out.println("Aucun utilisateur connecté ou pas ADMIN !");
        }

    }


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
    private void handleMateriel() {
        loadMateriels(); // Refresh the current view
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
    private void handleAddMaterielButton() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fournisseur/AjoutMateriel.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) addMaterielButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            showError("Erreur de chargement", "Impossible de charger AjoutMateriel.fxml", e);
        }
    }
 

    private void openModifyWindow(Materiel materiel, Stage stage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/materiel/ModifyMateriel.fxml"));
            Parent root = loader.load();
            ModifyMateriel controller = loader.getController();
            controller.setMaterielToModify(materiel);
            stage.setScene(new Scene(root));
            stage.show();
            stage.setOnHidden(event -> loadMateriels());
        } catch (IOException e) {
            System.err.println("Erreur lors du chargement de ModifyMateriel.fxml : " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getId_materiel()).asObject());
        nomColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNom()));
        typeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getType().toString()));
        quantiteColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getQuantite()).asObject());
        etatColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEtat().toString()));
        prixUnitaireColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleFloatProperty(cellData.getValue().getPrix_unitaire()).asObject());

        modifierColumn.setCellFactory(param -> new TableCell<Materiel, Void>() {
            private final Button btn = new Button("Modifier");

            {
                btn.setOnAction(event -> {
                    Materiel selectedMateriel = getTableView().getItems().get(getIndex());
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Confirmation");
                    alert.setHeaderText("Modifier ce matériel ?");
                    alert.setContentText("Matériel : " + selectedMateriel.getNom());
                    alert.showAndWait().ifPresent(response -> {
                        if (response == ButtonType.OK) {
                            Stage stage = (Stage) btn.getScene().getWindow();
                            openModifyWindow(selectedMateriel, stage);
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

        deleteColumn.setCellFactory(param -> new TableCell<Materiel, Void>() {
            private final Button btn = new Button("Supprimer");

            {
                btn.setOnAction(event -> {
                    Materiel selectedMateriel = getTableView().getItems().get(getIndex());
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Confirmation");
                    alert.setHeaderText("Supprimer ce matériel ?");
                    alert.setContentText("Matériel : " + selectedMateriel.getNom());
                    alert.showAndWait().ifPresent(response -> {
                        if (response == ButtonType.OK) {
                            materielService.supprimer(selectedMateriel);
                            loadMateriels();
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

        loadMateriels();
    }

    public void loadMateriels() {
        materielList.clear();
        materielList.addAll(materielService.recherche());
        tableView.setItems(materielList);
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