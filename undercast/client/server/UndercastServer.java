package undercast.client.server;

import undercast.client.UndercastData.MatchState;
import undercast.client.UndercastData.ServerType;

public class UndercastServer {

    public String name;
    public String currentMap;
    public String nextMap;
    public int playerCount;
    public ServerType type;
    public MatchState matchState;

    /**
     * Default constructor
     */
    public UndercastServer() {
        name = "";
        currentMap = "";
        nextMap = "";
        type = ServerType.Unknown;
        matchState = MatchState.Unknown;
    }

    public String getServerName() {
        return name;
    }

    public String getCurrentMap() {
        return currentMap;
    }

    public String getNextMap() {
        return nextMap;
    }

    public int getPlayerCount() {
        return playerCount;
    }

    public ServerType getServerType() {
        return type;
    }

    public MatchState getMatchState() {
        return matchState;
    }
}
