package application.utils;

import static domain.Image.ChannelType.BLUE;
import static domain.Image.ChannelType.GREEN;
import static domain.Image.ChannelType.HUE;
import static domain.Image.ChannelType.RED;
import static domain.Image.ChannelType.SATURATION;
import static domain.Image.ChannelType.VALUE;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import domain.Image;
import domain.Image.ChannelType;
import domain.SynthetizationType;
import domain.mask.Mask;
import domain.tracking.Tita;

public class MaskUtils {

	public static double applySusanPixelMask(int x, int y, Mask mask,
			Image img, ChannelType color) {
		boolean ignoreByX = x < mask.getWidth() / 2
				|| x > img.getWidth() - mask.getWidth() / 2;
		boolean ignoreByY = y < mask.getHeight() / 2
				|| y > img.getHeight() - mask.getHeight() / 2;
		if (ignoreByX || ignoreByY) {
			return 1;
		}

		// Step 2.
		final int threshold = 27;
		int n_ro = 0;
		double ro = img.getPixel(x, y, color);
		for (int i = -mask.getWidth() / 2; i <= mask.getWidth() / 2; i++) {
			for (int j = -mask.getHeight() / 2; j <= mask.getHeight() / 2; j++) {
				if (img.validPixel(x + i, y + j) && mask.getValue(i, j) == 1) {
					double eachPixel = img.getPixel(x + i, y + j, color);
					if (Math.abs(ro - eachPixel) < threshold) {
						n_ro += 1;
					}
				}
			}
		}
		double s_ro = 1 - n_ro / 37.0;
		return s_ro;
	}

	public static double[][] applyMask(Tita tita, Set<Point> innerBorder, Set<Point> outerBorder, Mask mask) {
		double[][] conv = new double[tita.getWidth()][tita.getHeight()];
		for (Point p : innerBorder) {
			conv[p.x][p.y] = applyMask(tita, mask, p.x, p.y);
		}
		for (Point p : outerBorder) {
			conv[p.x][p.y] = applyMask(tita, mask, p.x, p.y);
		}
		return conv;
	}
	
	private static double applyMask(Tita tita, Mask mask, int x, int y) {
		double pix = 0;
		int w = tita.getWidth(), h = tita.getHeight();
		for (int i = -mask.getWidth() / 2; i <= mask.getWidth() / 2; i++) {
			for (int j = -mask.getHeight() / 2; j <= mask.getHeight() / 2; j++) {
				int a = getPoint(x, i, w);
				int b = getPoint(y, j, h);
				pix += mask.getValue(i, j) * tita.getValue(a, b);
			}
		}
		return pix;
	}
	
	public static Image applyMask(Image original, Mask mask) {
		if (original == null || mask == null) {
			return null;
		}
		Image image = original.shallowClone();
		if (original.isRgb()) {
			applyMask(original, image, mask, RED);
			applyMask(original, image, mask, GREEN);
			applyMask(original, image, mask, BLUE);
		} else if (original.isHsv()) {
			applyMask(original, image, mask, HUE);
			applyMask(original, image, mask, SATURATION);
			applyMask(original, image, mask, VALUE);
		}
		return image;
	}

	private static void applyMask(Image original, Image image, Mask mask,
			ChannelType color) {
		for (int x = 0; x < original.getWidth(); x++) {
			for (int y = 0; y < original.getHeight(); y++) {
				applyMask(original, image, mask, color, x, y);
			}
		}
	}

	private static void applyMask(Image original, Image image, Mask mask,
			ChannelType color, int x, int y) {
		double pix = 0;
		int w = image.getWidth(), h = image.getHeight();
		for (int i = -mask.getWidth() / 2; i <= mask.getWidth() / 2; i++) {
			for (int j = -mask.getHeight() / 2; j <= mask.getHeight() / 2; j++) {
				int a = getPoint(x, i, w);
				int b = getPoint(y, j, h);
				pix += mask.getValue(i, j) * original.getPixel(a, b, color);
			}
		}
		image.setPixel(x, y, color, pix);
	}

	private static int getPoint(int point, int eps, int max) {
		if (point + eps >= 0 && point + eps < max) {
			return point + eps;
		}
		if (eps > 0)
			return getPoint(point, eps - 1, max);
		return getPoint(point, eps + 1, max);
	}

	public static Image applyMedianMask(Image original, int maskWidth,
			int maskHeight) {
		if (original == null) {
			return null;
		}
		Image image = original.shallowClone();
		for (int x = 0; x < image.getWidth(); x++) {
			for (int y = 0; y < image.getHeight(); y++) {
				image.setPixel(
						x,
						y,
						RED,
						getMedianPixel(original, RED, x, y, maskWidth,
								maskHeight));
				image.setPixel(
						x,
						y,
						GREEN,
						getMedianPixel(original, GREEN, x, y, maskWidth,
								maskHeight));
				image.setPixel(
						x,
						y,
						BLUE,
						getMedianPixel(original, BLUE, x, y, maskWidth,
								maskHeight));
			}
		}
		return image;
	}

