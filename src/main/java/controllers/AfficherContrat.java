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
import javafx.scene.chart.BarChart;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import models.Contrat;
import services.ContratService;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import java.util.Map;
import java.util.stream.Collectors;

public class AfficherContrat {

    @FXML
    private TableView<Contrat> tableViewContrats;

    @FXML
    private TableColumn<Contrat, String> colTitre;

    @FXML
    private TableColumn<Contrat, String> colSponsor;

    @FXML
    private TableColumn<Contrat, String> colDateDebut;

    @FXML
    private TableColumn<Contrat, String> colDateFin;

    @FXML
    private TableColumn<Contrat, String> colMontant;

    @FXML
    private TableColumn<Contrat, Void> colActions;

    @FXML
    private AnchorPane stats;

    @FXML
    private TextField searchField;

    @FXML
    private Button btnAjouter, page1button, exportContrat, statbutton;

    private final ContratService contratService = new ContratService();
    private final ObservableList<Contrat> contratList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Set up table columns
        colTitre.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTitre()));
        colSponsor.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getId_sponsor())));
        colDateDebut.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDateDebut().toString()));
        colDateFin.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDateFin().toString()));
        colMontant.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getMontant())));

        addActionsColumn();
        loadContrats();

        // Add listener for search field to filter contracts dynamically
        if (searchField != null) {
            searchField.textProperty().addListener((observable, oldValue, newValue) -> filterContrats(newValue));
        }
    }

    private void addActionsColumn() {
        colActions.setCellFactory(new Callback<>() {
            @Override
            public TableCell<Contrat, Void> call(TableColumn<Contrat, Void> param) {
                return new TableCell<>() {
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

    private void loadContrats() {
        contratList.clear();
        List<Contrat> contrats = contratService.rechercher();
        contratList.addAll(contrats);
        tableViewContrats.setItems(contratList);
        filterContrats(searchField.getText());
    }

    private void filterContrats(String searchText) {
        if (searchText == null || searchText.isEmpty()) {
            tableViewContrats.setItems(contratList);
        } else {
            ObservableList<Contrat> filteredList = FXCollections.observableArrayList();
            String lowerCaseFilter = searchText.toLowerCase();
            for (Contrat contrat : contratList) {
                String dateDebutStr = contrat.getDateDebut().toString().toLowerCase();
                String dateFinStr = contrat.getDateFin().toString().toLowerCase();
                if (contrat.getTitre().toLowerCase().contains(lowerCaseFilter) || (dateDebutStr.contains(lowerCaseFilter) || // Filter by start date
                        dateFinStr.contains(lowerCaseFilter))) { // Filter by sponsor name (assuming getSponsor() exists or use getId_sponsor().toString())
                    filteredList.add(contrat);
                }
            }
            tableViewContrats.setItems(filteredList);
        }
    }

    @FXML
    private void addContrat() throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterContrat.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) btnAjouter.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Ajouter un contrat");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load the FXML file: " + e.getMessage());
        }
    }

    private void handleEdit(Contrat contrat) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Are you sure you want to modify this contract?");
        alert.setContentText("Contrat: " + contrat.getTitre());

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                Stage stage = (Stage) tableViewContrats.getScene().getWindow();
                openModifyWindow(contrat, stage);
            }
        });
    }

    @FXML
    private void openModifyWindow(Contrat contrat, Stage stage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModifierContrat.fxml"));
            Parent root = loader.load();

            ModifierContrat controller = loader.getController();
            controller.setContratToModify(contrat);

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

            stage.setOnHidden(event -> {
                try {
                    FXMLLoader displayLoader = new FXMLLoader(getClass().getResource("AfficherContrat.fxml"));
                    Parent displayRoot = displayLoader.load();

                    Scene displayScene = new Scene(displayRoot);
                    stage.setScene(displayScene);
                    stage.show();
                } catch (IOException e) {
                    showAlert(Alert.AlertType.ERROR, "Error", "Failed to load ModifierContrat.fxml: " + e.getMessage());
                }
            });

        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load the FXML file: " + e.getMessage());
        }
    }

    private void handleDelete(Contrat contrat) {
        if (contrat == null) return;

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation de suppression");
        alert.setHeaderText("Supprimer le contrat");
        alert.setContentText("Êtes-vous sûr de vouloir supprimer \"" + contrat.getTitre() + "\" ?");

        ButtonType buttonYes = new ButtonType("Oui", ButtonBar.ButtonData.YES);
        ButtonType buttonNo = new ButtonType("Non", ButtonBar.ButtonData.NO);
        alert.getButtonTypes().setAll(buttonYes, buttonNo);

        alert.showAndWait().ifPresent(response -> {
            if (response == buttonYes) {
                contratService.supprimer(contrat);
                loadContrats();
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
        loadScene("/AfficherSponsor.fxml", page1button);
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
    private void handleExportContrats() {
        ObservableList<Contrat> contracts = tableViewContrats.getItems();
        if (contracts.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Aucun Contrat", "Il n'y a aucun contrat à exporter !");
            return;
        }

        String fileName = "Contrats_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".pdf";

        try {
            Document document = new Document();
            document.setMargins(20, 20, 20, 20);
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(fileName));
            document.open();

            Font boldFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16, BaseColor.BLACK);
            Paragraph title = new Paragraph("Liste des Contrats", boldFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            document.add(new Paragraph(" "));

            PdfPTable pdfTable = new PdfPTable(5); // 6 columns for contracts
            pdfTable.setWidthPercentage(100);
            BaseColor deepGreen = new BaseColor(0, 100, 0);
            String[] headers = {"Titre", "Sponsor", "Date Début", "Date Fin", "Montant (€)"};
            for (String header : headers) {
                PdfPCell headerCell = new PdfPCell(new Paragraph(header));
                headerCell.setBackgroundColor(deepGreen);
                headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                headerCell.setPadding(5);
                pdfTable.addCell(headerCell);
            }

            for (Contrat contrat : contracts) {
                pdfTable.addCell(contrat.getTitre());
                pdfTable.addCell(String.valueOf(contrat.getId_sponsor()));
                pdfTable.addCell(contrat.getDateDebut().toString());
                pdfTable.addCell(contrat.getDateFin().toString());
                pdfTable.addCell(String.valueOf(contrat.getMontant()));
            }

            document.add(pdfTable);
            document.add(new Paragraph(" "));

            Paragraph footer1 = new Paragraph("Gestion des Contrats");
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

            showAlert(Alert.AlertType.INFORMATION, "Succès", "Les contrats ont été exportés dans " + fileName + " !");
        } catch (DocumentException | IOException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Échec de l'exportation des contrats : " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void showContratMontantStats() {
        if (contratList.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "No Data", "There are no contracts to display statistics for!");
            return;
        }

        // Debug: Print the contract list to verify data
        System.out.println("Contracts in contratList:");
        contratList.forEach(contrat -> System.out.println("Titre: " + contrat.getTitre() + ", Montant: " + contrat.getMontant()));

        // Group contracts by montant and sum their values
        Map<Float, Double> montantDistribution = contratList.stream()
                .collect(Collectors.groupingBy(
                        Contrat::getMontant, // Group by montant
                        Collectors.summingDouble(Contrat::getMontant) // Sum the montant values
                ));

        // Create category and value lists for the bar chart
        ObservableList<BarChart.Series<String, Number>> barChartData = FXCollections.observableArrayList();
        ObservableList<BarChart.Data<String, Number>> dataList = FXCollections.observableArrayList();

        montantDistribution.forEach((montant, total) -> {
            String montantLabel = String.format("€%.2f", montant); // Format montant for display
            dataList.add(new BarChart.Data<>(montantLabel, total));
        });

        BarChart.Series<String, Number> series = new BarChart.Series<>("Contract Amounts", dataList);
        barChartData.add(series);

        // Create and configure the bar chart
        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis, barChartData);
        barChart.setTitle("Contract Amount Distribution");
        barChart.setLegendVisible(false);

        // Style the bar chart
        barChart.setBarGap(5);
        barChart.setCategoryGap(10);
        barChart.setPrefSize(stats.getPrefWidth(), stats.getPrefHeight());

        // Clear the stats AnchorPane and add the bar chart
        stats.getChildren().clear();
        stats.getChildren().add(barChart);

        // Anchor the bar chart to fill the pane
        AnchorPane.setTopAnchor(barChart, 0.0);
        AnchorPane.setBottomAnchor(barChart, 0.0);
        AnchorPane.setLeftAnchor(barChart, 0.0);
        AnchorPane.setRightAnchor(barChart, 0.0);
    }



}