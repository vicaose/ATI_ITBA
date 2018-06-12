package application.utils;

import static application.utils.BasicImageUtils.paintN8;
import static application.utils.BasicImageUtils.paintRedN8;

import java.util.Vector;

import mpi.cbg.fly.Feature;
import mpi.cbg.fly.PointMatch;
import mpi.cbg.fly.SIFT;
import domain.Image;

public class SiftUtils {

	public static Image sift(Image image) {
		Vector<Feature> f1 = SIFT.getFeatures(image.getBufferedImage());
		paintFeatures(image, f1);
		return image;
	}

	public static Image[] sift(Image imageA, Image imageB) {
		Vector<Feature> fs1 = SIFT.getFeatures(imageA.getBufferedImage());
		Vector<Feature> fs2 = SIFT.getFeatures(imageB.getBufferedImage());
		Image image1 = imageA.clone();
		Image image2 = imageB.clone();

		Vector<PointMatch> matches = SIFT
				.createMatches(fs1, fs2, 1.5f, null, 1);
		for (PointMatch match : matches) {
			int[] color = { (int) (Math.random() * 255),
					(int) (Math.random() * 255), (int) (Math.random() * 255) };
			paintN8(image1, (int) match.getP1().getL()[0], (int) match.getP1()
					.getL()[1], color[0], color[1], color[2]);
			paintN8(image2, (int) match.getP2().getL()[0], (int) match.getP2()
					.getL()[1], color[0], color[1], color[2]);
		}
		System.out.println((double) matches.size()
				/ Math.min(fs1.size(), fs2.size()) * 100 + "%");
		paintFeatures(imageA, fs1);
		// paintFeatures(b, f2);
		Image[] pair = { image1, image2 };
		return pair;
	}

	private static void paintFeatures(Image image, Vector<Feature> features) {
		for (Feature feature : features) {
			paintRedN8(image, (int) feature.location[0],
					(int) feature.location[1]);
		}

	}
}
