import java.util.List;

import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.transform.Rotate;

public abstract class Tiger extends Eatable {
    public static final double PIXELS_PER_SECOND = 32;
    public static final int BASE_SIZE = 100;

    private Coord2D direction = new Coord2D(0, 0);
    private Name name;
    private int tigersEaten = 0;
    private Rotate rotate = new Rotate(0); 

    /**
     * Creates a new tiger with a given name.
     * @param name the name of the tiger
     */
    public Tiger(Name name) {
        this(name, Position.random(BASE_SIZE), BASE_SIZE);
    }

    /**
     * Creates a new tiger with a given name, position, and size.
     * @param name the name of the tiger
     * @param pos the position of the tiger on the game map
     * @param size the size of the tiger
     */
    public Tiger(Name name, Position pos, int size) {
        super(
            pos, size, new Group(
                new ImageView("file:assets/tiger.png") {{
                    setFitWidth(32);
                    setFitHeight(32);
                    setTranslateX(-size / 2);
                    setTranslateY(-size / 2);
                    setId("tiger");
                }},
                new Label(name.toString()) {{
                    setStyle(
                        "-fx-text-fill: white;"
                        + "-fx-font-size: 10px;"
                        + "-fx-background-color: rgba(0,0,0,0.5);"
                    );
                    setTranslateY(-((size + 1) / 2));
                    setTranslateX(-name.toString().length() / 2);
                    setId("nametag");
                }}
            )
        );
        this.name = name;

        Group sprite = (Group) this.getSprite();
        ImageView tigerIcon = (ImageView) sprite.lookup("#tiger");
        tigerIcon.getTransforms().add(rotate);

        tigerIcon.fitWidthProperty().addListener((obs, oldVal, newVal) -> {
            rotate.setPivotX(newVal.doubleValue() / 2);
        });
        tigerIcon.fitHeightProperty().addListener((obs, oldVal, newVal) -> {
            rotate.setPivotY(newVal.doubleValue() / 2);
        });
    }

    /**
     * Checks collisions with other eatable objects nearby, and if there are
     * collisions, eats the eatable object.
     * @param close
     */
    public void checkCollisions(List<Eatable> close) {
        Position pos = this.getPos();
        int size = this.getSize() / 2;

        for (Eatable actor : close) {
            if (actor == this) continue;
            if (
                pos.getDistance(actor.getPos()) < size + actor.getSize() / 4
            ) this.eat(actor);
        }
    }

    /**
     * Gets the number of tigers this tiger has eaten.
     * @return the number of tigers this tiger has eaten
     */
    public int getTigersEaten(){
        return tigersEaten;
    }

    /**
     * Eats an eatable object on the map, and increases the tiger's size.
     * @param eating the object to eat
     */
    public void eat(Eatable eating) {
        if (!eating.canEat(this)) return;

        int size = this.getSize() + eating.getSize() / 3;
        this.setSize(size);

        if (eating instanceof Tiger) tigersEaten++;

        eating.kill();
        if (eating instanceof Player) ((Player) eating).setKiller(this);
    }

    /**
     * Gets the name of the tiger.
     * @return the name of the tiger
     */
    public String getName() {
        return name.toString();
    }

    /**
     * Gets the current direction of the tiger.
     * @return the direction vector
     */
    public Coord2D getDirection() {
        return direction;
    }

    /**
     * Gets movement speed of the tiger.
     * @return speed of movement
     */
    public double getSpeed() {
        return Math.max(
            PIXELS_PER_SECOND / 2,
            PIXELS_PER_SECOND - (getSize() - BASE_SIZE) / 10
        );
    }

    /**
     * Gets a unique hashcode for the tiger based on the name.
     * @return a unique hashcode for the tiger based on the name
     */
    @Override
    public int hashCode() {
        return this.name.toString().hashCode();
    }

    /**
     * Sets the direction vector of the tiger.
     * @param dir the new direction vector of the tiger
     */
    public void setDirection(Coord2D dir) {
        direction = dir;
    }

    /**
     * Updates the tiger's position using its direction vector.
     * @param deltaTime the amount of time elapsed since the last movement
     * @throws IllegalStateException if the tiger is not on the map
     */
    public void update(long deltaTime) {
        if (this.getMap() == null) throw new IllegalStateException(
            String.format(
                "Tiger %s is not on the game map!", this.getName()
            )
        );

        Coord2D mapSize = this.getMap().getSize();
        Coord2D mapBoundaries = new Coord2D(
            mapSize.x / 2 - this.getSize() / 2,
            mapSize.y / 2 - this.getSize() / 2
        );
        double step = deltaTime / 1000.0;
        double speed = getSpeed();
        this.getPos().x += direction.x * speed * step;
        if (this.getPos().x < -mapBoundaries.x)
            this.getPos().x = -mapBoundaries.x;
        else if (this.getPos().x > mapBoundaries.x)
            this.getPos().x = mapBoundaries.x;

        this.getPos().y += direction.y * speed * step;
        if (this.getPos().y < -mapBoundaries.y)
            this.getPos().y = -mapBoundaries.y;
        else if (this.getPos().y > mapBoundaries.y)
            this.getPos().y = mapBoundaries.y;
        
        updateDisplay();
    }

    /**
     * Manually updates the position of a tiger.
     * Ignores map boundaries, should only be used when replicating data from
     * client-server.
     * @param pos the position of the tiger
     * @param dir the direction of the tiger
     */
    public void update(Position pos, Coord2D dir) {
        if (getMap() == null) return;

        this.getPos().x = pos.x;
        this.getPos().y = pos.y;
        this.getDirection().x = dir.x;
        this.getDirection().y = dir.y;
        this.checkCollisions(getMap().getActors());
        updateDisplay();
    }

    /**
     * Updates the tiger's nametag and rotation.
     */
    public void updateDisplay() {
        Group sprite = (Group) this.getSprite();
        int size = this.getSize();
        Label nametag = (Label) sprite.lookup("#nametag");
        nametag.setTranslateY(-((size + 1) / 2));
        nametag.setTranslateX(
            -nametag.getBoundsInLocal().getWidth() / 2 + (size - BASE_SIZE) / 2
        );

        if (!(direction.x == 0 && direction.y == 0)) rotate.setAngle(
            Math.toDegrees(Math.atan2(direction.y, direction.x)) - 90
        );
    }

    /**
     * Returns a string representation of the tiger object.
     * @return a string representation of the tiger object
     */
    @Override
    public String toString() {
        return name.toString();
    }

    @Override
    public void setSize(int size) {
        super.setSize(size);

        Group sprite = (Group) this.getSprite();
        ImageView tigerIcon = (ImageView) sprite.lookup("#tiger");
        tigerIcon.setFitHeight(size);
        tigerIcon.setFitWidth(size);
    }
}
