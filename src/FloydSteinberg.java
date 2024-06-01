import java.awt.Color;

import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.File;

import javax.imageio.ImageIO;

import java.util.Random;

public class FloydSteinberg {

	public static BufferedImage dither(BufferedImage originalImage) {
		int width = originalImage.getWidth();
		int height = originalImage.getHeight();

		BufferedImage ditheredImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

		int[][] pixels = new int[width][height];
		// Copie des pixels de l'image originale dans un tableau
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				pixels[x][y] = originalImage.getRGB(x, y) & 0xFF;
			}
		}

		// Algorithme de diffusion d'erreur de Floyd-Steinberg
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				int oldPixel = pixels[x][y];

				//int white = 0xFFFFFF;
				int black = 0;
				int white = 255;

				int newPixel = oldPixel >= 128 ? white : black; // Convertir en noir et blanc
				ditheredImage.setRGB(x, y, new Color(newPixel, newPixel, newPixel).getRGB());

				int error = oldPixel - newPixel;
				if (x + 1 < width)
					distributeError(pixels, x + 1, y, 7.0 / 16, error);
				if (x - 1 >= 0 && y + 1 < height)
					distributeError(pixels, x - 1, y + 1, 3.0 / 16, error);
				if (y + 1 < height)
					distributeError(pixels, x, y + 1, 5.0 / 16, error);
				if (x + 1 < width && y + 1 < height)
					distributeError(pixels, x + 1, y + 1, 1.0 / 16, error);
			}
		}

		return ditheredImage;
	}

	private static void distributeError(int[][] pixels, int x, int y, double coefficient, int error) {
		int oldPixel = pixels[x][y];
		int newPixel = (int) (oldPixel + error * coefficient);
		pixels[x][y] = newPixel;
	}

	// Exemple d'utilisation
	public static void main(String[] args) {
		if(args.length == 1) {
			String path = args[0];
			File input_file = new File(path);
			try {

				BufferedImage image = ImageIO.read(input_file);

				BufferedImage ditheredImage = dither(image);
				File reduced = new File("soutenance/output/floyd-gris.png");
				ImageIO.write(ditheredImage, "png", reduced);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}

