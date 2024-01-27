
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.util.Random;
import java.util.Set;
import java.util.HashSet;

public class ReduceGrayTrameIrrDisp {

	public static int [] genPattern(int nWhite) {
	//nWhite is the number of white pixels we need to place in res
		Random r = new Random();
		int w = 0xFFFFFF;
		int b = 0;
		int res[] =
				{
					b, b, b, b,
					b, b, b, b,
					b, b, b, b,
					b, b, b, b
				};
				
		Set<Integer> toWhite = new HashSet<>();
		while(toWhite.size() < nWhite) {
			toWhite.add(r.nextInt(16));
		}
		
		for(Integer index : toWhite) {
			res[index] = w;
		}
		
		return res;
	}

	public static void main (String[] args) {
			
BufferedImage image;
		String path = "images/image5.png";
		File input_file = new File(path);
		try {
			image = ImageIO.read(input_file);

			int w = 0xFFFFFF;
			int b = 0;

			int t_x = image.getWidth() / 4;
			int t_y = image.getHeight() / 4;

			for(int i = 0; i < t_x ; i++)
				for(int j = 0; j < t_y; j++) {
					int tot = 0;
					for(int x = 0; x < 4; x++)
					for(int y = 0; y < 4; y++) {
						tot += image.getRGB(4 * i + x, 4 * j + y) & 0xFF;
					}
					System.out.println(tot / 16 / 16);

						image.setRGB(4 * i, 4 * j, 4, 4, genPattern(tot / 16 / 16), 0, 4);
				}

			File reduced = new File("output/reduced-image-irr-disp.png");
			ImageIO.write(image, "png", reduced);

		} catch (Exception e) {
			System.out.println("Problème lors de la lecture du fichier " + input_file.getAbsolutePath());
			e.printStackTrace();
		}

	}
}
