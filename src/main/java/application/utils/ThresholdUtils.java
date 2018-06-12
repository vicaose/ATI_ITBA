package application.utils;

import static domain.Image.ChannelType.BLUE;
import static domain.Image.ChannelType.GREEN;
import static domain.Image.ChannelType.RED;
import domain.Image;
import domain.Image.ChannelType;
import domain.Image.ImageType;

public class ThresholdUtils {

	public static Image threshold(Image original, int value) {
		if (original == null) {
			return null;
		}
		Image thresholded = (Image) original.clone();
		threshold(thresholded, value, RED);
		threshold(thresholded, value, GREEN);
		threshold(thresholded, value, BLUE);
		return thresholded;
	}

	public static void threshold(Image image, int value, ChannelType c) {
		for (int x = 0; x < image.getWidth(); x++) {
			for (int y = 0; y < image.getHeight(); y++) {
				image.setPixel(x, y, c,
						image.getPixel(x, y, c) > value ? Image.MAX_VAL : 0);
			}
		}
	}

	public static Image global(Image image, int T, int delta) {
		int globalThreshold = getGlobalThresholdValue(T, image, delta, RED);
		return threshold(image, globalThreshold);
	}

	public static void global(Image image, ChannelType c, int T, int delta) {
		int globalThreshold = getGlobalThresholdValue(T, image, delta, c);
		threshold(image, globalThreshold, c);
	}

	static int getGlobalThresholdValue(int T, Image img, int delta,
			ChannelType c) {
		int currentT = T;
		int previousT = 0;
		int i = 0;
		do {
			previousT = currentT;
			currentT = getAdjustedThreshold(currentT, img, c);
			i++;
		} while (Math.abs((currentT - previousT)) >= delta);
		// currentT -= 30;
		System.out.println(c);
		System.out.println("Iteraciones: " + i);
		System.out.println("T: " + currentT);
		return currentT;
	}

	private static int getAdjustedThreshold(int previousThreshold, Image img,
			ChannelType c) {
		int amountOfHigher = 0;
		int amountOfLower = 0;
		double sumOfHigher = 0;
		double sumOfLower = 0;

		for (int x = 0; x < img.getWidth(); x++) {
			for (int y = 0; y < img.getHeight(); y++) {
				double aPixel = img.getPixel(x, y, c);
				if (aPixel >= previousThreshold) {
					amountOfHigher++;
					sumOfHigher += (int) aPixel;
				} else {
					amountOfLower++;
					sumOfLower += (int) aPixel;
				}
			}
		}
		double m1 = sumOfHigher / amountOfHigher;
		double m2 = sumOfLower / amountOfLower;
		return (int) ((m1 + m2) / 2);
	}

	public static Image otsu(Image img) {
		Image thresholded = (Image) img.clone();
		otsu(thresholded, RED);
		otsu(thresholded, GREEN);
		otsu(thresholded, BLUE);
		return thresholded;
	}

	public static void otsu(Image img, ChannelType c) {
		double maxSigma = 0;
		int threshold = 0;
		double[] probabilities = getProbabilitiesOfColorLevel(img, c);
		for (int i = 0; i < Image.MAX_VAL; i++) {
			double aSigma = getSigma(i, probabilities);
			if (aSigma > maxSigma) {
				maxSigma = aSigma;
				threshold = i;
			}
		}
		// System.out.println("Threshold " + c.toString() + ": " + threshold);
		threshold(img, threshold, c);
	}

	private static double getSigma(int threshold, double[] probabilities) {
		double w1 = 0;
		double w2 = 0;
		for (int i = 0; i < probabilities.length; i++) {
			if (i <= threshold) {
				w1 += probabilities[i];
			} else {
				w2 += probabilities[i];
			}
		}
		if (w1 == 0 || w2 == 0) {
			return 0;
		}
		double mu1 = 0;
		double mu2 = 0;
		for (int i = 0; i < probabilities.length; i++) {
			if (i <= threshold) {
				mu1 += i * probabilities[i] / w1;
			} else {
				mu2 += i * probabilities[i] / w2;
			}
		}
		double mu_t = mu1 * w1 + mu2 * w2;
		return w1 * Math.pow((mu1 - mu_t), 2) + w2 * Math.pow((mu2 - mu_t), 2);
	}

	private static double[] getProbabilitiesOfColorLevel(Image img,
			ChannelType c) {
		double[] probabilities = new double[Image.MAX_VAL + 1];

		for (int x = 0; x < img.getWidth(); x++) {
			for (int y = 0; y < img.getHeight(); y++) {
				int aColorPixel = (int) img.getPixel(x, y, c);
				probabilities[aColorPixel]++;
			}
		}
		int size = (img.getWidth() * img.getHeight());
		for (int i = 0; i < probabilities.length; i++) {
			probabilities[i] /= size;
		}
		return probabilities;
	}

	public static Image hysteresis(Image image, double t1, double t2) {
		Image t = (Image) image.clone();
		if (image.getType() == ImageType.GREYSCALE) {
			applyHysteresis(t, t1, t2, null);
		} else {
			applyHysteresis(t, t1, t2, RED);
			applyHysteresis(t, t1, t2, GREEN);
			applyHysteresis(t, t1, t2, BLUE);
		}
		return t;
	}

	public static void hysteresisForCanny(Image image, ChannelType c,
			double t1, double t2) {
		applyHysteresis(image, t1, t2, c);
		checkHysteresisNeighbors(image, t1, t2);
	}

	private static void checkHysteresisNeighbors(Image image, double t1,
			double t2) {
		int height = image.getHeight();
		int width = image.getWidth();
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				for (ChannelType c : Image.rgbValues()) {
					double pixel = image.getPixel(x, y, c);
					if (pixel >= t1 && pixel <= t2) {
						boolean isBorderNeighbor1 = y > 0
								&& image.getPixel(x, y - 1, c) == Image.MAX_VAL;
						boolean isBorderNeighbor2 = x > 0
								&& image.getPixel(x - 1, y, c) == Image.MAX_VAL;
						boolean isBorderNeighbor3 = y < height - 1
								&& image.getPixel(x, y + 1, c) == Image.MAX_VAL;
						boolean isBorderNeighbor4 = x < width - 1
								&& image.getPixel(x + 1, y, c) == Image.MAX_VAL;
						if (isBorderNeighbor1 || isBorderNeighbor2
								|| isBorderNeighbor3 || isBorderNeighbor4) {
							image.setPixel(x, y, c, Image.MAX_VAL);
						} else {
							image.setPixel(x, y, c, 0);
						}
					}
				}
			}
		}
	}

	private static void applyHysteresis(Image image, double t1, double t2,
			ChannelType c) {
		ChannelType color = c == null ? RED : c;
		for (int x = 0; x < image.getWidth(); x++) {
			for (int y = 0; y < image.getHeight(); y++) {
				double pixel = image.getPixel(x, y, color);
				double val = pixel;
				if (pixel < t1) {
					val = 0;
				} else if (pixel > t2) {
					val = Image.MAX_VAL;
				}
				if (c == null) {
					image.setPixel(x, y, RED, val);
					image.setPixel(x, y, GREEN, val);
					image.setPixel(x, y, BLUE, val);
				} else {
					image.setPixel(x, y, c, val);
				}
			}
		}
	}

	public static void hysteresisForCanny(Image image) {
		for (ChannelType c : Image.rgbValues()) {
			double thresholdVal = ThresholdUtils.getGlobalThresholdValue(128,
					image, 1, c);
			hysteresisForCanny(image, c, thresholdVal - 15, thresholdVal + 15);
		}
	}
}
