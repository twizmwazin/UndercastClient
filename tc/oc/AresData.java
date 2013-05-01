package tc.oc;
//You may not release this source under any condition, it must be linked to this page
//You may recompile and publish as long as skipperguy12 and Guru_Fraser are given credit
//You may not claim this to be your own
//You may not remove these comments

import org.lwjgl.input.Keyboard;

import tc.oc.internetTools.InformationLoaderThread;
import tc.oc.internetTools.ServerStatusHTMLParser;
import tc.oc.server.AresServer;

import java.net.URL;
import java.util.HashSet;
import net.minecraft.client.settings.KeyBinding;

public class AresData {
    //Data Varibles

    public static String map;
    public static String nextMap;
    public static double kills;
    public static double deaths;
    public static double killed;
    public static int killstreak;
    public static int largestKillstreak;
    public static HashSet<String> friends = new HashSet<String>();
    public static String server;
    public static Teams team;
    public static boolean isPA;
    public static boolean isLobby;
    public static boolean update;
    public static String updateLink;
    private static InformationLoaderThread mapLoader;
    private static boolean mapLoaderFinished;
    public static AresServer[] serverInformation;
    public static int serverCount;
    // if it's true, the /server comand isn't executed after a "Welcome to Project Ares" message
    public static boolean welcomeMessageExpected = true;
    public static boolean redirect = false;
    public static String directionServer;
    public static boolean guiShowing;
    public static KeyBinding keybind;
    public static KeyBinding keybind2;
    public static KeyBinding keybind3;

    public static enum Teams {

        Red, Blue, Purple, Cyan, Lime, Yellow, Green, Orange, Observers, Unknown
    };

    public static enum MatchState {

        Starting, Started, Finished, Waiting, Lobby, Unknown
    };

    public static enum ServerType {

        Lobby, Blitz, ProjectAres, Unknown
    };

    public AresData() {
        update = true;
        setMap("Fetching...");
        resetKills();
        resetDeaths();
        resetKilled();
        resetLargestKillstreak();
        setTeam(Teams.Observers);
        guiShowing = true;
        keybind = new KeyBinding("gui", Keyboard.getKeyIndex("F6"));
        keybind2 = new KeyBinding("inGameGui", Keyboard.getKeyIndex("L"));
        keybind3 = new KeyBinding("fullBright", Keyboard.getKeyIndex("G"));
        mapLoaderFinished = false;
        serverInformation = new AresServer[20];
        serverCount = 0;
        for (int c = 0; c < serverInformation.length; c++) {
            serverInformation[c] = new AresServer();
        }
        try {
            mapLoader = new InformationLoaderThread(new URL("https://oc.tc/play"));
        } catch (Exception e) {
            System.out.println("[ProjectAres]: Failed to load maps");
            System.out.println("[ProjectAres]: ERROR: " + e.toString());
        }
    }

    public static void update() {
        if (!mapLoaderFinished && mapLoader.getContents() != null) {
            mapLoaderFinished = true;
            try {
                String[][] mapData = ServerStatusHTMLParser.parse(mapLoader.getContents());

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
            } catch (Exception e) {
                System.out.println("[ProjectAres]: Failed to parse maps");
                System.out.println("[ProjectAres]: ERROR: " + e.toString());
            }
        }
    }

    public static void reload() {
        map = "Loading...";
        nextMap = "Loading...";

        try {
            mapLoader = new InformationLoaderThread(new URL("https://oc.tc/play"));
        } catch (Exception e) {
            System.out.println("[ProjectAres]: Failed to load maps");
            System.out.println("[ProjectAres]: ERROR: " + e.toString());
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

    public static boolean isPlayingAres() {
        return isPA;
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
        AresData.update = update;
    }

    public static String getUpdateLink() {
        return updateLink;
    }

    public static void setUpdateLink(String updateLink) {
        AresData.updateLink = updateLink;
    }
}
