package application.utils;

public class BucketForCircles implements Comparable<BucketForCircles> {
	int a;
	int b;
	int r;
	int votes;

	public BucketForCircles(int a, int b, int r, int votes) {
		this.a = a;
		this.b = b;
		this.r = r;
		this.votes = votes;
	}

	@Override
	public boolean equals(Object obj) {
		boolean equalA = a == ((BucketForCircles) obj).a;
		boolean equalB = b == ((BucketForCircles) obj).b;
		boolean equalR = r == ((BucketForCircles) obj).r;
		return equalA && equalB && equalR;
	}

	@Override
	public int hashCode() {
		return (int) (3 * a + 5 * b + 7 * r);
	}

	@Override
	public int compareTo(BucketForCircles obj) {
		return obj.votes - votes;
	}

}