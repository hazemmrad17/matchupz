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
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
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
import models.joueur.PerformanceJoueur;
import models.match.SessionManager;
import models.utilisateur.Role;
import models.utilisateur.User;
import services.joueur.PerformanceJoueurService;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.Optional;

public class DisplayPerformance {
    @FXML private Button bt_user;
    @FXML private Button dashboard;
    @FXML private Button espace;
    @FXML private Button logistique;
    @FXML private Label nom_user;
    @FXML private Button joueurButton;
    @FXML private Button homeButton;
    @FXML private Button addPerformanceButton;
    @FXML private TextField searchField;
    @FXML private TableView<PerformanceJoueur> tableView;
    @FXML private TableColumn<PerformanceJoueur, Integer> idPerformanceColumn;
    @FXML private TableColumn<PerformanceJoueur, Integer> idJoueurColumn;
    @FXML private TableColumn<PerformanceJoueur, String> saisonColumn;
    @FXML private TableColumn<PerformanceJoueur, Integer> nombreMatchesColumn;
    @FXML private TableColumn<PerformanceJoueur, Integer> minutesJoueesColumn;
    @FXML private TableColumn<PerformanceJoueur, Integer> butsMarquesColumn;
    @FXML private TableColumn<PerformanceJoueur, Integer> passesDecisivesColumn;
    @FXML private TableColumn<PerformanceJoueur, Integer> cartonsJaunesColumn;
    @FXML private TableColumn<PerformanceJoueur, Integer> cartonsRougesColumn;
    @FXML private TableColumn<PerformanceJoueur, Void> modifierColumn;
    @FXML private TableColumn<PerformanceJoueur, Void> deleteColumn;
    @FXML private Button exportButton; // Added for export
    @FXML private Button sortButton;   // Added for sort

    private ObservableList<PerformanceJoueur> performanceList = FXCollections.observableArrayList();
    private ObservableList<PerformanceJoueur> allPerformances = FXCollections.observableArrayList(); // For filtering
    private PerformanceJoueurService performanceService = new PerformanceJoueurService();

