package domain;

import static domain.Image.ChannelType.BLUE;
import static domain.Image.ChannelType.GREEN;
import static domain.Image.ChannelType.RED;

import java.awt.Color;
import java.awt.image.BufferedImage;

import application.utils.ColorUtils;

public class RgbImage extends Image {
	
	private Channel red;
	private Channel green;
	private Channel blue;
	public RgbImage(int width, int height) {
		this.red = new Channel(width, height);
		this.green = new Channel(width, height);
		this.blue = new Channel(width, height);
		changed = true;
	}
	
	public RgbImage shallowClone(){
		return new RgbImage(getWidth(), getHeight());
	}

	public RgbImage(BufferedImage bi) {
		this(bi.getWidth(), bi.getHeight());
		bufferedImage = bi;
		initRGB(bi);
		changed = true;
	}

	private void initRGB(BufferedImage bi) {
		int[] rgbData = bi.getRGB(0, 0, bi.getWidth(), bi.getHeight(), null, 0,
				bi.getWidth());

		int colorRed;
		int colorGreen;
		int colorBlue;
		for (int x = 0; x < bi.getWidth(); x++) {
			for (int y = 0; y < bi.getHeight(); y++) {
				colorRed = (rgbData[(y * bi.getWidth()) + x] >> 16) & 0xFF;
				colorGreen = (rgbData[(y * bi.getWidth()) + x] >> 8) & 0xFF;
				colorBlue = (rgbData[(y * bi.getWidth()) + x]) & 0xFF;

				red.setPixel(x, y, colorRed);
				green.setPixel(x, y, colorGreen);
				blue.setPixel(x, y, colorBlue);
			}
		}
	}
	
	public static Image reuse(RgbImage img, BufferedImage newImg) {
		img.bufferedImage = newImg;
		img.initRGB(newImg);
		return img;
	}
	
	public void setPixel(int x, int y, int rgb) {
		setPixel(x, y, RED, ColorUtils.getRedFromRGB(rgb));
		setPixel(x, y, GREEN, ColorUtils.getGreenFromRGB(rgb));
		setPixel(x, y, BLUE, ColorUtils.getBlueFromRGB(rgb));
		changed = true;
	}

	
	public void setPixel(int x, int y, ChannelType channel, double color) {
		if (!red.validPixel(x, y)) 
			throw new IllegalArgumentException("Invalid pixels on setPixel");
		changed = true;
		switch(channel) {
		case RED:
			red.setPixel(x, y, color);
			return;
		case GREEN:
			green.setPixel(x, y, color);
			return;
		case BLUE:
			blue.setPixel(x, y, color);
			return;
		default:
			throw new IllegalStateException();
		}
	}
	
	int getPixel(int x, int y) {
		int redComponent = this.red.truncatePixel(getPixel(x, y, RED));
		int greenComponent = this.green.truncatePixel(getPixel(x, y, GREEN));
		int blueComponent = this.blue.truncatePixel(getPixel(x, y, BLUE));
		return new Color(redComponent, greenComponent, blueComponent).getRGB();
	}
	
	public double getPixel(int x, int y, ChannelType channel) {
		switch(channel) {
		case RED:
			return red.getPixel(x, y);
		case GREEN:
				return green.getPixel(x, y);
		case BLUE:
				return blue.getPixel(x, y);
		default:
			throw new IllegalStateException();
		}
	}
	
	public int getHeight() {
		return red.getHeight();
	}

	public int getWidth() {
		return red.getWidth();
	}
	
	public BufferedImage getBufferedImage() {
		if (bufferedImage == null) {
			bufferedImage = new BufferedImage(getWidth(), getHeight(), 1);
		}
		for (int x = 0; x < getWidth(); x++) {
			for (int y = 0; y < getHeight(); y++) {
				bufferedImage.setRGB(x, y, getPixel(x, y));
			}
		}
		return bufferedImage;
	}
	
	public double getGraylevelFromPixel(int x, int y) {
		double red = this.red.getPixel(x, y);
		double green = this.green.getPixel(x, y);
		double blue = this.blue.getPixel(x, y);
		return (red + green + blue) / 3.0;
	}
	
	@Override
	public RgbImage clone() {
		RgbImage cloned = shallowClone();
		for (int x = 0; x < getWidth(); x++) {
			for (int y = 0; y < getHeight(); y++) {
				cloned.setPixel(x, y, RED, getPixel(x, y, RED));
				cloned.setPixel(x, y, GREEN, getPixel(x, y, GREEN));
				cloned.setPixel(x, y, BLUE, getPixel(x, y, BLUE));
			}
		}
		return cloned;
	}
	
	protected void checkType() {
		for (int x = 0; x < getWidth(); x++) {
			for (int y = 0; y < getHeight(); y++) {
				double r = red.getPixel(x, y);
				double g = green.getPixel(x, y);
				double b = blue.getPixel(x, y);
				if (r != g || r != b || g != b) {
					type = ImageType.COLOR;
					return;
				}
			}
		}
		type = ImageType.GREYSCALE; 
		return;
	}

	@Override
	public boolean isHsv() {
		return false;
	}

	@Override
	public boolean isRgb() {
		return true;
	}
}
