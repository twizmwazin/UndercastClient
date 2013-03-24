package tc.oc.server;

public class AresServer implements AresServerInterface {

    private Ares_ThreadPollServers server;

    /**
     * Default constructor
     * Creates a Thread Poll for the server
     *
     * @param server String server IP
     * @param port   int port for the server
     */
    public AresServer(String server, int port) {
        this.server = new Ares_ThreadPollServers(server, port);
    }

    /**
     * Runs the Thread to poll the server
     */
    public void pollServer() {
        server.run();
    }

    @Override
    public String getServer() {
        return server.serverIP;
    }

    @Override
    public String getServerMOTD() {
        return server.MOTD;
    }

    @Override
    public String getServerPlayers() {
        return server.populationInfo;
    }

    @Override
    public String getServerVersion() {
        return server.gameVersion;
    }

    @Override
    public String getPing() {
        return server.pingToServer;
    }

    @Override
    public int getPort() {
        return server.port;
    }

}
