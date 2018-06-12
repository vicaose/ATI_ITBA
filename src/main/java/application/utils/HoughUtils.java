package application.utils;

import static application.utils.BasicImageUtils.paintRed;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jfree.data.Range;

import domain.Image;
import domain.SynthetizationType;
import domain.mask.MaskFactory;

public class HoughUtils {

	public static Image houghLineDetector(Image original, double epsilon,
			double threshold) {
		// Apply border detector
		Image borderImage = MaskUtils.applyMasks(original,
				MaskFactory.buildSobelMasks(), SynthetizationType.ABS);
		borderImage = ThresholdUtils.global(borderImage, Image.MAX_VAL / 2, 1);

		double D = Math.max(borderImage.getWidth(), borderImage.getHeight());
		Range roRange = new Range(-Math.sqrt(2) * D, Math.sqrt(2) * D);
		Range thetaRange = new Range(-90, 90);
		int roSize = (int) (Math.abs(roRange.getLength()));
		int thetaSize = (int) (Math.abs(thetaRange.getLength()));
		int[][] A = new int[roSize][thetaSize];

		// Step 3
		for (int x = 0; x < borderImage.getWidth(); x++) {
			for (int y = 0; y < borderImage.getHeight(); y++) {
				if (isWhite(borderImage, x, y)) {
					// Iterates theta (j) from 1 to m
					for (int theta = 0; theta < thetaSize; theta++) {
						double thetaValue = thetaRange.getLowerBound() + theta;
						double thetaTerm = x
								* Math.cos(thetaValue * Math.PI / 180) - y
								* Math.sin(thetaValue * Math.PI / 180);

						// Iterates ro (i) from 1 to n
						for (int ro = 0; ro < roSize; ro++) {
							double roValue = roRange.getLowerBound() + ro;
							double total = roValue - thetaTerm;
							// If verifies the normal equation of the line, add
							// 1 to the acumulator
							// Step 4
							if (Math.abs(total) < epsilon) {
								// The maximum values from this vector, gives
								// the most voted positions.
								A[ro][theta] += 1;
							}
						}
					}
				}
			}
		}

		// Step 5
		Set<BucketForLines> allBuckets = new HashSet<BucketForLines>();
		for (int ro = 0; ro < roSize; ro++) {
			for (int theta = 0; theta < thetaSize; theta++) {
				BucketForLines newBucket = new BucketForLines(ro, theta,
						A[ro][theta]);
				allBuckets.add(newBucket);
			}
		}

		// Generates a descending sorted list.
		List<BucketForLines> allBucketsAsList = new ArrayList<BucketForLines>(
				allBuckets);
		Collections.sort(allBucketsAsList);

		Image houghed = original.clone();
		// Gets the max vote number
		int maxVotes = allBucketsAsList.get(0).votes;
		if (maxVotes > 1) {
			for (BucketForLines b : allBucketsAsList) {

				// Only for those with max votes
				if (b.votes < maxVotes * threshold) {
					break;
				}

				double roValue = roRange.getLowerBound() + b.ro;
				double thetaValue = thetaRange.getLowerBound() + b.theta;

				for (int x = 0; x < borderImage.getWidth(); x++) {
					for (int y = 0; y < borderImage.getHeight(); y++) {
						double thetaTerm = x
								* Math.cos(thetaValue * Math.PI / 180) - y
								* Math.sin(thetaValue * Math.PI / 180);
						double total = roValue - thetaTerm;
						// Step 6
						if (Math.abs(total) < epsilon) {
							paintRed(houghed, x, y);
						}
					}
				}

			}
		}

		return houghed;

	}

