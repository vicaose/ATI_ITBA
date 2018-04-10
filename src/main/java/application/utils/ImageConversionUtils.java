package application.utils;

import static domain.Image.ChannelType.BLUE;
import static domain.Image.ChannelType.GREEN;
import static domain.Image.ChannelType.HUE;
import static domain.Image.ChannelType.RED;
import static domain.Image.ChannelType.SATURATION;
import static domain.Image.ChannelType.VALUE;
import domain.HsvImage;
import domain.Image;
import domain.RgbImage;

public class ImageConversionUtils {
	
	public static Image convert(Image image) {
		if (image.isRgb()) 
			return convertToHsv((RgbImage) image);
		else if (image.isHsv()) 
			return convertToRgb((HsvImage)image);
		return null;
	}

	public static HsvImage convertToHsv(RgbImage rgb) {
		HsvImage hsv = new HsvImage(rgb.getWidth(), rgb.getHeight());
		for (int x = 0; x < rgb.getWidth(); x++) {
			for (int y = 0; y < rgb.getHeight(); y++) {
				HsvPixel px = convertPixel(rgb, x, y);
				hsv.setPixel(x, y, HUE, px.hue);
				hsv.setPixel(x, y, SATURATION, px.saturation);
				hsv.setPixel(x, y, VALUE, px.value);
			}
		}
		return hsv;
	}
	
	public static RgbImage convertToRgb(HsvImage hsv) {
		RgbImage rgb = new RgbImage(hsv.getWidth(), hsv.getHeight());
		for (int x = 0; x < rgb.getWidth(); x++) {
			for (int y = 0; y < rgb.getHeight(); y++) {
				RgbPixel px = convertPixel(hsv, x, y);
				rgb.setPixel(x, y, RED, px.red);
				rgb.setPixel(x, y, GREEN, px.green);
				rgb.setPixel(x, y, BLUE, px.blue);
			}
		}
		return rgb;
	}
	
	private static HsvPixel convertPixel(RgbImage image, int x, int y) {
		double min, max, delta;
		double r = image.getPixel(x, y, RED);
		double g = image.getPixel(x, y, GREEN);
		double b = image.getPixel(x, y, BLUE);

		min = Math.min(Math.min(r, g), b);
		max = Math.max(Math.max(r, g), b);
		double v = max, h, s;
		delta = max - min;
		if (max != 0)
			s = delta / max; // s
		else {
			// r = g = b = 0 // s = 0, v is undefined
			s = 0;
			h = 0; //-1
			return new HsvPixel(h, s, v);
		}
		if (delta == 0)
			delta = 1;
		if (r == max)
			h = (g - b) / delta; // between yellow & magenta
		else if (g == max)
			h = 2 + ((b - r) / delta); // between cyan & yellow
		else
			h = 4 + ((r - g) / delta); // between magenta & cyan
		h *= 60; // degrees
		if (h < 0)
			h += 360;
		return new HsvPixel(h, s, v);
	}

	private static RgbPixel convertPixel(HsvImage image, int x, int y) {
		int i;
		double f, p, q, t;
		double h = image.getPixel(x, y, HUE);
		double s = image.getPixel(x, y, SATURATION);
		double v = image.getPixel(x, y, VALUE);

		if (s == 0) {
			// achromatic (grey)
			return new RgbPixel(v, v, v);
		}
		h /= 60; // sector 0 to 5
		i = (int) h;
		f = h - i; // factorial part of h
		p = v * (1 - s);
		q = v * (1 - s * f);
		t = v * (1 - s * (1 - f));
		switch (i) {
		case 0:
			return new RgbPixel(v, t, p);
		case 1:
			return new RgbPixel(q, v, p);
		case 2:
			return new RgbPixel(p, v, t);
		case 3:
			return new RgbPixel(p, q, v);
		case 4:
			return new RgbPixel(t, p, v);
		default: // case 5:
			return new RgbPixel(v, p, q);
		}
	}

	private static class RgbPixel {
		private double red, green, blue;

		public RgbPixel(double red, double green, double blue) {
			this.red = red;
			this.green = green;
			this.blue = blue;
		}

	}

	private static class HsvPixel {
		private double hue, saturation, value;

		public HsvPixel(double hue, double saturation, double value) {
			this.hue = hue;// * 255 / 360;
			this.saturation = saturation;// * 255;
			this.value = value;// * 255;
		}

	}

}
