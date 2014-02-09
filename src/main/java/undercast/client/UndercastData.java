package undercast.client;

//You may not release this source under any condition, it must be linked to this page
//You may recompile and publish as long as skipperguy12 and Guru_Fraser are given credit
//You may not claim this to be your own
//You may not remove these comments

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import undercast.client.achievements.SpecialObjectiveLogger;
import undercast.client.internetTools.InformationLoaderThread;
import undercast.client.internetTools.PlayerStatsHTMLParser;
import undercast.client.internetTools.ServerStatusHTMLParser;
import undercast.client.internetTools.ServersCommandParser;
import undercast.client.server.UndercastServer;
import undercast.network.client.NetClientManager;
import undercast.network.common.NetManager;
import undercast.network.common.packet.Packet06SendServer;
import undercast.network.common.packet.Packet10GetServers;

public class UndercastData {
    // Data Varibles

    public static String map;
    public static String nextMap;
    public static ServerType currentServerType = ServerType.Unknown;
    public static double kills;
    public static double deaths;
    public static double killed;
    public static int killstreak;
    public static int largestKillstreak;
    public static int previousKillstreak;
    public static int score;
    public static String server;
    public static String previousServer = "Lobby";
    public static String team;
    public static char teamColor;
    public static boolean isOC = false;
    public static boolean isLobby;
    public static boolean update;
    public static String updateLink;
    private static InformationLoaderThread mapLoader;
    private static InformationLoaderThread statsLoader;
    public static UndercastServer[] serverInformation;
    public static UndercastServer[] sortedServerInformation;
    public static int serverCount;
    public static int filteredServerCount;
    // if it's true, the /server comand isn't executed after a
    // "Welcome to Overcast Network" message
    public static boolean welcomeMessageExpected = false;
    public static boolean redirect = false;
    public static String directionServer;
    public static boolean guiShowing;
    public static boolean isGameOver = false;
    public static int playTimeHours;
    public static int playTimeMin;
    public static int sortIndex;
    public static int filterIndex;
    public static int locationIndex;
    // saves if a /server command (without argument) was executed, if it's
    // false, the user executed it
    public static boolean serverDetectionCommandExecuted = false;
    public static boolean isNextKillFirstBlood = false;
    public static boolean isLastKillFromPlayer = false;
    public static String latestVersion;
    public static int matchTimeSec;
    public static int matchTimeMin;
    public static int matchTimeHours;
    public static boolean incrementMatchTime;
    public static MatchTimer matchTimer;
    public static String currentGSClass = "Unknown";
    public static PlayerStats stats;
    public static Integer[] parsedPagesUS;
    public static Integer[] parsedPagesEU;
    public static boolean isObjectiveReload = false;
    public static boolean removeNextChatMessage = false;
    public static FinalStats finalStats;
    public static boolean isEU;
    // used for the revenge system
    public static ArrayList<String> victimList = new ArrayList<String>();
    public static ArrayList<String> killerList = new ArrayList<String>();
    public static String[][] mapData = null;
    public static boolean lobbyLeaveDetectionStarted = false;


    public static enum MatchState {

        Starting, Started, Finished, Waiting, Lobby, Unknown
    };

    public static enum ServerType {

        lobby, blitz, projectares, ghostsquadron, Unknown
    };

    public static enum ServerLocation {
        US, EU
    };

    public static String[] sortNames = { "Web", "Match", "Players", "Abc" };
    public static String[] filterNames = { "All", "PA", "Blitz", "GS" };
    public static String[] locationNames = { "US", "EU" };

