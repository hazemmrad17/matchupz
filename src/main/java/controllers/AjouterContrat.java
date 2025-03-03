package controllers;

import com.twilio.Twilio;
import com.twilio.exception.TwilioException;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import models.Contrat;
import models.Sponsor;
import services.ContratService;
import services.SponsorService;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javafx.embed.swing.SwingFXUtils; // Explicit import

public class AjouterContrat {

    @FXML
    private Button btnAjouterContrat, btnAnnuler;

    @FXML
    private Canvas signature;

    @FXML
    private TextField tx_contrat_titre, tx_contrat_montant;

    @FXML
    private ComboBox<Sponsor> tx_contrat_sponsor;

    @FXML
    private DatePicker dp_date_debut, dp_date_fin;

    private final ContratService contratService = new ContratService();
    private final SponsorService sponsorService = new SponsorService();
    private ObservableList<Contrat> contratList = FXCollections.observableArrayList();
    private ObservableList<Sponsor> sponsorList = FXCollections.observableArrayList();
    private Contrat selectedContrat = null;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter MESSAGE_DATE_FORMATTER = DateTimeFormatter.ofPattern("MMMM d, yyyy");
    private GraphicsContext gc;

    @FXML
    private void ajouterContrat(ActionEvent event) {
        handleAjouterOuModifier(event);
    }

    @FXML
    private void goToAfficherContrats(ActionEvent event) {
        loadFXML("/AfficherContrat.fxml", "Liste des Contrats", event);
    }

    @FXML
    public void initialize() {
        // Set up DatePicker converters
        dp_date_debut.setConverter(new StringConverter<LocalDate>() {
            @Override
            public String toString(LocalDate date) {
                return date != null ? DATE_FORMATTER.format(date) : "";
            }

            @Override
            public LocalDate fromString(String string) {
                return string != null && !string.isEmpty() ? LocalDate.parse(string, DATE_FORMATTER) : null;
            }
        });

        dp_date_fin.setConverter(new StringConverter<LocalDate>() {
            @Override
            public String toString(LocalDate date) {
                return date != null ? DATE_FORMATTER.format(date) : "";
            }

            @Override
            public LocalDate fromString(String string) {
                return string != null && !string.isEmpty() ? LocalDate.parse(string, DATE_FORMATTER) : null;
            }
        });

        // Load sponsors into ComboBox
        loadSponsors();
        if (sponsorList.isEmpty()) {
            System.out.println("Warning: No sponsors loaded into ComboBox.");
        }
        tx_contrat_sponsor.setItems(sponsorList);
        tx_contrat_sponsor.setPromptText("Sélectionner un sponsor");

        // Configure ComboBox to display sponsor names
        tx_contrat_sponsor.setCellFactory(param -> new ListCell<Sponsor>() {
            @Override
            protected void updateItem(Sponsor item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getNom());
            }
        });

        tx_contrat_sponsor.setConverter(new StringConverter<Sponsor>() {
            @Override
            public String toString(Sponsor sponsor) {
                return sponsor == null ? "" : sponsor.getNom();
            }

            @Override
            public Sponsor fromString(String string) {
                return sponsorList.stream()
                        .filter(s -> s.getNom().equals(string))
                        .findFirst()
                        .orElse(null);
            }
        });

