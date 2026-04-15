public class Coord2D {
    double x;
    double y;

    /**
     * Creates a new set of two dimensional coordinates.
     * @param x the x coordinate
     * @param y the y coordinate
     */
    public Coord2D(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Returns a string representation of the coordinates.
     * @return a string representation of the coordinates (x, y)
     */
    @Override
    public String toString() {
        return String.format("(%.0f, %.0f)", x, y);
    }
}