    public UndercastData() {
        update = true;
        setMap("Fetching...");
        resetKills();
        resetDeaths();
        resetKilled();
        resetLargestKillstreak();
        resetScore();
        setTeam("Observers");
        teamColor = 'b';
        stats = new PlayerStats();
        guiShowing = true;
        serverInformation = new UndercastServer[999];
        serverCount = 0;
        filteredServerCount = 0;
        parsedPagesUS = new Integer[4];
        parsedPagesUS[0] = 1;
        parsedPagesUS[1] = 2;
        parsedPagesUS[2] = 3;
        parsedPagesUS[3] = -1;
        parsedPagesEU = new Integer[2];
        parsedPagesEU[0] = 1;
        parsedPagesEU[1] = -1;

        for (int c = 0; c < serverInformation.length; c++) {
            serverInformation[c] = new UndercastServer();
        }
        sortedServerInformation = new UndercastServer[999];
        for (int c = 0; c < sortedServerInformation.length; c++) {
            sortedServerInformation[c] = new UndercastServer();
        }
        sortIndex = 0;
        filterIndex = UndercastConfig.lastUsedFilter < filterNames.length ? UndercastConfig.lastUsedFilter : 0;
        locationIndex = UndercastConfig.lastUsedLocation < locationNames.length ? UndercastConfig.lastUsedLocation : 0;
        filterIndex = 0;
        try {
            mapLoader = new InformationLoaderThread(new URL("https://oc.tc/play"));
            statsLoader = new InformationLoaderThread(new URL("https://oc.tc/" + Minecraft.getMinecraft().getSession().getUsername()));
        } catch (Exception e) {
            System.out.println("[UndercastMod]: Failed to start information loaders");
            System.out.println("[UndercastMod]: ERROR: " + e.toString());
        }
    }

    public static void reloadServerInformations(boolean getMatchState) {
        map = "Loading...";
        nextMap = "Loading...";

        NetManager.sendPacket(new Packet10GetServers());
        if (isOC && getMatchState) {
            removeNextChatMessage = true;
            ServersCommandParser.castByMod();
            Minecraft.getMinecraft().thePlayer.sendChatMessage("/servers 1");
        }
    }

    public static void reloadStats() {
        try {
            statsLoader = new InformationLoaderThread(new URL("https://oc.tc/" + Minecraft.getMinecraft().thePlayer.getCommandSenderName()));
        } catch (Exception e) {
            System.out.println("[UndercastMod]: Failed to start information loaders");
            System.out.println("[UndercastMod]: ERROR: " + e.toString());
        }
    }

    public static void websiteLoaded(String url, String contents) {
        if (url.equals("https://oc.tc/play")) {
            //updateMap();
        } else {
            updateStats(contents, url);
        }
    }

