package controllers;

import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import models.Sponsor;
import services.SponsorService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AjouterSponsor {

    @FXML
    private Button btn_ajout_sponsor;

    @FXML
    private Button btnAnnuler;
    @FXML
    private Button btnSupprimer;
    @FXML
    private Button btnActualiser;

    @FXML
    private TextField tx_sponsor_contact;

    @FXML
    private TextField tx_sponsor_nom;

    @FXML
    private ComboBox<String> tx_sponsor_pack;

    @FXML
    private TableView<Sponsor> tableViewSponsors;

    @FXML
    private TableColumn<Sponsor, String> colNom, colContact, colPack;

    private final SponsorService sponsorService = new SponsorService();
    private ObservableList<Sponsor> sponsorList = FXCollections.observableArrayList();
    private Sponsor selectedSponsor = null;

    @FXML
    private void ajouterSponsor(ActionEvent event) {
        // Récupérer les informations des champs
        String nom = tx_sponsor_nom.getText().trim();
        String contact = tx_sponsor_contact.getText().trim();
        String pack = tx_sponsor_pack.getValue();

        // Vérifier si les champs sont vides
        if (nom.isEmpty() || contact.isEmpty() || pack == null) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Veuillez remplir tous les champs !");
            return;
        }

        // Vérifier si le nom du sponsor est unique
        SponsorService ps = new SponsorService();
        if (ps.isSponsorNameExists(nom)) {
            showAlert(Alert.AlertType.ERROR, "Duplication", "Ce nom de sponsor existe déjà !");
            return;
        }

        // Vérifier que le contact contient exactement 8 chiffres
        if (!contact.matches("\\d{8}")) {
            showAlert(Alert.AlertType.ERROR, "Format incorrect", "Le numéro de contact doit contenir exactement 8 chiffres !");
            return;
        }


        // Ajouter le sponsor si toutes les vérifications passent
        ps.ajouter(new Sponsor(nom, contact, pack));

        // Message de confirmation
        showAlert(Alert.AlertType.INFORMATION, "Succès", "Le sponsor a été ajouté avec succès !");
        clearFields();
        goToAfficherSponsors(event);
    }


    @FXML
    private void goToAfficherSponsors(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherSponsor.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Liste des Sponsors");
            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de charger la page AfficherSponsor.");
            e.printStackTrace();
        }
    }



    @FXML
    public void initializer() {
        ObservableList<String> options = FXCollections.observableArrayList(
                "Bronze", "Silver", "Gold"
        );
        tx_sponsor_pack.setItems(options);
    }

    @FXML
    public void initialize() {
        // Set up column bindings
        colNom.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNom()));
        colContact.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getContact()));
        colPack.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPack()));

        tableViewSponsors.setItems(sponsorList);
        loadSponsors();

        // Event Listeners
        btn_ajout_sponsor.setOnAction(event -> handleAjouterOuModifier());
        btnAnnuler.setOnAction(event -> clearFields());
        btnSupprimer.setOnAction(event -> handleSupprimer());
        btnActualiser.setOnAction(event -> loadSponsors());

        // Selection Event
        tableViewSponsors.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                selectedSponsor = newSelection;
                tx_sponsor_nom.setText(selectedSponsor.getNom());
                tx_sponsor_contact.setText(selectedSponsor.getContact());
                tx_sponsor_pack.setValue(selectedSponsor.getPack());
            }
        });
    }

    private void loadSponsors() {
        sponsorList.clear();
        List<Sponsor> sponsors = new ArrayList();
        sponsors = sponsorService.rechercher();
        sponsorList.addAll(sponsors);
    }

    private void handleAjouterOuModifier() {
        String nom = tx_sponsor_nom.getText();
        String contact = tx_sponsor_contact.getText();
        String pack = tx_sponsor_pack.getValue();

        if (nom.isEmpty() || contact.isEmpty() || tx_sponsor_pack == null) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Tous les champs sont obligatoires !");
            return;
        }

        if (selectedSponsor == null) {
            Sponsor sponsor = new Sponsor(nom, contact, pack);
            sponsorService.ajouter(sponsor);
            sponsorList.add(sponsor);
        } else {
            selectedSponsor.setNom(nom);
            selectedSponsor.setContact(contact);
            selectedSponsor.setPack(pack);
            sponsorService.modifier(selectedSponsor);
            loadSponsors();
        }
        clearFields();
    }

    private void handleSupprimer() {
        Sponsor selected = tableViewSponsors.getSelectionModel().getSelectedItem();
        if (selected != null) {
            sponsorService.supprimer(selected);
            sponsorList.remove(selected);
        }
    }

    private void clearFields() {
        tx_sponsor_nom.clear();
        tx_sponsor_contact.clear();
        tx_sponsor_pack.setValue(null);
        selectedSponsor = null;
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }


}
