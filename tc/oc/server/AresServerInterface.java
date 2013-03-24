package tc.oc.server;

public interface AresServerInterface {

    //TODO: Add Java doc comments

    public String getServer();

    public int getPort();

    public String getServerMOTD();

    public String getServerPlayers();

    public String getServerVersion();

    public void pollServer();

    public String getPing();
}
