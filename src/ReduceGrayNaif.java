
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;


public class ReduceGrayNaif {

	public static void main (String[] args) {
		BufferedImage image;
		String path = "image.png";
		File input_file = new File(path);
		try {
			image = ImageIO.read(input_file);

			int white = 0xFFFFFF;
			int black = 0;

			int width = image.getWidth();
			int height = image.getHeight();

			for(int i = 0; i < width; i++)
				for(int j = 0; j < height; j++) {
					int color = (image.getRGB(i, j) & 0xFF) < (256 / 2) ? black : white;
					image.setRGB(i, j, color);
				}

			File reduced = new File("reduced-image.png");
			ImageIO.write(image, "png", reduced);

		} catch (Exception e) {
			System.out.println("Problème lors de la lecture du fichier " + input_file.getAbsolutePath());
			e.printStackTrace();
		}

	}
}
