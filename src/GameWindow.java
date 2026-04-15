import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javafx.scene.control.Alert;

import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class GameWindow {
    private static final String GAME_NAME = "Tiger Wars";
    private static final Coord2D DEFAULT_SCREEN_SIZE = new Coord2D(800, 400);

    private MapClient map;
    private Scene scene;
    private StackPane gamePane;
    private MainMenu mainMenu;
    private ImageView background;
    private boolean playerAlive = true;
    private Stage stage;
    private Leaderboard leaderboard;

    private Controller controller;
    private AnimationTimer timer;
    private Position centerPoint = new Position(0, 0);
    private long lastUpdate;

    private Set<Eatable> offScreen = new HashSet<>();
    private Set<Eatable> onScreen = new HashSet<>();

    /**
     * Creates a new game window for the client.
     * @param client the client launching the game window
     * @param primaryStage the primary Stage to display the GUI on
     */
    public GameWindow(GameClient client, Stage primaryStage) {
        this.stage = primaryStage;
        primaryStage.setTitle(GAME_NAME);
        gamePane = new StackPane();
        map = new MapClient(GameServer.BACKGROUND_IMAGE);

        mainMenu = new MainMenu(client);

        scene = new Scene(
            mainMenu.getGUI(), DEFAULT_SCREEN_SIZE.x, DEFAULT_SCREEN_SIZE.y
        );
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.setOnCloseRequest(event -> System.exit(0));
    }

    /**
     * Gets the map being used by the client.
     * @return the map being used by the client
     */
    public MapClient getMap() {
        return map;
    }

    public void startGame(Player player) {
        scene = new Scene(
            gamePane, DEFAULT_SCREEN_SIZE.x, DEFAULT_SCREEN_SIZE.y
        );
        stage.setScene(scene);
        stage.show();

        background = new ImageView(map.getBackground());
        gamePane.getChildren().add(background);

        controller = new Controller(player);
        onScreen.add(player);
        gamePane.getChildren().add(player.getSprite());
        Eatable.setMap(map);

        leaderboard = new Leaderboard(map);
        gamePane.getChildren().add(leaderboard.getGuiBox());

        scene.setOnKeyPressed(event -> controller.keyDown(event.getCode()));
        scene.setOnKeyReleased(event -> controller.keyUp(event.getCode()));

        lastUpdate = System.currentTimeMillis();
        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                center(player);
                background.setTranslateX(-centerPoint.x);
                background.setTranslateY(-centerPoint.y);

                List<Eatable> actors = map.getActors(centerPoint, new Coord2D(
                    (int) Math.floor(scene.getWidth() / 2),
                    (int) Math.floor(scene.getHeight() / 2)
                ), gamePane, offScreen, onScreen);

                onScreen.removeIf(actor -> !actor.isAlive());
                offScreen.removeIf(actor -> !actor.isAlive());
                //Anja: removing dead actors
                for (Eatable actor : actors) {
                    if (!actor.isAlive()) continue;

                    Position pos = actor.getPos();
                    actor.getSprite().setTranslateX(pos.x - centerPoint.x);
                    actor.getSprite().setTranslateY(pos.y - centerPoint.y);
                }

                if (player.isAlive()) {
                    player.update(System.currentTimeMillis() - lastUpdate);
                    lastUpdate = System.currentTimeMillis();
                }
                else if (playerAlive) {
                    playerAlive = false;
                    gameOver(player.getKiller().getName());
                }
                leaderboard.update(player);
            }
        };
        timer.start();
    }

    /**
     * Centers the game window's view on a specified tiger.
     * @param focus the tiger to center the view on
     */
    public void center(Tiger focus) {
        Coord2D mapSize = map.getSize();

        Coord2D boundaries = new Coord2D(
            (int) Math.floor((mapSize.x - scene.getWidth()) / 2),
            (int) Math.floor((mapSize.y - scene.getHeight()) / 2)
        );

        focus.getSprite().toFront();

        Position focusPos = focus.getPos();
        focusPos = new Position(focusPos.x, focusPos.y);
        if (focusPos.x < -boundaries.x)
            focusPos.x -= focusPos.x + boundaries.x;
        else if (focusPos.x > boundaries.x)
            focusPos.x -= focusPos.x - boundaries.x;

        if (focusPos.y < -boundaries.y)
            focusPos.y -= focusPos.y + boundaries.y;
        else if (focusPos.y > boundaries.y)
            focusPos.y -= focusPos.y - boundaries.y;

        centerPoint = focusPos;
    }

    public void gameOver(String killerName) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Game Over");
            alert.setHeaderText(null);
            alert.setContentText("You were eaten by " + killerName + "!");
            alert.showAndWait();
            System.exit(0);
        });
    }
}
