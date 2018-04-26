package gui.tp2.filters;

import gui.Panel;
import domain.mask.Mask;
import domain.mask.MaskFactory;
import domain.mask.MaskFactory.Direction;

@SuppressWarnings("serial")
public class AnOtherFilterDialog extends DirectionalBorderDetectorDialog {
	
	public AnOtherFilterDialog(final Panel panel) {
		super(panel, "An other filter border detection");
	}

	@Override
	protected Mask getMask(Direction d) {
		return MaskFactory.buildAnOtherMask(d);
	}

}
