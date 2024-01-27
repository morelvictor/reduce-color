
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;

public class ReduceGrayTrame {

	public static void main (String[] args) {
			
BufferedImage image;
		String path = "image.png";
		File input_file = new File(path);
		try {
			image = ImageIO.read(input_file);

			int w = 0xFFFFFF;
			int b = 0;

	/*
	 * Liste des patterns
	 */

			int z[] =
				{
					b, b, b, b,
					b, b, b, b,
					b, b, b, b,
					b, b, b, b
				};
			int u[] =
				{
					b, b, b, b,
					b, w, b, b,
					b, b, b, b,
					b, b, b, b
				};
			int d[] =
				{
					b, b, b, b,
					b, w, w, b,
					b, b, b, b,
					b, b, b, b
				};
			int t[] =
				{
					b, b, b, b,
					b, w, w, b,
					b, w, b, b,
					b, b, b, b
				};
			int q[] =
				{
					b, b, b, b,
					b, w, w, b,
					b, w, w, b,
					b, b, b, b
				};
			int c[] =
				{
					b, w, b, b,
					b, w, w, b,
					b, w, w, b,
					b, b, b, b
				};
			int s[] =
				{
					b, w, b, b,
					b, w, w, w,
					b, w, w, b,
					b, b, b, b
				};
			int se[] =
				{
					b, w, b, b,
					b, w, w, w,
					b, w, w, b,
					b, b, w, b
				};
			int h[] =
				{
					b, w, b, b,
					b, w, w, w,
					w, w, w, b,
					b, b, w, b
				};
			int n[] =
				{
					b, w, w, b,
					b, w, w, w,
					w, w, w, b,
					b, b, w, b
				};
			int di[] =
				{
					b, w, w, b,
					b, w, w, w,
					w, w, w, w,
					b, b, w, b
				};
			int o[] =
				{
					b, w, w, b,
					b, w, w, w,
					w, w, w, w,
					b, w, w, b
				};
			int dou[] =
				{
					b, w, w, b,
					w, w, w, w,
					w, w, w, w,
					b, w, w, b
				};
			int tr[] =
				{
					b, w, w, w,
					w, w, w, w,
					w, w, w, w,
					b, w, w, b
				};
			int qu[] =
				{
					b, w, w, w,
					w, w, w, w,
					w, w, w, w,
					b, w, w, w
				};
			int qui[] =
				{
					b, w, w, w,
					w, w, w, w,
					w, w, w, w,
					w, w, w, w
				};
			int sei[] =
				{
					w, w, w, w,
					w, w, w, w,
					w, w, w, w,
					w, w, w, w
				};

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
					int chosen[];
						switch (tot / 16 / 16) {
							case 0: chosen = z; break;
							case 1: chosen = u; break;
							case 2: chosen = d; break;
							case 3: chosen = t; break;
							case 4: chosen = q; break;
							case 5: chosen = c; break;
							case 6: chosen = s; break;
							case 7: chosen = se; break;
							case 8: chosen = h; break;
							case 9: chosen = n; break;
							case 10: chosen = di; break;
							case 11: chosen = o; break;
							case 12: chosen = dou; break;
							case 13: chosen = tr; break;
							case 14: chosen = qu; break;
							case 15: chosen = qui; break;
							case 16: chosen = sei; break;
							default: chosen = z;
						}
						image.setRGB(4 * i, 4 * j, 4, 4, chosen, 0, 4);
				}

			File reduced = new File("reduced-image.png");
			ImageIO.write(image, "png", reduced);

		} catch (Exception e) {
			System.out.println("Problème lors de la lecture du fichier " + input_file.getAbsolutePath());
			e.printStackTrace();
		}

	}
}
