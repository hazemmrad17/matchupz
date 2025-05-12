package controllers.joueur;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import models.joueur.EvaluationPhysique;
import models.match.SessionManager;
import models.utilisateur.Role;
import models.utilisateur.User;
import services.joueur.EvaluationPhysiqueService;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.Date;
import java.util.Optional;

public class DisplayEvaluation {
    @FXML private Button bt_user;
    @FXML private Button teams;
    @FXML private Button dashboard;
    @FXML private Button espace;
    @FXML private Button logistique;
    @FXML private Label nom_user;
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
    @FXML private Button exportButton; // Added for export
    @FXML private Button sortButton;   // Added for sort

    private ObservableList<EvaluationPhysique> evaluationList = FXCollections.observableArrayList();
    private ObservableList<EvaluationPhysique> filteredList = FXCollections.observableArrayList();
    private EvaluationPhysiqueService evaluationService = new EvaluationPhysiqueService();
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    private void afficherProfil(User user) {
        if (user.getImage() != null && !user.getImage().isEmpty()) {
            javafx.scene.image.Image image = new javafx.scene.image.Image(user.getImage());
            String name = user.getPrenom();
            nom_user.setText(name);
        }
    }

    @FXML private Button playerStatsButton; // Add to FXML too

    @FXML
    private void handlePlayerStats() {
        loadScene("/joueur/PlayerStats.fxml", playerStatsButton);
    }
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

        User user = SessionManager.getCurrentUser();
        if (user != null && user.getRole() == Role.ADMIN) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fournisseur/AfficherFournisseur.fxml"));
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
    private Button log_out;

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

