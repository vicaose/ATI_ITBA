package gui.tp2.filters;

import gui.Panel;
import domain.mask.Mask;
import domain.mask.MaskFactory;
import domain.mask.MaskFactory.Direction;

@SuppressWarnings("serial")
public class KirshBorderDetectorDialog extends DirectionalBorderDetectorDialog {

	public KirshBorderDetectorDialog(final Panel panel) {
		super(panel, "Kirsh border detection");
	}

	@Override
	protected Mask getMask(Direction d) {
		return MaskFactory.buildKirshMask(d);
	}
	
}
