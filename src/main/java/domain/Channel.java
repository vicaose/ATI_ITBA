package domain;

public class Channel implements Cloneable {

	static final int MIN_VAL = 0;
	static final int MAX_VAL = 255;

	private int width;
	private int height;

	// The matrix is represented by an array, and to get a pixel(x,y) make y *
	// this.getWidth() + x
	private double[] channel;

	public Channel(int width, int height) {
		if (width <= 0 || height <= 0) {
			throw new IllegalArgumentException(
					"Images must have at least 1x1 pixel size");
		}
		this.width = width;
		this.height = height;
		this.channel = new double[width * height];
	}

	public boolean validPixel(int x, int y) {
		return y >= 0 && y < height && x >= 0 && x < width;
	}

	public int getHeight() {
		return height;
	}

	public int getWidth() {
		return width;
	}

	public void setPixel(int x, int y, double val) {
		if (!validPixel(x, y)) {
			throw new IndexOutOfBoundsException(x + "," + y + ", tamanio es "
					+ width + ", " + height);
		}
		channel[y * width + x] = val;
	}

	public double getPixel(int x, int y) {
		if (!validPixel(x, y)) {
			throw new IndexOutOfBoundsException(x + "," + y + ", tamanio es "
					+ width + ", " + height);
		}
		return channel[y * width + x];
	}

	int truncatePixel(double originalValue) {
		if (originalValue > MAX_VAL) {
			return MAX_VAL;
		} else if (originalValue < MIN_VAL) {
			return MIN_VAL;
		}
		return (int) originalValue;
	}

	@Override
	public Channel clone() {
		Channel newChannel = new Channel(width, height);
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				newChannel.setPixel(i, j, this.getPixel(i, j));
			}
		}
		return newChannel;
	}
}
