package gui.tp1.punctual;

import gui.menus.PunctualOperationsMenu;
import application.utils.PunctualOperationsUtils;
import domain.Image;

@SuppressWarnings("serial")
public class MultiplyImagesItem extends ImageAlgebraicOperations {

	public MultiplyImagesItem(PunctualOperationsMenu t) {
		super("Multiply images", t);
	}

	@Override
	protected Image doOperation(Image panelImage, Image image) {
		return PunctualOperationsUtils.multiply(panelImage, image);
	}

}
