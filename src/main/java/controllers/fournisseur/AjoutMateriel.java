package controllers.fournisseur;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import models.logistics.Fournisseur;
import models.logistics.Materiel;
import models.logistics.TypeMateriel;
import models.logistics.EtatMateriel;
import models.match.SessionManager;
import models.utilisateur.Role;
import models.utilisateur.User;
import services.logistics.FournisseurService;
import services.logistics.MaterielService;

import com.github.sarxos.webcam.Webcam;
import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import javafx.embed.swing.SwingFXUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class AjoutMateriel {

    @FXML private Button bt_user;
    @FXML private Button dashboard;
    @FXML private Button espace;
    @FXML private Button logistique;
    @FXML private Label nom_user;
    @FXML private ComboBox<String> comboBoxCurrency;
    @FXML private Button convertPriceButton;
    private String baseCurrency = "TND";

    @FXML private Button annulerButton;
    @FXML private Button btnAjout;
    @FXML private TextField textFieldNom, textFieldQuantite, textFieldPrixUnitaire, barcodeField;
    @FXML private ComboBox<String> comboBoxType, comboBoxEtat, comboBoxFournisseur;
    @FXML private Button scanButton;
    @FXML private Button uploadImageButton;

    private ObservableList<Materiel> materielList = FXCollections.observableArrayList();
    private MaterielService materielService = new MaterielService();
    private FournisseurService fournisseurService = new FournisseurService();
    private ExecutorService executor = Executors.newSingleThreadExecutor();
    private volatile boolean scanning = false;
    private Materiel materielToAdd;
    private static final String IMAGE_DIR = "src/main/resources/ImageMaterial/";
    private static final String IMAGE_RELATIVE_PATH = "ImageMaterial/";

    private void afficherProfil(User user) {
        if (user.getImage() != null && !user.getImage().isEmpty()) {
            Image image = new Image(user.getImage());
            String name = user.getPrenom();
            nom_user.setText(name);
        }
    }

    @FXML
    void user(ActionEvent event) {
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
    void espace(ActionEvent event) {
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
    void logistique(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fournisseur/AfficherFournisseur.fxml"));
            Stage stage = (Stage) dashboard.getScene().getWindow();
            stage.setScene(new Scene(loader.load()));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de charger la page d'inscription.");
        }
    }

    @FXML
    void sponsor(ActionEvent event) {
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
    void match(ActionEvent event) {
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
    void teams(ActionEvent event) {
        User user = SessionManager.getCurrentUser();
        if (user != null && user.getRole() == Role.ADMIN) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/joueur/MainController.fxml"));
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
    public void initialize() {
        User user = SessionManager.getCurrentUser();
        if (nom_user == null) {
            System.out.println("Erreur : nom_user est null !");
        } else if (user != null) {
            afficherProfil(user);
        } else {
            System.out.println("Aucun utilisateur connecté !");
        }
        System.out.println("Méthode initialize() exécutée pour AjoutMateriel !");

        if (comboBoxType == null || comboBoxEtat == null || comboBoxFournisseur == null ||
                barcodeField == null || scanButton == null || uploadImageButton == null ||
                comboBoxCurrency == null || convertPriceButton == null) {
            System.err.println("Problème d'injection FXML ! Vérifiez les IDs dans le fichier FXML.");
            return;
        }

        comboBoxType.getItems().addAll(
                "EQUIPEMENT_SPORTIF",
                "ACCESSOIRE_ENTRAINEMENT",
                "MATERIEL_JEU",
                "TENUE_SPORTIVE",
                "EQUIPEMENT_PROTECTION",
                "INFRASTRUCTURE"
        );
        comboBoxType.setValue("MATERIEL_JEU");

        comboBoxEtat.getItems().addAll("NEUF", "USE", "ENDOMMAGE");
        comboBoxEtat.setValue("NEUF");

        comboBoxCurrency.getItems().addAll("USD", "EUR", "GBP", "JPY", "TND");
        comboBoxCurrency.setValue("USD");

        updateFournisseurComboBox("MATERIEL_JEU");

        comboBoxType.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null) {
                updateFournisseurComboBox(newValue);
            }
        });

        materielList.addAll(materielService.recherche());
        System.out.println("Matériels chargés : " + materielList.size());
        materielToAdd = new Materiel(0, "", TypeMateriel.MATERIEL_JEU, 0, EtatMateriel.NEUF, 0.0f, "");
    }

    private void updateFournisseurComboBox(String typeMateriel) {
        comboBoxFournisseur.getItems().clear();
        List<Fournisseur> matchingFournisseurs = fournisseurService.getFournisseursByCategory(typeMateriel);
        if (matchingFournisseurs.isEmpty()) {
            comboBoxFournisseur.getItems().add("Aucun fournisseur disponible");
            comboBoxFournisseur.setValue("Aucun fournisseur disponible");
        } else {
            for (Fournisseur f : matchingFournisseurs) {
                comboBoxFournisseur.getItems().add(f.getNom());
            }
            comboBoxFournisseur.setValue(matchingFournisseurs.get(0).getNom());
        }
    }

    @FXML
    private void handleAnnulerButton() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fournisseur/DisplayFournisseur.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) annulerButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Échec du chargement de DisplayFournisseur.fxml : " + e.getMessage());
        }
    }

    @FXML
    void ajouter(ActionEvent event) {
        if (textFieldNom.getText().trim().isEmpty() || textFieldQuantite.getText().trim().isEmpty() ||
                textFieldPrixUnitaire.getText().trim().isEmpty() || barcodeField.getText().trim().isEmpty() ||
                comboBoxType.getValue() == null || comboBoxEtat.getValue() == null || comboBoxFournisseur.getValue() == null ||
                comboBoxCurrency.getValue() == null) {
            showAlert(Alert.AlertType.WARNING, "Champs obligatoires", "Veuillez remplir tous les champs !");
            return;
        }

        if (!textFieldNom.getText().matches("^[a-zA-Z\\s]+$")) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Le nom doit contenir uniquement des lettres !");
            return;
        }

        int quantite;
        try {
            quantite = Integer.parseInt(textFieldQuantite.getText());
            if (quantite < 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "La quantité doit être un entier positif !");
            return;
        }

        float prixUnitaire;
        try {
            prixUnitaire = Float.parseFloat(textFieldPrixUnitaire.getText());
            if (prixUnitaire < 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Le prix unitaire doit être un nombre positif !");
            return;
        }

        String nom = textFieldNom.getText().trim();
        String type = comboBoxType.getValue();

        if (materielService.exists(nom, type)) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Matériel déjà existant");
            return;
        }

        String selectedFournisseurNom = comboBoxFournisseur.getValue();
        int idFournisseur = fournisseurService.getFournisseursByCategory(type)
                .stream()
                .filter(f -> f.getNom().equals(selectedFournisseurNom))
                .findFirst()
                .map(Fournisseur::getId_fournisseur)
                .orElse(1);

        try {
            String supplierCurrency = comboBoxCurrency.getValue();
            System.out.println("Adding material - Converting price from " + supplierCurrency + " to " + baseCurrency);
            prixUnitaire = materielService.convertPrice(prixUnitaire, supplierCurrency, baseCurrency);
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur de Conversion", "Impossible de convertir le prix : " + e.getMessage());
            return;
        }

        materielToAdd.setId_fournisseur(idFournisseur);
        materielToAdd.setNom(nom);
        materielToAdd.setType(TypeMateriel.valueOf(type));
        materielToAdd.setQuantite(quantite);
        materielToAdd.setEtat(EtatMateriel.valueOf(comboBoxEtat.getValue()));
        materielToAdd.setPrix_unitaire(prixUnitaire);
        materielToAdd.setBarcode(barcodeField.getText());

        materielList.add(materielToAdd);
        materielService.ajouter(materielToAdd);

        showAlert(Alert.AlertType.CONFIRMATION, "Succès", "Matériel ajouté avec succès !");
        clearFields();
    }

    @FXML
    private void convertPrice() {
        try {
            float originalPrice = Float.parseFloat(textFieldPrixUnitaire.getText());
            String fromCurrency = comboBoxCurrency.getValue();
            System.out.println("Converting price from " + fromCurrency + " to " + baseCurrency + ": " + originalPrice);
            float convertedPrice = materielService.convertPrice(originalPrice, fromCurrency, baseCurrency);
            textFieldPrixUnitaire.setText(String.format(Locale.US, "%.2f", convertedPrice));
            showAlert(Alert.AlertType.INFORMATION, "Conversion Réussie", "Prix converti en " + baseCurrency + ": " + convertedPrice);
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Veuillez entrer un prix valide.");
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur de Conversion", "Impossible de convertir le prix : " + e.getMessage());
        }
    }

    @FXML
    private void scanBarcode() {
        if (scanning) {
            scanning = false;
            showAlert(Alert.AlertType.INFORMATION, "Scan Arrêté", "Le scan a été arrêté.");
            return;
        }

        scanning = true;
        Webcam webcam = Webcam.getDefault();
        if (webcam == null) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Aucune webcam détectée.");
            scanning = false;
            return;
        }

        Stage scanStage = new Stage();
        scanStage.initModality(Modality.APPLICATION_MODAL);
        scanStage.setTitle("Scan Code-Barres");

        ImageView webcamView = new ImageView();
        webcamView.setFitWidth(640);
        webcamView.setFitHeight(480);

        double rectWidth = 600;
        double rectHeight = 200;
        Rectangle focusRect = new Rectangle((640 - rectWidth) / 2, (480 - rectHeight) / 2, rectWidth, rectHeight);
        focusRect.setFill(null);
        focusRect.setStroke(Color.GREEN);
        focusRect.setStrokeWidth(2);
        focusRect.getStrokeDashArray().addAll(10.0, 10.0);

        Rectangle scanLine = new Rectangle(focusRect.getX(), focusRect.getY(), rectWidth, 2);
        scanLine.setFill(Color.RED);
        scanLine.setStroke(Color.RED);

        Timeline scanAnimation = new Timeline(
                new KeyFrame(Duration.ZERO, e -> scanLine.setY(focusRect.getY())),
                new KeyFrame(Duration.seconds(2), e -> scanLine.setY(focusRect.getY() + rectHeight - 2))
        );
        scanAnimation.setCycleCount(Timeline.INDEFINITE);
        scanAnimation.setAutoReverse(true);
        scanAnimation.play();

        Label statusLabel = new Label("Positionnez le code-barres dans le cadre...");
        statusLabel.setStyle("-fx-text-fill: white; -fx-font-size: 14px;");

        Button stopButton = new Button("Arrêter le Scan");
        stopButton.setStyle("-fx-background-color: #ff4444; -fx-text-fill: white;");
        stopButton.setOnAction(e -> {
            scanning = false;
            scanStage.close();
        });

        Pane overlayPane = new Pane(webcamView, focusRect, scanLine);
        overlayPane.setPrefSize(640.0, 480.0);

        VBox mainVBox = new VBox(10.0, overlayPane, statusLabel, stopButton);
        mainVBox.setStyle("-fx-background-color: #333333; -fx-alignment: center; -fx-padding: 10;");
        Scene scanScene = new Scene(mainVBox, 640, 600);
        scanStage.setScene(scanScene);

        webcam.open();
        scanStage.show();

        executor.submit(() -> {
            MultiFormatReader reader = new MultiFormatReader();
            while (scanning) {
                try {
                    BufferedImage image = webcam.getImage();
                    if (image != null) {
                        Image fxImage = SwingFXUtils.toFXImage(image, null);
                        Platform.runLater(() -> webcamView.setImage(fxImage));

                        LuminanceSource source = new BufferedImageLuminanceSource(image);
                        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
                        Result result = reader.decode(bitmap);
                        if (result != null) {
                            String barcode = result.getText();
                            Platform.runLater(() -> {
                                barcodeField.setText(barcode);
                                statusLabel.setText("Code-barres détecté : " + barcode);
                                statusLabel.setStyle("-fx-text-fill: green; -fx-font-size: 14px;");
                                scanAnimation.stop();
                                new Timeline(new KeyFrame(Duration.seconds(3), e -> scanStage.close())).play();
                            });
                            scanning = false;
                            break;
                        }
                    }
                    Thread.sleep(50);
                } catch (NotFoundException e) {
                    // No barcode detected yet
                } catch (Exception e) {
                    e.printStackTrace();
                    Platform.runLater(() -> {
                        statusLabel.setText("Erreur : " + e.getMessage());
                        statusLabel.setStyle("-fx-text-fill: red; -fx-font-size: 14px;");
                        showAlert(Alert.AlertType.ERROR, "Erreur de Scan", "Erreur: " + e.getMessage());
                        scanAnimation.stop();
                        new Timeline(new KeyFrame(Duration.seconds(2), ev -> scanStage.close())).play();
                    });
                    scanning = false;
                    break;
                }
            }
            webcam.close();
            if (!scanning) {
                Platform.runLater(scanStage::close);
            }
        });
    }

    @FXML
    private void handleUploadImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choisir une image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg")
        );
        Stage stage = (Stage) uploadImageButton.getScene().getWindow();
        File imageFile = fileChooser.showOpenDialog(stage);
        if (imageFile != null) {
            try {
                String imagePath = saveImageToFile(imageFile);
                materielToAdd.setImagePath(imagePath);
                showAlert(Alert.AlertType.INFORMATION, "Succès", "Image téléchargée avec succès");
            } catch (IOException e) {
                showError("Erreur", "Échec du téléchargement de l'image", e);
            }
        }
    }

    private String saveImageToFile(File imageFile) throws IOException {
        // Ensure ImageMaterial directory exists
        Path imageDirPath = Paths.get(IMAGE_DIR);
        if (!Files.exists(imageDirPath)) {
            Files.createDirectories(imageDirPath);
        }

        // Generate unique filename using timestamp and barcode (if available)
        String barcode = barcodeField.getText().trim().isEmpty() ? String.valueOf(System.currentTimeMillis()) : barcodeField.getText();
        String fileName = "material_" + barcode + ".jpg";
        Path destinationPath = Paths.get(IMAGE_DIR, fileName);

        // Read and save the image
        BufferedImage bufferedImage = ImageIO.read(imageFile);
        ImageIO.write(bufferedImage, "jpg", destinationPath.toFile());

        // Return relative path for storage in database
        return IMAGE_RELATIVE_PATH + fileName;
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showError(String title, String header, Exception e) {
        e.printStackTrace();
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText("Détails : " + e.getMessage());
        alert.showAndWait();
    }

    private void clearFields() {
        textFieldNom.clear();
        textFieldQuantite.clear();
        textFieldPrixUnitaire.clear();
        barcodeField.clear();
        comboBoxType.setValue("MATERIEL_JEU");
        comboBoxEtat.setValue("NEUF");
        comboBoxCurrency.setValue("USD");
        updateFournisseurComboBox("MATERIEL_JEU");
        materielToAdd.setImagePath(null);
    }
}