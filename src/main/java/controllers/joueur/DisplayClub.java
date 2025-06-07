

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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import models.joueur.Club;
import models.match.SessionManager;
import models.utilisateur.Role;
import models.utilisateur.User;
import services.joueur.ClubService;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.Optional;

public class DisplayClub {
    @FXML private Button bt_user;
    @FXML private Button teams;
    @FXML private Button dashboard;
    @FXML private Button espace;
    @FXML private Button logistique;
    @FXML private Label nom_user;
    @FXML private Button homeButton;
    @FXML private Button addClubButton;
    @FXML private Button joueurButton;
    @FXML private TextField searchField;
    @FXML private TableView<Club> tableView;
    @FXML private TableColumn<Club, Integer> idColumn;
    @FXML private TableColumn<Club, String> nomColumn;
    @FXML private TableColumn<Club, String> photoColumn;
    @FXML private TableColumn<Club, Void> modifierColumn;
    @FXML private TableColumn<Club, Void> deleteColumn;
    @FXML private Button exportButton; // Added for export
    @FXML private Button sortButton;   // Added for sort

    private ObservableList<Club> clubList = FXCollections.observableArrayList();
    private ObservableList<Club> allClubs = FXCollections.observableArrayList(); // For filtering
    private ClubService clubService = new ClubService();

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
    private void HandleJoueur() {
        loadScene("/joueur/MainController.fxml", joueurButton);
    }

    @FXML
    private void handleHome() {
        loadScene("/Home.fxml", homeButton);
    }

    @FXML
    private void handleAddClubButton() {
        loadScene("/joueur/AjoutClub.fxml", addClubButton);
    }

    private void openModifyWindow(Club club, Stage stage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/joueur/ModifierClub.fxml"));
            Parent root = loader.load();
            ModifyClub controller = loader.getController();
            controller.setClubToModify(club);
            stage.setScene(new Scene(root));
            stage.show();
            stage.setOnHidden(event -> loadClubs());
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Échec du chargement", e.getMessage());
        }
    }

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("idClub"));
        nomColumn.setCellValueFactory(new PropertyValueFactory<>("nomClub"));
        photoColumn.setCellFactory(param -> new TableCell<>() {
            private final ImageView imageView = new ImageView();
            {
                imageView.setFitHeight(30);
                imageView.setFitWidth(30);
            }
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    try {
                        imageView.setImage(new javafx.scene.image.Image(item));
                        setGraphic(imageView);
                    } catch (Exception e) {
                        setGraphic(null);
                    }
                }
            }
        });
        photoColumn.setCellValueFactory(new PropertyValueFactory<>("photoUrl"));

        modifierColumn.setCellFactory(param -> new TableCell<>() {
            private final Button btn = new Button("Modifier");
            {
                btn.setId("btn-modify");
                btn.setOnAction(event -> {
                    Club selectedClub = getTableView().getItems().get(getIndex());
                    showConfirmation("Confirmation", "Modifier ce club ?", "Club: " + selectedClub.getNomClub(), () -> {
                        Stage stage = (Stage) btn.getScene().getWindow();
                        openModifyWindow(selectedClub, stage);
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
                    Club selectedClub = getTableView().getItems().get(getIndex());
                    showConfirmation("Confirmation", "Supprimer ce club ?", "Club: " + selectedClub.getNomClub(), () -> {
                        clubService.supprimer(selectedClub);
                        loadClubs();
                    });
                });
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btn);
            }
        });

        searchField.textProperty().addListener((observable, oldValue, newValue) -> filterClubs(newValue));
        loadClubs();
    }

    public void loadClubs() {
        allClubs.clear();
        allClubs.addAll(clubService.recherche());
        clubList.clear();
        clubList.addAll(allClubs);
        tableView.setItems(clubList);
    }

    private void filterClubs(String searchText) {
        if (searchText == null || searchText.trim().isEmpty()) {
            clubList.setAll(allClubs);
        } else {
            String lowerCaseFilter = searchText.toLowerCase().trim();
            clubList.setAll(allClubs.stream()
                    .filter(club ->
                            String.valueOf(club.getIdClub()).contains(lowerCaseFilter) ||
                                    club.getNomClub().toLowerCase().contains(lowerCaseFilter))
                    .collect(java.util.stream.Collectors.toList()));
        }
        tableView.setItems(clubList);
    }

    // Export Functionality
    @FXML
    private void handleExport() {
        ObservableList<Club> clubs = tableView.getItems();
        if (clubs.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Aucun Club", "Il n'y a aucun club à exporter !");
            return;
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Enregistrer la liste des clubs");
        fileChooser.setInitialFileName("Clubs_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".pdf");
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

            document.add(new Paragraph("Liste des Clubs")
                    .setFont(boldFont)
                    .setFontSize(16)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginBottom(10));

            float[] columnWidths = {100, 200, 300}; // Adjusted for ID, Name, Photo
            Table pdfTable = new Table(UnitValue.createPointArray(columnWidths)).useAllAvailableWidth();

            String[] headers = {"ID Club", "Nom Club", "Photo"};
            for (String header : headers) {
                pdfTable.addHeaderCell(new Cell()
                        .add(new Paragraph(header).setFont(boldFont).setFontSize(10).setTextAlignment(TextAlignment.CENTER))
                        .setPadding(3));
            }

            for (Club club : clubs) {
                pdfTable.addCell(new Cell().add(new Paragraph(String.valueOf(club.getIdClub()))
                        .setFont(regularFont).setFontSize(8).setTextAlignment(TextAlignment.CENTER)));
                pdfTable.addCell(new Cell().add(new Paragraph(club.getNomClub() != null ? club.getNomClub() : "")
                        .setFont(regularFont).setFontSize(8).setTextAlignment(TextAlignment.CENTER)));
                pdfTable.addCell(new Cell().add(new Paragraph(club.getPhotoUrl() != null ? club.getPhotoUrl() : "N/A")
                        .setFont(regularFont).setFontSize(8).setTextAlignment(TextAlignment.CENTER)));
            }

            document.add(pdfTable);
            document.add(new Paragraph(" ").setMarginBottom(10));

            document.add(new Paragraph("Gestion des Clubs")
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

            showAlert(Alert.AlertType.INFORMATION, "Succès", "Les clubs ont été exportés dans " + fileName + " !");

        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Échec de l'exportation des clubs : " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Sort Functionality
    @FXML
    private void handleSort() {
        String[] sortableColumns = {"ID Club", "Nom Club"};
        ChoiceDialog<String> dialog = new ChoiceDialog<>("Nom Club", sortableColumns);
        dialog.setTitle("Trier les Clubs");
        dialog.setHeaderText("Sélectionnez une colonne pour trier en ordre croissant");
        dialog.setContentText("Colonne:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(this::sortTableByColumn);
    }

    private void sortTableByColumn(String columnName) {
        Comparator<Club> comparator;
        switch (columnName) {
            case "ID Club":
                comparator = Comparator.comparingInt(Club::getIdClub);
                break;
            case "Nom Club":
                comparator = Comparator.comparing(Club::getNomClub, Comparator.nullsLast(String::compareTo));
                break;
            default:
                return;
        }

        clubList.sort(comparator);
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