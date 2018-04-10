package application.utils;

import static domain.Image.ChannelType.BLUE;
import static domain.Image.ChannelType.GREEN;
import static domain.Image.ChannelType.RED;
import domain.Image;
import domain.Image.ChannelType;
import domain.RgbImage;
import domain.SynthetizationType;

public class SynthesizationUtils {

	public static Image synthesize(SynthetizationType synthesizationType,
			Image... images) {
		switch (synthesizationType) {
		case MAX:
			return synthesize(new MaxFunction(), images);
		case MIN:
			return synthesize(new MinFunction(), images);
		case AVG:
			return synthesize(new AvgFunction(), images);
		case ABS:
			return synthesize(new AbsFunction(), images);
		default:
			return null;
		}
	}

	private static Image synthesize(Function f, Image[] images) {
		Image synthesized = new RgbImage(images[0].getWidth(),
				images[0].getHeight());
		for (int x = 0; x < synthesized.getWidth(); x++) {
			for (int y = 0; y < synthesized.getHeight(); y++) {
				synthesized.setPixel(x, y, RED, f.apply(getPixels(x, y, RED, images)));
				synthesized.setPixel(x, y, GREEN, f.apply(getPixels(x, y, GREEN, images)));
				synthesized.setPixel(x, y, BLUE, f.apply(getPixels(x, y, BLUE, images)));
			}
		}
		return synthesized;
	}

	private static double[] getPixels(int x, int y, ChannelType channel,
			Image[] images) {
		double[] data = new double[images.length];
		for (int i = 0; i < images.length; i++) {
			data[i] = images[i].getPixel(x, y, channel);
		}
		return data;
	}
	
	private interface Function {
		public double apply(double[] data);
	}


	private static class MaxFunction implements Function {
		@Override
		public double apply(double[] data) {
			double max = Double.MIN_VALUE;
			for (double d : data) {
				max = d > max ? d : max;
			}
			return max;
		}
	}

	private static class MinFunction implements Function {
		@Override
		public double apply(double[] data) {
			double min = Double.MAX_VALUE;
			for (double d : data) {
				min = d < min ? d : min;
			}
			return min;
		}
	}

	private static class AbsFunction implements Function {
		@Override
		public double apply(double[] data) {
			double sum = 0;
			for (double d : data) {
				sum += d * d;
			}
			return Math.sqrt(sum);
		}
	}

	private static class AvgFunction implements Function {
		@Override
		public double apply(double[] data) {
			double sum = 0;
			for (double d : data) {
				sum += d;
			}
			return sum / data.length;
		}
	}

}
