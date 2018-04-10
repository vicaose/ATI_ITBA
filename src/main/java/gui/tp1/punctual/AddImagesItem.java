package gui.tp1.punctual;

import gui.menus.PunctualOperationsMenu;
import application.utils.PunctualOperationsUtils;
import domain.Image;

@SuppressWarnings("serial")
public class AddImagesItem extends ImageAlgebraicOperations {

	public AddImagesItem(PunctualOperationsMenu m) {
		super("Add images", m);
	}

	@Override
	protected Image doOperation(Image panelImage, Image image) {
		return PunctualOperationsUtils.add(panelImage, image);
	}

}
