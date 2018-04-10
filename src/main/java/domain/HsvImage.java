package domain;

import static domain.Image.ChannelType.HUE;
import static domain.Image.ChannelType.SATURATION;
import static domain.Image.ChannelType.VALUE;

import java.awt.image.BufferedImage;

import application.utils.ImageConversionUtils;

public class HsvImage extends Image {

	private Channel hue;
	private Channel saturation;
	private Channel value;

	public HsvImage(int width, int height) {
		this.hue = new Channel(width, height);
		this.saturation = new Channel(width, height);
		this.value = new Channel(width, height);
		changed = true;
	}

	@Override
	public void setPixel(int x, int y, int val) {
		throw new IllegalAccessError();
		// setPixel(x, y, HUE, ColorUtils.getRedFromRGB(rgb));
		// setPixel(x, y, VALUE, ColorUtils.getGreenFromRGB(rgb));
		// setPixel(x, y, SATURATION, ColorUtils.getBlueFromRGB(rgb));
		// changed = true;
	}

	@Override
	public void setPixel(int x, int y, ChannelType channel, double val) {
		if (!hue.validPixel(x, y)) {
			throw new IllegalArgumentException("Invalid pixels on setPixel");
		}
		changed = true;
		switch (channel) {
		case HUE:
			hue.setPixel(x, y, val);
			return;
		case SATURATION:
			saturation.setPixel(x, y, val);
			return;
		case VALUE:
			value.setPixel(x, y, val);
			return;
		default:
			throw new IllegalStateException();
		}
	}

	@Override
	public double getPixel(int x, int y, ChannelType channel) {
		switch (channel) {
		case HUE:
		case RED:
			return hue.getPixel(x, y);
		case SATURATION:
		case GREEN:
			return saturation.getPixel(x, y);
		case VALUE:
		case BLUE:
			return value.getPixel(x, y);
		default:
			throw new IllegalStateException();
		}
	}

	@Override
	public int getHeight() {
		return hue.getHeight();
	}

	@Override
	public int getWidth() {
		return hue.getWidth();
	}

	@Override
	protected void checkType() {
		for (int x = 0; x < getWidth(); x++) {
			for (int y = 0; y < getHeight(); y++) {
				if (saturation.getPixel(x, y) != 0) {
					type = ImageType.COLOR;
					return;
				}
			}
		}
		type = ImageType.GREYSCALE;
	}

	@Override
	public BufferedImage getBufferedImage() {
		if (bufferedImage == null) {
			bufferedImage = new BufferedImage(getWidth(), getHeight(), 1);
		}
		RgbImage rgb = ImageConversionUtils.convertToRgb(this);
		for (int x = 0; x < getWidth(); x++) {
			for (int y = 0; y < getHeight(); y++) {
				bufferedImage.setRGB(x, y, rgb.getPixel(x, y));
			}
		}
		return bufferedImage;
	}

	@Override
	public HsvImage clone() {
		HsvImage cloned = shallowClone();
		for (int x = 0; x < getWidth(); x++) {
			for (int y = 0; y < getHeight(); y++) {
				cloned.setPixel(x, y, HUE, getPixel(x, y, HUE));
				cloned.setPixel(x, y, SATURATION, getPixel(x, y, SATURATION));
				cloned.setPixel(x, y, VALUE, getPixel(x, y, VALUE));
			}
		}
		return cloned;
	}

	public HsvImage shallowClone() {
		return new HsvImage(getWidth(), getHeight());
	}

	@Override
	public double getGraylevelFromPixel(int x, int y) {
		throw new IllegalAccessError();
	}

	@Override
	public boolean isHsv() {
		return true;
	}

	@Override
	public boolean isRgb() {
		return false;
	}

}
