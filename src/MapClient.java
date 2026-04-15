import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import javafx.scene.layout.StackPane;

public class MapClient extends Map {
    private HashMap<Integer, Food> loadedFood = new HashMap<>();
    private HashMap<String, Tiger> loadedTigers = new HashMap<>();

    /**
     * Constructs a new client-side copy of the game map.
     * @param imagePath the image to use as the background
     */
    public MapClient(String imagePath) {
        super(imagePath);
    }

    /**
     * Gets the actors currently visible on the screen.
     * Will also hide and display the sprites accordingly.
     * @param center the current center point of the visible screen
     * @param boundaries the screen boundaries
     * @param pane the StackPane to display actors on
     * @param offScreen a set of the actors currently off screen
     * @param onScreen a set of the actors currently on screen
     * @return the actors currently visible on the screen
     */
    public List<Eatable> getActors(
        Position center, Coord2D boundaries, StackPane pane,
        Set<Eatable> offScreen, Set<Eatable> onScreen
    ) {
        List<Eatable> actors = getActors();

        List<Eatable> display = new ArrayList<>();
        for (Eatable actor : actors) {
            Position pos = actor.getPos();

            // Padding ensures partial images (<50%) on screen will still load
            int padding = actor.getSize() / 2;
            if (
                actor.isAlive() &&
                Math.abs(pos.x - center.x) < boundaries.x + padding
                && Math.abs(pos.y - center.y) < boundaries.y + padding
            ) {
                display.add(actor);
                if (!onScreen.contains(actor)) {
                    onScreen.add(actor);
                    offScreen.remove(actor);
                    pane.getChildren().add(actor.getSprite());
                }
            } else if (!offScreen.contains(actor)) {
                onScreen.remove(actor);
                offScreen.add(actor);
                pane.getChildren().remove(actor.getSprite());
            }
        }
        return display;
    }

    /**
     * Returns a player tiger for a given name.
     * @param name the name of the tiger
     * @return the player tiger
     */
    public Player getPlayer(String name) {
        if (!loadedTigers.containsKey(name)) return null;
        Tiger tiger = loadedTigers.get(name);
        if (!(tiger instanceof Player)) return null;
        return (Player) tiger;
    }

    /**
     * Creates a new object to represent an actor received from the server.
     * @param actor the actor received from the server
     */
    public Eatable load(ServerPacket.Actor actor) {
        switch (actor.type) {
            case COMPUTER:
                Computer tiger = new Computer(
                    actor.name, new Position(actor.xPos, actor.yPos), actor.size
                );
                super.addTiger(tiger);
                loadedTigers.put(actor.name, tiger);

                System.out.printf(
                    "Loaded new computer tiger \"%s\"!\n", actor.name
                );
                return tiger;
            case PLAYER:
                Player player = new Player(
                    actor.name, new Position(actor.xPos, actor.yPos), actor.size
                );
                super.addTiger(player);
                loadedTigers.put(actor.name, player);
                System.out.printf(
                    "Loaded new player tiger \"%s\"!\n", actor.name
                );
                return player;
            default:
                Food food = new Food(new Position(actor.xPos, actor.yPos));
                super.addFood(food);
                loadedFood.put(food.hashCode(), food);
                System.out.printf(
                    "Loaded new food object with ID %s!\n", actor.name
                );
                return food;
        }
    }

    /**
     * Checks if an actor received from the server is loaded on the map.
     * @param actor the actor received from the server
     * @return if the actor is loaded on the map
     */
    public boolean loaded(ServerPacket.Actor actor) {
        if (actor.type == ServerPacket.ActorType.FOOD)
            return loadedFood.containsKey(Integer.parseInt(actor.name));
        else return loadedTigers.containsKey(actor.name);
    }

    public void updateActor(ServerPacket.Actor actor) {
        updateActor(actor, false);
    }

    public void updateActor(ServerPacket.Actor actor, boolean clientOwnership) {
        switch (actor.type) {
            case COMPUTER:
            case PLAYER:
                Tiger tiger = loadedTigers.get(actor.name);
                if (!actor.alive) {
                    tiger.kill();
                    break;
                }
                if (!clientOwnership) tiger.update(
                    new Position(actor.xPos, actor.yPos),
                    new Coord2D(actor.dirX, actor.dirY)
                );
                tiger.setSize(actor.size);
                break;
            default:
                Food food = loadedFood.get(Integer.parseInt(actor.name));
                if (!actor.alive) {
                    food.kill();
                }
                break;
        }
    }
}
