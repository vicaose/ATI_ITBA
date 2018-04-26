package gui.tp2.filters;

import gui.Panel;
import application.utils.MaskUtils;
import domain.SynthetizationType;
import domain.mask.MaskFactory;

@SuppressWarnings("serial")
public class SobelBorderDetectorDialog extends BorderDetectorDialog {

	public SobelBorderDetectorDialog(final Panel panel) {
		super(panel, "Sobel border detection");
	}

	public void applyFunction(SynthetizationType synthesizationType) {
		panel.setImage(MaskUtils.applyMasks(panel.getImage(),
				MaskFactory.buildSobelMasks(), synthesizationType));
		panel.repaint();
		dispose();
	}
}
