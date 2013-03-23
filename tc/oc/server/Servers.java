package tc.oc.server;

public class Servers implements ServerInterface {

    private Ares_ThreadPollServers server;

    public Servers(String server) {
        this.server = new Ares_ThreadPollServers(server, 25565);
        try {
            this.server.pollServer(server, 25565);
        } catch (Exception e) {
        }
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
}
