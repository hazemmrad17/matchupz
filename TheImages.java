package models.match;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class TheImages {
    private ImageView images;

    public TheImages(String imageUrl) {
        setImages(imageUrl);
    }

    public void setImages(String imageUrl) {
        if (imageUrl != null && !imageUrl.isEmpty()) {
            String normalizedUrl = imageUrl;
            if (!imageUrl.startsWith("file:/") && !imageUrl.startsWith("http://") && !imageUrl.startsWith("https://")) {
                normalizedUrl = "file:/" + imageUrl.replace("\\", "/");
            }
            try {
                Image image = new Image(normalizedUrl);
                images = new ImageView(image);
                images.setFitHeight(50);
                images.setFitWidth(50);
                System.out.println("image exists");
            } catch (IllegalArgumentException e) {
                System.err.println("Invalid image URL: " + normalizedUrl);
                e.printStackTrace();
                images = null;
            }
        } else {
            images = null;
        }
    }

    public ImageView getImages() {
        return images;
    }
}