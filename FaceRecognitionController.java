package controllers.user;

import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.objdetect.Objdetect;
import org.opencv.videoio.VideoCapture;

import java.io.File;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class FaceRecognitionController {
    private VideoCapture capture;
    private CascadeClassifier faceCascade;
    private int absoluteFaceSize;
    private boolean cameraActive;
    private ScheduledExecutorService timer;
    private ImageView imageView;
    private String capturedImagePath;

    public FaceRecognitionController() {
        this.capture = new VideoCapture();
        this.faceCascade = new CascadeClassifier("C:/Users/MSI/Desktop/matchupz-dev0/data/haarcascades/haarcascade_frontalface_default.xml");


        this.absoluteFaceSize = 0;
        this.cameraActive = false;
        this.capturedImagePath = null;

        if (faceCascade.empty()) {
            throw new RuntimeException("Erreur : Impossible de charger haarcascade_frontalface_default.xml");
        }
    }

    public void startCamera(ImageView view) {
        if (!this.cameraActive) {
            this.imageView = view;
            this.capture.open(0);

            if (this.capture.isOpened()) {
                this.cameraActive = true;

                Runnable frameGrabber = () -> {
                    Mat frame = grabFrame();
                    if (frame != null && !frame.empty()) {
                        Image imageToShow = Utils.mat2Image(frame);
                        updateImageView(imageView, imageToShow);
                    }
                };

                this.timer = Executors.newSingleThreadScheduledExecutor();
                this.timer.scheduleAtFixedRate(frameGrabber, 0, 33, TimeUnit.MILLISECONDS);
            } else {
                System.err.println("Erreur : Impossible d'ouvrir la caméra...");
            }
        }
    }

    public void stopCamera() {
        if (this.cameraActive) {
            this.cameraActive = false;
            stopAcquisition();
        }
    }

    public String captureFaceImage() {
        if (!this.capture.isOpened()) {
            System.err.println("La caméra n’est pas ouverte !");
            return null;
        }

        Mat frame = new Mat();
        this.capture.read(frame);

        if (!frame.empty()) {
            MatOfRect faces = new MatOfRect();
            Mat grayFrame = new Mat();

            // Convertir en gris et égaliser l’histogramme
            Imgproc.cvtColor(frame, grayFrame, Imgproc.COLOR_BGR2GRAY);
            Imgproc.equalizeHist(grayFrame, grayFrame);

            // Calculer la taille minimale du visage
            if (this.absoluteFaceSize == 0) {
                int height = grayFrame.rows();
                if (Math.round(height * 0.2f) > 0) {
                    this.absoluteFaceSize = Math.round(height * 0.2f);
                }
            }

            // Détecter les visages
            faceCascade.detectMultiScale(grayFrame, faces, 1.1, 2, 0 | Objdetect.CASCADE_SCALE_IMAGE,
                    new Size(this.absoluteFaceSize, this.absoluteFaceSize), new Size());

            Rect[] facesArray = faces.toArray();
            if (facesArray.length > 0) {
                Rect face = facesArray[0];
                Mat faceRegion = frame.submat(face);

                // Sauvegarder l’image capturée
                String filePath = " C:\\Users\\MSI\\Pictures\\Camera Roll\\image.jpg"; // + System.currentTimeMillis() + ".jpg";

                File file = new File(filePath);
                file.getParentFile().mkdirs(); // Créer le dossier si nécessaire
                Imgcodecs.imwrite(filePath, faceRegion);
                this.capturedImagePath = filePath;
                System.out.println("Image capturée pour login : " + filePath);
                return filePath;
            } else {
                System.out.println("❌ Aucun visage détecté dans l'image capturée.");
                return null;
            }
        }
        return null;
    }

    private Mat grabFrame() {
        Mat frame = new Mat();
        if (this.capture.isOpened()) {
            try {
                this.capture.read(frame);
                if (!frame.empty()) {
                    detectAndDisplay(frame);
                }
            } catch (Exception e) {
                System.err.println("Exception during the image elaboration: " + e);
            }
        }
        return frame;
    }

    private void detectAndDisplay(Mat frame) {
        MatOfRect faces = new MatOfRect();
        Mat grayFrame = new Mat();

        Imgproc.cvtColor(frame, grayFrame, Imgproc.COLOR_BGR2GRAY);
        Imgproc.equalizeHist(grayFrame, grayFrame);

        if (this.absoluteFaceSize == 0) {
            int height = grayFrame.rows();
            if (Math.round(height * 0.2f) > 0) {
                this.absoluteFaceSize = Math.round(height * 0.2f);
            }
        }

        faceCascade.detectMultiScale(grayFrame, faces, 1.1, 2, 0 | Objdetect.CASCADE_SCALE_IMAGE,
                new Size(this.absoluteFaceSize, this.absoluteFaceSize), new Size());

        Rect[] facesArray = faces.toArray();
        for (Rect face : facesArray) {
            Imgproc.rectangle(frame, face.tl(), face.br(), new Scalar(0, 255, 0), 3);
        }
    }

    private void stopAcquisition() {
        if (this.timer != null && !this.timer.isShutdown()) {
            try {
                this.timer.shutdown();
                this.timer.awaitTermination(33, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                System.err.println("Exception in stopping the frame capture: " + e);
            }
        }
        if (this.capture.isOpened()) {
            this.capture.release();
        }
    }

    private void updateImageView(ImageView view, javafx.scene.image.Image image) {
        if (view != null) {
            Platform.runLater(() -> view.setImage(image));
        }
    }

    public String getCapturedImagePath() {
        return this.capturedImagePath;
    }
}