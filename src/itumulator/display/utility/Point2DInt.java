package itumulator.display.utility;

/**
 * Utility class to produce points 
 */
public class Point2DInt {
    private int x, y;

    public Point2DInt(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    /**
     * Interpolates between two pixel point 2d with rounding
     * @param other the other point to interpolate to
     * @param progress from 0.0 to 1.0
     * @return a new Point2DInt with the interpolated coordinates
     */
    public Point2DInt interpolate(Point2DInt other, double progress){
        double newX = this.x + (progress * (other.x - this.x));
        double newY = this.y + (progress * (other.y - this.y));
        return new Point2DInt((int)newX, (int)newY);
    }
}