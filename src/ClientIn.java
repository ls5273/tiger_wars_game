import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

import javafx.application.Platform;

public class ClientIn extends Thread {
    private Socket client;
    private ClientOut associatedOutput;
    private MapClient map;
    private ObjectInputStream in;
    private String clientName;
    private GameWindow gui;
    private Player player;

    /**
     * Creates a new thread to handle the client input stream from the server.
     * @param client the server socket to get input from
     * @param gui the client's GUI
     * @throws IOException if the client fails to get an input stream from the
     * server
     */
    public ClientIn(Socket client, GameWindow gui) throws IOException {
        this.client = client;
        this.gui = gui;
        this.map = gui.getMap();
        this.in = new ObjectInputStream(client.getInputStream());
    }

    /**
     * Determines whether the name sent to the server was valid.
     * @return if the name sent to the server was valid
     */
    public boolean nameGood() {
        Object response;
        try {
            response = in.readObject();
            if (response instanceof String) return false;
            return true;
        } catch (ClassNotFoundException e) {
            System.out.printf(
                "A ClassNotFoundException occured while analyzing the "
                + "server's polling for a name: %s\n", e
            );
        } catch (IOException e) {
            System.out.printf(
                "An IOException occured while analyzing the "
                + "server's polling for a name: %s\n", e
            );
        }
        return false;
    }

    /**
     * Sets the associated output thread for the client.
     * @param out the associated output thread for the client.
     */
    public void setAssociatedOutput(ClientOut out) {
        this.associatedOutput = out;
    }

    /**
     * Sets the name of the player tiger the client is using.
     * @param clientName the name of the player tiger the client is using
     */
    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    @Override
    public void run() {
        System.out.println("Waiting for packets from server...");
        while (player == null || player.isAlive()) {
            ServerPacket packet;
            try {
                Object fromServer = in.readObject();
                if (fromServer instanceof String) {
                    System.out.println(fromServer);
                    continue;
                }
                packet = (ServerPacket) fromServer;
            } catch (ClassNotFoundException e) {
                System.out.printf(
                    "A ClassNotFoundException occured while trying to "
                    + "get a packet from the server: %s\n", e
                );
                continue;
            } catch (IOException e) {
                System.out.printf(
                    "An IOException occured while trying to get a packet from "
                    + "the server: %s\n", e
                );
                if (e.toString().contains("Connection reset")) break;
                continue;
            }
            
            if (this.player == null) {
                this.player = map.getPlayer(clientName);
                if (this.player != null) {
                    associatedOutput.setPlayer(this.player);
                }
            }
            
            for (ServerPacket.Actor actor : packet.actors) {
                if (!map.loaded(actor)) {
                    Eatable mapActor = map.load(actor);
                    if (
                        actor.name.equals(clientName)
                        && actor.type == ServerPacket.ActorType.PLAYER
                    ) Platform.runLater(() -> gui.startGame((Player) mapActor));
                } else {
                    if (
                        actor.type == ServerPacket.ActorType.PLAYER
                        && actor.name.equals(clientName)
                    ) map.updateActor(actor, true);
                    else map.updateActor(actor);
                }
            }
        }
        try {
            client.close();
        } catch (IOException e) {
            System.out.printf(
                "An error occured trying to close the socket with the "
                + "server: %s\\n",
                e
            );
        }
    }
}
