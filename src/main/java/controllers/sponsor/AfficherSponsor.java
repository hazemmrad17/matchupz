package controllers.sponsor;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import controllers.sponsor.ModifierSponsor;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import models.sponsor.Contrat;
import models.sponsor.Sponsor;
import models.match.SessionManager;
import models.utilisateur.Role;
import models.utilisateur.User;
import services.sponsor.ContratService;
import services.sponsor.SponsorService;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import javafx.scene.chart.PieChart;

public class AfficherSponsor {

    @FXML
    private Button bt_user;

    @FXML
    private Button dashboard, goTosponsor, buttontri;

    @FXML
    private Button espace;

    @FXML
    private Button logistique;

    @FXML
    private Label nom_user;

    @FXML
    private TextField searchField;

    @FXML
    private Button annulerButton;

    @FXML
    private Button addSponsorButton, exportSponsor, tributton, page2button, geminiButton, goContrat;

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
    private TableColumn<Sponsor, ImageView> colSponsorPicture;

    @FXML
    private TableColumn<Sponsor, Void> modifierColumn;

    private final SponsorService sponsorService = new SponsorService();
    private final ObservableList<Sponsor> sponsorList = FXCollections.observableArrayList();

    private final ContratService contratService = new ContratService();
    private ObservableList<Sponsor> filteredSponsorList = FXCollections.observableArrayList();

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
        loadScene("/sponsoring/MainPage.fxml", (Button) event.getSource());
    }

    @FXML
    void pageuser(ActionEvent event) {
        User user = SessionManager.getCurrentUser();
        if (user != null) {
            String role = user.getRole().getValue();
            if (user.getRole() == Role.ADMIN) {
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
    void goTosponsor(ActionEvent event) {
        loadScene("/sponsoring/MainPage.fxml", (Button) event.getSource());
    }

    @FXML
    void goContrat(ActionEvent event) {
        loadScene("/sponsoring/AfficherContrat.fxml", (Button) event.getSource());
    }

    @FXML
    void teams(ActionEvent event) {
        loadScene("/sponsoring/MainPage.fxml", (Button) event.getSource());
    }

    @FXML
    void dashboard(ActionEvent event) {
        loadScene("/sponsoring/MainPage.fxml", (Button) event.getSource());
    }

    @FXML
    void espace(ActionEvent event) {
        User user = SessionManager.getCurrentUser();
        if (user != null) {
            String role = user.getRole().getValue();
            if (user.getRole() == Role.ADMIN) {
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

        colNom.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNom()));
        colContact.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getContact()));
        colPack.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPack()));
        colSponsorPicture.setCellFactory(column -> new TableCell<Sponsor, ImageView>() {
            private final ImageView imageView = new ImageView();
            {
                imageView.setFitWidth(80);
                imageView.setFitHeight(80);
                imageView.setPreserveRatio(true);
            }
            @Override
            protected void updateItem(ImageView item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || getTableRow() == null || getTableRow().getItem() == null) {
                    setGraphic(null);
                } else {
                    Sponsor sponsor = getTableRow().getItem();
                    String picturePath = sponsor.getSponsor_picture();
                    if (picturePath != null && !picturePath.isEmpty()) {
                        try {
                            // Construct the full path using the uploads/sponsors/ directory
                            File file = new File("uploads/sponsors/" + picturePath);
                            if (file.exists()) {
                                javafx.scene.image.Image image = new javafx.scene.image.Image(file.toURI().toString());
                                imageView.setImage(image);
                                setGraphic(imageView);
                            } else {
                                setGraphic(null);
                            }
                        } catch (Exception e) {
                            setGraphic(null);
                            System.err.println("Error loading image for sponsor " + sponsor.getNom() + ": " + e.getMessage());
                        }
                    } else {
                        setGraphic(null);
                    }
                }
            }
        });

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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/sponsoring/AjouterSponsor.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) addSponsorButton.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Ajouter un sponsor");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load the FXML file: " + e.getMessage());
        }
    }

    private void loadSponsors() {
        sponsorList.clear(); // Prevent duplication
        List<Sponsor> sponsors = sponsorService.recherche();
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
            Document document = new Document();
            document.setMargins(20, 20, 20, 20);
            PdfWriter.getInstance(document, new FileOutputStream(fileName));
            document.open();

            Font boldFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16, BaseColor.BLACK);
            Paragraph title = new Paragraph("Liste des Sponsors", boldFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            document.add(new Paragraph(" "));

            PdfPTable pdfTable = new PdfPTable(4); // 4 columns including sponsorPicture
            pdfTable.setWidthPercentage(100);
            pdfTable.setWidths(new float[]{2, 2, 2, 3}); // Adjust column widths
            BaseColor deepGreen = new BaseColor(0, 100, 0);
            String[] headers = {"Nom", "Contact", "Pack", "Image"};
            for (String header : headers) {
                PdfPCell headerCell = new PdfPCell(new Phrase(header, FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12)));
                headerCell.setBackgroundColor(deepGreen);
                headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                headerCell.setPadding(5);
                pdfTable.addCell(headerCell);
            }

            for (Sponsor sponsor : sponsors) {
                pdfTable.addCell(new Phrase(sponsor.getNom()));
                pdfTable.addCell(new Phrase(sponsor.getContact()));
                pdfTable.addCell(new Phrase(sponsor.getPack()));
                String imagePath = sponsor.getSponsor_picture();
                String imageName = imagePath != null && !imagePath.isEmpty() ? new File(imagePath).getName() : "No Image";
                pdfTable.addCell(new Phrase(imageName));
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
            InputStream inputStream = getClass().getResourceAsStream("/images/logo_horizantalDARK.jpeg");
            if (inputStream == null) {
                throw new IOException("Image resource not found: /images/logo_horizantalDARK.jpeg");
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
            logo.scaleToFit(184f, 41f);
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/sponsoring/AfficherSponsor.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) annulerButton.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load the FXML file: " + e.getMessage());
        }
    }

    private void handleEdit(Sponsor sponsor) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Are you sure you want to modify this sponsor?");
        alert.setContentText("Sponsor: " + sponsor.getNom());

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                Stage stage = (Stage) tableViewSponsors.getScene().getWindow();
                openModifyWindow(sponsor, stage);
            }
        });
    }

    private void openModifyWindow(Sponsor sponsor, Stage stage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/sponsoring/ModifierSponsor.fxml"));
            Parent root = loader.load();

            ModifierSponsor controller = loader.getController();
            controller.setSponsorToModify(sponsor);

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

            stage.setOnHidden(event -> {
                try {
                    FXMLLoader displayLoader = new FXMLLoader(getClass().getResource("/sponsoring/AfficherSponsor.fxml"));
                    Parent displayRoot = displayLoader.load();

                    Scene displayScene = new Scene(displayRoot);
                    stage.setScene(displayScene);
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
                // Delete the image file if it exists
                String picturePath = sponsor.getSponsor_picture();
                if (picturePath != null && !picturePath.isEmpty()) {
                    try {
                        File file = new File("uploads/sponsors/" + picturePath);
                        if (file.exists()) {
                            file.delete();
                        }
                    } catch (Exception e) {
                        System.err.println("Error deleting image file: " + e.getMessage());
                    }
                }
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
        loadScene("/sponsoring/AfficherContrat.fxml", page2button);
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

        System.out.println("Sponsors in sponsorList:");
        sponsorList.forEach(sponsor -> System.out.println("Name: " + sponsor.getNom() + ", Pack: " + sponsor.getPack()));

        Map<String, Long> packDistribution = sponsorList.stream()
                .collect(Collectors.groupingBy(
                        sponsor -> sponsor.getPack().toLowerCase(),
                        Collectors.counting()
                ));

        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        packDistribution.forEach((pack, count) -> {
            String displayPack = pack.substring(0, 1).toUpperCase() + pack.substring(1);
            pieChartData.add(new PieChart.Data(displayPack + " (" + count + ")", count));
        });

        PieChart pieChart = new PieChart(pieChartData);
        pieChart.setTitle("Sponsor Pack Distribution");
        pieChart.setLabelsVisible(true);
        pieChart.setLabelLineLength(10);
        pieChart.setLegendVisible(true);
        pieChart.setPrefSize(600, 400);

        Stage popupStage = new Stage();
        popupStage.initModality(Modality.NONE);
        popupStage.initOwner(statbutton.getScene().getWindow());
        popupStage.setTitle("Sponsor Pack Statistics");

        Scene popupScene = new Scene(pieChart);
        popupStage.setScene(popupScene);
        popupStage.setResizable(false);

        popupStage.show();
    }

    @FXML
    private void showSortDialog() {
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Trier les Sponsors");
        dialog.setHeaderText("Choisissez le critère de tri :");

        ButtonType sortButtonType = new ButtonType("Trier", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(sortButtonType, ButtonType.CANCEL);

        ComboBox<String> sortComboBox = new ComboBox<>();
        sortComboBox.getItems().addAll("Nom", "Contact", "Pack");
        sortComboBox.setValue("Nom");

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
            case "Nom":
                sponsorList.sort(Comparator.comparing(Sponsor::getNom, String.CASE_INSENSITIVE_ORDER).reversed());
                break;
            case "Contact":
                sponsorList.sort(Comparator.comparing(Sponsor::getContact, String.CASE_INSENSITIVE_ORDER).reversed());
                break;
            case "Pack":
                sponsorList.sort(Comparator.comparing(Sponsor::getPack, String.CASE_INSENSITIVE_ORDER).reversed());
                break;
            default:
                return;
        }
        tableViewSponsors.setItems(sponsorList);
        filterSponsors(searchField.getText());
        tableViewSponsors.refresh();
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