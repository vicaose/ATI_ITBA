package application.utils;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;

import domain.Image;

public class ColorUtils {

	public static int getRedFromRGB(int rgb) {
		return new Color(rgb).getRed();
	}

	public static int getGreenFromRGB(int rgb) {
		return new Color(rgb).getGreen();
	}

	public static int getBlueFromRGB(int rgb) {
		return new Color(rgb).getBlue();
	}

	public static BufferedImage populateEmptyBufferedImage(
			Image image) {
        ColorModel cm = image.getBufferedImage().getColorModel();
        boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
        WritableRaster raster = image.getBufferedImage().copyData(null);
        return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
	}

	public static int toBufferedImageType(Image.ImageType type) {
		switch(type) {
		case GREYSCALE:
			return BufferedImage.TYPE_BYTE_GRAY;
		case COLOR:
			return BufferedImage.TYPE_INT_RGB;
		default:
			throw new IllegalArgumentException();
		}
	}
	
//	public static ImageFormat toSanselanImageFormat(Image.ImageFormat fmt) {
//		switch(fmt) {
//		case BMP:
//			return ImageFormat.IMAGE_FORMAT_BMP;
//		case PGM:
//			return ImageFormat.IMAGE_FORMAT_PGM;
//		case PPM:
//			return ImageFormat.IMAGE_FORMAT_PPM;
//		case RAW:
//			return ImageFormat.IMAGE_FORMAT_UNKNOWN;
//		default:
//			throw new IllegalArgumentException();			
//		}
//	}
}
