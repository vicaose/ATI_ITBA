package gui.tp3;

import gui.Panel;

import java.awt.Point;

import application.utils.ImageConversionUtils;
import domain.HsvImage;
import domain.Image;
import domain.RgbImage;
import domain.tracking.Frontier;

@SuppressWarnings("serial")
public class RgbVideoTrackingDialog extends VideoTrackingDialog {

	public RgbVideoTrackingDialog(Panel panel) {
		super(panel);
	}

	@Override
	protected Frontier getFrontier(int px, int py, int qx, int qy) {
		Image image = panel.getImage();
		RgbImage rgb;
		if (image.isHsv())
			rgb = ImageConversionUtils.convertToRgb((HsvImage)image);
		else
			rgb = (RgbImage)image;
			
		return new Frontier(new Point(px, py), new Point(qx, qy), rgb);
	}
}
