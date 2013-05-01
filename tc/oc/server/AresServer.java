package tc.oc.server;

import tc.oc.AresData;
import tc.oc.AresData.ServerType;
import tc.oc.AresData.MatchState;;

public class AresServer {
    public String name;
    public String currentMap;
    public String nextMap;
    public int playerCount;
    public ServerType type;
    public MatchState matchState;
    /**
     * Default constructor
     */
    public AresServer() {
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
