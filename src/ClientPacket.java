import java.io.Serializable;

public class ClientPacket implements Serializable {
    public double posX;
    public double posY;
    public double dirX;
    public double dirY;

    /**
     * A new packet to be sent from the client to the server.
     * @param player the player tiger the client is controlling
     */
    public ClientPacket(Player player) {
        Position pos = player.getPos();
        this.posX = pos.x;
        this.posY = pos.y;
        Coord2D dir = player.getDirection();
        this.dirX = dir.x;
        this.dirY = dir.y;
    }
}
