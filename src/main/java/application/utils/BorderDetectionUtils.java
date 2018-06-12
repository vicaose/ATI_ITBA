package application.utils;

import static application.utils.BasicImageUtils.paintRedN8;
import static domain.Image.ChannelType.BLUE;
import static domain.Image.ChannelType.GREEN;
import static domain.Image.ChannelType.RED;

import java.util.ArrayList;
import java.util.List;

import domain.Image;
import domain.Image.ChannelType;
import domain.SynthetizationType;
import domain.mask.Mask;
import domain.mask.MaskFactory;
import domain.mask.MaskFactory.Direction;

public class BorderDetectionUtils {

	public static Image canny(Image original) {
		Image image = (Image) original.clone();
		MaskUtils.applyMask(image, MaskFactory.buildGaussianMask(5, 0.2));
		image = supressNoMaximums(image);
		ThresholdUtils.hysteresisForCanny(image);
		PunctualOperationsUtils.maximize(image);
		return image;
	}

	private static Image buildDirectionsImage(Image image, Image Gx, Image Gy) {
		Image direction = image.shallowClone();
		for (int x = 0; x < image.getWidth(); x++) {
			for (int y = 0; y < image.getHeight(); y++) {
				for (ChannelType c : Image.rgbValues()) {
					double pxGx = Gx.getPixel(x, y, c);
					double pxGy = Gy.getPixel(x, y, c);
					double anAngle = 0;
					if (pxGy != 0) {
						anAngle = Math.atan(pxGx / pxGy);
					}
					anAngle *= (180 / Math.PI);
					direction.setPixel(x, y, c, anAngle);
				}
			}
		}
		return direction;
	}

	public static Image supressNoMaximums(Image image) {
		Image Gx = MaskUtils.applyMask((Image) image.clone(),
				MaskFactory.buildSobelMask(Direction.HORIZONTAL));
		Image Gy = MaskUtils.applyMask((Image) image.clone(),
				MaskFactory.buildSobelMask(Direction.VERTICAL));
		Image direction = buildDirectionsImage(image, Gx, Gy);
		Image[] Gs = { Gx, Gy };
		image = SynthesizationUtils.synthesize(SynthetizationType.ABS, Gs);
		return supressNoMaximums(image, direction);
	}

	private static Image supressNoMaximums(Image image, Image direction) {
		Image[] images = new Image[3];
		images[0] = applyDirection(image, direction, RED);
		images[1] = applyDirection(image, direction, GREEN);
		images[2] = applyDirection(image, direction, BLUE);
		return SynthesizationUtils.synthesize(SynthetizationType.MAX, images);
	}

	private static Image applyDirection(Image original, Image directionImage,
			ChannelType c) {
		Image image = original.shallowClone();
		for (int x = 1; x < original.getWidth() - 1; x++) {
			for (int y = 1; y < original.getHeight() - 1; y++) {
				double pixel = original.getPixel(x, y, c);
				if (pixel == 0) {
					continue;
				}

				double direction = directionImage.getPixel(x, y, c);
				double neighbor1 = 0;
				double neighbor2 = 0;
				// Get neighbours
				if (direction >= -90 && direction < -45) {
					neighbor1 = original.getPixel(x, y - 1, c);
					neighbor2 = original.getPixel(x, y + 1, c);
				} else if (direction >= -45 && direction < 0) {
					neighbor1 = original.getPixel(x + 1, y - 1, c);
					neighbor2 = original.getPixel(x - 1, y + 1, c);
				} else if (direction >= 0 && direction < 45) {
					neighbor1 = original.getPixel(x + 1, y, c);
					neighbor2 = original.getPixel(x - 1, y, c);
				} else if (direction >= 45 && direction <= 90) {
					neighbor1 = original.getPixel(x + 1, y + 1, c);
					neighbor2 = original.getPixel(x - 1, y - 1, c);
				}

				// If neighbours are greater than the pixels, erase them from
				// borders.
				if (neighbor1 > pixel || neighbor2 > pixel) {
					image.setPixel(x, y, c, 0);
				} else {
					image.setPixel(x, y, c, pixel);
				}
			}
		}
		return image;
	}

	public static Image susan(Image image, double min, double max) {
		Mask mask = MaskFactory.buildSusanMask();
		Image susaned = (Image) image.clone();
		for (int x = 0; x < susaned.getWidth(); x++) {
			for (int y = 0; y < susaned.getHeight(); y++) {
				for (ChannelType c : Image.rgbValues()) {
					double s_ro = MaskUtils.applySusanPixelMask(x, y, mask,
							image, c);
					if (s_ro < max && s_ro > min) {
						paintRedN8(susaned, x, y);
					}
				}
			}
		}
		return susaned;
	}

