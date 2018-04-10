package application.utils;

import static domain.Image.ChannelType.BLUE;
import static domain.Image.ChannelType.GREEN;
import static domain.Image.ChannelType.RED;
import domain.Image;
import domain.Image.ChannelType;

public class PunctualOperationsUtils {

	public static Image negative(Image original) {
		if (original == null) {
			return null;
		}
		Image inverse = original.shallowClone();
		for (int x = 0; x < original.getWidth(); x++) {
			for (int y = 0; y < original.getHeight(); y++) {
				double red = original.getPixel(x, y, RED);
				double green = original.getPixel(x, y, GREEN);
				double blue = original.getPixel(x, y, BLUE);
				inverse.setPixel(x, y, RED, Image.MAX_VAL - red);
				inverse.setPixel(x, y, GREEN, Image.MAX_VAL - green);
				inverse.setPixel(x, y, BLUE, Image.MAX_VAL - blue);
			}
		}
		return inverse;
	}

	public static Image add(Image img1, Image img2) {
		if (img1 == null || img2 == null) {
			return null;
		}
		Image img = img1.shallowClone();
		double min = 0, max = 255;
		for (int x = 0; x < img1.getWidth(); x++) {
			for (int y = 0; y < img1.getHeight(); y++) {
				double red = img1.getPixel(x, y, RED)
						+ img2.getPixel(x, y, RED);
				double green = img1.getPixel(x, y, GREEN)
						+ img2.getPixel(x, y, GREEN);
				double blue = img1.getPixel(x, y, BLUE)
						+ img2.getPixel(x, y, BLUE);
				min = min(red, green, blue, min);
				max = max(red, green, blue, max);
				img.setPixel(x, y, RED, red);
				img.setPixel(x, y, GREEN, green);
				img.setPixel(x, y, BLUE, blue);
			}
		}
		normalize(img, min, max);
		return img;
	}

	public static Image substract(Image img1, Image img2) {
		if (img1 == null || img2 == null) {
			return null;
		}
		Image img = img1.shallowClone();
		double min = 0, max = 255;
		for (int x = 0; x < img1.getWidth(); x++) {
			for (int y = 0; y < img1.getHeight(); y++) {
				double red = img1.getPixel(x, y, RED)
						- img2.getPixel(x, y, RED);
				double green = img1.getPixel(x, y, GREEN)
						- img2.getPixel(x, y, GREEN);
				double blue = img1.getPixel(x, y, BLUE)
						- img2.getPixel(x, y, BLUE);
				min = min(red, green, blue, min);
				max = max(red, green, blue, max);
				img.setPixel(x, y, RED, red);
				img.setPixel(x, y, GREEN, green);
				img.setPixel(x, y, BLUE, blue);
			}
		}
		normalize(img, min, max);
		return img;
	}

	public static Image multiply(Image img1, Image img2) {
		if (img1 == null || img2 == null) {
			return null;
		}
		Image img = img1.shallowClone();
		double min = 0, max = 255;
		for (int x = 0; x < img1.getWidth(); x++) {
			for (int y = 0; y < img1.getHeight(); y++) {
				double red = img1.getPixel(x, y, RED)
						* img2.getPixel(x, y, RED);
				double green = img1.getPixel(x, y, GREEN)
						* img2.getPixel(x, y, GREEN);
				double blue = img1.getPixel(x, y, BLUE)
						* img2.getPixel(x, y, BLUE);
				min = min(red, green, blue, min);
				max = max(red, green, blue, max);
				img.setPixel(x, y, RED, red);
				img.setPixel(x, y, GREEN, green);
				img.setPixel(x, y, BLUE, blue);
			}
		}
		normalize(img, min, max);
		return img;
	}

	public static Image multiply(Image original, double value) {
		if (original == null) {
			return null;
		}
		Image img = original.shallowClone();
		for (int x = 0; x < img.getWidth(); x++) {
			for (int y = 0; y < img.getHeight(); y++) {
				double red = original.getPixel(x, y, RED) * value;
				double green = original.getPixel(x, y, GREEN) * value;
				double blue = original.getPixel(x, y, BLUE) * value;
				img.setPixel(x, y, RED, red);
				img.setPixel(x, y, GREEN, green);
				img.setPixel(x, y, BLUE, blue);
			}
		}
		dynamicCompression(img);
		return img;
	}

	private static double min(double a, double b, double c, double d) {
		return Math.min(a, Math.min(b, Math.min(c, d)));
	}

