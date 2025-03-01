package controllers.MatchSchedule;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import models.EspaceSportif.EspaceSportif;
import models.match.Match;
import models.match.Schedule;
import models.match.SessionManager;
import models.utilisateur.Role;
import models.utilisateur.User;
import services.match.MatchService;
import services.match.ScheduleService;
import utils.MyDataSource;


import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ScheduleController {
    @FXML
    private ImageView profileImageView;

    @FXML
    private ImageView Home;

    @FXML
    private Button logout;
    @FXML
    private Label nom_user;

    @FXML
    private Button dashboard;
    @FXML
    private Button teams;


    @FXML
    private ImageView icon7;

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
    private Button bt_user;

    @FXML
    private ImageView icon2;

    @FXML
    private ImageView icon3;

    @FXML
    private ImageView icon4;

    @FXML
    private ImageView icon5;

    //******** Feryel
    @FXML
    private Button exportSchedulesButton;

    @FXML
    private Button exportMatchesButton;

    @FXML
    private Button addMatchButton;

    @FXML
    private Button addScheduleButton;

    @FXML
    private Button annulerButton;

    @FXML
    private ComboBox<String> comboBoxSportType;

    @FXML
    private TextField textFieldC1, textFieldC2;

    @FXML
    private Button btnAjoutMatch;

    @FXML
    private Button btnAnnulerMatch, btnActualiserMatch;

    @FXML
    private TableView<Match> tableViewMatches;

    @FXML
    private TableColumn<Match, Integer> colId;

    @FXML
    private TableColumn<Match, String> colC1, colC2, colSportType;

    @FXML
    private TableColumn<Match, Void> colActions;

    @FXML
    private DatePicker datePickerDateMatch;

    @FXML
    private Spinner<LocalTime> spinnerStartTime;

    @FXML
    private Spinner<LocalTime> spinnerEndTime;

    @FXML
    private ComboBox<Match> comboBoxIdMatchFK;

    @FXML
    private ComboBox<EspaceSportif> comboBoxEspaceSportif;

    @FXML
    private Button btnAjoutSchedule, btnAnnulerSchedule, btnActualiserSchedule;

    @FXML
    private Button btnModifierMatch;

    @FXML
    private Button btnModifierSchedule;

    @FXML
    private TableView<Schedule> tableViewSchedules;

    @FXML
    private TableColumn<Schedule, Integer> colIdSchedule;

    @FXML
    private TableColumn<Schedule, String> colMatchTeams;

    @FXML
    private TableColumn<Schedule, String> colLieuName;

    @FXML
    private TableColumn<Schedule, LocalDate> colDateMatch;

    @FXML
    private TableColumn<Schedule, LocalTime> colStartTime, colEndTime;

    @FXML
    private TableColumn<Schedule, Void> colActionsSchedule;

    @FXML
    private TextField searchField; // Match search field

    @FXML
    private TextField scheduleSearchField; // Schedule search field

    @FXML
    private Label unplayedMatchesLabel; // Label for unplayed matches count
    @FXML
    private Label playedMatchesLabel;    // Label for played matches count

    private final ScheduleService scheduleService = new ScheduleService();
    private ObservableList<Schedule> scheduleList = FXCollections.observableArrayList();
    private ObservableList<Schedule> filteredScheduleList = FXCollections.observableArrayList();
    private Schedule selectedSchedule = null;

    private final MatchService matchService = new MatchService();
    private ObservableList<Match> matchList = FXCollections.observableArrayList();
    private ObservableList<Match> filteredMatchList = FXCollections.observableArrayList();
    private Match selectedMatch = null;

    private ObservableList<EspaceSportif> espaceSportifList = FXCollections.observableArrayList();
    private ObservableList<Match> matchFKList = FXCollections.observableArrayList();

    Connection connection = MyDataSource.getInstance().getConn();

    private void afficherProfil(User user) {

        if (user.getImage() != null && !user.getImage().isEmpty()) {
            javafx.scene.image.Image image = new javafx.scene.image.Image(user.getImage());
            String name = user.getPrenom();
            nom_user.setText(name);
            profileImageView.setImage(image);

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
        loadMatchesForFK();
        loadEspaceSportifsFromDB();

        if (comboBoxSportType != null) {
            comboBoxSportType.setItems(FXCollections.observableArrayList(
                    "Football", "Basketball", "Tennis", "Volleyball", "Handball", "Boxing", "Wrestling"
            ));
        }

        if (tableViewMatches != null) {
            colId.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getIdMatch()).asObject());
            colC1.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getC1()));
            colC2.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getC2()));
            colSportType.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getSportType()));
            colActions.setCellFactory(param -> new TableCell<>() {
                private final Button btnModifier = new Button("Modifier");
                private final Button btnSupprimer = new Button("Supprimer");
                private final HBox container = new HBox(10, btnModifier, btnSupprimer);

                {
                    btnModifier.setOnAction(event -> remplirChampsPourModification(getTableView().getItems().get(getIndex())));
                    btnSupprimer.setOnAction(event -> handleSupprimerMatch(getTableView().getItems().get(getIndex())));
                }

                @Override
                protected void updateItem(Void item, boolean empty) {
                    super.updateItem(item, empty);
                    setGraphic(empty ? null : container);
                }
            });
            loadMatches();
            tableViewMatches.setItems(filteredMatchList);

            if (searchField != null) {
                searchField.textProperty().addListener((observable, oldValue, newValue) -> filterMatches(newValue));
            }
        }

        if (exportMatchesButton != null) {
            exportMatchesButton.setDisable(tableViewMatches.getItems().isEmpty());
            tableViewMatches.getItems().addListener((javafx.collections.ListChangeListener<Match>) change -> {
                exportMatchesButton.setDisable(tableViewMatches.getItems().isEmpty());
            });
        }

        if (tableViewSchedules != null) {
            colIdSchedule.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getIdSchedule()).asObject());
            colDateMatch.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getDateMatch()));
            colStartTime.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getStartTime()));
            colEndTime.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getEndTime()));
            colMatchTeams.setCellValueFactory(cellData -> {
                int idMatchFK = cellData.getValue().getIdMatchFK();
                Match match = matchFKList.stream()
                        .filter(m -> m.getIdMatch() == idMatchFK)
                        .findFirst()
                        .orElse(null);
                return new SimpleStringProperty(match != null ? match.getC1() + " vs " + match.getC2() : "N/A");
            });
            colLieuName.setCellValueFactory(cellData -> {
                int idLieu = cellData.getValue().getIdLieu();
                EspaceSportif espace = espaceSportifList.stream()
                        .filter(e -> e.getIdLieu() == idLieu)
                        .findFirst()
                        .orElse(null);
                return new SimpleStringProperty(espace != null ? espace.getNomEspace() : "N/A");
            });
            colActionsSchedule.setCellFactory(param -> new TableCell<>() {
                private final Button btnModifier = new Button("Modifier");
                private final Button btnSupprimer = new Button("Supprimer");
                private final HBox container = new HBox(10, btnModifier, btnSupprimer);

                {
                    btnModifier.setOnAction(event -> remplirChampsPourModificationSchedule(getTableView().getItems().get(getIndex())));
                    btnSupprimer.setOnAction(event -> handleSupprimerSchedule(getTableView().getItems().get(getIndex())));
                }

                @Override
                protected void updateItem(Void item, boolean empty) {
                    super.updateItem(item, empty);
                    setGraphic(empty ? null : container);
                }
            });

            // Add rowFactory to color rows based on schedule status
            tableViewSchedules.setRowFactory(tv -> new TableRow<Schedule>() {
                @Override
                protected void updateItem(Schedule schedule, boolean empty) {
                    super.updateItem(schedule, empty);
                    if (empty || schedule == null) {
                        setStyle(""); // Clear style if row is empty
                    } else {
                        LocalDateTime now = LocalDateTime.now();
                        LocalDateTime startTime = LocalDateTime.of(schedule.getDateMatch(), schedule.getStartTime());
                        LocalDateTime endTime = LocalDateTime.of(schedule.getDateMatch(), schedule.getEndTime());

                        if (now.isAfter(startTime) && now.isBefore(endTime)) {
                            // Color the row light green if current time is between startTime and endTime
                            setStyle("-fx-background-color: #90EE90;"); // Light green
                        } else if (now.isAfter(endTime)) {
                            // Color the row light red if the schedule has already ended
                            setStyle("-fx-background-color: #FF6E6E;"); // Light salmon (light red)
                        } else {
                            // No color (default style) if not active or not finished
                            setStyle("");
                        }
                    }
                }
            });

            loadSchedules();
            tableViewSchedules.setItems(filteredScheduleList);

            if (scheduleSearchField != null) {
                scheduleSearchField.textProperty().addListener((observable, oldValue, newValue) -> filterSchedules(newValue));
            }
        }

        if (exportSchedulesButton != null) {
            exportSchedulesButton.setDisable(tableViewSchedules.getItems().isEmpty());
            tableViewSchedules.getItems().addListener((javafx.collections.ListChangeListener<Schedule>) change -> {
                exportSchedulesButton.setDisable(tableViewSchedules.getItems().isEmpty());
            });
        }

        if (spinnerStartTime != null) {
            spinnerStartTime.setValueFactory(new SpinnerValueFactory<LocalTime>() {
                {
                    setValue(LocalTime.of(9, 0));
                }
                @Override
                public void decrement(int steps) {
                    LocalTime time = getValue().minusMinutes(steps * 30);
                    if (time.isBefore(LocalTime.of(0, 0))) time = LocalTime.of(0, 0);
                    setValue(time);
                }
                @Override
                public void increment(int steps) {
                    LocalTime time = getValue().plusMinutes(steps * 30);
                    if (time.isAfter(LocalTime.of(23, 59))) time = LocalTime.of(23, 59);
                    setValue(time);
                }
            });
            spinnerStartTime.setEditable(true);
        }

        if (spinnerEndTime != null) {
            spinnerEndTime.setValueFactory(new SpinnerValueFactory<LocalTime>() {
                {
                    setValue(LocalTime.of(11, 0));
                }
                @Override
                public void decrement(int steps) {
                    LocalTime time = getValue().minusMinutes(steps * 30);
                    if (time.isBefore(LocalTime.of(0, 0))) time = LocalTime.of(0, 0);
                    setValue(time);
                }
                @Override
                public void increment(int steps) {
                    LocalTime time = getValue().plusMinutes(steps * 30);
                    if (time.isAfter(LocalTime.of(23, 59))) time = LocalTime.of(23, 59);
                    setValue(time);
                }
            });
            spinnerEndTime.setEditable(true);
        }

        if (comboBoxEspaceSportif != null) {
            loadEspaceSportifsFromDB();
            comboBoxEspaceSportif.setItems(espaceSportifList);
            comboBoxEspaceSportif.setPromptText("Sélectionner un lieu");
            comboBoxEspaceSportif.setCellFactory(param -> new ListCell<EspaceSportif>() {
                @Override
                protected void updateItem(EspaceSportif item, boolean empty) {
                    super.updateItem(item, empty);
                    setText(empty || item == null ? null : item.getNomEspace());
                }
            });
            comboBoxEspaceSportif.setConverter(new StringConverter<EspaceSportif>() {
                @Override
                public String toString(EspaceSportif espace) {
                    return espace == null ? "Sélectionner un lieu" : espace.getNomEspace();
                }

                @Override
                public EspaceSportif fromString(String string) {
                    return espaceSportifList.stream()
                            .filter(e -> e.getNomEspace().equals(string))
                            .findFirst()
                            .orElse(null);
                }
            });
        }

        if (comboBoxIdMatchFK != null) {
            loadMatchesForFK();
            comboBoxIdMatchFK.setItems(matchFKList);
            comboBoxIdMatchFK.setPromptText("Sélectionner un match");
            comboBoxIdMatchFK.setCellFactory(param -> new ListCell<Match>() {
                @Override
                protected void updateItem(Match item, boolean empty) {
                    super.updateItem(item, empty);
                    setText(empty || item == null ? null : item.getC1() + " vs " + item.getC2());
                }
            });
            comboBoxIdMatchFK.setConverter(new StringConverter<Match>() {
                @Override
                public String toString(Match match) {
                    return match == null ? "Sélectionner un match" : match.getC1() + " vs " + match.getC2();
                }

                @Override
                public Match fromString(String string) {
                    return matchFKList.stream()
                            .filter(m -> (m.getC1() + " vs " + m.getC2()).equals(string))
                            .findFirst()
                            .orElse(null);
                }
            });
        }

        // Check if labels exist before updating counts to avoid NPE
        if (unplayedMatchesLabel != null && playedMatchesLabel != null) {
            updateMatchCounts();
        }
    }

    private void loadEspaceSportifsFromDB() {
        espaceSportifList.clear();
        String query = "SELECT id_lieu, nom_espace, adresse, categorie, capacite FROM espacesportif";
        try (PreparedStatement pstmt = connection.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                int idLieu = rs.getInt("id_lieu");
                String nomEspace = rs.getString("nom_espace");
                String adresse = rs.getString("adresse");
                String categorie = rs.getString("categorie");
                float capacite = rs.getFloat("capacite");
                espaceSportifList.add(new EspaceSportif(idLieu, nomEspace, adresse, categorie, capacite));
                System.out.println("Loaded Espace: ID=" + idLieu + ", Name=" + nomEspace);
            }
            System.out.println("espaceSportifList size: " + espaceSportifList.size());
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur de base de données", "Impossible de charger les espaces sportifs : " + e.getMessage());
        }
    }

    private void loadMatchesForFK() {
        matchFKList.clear();
        String query = "SELECT idMatch, c1, c2, sportType FROM matchs";
        try (PreparedStatement pstmt = connection.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                int idMatch = rs.getInt("idMatch");
                String c1 = rs.getString("c1");
                String c2 = rs.getString("c2");
                String sportType = rs.getString("sportType");
                matchFKList.add(new Match(idMatch, c1, c2, sportType));
                System.out.println("Loaded Match: ID=" + idMatch + ", Teams=" + c1 + " vs " + c2);
            }
            System.out.println("matchFKList size: " + matchFKList.size());
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur de base de données", "Impossible de charger les matchs : " + e.getMessage());
        }
    }

    @FXML
    private void handleExportMatches() {
        ObservableList<Match> matches = tableViewMatches.getItems();
        if (matches.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Aucun Match", "Il n'y a aucun match à exporter !");
            return;
        }

        String fileName = "Matches_" + java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".pdf";

        try {
            Document document = new Document();
            document.setMargins(20, 20, 20, 20);
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(fileName));
            writer.setPageEvent(new BorderEvent());
            document.open();

            Font boldFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16, BaseColor.BLACK);
            Paragraph title = new Paragraph("Liste des Matchs", boldFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            document.add(new Paragraph(" "));

            PdfPTable pdfTable = new PdfPTable(4);
            pdfTable.setWidthPercentage(100);
            BaseColor deepGreen = new BaseColor(0, 100, 0);
            String[] headers = {"ID Match", "Équipe 1", "Équipe 2", "Type de Sport"};
            for (String header : headers) {
                PdfPCell headerCell = new PdfPCell(new Paragraph(header));
                headerCell.setBackgroundColor(deepGreen);
                headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                headerCell.setPadding(5);
                pdfTable.addCell(headerCell);
            }

            for (Match match : matches) {
                pdfTable.addCell(String.valueOf(match.getIdMatch()));
                pdfTable.addCell(match.getC1());
                pdfTable.addCell(match.getC2());
                pdfTable.addCell(match.getSportType());
            }

            document.add(pdfTable);
            document.add(new Paragraph(" "));

            Paragraph footer1 = new Paragraph("Gestion des Match et Planification");
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
            logo.scaleToFit(184, 41);
            logo.setAlignment(Element.ALIGN_CENTER);
            document.add(logo);

            document.close();

            showAlert(Alert.AlertType.INFORMATION, "Succès", "Les matchs ont été exportés dans " + fileName + " !");
        } catch (DocumentException | IOException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Échec de l'exportation des matchs : " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void goToEspaceSportif(ActionEvent actionEvent) {

    }

    private static class BorderEvent extends PdfPageEventHelper {
        @Override
        public void onEndPage(PdfWriter writer, Document document) {
            PdfContentByte cb = writer.getDirectContent();
            cb.setColorStroke(new BaseColor(255, 204, 0));
            cb.setLineWidth(2f);
            cb.setLineDash(5f, 3f);
            Rectangle pageSize = document.getPageSize();
            float margin = 10;
            float left = margin;
            float right = pageSize.getWidth() - margin;
            float top = pageSize.getHeight() - margin;
            float bottom = margin;
            cb.rectangle(left, bottom, right - left, top - bottom);
            cb.stroke();
        }
    }

    @FXML
    private void handleExportSchedules() {
        ObservableList<Schedule> schedules = tableViewSchedules.getItems();
        if (schedules.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Aucun Programme", "Il n'y a aucun programme à exporter !");
            return;
        }

        String fileName = "Schedules_" + java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".pdf";

        try {
            Document document = new Document();
            document.setMargins(20, 20, 20, 20);
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(fileName));
            writer.setPageEvent(new BorderEvent());
            document.open();

            Font boldFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16, BaseColor.BLACK);
            Paragraph title = new Paragraph("Liste des Programmes", boldFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            document.add(new Paragraph(" "));

            PdfPTable pdfTable = new PdfPTable(6);
            pdfTable.setWidthPercentage(100);
            BaseColor deepGreen = new BaseColor(0, 100, 0);
            String[] headers = {"ID Schedule", "Date", "Heure Début", "Heure Fin", "Équipes", "Lieu"};
            for (String header : headers) {
                PdfPCell headerCell = new PdfPCell(new Paragraph(header));
                headerCell.setBackgroundColor(deepGreen);
                headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                headerCell.setPadding(5);
                pdfTable.addCell(headerCell);
            }

            for (Schedule schedule : schedules) {
                pdfTable.addCell(String.valueOf(schedule.getIdSchedule()));
                pdfTable.addCell(schedule.getDateMatch().toString());
                pdfTable.addCell(schedule.getStartTime().toString());
                pdfTable.addCell(schedule.getEndTime().toString());
                Match match = matchFKList.stream()
                        .filter(m -> m.getIdMatch() == schedule.getIdMatchFK())
                        .findFirst()
                        .orElse(null);
                pdfTable.addCell(match != null ? match.getC1() + " vs " + match.getC2() : "N/A");
                EspaceSportif espace = espaceSportifList.stream()
                        .filter(e -> e.getIdLieu() == schedule.getIdLieu())
                        .findFirst()
                        .orElse(null);
                pdfTable.addCell(espace != null ? espace.getNomEspace() : "N/A");
            }

            document.add(pdfTable);
            document.add(new Paragraph(" "));

            Paragraph footer1 = new Paragraph("Gestion des Match et Planification");
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
            logo.scaleToFit(184, 41);
            logo.setAlignment(Element.ALIGN_CENTER);
            document.add(logo);

            document.close();

            showAlert(Alert.AlertType.INFORMATION, "Succès", "Les programmes ont été exportés dans " + fileName + " !");
        } catch (DocumentException | IOException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Échec de l'exportation des programmes : " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void loadMatches() {
        matchList.clear();
        matchList.addAll(matchService.recherche());
        filterMatches(""); // Initially show all matches
    }

    private void loadSchedules() {
        scheduleList.clear();
        scheduleList.addAll(scheduleService.recherche());
        filterSchedules(""); // Initially show all schedules
        // Check if labels exist before updating counts to avoid NPE
        if (unplayedMatchesLabel != null && playedMatchesLabel != null) {
            updateMatchCounts();
        }
    }

    private void filterMatches(String searchText) {
        filteredMatchList.clear();
        if (searchText == null || searchText.trim().isEmpty()) {
            filteredMatchList.addAll(matchList);
        } else {
            String lowerSearchText = searchText.trim().toLowerCase();
            filteredMatchList.addAll(matchList.stream()
                    .filter(match -> match.getC1().toLowerCase().contains(lowerSearchText) ||
                            match.getC2().toLowerCase().contains(lowerSearchText))
                    .collect(Collectors.toList()));
        }
    }

    private void filterSchedules(String searchText) {
        filteredScheduleList.clear();
        if (searchText == null || searchText.trim().isEmpty()) {
            filteredScheduleList.addAll(scheduleList);
        } else {
            String lowerSearchText = searchText.trim().toLowerCase();
            filteredScheduleList.addAll(scheduleList.stream()
                    .filter(schedule -> {
                        Match match = matchFKList.stream()
                                .filter(m -> m.getIdMatch() == schedule.getIdMatchFK())
                                .findFirst()
                                .orElse(null);
                        boolean teamMatch = match != null &&
                                (match.getC1().toLowerCase().contains(lowerSearchText) ||
                                        match.getC2().toLowerCase().contains(lowerSearchText));

                        EspaceSportif espace = espaceSportifList.stream()
                                .filter(e -> e.getIdLieu() == schedule.getIdLieu())
                                .findFirst()
                                .orElse(null);
                        boolean lieuMatch = espace != null &&
                                espace.getNomEspace().toLowerCase().contains(lowerSearchText);

                        return teamMatch || lieuMatch;
                    })
                    .collect(Collectors.toList()));
        }
    }

    @FXML
    private void handleAjouterOuModifyMatch() {
        System.out.println("handleAjouterOuModifyMatch called");
        if (!validerChampsMatch()) return;

        String c1 = textFieldC1.getText().trim();
        String c2 = textFieldC2.getText().trim();
        String sportType = comboBoxSportType.getValue();

        Match match = new Match(c1, c2, sportType);
        matchService.ajouter(match);
        matchList.add(match);
        loadMatchesForFK();
        filterMatches(searchField != null ? searchField.getText() : "");
        showAlert(Alert.AlertType.INFORMATION, "Succès", "Match ajouté avec succès !");

        clearFieldsMatch();
        handleAnnulerButton();
    }

    @FXML
    private void handleModifierMatch() {
        System.out.println("handleModifierMatch called");
        if (!validerChampsMatch()) return;

        String c1 = textFieldC1.getText().trim();
        String c2 = textFieldC2.getText().trim();
        String sportType = comboBoxSportType.getValue();

        if (selectedMatch != null) {
            selectedMatch.setC1(c1);
            selectedMatch.setC2(c2);
            selectedMatch.setSportType(sportType);
            matchService.modifier(selectedMatch);
            loadMatches();
            filterMatches(searchField != null ? searchField.getText() : "");
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Match modifié avec succès !");
        }

        clearFieldsMatch();
        handleAnnulerButton();
    }

    private void handleSupprimerMatch(Match match) {
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirmation");
        confirmation.setContentText("Voulez-vous vraiment supprimer ce match ? Cela supprimera également tous les programmes associés.");
        Optional<ButtonType> result = confirmation.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            List<Schedule> schedulesToDelete = new ArrayList<>();
            for (Schedule schedule : scheduleList) {
                if (schedule.getIdMatchFK() == match.getIdMatch()) {
                    schedulesToDelete.add(schedule);
                }
            }

            for (Schedule schedule : schedulesToDelete) {
                scheduleService.supprimer(schedule);
                scheduleList.remove(schedule);
            }

            matchService.supprimer(match);
            matchList.remove(match);
            loadMatchesForFK();
            filterMatches(searchField != null ? searchField.getText() : "");
            filterSchedules(scheduleSearchField != null ? scheduleSearchField.getText() : "");
            // Check if labels exist before updating counts
            if (unplayedMatchesLabel != null && playedMatchesLabel != null) {
                updateMatchCounts();
            }
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Match et " + schedulesToDelete.size() + " programme(s) associé(s) supprimé(s) !");
        }
    }

    private void remplirChampsPourModification(Match match) {
        selectedMatch = match;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/MatchSchedule/EditMatch.fxml"));
            Parent root = loader.load();
            ScheduleController editController = loader.getController();
            editController.textFieldC1.setText(match.getC1());
            editController.textFieldC2.setText(match.getC2());
            editController.comboBoxSportType.setValue(match.getSportType());
            editController.selectedMatch = match;

            editController.comboBoxSportType.setConverter(new StringConverter<String>() {
                @Override
                public String toString(String sport) {
                    return sport == null ? "Choisir le type du sport" : sport;
                }

                @Override
                public String fromString(String string) {
                    return string;
                }
            });
            editController.comboBoxSportType.setValue(match.getSportType());

            Stage stage = (Stage) tableViewMatches.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Échec du chargement du formulaire de modification : " + e.getMessage());
        }
    }

    private void clearFieldsMatch() {
        if (textFieldC1 != null) textFieldC1.clear();
        if (textFieldC2 != null) textFieldC2.clear();
        if (comboBoxSportType != null) comboBoxSportType.setValue(null);
        selectedMatch = null;
    }

    private boolean validerChampsMatch() {
        if (textFieldC1.getText().trim().isEmpty() || textFieldC2.getText().trim().isEmpty() || comboBoxSportType.getValue() == null) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Tous les champs doivent être remplis !");
            return false;
        }
        return true;
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    private void handleAjouterOuModifySchedule() {
        System.out.println("handleAjouterOuModifySchedule called");
        if (!validerChampsSchedule()) return;

        LocalDate dateMatch = datePickerDateMatch.getValue();
        LocalTime startTime = spinnerStartTime.getValue();
        LocalTime endTime = spinnerEndTime.getValue();

        // Check if the selected date is in the past
        LocalDate now = LocalDate.now();
        if (dateMatch.isBefore(now)) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Vous ne pouvez pas ajouter un programme avec une date passée (avant aujourd'hui) !");
            return;
        }

        Match selectedMatchFK = comboBoxIdMatchFK.getValue();
        if (selectedMatchFK == null) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Veuillez sélectionner un match !");
            return;
        }
        int idMatchFK = selectedMatchFK.getIdMatch();

        EspaceSportif selectedEspace = comboBoxEspaceSportif.getValue();
        if (selectedEspace == null) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Veuillez sélectionner un espace sportif !");
            return;
        }
        int idLieu = selectedEspace.getIdLieu();

        if (startTime.isAfter(endTime)) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "L'heure de début (" + startTime + ") ne peut pas être après l'heure de fin (" + endTime + ") !");
            return;
        }
        if (startTime.equals(endTime)) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "L'heure de début et l'heure de fin ne peuvent pas être identiques !");
            return;
        }

        Schedule schedule = new Schedule(dateMatch, startTime, endTime, idMatchFK, idLieu);
        scheduleService.ajouter(schedule);
        scheduleList.add(schedule);
        filterSchedules(scheduleSearchField != null ? scheduleSearchField.getText() : "");
        // Check if labels exist before updating counts
        if (unplayedMatchesLabel != null && playedMatchesLabel != null) {
            updateMatchCounts();
        }
        showAlert(Alert.AlertType.INFORMATION, "Succès", "Programme ajouté avec succès !");

        clearFieldsSchedule();
        handleAnnulerButton();
    }

    @FXML
    private void handleModifierSchedule() {
        System.out.println("handleModifierSchedule called");
        if (!validerChampsSchedule()) return;

        LocalDate dateMatch = datePickerDateMatch.getValue();
        LocalTime startTime = spinnerStartTime.getValue();
        LocalTime endTime = spinnerEndTime.getValue();

        // Check if the selected date is in the past
        LocalDate now = LocalDate.now();
        if (dateMatch.isBefore(now)) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Vous ne pouvez pas modifier un programme avec une date passée (avant aujourd'hui) !");
            return;
        }

        Match selectedMatchFK = comboBoxIdMatchFK.getValue();
        if (selectedMatchFK == null) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Veuillez sélectionner un match !");
            return;
        }
        int idMatchFK = selectedMatchFK.getIdMatch();

        EspaceSportif selectedEspace = comboBoxEspaceSportif.getValue();
        if (selectedEspace == null) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Veuillez sélectionner un espace sportif !");
            return;
        }
        int idLieu = selectedEspace.getIdLieu();

        if (startTime.isAfter(endTime)) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "L'heure de début (" + startTime + ") ne peut pas être après l'heure de fin (" + endTime + ") !");
            return;
        }
        if (startTime.equals(endTime)) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "L'heure de début et l'heure de fin ne peuvent pas être identiques !");
            return;
        }

        if (selectedSchedule != null) {
            selectedSchedule.setDateMatch(dateMatch);
            selectedSchedule.setStartTime(startTime);
            selectedSchedule.setEndTime(endTime);
            selectedSchedule.setIdMatchFK(idMatchFK);
            selectedSchedule.setIdLieu(idLieu);
            scheduleService.modifier(selectedSchedule);
            loadSchedules();
            filterSchedules(scheduleSearchField != null ? scheduleSearchField.getText() : "");
            // Check if labels exist before updating counts
            if (unplayedMatchesLabel != null && playedMatchesLabel != null) {
                updateMatchCounts();
            }
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Programme modifié avec succès !");
        }

        clearFieldsSchedule();
        handleAnnulerButton();
    }

    private void handleSupprimerSchedule(Schedule schedule) {
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirmation");
        confirmation.setContentText("Voulez-vous vraiment supprimer ce programme ?");
        Optional<ButtonType> result = confirmation.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            scheduleService.supprimer(schedule);
            scheduleList.remove(schedule);
            filterSchedules(scheduleSearchField != null ? scheduleSearchField.getText() : "");
            // Check if labels exist before updating counts
            if (unplayedMatchesLabel != null && playedMatchesLabel != null) {
                updateMatchCounts();
            }
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Programme supprimé !");
        }
    }

    private void remplirChampsPourModificationSchedule(Schedule schedule) {
        selectedSchedule = schedule;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/MatchSchedule/EditSchedule.fxml"));
            Parent root = loader.load();
            ScheduleController editController = loader.getController();
            editController.datePickerDateMatch.setValue(schedule.getDateMatch());
            editController.spinnerStartTime.getValueFactory().setValue(schedule.getStartTime());
            editController.spinnerEndTime.getValueFactory().setValue(schedule.getEndTime());
            Match selectedMatch = matchFKList.stream()
                    .filter(m -> m.getIdMatch() == schedule.getIdMatchFK())
                    .findFirst()
                    .orElse(null);
            editController.comboBoxIdMatchFK.setValue(selectedMatch);
            EspaceSportif selectedEspace = espaceSportifList.stream()
                    .filter(es -> es.getIdLieu() == schedule.getIdLieu())
                    .findFirst()
                    .orElse(null);
            editController.comboBoxEspaceSportif.setValue(selectedEspace);
            editController.selectedSchedule = schedule;

            editController.comboBoxIdMatchFK.setConverter(new StringConverter<Match>() {
                @Override
                public String toString(Match match) {
                    return match == null ? "Sélectionner un match" : match.getC1() + " vs " + match.getC2();
                }

                @Override
                public Match fromString(String string) {
                    return matchFKList.stream()
                            .filter(m -> (m.getC1() + " vs " + m.getC2()).equals(string))
                            .findFirst()
                            .orElse(null);
                }
            });
            editController.comboBoxIdMatchFK.setValue(selectedMatch);

            editController.comboBoxEspaceSportif.setConverter(new StringConverter<EspaceSportif>() {
                @Override
                public String toString(EspaceSportif espace) {
                    return espace == null ? "Sélectionner un lieu" : espace.getNomEspace();
                }

                @Override
                public EspaceSportif fromString(String string) {
                    return espaceSportifList.stream()
                            .filter(e -> e.getNomEspace().equals(string))
                            .findFirst()
                            .orElse(null);
                }
            });
            editController.comboBoxEspaceSportif.setValue(selectedEspace);

            Stage stage = (Stage) tableViewSchedules.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Échec du chargement du formulaire de modification : " + e.getMessage());
        }
    }

    private void clearFieldsSchedule() {
        if (datePickerDateMatch != null) datePickerDateMatch.setValue(null);
        if (spinnerStartTime != null) spinnerStartTime.getValueFactory().setValue(LocalTime.of(9, 0));
        if (spinnerEndTime != null) spinnerEndTime.getValueFactory().setValue(LocalTime.of(11, 0));
        if (comboBoxIdMatchFK != null) comboBoxIdMatchFK.setValue(null);
        if (comboBoxEspaceSportif != null) comboBoxEspaceSportif.setValue(null);
        selectedSchedule = null;
    }

    private boolean validerChampsSchedule() {
        if (datePickerDateMatch.getValue() == null || spinnerStartTime.getValue() == null ||
                spinnerEndTime.getValue() == null || comboBoxIdMatchFK.getValue() == null ||
                comboBoxEspaceSportif.getValue() == null) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Tous les champs doivent être remplis !");
            return false;
        }
        return true;
    }

    @FXML
    private void handleAddMatchButton() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/MatchSchedule/AjoutMatch.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) addMatchButton.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Échec du chargement du formulaire d'ajout de match : " + e.getMessage());
        }
    }

    @FXML
    private void handleAddScheduleButton() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/MatchSchedule/AjoutSchedule.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) addScheduleButton.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Échec du chargement du formulaire d'ajout de programme : " + e.getMessage());
        }
    }

    @FXML
    private void handleAnnulerButton() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/MatchSchedule/AffichageMatch.fxml"));
            Parent root = loader.load();
            Stage stage = null;
            boolean fromScheduleContext = false;

            if (annulerButton != null) {
                stage = (Stage) annulerButton.getScene().getWindow();
            } else if (btnAnnulerSchedule != null) {
                stage = (Stage) btnAnnulerSchedule.getScene().getWindow();
                fromScheduleContext = true;
            } else if (btnAjoutSchedule != null) {
                stage = (Stage) btnAjoutSchedule.getScene().getWindow();
                fromScheduleContext = true;
            } else if (btnAjoutMatch != null) {
                stage = (Stage) btnAjoutMatch.getScene().getWindow();
            } else if (addMatchButton != null) {
                stage = (Stage) addMatchButton.getScene().getWindow();
            } else if (addScheduleButton != null) {
                stage = (Stage) addScheduleButton.getScene().getWindow();
                fromScheduleContext = true;
            } else if (btnModifierMatch != null) {
                stage = (Stage) btnModifierMatch.getScene().getWindow();
            } else if (btnModifierSchedule != null) {
                stage = (Stage) btnModifierSchedule.getScene().getWindow();
                fromScheduleContext = true;
            }
            if (stage == null) {
                throw new IllegalStateException("No valid button to determine stage");
            }

            Scene scene = new Scene(root);
            stage.setScene(scene);

            if (fromScheduleContext) {
                TabPane tabPane = (TabPane) root.lookup("#tabPane");
                if (tabPane != null) {
                    tabPane.getSelectionModel().select(1); // 0 = Matches, 1 = Schedules
                }
            }

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Échec du retour à l'affichage principal : " + e.getMessage());
        }
    }

    private void updateMatchCounts() {
        if (scheduleList == null || scheduleList.isEmpty()) {
            if (unplayedMatchesLabel != null) unplayedMatchesLabel.setText("0");
            if (playedMatchesLabel != null) playedMatchesLabel.setText("0");
            return;
        }

        int unplayedCount = 0;
        int playedCount = 0;
        LocalDateTime now = LocalDateTime.now();

        for (Schedule schedule : scheduleList) {
            LocalDateTime scheduleTime = LocalDateTime.of(schedule.getDateMatch(), schedule.getStartTime());
            if (scheduleTime.isAfter(now)) {
                unplayedCount++; // Match hasn't been played yet (start time is in the future)
            } else {
                playedCount++; // Match has already been played (start time is in the past or now)
            }
        }

        if (unplayedMatchesLabel != null) unplayedMatchesLabel.setText(String.valueOf(unplayedCount));
        if (playedMatchesLabel != null) playedMatchesLabel.setText(String.valueOf(playedCount));
    }



    // Feryel :

    @FXML
    void dashboard(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Home.fxml"));
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
            Stage stage = (Stage) logout.getScene().getWindow();
            stage.setScene(new Scene(loader.load()));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de charger la page d'inscription.");
        }
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
    void teams(ActionEvent event) {
        User user = SessionManager.getCurrentUser();
        if (user != null) {

            String role = user.getRole().getValue();
            if (user.getRole() == Role.ADMIN)
            {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/joueur/DisplayJoueur.fxml"));
                    Stage stage = (Stage) teams.getScene().getWindow();
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
    void espace(ActionEvent event) {
        User user = SessionManager.getCurrentUser();
        if (user != null) {

            String role = user.getRole().getValue();
            if (user.getRole() == Role.ADMIN)
            {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/AffichageEspace.fxml"));
                    Stage stage = (Stage) teams.getScene().getWindow();
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
        User user = SessionManager.getCurrentUser();
        if (user != null) {

            String role = user.getRole().getValue();
            if (user.getRole() == Role.ADMIN)
            {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/fournisseur/DisplayFournisseur.fxml"));
                    Stage stage = (Stage) teams.getScene().getWindow();
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
        User user = SessionManager.getCurrentUser();
        if (user != null) {

            String role = user.getRole().getValue();
            if (user.getRole() == Role.ADMIN)
            {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherSponsor.fxml"));
                    Stage stage = (Stage) teams.getScene().getWindow();
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

}