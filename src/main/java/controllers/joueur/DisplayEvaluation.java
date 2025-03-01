package controllers.joueur;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import models.joueur.EvaluationPhysique;
import models.match.SessionManager;
import models.utilisateur.Role;
import models.utilisateur.User;
import services.joueur.EvaluationPhysiqueService;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DisplayEvaluation {
    @FXML
    private Button bt_user;
    @FXML
    private Button teams;
    @FXML
    private Button dashboard;
    @FXML
    private Button espace;
    @FXML
    private Button logistique;
    @FXML
    private Label nom_user;
    private void afficherProfil(User user) {

        if (user.getImage() != null && !user.getImage().isEmpty()) {
            javafx.scene.image.Image image = new javafx.scene.image.Image(user.getImage());
            String name = user.getPrenom();
            nom_user.setText(name);
            // profileImageView.setImage(image);

        }
    }
    @FXML
    void match(ActionEvent event) {

    }


    @FXML
    void pageuser(ActionEvent event) {
        User user = SessionManager.getCurrentUser();
        if (user != null) {

            String role = user.getRole().getValue();
            if (user.getRole() == Role.ADMIN)
            {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/user/adminpage.fxml"));
                    Stage stage = (Stage) bt_user.getScene().getWindow();
                    stage.setScene(new Scene(loader.load()));
                    stage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                    showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de charger la page d'inscription.");
                }
            }

        } else {
            System.out.println("Aucun utilisateur connecté !");
        }


    }
    @FXML
    void sponsor(ActionEvent event) {

    }

    @FXML
    void teams(ActionEvent event) {
        loadScene("/joueur/MainController.fxml",teams);
    }

    @FXML
    void dashboard(ActionEvent event) {

    }

    @FXML
    void espace(ActionEvent event) {
        User user = SessionManager.getCurrentUser();
        if (user != null) {

            String role = user.getRole().getValue();
            if (user.getRole() == Role.ADMIN)
            {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/AffichageEspace.fxml"));
                    Stage stage = (Stage) espace.getScene().getWindow();
                    stage.setScene(new Scene(loader.load()));
                    stage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                    showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de charger la page d'inscription.");
                }
            }

        } else {
            System.out.println("Aucun utilisateur connecté !");
        }


    }

    @FXML
    void logistique(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fournisseur/DisplayFournisseur.fxml"));
            Stage stage = (Stage) logistique.getScene().getWindow();
            stage.setScene(new Scene(loader.load()));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de charger la page d'inscription.");
        }


    }

    private void showAlert(Alert.AlertType alertType, String erreur, String content) {
    }


    @FXML private Button joueurButton;
    @FXML private Button homeButton;
    @FXML private Button addEvaluationButton;
    @FXML private TextField searchField;
    @FXML private TableView<EvaluationPhysique> tableView;
    @FXML private TableColumn<EvaluationPhysique, Integer> idEvaluationColumn;
    @FXML private TableColumn<EvaluationPhysique, Integer> idJoueurColumn;
    @FXML private TableColumn<EvaluationPhysique, Date> dateEvaluationColumn;
    @FXML private TableColumn<EvaluationPhysique, Float> niveauEnduranceColumn;
    @FXML private TableColumn<EvaluationPhysique, Float> forcePhysiqueColumn;
    @FXML private TableColumn<EvaluationPhysique, Float> vitesseColumn;
    @FXML private TableColumn<EvaluationPhysique, String> etatBlessureColumn;
    @FXML private TableColumn<EvaluationPhysique, Void> modifierColumn;
    @FXML private TableColumn<EvaluationPhysique, Void> deleteColumn;

    private ObservableList<EvaluationPhysique> evaluationList = FXCollections.observableArrayList();
    private ObservableList<EvaluationPhysique> filteredList = FXCollections.observableArrayList();
    private EvaluationPhysiqueService evaluationService = new EvaluationPhysiqueService();
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @FXML
    private void handleHome() {
        loadScene("/Home.fxml", homeButton);
    }

    @FXML
    private void handleJoueur() {
        loadScene("/joueur/MainController.fxml", joueurButton);
    }

    @FXML
    private void handleEvaluationButton() {
        loadScene("/joueur/AjoutEvaluation.fxml", addEvaluationButton);
    }

    private void loadScene(String fxmlPath, Button button) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            Stage stage = (Stage) button.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            showAlert("Error", "Failed to load the FXML file", e.getMessage());
        }
    }

    private void openModifyWindow(EvaluationPhysique evaluation, Stage stage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/joueur/ModifierEvaluation.fxml"));
            Parent root = loader.load();
            ModifyEvaluation controller = loader.getController();
            controller.setEvaluationToModify(evaluation);
            stage.setScene(new Scene(root));
            stage.show();
            stage.setOnHidden(event -> loadEvaluations()); // Refresh after modification
        } catch (IOException e) {
            showAlert("Error", "Failed to load the ModifierEvaluation.fxml", e.getMessage());
        }
    }

    @FXML
    private void initialize() {
        // Set up table columns
        idEvaluationColumn.setCellValueFactory(cellData -> cellData.getValue().idEvaluationProperty().asObject());
        idJoueurColumn.setCellValueFactory(cellData -> cellData.getValue().idJoueurProperty().asObject());
        dateEvaluationColumn.setCellValueFactory(cellData -> cellData.getValue().dateEvaluationProperty());
        niveauEnduranceColumn.setCellValueFactory(cellData -> cellData.getValue().niveauEnduranceProperty().asObject());
        forcePhysiqueColumn.setCellValueFactory(cellData -> cellData.getValue().forcePhysiqueProperty().asObject());
        vitesseColumn.setCellValueFactory(cellData -> cellData.getValue().vitesseProperty().asObject());
        etatBlessureColumn.setCellValueFactory(cellData -> cellData.getValue().etatBlessureProperty());

        // Modifier column
        modifierColumn.setCellFactory(param -> new TableCell<>() {
            private final Button btn = new Button("Modifier");
            {
                btn.setId("btn-modify");
                btn.setOnAction(event -> {
                    EvaluationPhysique selectedEvaluation = getTableView().getItems().get(getIndex());
                    showConfirmation("Confirmation", "Modify this evaluation?", "Evaluation ID: " + selectedEvaluation.getIdEvaluation(), () -> {
                        Stage stage = (Stage) btn.getScene().getWindow();
                        openModifyWindow(selectedEvaluation, stage);
                    });
                });
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btn);
            }
        });

        // Delete column
        deleteColumn.setCellFactory(param -> new TableCell<>() {
            private final Button btn = new Button("Supprimer");
            {
                btn.setId("btn-delete");
                btn.setOnAction(event -> {
                    EvaluationPhysique selectedEvaluation = getTableView().getItems().get(getIndex());
                    showConfirmation("Confirmation", "Delete this evaluation?", "Evaluation ID: " + selectedEvaluation.getIdEvaluation(), () -> {
                        evaluationService.supprimer(selectedEvaluation);
                        loadEvaluations();
                    });
                });
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btn);
            }
        });

        // Real-time search listener
        searchField.textProperty().addListener((observable, oldValue, newValue) -> filterEvaluations(newValue));

        // Load initial data
        loadEvaluations();
    }

    public void loadEvaluations() {
        evaluationList.clear();
        evaluationList.addAll(evaluationService.recherche());
        filteredList.clear();
        filteredList.addAll(evaluationList);
        tableView.setItems(filteredList);
    }

    @FXML
    private void handleSearch() {
        filterEvaluations(searchField.getText());
    }

    private void filterEvaluations(String searchText) {
        filteredList.clear();
        if (searchText == null || searchText.isEmpty()) {
            filteredList.addAll(evaluationList);
        } else {
            String lowerCaseFilter = searchText.toLowerCase().trim();
            for (EvaluationPhysique evaluation : evaluationList) {
                if (String.valueOf(evaluation.getIdJoueur()).contains(lowerCaseFilter) ||
                        dateFormat.format(evaluation.getDateEvaluation()).contains(lowerCaseFilter) ||
                        evaluation.getEtatBlessure().toLowerCase().contains(lowerCaseFilter)) {
                    filteredList.add(evaluation);
                }
            }
        }
        tableView.setItems(filteredList);
    }

    private void showAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void showConfirmation(String title, String header, String content, Runnable onConfirm) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                onConfirm.run();
            }
        });
    }
}