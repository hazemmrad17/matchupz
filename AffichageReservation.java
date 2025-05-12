package controllers.EspaceSportif;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Parent;
import javafx.scene.layout.VBox;
import models.EspaceSportif.Reservation;
import models.match.SessionManager;
import models.utilisateur.Role;
import models.utilisateur.User;
import services.EspaceSportif.ReservationService;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.awt.Desktop;
import java.io.*;
import java.net.URI;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class AffichageReservation {

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
    private TableView<Reservation> tableView;

    @FXML
    private TableColumn<Reservation, Integer> colId;

    @FXML
    private TableColumn<Reservation, String> colLieu;

    @FXML
    private TableColumn<Reservation, Timestamp> colDate;

    @FXML
    private TableColumn<Reservation, String> colMotif;

    @FXML
    private TableColumn<Reservation, String> colStatus;

    @FXML
    private TableColumn<Reservation, String> colActions;

    @FXML
    private TextField searchField;

    @FXML
    private Button btnTriStatus; // Bouton existant pour trier par statut
    @FXML
    private Button btnStatsMotifs; // Nouveau bouton pour afficher les statistiques des motifs

    private ReservationService reservationService;

    public AffichageReservation() {
        this.reservationService = new ReservationService();
    }

    @FXML
    public void initialize() {
        colId.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getIdReservation()).asObject());
        colLieu.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEspaceSportif().getNomEspace()));
        colDate.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getDateReservee()));
        colMotif.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getMotif()));
        colStatus.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStatus()));

        addActionsColumn();
        loadReservations();

        searchField.textProperty().addListener((observable, oldValue, newValue) -> searchReservation(newValue));

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

    private void loadReservations() {
        List<Reservation> reservations = reservationService.rechercher();
        if (reservations == null) {
            System.err.println("⚠ Aucune réservation trouvée !");
            return;
        }
        tableView.getItems().setAll(reservations);
    }

    private void addActionsColumn() {
        colActions.setCellValueFactory(cellData -> new SimpleStringProperty("Actions"));

        colActions.setCellFactory(new Callback<>() {
            @Override
            public TableCell<Reservation, String> call(TableColumn<Reservation, String> param) {
                return new TableCell<>() {
                    final Button editButton = new Button("Modifier");
                    final Button deleteButton = new Button("Supprimer");
                    final HBox hBox = new HBox(10, editButton, deleteButton);

                    {
                        editButton.setOnAction(event -> handleEdit(getTableRow().getItem()));
                        deleteButton.setOnAction(event -> handleDelete(getTableRow().getItem()));
                    }

                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        setGraphic(empty ? null : hBox);
                    }
                };
            }
        });
    }

    private void handleEdit(Reservation reservation) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/EspaceSportif/ModifierReservation.fxml"));
            Parent modifLayout = loader.load();

            ModifierReservation controller = loader.getController();
            controller.setReservationToEdit(reservation);

            Stage currentStage = (Stage) tableView.getScene().getWindow();
            currentStage.close();

            Stage newStage = new Stage();
            newStage.setScene(new Scene(modifLayout));
            newStage.setTitle("Modifier Réservation");
            newStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Erreur lors du chargement de ModifierReservation.fxml");
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de charger la page de modification de réservation.");
        }
    }

    private void handleDelete(Reservation reservation) {
        if (reservation == null) return;

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation de suppression");
        alert.setHeaderText("Supprimer la réservation");
        alert.setContentText("Êtes-vous sûr de vouloir supprimer cette réservation ?");

        ButtonType buttonYes = new ButtonType("Oui", ButtonBar.ButtonData.YES);
        ButtonType buttonNo = new ButtonType("Non", ButtonBar.ButtonData.NO);
        alert.getButtonTypes().setAll(buttonYes, buttonNo);

        alert.showAndWait().ifPresent(response -> {
            if (response == buttonYes) {
                reservationService.supprimer(reservation);
                loadReservations();
            }
        });
    }

    private void searchReservation(String keyword) {
        List<Reservation> searchResults = reservationService.rechercher().stream()
                .filter(res -> res.getMotif().toLowerCase().contains(keyword.toLowerCase()) ||
                        res.getStatus().toLowerCase().contains(keyword.toLowerCase()))
                .toList();

        ObservableList<Reservation> data = FXCollections.observableArrayList(searchResults);
        tableView.setItems(data);
    }

    @FXML
    private void refreshList() {
        loadReservations();
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
            e.printStackTrace();
            System.err.println("Erreur lors du chargement de AffichageEspace.fxml");
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de charger la page des espaces sportifs.");
        }
    }

    @FXML
    private void addReservation(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/EspaceSportif/AjouterReservation.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Ajouter une Réservation");
            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de charger la page AjouterReservation.");
            e.printStackTrace();
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
    public void goToAbonnement(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/EspaceSportif/AffichageAbonnement.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) tableView.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Liste des Abonnements");
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Erreur lors du chargement des Abonnements.fxml");
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de charger la page des abonnements.");
        }
    }

    @FXML
    private void handleExportReservations() {
        ObservableList<Reservation> reservations = tableView.getItems();
        if (reservations.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Aucune Réservation", "Il n'y a aucune réservation à exporter !");
            return;
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Enregistrer la liste des réservations");
        fileChooser.setInitialFileName("Reservations_" + java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".pdf");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));

        File file = fileChooser.showSaveDialog(tableView.getScene().getWindow());
        if (file == null) {
            showAlert(Alert.AlertType.INFORMATION, "Annulé", "L'exportation a été annulée.");
            return;
        }

        String fileName = file.getAbsolutePath();

        try {
            Document document = new Document();
            document.setMargins(20, 20, 20, 20);
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(fileName));
            document.open();

            Font boldFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16, BaseColor.BLACK);
            Paragraph title = new Paragraph("Liste des Réservations", boldFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            document.add(new Paragraph(" "));

            PdfPTable pdfTable = new PdfPTable(5); // 5 columns: ID, Lieu, Date, Motif, Status
            pdfTable.setWidthPercentage(100);
            BaseColor deepGreen = new BaseColor(0, 100, 0);
            String[] headers = {"ID", "Lieu", "Date", "Motif", "Statut"};
            for (String header : headers) {
                PdfPCell headerCell = new PdfPCell(new Paragraph(header));
                headerCell.setBackgroundColor(deepGreen);
                headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                headerCell.setPadding(5);
                pdfTable.addCell(headerCell);
            }

            for (Reservation reservation : reservations) {
                pdfTable.addCell(String.valueOf(reservation.getIdReservation()));
                pdfTable.addCell(reservation.getEspaceSportif().getNomEspace());
                pdfTable.addCell(reservation.getDateReservee().toString());
                pdfTable.addCell(reservation.getMotif());
                pdfTable.addCell(reservation.getStatus());
            }

            document.add(pdfTable);
            document.add(new Paragraph(" "));

            Paragraph footer1 = new Paragraph("Gestion des Réservations");
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

            showAlert(Alert.AlertType.INFORMATION, "Succès", "Les réservations ont été exportées dans " + fileName + " !");
        } catch (DocumentException | IOException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Échec de l'exportation des réservations : " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void triParStatus(ActionEvent event) {
        // Créer une liste déroulante pour sélectionner le statut
        ChoiceDialog<String> dialog = new ChoiceDialog<>("confirmée", "annulée", "en attente");
        dialog.setTitle("Trier par Statut");
        dialog.setHeaderText("Choisissez un statut");
        dialog.setContentText("Statut :");

        // Afficher la dialog et attendre la sélection de l'utilisateur
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(status -> {
            // Récupérer toutes les réservations depuis le service
            List<Reservation> allReservations = reservationService.rechercher();
            if (allReservations != null && !allReservations.isEmpty()) {
                // Filtrer les réservations localement par statut (insensible à la casse)
                List<Reservation> filteredReservations = allReservations.stream()
                        .filter(reservation -> reservation.getStatus() != null &&
                                reservation.getStatus().equalsIgnoreCase(status))
                        .collect(Collectors.toList());

                if (!filteredReservations.isEmpty()) {
                    tableView.setItems(FXCollections.observableArrayList(filteredReservations));
                } else {
                    showAlert(Alert.AlertType.WARNING, "Aucun résultat", "Aucune réservation trouvée pour le statut : " + status);
                    loadReservations(); // Recharger toutes les réservations si aucun résultat
                }
            } else {
                showAlert(Alert.AlertType.WARNING, "Aucun résultat", "Aucune réservation trouvée.");
                loadReservations(); // Recharger toutes les réservations si la liste est vide
            }
        });
    }

    @FXML
    private void showMotifStats(ActionEvent event) {
        // Récupérer toutes les réservations depuis le service
        List<Reservation> allReservations = reservationService.rechercher();
        if (allReservations == null || allReservations.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Aucune donnée", "Aucune réservation trouvée pour générer des statistiques.");
            return;
        }

        // Compter le nombre de réservations par motif
        Map<String, Long> motifCounts = allReservations.stream()
                .collect(Collectors.groupingBy(
                        reservation -> reservation.getMotif() != null ? reservation.getMotif() : "Inconnu",
                        Collectors.counting()
                ));

        // Créer les données pour le pie chart
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        motifCounts.forEach((motif, count) -> {
            pieChartData.add(new PieChart.Data(motif, count));
        });

        // Créer le pie chart
        PieChart pieChart = new PieChart(pieChartData);
        pieChart.setTitle("Statistiques des Motifs des Réservations");
        pieChart.setLabelsVisible(true);
        pieChart.setLegendVisible(true);

        // Créer une dialog pour afficher le pie chart (large, comme demandé)
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Statistiques des Motifs");
        dialog.setHeaderText("Distribution des Réservations par Motif");

        // Définir une taille plus grande pour la dialog (par exemple, 600x500 pixels)
        dialog.getDialogPane().setPrefSize(600, 500);

        // Ajouter le pie chart dans un VBox pour un meilleur affichage
        VBox content = new VBox(10, pieChart);
        content.setStyle("-fx-padding: 20px;"); // Ajouter un padding pour une meilleure présentation
        dialog.getDialogPane().setContent(content);

        // Ajouter un bouton "Fermer"
        ButtonType closeButton = new ButtonType("Fermer", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().add(closeButton);

        // Afficher la dialog
        dialog.showAndWait();
    }
}