    private static void updateStats(String cont, String url) {
        if (UndercastConfig.realtimeStats == false) {
            return;
        }
        try {
            String[] data = PlayerStatsHTMLParser.parse(cont);
            if (UndercastData.isObjectiveReload) {
                UndercastData.isObjectiveReload = false;
                int woolsDifference = Integer.parseInt(data[9]) - stats.wools;
                int coresDifference = Integer.parseInt(data[10]) - stats.cores;
                int monumentDifference = Integer.parseInt(data[11]) - stats.monuments;
                int i = 1;
                EntityClientPlayerMP player = Minecraft.getMinecraft().thePlayer;
                while (woolsDifference >= i) {
                    if (UndercastCustomMethods.isSpecialObjective(UndercastData.stats.wools + i)) {
                        SpecialObjectiveLogger.logSpecialObjective(UndercastData.stats.wools + i, "Wool", UndercastData.server, UndercastData.map);
                        if (UndercastConfig.displaySpecialObjectives) {
                            sendMessage("[UndercastMod] \u00A7lSPECIAL WOOL: \u00A7c" + String.valueOf(UndercastData.stats.wools + i));
                        }
                    }
                    i++;
                }
                i = 1;
                while (coresDifference >= i) {
                    if (UndercastCustomMethods.isSpecialObjective(UndercastData.stats.cores + i)) {
                        SpecialObjectiveLogger.logSpecialObjective(UndercastData.stats.cores + i, "Core", UndercastData.server, UndercastData.map);
                        if (UndercastConfig.displaySpecialObjectives) {
                            sendMessage("[UndercastMod] \u00A7lSPECIAL CORE: \u00A7c" + String.valueOf(UndercastData.stats.cores + i));
                        }
                    }
                    i++;
                }
                i = 1;
                while (monumentDifference >= i) {
                    if (UndercastCustomMethods.isSpecialObjective(UndercastData.stats.monuments + i)) {
                        SpecialObjectiveLogger.logSpecialObjective(UndercastData.stats.monuments + i, "Monument", UndercastData.server, UndercastData.map);
                        if (UndercastConfig.displaySpecialObjectives) {
                            sendMessage("[UndercastMod] \u00A7lSPECIAL MONUMENT: \u00A7c" + String.valueOf(UndercastData.stats.monuments + i));
                        }
                    }
                    i++;
                }

                if (UndercastConfig.displaySpecialObjectives) {
                    if (woolsDifference > 0) {
                        if (UndercastCustomMethods.isSpecialObjective(UndercastData.stats.wools + woolsDifference + 5)) {
                            sendMessage("[UndercastMod] Your are \u00A7c5\u00A7f wools away from a \u00A7ospecial wool\u00A7r (" + (UndercastData.stats.wools + woolsDifference + 5) + ")");
                        } else if (UndercastCustomMethods.isSpecialObjective(UndercastData.stats.wools + woolsDifference + 2)) {
                            sendMessage("[UndercastMod] Your are \u00A7c2\u00A7f wools away from a \u00A7ospecial wool\u00A7r (" + (UndercastData.stats.wools + woolsDifference + 2) + ")");
                        } else if (UndercastCustomMethods.isSpecialObjective(UndercastData.stats.wools + woolsDifference + 1)) {
                            sendMessage("[UndercastMod] Your are \u00A7c1\u00A7f wools away from a \u00A7ospecial wool\u00A7r (" + (UndercastData.stats.wools + woolsDifference + 1) + ")");
                        }
                    }
                    if (coresDifference > 0) {
                        if (UndercastCustomMethods.isSpecialObjective(UndercastData.stats.cores + coresDifference + 5)) {
                            sendMessage("[UndercastMod] Your are \u00A7c5\u00A7f cores away from a \u00A7ospecial core\u00A7r (" + (UndercastData.stats.cores + coresDifference + 5) + ")");
                        } else if (UndercastCustomMethods.isSpecialObjective(UndercastData.stats.cores + coresDifference + 2)) {
                            sendMessage("[UndercastMod] Your are \u00A7c2\u00A7f cores away from a \u00A7ospecial core\u00A7r (" + (UndercastData.stats.cores + coresDifference + 2) + ")");
                        } else if (UndercastCustomMethods.isSpecialObjective(UndercastData.stats.cores + coresDifference + 1)) {
                            sendMessage("[UndercastMod] Your are \u00A7c1\u00A7f cores away from a \u00A7ospecial core\u00A7r (" + (UndercastData.stats.cores + coresDifference + 1) + ")");
                        }
                    }
                    if (monumentDifference > 0) {
                        if (UndercastCustomMethods.isSpecialObjective(UndercastData.stats.monuments + monumentDifference + 5)) {
                            sendMessage("[UndercastMod] Your are \u00A7c5\u00A7f monuments away from a \u00A7ospecial monument\u00A7r (" + (UndercastData.stats.monuments + monumentDifference + 5) + ")");
                        } else if (UndercastCustomMethods.isSpecialObjective(UndercastData.stats.monuments + monumentDifference + 2)) {
                            sendMessage("[UndercastMod] Your are \u00A7c2\u00A7f monuments away from a \u00A7ospecial monument\u00A7r (" + (UndercastData.stats.monuments + monumentDifference + 2) + ")");
                        } else if (UndercastCustomMethods.isSpecialObjective(UndercastData.stats.monuments + monumentDifference + 1)) {
                            sendMessage("[UndercastMod] Your are \u00A7c1\u00A7f monuments away from a \u00A7ospecial monument\u00A7r (" + (UndercastData.stats.monuments + monumentDifference + 1) + ")");
                        }

                    }
                }
                finalStats.addObjectives(coresDifference, UndercastData.stats.cores + coresDifference, woolsDifference, UndercastData.stats.wools + woolsDifference, monumentDifference, UndercastData.stats.monuments + monumentDifference);
                finalStats.showStats();
            } else {
                PlayerStats stats = new PlayerStats();
                stats.kills = Integer.parseInt(data[0]);
                stats.deaths = Integer.parseInt(data[1]);
                stats.friendCount = Integer.parseInt(data[2]);
                stats.kd = Double.parseDouble(data[3]);
                stats.kk = Double.parseDouble(data[4]);
                stats.serverJoins = Integer.parseInt(data[5]);
                //skip days played
                stats.wools = Integer.parseInt(data[7]);
                stats.cores = Integer.parseInt(data[8]);
                stats.monuments = Integer.parseInt(data[9]);
                stats.name = url.replace("https://oc.tc/", "");
                // only if no data relates on the current stats
                if (UndercastData.kills == 0 && UndercastData.deaths == 0) {
                    UndercastData.stats = stats;
                }

            }
        } catch (Exception e) {
            System.out.println("[UndercastMod]: Failed to parse player stats");
            System.out.println("[UndercastMod]: ERROR: " + e.toString());
            e.printStackTrace();
        }
    }

