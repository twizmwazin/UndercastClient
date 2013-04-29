package tc.oc;
//You may not release this source under any condition, it must be linked to this page
//You may recompile and publish as long as skipperguy12 and Guru_Fraser are given credit
//You may not claim this to be your own
//You may not remove these comments

import org.lwjgl.input.Keyboard;

import tc.oc.internetTools.MatchLoaderThread;
import tc.oc.internetTools.ServerStatusHTMLParser;

import java.net.URL;
import java.util.HashSet;

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
    public static boolean update;
    public static String updateLink;
    private static MatchLoaderThread mapLoader;
    private static boolean mapLoaderFinished;
    public static String[][] mapData;

    public static boolean guiShowing;

    public static enum Teams {Red, Blue, Purple, Cyan, Lime, Yellow, Green, Orange, Observers, Unknown};

    public AresData() {
        update=true;
        setMap("Fetching...");
        resetKills();
        resetDeaths();
        resetKilled();
        resetLargestKillstreak();
        setTeam(Teams.Observers);
        guiShowing = true;
        mapLoaderFinished = false;
        try {
            mapLoader = new MatchLoaderThread(new URL("https://oc.tc/play"));
        } catch(Exception e) {
            System.out.println("[ProjectAres]: Failed to load maps");
            System.out.println("[ProjectAres]: ERROR: " + e.toString());
        }
    }

    public static void update() {
        if(!mapLoaderFinished && mapLoader.getContents() != null) {
            mapLoaderFinished = true;
            try {
                mapData = ServerStatusHTMLParser.parse(mapLoader.getContents());
            } catch (Exception e) {
                System.out.println("[ProjectAres]: Failed to parse maps");
                System.out.println("[ProjectAres]: ERROR: " + e.toString());
            }
            // set the map
            for(int c = 0; c < mapData.length; c++) {
                if(mapData[c][0] == null) {
                    break;
                }
                if(mapData[c][0].replace(" ", "").equalsIgnoreCase(server)) { // that space in the server name has taken me a lot of time
                    map = mapData[c][2].replace("Now: ", "");
                    nextMap = mapData[c][3].replace("Next: ", "");
                }
            }
        }
    }

    public static void reload() {
        map = "Loading...";
        nextMap = "Loading...";

        try {
            mapLoader = new MatchLoaderThread(new URL("https://oc.tc/play"));
        } catch(Exception e) {
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
