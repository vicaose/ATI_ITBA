package application.utils;

import static domain.Image.ChannelType.BLUE;
import static domain.Image.ChannelType.GREEN;
import static domain.Image.ChannelType.RED;

import java.awt.Color;

import domain.Image;
import domain.RgbImage;
import domain.Image.ChannelType;

public class BasicImageUtils {

	public static Image createSquareImage(int height, int width) {
		Image binaryImage = new RgbImage(width, height);
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				// Analyzes if the point is in the black or white square
				boolean fitsInSquareByWidth = (x > width / 4 && x < 3 * (width / 4));
				boolean fitsInSquareByHeight = (y > height / 4 && y < 3 * (height / 4));
				Color colorToApply = (fitsInSquareByWidth && fitsInSquareByHeight) ? Color.WHITE
						: Color.BLACK;
				binaryImage.setPixel(x, y, colorToApply.getRGB());
			}
		}
		return binaryImage;
	}

	public static boolean invalidValue(int x) {
		return x < 0 || x > Image.MAX_VAL;
	}

	public static Image createWhiteImage(int height, int width) {
		Image whiteImage = new RgbImage(width, height);
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				whiteImage.setPixel(x, y, Color.WHITE.getRGB());
			}
		}
		return whiteImage;
	}

	public static Image createDegrade(boolean isColor, int height, int width,
			int color1, int color2) {
		Image degradee = null;
		if (isColor) {
			degradee = new RgbImage(width, height);
		} else {
			degradee = new RgbImage(width, height);
		}
		Color c1 = new Color(color1);
		Color c2 = new Color(color2);
		Color currentColor = new Color(color1);
		float redFactor = (float) (c2.getRed() - c1.getRed()) / height;
		float greenFactor = (float) (c2.getGreen() - c1.getGreen()) / height;
		float blueFactor = (float) (c2.getBlue() - c1.getBlue()) / height;
		float red = 0;
		float green = 0;
		float blue = 0;
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				degradee.setPixel(x, y, currentColor.getRGB());
			}
			red = red + redFactor;
			green = green + greenFactor;
			blue = blue + blueFactor;
			currentColor = new Color(c1.getRGB() + (int) ((int) red * 0x010000)
					+ (int) ((int) green * 0x000100)
					+ (int) ((int) blue * 0x000001));
		}
		return degradee;
	}

	public static Image createCircleImage(int height, int width) {
		Image binaryImage = new RgbImage(width, height);
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				double aTerm = Math.pow(x - width / 2, 2);
				double bTerm = Math.pow(y - height / 2, 2);
				double rTerm = Math.pow(height / 4, 2);
				Color colorToApply = (aTerm + bTerm) <= rTerm ? Color.WHITE
						: Color.BLACK;
				binaryImage.setPixel(x, y, colorToApply.getRGB());
			}
		}
		return binaryImage;
	}

	public static Image crop(int height, int width, int x, int y, Image original) {
		Image image = new RgbImage(width, height);
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < height; j++) {
				image.setPixel(i, j, RED, original.getPixel(i + x, j + y, RED));
				image.setPixel(i, j, GREEN,
						original.getPixel(i + x, j + y, GREEN));
				image.setPixel(i, j, BLUE,
						original.getPixel(i + x, j + y, BLUE));
			}
		}
		return image;
	}

	public static void paint(Image image, int x, int y, int r, int g, int b) {
		if (image.validPixel(x, y)) {
			image.setPixel(x, y, ChannelType.RED, r);
			image.setPixel(x, y, ChannelType.GREEN, g);
			image.setPixel(x, y, ChannelType.BLUE, b);
		}
	}

	public static void paintN8(Image image, int x, int y, int r, int g, int b) {
		for (int i = -1; i < 2; i++) {
			for (int j = -1; j < 2; j++) {
				paint(image, x + i, y + j, r, g, b);
			}
		}
	}

	public static void paintRed(Image image, int x, int y) {
		paint(image, x, y, 255, 0, 0);
	}

	public static void paintRedN8(Image image, int x, int y) {
		for (int i = -1; i < 2; i++) {
			for (int j = -1; j < 2; j++) {
				paintRed(image, x + i, y + j);
			}
		}
	}
}