    @FXML
    void teams(javafx.event.ActionEvent event) {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/joueur/MainController.fxml"));
            Stage stage = (Stage) dashboard.getScene().getWindow();
            stage.setScene(new Scene(loader.load()));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de charger la page d'inscription.");
        }


    }




    private void loadScene(String fxmlPath, Button button) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            Stage stage = (Stage) button.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Failed to load the FXML file", e.getMessage());
        }
    }

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

    private void openModifyWindow(EvaluationPhysique evaluation, Stage stage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/joueur/ModifierEvaluation.fxml"));
            Parent root = loader.load();
            ModifyEvaluation controller = loader.getController();
            controller.setEvaluationToModify(evaluation);
            stage.setScene(new Scene(root));
            stage.show();
            stage.setOnHidden(event -> loadEvaluations());
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Failed to load the ModifierEvaluation.fxml", e.getMessage());
        }
    }

    @FXML
    private void initialize() {
        idEvaluationColumn.setCellValueFactory(cellData -> cellData.getValue().idEvaluationProperty().asObject());
        idJoueurColumn.setCellValueFactory(cellData -> cellData.getValue().idJoueurProperty().asObject());
        dateEvaluationColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getDateEvaluation()));
        niveauEnduranceColumn.setCellValueFactory(cellData -> cellData.getValue().niveauEnduranceProperty().asObject());
        forcePhysiqueColumn.setCellValueFactory(cellData -> cellData.getValue().forcePhysiqueProperty().asObject());
        vitesseColumn.setCellValueFactory(cellData -> cellData.getValue().vitesseProperty().asObject());
        etatBlessureColumn.setCellValueFactory(cellData -> cellData.getValue().etatBlessureProperty());

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

        searchField.textProperty().addListener((observable, oldValue, newValue) -> filterEvaluations(newValue));
        loadEvaluations();
    }

    public void loadEvaluations() {
        evaluationList.clear();
        evaluationList.addAll(evaluationService.recherche());
        filteredList.clear();
        filteredList.addAll(evaluationList);
        tableView.setItems(filteredList);
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

    // Export Functionality
    @FXML
    private void handleExport() {
        ObservableList<EvaluationPhysique> evaluations = tableView.getItems();
        if (evaluations.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Aucune Évaluation", "Il n'y a aucune évaluation à exporter !");
            return;
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Enregistrer la liste des évaluations");
        fileChooser.setInitialFileName("Evaluations_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".pdf");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));

        File file = fileChooser.showSaveDialog(tableView.getScene().getWindow());
        if (file == null) {
            showAlert(Alert.AlertType.INFORMATION, "Annulé", "L'exportation a été annulée.");
            return;
        }

        String fileName = file.getAbsolutePath();

        try {
            PdfWriter writer = new PdfWriter(fileName);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);
            document.setMargins(20, 20, 20, 20);

            PdfFont boldFont = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);
            PdfFont regularFont = PdfFontFactory.createFont(StandardFonts.HELVETICA);

            document.add(new Paragraph("Liste des Évaluations Physiques")
                    .setFont(boldFont)
                    .setFontSize(16)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginBottom(10));

            float[] columnWidths = {80, 80, 100, 80, 80, 80, 100}; // Adjusted for 7 columns
            Table pdfTable = new Table(UnitValue.createPointArray(columnWidths)).useAllAvailableWidth();

            String[] headers = {"ID Évaluation", "ID Joueur", "Date", "Endurance", "Force", "Vitesse", "État Blessure"};
            for (String header : headers) {
                pdfTable.addHeaderCell(new Cell()
                        .add(new Paragraph(header).setFont(boldFont).setFontSize(8).setTextAlignment(TextAlignment.CENTER))
                        .setPadding(3));
            }

            for (EvaluationPhysique evaluation : evaluations) {
                pdfTable.addCell(new Cell().add(new Paragraph(String.valueOf(evaluation.getIdEvaluation()))
                        .setFont(regularFont).setFontSize(8).setTextAlignment(TextAlignment.CENTER)));
                pdfTable.addCell(new Cell().add(new Paragraph(String.valueOf(evaluation.getIdJoueur()))
                        .setFont(regularFont).setFontSize(8).setTextAlignment(TextAlignment.CENTER)));
                pdfTable.addCell(new Cell().add(new Paragraph(dateFormat.format(evaluation.getDateEvaluation()))
                        .setFont(regularFont).setFontSize(8).setTextAlignment(TextAlignment.CENTER)));
                pdfTable.addCell(new Cell().add(new Paragraph(String.valueOf(evaluation.getNiveauEndurance()))
                        .setFont(regularFont).setFontSize(8).setTextAlignment(TextAlignment.CENTER)));
                pdfTable.addCell(new Cell().add(new Paragraph(String.valueOf(evaluation.getForcePhysique()))
                        .setFont(regularFont).setFontSize(8).setTextAlignment(TextAlignment.CENTER)));
                pdfTable.addCell(new Cell().add(new Paragraph(String.valueOf(evaluation.getVitesse()))
                        .setFont(regularFont).setFontSize(8).setTextAlignment(TextAlignment.CENTER)));
                pdfTable.addCell(new Cell().add(new Paragraph(evaluation.getEtatBlessure())
                        .setFont(regularFont).setFontSize(8).setTextAlignment(TextAlignment.CENTER)));
            }

            document.add(pdfTable);
            document.add(new Paragraph(" ").setMarginBottom(10));

            document.add(new Paragraph("Gestion des Évaluations Physiques")
                    .setFont(regularFont).setFontSize(12).setTextAlignment(TextAlignment.RIGHT));
            document.add(new Paragraph("Nexus Team 2025 ©")
                    .setFont(regularFont).setFontSize(12).setTextAlignment(TextAlignment.RIGHT));
            document.add(new Paragraph(" ").setMarginBottom(10));

            InputStream inputStream = getClass().getResourceAsStream("/images/logo_horizantalDARK.jpg");
            if (inputStream == null) {
                throw new IOException("Image resource not found: /images/logo_horizantalDARK.jpg");
            }
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                baos.write(buffer, 0, bytesRead);
            }
            inputStream.close();
            byte[] imageBytes = baos.toByteArray();
            Image logo = new Image(ImageDataFactory.create(imageBytes)).setWidth(184).setHeight(41);
            document.add(logo);

            document.close();

            showAlert(Alert.AlertType.INFORMATION, "Succès", "Les évaluations ont été exportées dans " + fileName + " !");

        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Échec de l'exportation des évaluations : " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Sort Functionality
    @FXML
    private void handleSort() {
        String[] sortableColumns = {
                "ID Évaluation", "ID Joueur", "Date", "Endurance", "Force", "Vitesse", "État Blessure"
        };
        ChoiceDialog<String> dialog = new ChoiceDialog<>("Date", sortableColumns);
        dialog.setTitle("Trier les Évaluations");
        dialog.setHeaderText("Sélectionnez une colonne pour trier en ordre croissant");
        dialog.setContentText("Colonne:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(this::sortTableByColumn);
    }

    private void sortTableByColumn(String columnName) {
        Comparator<EvaluationPhysique> comparator;
        switch (columnName) {
            case "ID Évaluation":
                comparator = Comparator.comparingInt(EvaluationPhysique::getIdEvaluation);
                break;
            case "ID Joueur":
                comparator = Comparator.comparingInt(EvaluationPhysique::getIdJoueur);
                break;
            case "Date":
                comparator = Comparator.comparing(EvaluationPhysique::getDateEvaluation);
                break;
            case "Endurance":
                comparator = Comparator.comparingDouble(EvaluationPhysique::getNiveauEndurance);
                break;
            case "Force":
                comparator = Comparator.comparingDouble(EvaluationPhysique::getForcePhysique);
                break;
            case "Vitesse":
                comparator = Comparator.comparingDouble(EvaluationPhysique::getVitesse);
                break;
            case "État Blessure":
                comparator = Comparator.comparing(EvaluationPhysique::getEtatBlessure, Comparator.nullsLast(String::compareTo));
                break;
            default:
                return;
        }

        filteredList.sort(comparator);
        tableView.refresh();
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
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



