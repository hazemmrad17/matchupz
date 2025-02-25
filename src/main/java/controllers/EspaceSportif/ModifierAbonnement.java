package controllers.EspaceSportif;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import models.EspaceSportif.Abonnement;
import models.Sport;
import services.EspaceSportif.AbonnementService;
import services.SportService;
import utils.MyDatabase; // Import your MyDatabase class for connection
import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

public class ModifierAbonnement {

    @FXML
    private ComboBox<String> sportField;

    @FXML
    private ComboBox<String> typeField;

    @FXML
    private DatePicker dateDebutField;

    @FXML
    private DatePicker dateFinField;

    @FXML
    private TextField tarifField;

    @FXML
    private ComboBox<String> etatField;

    @FXML
    private Button updateButton;

    @FXML
    private Button cancelButton;

    private final AbonnementService abonnementService;
    private final SportService sportService;
    private Abonnement abonnementToEdit;

    // Default constructor using MyDatabase singleton (similar to ModifierReservation)
    public ModifierAbonnement() {
        this.abonnementService = new AbonnementService(MyDatabase.getInstance().getConnection());
        this.sportService = new SportService();
    }

    @FXML
    public void initialize() {
        loadSports();
        loadTypes();
        loadEtats();
    }

    private void loadSports() {
        try {
            List<Sport> sports = sportService.rechercher();
            if (sports != null && !sports.isEmpty()) {
                sportField.getItems().setAll(sports.stream().map(Sport::getNomSport).toList());
            } else {
                showAlert(Alert.AlertType.WARNING, "Aucun sport", "Aucun sport disponible.");
            }
        } catch (RuntimeException e) { // Updated to catch RuntimeException (from SportService)
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de charger les sports : " + e.getMessage());
        }
    }

    private void loadTypes() {
        typeField.getItems().setAll("Mensuel", "Trimestriel", "Annuel");
    }

    private void loadEtats() {
        etatField.getItems().setAll("Actif", "Expiré", "Suspendu");
    }

    // Méthode utilitaire pour convertir java.sql.Date en LocalDate
    private LocalDate sqlDateToLocalDate(Date sqlDate) {
        if (sqlDate == null) return null;
        return sqlDate.toLocalDate();
    }

    public void setAbonnementToEdit(Abonnement abonnement) {
        this.abonnementToEdit = abonnement;
        sportField.setValue(abonnement.getNomSport());
        typeField.setValue(abonnement.getTypeAbonnement());
        // Conversion de java.sql.Date en LocalDate
        dateDebutField.setValue(sqlDateToLocalDate(abonnement.getDateDebut()));
        dateFinField.setValue(sqlDateToLocalDate(abonnement.getDateFin()));
        tarifField.setText(String.valueOf(abonnement.getTarif()));
        etatField.setValue(abonnement.getEtat());
    }

    @FXML
    private void updateAbonnement(ActionEvent event) {
        if (abonnementToEdit == null) return;

        String sportNom = sportField.getValue();
        String type = typeField.getValue();
        LocalDate dateDebut = dateDebutField.getValue();
        LocalDate dateFin = dateFinField.getValue();
        String tarifText = tarifField.getText();
        String etat = etatField.getValue();

        // Validation des champs
        if (sportNom == null || type == null || dateDebut == null || dateFin == null || tarifText.isEmpty() || etat == null) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Veuillez remplir tous les champs !");
            return;
        }

        if (dateFin.isBefore(dateDebut)) {
            showAlert(Alert.AlertType.WARNING, "Date invalide", "La date de fin doit être après la date de début.");
            return;
        }

        double tarif;
        try {
            tarif = Double.parseDouble(tarifText);
            if (tarif <= 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Le tarif doit être un nombre positif.");
            return;
        }

        // Récupérer l'ID du sport
        int idSport = getIdSportByName(sportNom);
        if (idSport == -1) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Sport non trouvé.");
            return;
        }

        // Mettre à jour l'objet Abonnement
        abonnementToEdit.setIdSport(idSport);
        abonnementToEdit.setNomSport(sportNom);
        abonnementToEdit.setTypeAbonnement(type);
        // Conversion de LocalDate en java.sql.Date
        abonnementToEdit.setDateDebut(Date.valueOf(dateDebut));
        abonnementToEdit.setDateFin(Date.valueOf(dateFin));
        abonnementToEdit.setTarif(tarif);
        abonnementToEdit.setEtat(etat);

        try {
            abonnementService.modifier(abonnementToEdit);
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Abonnement modifié avec succès !");
            goToAfficherAbonnements(event);
        } catch (RuntimeException e) { // Updated to catch RuntimeException (from AbonnementService)
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de modifier l'abonnement : " + e.getMessage());
        }
    }

    @FXML
    private void annuler(ActionEvent event) {
        closeWindow(event);
    }

    private int getIdSportByName(String nomSport) {
        try {
            List<Sport> sports = sportService.rechercher();
            for (Sport sport : sports) {
                if (sport.getNomSport().equals(nomSport)) {
                    return sport.getIdSport();
                }
            }
        } catch (RuntimeException e) { // Updated to catch RuntimeException (from SportService)
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de la récupération des sports : " + e.getMessage());
        }
        return -1;
    }

    private void closeWindow(ActionEvent event) {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.close();
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    private void goToAfficherAbonnements(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AffichageAbonnement.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Liste des Abonnements");
            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de charger la page AffichageAbonnement : " + e.getMessage());
        }
    }
}