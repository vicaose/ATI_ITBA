package gui.tp2.filters;

import gui.Panel;
import domain.mask.Mask;
import domain.mask.MaskFactory;
import domain.mask.MaskFactory.Direction;

@SuppressWarnings("serial")
public class DirectionalSobelFilterDialog extends DirectionalBorderDetectorDialog {

	public DirectionalSobelFilterDialog(final Panel panel) {
		super(panel, "Directional Sobel filter border detection");
	}

	@Override
	protected Mask getMask(Direction d) {
		return MaskFactory.buildSobelMask(d);
	}
}
