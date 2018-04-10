package application;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import org.apache.sanselan.ImageReadException;

import domain.Image;
import domain.RgbImage;

public class Loader {

	private static File currentFile;

	public static Image loadImage(File arch) throws ImageReadException,
			IOException {

		currentFile = arch;
		BufferedImage bi  = ImageIO.read(arch);

		if (bi.getType() == BufferedImage.TYPE_INT_RGB || 
				bi.getType() == BufferedImage.TYPE_USHORT_555_RGB ||
				bi.getType() == BufferedImage.TYPE_USHORT_565_RGB ||
				bi.getType() == BufferedImage.TYPE_3BYTE_BGR ||
				bi.getType() == BufferedImage.TYPE_4BYTE_ABGR ||
				bi.getType() == BufferedImage.TYPE_4BYTE_ABGR_PRE ||
				bi.getType() == BufferedImage.TYPE_INT_ARGB ||
				bi.getType() == BufferedImage.TYPE_INT_BGR ||
				bi.getType() == BufferedImage.TYPE_INT_RGB) {
			return new RgbImage(bi);
		} else if (bi.getType() == BufferedImage.TYPE_BYTE_GRAY ||
				bi.getType() == BufferedImage.TYPE_USHORT_GRAY) {
			return new RgbImage(bi);
		} else {
			throw new IllegalStateException("Image wasn't RGB nor Grayscale");
		}

	}

	public static Image loadRaw(File file, int width, int height)
			throws IOException {
		
		BufferedImage ret;
		byte[] data = getBytesFromFile(file);
		ret = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
		WritableRaster raster = ret.getRaster();
		int k = 0;
		for (int j = 0; j < height; j++) {
			for (int i = 0; i < width; i++) {
				raster.setSample(i, j, 0, data[k]);
				k = k + 1;
			}
		} 
		Image image = new RgbImage(width, height);
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				image.setPixel(i, j, ret.getRGB(i, j));
			}
		}
		return image;
	}

	@SuppressWarnings("resource")
	public static byte[] getBytesFromFile(File file) throws IOException {
		InputStream is = new FileInputStream(file);
		byte[] bytes = new byte[(int) file.length()];
		int offset = 0;
		int numRead = 0;
		while (offset < bytes.length
				&& (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
			offset += numRead;
		}
		if (offset < bytes.length) {
			throw new IOException();
		}
		is.close();
		return bytes;
	}

	
	public static File getCurrentFile() {
		return currentFile;
	}
}