	private static double max(double a, double b, double c, double d) {
		return Math.max(a, Math.max(b, Math.max(c, d)));
	}

	public static void normalize(Image image) {
		double max = 0, min = 0;
		for (int x = 0; x < image.getWidth(); x++) {
			for (int y = 0; y < image.getHeight(); y++) {
				max = Math.max(max, image.getPixel(x, y, RED));
				max = Math.max(max, image.getPixel(x, y, GREEN));
				max = Math.max(max, image.getPixel(x, y, BLUE));
				min = Math.min(min, image.getPixel(x, y, RED));
				min = Math.min(min, image.getPixel(x, y, GREEN));
				min = Math.min(min, image.getPixel(x, y, BLUE));

			}
		}
		normalize(image, min, max);
	}

	public static void normalize(Image image, double min, double max) {
		if (min == 0 && max == 255) {
			return;
		}
		double dif = max - min;
		for (int x = 0; x < image.getWidth(); x++) {
			for (int y = 0; y < image.getHeight(); y++) {
				double red = (image.getPixel(x, y, RED) - min) / dif;
				double green = (image.getPixel(x, y, GREEN) - min) / dif;
				double blue = (image.getPixel(x, y, BLUE) - min) / dif;
				image.setPixel(x, y, RED, toBase256(red));
				image.setPixel(x, y, GREEN, toBase256(green));
				image.setPixel(x, y, BLUE, toBase256(blue));
			}
		}
	}

	public static void truncate(Image image) {
		for (int x = 0; x < image.getWidth(); x++) {
			for (int y = 0; y < image.getHeight(); y++) {
				double red = image.getPixel(x, y, RED);
				double green = image.getPixel(x, y, GREEN);
				double blue = image.getPixel(x, y, BLUE);
				if (red > Image.MAX_VAL) {
					red = Image.MAX_VAL;
				} else if (red < 0) {
					red = 0;
				}
				if (green > Image.MAX_VAL) {
					green = Image.MAX_VAL;
				} else if (green < 0) {
					green = 0;
				}
				if (blue > Image.MAX_VAL) {
					blue = Image.MAX_VAL;
				} else if (blue < 0) {
					blue = 0;
				}

				image.setPixel(x, y, RED, red);
				image.setPixel(x, y, GREEN, green);
				image.setPixel(x, y, BLUE, blue);
			}
		}
	}

	public static void round(Image image) {
		for (int x = 0; x < image.getWidth(); x++) {
			for (int y = 0; y < image.getHeight(); y++) {
				double red = image.getPixel(x, y, RED);
				double green = image.getPixel(x, y, GREEN);
				double blue = image.getPixel(x, y, BLUE);
				if (red > Image.MAX_VAL || red < 0) {
					red = red % Image.MAX_VAL;
				}
				if (green > Image.MAX_VAL || green < 0) {
					green = green % Image.MAX_VAL;
				}
				if (blue > Image.MAX_VAL || blue < 0) {
					blue = blue % Image.MAX_VAL;
				}

				image.setPixel(x, y, RED, red);
				image.setPixel(x, y, GREEN, green);
				image.setPixel(x, y, BLUE, blue);
			}
		}
	}

	private static double toBase256(double val) {
		return Math.floor(val * 255 + 0.5);
	}

	public static Image dynamicCompression(Image original) {
		if (original == null) {
			return null;
		}
		double L = Image.MAX_VAL;
		double RRed = 0, RGreen = 0, RBlue = 0;
		for (int x = 0; x < original.getWidth(); x++) {
			for (int y = 0; y < original.getHeight(); y++) {
				RRed = Math.max(RRed, original.getPixel(x, y, RED));
				RGreen = Math.max(RGreen, original.getPixel(x, y, GREEN));
				RBlue = Math.max(RBlue, original.getPixel(x, y, BLUE));
			}
		}
		double cRed = (L - 1) / Math.log(1 + RRed);
		double cGreen = (L - 1) / Math.log(1 + RGreen);
		double cBlue = (L - 1) / Math.log(1 + RBlue);
		Image image = original.shallowClone();
		for (int x = 0; x < image.getWidth(); x++) {
			for (int y = 0; y < image.getHeight(); y++) {
				double rRed = original.getPixel(x, y, RED);
				double rGreen = original.getPixel(x, y, GREEN);
				double rBlue = original.getPixel(x, y, BLUE);
				double colorRed = (double) (cRed * Math.log(1 + rRed));
				double colorGreen = (double) (cGreen * Math.log(1 + rGreen));
				double colorBlue = (double) (cBlue * Math.log(1 + rBlue));
				image.setPixel(x, y, RED, colorRed);
				image.setPixel(x, y, GREEN, colorGreen);
				image.setPixel(x, y, BLUE, colorBlue);
			}
		}
		return image;
	}