        // Initialize signature canvas
        gc = signature.getGraphicsContext2D();
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, signature.getWidth(), signature.getHeight());
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(2);

        signature.addEventHandler(MouseEvent.MOUSE_PRESSED, e -> {
            gc.beginPath();
            gc.moveTo(e.getX(), e.getY());
            gc.stroke();
        });

        signature.addEventHandler(MouseEvent.MOUSE_DRAGGED, e -> {
            gc.lineTo(e.getX(), e.getY());
            gc.stroke();
        });

        signature.addEventHandler(MouseEvent.MOUSE_RELEASED, e -> {
            // Drawing completed
        });
    }

    @FXML
    private void handleAjouterOuModifier(ActionEvent event) {
        String titre = tx_contrat_titre.getText().trim();
        Sponsor selectedSponsor = tx_contrat_sponsor.getValue();
        LocalDate dateDebut = dp_date_debut.getValue();
        LocalDate dateFin = dp_date_fin.getValue();
        String montantStr = tx_contrat_montant.getText().trim();

        // Validation: Check for empty fields
        if (titre.isEmpty() || selectedSponsor == null || dateDebut == null || dateFin == null || montantStr.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Veuillez remplir tous les champs.");
            return;
        }

        // Validation: Check title format (letters only)
        if (!titre.matches("[a-zA-Z\\s]+")) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Le titre doit contenir uniquement des lettres.");
            return;
        }

        // Validation: Check date logic
        if (!dateFin.isAfter(dateDebut)) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "La date de fin doit être postérieure à la date de début.");
            return;
        }

        // Validation: Check montant format
        float montant;
        try {
            montant = Float.parseFloat(montantStr);
            if (montant <= 0) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Le montant doit être supérieur à 0.");
                return;
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Le montant doit être un nombre valide.");
            return;
        }

        // Check if contract with the same title exists (for new contracts only)
        if (selectedContrat == null && contratService.isContratTitreExists(titre)) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Un contrat avec ce titre existe déjà.");
            return;
        }

        // Format dates as "yyyy-MM-dd" for database compatibility
        String dateDebutStr = dateDebut.format(DATE_FORMATTER);
        String dateFinStr = dateFin.format(DATE_FORMATTER);

        // Save signature and get its path
        String signaturePath = saveSignatureToFile(titre);
        if (signaturePath == null) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Échec de la sauvegarde de la signature. Veuillez réessayer.");
            return;
        }

        if (selectedContrat == null) {
            // New contract
            Contrat contrat = new Contrat(selectedSponsor.getId_sponsor(), titre, dateDebutStr, dateFinStr, montant, signaturePath);
            contratService.ajouter(contrat);
            sendTwilioMessage(contrat, selectedSponsor, dateDebut, dateFin, montant);
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Le contrat a été ajouté avec succès.");
            contratList.add(contrat);
        } else {
            // Modify existing contract
            selectedContrat.setTitre(titre);
            selectedContrat.setId_sponsor(selectedSponsor.getId_sponsor());
            selectedContrat.setDateDebut(dateDebutStr);
            selectedContrat.setDateFin(dateFinStr);
            selectedContrat.setMontant(montant);
            selectedContrat.setSignaturePath(signaturePath);
            contratService.modifier(selectedContrat);
            sendTwilioMessage(selectedContrat, selectedSponsor, dateDebut, dateFin, montant);
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Le contrat a été modifié avec succès.");
            loadContrats();
        }
        clearFields();
        handleAnnulerButton();
    }

    private String saveSignatureToFile(String titre) {
        String filePath = null;
        try {
            // Create a unique filename using the contract title and timestamp
            String fileName = "signature_" + titre.replaceAll("\\s+", "_") + "_" + System.currentTimeMillis() + ".png";
            File directory = new File("signatures");
            if (!directory.exists()) {
                directory.mkdirs();
            }
            filePath = "signatures/" + fileName;

            // Capture the canvas as an image
            WritableImage snapshot = signature.snapshot(null, null);
            BufferedImage bufferedImage = SwingFXUtils.fromFXImage(snapshot, null); // Correct usage

            // Save the image to file
            File outputFile = new File(filePath);
            ImageIO.write(bufferedImage, "png", outputFile);
            System.out.println("Signature saved successfully at: " + filePath);

            // Clear the canvas after saving
            gc.setFill(Color.WHITE);
            gc.fillRect(0, 0, signature.getWidth(), signature.getHeight());
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Échec de la sauvegarde de la signature : " + e.getMessage());
            e.printStackTrace();
        }
        return filePath;
    }

    private void loadContrats() {
        contratList.clear();
        contratList.addAll(contratService.rechercher());
    }

    private void loadSponsors() {
        sponsorList.clear();
        try {
            List<Sponsor> sponsors = sponsorService.rechercher();
            sponsorList.addAll(sponsors);
            if (sponsors.isEmpty()) {
                System.out.println("No sponsors found in the database.");
            }
        } catch (Exception e) {
            System.out.println("Error loading sponsors: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleAnnulerButton() {
        loadFXML("/AfficherContrat.fxml", "Liste des Contrats", btnAnnuler);
    }

    private void clearFields() {
        tx_contrat_titre.clear();
        tx_contrat_sponsor.setValue(null);
        dp_date_debut.setValue(null);
        dp_date_fin.setValue(null);
        tx_contrat_montant.clear();
        selectedContrat = null;
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, signature.getWidth(), signature.getHeight());
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void loadFXML(String fxmlPath, String stageTitle, Object eventSource) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            Stage stage = (Stage) (eventSource instanceof Node ? ((Node) eventSource).getScene().getWindow() : ((Button) eventSource).getScene().getWindow());
            stage.setScene(new Scene(root));
            stage.setTitle(stageTitle);
            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de charger la page: " + fxmlPath);
            e.printStackTrace();
        }
    }

    public void sendTwilioMessage(Contrat contract, Sponsor sponsor, LocalDate dateDebut, LocalDate dateFin, float montant) {
        System.out.println("Starting sendTwilioMessage function...");
        String toNumber = "21656623537";
        String fromNumber = "12517580857";
        System.out.println("Using To Number: " + toNumber);
        System.out.println("Using From Number: " + fromNumber);

        String body = "Contract titled " + contract.getTitre() + " with sponsor " + sponsor.getNom() +
                " from " + dateDebut.format(MESSAGE_DATE_FORMATTER) +
                " to " + dateFin.format(MESSAGE_DATE_FORMATTER) +
                " with amount " + String.format("%.2f", montant) + " has been registered.";
        System.out.println("Message body: " + body);

        try {
            System.out.println("Initializing Twilio with credentials...");
            Twilio.init("AC9192d3a406d49f6389fad1a6d7e33bfc", "275b9f165cf58235592e2649ac0ca900");
            System.out.println("Attempting to send message to " + toNumber + " from " + fromNumber);

            Message message = Message.creator(
                    new PhoneNumber(toNumber),
                    new PhoneNumber(fromNumber),
                    body
            ).create();

            System.out.println("Message sent successfully. SID: " + message.getSid());
            System.out.println("Message status: " + message.getStatus());
            System.out.println("Message date created: " + message.getDateCreated());
        } catch (TwilioException e) {
            System.err.println("Twilio Error sending message: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error sending message: " + e.getMessage());
            e.printStackTrace();
        }
        System.out.println("Finished sendTwilioMessage function.");
    }
}