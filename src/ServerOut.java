import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ServerOut extends Thread {
    private Socket client;
    private ObjectOutputStream out;

    private MapServer map;
    private Player player;

    /**
     * Creates a new thread to handle the server output stream for an individual
     * client.
     * @param client the client socket to output to
     * @param map the game map in use on the serverside
     * @throws IOException if the server fails to get an output stream to the
     * client
     */
    public ServerOut(Socket client, MapServer map) throws IOException {
        this.client = client;
        this.map = map;
        this.out = new ObjectOutputStream(this.client.getOutputStream());
    }

    /**
     * Polls the client for a name.
     * @param msg the message to send to the client if an error occured
     * @throws IOException if an IOException occurs trying to poll the client
     */
    public void pollName(String msg) throws IOException {
        out.writeObject(msg);
        out.flush();
    }

    /**
     * Sets the player object associated with the client.
     * @param player the player object associated with the client
     */
    public void setPlayer(Player player) {
        this.player = player;
        System.out.printf(
            "Bound client to Player \"%s\"\n", player.getName()
        );
        this.start();
    }

    @Override
    public void run() {
        System.out.printf(
            "Beginning server-client communication with client %s\n",
            player.getName()
        );
        while (player.isAlive()) {
            try {
                out.writeObject(new ServerPacket(map.getActors()));
                out.flush();
            } catch (IOException e) {
                System.out.printf(
                    "An IOException occured attempting to send a packet "
                    + "to the client %s: %s\n", player.getName(), e
                );
            }
            try {
                Thread.sleep(60);
            } catch (InterruptedException e) {
                System.out.printf(
                    "Thread.sleep was interrupted for the server-client "
                    + "communication with %s: %s\n", player.getName(), e
                );
            }
        }
        try {
            out.close();
        } catch (IOException e) {
            System.out.printf(
                "An error occured trying to close the output stream "
                + "to %s: %s\n", player, e
            );
        }
    }
}
