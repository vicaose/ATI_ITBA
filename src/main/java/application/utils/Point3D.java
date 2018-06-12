package application.utils;

public class Point3D {

    /** x-coordinate of the point */
    public double x;

    /** y-coordinate of the point */
    public double y;

    /** z-coordinate of the point */
    public double z;

    /**
     * Required by ProActive
     */
    public Point3D() {
    }

    /**
     * Creation of a new Point3D
     * @param a x-coordinate of the point
     * @param b y-coordinate of the point
     * @param c z-coordinate of the point
     */
    public Point3D(double a, double b, double c) {
        x = a;
        y = b;
        z = c;
    }

    /** For displaying a Point3D */
    @Override
    public String toString() {
        return x + ", " + y + ", " + z;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }
}
