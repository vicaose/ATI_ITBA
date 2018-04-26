package gui.tp2.filters;

import gui.Panel;
import domain.mask.Mask;
import domain.mask.MaskFactory;
import domain.mask.MaskFactory.Direction;

@SuppressWarnings("serial")
public class DirectionalPrewittFilterDialog extends DirectionalBorderDetectorDialog {

	public DirectionalPrewittFilterDialog(final Panel panel) {
		super(panel, "Directional Prewitt filter border detection");
	}

	@Override
	protected Mask getMask(Direction d) {
		return MaskFactory.buildPrewittMask(d);
	}
}
