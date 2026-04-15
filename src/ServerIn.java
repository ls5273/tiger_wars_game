import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

public class ServerIn extends Thread {
    private Socket client;
    private ServerOut associatedOutput;
    private Player player;
    private MapServer map;

    /**
     * Creates a new thread to handle the server input stream for an individual
     * client.
     * @param client the client socket to get input from
     * @param map the game map in use on the serverside
     */
    public ServerIn(Socket client, MapServer map) {
        this.client = client;
        this.map = map;
        this.start();
    }

    /**
     * Sets the associated output thread for the client.
     * @param out the associated output thread for the client.
     */
    public void setAssociatedOutput(ServerOut out) {
        this.associatedOutput = out;
    }

    @Override
    public void run() {
        ObjectInputStream in;
        try {
            in = new ObjectInputStream(client.getInputStream());
        } catch (IOException e) {
            System.out.printf(
                "Error getting client input stream: %s\n", e.toString()
            );
            return;
        }

        String errorMsg = "";
        while (true) {
            if (!errorMsg.equals("")) System.out.println(errorMsg);

            String name;
            try {
                associatedOutput.pollName(errorMsg);
            } catch (IOException e) {
                if (e.toString().contains("Connection reset")) break;
                System.out.printf(
                    "An IOException occured while trying to poll the client "
                    + "for a name: %s\n", e
                );
            }
            try {
                name = (String) in.readObject();
            } catch (ClassNotFoundException e) {
                errorMsg = "Name must be a string!";
                continue;
            } catch (IOException e) {
                errorMsg = String.format(
                    "Error getting name: %s", e.toString()
                );
                continue;
            }

            System.out.println(name);
            try {
                player = new Player(new Name(name));
                map.addTiger(player);
            } catch(IllegalArgumentException iae) {
                errorMsg = "Error assigning name: Name cannot be empty.";
                continue;
            } catch(IllegalStateException ise) {
                errorMsg = String.format(
                    "Error assigning name: "
                    + "The name \"%s\" is already in use.", name
                );
                continue;
            }
            break;
        }

        if (player != null) {
            associatedOutput.setPlayer(player);

            while(player.isAlive()) {
                ClientPacket packet;
                try {
                    Object fromServer = in.readObject();
                    packet = (ClientPacket) fromServer;
                } catch (ClassNotFoundException e) {
                    System.out.printf(
                        "A ClassNotFoundException occured while trying to "
                        + "get a packet from the client %s: %s\n", player, e
                    );
                    continue;
                } catch (IOException e) {
                    if (e.toString().contains("Connection reset")) {
                        System.out.printf(
                            "%s has left the game\n", player
                        );
                        break;
                    }
                    System.out.printf(
                        "An IOException occured while trying to get a packet "
                        + "from the client %s: %s\n", player, e
                    );
                    continue;
                }

                player.update(
                    new Position(packet.posX, packet.posY),
                    new Coord2D(packet.dirX, packet.dirY)
                );
            }
            player.kill();
            try {
                in.close();
            } catch (IOException e) {
                System.out.printf(
                    "An error occured trying to close the input stream "
                    + "from %s: %s\n", player, e
                );
            }
            try {
                client.close();
            } catch (IOException e) {
                System.out.printf(
                    "An error occured trying to close the socket with "
                    + "%s: %s\\n",
                    player, e
                );
            }
        }
    }
}
