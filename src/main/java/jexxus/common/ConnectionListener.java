package jexxus.common;

import jexxus.server.ServerConnection;

public interface ConnectionListener {

    void connectionBroken(Connection broken, boolean forced);

    void receive(byte[] data, Connection from);

    void clientConnected(ServerConnection conn);

}
