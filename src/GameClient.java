import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import javafx.application.Application;
import javafx.stage.Stage;

public class GameClient extends Application {
    private Socket client;
    private ClientIn in;
    private ClientOut out;

    /**
     * Sets the name of the client set from the main menu, polls the server, and
     * then if successful, starts the game.
     * @param name the desired name of the player
     */
    public void setName(String name) {
        out.pushName(name);
        if (in.nameGood()) {
            in.setClientName(name);
            in.start();
        };
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        try {
            client = new Socket(GameServer.ADDRESS, GameServer.PORT);
        } catch (UnknownHostException e) {
            System.out.printf(
                "Failed to connect to server at %s:%d: %s\n",
                GameServer.ADDRESS, GameServer.PORT, e.toString()
            );
            return;
        } catch (IOException e) {
            System.out.printf(
                "Failed to connect to game server, an IOException "
                + "occured: %s\n",
                e.toString()
            );
            return;
        }

        GameWindow gui = new GameWindow(this, primaryStage);

        try {
            in = new ClientIn(client, gui);
        } catch (IOException e) {
            System.out.printf(
                "An IOException occured while trying to set up the " + 
                "client's input stream: %s\n", e.toString()
            );
            return;
        }

        try {
            out = new ClientOut(client);
        } catch (IOException e) {
            System.out.printf(
                "An IOException occured while trying to set up the " + 
                "client's output stream: %s\n", e.toString()
            );
            return;
        }

        in.setAssociatedOutput(out);

        in.nameGood();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
