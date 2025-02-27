package controllers;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import models.Contrat;
import models.Sponsor;
import services.ContratService;
import services.SponsorService;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Element;
import javafx.scene.chart.PieChart;

public class AfficherSponsor {

    @FXML
    private TextField searchField;

    @FXML
    private Button annulerButton;

    @FXML
    private Button addSponsorButton, exportSponsor, page2button;

    @FXML
    private AnchorPane stats;

    @FXML
    private Button statbutton;

    @FXML
    private TableView<Sponsor> tableViewSponsors;

    @FXML
    private TableColumn<Sponsor, String> colNom;

    @FXML
    private TableColumn<Sponsor, String> colContact;

    @FXML
    private TableColumn<Sponsor, String> colPack;

    @FXML
    private TableColumn<Sponsor, Void> modifierColumn;

    private final SponsorService sponsorService = new SponsorService();
    private final ObservableList<Sponsor> sponsorList = FXCollections.observableArrayList();

    private final ContratService contratService = new ContratService();
    private ObservableList<Sponsor> filteredSponsorList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colNom.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNom()));
        colContact.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getContact()));
        colPack.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPack()));

        addActionsColumn();
        loadSponsors();

        if (searchField != null) {
            searchField.textProperty().addListener((observable, oldValue, newValue) -> filterSponsors(newValue));
        }

        if (exportSponsor != null) {
            exportSponsor.setDisable(tableViewSponsors.getItems().isEmpty());
            tableViewSponsors.getItems().addListener((javafx.collections.ListChangeListener<Sponsor>) change -> {
                exportSponsor.setDisable(tableViewSponsors.getItems().isEmpty());
            });
        }
    }

    private void addActionsColumn() {
        modifierColumn.setCellFactory(new Callback<TableColumn<Sponsor, Void>, TableCell<Sponsor, Void>>() {
            @Override
            public TableCell<Sponsor, Void> call(TableColumn<Sponsor, Void> param) {
                return new TableCell<Sponsor, Void>() {
                    final Button btnModifier = new Button("Modifier");
                    final Button btnSupprimer = new Button("Supprimer");
                    final HBox pane = new HBox(10, btnModifier, btnSupprimer);

                    {
                        btnModifier.setOnAction(event -> handleEdit(getTableRow().getItem()));
                        btnSupprimer.setOnAction(event -> handleDelete(getTableRow().getItem()));
                    }

                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        setGraphic(empty ? null : pane);
                    }
                };
            }
        });
    }

    @FXML
    private void addSponsor() throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterSponsor.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) addSponsorButton.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Ajouter un sponsor");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the error using showAlert
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load the FXML file: " + e.getMessage());
        }
    }

    private void loadSponsors() {
        sponsorList.clear(); // Prevent duplication
        List<Sponsor> sponsors = sponsorService.rechercher();
        sponsorList.addAll(sponsors);
        tableViewSponsors.setItems(sponsorList);
        addActionsColumn();
        filterSponsors(searchField.getText());
    }

    private void filterSponsors(String searchText) {
        if (searchText == null || searchText.isEmpty()) {
            tableViewSponsors.setItems(sponsorList);
        } else {
            ObservableList<Sponsor> filteredList = FXCollections.observableArrayList();
            String lowerCaseFilter = searchText.toLowerCase();
            for (Sponsor sponsor : sponsorList) {
                if (sponsor.getNom().toLowerCase().contains(lowerCaseFilter) ||
                        sponsor.getContact().toLowerCase().contains(lowerCaseFilter)) {
                    filteredList.add(sponsor);
                }
            }
            tableViewSponsors.setItems(filteredList);
        }
    }

    @FXML
    private void handleExportSponsors() {
        ObservableList<Sponsor> sponsors = tableViewSponsors.getItems();
        if (sponsors.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Aucun Sponsor", "Il n'y a aucun sponsor à exporter !");
            return;
        }

        String fileName = "Sponsors_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".pdf";

        try {
            Document document = new Document(); // Use com.itextpdf.text.Document
            document.setMargins(20, 20, 20, 20);
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(fileName));
            document.open();

            Font boldFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16, BaseColor.BLACK);
            Paragraph title = new Paragraph("Liste des Sponsors", boldFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            document.add(new Paragraph(" "));

            PdfPTable pdfTable = new PdfPTable(3);
            pdfTable.setWidthPercentage(100);
            BaseColor deepGreen = new BaseColor(0, 100, 0);
            String[] headers = {"Nom", "Contact", "Pack"};
            for (String header : headers) {
                PdfPCell headerCell = new PdfPCell(new Paragraph(header));
                headerCell.setBackgroundColor(deepGreen);
                headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                headerCell.setPadding(5);
                pdfTable.addCell(headerCell);
            }

            for (Sponsor sponsor : sponsors) {
                pdfTable.addCell(sponsor.getNom());
                pdfTable.addCell(sponsor.getContact());
                pdfTable.addCell(sponsor.getPack());
            }

            document.add(pdfTable);
            document.add(new Paragraph(" "));

            Paragraph footer1 = new Paragraph("Gestion des Sponsors");
            footer1.setAlignment(Element.ALIGN_RIGHT);
            document.add(footer1);

            Paragraph footer2 = new Paragraph("Nexus Team 2025 ©");
            footer2.setAlignment(Element.ALIGN_RIGHT);
            document.add(footer2);

            document.add(new Paragraph(" "));
            InputStream inputStream = getClass().getResourceAsStream("/logo_horizantalDARK.jpeg");
            if (inputStream == null) {
                throw new IOException("Image resource not found: /logo_horizantalDARK.jpeg");
            }
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                baos.write(buffer, 0, bytesRead);
            }
            inputStream.close();
            byte[] imageBytes = baos.toByteArray();
            Image logo = Image.getInstance(imageBytes);
            logo.scaleToFit(184, 41);
            logo.setAlignment(Element.ALIGN_CENTER);
            document.add(logo);

            document.close();

            showAlert(Alert.AlertType.INFORMATION, "Succès", "Les sponsors ont été exportés dans " + fileName + " !");
        } catch (DocumentException | IOException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Échec de l'exportation des sponsors : " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleAnnulerButton() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherSponsor.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) annulerButton.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            // Replaced showErrorAlert with showAlert
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load the FXML file: " + e.getMessage());
        }
    }

    private void handleEdit(Sponsor sponsor) {
        // Confirmation dialog before modification
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Are you sure you want to modify this sponsor?");
        alert.setContentText("Sponsor: " + sponsor.getNom());

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                // Get the current Stage from the button that was clicked
                Stage stage = (Stage) tableViewSponsors.getScene().getWindow(); // Stage from TableView
                openModifyWindow(sponsor, stage); // Pass stage directly to open the modify window
            }
        });
    }

    private void openModifyWindow(Sponsor sponsor, Stage stage) {
        try {
            // Load ModifierJoueur.fxml for editing
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModifierSponsor.fxml"));
            Parent root = loader.load();

            ModifierSponsor controller = loader.getController();
            controller.setSponsorToModify(sponsor);

            // Set the scene for the modification window
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

            stage.setOnHidden(event -> {
                try {
                    // After modification, load DisplayJoueur.fxml
                    FXMLLoader displayLoader = new FXMLLoader(getClass().getResource("AfficherSponsor.fxml"));
                    Parent displayRoot = displayLoader.load();

                    Scene displayScene = new Scene(displayRoot);
                    stage.setScene(displayScene); // Change the scene
                    stage.show();
                } catch (IOException e) {
                    showAlert(Alert.AlertType.ERROR, "Error", "Failed to load ModifierSponsor.fxml: " + e.getMessage());
                }
            });

        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load the FXML file: " + e.getMessage());
        }
    }

    private void handleDelete(Sponsor sponsor) {
        if (sponsor == null) return;

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation de suppression");
        alert.setHeaderText("Supprimer le sponsor");
        alert.setContentText("Êtes-vous sûr de vouloir supprimer \"" + sponsor.getNom() + "\" ?");

        ButtonType buttonYes = new ButtonType("Oui", ButtonBar.ButtonData.YES);
        ButtonType buttonNo = new ButtonType("Non", ButtonBar.ButtonData.NO);
        alert.getButtonTypes().setAll(buttonYes, buttonNo);

        alert.showAndWait().ifPresent(response -> {
            if (response == buttonYes) {
                sponsorService.supprimer(sponsor);
                loadSponsors();
            }
        });
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    private void handleHome() {
        loadScene("/AfficherContrat.fxml", page2button);
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
    private void showSponsorPackStats() {
        if (sponsorList.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "No Data", "There are no sponsors to display statistics for!");
            return;
        }

        // Debug: Print the sponsor list to verify data
        System.out.println("Sponsors in sponsorList:");
        sponsorList.forEach(sponsor -> System.out.println("Name: " + sponsor.getNom() + ", Pack: " + sponsor.getPack()));

        // Group sponsors by pack and count occurrences (case-insensitive for consistency)
        Map<String, Long> packDistribution = sponsorList.stream()
                .collect(Collectors.groupingBy(
                        sponsor -> sponsor.getPack().toLowerCase(),
                        Collectors.counting()
                ));

        // Create pie chart data with one entry per unique pack, using title case for display
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        packDistribution.forEach((pack, count) -> {
            // Capitalize the pack name for display (e.g., "gold" -> "Gold")
            String displayPack = pack.substring(0, 1).toUpperCase() + pack.substring(1);
            pieChartData.add(new PieChart.Data(displayPack + " (" + count + ")", count));
        });

        // Create and configure the pie chart
        PieChart pieChart = new PieChart(pieChartData);
        pieChart.setTitle("Sponsor Pack Distribution");
        pieChart.setLabelsVisible(true);
        pieChart.setLabelLineLength(10);
        pieChart.setLegendVisible(true);

        // Clear the stats AnchorPane and add the pie chart
        stats.getChildren().clear();
        stats.getChildren().add(pieChart);

        // Anchor the pie chart to fill the pane
        AnchorPane.setTopAnchor(pieChart, 0.0);
        AnchorPane.setBottomAnchor(pieChart, 0.0);
        AnchorPane.setLeftAnchor(pieChart, 0.0);
        AnchorPane.setRightAnchor(pieChart, 0.0);
    }
}