	public static Image harris(Image image, Mask mask,
			double threshold, double k) {
		Image harried = image.clone();
		int w = image.getWidth(), h = image.getHeight();
		double[][] lx2 = new double[w][h];
		double[][] ly2 = new double[w][h];
		double[][] lxy = new double[w][h];
		List<Corner> corners = new ArrayList<Corner>();
		computeDerivatives(harried, mask, lx2, ly2, lxy);
		double[][] harrismap = computeHarrisResponse(k, lx2, ly2, lxy);
		for (int x = 1; x < w - 1; x++) {
			for (int y = 1; y < h - 1; y++) {
				double v = harrismap[x][y];
				if (v >= threshold
						&& isSpatialMaxima(harrismap, (int) x, (int) y))
					corners.add(new Corner(x, y, v));
			}
		}
		for (Corner p : new ArrayList<Corner>(corners)) {
			for (Corner n : corners) {
				if (n != p) {
					int dist = (int) Math.sqrt((p.x - n.x) * (p.x - n.x)
							+ (p.y - n.y) * (p.y - n.y));
					if (dist <= 3 && n.measure < p.measure) {
						corners.remove(p);
						break;
					}
				}
			}
		}
		for (Corner c : corners) {
			System.out.println(c.x + " " + c.y);
			paintRedN8(harried, c.x, c.y);
		}
		return harried;
	}

	private static boolean isSpatialMaxima(double[][] hmap, int x, int y) {
		int n = 8;
		int[] dx = new int[] { -1, 0, 1, 1, 1, 0, -1, -1 };
		int[] dy = new int[] { -1, -1, -1, 0, 1, 1, 1, 0 };
		double w = hmap[x][y];
		for (int i = 0; i < n; i++) {
			double wk = hmap[x + dx[i]][y + dy[i]];
			if (wk >= w)
				return false;
		}
		return true;
	}

	private static double[][] computeHarrisResponse(double k, double[][] lx2,
			double[][] ly2, double[][] lxy) {
		int width = ly2.length, height = ly2[0].length;
		double[][] harrisResponse = new double[width][height];
		double max = 0;
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				harrisResponse[x][y] = harrisResponse(x, y, k, lx2, ly2, lxy);
				if (harrisResponse[x][y] > max)
					max = harrisResponse[x][y];
			}
		}
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				double r = harrisResponse[x][y];
				if (r < 0) {
					r = 0;
				} else {
					r = 100 * Math.log(1 + r) / Math.log(1 + max);
				}
				harrisResponse[x][y] = r;
			}
		}
		return harrisResponse;
	}

	private static double harrisResponse(int x, int y, double k,
			double[][] lx2, double[][] ly2, double[][] lxy) {
		return lx2[x][y] * ly2[x][y] - lxy[x][y] * lxy[x][y] - k
				* (lx2[x][y] + ly2[x][y]) * (lx2[x][y] + ly2[x][y]);
	}

	private static void computeDerivatives(Image image, Mask gaussian, double[][] lx2, double[][] ly2, double[][] lxy) {
		int w = image.getWidth(), h = image.getHeight();
		Image gx = MaskUtils.applyMask(image,
				MaskFactory.buildSobelMask(Direction.HORIZONTAL));
		Image gy = MaskUtils.applyMask(image,
				MaskFactory.buildSobelMask(Direction.VERTICAL));
		for (int x = 0; x < w; x++) {
			for (int y = 0; y < h; y++) {
				for (int dx = -gaussian.getHeight() / 2; dx <= gaussian.getWidth()/2 ; dx++) {
					for (int dy = -gaussian.getHeight() / 2; dy <= gaussian.getHeight()/2; dy++) {
						int xk = x + dx;
						int yk = y + dy;
						if (xk >= 0 && xk < w && yk >= 0 && yk < h) {
							double f = gaussian.getValue(dx, dy);
							double gxp = gx.getPixel(xk, yk, RED), gyp = gy
									.getPixel(xk, yk, RED);
							lx2[x][y] += f * gxp * gxp;
							ly2[x][y] += f * gyp * gyp;
							lxy[x][y] += f * gxp * gyp;
						}
					}
				}
			}
		}
	}
}
