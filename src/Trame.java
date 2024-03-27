import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.File;

import javax.imageio.ImageIO;

import java.util.Random;

public class Trame {
	public int width;
	public int height;
	public int matrix[];
	public int patterns[][];

	public Trame(int w, int h, int m[]) {
		width = w;
		height = h;
		matrix = m;
		patterns = genPatterns();
	}

	public Trame(int w, int h) {
		width = w;
		height = h;

	}

	int[][] genRandomPatterns() {
		Random r = new Random();
		int ret[][] = new int[width * height][width * height];
		matrix = gen_centered_matrix(width, r.nextInt(width), r.nextInt(width), r.nextBoolean());
		int w = 0xFFFFFF;
		int b = 0;
		for (int i = 0; i < width * height; i++) {
			for (int j = 0; j < width * height; j++) {
				ret[i][j] = matrix[j] > i ? b : w;
			}
		}

		return ret;
	}

	int[][] genPatterns() {
		int ret[][] = new int[width * height][width * height];

		int w = 0xFFFFFF;
		int b = 0;
		for (int i = 0; i < width * height; i++) {
			for (int j = 0; j < width * height; j++) {
				ret[i][j] = matrix[j] > i ? b : w;
			}
		}

		return ret;
	}

	void printPattern() {
		for(int i = 0; i < patterns.length; ++i) {
			for(int j = 0; j < width * height; ++j) {
				System.out.print(patterns[i][j] == 0 ? "b " : "w ");
				if((j + 1) % width == 0) {
					System.out.print("\n");
				}
			}
			System.out.println("---------------------------------------------------\n");
		}
	}

	BufferedImage randomTraming(BufferedImage src) {
		ColorModel cm = src.getColorModel();
		boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
		WritableRaster raster = src.copyData(null);
		BufferedImage image = new BufferedImage(cm, raster, isAlphaPremultiplied, null);

		int t_x = image.getWidth() / width;
		int t_y = image.getHeight() / height;

		//printPattern();

		for (int i = 0; i < t_x; i++)
			for (int j = 0; j < t_y; j++) {
				int tot = 0;
				patterns = genRandomPatterns();
				for (int x = 0; x < width; x++)
					for (int y = 0; y < height; y++) {
						tot += image.getRGB(width * i + x, height * j + y) & 0xFF;
					}
				image.setRGB(width * i, height * j, width, height, patterns[tot / (height * width * width * height)], 0, width);
			}

		return image;
	}


	BufferedImage traming(BufferedImage src) {
		ColorModel cm = src.getColorModel();
		boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
		WritableRaster raster = src.copyData(null);
		BufferedImage image = new BufferedImage(cm, raster, isAlphaPremultiplied, null);

		int t_x = image.getWidth() / width;
		int t_y = image.getHeight() / height;

		//printPattern();

		for (int i = 0; i < t_x; i++)
			for (int j = 0; j < t_y; j++) {
				int tot = 0;
				for (int x = 0; x < width; x++)
					for (int y = 0; y < height; y++) {
						tot += image.getRGB(width * i + x, height * j + y) & 0xFF;
					}
				image.setRGB(width * i, height * j, width, height, patterns[tot / (height * width * width * height)], 0, width);
			}

		return image;
	}

	public static int[] gen_matrix(int centre) {
		// centre correspond à l'indice du centre
		int m[] = {
			15, 4, 8, 12,
			11, 0, 1, 5,
			7, 2, 3, 9,
			14, 10, 6, 13
		};
		int res[] = new int[16];
		int decalage = (centre - 5 + 16) % 16;
		for (int i = 0; i < 16; i++) {
			res[(i + decalage) % 16] = m[i];
		}
		System.out.print("res : ");
		for (int i = 0; i < 16; i++) {
			System.out.print(res[i] + " ");
		}
		System.out.println(";");
		return res;
	}

	static enum Phase {
		LEFT, RIGHT, UP, DOWN
	}

	public static void p_arr2(int[][] t) {
		for (int y = 0; y < t.length; y++) {
			for (int x = 0; x < t[y].length; x++) {
				System.out.print(t[y][x] + "	");
			}
			System.out.println("\n");
		}
	}

	public static int[] flatten(int[][] array2D) {
        int totalLength = 0;
        for (int[] row : array2D) {
            totalLength += row.length;
        }

        int[] flattenedArray = new int[totalLength];
        int index = 0;
        for (int[] row : array2D) {
            for (int num : row) {
                flattenedArray[index++] = num;
            }
        }

        return flattenedArray;
    }

	public static int[] gen_centered_matrix(int size, int center_x, int center_y, boolean black) {
		// true si centré blanc 
		// false si centré noir
		assert size > 0;
		assert center_x >= 0;
		assert center_x < size;
		assert center_y >= 0;
		assert center_y < size;

		int result[][] = new int[size][size];
		for (int i = 0; i < size; i++)
			for (int j = 0; j < size; j++) {
				result[i][j] = -1;
			}

		int l = center_x;
		int r = center_x;
		int u = center_y;
		int d = center_y;

		Phase p = Phase.DOWN;

		int x = center_x;
		int y = center_y;

		int count = 1;
		result[y][x] = black ? 0 : size * size - 1;
		while (l >= 0 || r <= size - 1 || u >= 0 || d <= size - 1) {
			switch (p) {
				case LEFT:
					x--;
					if (x == l - 1) {
						p = Phase.DOWN;
						l--;
					}
					break;
				case RIGHT:
					x++;
					if (x == r + 1) {
						p = Phase.UP;
						r++;
					}
					break;
				case UP:
					y--;
					if (y == u - 1) {
						p = Phase.LEFT;
						u--;
					}
					break;
				case DOWN:
					y++;
					if (y == d + 1) {
						p = Phase.RIGHT;
						d++;
					}
					break;
				default:
					break;
			}
			if (x >= 0 && x < size && y >= 0 && y < size) {
				if (result[y][x] == -1) {
					result[y][x] = black ? count : size * size - 1 - count;
					count++;
				}
			}
		}
		//p_arr2(result);
		return flatten(result);
	}

	public static void main(String[] args) {
		int m[] = gen_centered_matrix(4, 2, 2, true);

		Trame t = new Trame(4, 4, m);
		for (int i = 1; i < 8; i++) {
			String path = "images/image" + i + ".png";
			File input_file = new File(path);
			try {
				BufferedImage image = ImageIO.read(input_file);
				BufferedImage n = t.randomTraming(image);

				File reduced = new File("output/trame/gen-trame" + i + ".png");
				ImageIO.write(n, "png", reduced);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
