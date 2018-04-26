package domain.mask;

import java.util.ArrayList;
import java.util.List;

public class MaskFactory {

	public enum Direction {
		VERTICAL, HORIZONTAL, DIAGONAL, INVERSE_DIAGONAL
	}
	
	public static Mask buildSusanMask() {
		double[][] values = {{0.0, 0.0, 1.0, 1.0, 1.0, 0.0, 0.0}, {0.0, 1.0, 1.0, 1.0, 1.0, 1.0, 0.0},
				{1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0}, {1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0}, {1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0},
				{0.0, 1.0, 1.0, 1.0, 1.0, 1.0, 0.0}, {0.0, 0.0, 1.0, 1.0, 1.0, 0.0, 0.0}};
		return new Mask(values);
	}

	public static Mask buildEdgeEnhancementMask(int width, int height) {
		Mask mask = new Mask(width, height);
		double pixelAmount = width * height;
		for (int i = -mask.getWidth() / 2; i <= mask.getWidth() / 2; i++) {
			for (int j = -mask.getHeight() / 2; j <= mask.getHeight() / 2; j++) {
				mask.setPixel(i, j, -1);
			}
		}
		mask.setPixel(0, 0, (pixelAmount - 1));
		return mask;
	}

	public static Mask buildGaussianMask(int size, double sigma) {
		if (size % 2 == 0) {
			size++;
		}
		Mask mask = new Mask(size);
		double total = 0;
		for (int i = -mask.getWidth() / 2; i <= mask.getWidth() / 2; i++) {
			for (int j = -mask.getHeight() / 2; j <= mask.getHeight() / 2; j++) {
				double gaussian = Math.exp(-(i * i + j * j) / (sigma * sigma))
						/ (2 * Math.PI * sigma * sigma);
				mask.setPixel(i, j, gaussian);
				total += gaussian;
			}
		}
		for (int i = -mask.getWidth() / 2; i <= mask.getWidth() / 2; i++) {
			for (int j = -mask.getHeight() / 2; j <= mask.getHeight() / 2; j++) {
				mask.setPixel(i, j, mask.getValue(i, j) / total);
			}
		}

		return mask;
	}

	public static List<Mask> buildPrewittMasks() {
		double[][] dxValues = { { -1, -1, -1 }, { 0, 0, 0 }, { 1, 1, 1 } };
		double[][] dyValues = { { -1, 0, 1 }, { -1, 0, 1 }, { -1, 0, 1 } };
		List<Mask> list = new ArrayList<Mask>();
		list.add(new Mask(dxValues));
		list.add(new Mask(dyValues));
		return list;
	}

	public static List<Mask> buildSobelMasks() {
		double[][] dxValues = { { -1, -2, -1 }, { 0, 0, 0 }, { 1, 2, 1 } };
		double[][] dyValues = { { -1, 0, 1 }, { -2, 0, 2 }, { -1, 0, 1 } };
		List<Mask> list = new ArrayList<Mask>();
		list.add(new Mask(dxValues));
		list.add(new Mask(dyValues));
		return list;
	}

	public static List<Mask> buildRobertsMasks() {
		double[][] dxValues = { { 0, 0, 0 }, { 0, 1, 0 }, { 0, 0, -1 } };
		double[][] dyValues = { { 0, 0, 0 }, { 0, 0, 1 }, { 0, -1, 0 } };
		List<Mask> list = new ArrayList<Mask>();
		list.add(new Mask(dxValues));
		list.add(new Mask(dyValues));
		return list;
	}

	public static List<Mask> buildKirshMasks() {
		List<Mask> list = new ArrayList<Mask>();
		list.add(buildKirshMask(Direction.VERTICAL));
		list.add(buildKirshMask(Direction.HORIZONTAL));
		list.add(buildKirshMask(Direction.DIAGONAL));
		list.add(buildKirshMask(Direction.INVERSE_DIAGONAL));
		return list;
	}

	public static Mask buildKirshMask(Direction d) {
		switch (d) {
		case HORIZONTAL:
			double[][] dValues = { { 5, -3, -3 }, { 5, 0, -3 }, { 5, -3, -3 } };
			return new Mask(dValues);
		case VERTICAL:
			double[][] aValues = { { 5, 5, 5 }, { -3, 0, -3 }, { -3, -3, -3 } };
			return new Mask(aValues);
		case DIAGONAL:
			double[][] bValues = { { -3, 5, 5 }, { -3, 0, 5 }, { -3, -3, -3 } };
			return new Mask(bValues);
		case INVERSE_DIAGONAL:
			double[][] cValues = { { -3, -3, -3 }, { 5, 0, -3 }, { 5, 5, -3 } };
			return new Mask(cValues);
		}
		return null;
	}

	public static List<Mask> buildAllAnOtherMasks() {
		List<Mask> list = new ArrayList<Mask>();
		list.add(buildAnOtherMask(Direction.VERTICAL));
		list.add(buildAnOtherMask(Direction.HORIZONTAL));
		list.add(buildAnOtherMask(Direction.DIAGONAL));
		list.add(buildAnOtherMask(Direction.INVERSE_DIAGONAL));
		return list;
	}
	
