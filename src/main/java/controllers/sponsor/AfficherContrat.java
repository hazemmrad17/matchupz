package controllers.sponsor;

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
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import models.sponsor.Contrat;
import models.match.SessionManager;
import models.utilisateur.Role;
import models.utilisateur.User;
import services.sponsor.ContratService;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
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
    private TableColumn<Contrat, Void> colSignature;

    @FXML
    private TableColumn<Contrat, String> colArticles;

    @FXML
    private TableColumn<Contrat, Void> colActions;

    @FXML
    private AnchorPane stats;

    @FXML
    private TextField searchField;

    @FXML
    private Button btnAjouter, page1button, exportContrat, statbutton, buttontri, geminiButton, goTosponsor;

    private final ContratService contratService = new ContratService();
    private final ObservableList<Contrat> contratList = FXCollections.observableArrayList();

    @FXML
    private Button sponsor, goSponsor;

    @FXML
    private Button bt_user;

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
            Image image = new Image(user.getImage());
            String name = user.getPrenom();
            nom_user.setText(name);
        }
    }

    @FXML
    void match(javafx.event.ActionEvent event) {
        loadScene("/sponsoring/MainPage.fxml", (Button) event.getSource());
    }

    @FXML
    void pageuser(javafx.event.ActionEvent event) {
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
    void goTosponsor(javafx.event.ActionEvent event) {
        loadScene("/sponsoring/MainPage.fxml", (Button) event.getSource());
    }

    @FXML
    void goSponsor(javafx.event.ActionEvent event) {
        loadScene("/sponsoring/AfficherSponsor.fxml", (Button) event.getSource());
    }

    @FXML
    void teams(javafx.event.ActionEvent event) {
        loadScene("/sponsoring/MainPage.fxml", (Button) event.getSource());
    }

    @FXML
    void dashboard(javafx.event.ActionEvent event) {
        loadScene("/sponsoring/MainPage.fxml", (Button) event.getSource());
    }

    @FXML
    void espace(javafx.event.ActionEvent event) {
        User user = SessionManager.getCurrentUser();
        if (user != null && user.getRole() == Role.ADMIN) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/AffichageEspace.fxml"));
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fournisseur/DisplayFournisseur.fxml"));
            Stage stage = (Stage) logistique.getScene().getWindow();
            stage.setScene(new Scene(loader.load()));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de charger la page d'inscription.");
        }
    }

    @FXML
    public void initialize() {
        User user = SessionManager.getCurrentUser();
        if (nom_user == null) {
            System.out.println("Erreur : nom_user est null !");
        } else if (user != null) {
            afficherProfil(user);
        } else {
            System.out.println("Aucun utilisateur connecté !");
        }

        // Set cell value factories
        colTitre.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTitre()));
        colSponsor.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getId_sponsor())));
        colDateDebut.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDateDebut()));
        colDateFin.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDateFin()));
        colMontant.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getMontant())));
        colArticles.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getArticle()));

        // Set signature column cell factory
        colSignature.setCellFactory(new Callback<>() {
            @Override
            public TableCell<Contrat, Void> call(TableColumn<Contrat, Void> param) {
                return new TableCell<>() {
                    private final ImageView imageView = new ImageView();

                    {
                        imageView.setFitHeight(50);
                        imageView.setFitWidth(100);
                        imageView.setPreserveRatio(true);
                    }

                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || getTableRow() == null || getTableRow().getItem() == null) {
                            setGraphic(null);
                        } else {
                            Contrat contrat = getTableRow().getItem();
                            String signaturePath = contrat.getSignaturePath();
                            if (signaturePath != null && !signaturePath.isEmpty()) {
                                try {
                                    File file = new File(signaturePath);
                                    if (file.exists()) {
                                        Image image = new Image(file.toURI().toString());
                                        imageView.setImage(image);
                                        setGraphic(imageView);
                                    } else {
                                        setGraphic(new Label("Image not found"));
                                    }
                                } catch (Exception e) {
                                    setGraphic(new Label("Error loading image"));
                                    e.printStackTrace();
                                }
                            } else {
                                setGraphic(new Label("No signature"));
                            }
                        }
                    }
                };
            }
        });

        // Add actions column
        addActionsColumn();

        // Set row factory to highlight rows nearing end date
        tableViewContrats.setRowFactory(tv -> new TableRow<Contrat>() {
            @Override
            protected void updateItem(Contrat contrat, boolean empty) {
                super.updateItem(contrat, empty);
                if (empty || contrat == null) {
                    getStyleClass().remove("highlight-row");
                } else {
                    try {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        Date endDate = sdf.parse(contrat.getDateFin());
                        Date today = new Date();
                        long diffInMillies = endDate.getTime() - today.getTime();
                        long diffInDays = diffInMillies / (1000 * 60 * 60 * 24);
                        if (diffInDays <= 3 && diffInDays >= 0) {
                            if (!getStyleClass().contains("highlight-row")) {
                                getStyleClass().add("highlight-row");
                            }
                        } else {
                            getStyleClass().remove("highlight-row");
                        }
                    } catch (Exception e) {
                        getStyleClass().remove("highlight-row");
                        e.printStackTrace();
                    }
                }
            }
        });

        // Load contracts
        loadContrats();

        // Set search listener
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
                    final Button btnPay = new Button("Pay");
                    final HBox pane = new HBox(10, btnModifier, btnSupprimer, btnPay);

                    {
                        btnModifier.setOnAction(event -> handleEdit(getTableRow().getItem()));
                        btnSupprimer.setOnAction(event -> handleDelete(getTableRow().getItem()));
                        btnPay.setOnAction(event -> handlePay(getTableRow().getItem()));
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

    private void handlePay(Contrat contrat) {
        if (contrat == null) return;

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/sponsoring/StripePayment.fxml"));
            Parent root = loader.load();

            StripePaymentController controller = loader.getController();
            controller.setContractDetails(contrat.getTitre(), contrat.getMontant());

            Stage stage = (Stage) tableViewContrats.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Stripe Payment for " + contrat.getTitre());
            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de charger la page de paiement Stripe: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void loadContrats() {
        contratList.clear();
        List<Contrat> contrats = contratService.recherche();
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
                String dateDebutStr = contrat.getDateDebut().toLowerCase();
                String dateFinStr = contrat.getDateFin().toLowerCase();
                String articleStr = contrat.getArticle() != null ? contrat.getArticle().toLowerCase() : "";
                if (contrat.getTitre().toLowerCase().contains(lowerCaseFilter) ||
                        dateDebutStr.contains(lowerCaseFilter) ||
                        dateFinStr.contains(lowerCaseFilter) ||
                        articleStr.contains(lowerCaseFilter)) {
                    filteredList.add(contrat);
                }
            }
            tableViewContrats.setItems(filteredList);
        }
    }

    @FXML
    private void addContrat() throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/sponsoring/AjouterContrat.fxml"));
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/sponsoring/ModifierContrat.fxml"));
            Parent root = loader.load();

            ModifierContrat controller = loader.getController();
            controller.setContratToModify(contrat);

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

            stage.setOnHidden(event -> {
                try {
                    FXMLLoader displayLoader = new FXMLLoader(getClass().getResource("/sponsoring/AfficherContrat.fxml"));
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
        loadScene("/sponsoring/AfficherSponsor.fxml", page1button);
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

            PdfPTable pdfTable = new PdfPTable(7);
            pdfTable.setWidthPercentage(100);
            BaseColor deepGreen = new BaseColor(0, 100, 0);
            String[] headers = {"Titre", "Sponsor", "Date Début", "Date Fin", "Montant (€)", "Article", "Signature"};
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
                pdfTable.addCell(contrat.getDateDebut());
                pdfTable.addCell(contrat.getDateFin());
                pdfTable.addCell(String.valueOf(contrat.getMontant()));
                pdfTable.addCell(contrat.getArticle() != null ? contrat.getArticle() : "");
                String signaturePath = contrat.getSignaturePath();
                if (signaturePath != null && !signaturePath.isEmpty()) {
                    File file = new File(signaturePath);
                    if (file.exists()) {
                        com.itextpdf.text.Image img = com.itextpdf.text.Image.getInstance(signaturePath);
                        img.scaleToFit(50, 25);
                        PdfPCell cell = new PdfPCell(img, true);
                        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                        pdfTable.addCell(cell);
                    } else {
                        pdfTable.addCell("Image not found");
                    }
                } else {
                    pdfTable.addCell("No signature");
                }
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
            InputStream inputStream = getClass().getResourceAsStream("/images/logo_horizantalDARK.jpeg");
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
            com.itextpdf.text.Image logo = com.itextpdf.text.Image.getInstance(imageBytes);
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
            showAlert(Alert.AlertType.WARNING, "No Data", "Il n'y a aucun contrat à analyser !");
            return;
        }

        Map<Float, Double> montantDistribution = contratList.stream()
                .collect(Collectors.groupingBy(
                        Contrat::getMontant,
                        Collectors.summingDouble(Contrat::getMontant)
                ));

        ObservableList<BarChart.Series<String, Number>> barChartData = FXCollections.observableArrayList();
        ObservableList<BarChart.Data<String, Number>> dataList = FXCollections.observableArrayList();

        contratList.forEach(contrat -> {
            String titreLabel = contrat.getTitre();
            dataList.add(new BarChart.Data<>(titreLabel, contrat.getMontant()));
        });

        BarChart.Series<String, Number> series = new BarChart.Series<>("Contract Amounts", dataList);
        barChartData.add(series);

        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis, barChartData);
        barChart.setTitle("Distribution des montant des contrats");
        barChart.setLegendVisible(false);
        barChart.setBarGap(5);
        barChart.setCategoryGap(10);
        barChart.setPrefSize(600, 400);

        Stage popupStage = new Stage();
        popupStage.initModality(Modality.NONE);
        popupStage.initOwner(statbutton.getScene().getWindow());
        popupStage.setTitle("Statistiques montant des contrats");

        Scene popupScene = new Scene(barChart);
        popupStage.setScene(popupScene);
        popupStage.setResizable(false);

        popupStage.show();
    }

    @FXML
    private void showSortDialog() {
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Trier les Contrats");
        dialog.setHeaderText("Choisissez le critère de tri :");

        ButtonType sortButtonType = new ButtonType("Trier", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(sortButtonType, ButtonType.CANCEL);

        ComboBox<String> sortComboBox = new ComboBox<>();
        sortComboBox.getItems().addAll("Titre", "Date Début", "Date Fin", "Montant");
        sortComboBox.setValue("Montant");

        dialog.getDialogPane().setContent(sortComboBox);

        Button sortButton = (Button) dialog.getDialogPane().lookupButton(sortButtonType);
        sortButton.setDisable(false);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == sortButtonType) {
                return sortComboBox.getValue();
            }
            return null;
        });

        dialog.showAndWait().ifPresent(sortCriteria -> {
            sortTableByCriteria(sortCriteria);
        });
    }

    private void sortTableByCriteria(String criteria) {
        switch (criteria) {
            case "Titre":
                contratList.sort(Comparator.comparing(Contrat::getTitre, String.CASE_INSENSITIVE_ORDER).reversed());
                break;
            case "Date Début":
                contratList.sort(Comparator.comparing(Contrat::getDateDebut).reversed());
                break;
            case "Date Fin":
                contratList.sort(Comparator.comparing(Contrat::getDateFin).reversed());
                break;
            case "Montant":
                contratList.sort(Comparator.comparingDouble(Contrat::getMontant).reversed());
                break;
            default:
                return;
        }
        tableViewContrats.setItems(contratList);
        filterContrats(searchField.getText());
        tableViewContrats.refresh();
    }

    @FXML
    private void askGemini() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/sponsoring/AfficherGemini.fxml"));
            Parent root = loader.load();

            Stage popupStage = new Stage();
            popupStage.initModality(Modality.NONE);
            popupStage.initOwner(geminiButton.getScene().getWindow());
            popupStage.setTitle("Gemini Chat");
            popupStage.setScene(new Scene(root));
            popupStage.setResizable(false);

            popupStage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de charger l'interface Gemini : " + e.getMessage());
            e.printStackTrace();
        }
    }
}