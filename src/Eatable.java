import java.util.Random;

import javafx.scene.Node;

public abstract class Eatable {
    private static final int MIN_RESPAWN_TIME = 10000;
    private static final int MAX_RESPAWN_TIME = 120000;

    private static Random RNG = new Random();

    private boolean alive = true;
    private Position position;
    private int size;
    private static Map map;
    private Node sprite;

    /**
     * Constructs an Eatable object.
     * @param pos the initial position of the object in the map's 2D coordinate
     * space
     * @param size the size of the object
     */
    public Eatable(Position pos, int size, Node sprite) {
        this.position = pos;
        this.size = size;
        this.sprite = sprite;
    }

    /**
     * Determines if this Object can be eaten by a Tiger.
     * For the Tiger to be able to eat the Object, the Tiger must be 10% larger
     * than the Object.
     * @param eater the Tiger attempting to eat the Object
     * @return if this Object can be eaten by the Tiger
     */
    public boolean canEat(Tiger eater) {
        return this.alive && eater.getSize() > this.size * 1.1;
    }

    /**
     * Marks the object as dead/eaten.
     * If the object is a Computer or Food, respawns the object.
     */
    public void kill() {
        if (!this.isAlive()) return;
        this.alive = false;

        if (map == null) return;
        
        if (
            map instanceof MapServer
            && (this instanceof Computer || this instanceof Food)
        ) {
            String respawnType = this instanceof Computer ? "Computer" : "Food";
            new Thread() {
                public void run() {
                    try {
                        Thread.sleep(
                            RNG.nextInt(MAX_RESPAWN_TIME - MIN_RESPAWN_TIME)
                            + MIN_RESPAWN_TIME
                        );
                    } catch (InterruptedException e) {
                        System.out.printf(
                            "An InterruptedException occured while waiting to "
                            + "spawn a new %s object: %s", respawnType, e
                        );
                    }

                    System.out.printf(
                        "Spawning new %s object\n", respawnType
                    );
                    if (respawnType == "Computer") map.addTiger(new Computer()); 
                    else map.addFood(new Food());
                };
            }.start();
        }
    }

    /**
     * Gets the position of the Object.
     * @return the position of the Object
     */
    public Position getPos() {
        return this.position;
    }

    /**
     * Gets the size of the Object.
     * @return the size of the Object
     */
    public int getSize() {
        return this.size;
    }

    /**
     * Gets the sprite of the object.
     * @return the sprite of the object
     */
    public Node getSprite() {
        return sprite;
    }

    /**
     * Gets the game map in use.
     * @return the game map in use
     */
    public Map getMap() {
        return Eatable.map;
    }

    /**
     * Checks if the eatable object has not been eaten yet.
     * @return if the eatable object has not been eaten yet
     */
    public boolean isAlive() {
        return alive;
    }

    /**
     * Sets the game map for all Eatable objects.
     * @param map the game map to use
     */
    public static void setMap(Map map) {
        Eatable.map = map;
    }

    /**
     * Sets the new position of the object.
     * @param pos the new position of the object
     */
    public void setPos(Position pos) {
        this.position = pos;
    }

    /**
     * Sets the new size of the object.
     * @param size the new size of the object
     */
    public void setSize(int size) {
        this.size = size;
    }
}