    private void afficherProfil(User user) {
        if (user.getImage() != null && !user.getImage().isEmpty()) {
            javafx.scene.image.Image image = new javafx.scene.image.Image(user.getImage());
            String name = user.getPrenom();
            nom_user.setText(name);
        }
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



    @FXML
    private void handleHome() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Home.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) homeButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            showError("Failed to load the Home page", e);
        }
    }

    @FXML
    private void HandleJoueur() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/joueur/MainController.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) joueurButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            showError("Failed to load the Main Joueur page", e);
        }
    }

    @FXML
    private void handlePerformanceButton() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/joueur/AjoutPerformance.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) addPerformanceButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            showError("Failed to load the Add Performance page", e);
        }
    }

    private void openModifyWindow(PerformanceJoueur performance, Stage stage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/joueur/ModifierPerformance.fxml"));
            Parent root = loader.load();
            ModifyPerformance controller = loader.getController();
            controller.setPerformanceToModify(performance);
            stage.setScene(new Scene(root));
            stage.show();
            stage.setOnHidden(event -> reloadDisplay(stage));
        } catch (IOException e) {
            showError("Failed to load the Modify Performance page", e);
        }
    }

    private void reloadDisplay(Stage stage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/joueur/DisplayPerformance.fxml"));
            Parent root = loader.load();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            showError("Failed to reload DisplayPerformance.fxml", e);
        }
    }

    @FXML
    public void initialize() {
        if (idPerformanceColumn == null) {
            System.err.println("idPerformanceColumn is null - Check FXML fx:id");
            return;
        }

        idPerformanceColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getIdPerformance()).asObject());
        idJoueurColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getIdJoueur()).asObject());
        saisonColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getSaison()));
        nombreMatchesColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getNombreMatches()).asObject());
        minutesJoueesColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getMinutesJouees()).asObject());
        butsMarquesColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getButsMarques()).asObject());
        passesDecisivesColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getPassesDecisives()).asObject());
        cartonsJaunesColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getCartonsJaunes()).asObject());
        cartonsRougesColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getCartonsRouges()).asObject());

        modifierColumn.setCellFactory(param -> new TableCell<>() {
            private final Button btn = new Button("Modifier");
            {
                btn.setId("btn-modify");
                btn.setOnAction(event -> {
                    PerformanceJoueur selectedPerformance = getTableView().getItems().get(getIndex());
                    showConfirmation("Are you sure you want to modify this performance?",
                            "ID Performance: " + selectedPerformance.getIdPerformance(), () -> {
                                Stage stage = (Stage) btn.getScene().getWindow();
                                openModifyWindow(selectedPerformance, stage);
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
                    PerformanceJoueur selectedPerformance = getTableView().getItems().get(getIndex());
                    showConfirmation("Are you sure you want to delete this performance?",
                            "ID Performance: " + selectedPerformance.getIdPerformance(), () -> {
                                performanceService.supprimer(selectedPerformance);
                                loadPerformances();
                            });
                });
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btn);
            }
        });

        searchField.textProperty().addListener((observable, oldValue, newValue) -> filterPerformances(newValue));
        loadPerformances();
    }

    public void loadPerformances() {
        allPerformances.clear();
        allPerformances.addAll(performanceService.recherche());
        performanceList.clear();
        performanceList.addAll(allPerformances);
        tableView.setItems(performanceList);
    }

    private void filterPerformances(String searchText) {
        if (searchText == null || searchText.trim().isEmpty()) {
            performanceList.setAll(allPerformances);
        } else {
            String lowerCaseFilter = searchText.toLowerCase().trim();
            performanceList.setAll(allPerformances.stream()
                    .filter(performance ->
                            String.valueOf(performance.getIdJoueur()).contains(lowerCaseFilter) ||
                                    performance.getSaison().toLowerCase().contains(lowerCaseFilter))
                    .collect(java.util.stream.Collectors.toList()));
        }
        tableView.setItems(performanceList);
    }

    // Export Functionality
    @FXML
    private void handleExport() {
        ObservableList<PerformanceJoueur> performances = tableView.getItems();
        if (performances.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Aucune Performance", "Il n'y a aucune performance à exporter !");
            return;
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Enregistrer la liste des performances");
        fileChooser.setInitialFileName("Performances_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".pdf");
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

            document.add(new Paragraph("Liste des Performances des Joueurs")
                    .setFont(boldFont)
                    .setFontSize(16)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginBottom(10));

            float[] columnWidths = {80, 80, 80, 80, 80, 80, 80, 80, 80}; // Adjusted for 9 columns
            Table pdfTable = new Table(UnitValue.createPointArray(columnWidths)).useAllAvailableWidth();

            String[] headers = {"ID Performance", "ID Joueur", "Saison", "Matches", "Minutes", "Goals", "Assists", "Yellow Cards", "Red Cards"};
            for (String header : headers) {
                pdfTable.addHeaderCell(new Cell()
                        .add(new Paragraph(header).setFont(boldFont).setFontSize(8).setTextAlignment(TextAlignment.CENTER))
                        .setPadding(3));
            }

            for (PerformanceJoueur performance : performances) {
                pdfTable.addCell(new Cell().add(new Paragraph(String.valueOf(performance.getIdPerformance()))
                        .setFont(regularFont).setFontSize(8).setTextAlignment(TextAlignment.CENTER)));
                pdfTable.addCell(new Cell().add(new Paragraph(String.valueOf(performance.getIdJoueur()))
                        .setFont(regularFont).setFontSize(8).setTextAlignment(TextAlignment.CENTER)));
                pdfTable.addCell(new Cell().add(new Paragraph(performance.getSaison())
                        .setFont(regularFont).setFontSize(8).setTextAlignment(TextAlignment.CENTER)));
                pdfTable.addCell(new Cell().add(new Paragraph(String.valueOf(performance.getNombreMatches()))
                        .setFont(regularFont).setFontSize(8).setTextAlignment(TextAlignment.CENTER)));
                pdfTable.addCell(new Cell().add(new Paragraph(String.valueOf(performance.getMinutesJouees()))
                        .setFont(regularFont).setFontSize(8).setTextAlignment(TextAlignment.CENTER)));
                pdfTable.addCell(new Cell().add(new Paragraph(String.valueOf(performance.getButsMarques()))
                        .setFont(regularFont).setFontSize(8).setTextAlignment(TextAlignment.CENTER)));
                pdfTable.addCell(new Cell().add(new Paragraph(String.valueOf(performance.getPassesDecisives()))
                        .setFont(regularFont).setFontSize(8).setTextAlignment(TextAlignment.CENTER)));
                pdfTable.addCell(new Cell().add(new Paragraph(String.valueOf(performance.getCartonsJaunes()))
                        .setFont(regularFont).setFontSize(8).setTextAlignment(TextAlignment.CENTER)));
                pdfTable.addCell(new Cell().add(new Paragraph(String.valueOf(performance.getCartonsRouges()))
                        .setFont(regularFont).setFontSize(8).setTextAlignment(TextAlignment.CENTER)));
            }

            document.add(pdfTable);
            document.add(new Paragraph(" ").setMarginBottom(10));

            document.add(new Paragraph("Gestion des Performances des Joueurs")
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

            showAlert(Alert.AlertType.INFORMATION, "Succès", "Les performances ont été exportées dans " + fileName + " !");

        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Échec de l'exportation des performances : " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Sort Functionality
    @FXML
    private void handleSort() {
        String[] sortableColumns = {
                "ID Performance", "ID Joueur", "Saison", "Matches", "Minutes",
                "Goals", "Assists", "Yellow Cards", "Red Cards"
        };
        ChoiceDialog<String> dialog = new ChoiceDialog<>("Saison", sortableColumns);
        dialog.setTitle("Trier les Performances");
        dialog.setHeaderText("Sélectionnez une colonne pour trier en ordre croissant");
        dialog.setContentText("Colonne:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(this::sortTableByColumn);
    }

    private void sortTableByColumn(String columnName) {
        Comparator<PerformanceJoueur> comparator;
        switch (columnName) {
            case "ID Performance":
                comparator = Comparator.comparingInt(PerformanceJoueur::getIdPerformance);
                break;
            case "ID Joueur":
                comparator = Comparator.comparingInt(PerformanceJoueur::getIdJoueur);
                break;
            case "Saison":
                comparator = Comparator.comparing(PerformanceJoueur::getSaison, Comparator.nullsLast(String::compareTo));
                break;
            case "Matches":
                comparator = Comparator.comparingInt(PerformanceJoueur::getNombreMatches);
                break;
            case "Minutes":
                comparator = Comparator.comparingInt(PerformanceJoueur::getMinutesJouees);
                break;
            case "Goals":
                comparator = Comparator.comparingInt(PerformanceJoueur::getButsMarques);
                break;
            case "Assists":
                comparator = Comparator.comparingInt(PerformanceJoueur::getPassesDecisives);
                break;
            case "Yellow Cards":
                comparator = Comparator.comparingInt(PerformanceJoueur::getCartonsJaunes);
                break;
            case "Red Cards":
                comparator = Comparator.comparingInt(PerformanceJoueur::getCartonsRouges);
                break;
            default:
                return;
        }

        performanceList.sort(comparator);
        tableView.refresh();
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void showError(String header, Exception e) {
        e.printStackTrace();
        showAlert(Alert.AlertType.ERROR, header, "Details: " + e.getMessage());
    }

    private void showConfirmation(String header, String content, Runnable onConfirm) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                onConfirm.run();
            }
        });
    }
}


