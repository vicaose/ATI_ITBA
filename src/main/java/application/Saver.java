package application;

import ij.ImagePlus;
import ij.io.FileSaver;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.sanselan.ImageWriteException;

import application.utils.ColorUtils;

import domain.Image;

public class Saver {
	
	public static void saveImage(File arch, Image image) throws ImageWriteException, IOException {
		String[] cadena = (arch.getName()).split("\\.");
		String extension = cadena[cadena.length-1];
        BufferedImage bi = ColorUtils.populateEmptyBufferedImage(image);
        if(!extension.equals("raw")){
            ImageIO.write(bi, "jpg", arch);
        } else {
            new FileSaver(new ImagePlus("", bi)).saveAsRaw(arch.getAbsoluteFile().toString());
		}


	}

}
