import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class Name {
    private String name;
    private static Set<String> inUse = new HashSet<>();
    private static final String[] adjectives = {
        "Fierce", "Wild", "Zesty", "Bold", "Sneaky", "Drippy", "Clever", "Sus",
        "Skibidi", "Yappy", "Crazy", "Chill", "Silent", "Lurking", "Rocky",
        "Loud", "Techy", "Funky", "Bombastic"
    };

    private static final String[] nouns = {
        "Tiger", "Furball", "Meowster", "Tiggy", "Meowgician", "Fluffinator",
        "Fuzzlord", "Roarer", "Chunk", "Ball", "Chonk"
    };

    private static final Random random = new Random();


    /**
     * Creates a new name with the given name.
     * @param name the provided name
     */
    public Name(String name) {
        setName(name);
    }

    /**
     * Creates a new default-assigned name.
     */
    public Name() {
        String generatedName = Name.random();
        this.name= generatedName;
        inUse.add(generatedName);
    }

    /**
     * Checks if a name is already in use.
     * @param name the name to check
     * @return whether the name is in use
     */
    public static boolean isInUse(String name) {
        return inUse.contains(name);
    }

    /**
     * Generates a random unique name for a tiger.
     * @return the generated name in the format AdjectiveNounXX
     */
    public static String random() {
        String newName;
        do { 
            newName = String.format(
                "%s%s%02d",
                adjectives[random.nextInt(adjectives.length)],
                nouns[random.nextInt(nouns.length)],
                random.nextInt(99) + 1
            ); 
        } while (inUse.contains(newName));
        return newName;
    }

    /**
     * Sets the name.
     * @param name the name to be set
     * @throws IllegalArgumentException if the name is empty
     * @throws IllegalStateException if the name is already in use
     */
    public void setName(String name) {
        if (name== null|| name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty.");
        }

        String trimmedName= name.trim();
        if (inUse.contains(trimmedName)) {
            throw new IllegalStateException("Name already in use.");
        }

        this.name = trimmedName;
        inUse.add(trimmedName);
    }

    /**
     * Returns a string representation of the name.
     * @return the name as a string
     */
    @Override
    public String toString() {
        return name;
    }

    /**
     * Mark a name as in use.
     * @param name the name to mark as in use
     */
    public static void use(String name) {
        inUse.add(name);
    }
}