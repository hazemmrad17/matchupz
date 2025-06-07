
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
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import models.joueur.Sport;
import models.match.SessionManager;
import models.utilisateur.Role;
import models.utilisateur.User;
import services.joueur.SportService;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.Optional;

public class DisplaySport {
    @FXML private Button bt_user;
    @FXML private Button teams;
    @FXML private Button dashboard;
    @FXML private Button espace;
    @FXML private Button logistique;
    @FXML private Label nom_user;
    @FXML private AnchorPane main_form;
    @FXML private Button button_theme_switch;
    @FXML private Button sportButton;
    @FXML private Button joueurButton;
    @FXML private Button homeButton;
    @FXML private Button addSportButton;
    @FXML private TextField searchField;
    @FXML private TableView<Sport> tableView;
    @FXML private TableColumn<Sport, Integer> idSportColumn;
    @FXML private TableColumn<Sport, String> nomSportColumn;
    @FXML private TableColumn<Sport, String> descriptionColumn;
    @FXML private TableColumn<Sport, Void> modifierColumn;
    @FXML private TableColumn<Sport, Void> deleteColumn;
    @FXML private Button exportButton; // Added for export
    @FXML private Button sortButton;   // Added for sort

    private boolean isGreenTheme = false;
    private ObservableList<Sport> sportList = FXCollections.observableArrayList();
    private ObservableList<Sport> allSports = FXCollections.observableArrayList(); // For filtering
    private SportService sportService = new SportService();

    @FXML
    private void switchTheme() {
        main_form.getStylesheets().clear();
        if (isGreenTheme) {
            main_form.getStylesheets().add(getClass().getResource("../style_file_blue.css").toExternalForm());
            isGreenTheme = false;
        } else {
            main_form.getStylesheets().add(getClass().getResource("../style_file_green.css").toExternalForm());
            isGreenTheme = true;
        }
    }

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