	public static Image contrast(Image original, int r1, int r2, int s1, int s2) {
		if (original == null) {
			return null;
		}
		Image img = original.shallowClone();
		for (int x = 0; x < img.getWidth(); x++) {
			for (int y = 0; y < img.getHeight(); y++) {
				double red = contrastValue(original.getPixel(x, y, RED), r1,
						r2, s1, s2);
				double green = contrastValue(original.getPixel(x, y, GREEN),
						r1, r2, s1, s2);
				double blue = contrastValue(original.getPixel(x, y, BLUE), r1,
						r2, s1, s2);
				img.setPixel(x, y, RED, red);
				img.setPixel(x, y, GREEN, green);
				img.setPixel(x, y, BLUE, blue);
			}
		}
		return img;
	}

	private static double contrastValue(double pixel, int r1, int r2, int s1,
			int s2) {
		double m = 0;
		double b = 0;
		if (pixel < r1) {
			m = s1 / r1;
			b = 0;
		} else if (pixel > r2) {
			m = (Image.MAX_VAL - s2) / (Image.MAX_VAL - r2);
			b = s2 - m * r2;
		} else {
			m = (s2 - s1) / (r2 - r1);
			b = s1 - m * r1;
		}
		return m * pixel + b;
	}

	public static Image ecualizate(Image original) {
		Image image = original.shallowClone();
		equalizeChannel(original, image, RED);
		equalizeChannel(original, image, GREEN);
		equalizeChannel(original, image, BLUE);
		return image;
	}

	public static void equalizeChannel(Image original, Image image,
			ChannelType color) {
		int[] ocurrences = getColorOccurrences(original, color);
		int totalPixels = image.getWidth() * image.getHeight();
		double[] levels = new double[totalPixels];
		double s_min = 0;
		double s_max = Image.MAX_VAL;
		for (int i = 0; i < levels.length; i++) {
			int level = (int) Math.floor(original.getPixel(
					i / image.getHeight(), i % image.getWidth(), color));
			double levelVal = 0;
			for (int k = 0; k < level; k++) {
				levelVal += ocurrences[k];
			}
			levels[i] = levelVal / totalPixels;
			s_min = Math.min(s_min, levels[i]);
			s_max = Math.max(s_max, levels[i]);

		}
		for (int i = 0; i < levels.length; i++) {
			double aux = Image.MAX_VAL * (levels[i] - s_min) / (1 - s_min);
			levels[i] = Math.ceil(aux);
		}
		int i = 0;
		for (int x = 0; x < image.getWidth(); x++) {
			for (int y = 0; y < image.getHeight(); y++) {
				image.setPixel(x, y, color, levels[i++]);
			}
		}
	}

	private static int[] getColorOccurrences(Image image, ChannelType color) {
		int[] dataset = new int[Image.MAX_VAL + 1];
		for (int x = 0; x < image.getWidth(); x++) {
			for (int y = 0; y < image.getHeight(); y++) {
				dataset[(int) Math.floor(image.getPixel(x, y, color))]++;
			}
		}
		return dataset;
	}

	public static void maximize(Image image) {
		for (int x = 0; x < image.getWidth(); x++) {
			for (int y = 0; y < image.getHeight(); y++) {
				double val = Math.max(
						image.getPixel(x, y, RED),
						Math.max(image.getPixel(x, y, GREEN),
								image.getPixel(x, y, BLUE)));
				image.setPixel(x, y, RED, val);
				image.setPixel(x, y, GREEN, val);
				image.setPixel(x, y, BLUE, val);
			}
		}
	}

	public static Image toGreyscale(Image original) {
		Image image = original.shallowClone();
		for (int x = 0; x < image.getWidth(); x++) {
			for (int y = 0; y < image.getHeight(); y++) {
				double r = original.getPixel(x, y, RED);
				double g = original.getPixel(x, y, GREEN);
				double b = original.getPixel(x, y, BLUE);
				double grey = Math.min(255, Math.sqrt(r * r + g * g + b * b));
				image.setPixel(x, y, RED, grey);
				image.setPixel(x, y, GREEN, grey);
				image.setPixel(x, y, BLUE, grey);
			}
		}
		return image;
	}

}
