package domain.tracking;

import static domain.Image.ChannelType.BLUE;
import static domain.Image.ChannelType.GREEN;
import static domain.Image.ChannelType.RED;

import java.awt.Point;

import application.utils.ImageConversionUtils;
import domain.HsvImage;
import domain.Image;
import domain.Image.ChannelType;
import domain.RgbImage;

public class RgbTita extends Tita {

	public RgbTita(RgbImage image, Frontier frontier) {
		super(image, frontier);
	}

	@Override
	protected int getAvgIndex(ChannelType c) {
		switch (c) {
		case RED:
			return 0;
		case GREEN:
			return 1;
		case BLUE:
			return 2;
		default:
			throw new IllegalArgumentException();
		}
	}

	@Override
	protected void removeFromOuter(int x, int y) {
		outerSum[0] -= image.getPixel(x, y, RED);
		outerSum[1] -= image.getPixel(x, y, GREEN);
		outerSum[2] -= image.getPixel(x, y, BLUE);
		outerSize--;

	}

	@Override
	protected void removeFromInner(int x, int y) {
		innerSum[0] -= image.getPixel(x, y, RED);
		innerSum[1] -= image.getPixel(x, y, GREEN);
		innerSum[2] -= image.getPixel(x, y, BLUE);
		innerSize--;

	}

	@Override
	protected void addToOuter(int x, int y) {
		outerSum[0] += image.getPixel(x, y, RED);
		outerSum[1] += image.getPixel(x, y, GREEN);
		outerSum[2] += image.getPixel(x, y, BLUE);
		outerSize++;
	}

	@Override
	protected void addToInner(int x, int y) {
		innerSum[0] += image.getPixel(x, y, RED);
		innerSum[1] += image.getPixel(x, y, GREEN);
		innerSum[2] += image.getPixel(x, y, BLUE);
		innerSize++;
	}

	@Override
	public double calculateVelocity(Point p) {
		double p1, p2;
		double red = image.getPixel(p.x, p.y, RED);
		double green = image.getPixel(p.x, p.y, GREEN);
		double blue = image.getPixel(p.x, p.y, BLUE);

		p1 = Math.sqrt(Math.pow((averageInner(RED) - red), 2)
				+ Math.pow((averageInner(GREEN) - green), 2)
				+ Math.pow((averageInner(BLUE) - blue), 2));
		p2 = Math.sqrt(Math.pow((averageOuter(RED) - red), 2)
				+ Math.pow((averageOuter(GREEN) - green), 2)
				+ Math.pow((averageOuter(BLUE) - blue), 2));

		return p2 - p1;
	}

	@Override
	public void setImage(Image image) {
		RgbImage rgb;
		if (image.isHsv())
			rgb = ImageConversionUtils.convertToRgb((HsvImage) image);
		else
			rgb = (RgbImage) image;
		super.setImage(rgb);
	}

	@Override
	protected void recalculateAvgs() {
		innerSum[0] = innerSum[1] = innerSum[2] = outerSum[0] = outerSum[1] = outerSum[2] = innerSize = outerSize = 0;
		for (int x = 0; x < image.getWidth(); x++) {
			for (int y = 0; y < image.getHeight(); y++) {
				if (getValue(x, y) == 3) {
					addToOuter(x, y);
				} else if (getValue(x, y) == -3) {
					addToInner(x, y);
				}
			}
		}
	}

}