    public static void updateMap() {
    	try {
            serverCount = mapData.length;
            for (int c = 0; c < mapData.length; c++) {
                serverInformation[c].name = mapData[c][0];
                try {
                    serverInformation[c].playerCount = Integer.parseInt(mapData[c][1]);
                } catch (Exception e) {
                    serverInformation[c].playerCount = -1;
                }
                serverInformation[c].currentMap = mapData[c][2];
                serverInformation[c].nextMap = mapData[c][3];
                if (serverInformation[c].matchState == null || !isOC) {
                    serverInformation[c].matchState = MatchState.Unknown;
                }
                try {
                    serverInformation[c].type = ServerType.valueOf(mapData[c][4].replace("-", ""));
                } catch (Exception e) {
                    serverInformation[c].type = ServerType.Unknown;
                }
                serverInformation[c].location = UndercastCustomMethods.getLocationForString(mapData[c][5]);

            }

            // set the map
            for (int c = 0; c < serverInformation.length; c++) {
                if (serverInformation[c].getServerName() == null) {
                    serverCount = c - 1;
                    break;
                }
                if (serverInformation[c].name.replace(" ", "").equalsIgnoreCase(server)) { // that
                    // space
                    // in
                    // the
                    // server
                    // name
                    // has
                    // taken
                    // me
                    // a
                    // lot
                    // of
                    // time
                    map = serverInformation[c].currentMap;
                    nextMap = serverInformation[c].nextMap;
                    currentServerType = serverInformation[c].type;
                }
            }

            filteredServerCount = serverCount;
            UndercastCustomMethods.sortAndFilterServers();
        } catch (Exception e) {
            System.out.println("[UndercastMod]: Failed to load maps");
            System.out.println("[UndercastMod]: ERROR: " + e.toString());
            e.printStackTrace();
        }
    }

    public static void addKills(double d) {
        kills += d;
    }

    public static void addScore(int i) {
        score += i;
    }

    public static void resetKills() {
        kills = 0;
    }

    public static void resetScore() {
        score = 0;
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

    public static double getPreviousKillstreak() {
        return previousKillstreak;
    }

    public static void resetPreviousKillstreak() {
        previousKillstreak = 0;
    }

    public static void setPreviousKillstreak(int i) {
        previousKillstreak = i;
    }

    public static void resetLargestKillstreak() {
        largestKillstreak = 0;
    }

    public static double getLargestKillstreak() {
        return largestKillstreak;
    }

    public static boolean isPlayingOvercastNetwork() {
        return isOC;
    }

    public static String getTeam() {
        return team;
    }

    public static void setTeam(String teams) {
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
        previousServer = server;
        server = servers;
        NetClientManager.sendPacket(new Packet06SendServer(server, UndercastData.isEU ? "EU" : "US"));
        reloadServerInformations(false);
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
    
    private static void sendMessage(String text) {
    	IChatComponent thingy = new ChatComponentText(text);
        EntityClientPlayerMP player = Minecraft.getMinecraft().thePlayer;
        player.func_146105_b(thingy);
    }

}
