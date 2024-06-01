import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.lang.reflect.Array;

import javax.imageio.ImageIO;

import java.util.Arrays;
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
				for (int x = 0; x < width; x++) {
					for (int y = 0; y < height; y++) {
						tot += image.getRGB(width * i + x, height * j + y) & 0xFF;
					}
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

	public static int[][] matrix_product(int[][] m1, int[][] m2) {
		int res[][] = new int[m1.length][m2[0].length];
		for (int i = 0; i < m1.length; i++) {
			for (int j = 0; j < m2[0].length; j++) {
				for (int k = 0; k < m1[0].length; k++) {
					res[i][j] += m1[i][k] * m2[k][j];
				}
			}
		}
		return res;
	}

	public static int[][] matrix_real_product(int[][] m, int scalar) {
		int res[][] = new int[m.length][m[0].length];
		for (int i = 0; i < m.length; i++) {
			for (int j = 0; j < m[0].length; j++) {
				res[i][j] = m[i][j] * scalar;
			}
		}
		return res;
	}

	public static int[][] matrix_sum(int[][] m1, int[][] m2) {
		int res[][] = new int[m1.length][m1[0].length];
		for (int i = 0; i < m1.length; i++) {
			for (int j = 0; j < m1[0].length; j++) {
				res[i][j] = m1[i][j] + m2[i][j];
			}
		}
		return res;
	}

	public static int[][] only_one(int size) {
		int res[][] = new int[size][size];
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				res[i][j] = 1;
			}
		}
		return res;
	}

	public static void set_block(int[][]origin, int[][]block, int x, int y) {
		for (int i = 0; i < block.length; i++) {
			for (int j = 0; j < block[0].length; j++) {
				origin[y + i][x + j] = block[i][j];
			}
		}
	}

	static int d_2[][] = {
		{0, 2},
		{3, 1}
	};

	public static int[][] gen_disp_matrix2(int size) {
		int res[][] = new int[size][size];
		if(size == 2) {
			return d_2;
		} else {
			int k = (int) Math.sqrt(size);
			assert k * k == size;
			int n = size / 2;

			int one_n[][] = only_one(n);
			int d_n[][] = gen_disp_matrix2(n);

			int b1[][] = matrix_sum(matrix_real_product(d_n, 4), matrix_real_product(one_n, d_2[0][0]));
			int b2[][] = matrix_sum(matrix_real_product(d_n, 4), matrix_real_product(one_n, d_2[0][1]));
			int b3[][] = matrix_sum(matrix_real_product(d_n, 4), matrix_real_product(one_n, d_2[1][0]));
			int b4[][] = matrix_sum(matrix_real_product(d_n, 4), matrix_real_product(one_n, d_2[1][1]));

			set_block(res, b1, 0, 0);
			set_block(res, b2, n, 0);
			set_block(res, b3, 0, n);
			set_block(res, b4, n, n);

			return res;
		}
	}

	public static int[] gen_disp_matrix(int size) {
		int res[][] = gen_disp_matrix2(size);
		return flatten(res);
	}

	public static void main(String[] args) {
		Trame ord_disp = new Trame(4, 4, gen_disp_matrix(4));
		Trame ord_centered = new Trame(4, 4, gen_centered_matrix(4, 2, 2, true));
		Trame random = new Trame(4, 4);

		if(args.length > 0) {
			String path = args[0];
			File input_file = new File(path);
			try {
				BufferedImage image = ImageIO.read(input_file);

				BufferedImage ord_disp_reduce = ord_disp.traming(image);
				BufferedImage ord_centered_reduce = ord_centered.traming(image);
				BufferedImage random_reduce = random.randomTraming(image);

				File reduced = new File("soutenance/output/trame-ord-disp.png");
				File reduced2 = new File("soutenance/output/trame-ord-centered.png");
				File reduced3 = new File("soutenance/output/trame-random.png");

				ImageIO.write(ord_disp_reduce, "png", reduced);
				ImageIO.write(ord_centered_reduce, "png", reduced2);
				ImageIO.write(random_reduce, "png", reduced3);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
