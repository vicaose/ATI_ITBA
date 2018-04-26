package application.utils;

import static domain.Image.ChannelType.BLUE;
import static domain.Image.ChannelType.GREEN;
import static domain.Image.ChannelType.RED;
import domain.Image;
import domain.Image.ChannelType;

public class NoiseUtils {

	public static Image saltAndPepper(Image original, double minP, double maxP) {
		if (original == null)
			return null;
		Image saltAndPepper = original.shallowClone();
		for (int x = 0; x < original.getWidth(); x++) {
			for (int y = 0; y < original.getHeight(); y++) {
				double p = Math.random();
				setPixelSaltAndPepper(original, saltAndPepper, minP, maxP, p,
						RED, x, y);
				setPixelSaltAndPepper(original, saltAndPepper, minP, maxP, p,
						GREEN, x, y);
				setPixelSaltAndPepper(original, saltAndPepper, minP, maxP, p,
						BLUE, x, y);
			}
		}
		return saltAndPepper;
	}

	private static void setPixelSaltAndPepper(Image original,
			Image saltAndPepper, double minP, double maxP, double p,
			ChannelType color, int x, int y) {
		double pix;
		if (p < minP) {
			pix = 0;
		} else if (p > maxP) {
			pix = Image.MAX_VAL;
		} else {
			pix = original.getPixel(x, y, color);
		}
		saltAndPepper.setPixel(x, y, color, pix);
	}

	public static Image gaussianNoise(Image original, double avg, double dev,
			double p) {
		if (original == null)
			return null;
		Image gaussian = original.shallowClone();
		for (int x = 0; x < original.getWidth(); x++) {
			for (int y = 0; y < original.getHeight(); y++) {
				double rand = Math.random();
				double noise = 0;
				if (rand < p) {
					noise = gaussianGenerator(avg, dev);
				}
				gaussian.setPixel(x, y, RED, original.getPixel(x, y, RED)
						+ noise);
				gaussian.setPixel(x, y, GREEN, original.getPixel(x, y, GREEN)
						+ noise);
				gaussian.setPixel(x, y, BLUE, original.getPixel(x, y, BLUE)
						+ noise);
			}
		}
		return gaussian;
	}

	private static double gaussianGenerator(double avg, double dev) {
		return avg + dev * Math.sqrt(-2 * Math.log(Math.random()))
				* Math.cos(2 * Math.PI * Math.random());
	}

	public static Image exponentialNoise(Image original, double lambda, double p) {
		if (original == null)
			return null;
		Image expo = original.shallowClone();
		for (int x = 0; x < original.getWidth(); x++) {
			for (int y = 0; y < original.getHeight(); y++) {
				double rand = Math.random();
				double noise = 1;
				if (rand < p) {
					noise = expGenerator(lambda);
				}
				expo.setPixel(x, y, RED, original.getPixel(x, y, RED) * noise);
				expo.setPixel(x, y, GREEN, original.getPixel(x, y, GREEN)
						* noise);
				expo.setPixel(x, y, BLUE, original.getPixel(x, y, BLUE) * noise);
			}
		}
		return expo;
	}

	private static double expGenerator(double lambda) {
		return -Math.log(Math.random()) / lambda;
	}

	public static Image rayleighNoise(Image original, double psi, double p) {
		if (original == null)
			return null;
		Image rayleigh = original.shallowClone();
		for (int x = 0; x < original.getWidth(); x++) {
			for (int y = 0; y < original.getHeight(); y++) {
				double rand = Math.random();
				double noise = 1;
				if (rand < p) {
					noise = rayleighGenerator(psi);
				}
				rayleigh.setPixel(x, y, RED, original.getPixel(x, y, RED)
						* noise);
				rayleigh.setPixel(x, y, GREEN, original.getPixel(x, y, GREEN)
						* noise);
				rayleigh.setPixel(x, y, BLUE, original.getPixel(x, y, BLUE)
						* noise);
			}
		}
		return rayleigh;
	}

	private static double rayleighGenerator(double psi) {
		return psi * Math.sqrt(-Math.log(1 - Math.random()));
	}
}
