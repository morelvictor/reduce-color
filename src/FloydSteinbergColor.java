import java.awt.Color;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class FloydSteinbergColor {
    // Définir les couleurs prédéfinies
    private static final Color[] predefinedColors = {
        new Color(255, 255, 0),   // Jaune
        new Color(0, 255, 255),   // Cyan
        new Color(255, 0, 255),   // Magenta
        new Color(0, 0, 0)        // Noir
    };

    public static void main(String[] args) {
        for(int i = 1; i < 10; i++) {
            String path = "images/image" + i + ".png";
            try {
                BufferedImage originalImage = ImageIO.read(new File(path));
                BufferedImage newImage = floyd(originalImage);

                File reduced = new File("output/" + i + "-floyd-color.png");
                ImageIO.write(newImage, "png", reduced);
            } catch (IOException e) {
                System.out.println("Error reading or writing image file: " + e.getMessage());
            }
        }
    }

    public static BufferedImage floyd(BufferedImage originalImage) {
        BufferedImage newImage = new BufferedImage(originalImage.getWidth(), originalImage.getHeight(), BufferedImage.TYPE_INT_RGB);

            for (int y = 0; y < originalImage.getHeight(); y++) {
                for (int x = 0; x < originalImage.getWidth(); x++) {
                    Color originalColor = new Color(originalImage.getRGB(x, y));
                    Color closestColor = findClosestPredefinedColor(originalColor);
                    newImage.setRGB(x, y, closestColor.getRGB());

                    int[] error = {
                        originalColor.getRed() - closestColor.getRed(),
                        originalColor.getGreen() - closestColor.getGreen(),
                        originalColor.getBlue() - closestColor.getBlue()
                    };

                    diffuseError(originalImage, x, y, error);
                }
            }
        return newImage;
    }

    private static Color findClosestPredefinedColor(Color color) {
        Color closestColor = predefinedColors[0];
        double minDistance = colorDistance(color, closestColor);

        for (Color predefinedColor : predefinedColors) {
            double distance = colorDistance(color, predefinedColor);
            if (distance < minDistance) {
                minDistance = distance;
                closestColor = predefinedColor;
            }
        }

        return closestColor;
    }

    private static double colorDistance(Color c1, Color c2) {
        int redDiff = c1.getRed() - c2.getRed();
        int greenDiff = c1.getGreen() - c2.getGreen();
        int blueDiff = c1.getBlue() - c2.getBlue();
        return Math.sqrt(redDiff * redDiff + greenDiff * greenDiff + blueDiff * blueDiff);
    }

    private static void diffuseError(BufferedImage image, int x, int y, int[] error) {
        int[][] directions = {
            {1, 0, 7},  // droite
            {-1, 1, 3}, // bas gauche
            {0, 1, 5},  // bas
            {1, 1, 1}   // bas droite
        };

        for (int[] direction : directions) {
            int newX = x + direction[0];
            int newY = y + direction[1];
            int weight = direction[2];

            if (newX >= 0 && newX < image.getWidth() && newY >= 0 && newY < image.getHeight()) {
                Color oldColor = new Color(image.getRGB(newX, newY));
                int newRed = clamp(oldColor.getRed() + error[0] * weight / 16);
                int newGreen = clamp(oldColor.getGreen() + error[1] * weight / 16);
                int newBlue = clamp(oldColor.getBlue() + error[2] * weight / 16);
                Color newColor = new Color(newRed, newGreen, newBlue);
                image.setRGB(newX, newY, newColor.getRGB());
            }
        }
    }

    private static int clamp(int value) {
        return Math.max(0, Math.min(255, value));
    }
}

