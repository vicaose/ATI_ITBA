package gui.tp2.filters;

import gui.Panel;
import application.utils.MaskUtils;
import domain.SynthetizationType;
import domain.mask.MaskFactory;

@SuppressWarnings("serial")
public class RobertsBorderDetectorDialog extends BorderDetectorDialog {

	public RobertsBorderDetectorDialog(final Panel panel) {
		super(panel, "Roberts border detection");
	}

	public void applyFunction(SynthetizationType synthesizationType) {
		panel.setImage(MaskUtils.applyMasks(panel.getImage(),
				MaskFactory.buildRobertsMasks(), synthesizationType));
		panel.repaint();
		dispose();
	}

}
