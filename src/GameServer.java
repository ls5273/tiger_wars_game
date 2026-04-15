import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import javafx.application.Application;
import javafx.stage.Stage;

public class GameServer extends Application implements Runnable {
    public static final String ADDRESS = "localhost";
    public static final int PORT = 54321;
    public static final String BACKGROUND_IMAGE = "file:assets/junglemap.jpg";

    private ServerSocket server;
    private boolean running = true;
    private MapServer map;

    @Override
    public void start(Stage primaryStage) throws Exception {
        try {
            server = new ServerSocket(PORT);
            new Thread(this).start();
        } catch (IOException ioe) {
            System.out.printf(
                "Failed to start game server on port %d: %s\n",
                PORT, ioe.toString()
            );
        }

        this.map = new MapServer(BACKGROUND_IMAGE);
        Eatable.setMap(this.map);
    }

    @Override
    public void run() {
        while (running) {
            Socket client;
            try {
                System.out.printf(
                    "Waiting for client to connect on port %d\n",
                    PORT
                );
                client = server.accept();
            } catch(IOException ioe) {
                System.out.printf(
                    "An IOException occured while trying to connect to "
                    + " a client on port %d: %s\n",
                    PORT, ioe.toString()
                );
                continue;
            }

            ServerOut out;
            try {
                out = new ServerOut(client, map);
            } catch (IOException e) {
                System.out.printf(
                    "An IOException occured while trying to set up the " + 
                    "server's output stream: %s\n", e.toString()
                );
                return;
            }

            ServerIn in = new ServerIn(client, map);
            in.setAssociatedOutput(out);
            
            System.out.printf(
                "Client connected on port %s, waiting for name.\n",
                PORT
            );
        }
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}
