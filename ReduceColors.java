import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.util.Set;
import java.util.HashSet;


public class ReduceColors {
    
    public static void main(String[] args) {
    String input = args.length >= 1 ? "images/" + args[0] : "images/colorfull.png";
    String output = args.length >= 1 ? "output/" + args[0] + ".out" : "output/colorfull.png.out";
	int div = args.length >= 2 ? Integer.valueOf(args[1]) : 64;
        try {
            System.out.println(input);
            BufferedImage originalImage = ImageIO.read(new File(input));

            BufferedImage reducedImage = reduceColors(originalImage, div);

            ImageIO.write(reducedImage, "jpg", new File(output));

            System.out.println("Réduction des couleurs terminée.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static int reduceColor(int colorComponent, int div) {
        return (colorComponent / div) * div;
    }

    private static BufferedImage reduceColors(BufferedImage image, int div) {
    	Set<Integer> inputColors = new HashSet<>();
    	Set<Integer> outputColors = new HashSet<>();
    
        int width = image.getWidth();
        int height = image.getHeight();

        BufferedImage newImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int rgb = image.getRGB(x, y);
                inputColors.add(rgb);

                int red = (rgb >> 16) & 0xFF;
                int green = (rgb >> 8) & 0xFF;
                int blue = rgb & 0xFF;

                int newRed = reduceColor(red, div);
                int newGreen = reduceColor(green, div);
                int newBlue = reduceColor(blue, div);

                int newRgb = (newRed << 16) | (newGreen << 8) | newBlue;
                outputColors.add(newRgb);

                newImage.setRGB(x, y, newRgb);
            }
        }
        System.out.println("number of different input colors : "+inputColors.size());
        System.out.println("number of different output colors : "+outputColors.size());

        return newImage;
    }
}

