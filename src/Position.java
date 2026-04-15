import java.util.Random;

public class Position extends Coord2D {
    private static Coord2D mapSize;
    private static Random RNG = new Random();

    /**
     * Creates a new point in a 2D space.
     * @param x the x coordinate
     * @param y the y coordinate
     */
    public Position(double x, double y) {
        super(x, y);
    }

    /**
     * Gets the distance between two positions.
     * @param p1 the first position
     * @param p2 the second position
     * @return the distance between the two positions
     */
    public static double distance(Position p1, Position p2) {
        double distanceX = p1.x - p2.x;
        double distanceY = p1.y - p2.y;

        return Math.sqrt(distanceX * distanceX + distanceY * distanceY);
    }

    /**
     * Get the distance from another position to this one
     * @param other the position to get distance to
     * @return distance from this position to another
     */
    public double getDistance(Position other) {
        return distance(this, other);
    }

    /**
     * Returns a random position on the map.
     * @return a random position on the map
     */
    public static Position random() {
        return Position.random(0);
    }

    /**
     * Returns a random position on the map.
     * @param size the size of the object to place on the map
     * @return a random position on the map
     */
    public static Position random(int size) {
        return new Position(
            RNG.nextInt(
                ((int) Math.floor(mapSize.x)) - size * 2
            ) + size - mapSize.x / 2,
            RNG.nextInt(
                ((int) Math.floor(mapSize.y)) - size * 2
            ) + size - mapSize.y / 2
        );
    }
    
    /**
     * Sets the static reference to the map's size.
     * @param mapSize the map's size (in pixels)
     */
    public static void setMapSize(Coord2D mapSize) {
        Position.mapSize = mapSize;
    }
}
