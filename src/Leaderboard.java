import java.util.List;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.util.Collections;
import java.util.Comparator;

/**
 * Leaderboard for tracking top 10 players based on size using lists
 */
public class Leaderboard {
    private Map map;
    private VBox guiBox;
    private Label[] labels;

    /**
     * Constructs a new leaderboard and builds the UI element to contain it.
     * @param map the map to get the tigers from
     */
    public Leaderboard(Map map) {
        this.map = map;

        VBox leaderboard = new VBox(8);
        leaderboard.setPadding(new Insets(15));
        leaderboard.setAlignment(Pos.TOP_RIGHT);
        leaderboard.setMaxWidth(Region.USE_PREF_SIZE);
        leaderboard.setMaxHeight(Region.USE_PREF_SIZE);
        leaderboard.setStyle(
            "-fx-background-color: rgba(0, 0, 0, 0.6);"
            + "-fx-border-radius: 8;"
        );
        StackPane.setAlignment(leaderboard, Pos.TOP_RIGHT);

        this.labels = new Label[5];
        for (int i = 0; i < labels.length; i++) {
            Label slot = new Label(String.format("%d.", i + 1));
            slot.setStyle("-fx-text-fill: white;");
            labels[i] = slot;
            leaderboard.getChildren().add(slot);
        }

        this.guiBox = leaderboard;
    }
    
    /**
     * Gets the leaderboard display.
     * @return the leaderboard display
     */
    public VBox getGuiBox() {
        return this.guiBox;
    }

    /**
     * Gets the top tigers by size.
     * The player will always be at least at the bottom position of the
     * leaderboard
     * @param player the player requesting the leaderboard
     * @return a sorted list containing the top tigers by size
     */
    private String[] getTopTigers(Player player) {
        List<Tiger> sorted = map.getTigers();

        Collections.sort(sorted, new Comparator<Tiger>() {
            @Override
            public int compare(Tiger p1, Tiger p2) {
                return Integer.compare(p2.getSize(), p1.getSize());
            }
        });

        int count = Math.min(labels.length, sorted.size());
        String[] onLeaderboard = new String[count];

        for (int i = 0; i < count; i++) {
            onLeaderboard[i] = String.format(
                "%d. %s - %d", i + 1,
                sorted.get(i).getName(), sorted.get(i).getSize()
            );
        }

        int playerIndex = -1;
        if (sorted.contains(player)) playerIndex = sorted.indexOf(player);
        if (player.isAlive() && playerIndex >= count) {
            onLeaderboard[count - 1] = String.format(
                "%d. %s - %d", playerIndex + 1,
                player.getName(), player.getSize()
            );
        }

        return onLeaderboard;
    }

    /**
     * Updates the leaderboard.
     * @param player the player requesting the leaderboard
     */
    public void update(Player player) {
        String[] onLeaderboard = getTopTigers(player);
        for (int i = 0; i < labels.length; i++) labels[i].setText(
            i < onLeaderboard.length ? onLeaderboard[i] : ""
        );
        guiBox.toFront();
    }
}