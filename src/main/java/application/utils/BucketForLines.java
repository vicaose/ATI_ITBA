package application.utils;

public class BucketForLines implements Comparable<BucketForLines> {
	double ro;
	double theta;
	int votes;

	public BucketForLines(double ro, double theta, int votes) {
		this.ro = ro;
		this.theta = theta;
		this.votes = votes;
	}

	@Override
	public boolean equals(Object obj) {
		return ro == ((BucketForLines) obj).ro
				&& theta == ((BucketForLines) obj).theta;
	}

	@Override
	public int hashCode() {
		return (int) (3 * ro + 5 * theta);
	}

	@Override
	public int compareTo(BucketForLines obj) {
		return obj.votes - votes;
	}

}