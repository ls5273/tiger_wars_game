import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javafx.scene.image.Image;

public abstract class Map {
    private Image background;
    private Coord2D backgroundSize;
    private Set<Tiger> tigers = new HashSet<Tiger>();
    private Set<Food> food = new HashSet<Food>();

    /**
     * Creates a new game map.
     * Also serves as a "main" method for spawning food and computer players.
     * @param imagePath the image to use as the background
     */
    public Map(String imagePath) {
        background = new Image(
            imagePath, false
        );
        backgroundSize = new Coord2D(
            (int) Math.floor(background.getWidth()),
            (int) Math.floor(background.getHeight())
        );
        Position.setMapSize(backgroundSize);
    }

    /**
     * Adds a new food item to the map.
     * @param food the food to add to the map
     */
    public void addFood(Food food) {
        this.food.add(food);
    }

    /**
     * Adds a new tiger to the map.
     * @param tiger the tiger to add to the map
     */
    public void addTiger(Tiger tiger) {
        tigers.add(tiger);
    }

    public void cleanup(Eatable obj) {
        new Thread() {
            @Override
            public void run() {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    System.out.printf(
                        "An InterruptedException occured while waiting to "
                        + "clean up a map object: %s\n", e
                    );
                }
                if (obj instanceof Tiger) tigers.remove(obj);
                else food.remove(obj);
            }
        }.start();
    }

    /**
     * Gets all actors currently on the map.
     * @return all actors currently on the map
     */
    public List<Eatable> getActors() {
        List<Eatable> actors = new ArrayList<>();
        actors.addAll(this.food);
        actors.addAll(this.tigers);
        return actors;
    }

    /**
     * Gets the background image for the map.
     * @return the background image for the map
     */
    public Image getBackground() {
        return background;
    }

    /**
     * Gets all the computer-controlled tigers currently on the game map.
     * @return a list of all the computer tigers currently on the game map
     */
    public List<Computer> getComputers() {
        List<Computer> computers = new ArrayList<>();
        tigers.stream().filter((tiger) -> (tiger instanceof Computer)).forEach(
            (tiger) -> computers.add((Computer) tiger)
        );
        return computers;
    }

    /**
     * Gets all the tigers currently on the game map.
     * @return a list of all the tigers currently on the game map
     */
    public List<Tiger> getTigers() {
        return new ArrayList<>(this.tigers);
    }

    /**
     * Gets the background size of the map.
     * @return the background size of the map;
     */
    public Coord2D getSize() {
        return backgroundSize;
    }
}
