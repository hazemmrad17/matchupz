package controllers.EspaceSportif;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import models.EspaceSportif.Abonnement;
import models.match.SessionManager;
import models.utilisateur.Role;
import models.utilisateur.User;
import services.EspaceSportif.AbonnementService;
import utils.MyDataSource;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.awt.Desktop;
import java.io.*;
import java.net.URI;
import java.sql.Connection;
import java.sql.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class AffichageAbonnement {

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

    @FXML
    private TableView<Abonnement> tableView;

    @FXML
    private TableColumn<Abonnement, Integer> colId;

    @FXML
    private TableColumn<Abonnement, String> colClub;

    @FXML
    private TableColumn<Abonnement, String> colType;

    @FXML
    private TableColumn<Abonnement, java.sql.Date> colDateDebut;

    @FXML
    private TableColumn<Abonnement, java.sql.Date> colDateFin;

    @FXML
    private TableColumn<Abonnement, Double> colTarif;

    @FXML
    private TableColumn<Abonnement, String> colEtat;

    @FXML
    private TableColumn<Abonnement, Void> colActions;

    @FXML
    private TextField searchField;

    @FXML
    private Button btnTriEtat; // Bouton existant pour trier par état
    @FXML
    private Button btnStatsTypes; // Nouveau bouton pour afficher les statistiques des types

    private AbonnementService abonnementService;
    private ObservableList<Abonnement> abonnementList;
    Connection connection = MyDataSource.getInstance().getConn();


    public AffichageAbonnement() {
        this.abonnementService = new AbonnementService(MyDataSource.getInstance().getConn());
        this.abonnementList = FXCollections.observableArrayList();
    }

    @FXML
    public void initialize() {
        // Configuration des colonnes du TableView
        colId.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getIdAbonnement()).asObject());
        colClub.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNomClub()));
        colType.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTypeAbonnement()));
        colDateDebut.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getDateDebut()));
        colDateFin.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getDateFin()));
        colTarif.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getTarif()));
        colEtat.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEtat()));

        // Ajout de la colonne actions
        addActionsColumn();

        // Chargement initial des données
        loadAbonnements();

        // Listener pour la recherche en temps réel
        searchField.textProperty().addListener((observable, oldValue, newValue) -> searchAbonnement(newValue));

        // Afficher le nom de l'utilisateur connecté
        User user = SessionManager.getCurrentUser();
        if (user != null) {
            afficherProfil(user);
        }
    }

    private void afficherProfil(User user) {
        if (user.getImage() != null && !user.getImage().isEmpty()) {
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
    void teams(javafx.event.ActionEvent event) {

        User user = SessionManager.getCurrentUser();
        if (user != null && user.getRole() == Role.ADMIN) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/joueur/MainController.fxml"));
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
    void espace(javafx.event.ActionEvent event) {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/EspaceSportif/AffichageEspace.fxml"));
            Stage stage = (Stage) dashboard.getScene().getWindow();
            stage.setScene(new Scene(loader.load()));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de charger la page d'inscription.");
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


    private void loadAbonnements() {
        try {
            List<Abonnement> abonnements = abonnementService.rechercher();
            if (abonnements == null || abonnements.isEmpty()) {
                System.err.println("⚠ Aucune abonnements trouvée !");
                return;
            }
            abonnementList.setAll(abonnements);
            tableView.setItems(abonnementList);
        } catch (RuntimeException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur de chargement", "Impossible de charger les abonnements : " + e.getMessage());
        }
    }

    private void addActionsColumn() {
        colActions.setCellFactory(param -> new TableCell<>() {
            final Button editButton = new Button("Modifier");
            final Button deleteButton = new Button("Supprimer");
            final HBox hBox = new HBox(10, editButton, deleteButton);

            {
                editButton.setOnAction(event -> handleEdit(getTableRow().getItem()));
                deleteButton.setOnAction(event -> handleDelete(getTableRow().getItem()));
            }

            @Override
            public void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : hBox);
            }
        });
    }

    private void handleEdit(Abonnement abonnement) {
        if (abonnement == null) return;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/EspaceSportif/ModifierAbonnement.fxml"));
            Parent modifLayout = loader.load();

            ModifierAbonnement controller = loader.getController();
            controller.setAbonnementToEdit(abonnement);

            Stage currentStage = (Stage) tableView.getScene().getWindow();
            currentStage.close();

            Stage newStage = new Stage();
            newStage.setScene(new Scene(modifLayout));
            newStage.setTitle("Modifier Abonnement");
            newStage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible d'ouvrir la fenêtre de modification : " + e.getMessage());
        }
    }

    private void handleDelete(Abonnement abonnement) {
        if (abonnement == null) return;

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation de suppression");
        alert.setHeaderText("Supprimer l'abonnement");
        alert.setContentText("Êtes-vous sûr de vouloir supprimer cet abonnement ?");

        ButtonType buttonYes = new ButtonType("Oui", ButtonBar.ButtonData.YES);
        ButtonType buttonNo = new ButtonType("Non", ButtonBar.ButtonData.NO);
        alert.getButtonTypes().setAll(buttonYes, buttonNo);

        alert.showAndWait().ifPresent(response -> {
            if (response == buttonYes) {
                try {
                    abonnementService.supprimer(abonnement);
                    loadAbonnements();
                } catch (RuntimeException e) {
                    showAlert(Alert.AlertType.ERROR, "Erreur de suppression", "Impossible de supprimer l'abonnement : " + e.getMessage());
                }
            }
        });
    }

    private void searchAbonnement(String keyword) {
        List<Abonnement> searchResults = abonnementService.rechercher().stream()
                .filter(abn -> abn.getNomClub().toLowerCase().contains(keyword.toLowerCase()) ||
                        abn.getTypeAbonnement().toLowerCase().contains(keyword.toLowerCase()) ||
                        abn.getEtat().toLowerCase().contains(keyword.toLowerCase()))
                .toList();

        ObservableList<Abonnement> data = FXCollections.observableArrayList(searchResults);
        tableView.setItems(data);
    }

    @FXML
    private void refreshList() {
        loadAbonnements();
    }

    @FXML
    private void addAbonnement(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/EspaceSportif/AjouterAbonnement.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Ajouter un Abonnement");
            stage.show();
            stage.setOnHidden(e -> refreshList());
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible d'ouvrir la fenêtre d'ajout : " + e.getMessage());
        }
    }

    @FXML
    private void goToEspaceSportif(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/EspaceSportif/AffichageEspace.fxml"));
            Parent espaceSportifLayout = loader.load(); // Charger comme Parent au lieu d'AnchorPane
            Scene scene = new Scene(espaceSportifLayout);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de charger AffichageEspace.fxml : " + e.getMessage());
        }
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    private void goToReservation(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/EspaceSportif/AffichageReservation.fxml"));
            Parent reservationLayout = loader.load(); // Charger comme Parent au lieu d'AnchorPane
            Scene scene = new Scene(reservationLayout);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de charger AffichageReservation.fxml : " + e.getMessage());
        }
    }

    @FXML
    private void handleExportAbonnements() {
        ObservableList<Abonnement> abonnements = tableView.getItems();
        if (abonnements.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Aucun Abonnement", "Il n'y a aucun abonnement à exporter !");
            return;
        }

        // Create a FileChooser to let the user select the save location
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Enregistrer la liste des abonnements");
        fileChooser.setInitialFileName("Abonnements_" + java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".pdf");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));

        // Show save dialog and get the selected file
        File file = fileChooser.showSaveDialog(tableView.getScene().getWindow());
        if (file == null) {
            showAlert(Alert.AlertType.INFORMATION, "Annulé", "L'exportation a été annulée.");
            return; // User canceled the dialog
        }

        String fileName = file.getAbsolutePath();

        try {
            Document document = new Document();
            document.setMargins(20, 20, 20, 20);
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(fileName));
            document.open();

            Font boldFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16, BaseColor.BLACK);
            Paragraph title = new Paragraph("Liste des Abonnements", boldFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            document.add(new Paragraph(" "));

            PdfPTable pdfTable = new PdfPTable(7); // 7 columns: ID, Club, Type, DateDebut, DateFin, Tarif, Etat
            pdfTable.setWidthPercentage(100);
            BaseColor deepGreen = new BaseColor(0, 100, 0);
            String[] headers = {"ID", "Club", "Type", "Date Début", "Date Fin", "Tarif", "État"};
            for (String header : headers) {
                PdfPCell headerCell = new PdfPCell(new Paragraph(header));
                headerCell.setBackgroundColor(deepGreen);
                headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                headerCell.setPadding(5);
                pdfTable.addCell(headerCell);
            }

            for (Abonnement abonnement : abonnements) {
                pdfTable.addCell(String.valueOf(abonnement.getIdAbonnement()));
                pdfTable.addCell(abonnement.getNomClub());
                pdfTable.addCell(abonnement.getTypeAbonnement());
                pdfTable.addCell(abonnement.getDateDebut().toString());
                pdfTable.addCell(abonnement.getDateFin().toString());
                pdfTable.addCell(String.valueOf(abonnement.getTarif()));
                pdfTable.addCell(abonnement.getEtat());
            }

            document.add(pdfTable);
            document.add(new Paragraph(" "));

            Paragraph footer1 = new Paragraph("Gestion des Abonnements");
            footer1.setAlignment(Element.ALIGN_RIGHT);
            document.add(footer1);

            Paragraph footer2 = new Paragraph("Nexus Team 2025 ©");
            footer2.setAlignment(Element.ALIGN_RIGHT);
            document.add(footer2);

            document.add(new Paragraph(" "));
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
            Image logo = Image.getInstance(imageBytes);
            logo.scaleToFit(184, 41);
            logo.setAlignment(Element.ALIGN_CENTER);
            document.add(logo);

            document.close();

            showAlert(Alert.AlertType.INFORMATION, "Succès", "Les abonnements ont été exportés dans " + fileName + " !");
        } catch (DocumentException | IOException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Échec de l'exportation des abonnements : " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void triParEtat(ActionEvent event) {
        // Créer une liste déroulante pour sélectionner l'état
        ChoiceDialog<String> dialog = new ChoiceDialog<>("Actif", "Expiré", "Suspendu");
        dialog.setTitle("Trier par État");
        dialog.setHeaderText("Choisissez un état");
        dialog.setContentText("État :");

        // Afficher la dialog et attendre la sélection de l'utilisateur
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(etat -> {
            // Récupérer tous les abonnements depuis le service
            List<Abonnement> allAbonnements = abonnementService.rechercher();
            if (allAbonnements != null && !allAbonnements.isEmpty()) {
                // Filtrer les abonnements localement par état (insensible à la casse)
                List<Abonnement> filteredAbonnements = allAbonnements.stream()
                        .filter(abonnement -> abonnement.getEtat() != null &&
                                abonnement.getEtat().equalsIgnoreCase(etat))
                        .collect(Collectors.toList());

                if (!filteredAbonnements.isEmpty()) {
                    tableView.setItems(FXCollections.observableArrayList(filteredAbonnements));
                } else {
                    showAlert(Alert.AlertType.WARNING, "Aucun résultat", "Aucun abonnement trouvé pour l'état : " + etat);
                    loadAbonnements(); // Recharger tous les abonnements si aucun résultat
                }
            } else {
                showAlert(Alert.AlertType.WARNING, "Aucun résultat", "Aucun abonnement trouvé.");
                loadAbonnements(); // Recharger tous les abonnements si la liste est vide
            }
        });
    }

    @FXML
    private void showTypeStats(ActionEvent event) {
        // Récupérer tous les abonnements depuis le service
        List<Abonnement> allAbonnements = abonnementService.rechercher();
        if (allAbonnements == null || allAbonnements.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Aucune donnée", "Aucun abonnement trouvé pour générer des statistiques.");
            return;
        }

        // Compter le nombre d'abonnements par type
        Map<String, Long> typeCounts = allAbonnements.stream()
                .collect(Collectors.groupingBy(
                        abonnement -> abonnement.getTypeAbonnement() != null ? abonnement.getTypeAbonnement() : "Inconnu",
                        Collectors.counting()
                ));

        // Créer les axes pour le bar chart
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Type d'Abonnement");
        yAxis.setLabel("Nombre");

        // Créer le bar chart
        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        barChart.setTitle("Statistiques des Types d'Abonnements");
        barChart.setLegendVisible(false); // Pas de légende nécessaire ici

        // Ajouter les données au bar chart
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        typeCounts.forEach((type, count) -> {
            series.getData().add(new XYChart.Data<>(type, count));
        });
        barChart.getData().add(series);

        // Créer une dialog pour afficher le bar chart (large, comme demandé)
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Statistiques des Types");
        dialog.setHeaderText("Distribution des Abonnements par Type");

        // Définir une taille plus grande pour la dialog (par exemple, 600x500 pixels)
        dialog.getDialogPane().setPrefSize(600, 500);

        // Ajouter le bar chart dans un VBox pour un meilleur affichage
        VBox content = new VBox(10, barChart);
        content.setStyle("-fx-padding: 20px;"); // Ajouter un padding pour une meilleure présentation
        dialog.getDialogPane().setContent(content);

        // Ajouter un bouton "Fermer"
        ButtonType closeButton = new ButtonType("Fermer", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().add(closeButton);

        // Afficher la dialog
        dialog.showAndWait();
    }
}