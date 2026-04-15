import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ServerPacket implements Serializable {
    public List<Actor> actors;

    /**
     * A new packet to be sent from the server to the client.
     * @param actors a list of all the eatable objects currently on the map
     */
    public ServerPacket(List<Eatable> actors) {
        this.actors = new ArrayList<>();
        for (Eatable actor : actors) this.actors.add(new Actor(actor));
    }

    enum ActorType {
        COMPUTER,
        PLAYER,
        FOOD
    }

    class Actor implements Serializable {
        public boolean alive;
        public int size;
        public double xPos;
        public double yPos;
        public double dirX;
        public double dirY;
        public String name;
        public ActorType type;

        /**
         * Converts an eatable object on the game map into a serializable
         * format to send to the client.
         * @param actor
         */
        public Actor(Eatable actor) {
            this.size = actor.getSize();
            Position pos = actor.getPos();
            this.xPos = pos.x;
            this.yPos = pos.y;
            this.alive = actor.isAlive();
            if (actor instanceof Tiger) {
                Tiger tiger = (Tiger) actor;
                this.name = tiger.getName();
                Coord2D dir = tiger.getDirection();
                this.dirX = dir.x;
                this.dirY = dir.y;
                if (actor instanceof Player) this.type = ActorType.PLAYER;
                else this.type = ActorType.COMPUTER;
            } else {
                this.name = String.format("%d", actor.hashCode());
                this.type = ActorType.FOOD;
            }
        }
    }
}