	private static double getMedianPixel(Image image, ChannelType color, int x,
			int y, int maskWidth, int maskHeight) {
		List<Double> pixelsAffected = new ArrayList<Double>();
		for (int i = -maskWidth / 2; i <= maskWidth / 2; i++) {
			for (int j = -maskHeight / 2; j <= maskHeight / 2; j++) {
				if (image.validPixel(x + i, y + j)) {
					pixelsAffected.add(image.getPixel(x + i, y + j, color));
				}
			}
		}
		Collections.sort(pixelsAffected, new Comparator<Double>() {
			public int compare(Double arg0, Double arg1) {
				return (int) (arg0 - arg1);
			}
		});
		double val;
		int size = pixelsAffected.size();
		if (size % 2 == 0) {
			val = ((double) (pixelsAffected.get(size / 2) + pixelsAffected
					.get(size / 2 + 1))) / 2;
		} else {
			val = pixelsAffected.get(size / 2);
		}
		return val;
	}

	public static Image applyMeanMask(Image original, int maskWidth,
			int maskHeight) {
		if (original == null) {
			return null;
		}
		Image image = original.shallowClone();
		for (int x = 0; x < image.getWidth(); x++) {
			for (int y = 0; y < image.getHeight(); y++) {
				image.setPixel(
						x,
						y,
						RED,
						getMeanPixel(original, RED, x, y, maskWidth, maskHeight));
				image.setPixel(
						x,
						y,
						GREEN,
						getMeanPixel(original, GREEN, x, y, maskWidth,
								maskHeight));
				image.setPixel(
						x,
						y,
						BLUE,
						getMeanPixel(original, BLUE, x, y, maskWidth,
								maskHeight));
			}
		}
		return image;
	}

	private static double getMeanPixel(Image image, ChannelType color, int x,
			int y, int maskWidth, int maskHeight) {
		List<Double> pixelsAffected = new ArrayList<Double>();
		for (int i = -maskWidth / 2; i <= maskWidth / 2; i++) {
			for (int j = -maskHeight / 2; j <= maskHeight / 2; j++) {
				if (image.validPixel(x + i, y + j)) {
					pixelsAffected.add(image.getPixel(x + i, y + j, color));
				}
			}
		}
		double val = 0;
		for (Double var : pixelsAffected) {
			val += var;
		}
		return val / pixelsAffected.size();
	}

	public static Image applyMasks(Image original, List<Mask> masks,
			SynthetizationType synthesizationType) {
		Image[] images = new Image[masks.size()];
		int i = 0;
		for (Mask mask : masks) {
			images[i++] = createImageAndApplyMask(original, mask);
		}
		return SynthesizationUtils.synthesize(synthesizationType, images);
	}

	private static Image createImageAndApplyMask(Image original, Mask mask) {
		Image image = original.shallowClone();
		if (original.isRgb()) {
			applyMask(original, image, mask, RED);
			applyMask(original, image, mask, GREEN);
			applyMask(original, image, mask, BLUE);
		} else if (original.isHsv()) {
			applyMask(original, image, mask, HUE);
			applyMask(original, image, mask, SATURATION);
			applyMask(original, image, mask, VALUE);
		}
		return image;
	}

	public static Image applyMaskAndZeroCross(Image original, Mask mask,
			int threshold) {
		Image masked = applyMask(original, mask);
		Image image = original.shallowClone();
		if (original.isRgb()) {
			zeroCross(masked, image, threshold, RED);
			zeroCross(masked, image, threshold, GREEN);
			zeroCross(masked, image, threshold, BLUE);
		} else if (original.isHsv()) {
			zeroCross(masked, image, threshold, HUE);
			zeroCross(masked, image, threshold, SATURATION);
			zeroCross(masked, image, threshold, VALUE);
		}
		return image;
	}

	private static void zeroCross(Image masked, Image image, int threshold,
			ChannelType color) {

		int width = image.getWidth();
		int height = image.getHeight();
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				double lastX;
				double pastX = 0;
				double currentX = 0;
				double pastY = 0;
				double lastY;
				double currentY = 0;
				if (x + 1 < width) {
					currentX = masked.getPixel(x + 1, y, color);
					lastX = pastX;
					pastX = masked.getPixel(x, y, color);

					if (pastX == 0 && x > 0) {
						pastX = lastX;
					}

					if (((currentX < 0 && pastX > 0) || (currentX > 0 && pastX < 0))
							&& Math.abs(currentX - pastX) > threshold) {
						image.setPixel(x, y, color, Image.MAX_VAL);
					}
				}
				if (y + 1 < height) {
					currentY = masked.getPixel(x, y + 1, color);
					lastY = pastY;
					pastY = masked.getPixel(x, y, color);

					if (pastY == 0 && x > 0) {
						pastY = lastY;
					}

					if (((currentY < 0 && pastY > 0) || (currentY > 0 && pastY < 0))
							&& Math.abs(currentY - pastY) > threshold) {
						image.setPixel(x, y, color, Image.MAX_VAL);
					}
				}
			}
		}

	}
}