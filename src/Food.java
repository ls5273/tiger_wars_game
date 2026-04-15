import java.util.Random;

import javafx.scene.image.ImageView;

public class Food extends Eatable {
    private static final int DEFAULT_SIZE = 50;
    private static final String[] FOOD_IMAGES = {
        "meat1.png",
        "meat2.png",
        "meat3.png",
        "meat4.png",
        "meat5.png",
        "meat6.png",
        "meat7.png",
        "meat8.png",
        "meat9.png",
        "meat10.png",
        "meat11.png"
    };

    private static Random RNG = new Random();
    private static int last_id = 0;

    public final int ID;

    /**
     * Creates a food object with a random position.
     */
    public Food() {
        this(Position.random(DEFAULT_SIZE));
    }

    /**
     * Creates a food object with a given position.
     * @param position the position to place the food object at
     */
    public Food(Position position) {
        super(position, DEFAULT_SIZE, new ImageView(
            "file:assets/" + FOOD_IMAGES[RNG.nextInt(FOOD_IMAGES.length)]
        ));
        this.ID = ++last_id;

        ImageView sprite = (ImageView) this.getSprite();
        sprite.setFitHeight(DEFAULT_SIZE);
        sprite.setFitWidth(DEFAULT_SIZE);
    }

    /**
     * Gets a unique hashcode for the food item based on the food ID.
     * @return a unique hashcode for the food item based on the food ID
     */
    @Override
    public int hashCode() {
        return ID;
    }
}
