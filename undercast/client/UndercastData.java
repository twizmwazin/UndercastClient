package undercast.client;
//You may not release this source under any condition, it must be linked to this page
//You may recompile and publish as long as skipperguy12 and Guru_Fraser are given credit
//You may not claim this to be your own
//You may not remove these comments

import java.net.URL;
import java.util.HashSet;
import undercast.client.internetTools.InformationLoaderThread;
import undercast.client.internetTools.ServerStatusHTMLParser;
import undercast.client.server.UndercastServer;

public class UndercastData {
    //Data Varibles

    public static String map;
    public static String nextMap;
    public static double kills;
    public static double deaths;
    public static double killed;
    public static int killstreak;
    public static int largestKillstreak;
    // redudant assignation but kept for being java 6 compatible
    public static HashSet<String> friends = new HashSet<String>();
    public static String server;
    public static Teams team;
    public static boolean isOC = false;
    public static boolean isLobby;
    public static boolean update;
    public static String updateLink;
    private static InformationLoaderThread mapLoader;
    private static boolean mapLoaderFinished;
    public static UndercastServer[] serverInformation;
    public static UndercastServer[] sortedServerInformation;
    public static int serverCount;
    // if it's true, the /server comand isn't executed after a "Welcome to Overcast Network" message
    public static boolean welcomeMessageExpected = true;
    public static boolean redirect = false;
    public static String directionServer;
    public static boolean guiShowing;
    public static boolean isGameOver = false;
    public static int playTimeHours;
    public static int playTimeMin;
    public static int sortIndex;
    // saves if a /server command (without argument) was executed, if it's false, the user executed it 
    public static boolean serverDetectionCommandExecuted = false;
    public static boolean isNextKillFirstBlood = false;
    public static boolean isLastKillFromPlayer = false;
    public static String latestVersion;
    public static int matchTimeSec;
    public static int matchTimeMin;
    public static int matchTimeHours;
    public static boolean incrementMatchTime;
    public static MatchTimer matchTimer;

    public static enum Teams {

        Red, Blue, Purple, Cyan, Lime, Yellow, Green, Orange, Cot, Bot, Observers, Unknown
    };

    public static enum MatchState {

        Starting, Started, Finished, Waiting, Lobby, Unknown
    };

    public static enum ServerType {

        Lobby, Blitz, OvercastNetwork, Unknown
    };
    public static String[] sortNames = {"Web", "Match", "Players", "Abc"};

    public UndercastData() {
        update = true;
        setMap("Fetching...");
        resetKills();
        resetDeaths();
        resetKilled();
        resetLargestKillstreak();
        setTeam(Teams.Observers);
        guiShowing = true;
        mapLoaderFinished = false;
        serverInformation = new UndercastServer[30];
        serverCount = 0;
        for (int c = 0; c < serverInformation.length; c++) {
            serverInformation[c] = new UndercastServer();
        }
        sortedServerInformation = new UndercastServer[30];
        for (int c = 0; c < sortedServerInformation.length; c++) {
            sortedServerInformation[c] = new UndercastServer();
        }
        sortIndex = 0;
        try {
            mapLoader = new InformationLoaderThread(new URL("https://oc.tc/play"));
        } catch (Exception e) {
            System.out.println("[UndercastMod]: Failed to load maps");
            System.out.println("[UndercastMod]: ERROR: " + e.toString());
        }
    }

    public static void update() {
        if (!mapLoaderFinished && mapLoader.getContents() != null) {
            mapLoaderFinished = true;
            try {
                String[][] mapData = ServerStatusHTMLParser.parse(mapLoader.getContents());
                serverCount = mapData.length - 1; //-1 for lobby
                for (int c = 0; c < mapData.length; c++) {
                    serverInformation[c].name = mapData[c][0];
                    try {
                        serverInformation[c].playerCount = Integer.parseInt(mapData[c][1]);
                    } catch (Exception e) {
                        serverInformation[c].playerCount = -1;
                    }
                    serverInformation[c].currentMap = mapData[c][2];
                    serverInformation[c].nextMap = mapData[c][3];
                    serverInformation[c].matchState = MatchState.Started; //API support
                    serverInformation[c].type = ServerType.Unknown;
                }

                // set the map
                for (int c = 0; c < serverInformation.length; c++) {
                    if (serverInformation[c].getServerName() == null) {
                        serverCount = c - 1;
                        break;
                    }
                    if (serverInformation[c].name.replace(" ", "").equalsIgnoreCase(server)) { // that space in the server name has taken me a lot of time
                        map = serverInformation[c].currentMap;
                        nextMap = serverInformation[c].nextMap;
                    }
                }
                UndercastCustomMethods.sortServers();
            } catch (Exception e) {
                System.out.println("[UndercastMod]: Failed to parse maps");
                System.out.println("[UndercastMod]: ERROR: " + e.toString());
            }
        }
    }

    public static void reload() {
        map = "Loading...";
        nextMap = "Loading...";

        try {
            mapLoader = new InformationLoaderThread(new URL("https://oc.tc/play"));
        } catch (Exception e) {
            System.out.println("[UndercastMod]: Failed to load maps");
            System.out.println("[UndercastMod]: ERROR: " + e.toString());
        }
        mapLoaderFinished = false;
    }

    public static void addKills(double d) {
        kills += d;
    }

    public static void resetKills() {
        kills = 0;
    }

    public static double getKills() {
        return kills;
    }

    public static void addDeaths(double d) {
        deaths += d;
    }

    public static void resetDeaths() {
        deaths = 0;
    }

    public static double getDeaths() {
        return deaths;
    }

    public static void addKilled(double d) {
        killed += d;
    }

    public static void resetKilled() {
        killed = 0;
    }

    public static double getKilled() {
        return killed;
    }

    public static void addKillstreak(int i) {
        killstreak += i;
        if (largestKillstreak < killstreak) {
            largestKillstreak = killstreak;
        }
    }

    public static void resetKillstreak() {
        killstreak = 0;
    }

    public static double getKillstreak() {
        return killstreak;
    }

    public static void resetLargestKillstreak() {
        largestKillstreak = 0;
    }

    public static double getLargestKillstreak() {
        return largestKillstreak;
    }

    public static int getFriends() {
        return friends.size();
    }

    public static void addFriend(String s) {
        friends.add(s);
    }

    public static void removeFriend(String s) {
        friends.remove(s);
    }

    public static void clearFriends() {
        friends.clear();
    }

    public static boolean isFriend(String s) {
        return friends.contains(s);
    }

    public static boolean isPlayingOvercastNetwork() {
        return isOC;
    }

    public static Teams getTeam() {
        return team;
    }

    public static void setTeam(Teams teams) {
        team = teams;
    }

    public static String getMap() {
        return map;
    }

    public static void setMap(String maps) {
        map = maps;
    }

    public static String getNextMap() {
        return nextMap;
    }

    public static void setServer(String servers) {
        server = servers;
        reload();
    }

    public static String getServer() {
        return server;
    }

    public static boolean isUpdate() {
        return update;
    }

    public static void setUpdate(boolean update) {
        UndercastData.update = update;
    }

    public static String getUpdateLink() {
        return updateLink;
    }

    public static void setUpdateLink(String updateLink) {
        UndercastData.updateLink = updateLink;
    }

    public static void resetMatchTime() {
        matchTimeHours = 0;
        matchTimeMin = 0;
        matchTimeSec = 0;
    }
}
