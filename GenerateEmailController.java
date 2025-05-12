package controllers.user;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import models.utilisateur.User;
import services.user.UserService;

import java.awt.image.BufferedImage;
import java.util.HashMap;

public class GenerateEmailController {

    @FXML
    private TextField emailField;

    @FXML
    private Button generateButton;

    @FXML
    private Button scanButton;

    @FXML
    private ImageView qrImageView;

    private UserService userService = new UserService();

    @FXML
    public void initialize() {

    }

    @FXML
    private void generateQRCode() {
        String emailText = emailField.getText().trim();
        if (!emailText.isEmpty()) {

            User user = userService.verifierUtilisateurParEmail(emailText);
            if (user == null) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Réessayez, mail non valide.");
                return;
            }


            String qrData = String.format("id:%d,nom:%s,prenom:%s,dateNaissance:%s,telephone:%s",
                    user.getId_user(),
                    user.getNom(),
                    user.getPrenom(),
                    user.getDate_de_naissance().toString(),
                    user.getNum_telephone());

            try {
                QRCodeWriter qrCodeWriter = new QRCodeWriter();
                HashMap<EncodeHintType, Object> hints = new HashMap<>();
                hints.put(EncodeHintType.MARGIN, 1); // Margin around QR code
                BitMatrix bitMatrix = qrCodeWriter.encode(qrData, BarcodeFormat.QR_CODE, 250, 250, hints);

                // Convert BitMatrix to BufferedImage
                BufferedImage qrImage = MatrixToImageWriter.toBufferedImage(bitMatrix);
                Image fxImage = SwingFXUtils.toFXImage(qrImage, null);

                // Display the QR code in the ImageView
                qrImageView.setImage(fxImage);

                // Show success alert
                showAlert(Alert.AlertType.INFORMATION, "Succès", "QR code generated successfully.");
            } catch (WriterException e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de générer le QR Code : " + e.getMessage());
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Attention", "Veuillez entrer un email.");
        }
    }

    @FXML
    private void scanQRCode() {
        // Open a stage to scan QR code directly
        Stage scanStage = new Stage();
        scanStage.initModality(Modality.APPLICATION_MODAL);
        scanStage.setTitle("Scanner QR Code");


        Stage dummyQrStage = new Stage();
        AdminPageController adminController = new AdminPageController();
        adminController.scanQRCode(null, dummyQrStage);
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}