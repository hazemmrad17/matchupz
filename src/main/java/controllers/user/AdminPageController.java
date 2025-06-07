package controllers.user;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.properties.UnitValue;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.control.Alert;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import models.match.SessionManager;
import models.match.TheImages;
import models.utilisateur.Role;
import models.utilisateur.User;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.videoio.VideoCapture;
import services.user.UserService;
import utils.MyDataSource;

import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AdminPageController {

    @FXML
    private ImageView Home;

    @FXML
    private Button log_out;
    @FXML
    private Label nom_user;
    @FXML private Button statsButton;

    @FXML
    private TableColumn<User, String> imagecol;

    @FXML
    private Button button_ajout;

    @FXML
    private TableColumn<User, Void> expocol;
    @FXML
    private TableView<User> table_user_view;

    @FXML
    private TableColumn<User, Integer> idcol;

    @FXML
    private TableColumn<User, String> nomcol;

    @FXML
    private TableColumn<User, String> prenomcol;

    @FXML
    private TableColumn<User, String> emailcol;

    @FXML
    private ImageView logogmatchupz;

    @FXML
    private TableColumn<User, String> genrecol;

    @FXML
    private TableColumn<User, Role> rolecol;

    @FXML
    private TableColumn<User, String> mdpcol;

    @FXML
    private TableColumn<User, LocalDate> datecol;

    @FXML
    private TableColumn<User, Integer> telcol;
    @FXML
    private ImageView profileImageView;
    @FXML
    private Button bt_user;

    @FXML
    private ImageView icon2;

    @FXML
    private ImageView icon3;

    @FXML
    private ImageView icon4;

    @FXML
    private ImageView icon5;

    @FXML
    private TableColumn<User, Void> modifcol;

    @FXML
    private TableColumn<User, Void> suppcol;
    @FXML
    private Button dashboard;
    @FXML
    private Button teams;
    @FXML
    private TextField searchField;

    @FXML
    private Button espace;

    @FXML
    private Button sponsor;

    @FXML
    private Button logistique;
    @FXML
    private Button match;

    @FXML
    private ImageView icon6;

    @FXML
    private ImageView icon7;
    @FXML
    private Button button_qr; // Existing button for QR code by name
    @FXML
    private Button button_qr_id;

    private boolean scanning = false; // Add scanning boolean field to control QR scanning state
    private final ExecutorService executor = Executors.newSingleThreadExecutor(); // Add ExecutorService for asynchronous webcam operations

    @FXML
    void dashboard(ActionEvent event) {
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
    void log_out(ActionEvent event) {
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
    void pageuser(ActionEvent event) {
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

    @FXML
    void teams(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/joueur/MainController.fxml"));
            Stage stage = (Stage) teams.getScene().getWindow();
            stage.setScene(new Scene(loader.load()));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de charger la page d'inscription.");
        }
    }

    @FXML
    void espace(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/EspaceSportif/AffichageEspace.fxml"));
            Stage stage = (Stage) logistique.getScene().getWindow();
            stage.setScene(new Scene(loader.load()));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de charger la page d'inscription.");
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
    void match(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/MatchSchedule/AffichageMatch.fxml"));
            Stage stage = (Stage) match.getScene().getWindow();
            stage.setScene(new Scene(loader.load()));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de charger la page d'inscription.");
        }
    }

    @FXML
    void sponsor(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/sponsoring/MainPage.fxml"));
            Stage stage = (Stage) logistique.getScene().getWindow();
            stage.setScene(new Scene(loader.load()));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de charger la page d'inscription.");
        }
    }

    @FXML
    void Home(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/user/log-in.fxml"));
            Stage stage = (Stage) Home.getScene().getWindow();
            stage.setScene(new Scene(loader.load()));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de charger la page d'inscription.");
        }
    }

    @FXML
    void showStatistics(ActionEvent event) {
        try {
            int hommeCount = (int) userList.stream().filter(u -> "Homme".equals(u.getGenre())).count();
            int femmeCount = (int) userList.stream().filter(u -> "Femme".equals(u.getGenre())).count();

            System.out.println("Loading statsWindow.fxml... Homme count: " + hommeCount + ", Femme count: " + femmeCount);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/user/statsWindow.fxml"));
            if (loader.getLocation() == null) {
                throw new IOException("FXML file not found: /user/statsWindow.fxml");
            }
            Parent root = loader.load();
            StatsWindowController controller = loader.getController();
            controller.initializeStats(hommeCount, femmeCount);

            Stage statsStage = new Stage();
            statsStage.setTitle("Statistiques par Genre");
            statsStage.setScene(new Scene(root));
            statsStage.initStyle(StageStyle.UTILITY);
            statsStage.setResizable(false);
            statsStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de charger la fenêtre de statistiques: " + e.getMessage());
        }
    }

    private ObservableList<User> userList = FXCollections.observableArrayList();
    private UserService userService = new UserService();
    Connection connection = MyDataSource.getInstance().getConn();
    private UserService user;

    @FXML
    void exportAllUsersToPDF(ActionEvent event) {
        try {
            String fileName = "C:/Users/MSI/Desktop/All_Users_" + System.currentTimeMillis() + ".pdf";
            PdfWriter writer = new PdfWriter(fileName);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            document.add(new Paragraph("Liste de tous les utilisateurs")
                    .setBold()
                    .setFontSize(16));
            document.add(new Paragraph("\n"));
            for (User user : userList) {
                document.add(new Paragraph("Utilisateur #" + user.getId_user())
                        .setBold()
                        .setFontSize(12));
                document.add(new Paragraph("ID: " + user.getId_user()));
                document.add(new Paragraph("Nom: " + user.getNom()));
                document.add(new Paragraph("Prénom: " + user.getPrenom()));
                document.add(new Paragraph("Email: " + user.getEmail()));
                document.add(new Paragraph("Genre: " + user.getGenre()));
                document.add(new Paragraph("Rôle: " + user.getRole()));
                document.add(new Paragraph("Date de naissance: " + user.getDate_de_naissance()));
                document.add(new Paragraph("Téléphone: " + user.getNum_telephone()));

                // Add the user's image instead of the URL
                if (user.getImage() != null && !user.getImage().isEmpty()) {
                    try {
                        String normalizedUrl = user.getImage().startsWith("file:/") ? user.getImage() : "file:/" + user.getImage().replace("\\", "/");
                        ImageData imageData = ImageDataFactory.create(normalizedUrl);
                        com.itextpdf.layout.element.Image pdfImage = new com.itextpdf.layout.element.Image(imageData);
                        pdfImage.setWidth(100); // Adjust size as needed
                        pdfImage.setHeight(100); // Adjust size as needed
                        document.add(pdfImage);
                    } catch (Exception e) {
                        document.add(new Paragraph("Image non disponible: " + e.getMessage()));
                    }
                } else {
                    document.add(new Paragraph("Aucune image disponible"));
                }

                document.add(new Paragraph("\n------------------------\n"));
            }

            document.close();
            System.out.println("PDF de tous les utilisateurs généré avec succès : " + fileName);
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Le PDF a été généré avec succès: " + fileName);
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Échec de l'exportation en PDF: " + e.getMessage());
        }
    }

    public AdminPageController() {
        this.user = new UserService();
    }

    private void searchUtilisateur(String motCle) {
        List<User> resultatRecherche = user.rechercherParMotCle(motCle.toLowerCase());

        ObservableList<User> data = FXCollections.observableArrayList(resultatRecherche);
        table_user_view.setItems(data);
    }

    @FXML
    public void initialize() {
        User user = SessionManager.getCurrentUser();
        if (user != null) {
            afficherProfil(user);
        } else {
            System.out.println("Aucun utilisateur connecté !");
        }

        idcol.setCellValueFactory(new PropertyValueFactory<>("id_user"));
        idcol.setPrefWidth(50); // Width for ID (typically short numbers)

        nomcol.setCellValueFactory(new PropertyValueFactory<>("nom"));
        nomcol.setPrefWidth(80); // Width for names (e.g., 10-20 characters)

        prenomcol.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        prenomcol.setPrefWidth(80); // Width for first names (e.g., 10-20 characters)

        emailcol.setCellValueFactory(new PropertyValueFactory<>("email"));
        emailcol.setPrefWidth(180); // Width for emails (e.g., 30-50 characters)

        genrecol.setCellValueFactory(new PropertyValueFactory<>("genre"));
        genrecol.setPrefWidth(80); // Width for "Homme" or "Femme" (short text)

        rolecol.setCellValueFactory(new PropertyValueFactory<>("role"));
        rolecol.setPrefWidth(180); // Width for roles (e.g., "ADMIN", "RESPONSABLE_LOGISTIQUE", etc.)

        datecol.setCellValueFactory(new PropertyValueFactory<>("date_de_naissance"));
        datecol.setPrefWidth(120); // Width for date (e.g., "YYYY-MM-DD")

        telcol.setCellValueFactory(new PropertyValueFactory<>("num_telephone"));
        telcol.setPrefWidth(80); // Width for phone numbers (e.g., 10 digits)

        // Do NOT bind mdpcol to password to hide it
        // mdpcol.setCellValueFactory(new PropertyValueFactory<>("password")); // Commented out

        imagecol.setCellValueFactory(new PropertyValueFactory<>("image"));
        imagecol.setCellFactory(param -> new TableCell<User, String>() {
            protected void updateItem(String imageUrl, boolean empty) {
                super.updateItem(imageUrl, empty);
                if (empty || imageUrl == null) {
                    setGraphic(null);
                } else {
                    TheImages theImage = new TheImages(imageUrl);
                    setGraphic(theImage.getImages());
                }
            }
        });
        imagecol.setPrefWidth(80);

        // Modify "Modifier" button to include an icon
        modifcol.setCellFactory(param -> new TableCell<>() {
            private final Button editButton = new Button();
            {
                // Load the modify icon from the specified path
                Image modifyIcon = new Image("file:/C:/Users/MSI/Downloads/bouton-modifier.png");
                ImageView modifyIconView = new ImageView(modifyIcon);
                modifyIconView.setFitWidth(24); // Adjust size as needed
                modifyIconView.setFitHeight(24); // Adjust size as needed
                editButton.setGraphic(modifyIconView); // Set the icon as the button's graphic

                editButton.setStyle("-fx-background-color: transparent; -fx-text-fill: white;"); // Remove background color for transparency
                editButton.setId("btn-modify");
                editButton.setOnAction(event -> {
                    try {
                        User selectedUser = getTableView().getItems().get(getIndex());
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/user/modifyuser.fxml"));
                        Parent parent = loader.load();
                        ModifyUserController controller = loader.getController();
                        controller.initData(selectedUser);
                        Stage stage = new Stage();
                        stage.setScene(new Scene(parent));
                        stage.initStyle(StageStyle.UTILITY);
                        stage.show();
                        stage.setOnHidden(event1 -> loadUsers());
                    } catch (IOException ex) {
                        Logger.getLogger(AdminPageController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                });
            }
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : editButton);
            }
        });
        modifcol.setPrefWidth(80);

        // Modify "Supprimer" button to include an icon
        suppcol.setCellFactory(param -> new TableCell<>() {
            private final Button btn = new Button();
            {
                // Load the delete icon from the specified path
                Image deleteIcon = new Image("file:/C:/Users/MSI/Downloads/supp.png");
                ImageView deleteIconView = new ImageView(deleteIcon);
                deleteIconView.setFitWidth(24); // Adjust size as needed
                deleteIconView.setFitHeight(24); // Adjust size as needed
                btn.setGraphic(deleteIconView); // Set the icon as the button's graphic

                btn.setStyle("-fx-background-color: transparent; -fx-text-fill: white;"); // Remove background color for transparency
                btn.setId("btn-delete");
                btn.setOnAction(event -> {
                    User selectedUser = getTableView().getItems().get(getIndex());
                    userService.supprimer(selectedUser);
                    loadUsers();
                });
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btn);
            }
        });
        suppcol.setPrefWidth(80);

        loadUsers();
        searchField.textProperty().addListener((observable, oldValue, newValue) -> searchUtilisateur(newValue));
    }

    @FXML
    void Ajouter(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/user/useradd.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) button_ajout.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de charger la page d'ajout: " + e.getMessage());
        }
    }

    private void exportUserToPDF(User user) throws Exception {
        String fileName = "C:/Users/MSI/Desktop/User_" + user.getId_user() + "_" + user.getNom() + ".pdf";
        System.out.println("Nom du fichier : " + fileName);

        PdfWriter writer = new PdfWriter(fileName);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

        document.add(new Paragraph("Informations de l'utilisateur : ")
                .setBold()
                .setFontSize(14));
        document.add(new Paragraph("\n"));
        document.add(new Paragraph("ID: " + user.getId_user()));
        document.add(new Paragraph("Nom: " + user.getNom()));
        document.add(new Paragraph("Prénom: " + user.getPrenom()));
        document.add(new Paragraph("Email: " + user.getEmail()));
        document.add(new Paragraph("Genre: " + user.getGenre()));
        document.add(new Paragraph("Rôle: " + user.getRole()));
        document.add(new Paragraph("Date de naissance: " + user.getDate_de_naissance()));
        document.add(new Paragraph("Téléphone: " + user.getNum_telephone()));
        document.add(new Paragraph("image: " + user.getImage()));

        // Ajout de la photo au lieu de l'URL
        if (user.getImage() != null && !user.getImage().isEmpty()) {
            try {
                String normalizedUrl = user.getImage().startsWith("file:/") ? user.getImage() : "file:/" + user.getImage().replace("\\", "/");
                ImageData imageData = ImageDataFactory.create(normalizedUrl);
                com.itextpdf.layout.element.Image pdfImage = new com.itextpdf.layout.element.Image(imageData);
                pdfImage.setWidth(UnitValue.createPercentValue(20)); // Adjust size as needed
                pdfImage.setHeight(UnitValue.createPercentValue(20)); // Adjust size as needed
                document.add(pdfImage);
            } catch (Exception e) {
                document.add(new Paragraph("Image non disponible: " + e.getMessage()));
            }
        } else {
            document.add(new Paragraph("Aucune image disponible"));
        }

        document.close();
        System.out.println("PDF généré avec succès : " + fileName);
        showAlert(Alert.AlertType.INFORMATION, "Succès", "Le PDF a été généré avec succès: " + fileName);
    }

    private void loadUsers() {
        userList.clear();
        userList.addAll(userService.recherche());
        table_user_view.setItems(userList);
        System.out.println("Loaded users into TableView: " + userList);
    }

    private void openModifyWindow(User user) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/user/modifyuser.fxml"));
            Parent root = loader.load();
            UpdateUtilisateurController controller = loader.getController();
            controller.setUserToModify(user);
            Stage modifyStage = new Stage();
            modifyStage.setTitle("Modifier Utilisateur");
            modifyStage.setScene(new Scene(root));
            modifyStage.setOnHidden(event -> loadUsers());
            modifyStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private boolean ascendingDate = true;
    @FXML
    private Button sortButton1;

    @FXML
    void sortUsersByDateNaissance(ActionEvent event) {
        FXCollections.sort(userList, (u1, u2) ->
                ascendingDate ? u1.getDate_de_naissance().compareTo(u2.getDate_de_naissance())
                        : u2.getDate_de_naissance().compareTo(u1.getDate_de_naissance())
        );
        ascendingDate = !ascendingDate; // Toggle direction
        table_user_view.refresh();
    }

    @FXML
    private Button sortButton; // Ensure this button is defined in the controller





    @FXML
    void Update(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/user/modifyuser.fxml"));
            Parent parent = loader.load();
            ModifyUserController controller = loader.getController();
            User user = SessionManager.getCurrentUser();
            if (user != null) {
                controller.initData(user);
            }
            Stage stage = new Stage();
            stage.setScene(new Scene(parent));
            stage.initStyle(StageStyle.UTILITY);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de charger la page de modification du profil.");
        }
    }
    @FXML
    void UpdateU(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/user/modifyuser.fxml"));
            Parent parent = loader.load();
            ModifyUserController controller = loader.getController();
            User user = SessionManager.getCurrentUser();
            if (user != null) {
                controller.initData(user);
            }
            Stage stage = new Stage();
            stage.setScene(new Scene(parent));
            stage.initStyle(StageStyle.UTILITY);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de charger la page de modification du profil.");
        }
    }

    private void afficherProfil(User user) {
        if (user.getImage() != null && !user.getImage().isEmpty()) {
           // Image image = new Image(user.getImage());
            String name = user.getPrenom();
            nom_user.setText(name);
           // profileImageView.setImage(image);
        }
    }





    @FXML
    private void generateQRCodeByEmail(ActionEvent event) {
        try {
            // Load the generemail.fxml interface
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/user/generemail.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Générer QR Code (Email)");
            stage.setScene(new Scene(root, 300, 400)); // Adjusted size to fit the QR code and controls
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initStyle(StageStyle.UTILITY);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de charger l'interface de génération de QR Code : " + e.getMessage());
        }
    }



    public void scanQRCode(String expectedData, Stage qrStage) {
        scanning = false; // Reset scanning state
        VideoCapture camera = new VideoCapture(0);
        if (!camera.isOpened()) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Aucune caméra détectée.");
            return;
        }

        Stage scanStage = new Stage();
        scanStage.initModality(Modality.APPLICATION_MODAL);
        scanStage.setTitle("Scanner QR Code");

        ImageView webcamView = new ImageView();
        webcamView.setFitWidth(640);
        webcamView.setFitHeight(480);

        Label statusLabel = new Label("Scannez le QR Code...");
        statusLabel.setStyle("-fx-text-fill: white; -fx-font-size: 14px;");

        Button stopButton = new Button("Arrêter");
        stopButton.setStyle("-fx-background-color: #ff4444; -fx-text-fill: white;");
        stopButton.setOnAction(e -> {
            scanning = false;
            scanStage.close();
            camera.release();
        });

        Pane overlayPane = new Pane(webcamView);
        overlayPane.setPrefSize(640, 480);
        VBox mainVBox = new VBox(10, overlayPane, statusLabel, stopButton);
        mainVBox.setStyle("-fx-background-color: #333333; -fx-alignment: center; -fx-padding: 10;");
        Scene scanScene = new Scene(mainVBox, 640, 550);
        scanStage.setScene(scanScene);
        scanStage.show();

        scanning = true; // Start scanning
        executor.submit(() -> {
            Mat frame = new Mat();
            while (scanning) {
                if (camera.read(frame)) {
                    BufferedImage capturedImage = matToBufferedImage(frame);
                    if (capturedImage == null) {
                        Platform.runLater(() -> {
                            statusLabel.setText("Erreur de conversion de l'image");
                            statusLabel.setStyle("-fx-text-fill: red; -fx-font-size: 14px;");
                        });
                        continue;
                    }
                    Image fxImage = SwingFXUtils.toFXImage(capturedImage, null);
                    Platform.runLater(() -> webcamView.setImage(fxImage));

                    // Scan QR code
                    String scannedData = decodeQRCode(capturedImage);
                    if (scannedData != null) {
                        if (expectedData == null || scannedData.equals(expectedData)) {
                            Platform.runLater(() -> {
                                statusLabel.setText("QR Code reconnu !");
                                statusLabel.setStyle("-fx-text-fill: green; -fx-font-size: 14px;");
                                saveScannedDataToFile(scannedData); // Save scanned data to file
                                showAlert(Alert.AlertType.INFORMATION, "Succès", "Les données scannées ont été enregistrées dans ScannedQR_" + System.currentTimeMillis() + ".txt sur le bureau.");
                                scanning = false;
                                scanStage.close();
                                if (qrStage != null) {
                                    qrStage.close();
                                }
                                camera.release();
                            });
                            break;
                        }
                    } else {
                        Platform.runLater(() -> statusLabel.setText("Scannez le QR Code..."));
                    }

                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            if (!scanning) {
                Platform.runLater(scanStage::close);
                camera.release();
            }
        });
    }

    private String decodeQRCode(BufferedImage image) {
        try {
            com.google.zxing.LuminanceSource source = new com.google.zxing.client.j2se.BufferedImageLuminanceSource(image);
            com.google.zxing.BinaryBitmap bitmap = new com.google.zxing.BinaryBitmap(new com.google.zxing.common.HybridBinarizer(source));
            com.google.zxing.Result result = new com.google.zxing.MultiFormatReader().decode(bitmap);
            return result.getText();
        } catch (Exception e) {
            return null;
        }
    }

    private void showUserInfo(String qrData) {
        if (qrData.startsWith("id:")) {
            String[] parts = qrData.split(",");
            String idPart = parts[0]; // Extract "id:<id>"
            int userId = Integer.parseInt(idPart.split(":")[1]); // Get the ID value
            User user = userService.rechercherParId(userId);
            if (user != null) {
                String userInfo = "Nom: " + user.getNom() + "\n" +
                        "Prénom: " + user.getPrenom() + "\n" +
                        "Email: " + user.getEmail() + "\n" +
                        "Rôle: " + user.getRole() + "\n" +
                        "Genre: " + user.getGenre() + "\n" +
                        "Date de naissance: " + user.getDate_de_naissance() + "\n" +
                        "Téléphone: " + user.getNum_telephone();
                writeUserInfoToFile(userInfo, "UserInfo_" + user.getEmail() + ".txt");
                showAlert(Alert.AlertType.INFORMATION, "Succès", "Les informations de l'utilisateur ont été enregistrées dans UserInfo_" + user.getEmail() + ".txt sur le bureau.");
            } else {
                showAlert(Alert.AlertType.WARNING, "Erreur", "Aucun utilisateur trouvé avec cet ID : " + userId);
            }
        }
    }

    private void writeUserInfoToFile(String userInfo, String fileName) {
        try {
            // Define the file path on the desktop (adjust the path as needed)
            String filePath = "C:/Users/MSI/Desktop/" + fileName;
            // Write the user information to a text file
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
                writer.write(userInfo);
                System.out.println("Informations utilisateur écrites dans : " + filePath);
            }
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible d'écrire les informations dans le fichier : " + e.getMessage());
        }
    }

    private void saveScannedDataToFile(String scannedData) {
        try {
            // Define the file path on the desktop with a timestamp to avoid overwriting
            String fileName = "ScannedQR_" + System.currentTimeMillis() + ".txt";
            String filePath = "C:/Users/MSI/Desktop/" + fileName;
            // Write the scanned QR data to a text file
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
                writer.write("Données scannées : " + scannedData);
                // Show alert instead of printing to console
                showAlert(Alert.AlertType.INFORMATION, "Succès", "Les données scannées ont été enregistrées dans ScannedQR_" + System.currentTimeMillis() + ".txt sur le bureau.");
            }
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible d'enregistrer les données scannées : " + e.getMessage());
        }
    }

    private BufferedImage matToBufferedImage(Mat mat) {
        MatOfByte matOfByte = new MatOfByte();
        Imgcodecs.imencode(".jpg", mat, matOfByte); // Encoder l'image en JPG
        byte[] byteArray = matOfByte.toArray();
        try {
            return javax.imageio.ImageIO.read(new java.io.ByteArrayInputStream(byteArray));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}