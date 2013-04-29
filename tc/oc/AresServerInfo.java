package tc.oc;

/**
 * @author Flv92
 */
public class AresServerInfo {

    public enum ServerType {

        LOBBY, BLITZ, OCTC
    };
    private String serverName;
    private String serverMap;
    private String serverNextMap;
    private ServerType serverType;

    public AresServerInfo(String name, String map, String nextMap, ServerType serverType) {
        this.serverName = name;
        this.serverMap = map;
        this.serverNextMap = nextMap;
        this.serverType = serverType;
    }

    /**
     * @return the serverName
     */
    public String getServerName() {
        return serverName;
    }

    /**
     * @return the serverMap
     */
    public String getServerMap() {
        return serverMap;
    }

    /**
     * @return the serverNextMap
     */
    public String getServerNextMap() {
        return serverNextMap;
    }

    /**
     * @return the serverType
     */
    public ServerType getServerType() {
        return serverType;
    }

    /**
     * @param serverName the serverName to set
     */
    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    /**
     * @param serverMap the serverMap to set
     */
    public void setServerMap(String serverMap) {
        this.serverMap = serverMap;
    }

    /**
     * @param serverNextMap the serverNextMap to set
     */
    public void setServerNextMap(String serverNextMap) {
        this.serverNextMap = serverNextMap;
    }

    /**
     * @param serverType the serverType to set
     */
    public void setServerType(ServerType serverType) {
        this.serverType = serverType;
    }
}