	public static Mask buildAnOtherMask(Direction d) {
		switch (d) {
		case HORIZONTAL:
			double[][] dValues = { { 1, 1, -1 }, { 1, -2, -1 }, { 1, 1, -1 } };
			return new Mask(dValues);
		case VERTICAL:
			double[][] aValues = { { 1, 1, 1 }, { 1, -2, 1 }, { -1, -1, -1 } };
			return new Mask(aValues);
		case DIAGONAL:
			double[][] bValues = { { 1, 1, 1 }, { 1, -2, -1 }, { 1, -1, -1 } };
			return new Mask(bValues);
		case INVERSE_DIAGONAL:
			double[][] cValues = { { 1, -1, -1 }, { 1, -2, -1 }, { 1, 1, 1 } };
			return new Mask(cValues);
		}
		return null;
	}

	public static List<Mask> buildAllPrewittMasks() {
		List<Mask> list = new ArrayList<Mask>();
		list.add(buildPrewittMask(Direction.VERTICAL));
		list.add(buildPrewittMask(Direction.HORIZONTAL));
		list.add(buildPrewittMask(Direction.DIAGONAL));
		list.add(buildPrewittMask(Direction.INVERSE_DIAGONAL));
		return list;
	}

	public static Mask buildPrewittMask(Direction d) {
		switch (d) {
		case HORIZONTAL:
			double[][] dValues = { { 1, 0, -1 }, { 1, 0, -1 }, { 1, 0, -1 } };
			return new Mask(dValues);
		case VERTICAL:
			double[][] aValues = { { 1, 1, 1 }, { 0, 0, 0 }, { -1, -1, -1 } };
			return new Mask(aValues);
		case DIAGONAL:
			double[][] bValues = { { 1, 1, 0 }, { 1, 0, -1 }, { 0, -1, -1 } };
			return new Mask(bValues);
		case INVERSE_DIAGONAL:
			double[][] cValues = { { 0, -1, -1 }, { 1, 0, -1 }, { 1, 1, 0 } };
			return new Mask(cValues);
		}
		return null;
	}
	
	public static List<Mask> buildAllSobelMasks() {
		double[][] aValues = { { 1, 2, 1 }, { 0, 0, 0 }, { -1, -2, -1 } };
		double[][] bValues = { { 2, 1, 0 }, { 1, 0, -1 }, { 0, -1, -2 } };
		double[][] cValues = { { 1, 0, -1 }, { 2, 0, -2 }, { 1, 0, -1 } };
		double[][] dValues = { { 0, -1, -2 }, { 1, 0, -1 }, { 2, 1, 0 } };

		List<Mask> list = new ArrayList<Mask>();
		list.add(new Mask(aValues));
		list.add(new Mask(bValues));
		list.add(new Mask(cValues));
		list.add(new Mask(dValues));
		
		return list;
	}

	public static Mask buildSobelMask(Direction d) {
		switch (d) {
		case VERTICAL:
			double[][] dValues = { { 1, 0, -1 }, { 2, 0, -2 }, { 1, 0, -1 } };
			return new Mask(dValues);
		case HORIZONTAL:
			double[][] aValues = { { 1, 2, 1 }, { 0, 0, 0 }, { -1, -2, -1 } };
			return new Mask(aValues);
		case DIAGONAL:
			double[][] bValues = { { 2, 1, 0 }, { 1, 0, -1 }, { 0, -1, -2 } };
			return new Mask(bValues);
		case INVERSE_DIAGONAL:
			double[][] cValues = { { 0, -1, -2 }, { 1, 0, -1 }, { 2, 1, 0 } };
			return new Mask(cValues);
		}
		return null;
	}

	public static Mask buildLaplacianMask() {
		double[][] mask = { { 0, -1, 0 }, { -1, 4, -1 }, { 0, -1, 0 } };
		return new Mask(mask);
	}

	public static Mask buildLogMask(int size, double sigma) {
		if (size % 2 == 0) {
            size++;
        }
		Mask mask = new Mask(size);
		for (int i = -mask.getWidth() / 2; i <= mask.getWidth() / 2; i++) {
			for (int j = -mask.getHeight() / 2; j <= mask.getHeight() / 2; j++) {
				//1/(sqrt(2*pi)*sigma^3)
				double factor = Math.pow(
						Math.sqrt(2 * Math.PI) * Math.pow(sigma, 3), -1);
				//e^(-(x^2+y^2)/(2*sigma^2))
				double exp = -(Math.pow(i, 2) + Math.pow(j, 2))
						/ (2 * Math.pow(sigma, 2));
				//2-((x^2+y^2)/(sigma^2))
				double term = 2 - (Math.pow(i, 2) + Math.pow(j, 2))
						/ Math.pow(sigma, 2);
				double pixelValue = -1 * factor * term * Math.pow(Math.E, exp);
				mask.setPixel(i, j, pixelValue);
			}
		}
		return mask;
	}
	
}
