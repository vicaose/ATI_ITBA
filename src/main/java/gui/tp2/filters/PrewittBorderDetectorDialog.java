package gui.tp2.filters;

import gui.Panel;
import application.utils.MaskUtils;
import domain.SynthetizationType;
import domain.mask.MaskFactory;

@SuppressWarnings("serial")
public class PrewittBorderDetectorDialog extends BorderDetectorDialog {

	public PrewittBorderDetectorDialog(final Panel panel) {
		super(panel, "Prewitt border detection");
	}

	public void applyFunction(SynthetizationType synthesizationType) {
		panel.setImage(MaskUtils.applyMasks(panel.getImage(),
				MaskFactory.buildPrewittMasks(), synthesizationType));
		panel.repaint();
		dispose();
	}
}
