public class Player extends Tiger {
    private Tiger killer;

    /**
     * Constructs a new player-controlled tiger with a provided name.
     * @param name the provided name of the player's tiger
     */
    public Player(Name name) {
        super(name);
    }

    /**
     * Creates a new player-controlled Tiger with a given name, position and
     * size.
     * Used primarily on the client, when loading a Tiger from the server.
     * @param name the name of the tiger
     * @param pos the position of the tiger on the game map
     * @param size the size of the tiger
     */
    public Player(String name, Position pos, int size) {
        super(new Name(name), pos, size);
    }

    /**
     * Gets the tiger that killed the player.
     * @return the tiger that killed the player
     */
    public Tiger getKiller() {
        return this.killer;
    }

    /**
     * Sets the tiger that killed the player.
     * @param killer the tiger that killed the player
     */
    public void setKiller(Tiger killer) {
        this.killer = killer;
    }
}
