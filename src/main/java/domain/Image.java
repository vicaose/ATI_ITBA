package domain;

import java.awt.Point;
import static domain.Image.ChannelType.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class Image implements Cloneable {

	public static enum ChannelType {
		RED, GREEN, BLUE, HUE, SATURATION, VALUE
	}

	public static enum ImageType {
		COLOR, GREYSCALE
	}

//	public static enum ImageFormat {
//		BMP, PGM, PPM, RAW
//	}

	public static final int MAX_VAL = 255;

	protected ImageType type;
	protected boolean changed;
	protected BufferedImage bufferedImage;

	public abstract void setPixel(int x, int y, int val); 
	
	public abstract void setPixel(int x, int y, ChannelType channel, double val); 
	
	public abstract double getPixel(int x, int y, ChannelType channel);

	public abstract int getHeight();

	public abstract int getWidth();
	
	public abstract BufferedImage getBufferedImage();
	
	public abstract Image clone();
	
	public abstract Image shallowClone();
	
	public ImageType getType() {
		if (!changed) {
			return type;
		}
		changed = false;
		checkType();
		return type;
	}
	
	protected abstract void checkType();

	public boolean validPixel(int i, int j) {
		return i >= 0 && j >= 0 && i < getWidth() && j < getHeight();
	}

	public double getPixel(Point p, ChannelType c) {
		return getPixel(p.x, p.y, c);
	}
	public void setPixel(Point p, ChannelType c, double val) {
		setPixel(p.x, p.y, c, val);
	}

	public abstract double getGraylevelFromPixel(int x, int y);

	public static Collection<ChannelType> rgbValues() {
		List<ChannelType> l = new ArrayList<ChannelType>();
		l.add(RED);
		l.add(GREEN);
		l.add(BLUE);
		return l;
	}
	
	public static Collection<ChannelType> hsvValues() {
		List<ChannelType> l = new ArrayList<ChannelType>();
		l.add(HUE);
		l.add(SATURATION);
		l.add(VALUE);
		return l;
	}

	public abstract boolean isHsv(); 
	public abstract boolean isRgb(); 
	
}
