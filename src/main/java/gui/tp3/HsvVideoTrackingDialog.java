package gui.tp3;

import gui.Panel;

import java.awt.Point;

import application.utils.ImageConversionUtils;
import domain.HsvImage;
import domain.Image;
import domain.RgbImage;
import domain.tracking.Frontier;

@SuppressWarnings("serial")
public class HsvVideoTrackingDialog extends VideoTrackingDialog {

	public HsvVideoTrackingDialog(Panel panel) {
		super(panel);
	}

	@Override
	protected Frontier getFrontier(int px, int py, int qx, int qy) {
		Image image = panel.getImage();
		HsvImage hsv;
		if (image.isRgb())
			hsv = ImageConversionUtils.convertToHsv((RgbImage) image);
		else
			hsv = (HsvImage) image;

		return new Frontier(new Point(px, py), new Point(qx, qy), hsv);
	}

}
