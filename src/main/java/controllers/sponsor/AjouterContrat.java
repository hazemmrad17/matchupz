package controllers.sponsor;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import models.Contrat;
import models.Sponsor;
import models.match.SessionManager;
import models.utilisateur.Role;
import models.utilisateur.User;
import services.ContratService;
import services.SponsorService;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class AjouterContrat {

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
            javafx.scene.image.Image image = new javafx.scene.image.Image(user.getImage());
            String name = user.getPrenom();
            nom_user.setText(name);
            // profileImageView.setImage(image);

        }
    }
    @FXML
    void match(ActionEvent event) {

    }


    @FXML
    void pageuser(ActionEvent event) {
        User user = SessionManager.getCurrentUser();
        if (user != null) {

            String role = user.getRole().getValue();
            if (user.getRole() == Role.ADMIN)
            {
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
    void sponsor(ActionEvent event) {

    }

    @FXML
    void teams(ActionEvent event) {

    }

    @FXML
    void dashboard(ActionEvent event) {

    }

    @FXML
    void espace(ActionEvent event) {
        User user = SessionManager.getCurrentUser();
        if (user != null) {

            String role = user.getRole().getValue();
            if (user.getRole() == Role.ADMIN)
            {
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
    private Button btnAjouterContrat, btnAnnuler;

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

    // Define the date formatter once for reuse
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

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
        // Set up the DatePicker converters
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

        // Optional: Add a listener to debug selection
        tx_contrat_sponsor.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            System.out.println("Selected Sponsor: " + (newVal != null ? newVal.getNom() : "None"));
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

        // Format dates as "yyyy-MM-dd" (e.g., 2025-02-15)
        String dateDebutStr = dateDebut.format(DATE_FORMATTER);
        String dateFinStr = dateFin.format(DATE_FORMATTER);

        if (selectedContrat == null) {
            Contrat contrat = new Contrat(selectedSponsor.getId_sponsor(), titre, dateDebutStr, dateFinStr, montant);
            contratService.ajouter(contrat);
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Le contrat a été ajouté avec succès.");
            contratList.add(contrat);
        } else {
            selectedContrat.setTitre(titre);
            selectedContrat.setId_sponsor(selectedSponsor.getId_sponsor());
            selectedContrat.setDateDebut(dateDebutStr);
            selectedContrat.setDateFin(dateFinStr);
            selectedContrat.setMontant(montant);
            contratService.modifier(selectedContrat);
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Le contrat a été modifié avec succès.");
            loadContrats();
        }
        clearFields();
        handleAnnulerButton();
    }

    private void loadContrats() {
        contratList.clear();
        contratList.addAll(contratService.recherche());
    }

    private void loadSponsors() {
        sponsorList.clear();
        try {
            List<Sponsor> sponsors = sponsorService.recherche();
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

    
}