    private void loadScene(String fxmlPath, Button button) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            Stage stage = (Stage) button.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Échec du chargement", e.getMessage());
        }
    }

    @FXML
    private void handleHome() {
        loadScene("/joueur/MainController.fxml", homeButton);
    }

    @FXML
    private void HandleJoueur() {
        loadScene("/joueur/MainController.fxml", joueurButton);
    }

    @FXML
    private void handleAddSportButton() {
        loadScene("/joueur/AjoutSport.fxml", addSportButton);
    }

    private void openModifyWindow(Sport sport, Stage stage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/joueur/ModifierSport.fxml"));
            Parent root = loader.load();
            ModifySport controller = loader.getController();
            controller.setSportToModify(sport);
            stage.setScene(new Scene(root));
            stage.show();
            stage.setOnHidden(event -> loadSports());
        } catch (IOException e) {
            showError("Failed to load the Modify Sport page", e);
        }
    }

    @FXML
    public void initialize() {
        idSportColumn.setCellValueFactory(cellData -> cellData.getValue().idSportProperty().asObject());
        nomSportColumn.setCellValueFactory(cellData -> cellData.getValue().nomSportProperty());
        descriptionColumn.setCellValueFactory(cellData -> cellData.getValue().descriptionProperty());

        modifierColumn.setCellFactory(param -> new TableCell<>() {
            private final Button btn = new Button("Modifier");
            {
                btn.setId("btn-modify");
                btn.setOnAction(event -> {
                    Sport selectedSport = getTableView().getItems().get(getIndex());
                    showConfirmation("Confirmation", "Are you sure you want to modify this sport?",
                            "Sport: " + selectedSport.getNomSport(), () -> {
                                Stage stage = (Stage) btn.getScene().getWindow();
                                openModifyWindow(selectedSport, stage);
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
                    Sport selectedSport = getTableView().getItems().get(getIndex());
                    showConfirmation("Confirmation", "Are you sure you want to delete this sport?",
                            "Sport: " + selectedSport.getNomSport(), () -> {
                                sportService.supprimer(selectedSport);
                                loadSports();
                            });
                });
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btn);
            }
        });

        searchField.textProperty().addListener((observable, oldValue, newValue) -> filterSports(newValue));
        loadSports();
    }

    public void loadSports() {
        allSports.clear();
        allSports.addAll(sportService.recherche());
        sportList.clear();
        sportList.addAll(allSports);
        tableView.setItems(sportList);
    }

    private void filterSports(String searchText) {
        if (searchText == null || searchText.trim().isEmpty()) {
            sportList.setAll(allSports);
        } else {
            String lowerCaseFilter = searchText.toLowerCase().trim();
            sportList.setAll(allSports.stream()
                    .filter(sport ->
                            String.valueOf(sport.getIdSport()).contains(lowerCaseFilter) ||
                                    sport.getNomSport().toLowerCase().contains(lowerCaseFilter) ||
                                    sport.getDescription().toLowerCase().contains(lowerCaseFilter))
                    .collect(java.util.stream.Collectors.toList()));
        }
        tableView.setItems(sportList);
    }

    // Export Functionality
    @FXML
    private void handleExport() {
        ObservableList<Sport> sports = tableView.getItems();
        if (sports.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Aucun Sport", "Il n'y a aucun sport à exporter !");
            return;
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Enregistrer la liste des sports");
        fileChooser.setInitialFileName("Sports_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".pdf");
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

            document.add(new Paragraph("Liste des Sports")
                    .setFont(boldFont)
                    .setFontSize(16)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginBottom(10));

            float[] columnWidths = {100, 300, 300}; // Adjust based on column widths
            Table pdfTable = new Table(UnitValue.createPointArray(columnWidths)).useAllAvailableWidth();

            String[] headers = {"ID Sport", "Nom Sport", "Description"};
            for (String header : headers) {
                pdfTable.addHeaderCell(new Cell()
                        .add(new Paragraph(header).setFont(boldFont).setFontSize(10).setTextAlignment(TextAlignment.CENTER))
                        .setPadding(3));
            }

            for (Sport sport : sports) {
                pdfTable.addCell(new Cell().add(new Paragraph(String.valueOf(sport.getIdSport()))
                        .setFont(regularFont).setFontSize(8).setTextAlignment(TextAlignment.CENTER)));
                pdfTable.addCell(new Cell().add(new Paragraph(sport.getNomSport())
                        .setFont(regularFont).setFontSize(8).setTextAlignment(TextAlignment.CENTER)));
                pdfTable.addCell(new Cell().add(new Paragraph(sport.getDescription())
                        .setFont(regularFont).setFontSize(8).setTextAlignment(TextAlignment.CENTER)));
            }

            document.add(pdfTable);
            document.add(new Paragraph(" ").setMarginBottom(10));

            document.add(new Paragraph("Gestion des Sports")
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

            showAlert(Alert.AlertType.INFORMATION, "Succès", "Les sports ont été exportés dans " + fileName + " !");

        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Échec de l'exportation des sports : " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Sort Functionality
    @FXML
    private void handleSort() {
        String[] sortableColumns = {"ID Sport", "Nom Sport", "Description"};
        ChoiceDialog<String> dialog = new ChoiceDialog<>("Nom Sport", sortableColumns);
        dialog.setTitle("Trier les Sports");
        dialog.setHeaderText("Sélectionnez une colonne pour trier en ordre croissant");
        dialog.setContentText("Colonne:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(this::sortTableByColumn);
    }

    private void sortTableByColumn(String columnName) {
        Comparator<Sport> comparator;
        switch (columnName) {
            case "ID Sport":
                comparator = Comparator.comparingInt(Sport::getIdSport);
                break;
            case "Nom Sport":
                comparator = Comparator.comparing(Sport::getNomSport, Comparator.nullsLast(String::compareTo));
                break;
            case "Description":
                comparator = Comparator.comparing(Sport::getDescription, Comparator.nullsLast(String::compareTo));
                break;
            default:
                return;
        }

        sportList.sort(comparator);
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

    private void showError(String header, IOException e) {
        e.printStackTrace();
        showAlert(Alert.AlertType.ERROR, header, "Details: " + e.getMessage());
    }
}