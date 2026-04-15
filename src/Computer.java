import java.util.List;
import java.util.Random;

public class Computer extends Tiger {
    //Fileds SLAY
    private static final Random RANDOM = new Random();
    private static final int DIRECTION_CHANGE_INTERVAL = 60;
    private static final int SEEK_FOOD_RANGE = 300;
    private static final int FLEE_RANGE = 200;
    private int directionChangeCounter = 0;

    /**
     * Creates a new AI Tiger with a random name.
     */
    public Computer () {
        super(new Name());
    }

    /**
     * Creates a new computer-controlled Tiger with a given name, position and
     * size.
     * Used primarily on the client, when loading a Tiger from the server.
     * @param name the name of the tiger
     * @param pos the position of the tiger on the game map
     * @param size the size of the tiger
     */
    public Computer(String name, Position pos, int size) {
        super(new Name(name), pos, size);
    }

    /**
     * Updates the AI tiger's position and behavior.
     * @param deltaTime the amount of time elapsed since the last movement
     */
    @Override
    public void update(long deltaTime) {
        List<Eatable> nearbyActors = getMap().getActors();
        checkCollisions(nearbyActors);
        
        // Check for threats (bigger tigers) to flee from
        Tiger threat = findThreat(nearbyActors);
        if (threat != null) {
            fleeFrom(threat);
        } 

        // Check for food to seek
        else if (getSize() < 150) { // Only seek food if not too big
            Food food = findFood(nearbyActors);
            if (food != null) {
                seekFood(food);
            } else {
                wander();
            }
        } 

        // Check for prey (smaller tigers) to chase
        else {
            Tiger prey = findPrey(nearbyActors);
            if (prey != null) {
                chase(prey);
            } else {
                wander();
            }
        }

        super.update(deltaTime);
    }

    /**
     * Finds larger tigers nearby.
     * @param actors List of nearby actors
     * @return The nearest tiger or null
     */
    private Tiger findThreat(List<Eatable> actors) {
        for (Eatable actor : actors) {
            if (actor instanceof Tiger && actor != this) {
                Tiger other = (Tiger) actor;
                if (other.getSize() > getSize() * 1.1 && 
                    getPos().getDistance(other.getPos()) < FLEE_RANGE) {
                    return other;
                }
            }
        }
        return null;
    }

    /**
     * Finds food nearby.
     * @param actors List of nearby actors
     * @return The nearest food or null
     */
    private Food findFood(List<Eatable> actors) {
        Food closestFood = null;
        double minDistance = Double.MAX_VALUE;
        
        for (Eatable actor : actors) {
            if (actor instanceof Food && actor.isAlive()) {
                double dist = getPos().getDistance(actor.getPos());
                if (dist < minDistance && dist < SEEK_FOOD_RANGE) {
                    minDistance = dist;
                    closestFood = (Food) actor;
                }
            }
        }
        return closestFood;
    }

    /**
     * Finds smaller tiger nearby.
     * @param actors List of nearby actors
     * @return The nearest prey or null
     */
    private Tiger findPrey(List<Eatable> actors) {
        for (Eatable actor : actors) {
            if (actor instanceof Tiger && actor != this) {
                Tiger other = (Tiger) actor;
                if (other.getSize() < getSize() * 0.9) {
                    return other;
                }
            }
        }
        return null;
    }
    
    /**
     * Sets movement to run from a threat
     * @param threat The tiger to run from
     */
    private void fleeFrom(Tiger threat) {
        Position threatPos = threat.getPos();
        Position myPos = getPos();
        
        setDirection(new Coord2D(
            (myPos.x < threatPos.x) ? -1 : 1,
            (myPos.y < threatPos.y) ? -1 : 1
        ));
    }
    
    /**
     * Sets movement direction towards a food source.
     * @param food The food object to seek
     */
    private void seekFood(Food food) {
        Position foodPos = food.getPos();
        Position myPos = getPos();
        
        setDirection(new Coord2D(
            (foodPos.x > myPos.x) ? 1 : -1,
            (foodPos.y > myPos.y) ? 1 : -1
        ));
    }
    
    /**
     * Sets movement direction towards a prey
     * @param prey The tiger to chase
     */
    private void chase(Tiger prey) {
        Position preyPos = prey.getPos();
        Position myPos = getPos();
        
        setDirection(new Coord2D(
            (preyPos.x > myPos.x) ? 1 : -1,
            (preyPos.y > myPos.y) ? 1 : -1
        ));
    }
    
    /**
     * Random movement behavior
     */
    private void wander() {
        if (directionChangeCounter++ >= DIRECTION_CHANGE_INTERVAL) {
            setDirection(new Coord2D(
                RANDOM.nextInt(3) - 1, 
                RANDOM.nextInt(3) - 1  
            ));
            
            if (getDirection().x == 0 && getDirection().y == 0) {
                setDirection(new Coord2D(
                    RANDOM.nextBoolean() ? 1 : -1,
                    0
                ));
            }
            
            directionChangeCounter = 0;
        }
    }
}
