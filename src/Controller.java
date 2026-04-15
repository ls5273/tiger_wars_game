import javafx.scene.input.KeyCode;

public class Controller {
    //Fields LOL
    private Coord2D direction = new Coord2D(0, 0);
    private Player player;

    /**
     * Constructs a new controller for a player's tiger.
     * @param player the player tiger to be controlled
     */
    public Controller(Player player) {
        this.player = player;
    }

    /**
     * Gets the tiger player the controller is controlling.
     * @return the tiger player the controller is controlling
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Handles a keydown event.
     * @param code the keycode pressed
     */
    public void keyDown(KeyCode code) {
        switch (code) {
            case UP:
            case W:
                direction.y = -1;
                break;
            case DOWN:
            case S:
                direction.y = 1;
                break;
            case RIGHT:
            case D:
                direction.x = 1;
                break;
            case LEFT:
            case A:
                direction.x = -1;
                break;
            default:
                break;
        }
        player.setDirection(direction);
    }

    /**
     * Handles a key up event.
     * @param code the keycode released
     */
    public void keyUp(KeyCode code) {
        switch (code) {
            case UP:
            case DOWN:
            case W:
            case S:
                direction.y = 0;
                break;
            case RIGHT:
            case LEFT:
            case D:
            case A:
                direction.x = 0;
                break;
            default:
                break;
        }
        player.setDirection(direction);
    }
}