	public static Image houghCircleDetector(Image original, double epsilon,
			double threshold, int rMin, int rMax) {
		// Apply border detector
		Image borderImage = MaskUtils.applyMasks(original,
				MaskFactory.buildSobelMasks(), SynthetizationType.ABS);
		borderImage = ThresholdUtils.global(borderImage, Image.MAX_VAL / 2, 1);

		Range aRange = new Range(rMin, borderImage.getWidth() - rMin);
		Range bRange = new Range(rMin, borderImage.getHeight() - rMin);
		Range rRange = new Range(rMin, rMax);

		int aSize = (int) (Math.abs(aRange.getLength()));
		int bSize = (int) (Math.abs(bRange.getLength()));
		int rSize = (int) (Math.abs(rRange.getLength()));
		int[][][] A = new int[aSize][bSize][rSize];

		for (int r = 0; r < rSize; r += 2) {
			double rValue = rRange.getLowerBound() + r;
			double rTerm = Math.pow(rValue, 2);
			for (int a = 0; a < aSize; a += 2) {
				double aValue = aRange.getLowerBound() + a;
				for (int b = 0; b < bSize; b += 2) {
					double bValue = bRange.getLowerBound() + b;
					for (int x = 0; x < borderImage.getWidth(); x += 2) {
						double aTerm = Math.pow(x - aValue, 2);
						for (int y = 0; y < borderImage.getHeight(); y += 2) {
							if (isWhite(borderImage, x, y)) {
								double bTerm = Math.pow(y - bValue, 2);
								double total = rTerm - aTerm - bTerm;
								if (Math.abs(total) < epsilon) {
									A[a][b][r] += 1;
								}
							}
						}
					}
				}
			}
		}

		System.out.println("Voted");

		Set<BucketForCircles> allBuckets = new HashSet<BucketForCircles>();
		for (int a = 0; a < aSize; a += 2) {
			for (int b = 0; b < bSize; b += 2) {
				for (int r = 0; r < rSize; r += 2) {
					if (A[a][b][r] > 0) {
						BucketForCircles newBucket = new BucketForCircles(a, b,
								r, A[a][b][r]);
						allBuckets.add(newBucket);
					}
				}
			}
		}
		Image houghed = original.clone();
		if (allBuckets.isEmpty()) {
			System.out.println("Empty Buckets");
			return houghed;
		}

		List<BucketForCircles> allBucketsAsList = new ArrayList<BucketForCircles>(
				allBuckets);
		Collections.sort(allBucketsAsList);

		int maxHits = allBucketsAsList.get(0).votes;

		System.out.println("maxHits:" + maxHits);

		if (maxHits > 2)
			for (BucketForCircles b : allBucketsAsList) {
				if (b.votes < maxHits * threshold) {
					break;
				}

				int aValue = rMin + b.a;

				int bValue = (int) bRange.getLowerBound() + b.b;
				int rValue = (int) rRange.getLowerBound() + b.r;

				System.out.println("Circle: (" + aValue + "," + bValue + ","
						+ rValue + ")");

				drawCircle(houghed, aValue, bValue, rValue);

			}

		return houghed;
	}

	private static boolean isWhite(Image image, int x, int y) {
		return image.getGraylevelFromPixel(x, y) == Image.MAX_VAL;
	}
	
	private static void drawCircle(Image image, int x0, int y0, int radius) {
	  int error = 1 - radius;
	  int errorY = 1;
	  int errorX = -2 * radius;
	  int x = radius, y = 0;
	 
	  paintRed(image, x0, y0 + radius);
	  paintRed(image, x0, y0 - radius);
	  paintRed(image, x0 + radius, y0);
	  paintRed(image, x0 - radius, y0);
	 
	  while(y < x)
	  {
	    if(error > 0) // >= 0 produces a slimmer circle. =0 produces the circle picture at radius 11 above
	    {
	      x--;
	      errorX += 2;
	      error += errorX;
	    }
	    y++;
	    errorY += 2;
	    error += errorY;    
	    paintRed(image, x0 + x, y0 + y);
	    paintRed(image, x0 - x, y0 + y);
	    paintRed(image, x0 + x, y0 - y);
	    paintRed(image, x0 - x, y0 - y);
	    paintRed(image, x0 + y, y0 + x);
	    paintRed(image, x0 - y, y0 + x);
	    paintRed(image, x0 + y, y0 - x);
	    paintRed(image, x0 - y, y0 - x);
	  }
	}
}
