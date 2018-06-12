package gui.tp3;

import gui.MessageFrame;
import gui.Panel;

import java.io.File;
import java.io.IOException;

import org.apache.sanselan.ImageReadException;

import application.Loader;
import application.utils.TrackingUtils;
import domain.tracking.Frontier;

@SuppressWarnings("serial")
public abstract class VideoTrackingDialog extends TrackingDialog {

	public VideoTrackingDialog(Panel panel) {
		super(panel);
	}

	@Override
	protected void track(int px, int py, int qx, int qy, int iterations) {
		dispose();
		String[] splitted = Loader.getCurrentFile().getAbsolutePath()
				.split("/");
		String fileName = splitted[splitted.length - 1];
		int x = fileName.indexOf('.');

		StringBuffer num = new StringBuffer();
		boolean end = false;
		while (!end && x > 0) {
			char n = fileName.charAt(--x);
			if (n >= '0' && n <= '9')
				num.append(n);
			else
				end = true;
		}
		num.reverse();
		String[] nameSplitted = Loader.getCurrentFile().getAbsolutePath()
				.split(num.toString() + ".");
		if (nameSplitted.length < 2) {
			new MessageFrame("Invalid Image");
			return;
		}
		String extension = nameSplitted[1];
		String filePrefix = nameSplitted[0];
		Frontier frontier = getFrontier(px, py, qx, qy);
		TrackingUtils.track(frontier, panel, iterations);
		panel.repaint();
		int i = Integer.valueOf(num.toString());
		i++;
		boolean read = true;
		while (read) {
//			long t0 = System.currentTimeMillis();
			File currentFile = new File(filePrefix + i + "." + extension);
			if (!currentFile.exists()) {
				read = false;
				break;
			}
			try {
				panel.setTempImage(Loader.loadImage(currentFile));
			} catch (ImageReadException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
//			long t1 = System.currentTimeMillis();
			frontier.setImage(panel.getTempImage());
//			System.out.println("Tardo en levantar la imagen: " + (System.currentTimeMillis() - t1));
			TrackingUtils.track(frontier, panel, iterations);
//			System.out.println("Tardo todo el ciclo: " + (System.currentTimeMillis() - t0));
			i++;
		}

	}

	protected abstract Frontier getFrontier(int px, int py, int qx, int qy);
}
