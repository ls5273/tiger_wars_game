import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientOut extends Thread {
    private ObjectOutputStream out;
    private Player player;

    /**
     * Creates a new thread to handle the client's output stream to the server.
     * @param client the server socket to output to
     * @throws IOException if the client fails to get an output stream to the
     * server
     */
    public ClientOut(Socket client) throws IOException {
        out = new ObjectOutputStream(client.getOutputStream());
    }

    /**
     * Sets the name of the client set from the main menu, polls the server, and
     * then if successful, starts the game.
     * @param name the desired name of the player
     */
    public void pushName(String name) {
        try {
            out.writeObject(name);
            out.flush();
        } catch (IOException e) {
            System.out.printf(
                "An IOException occured while trying to send the name " +
                "\"%s\" to the server: %s\n", name, e.toString()
            );
        }
    }

    /**
     * Sets the client's player object.
     * @param player the client's player tiger
     */
    public void setPlayer(Player player) {
        this.player = player;
        this.start();
    }

    @Override
    public void run() {
        System.out.printf(
            "Starting client-server communication for player %s\n",
            player.getName()
        );
        while (player.isAlive()) {
            try {
                out.writeObject(new ClientPacket(player));
                out.flush();
            } catch (IOException e) {
                if (e.toString().contains("Socket closed")) break;
                System.out.printf(
                    "An IOException occured while trying to send a packet to " 
                    + "the server: %s\n", e
                );
            }

            try {
                Thread.sleep(60);
            } catch (InterruptedException e) {
                System.out.printf(
                    "Thread.sleep was interrupted for the server-client "
                    + "communication: %s\n", e
                );
            }
        }
    }
}
