package domain.tracking;

import java.awt.Point;

import domain.Image;
import domain.Image.ChannelType;

public abstract class Tita {

	private double[][] values;
	private double[][] velocities;
	protected double[] innerSum = new double[3];
	protected double[] outerSum = new double[3];
	protected int innerSize = 0;
	protected int outerSize = 0;
	protected Image image;
	// private Mask mask;
	protected Frontier frontier;
	protected boolean end;

	public Tita(Image image, Frontier frontier) {
		values = new double[image.getWidth()][image.getHeight()];
		velocities = new double[image.getWidth()][image.getHeight()];
		this.frontier = frontier;
		// this.mask = mask;
		// setImage(image);
		this.image = image;
	}

	public double getValue(Point p) {
		return getValue(p.x, p.y);
	}

	public double getValue(int x, int y) {
		return values[x][y];
	}

	public void setValue(int x, int y, int i) {
		if (i != -1 && i != -3 && i != 3 && i != 1) {
			System.err.println(i);
			throw new IllegalStateException();
		}
		if (values[x][y] == 3 && i != 3) {
			// lo saco del outer
			removeFromOuter(x, y);
		} else if (values[x][y] == -3 && i != -3) {
			// lo saco del inner
			removeFromInner(x, y);
		} else if (i == 3 && values[x][y] != 3) {
			// lo agrego al outer
			addToOuter(x, y);
		} else if (i == -3 && values[x][y] != -3) {
			// lo agrego al inner
			addToInner(x, y);
		}
		values[x][y] = i;
	}

	public void setValue(Point p, int i) {
		setValue(p.x, p.y, i);
	}

	public int getWidth() {
		return values.length;
	}

	public int getHeight() {
		return values[0].length;
	}

	public double averageInner(ChannelType channel) {
		return innerSum[getAvgIndex(channel)] / innerSize;
	}

	public double averageOuter(ChannelType channel) {
		return outerSum[getAvgIndex(channel)] / outerSize;
	}

	public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
		checkEnded();
		recalculateAvgs();
		// this.image = MaskUtils.applyMask(image, mask);;
	}

	public boolean checkEnded() {
		end = calculateVelocities();
		return end;
	}

	public boolean isEnded() {
		return end;
	}

	public double velocity(Point p) {
		return velocities[p.x][p.y];
	}

	private boolean calculateVelocities() {
		boolean end1 = true, end2 = true;
		for (Point p : frontier.innerBorder) {
			velocities[p.x][p.y] = calculateVelocity(p);
			if (end1 && velocities[p.x][p.y] < 0) {
				end1 = false;
			}
		}
		for (Point p : frontier.outerBorder) {
			velocities[p.x][p.y] = calculateVelocity(p);
			if (end2 && velocities[p.x][p.y] > 0) {
				end2 = false;
			}
		}
		// System.out.println(end1 + " " + end2);
		return end1 && end2;
	}

	protected abstract int getAvgIndex(ChannelType c);

	protected abstract void removeFromOuter(int x, int y);

	protected abstract void removeFromInner(int x, int y);

	protected abstract void addToOuter(int x, int y);

	protected abstract void addToInner(int x, int y);

	protected abstract double calculateVelocity(Point p);

	protected abstract void recalculateAvgs();
}
