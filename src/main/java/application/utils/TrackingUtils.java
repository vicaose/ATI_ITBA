package application.utils;

import static domain.Image.ChannelType.BLUE;
import static domain.Image.ChannelType.GREEN;
import static domain.Image.ChannelType.HUE;
import static domain.Image.ChannelType.RED;
import static domain.Image.ChannelType.SATURATION;
import static domain.Image.ChannelType.VALUE;
import gui.Panel;

import java.awt.Point;

import domain.Image;
import domain.tracking.Frontier;

public class TrackingUtils {

	public static Image track(Frontier frontier, Panel panel, int iterations) {
		Image image = (Image) frontier.getImage();
		int i = 0;
		boolean changed = true;
//		long time0 = System.currentTimeMillis();
		while (i < iterations && changed) {
			changed = frontier.change();
			i++;
		}
		panel.setTempImage(drawBorder(frontier, (Image) frontier.getImage()
				.clone()));
		panel.paintImmediately(0, 0, image.getWidth(), image.getWidth());
//		System.out.println("Tardo en calcular el borde " + (System.currentTimeMillis() - time0) + " milisegundos");
		
//		System.out.println("painted");
		
		return image;
	}

	private static Image drawBorder(Frontier frontier, Image image) {
		if (image.isRgb()) {
//			for (Point p : frontier.getInnerBorder()) {
//				image.setPixel(p.x, p.y, RED, 255);
//				image.setPixel(p.x, p.y, GREEN, 0);
//				image.setPixel(p.x, p.y, BLUE, 0);
//			}
			for (Point p : frontier.getOuterBorder()) {
				image.setPixel(p.x, p.y, RED, 0);
				image.setPixel(p.x, p.y, GREEN, 255);
				image.setPixel(p.x, p.y, BLUE, 0);
			}
		} else if (image.isHsv()) {
			for (Point p : frontier.getInnerBorder()) {
				image.setPixel(p.x, p.y, HUE, 35);
				image.setPixel(p.x, p.y, SATURATION, 1);
				image.setPixel(p.x, p.y, VALUE, 255);

			}
		}
		return image;
	}
}
