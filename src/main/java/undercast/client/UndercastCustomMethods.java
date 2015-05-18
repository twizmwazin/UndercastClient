package undercast.client;

//You may not release this source under any condition, it must be linked to this page
//You may recompile and publish as long as skipperguy12 and Guru_Fraser are given credit
//You may not claim this to be your own
//You may not remove these comments

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import undercast.client.UndercastData.ServerLocation;
import undercast.client.UndercastData.ServerType;
import undercast.client.server.UndercastServer;
import undercast.network.common.NetManager;
import undercast.network.common.packet.Packet10GetServers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.concurrent.TimeUnit;

public class UndercastCustomMethods {

    private static Minecraft mc = Minecraft.getMinecraft();
    private static Boolean a1 = false;
    private static Boolean a = false;
    private static int c = 0;

    // simple rounding method
    private static double round(double d) {
        d = d * 100;
        d = Math.round(d);
        d = d / 100;
        return d;
    }

    private static double roundT(double d) {
        d = d * 1000;
        d = Math.round(d);
        d = d / 1000;
        return d;
    }

    /**
     * Calculates the KD Ratio
     *
     * @return KD double rounded
     */
    public static double getKD() {
        double k = UndercastData.getKills();
        double d = UndercastData.getDeaths();
        if (k == d && k == 0) {
            return 0D;
        } else if (k > 0 && d == 0) {
            return k;
        } else if (k == d && k > 0) {
            return 1D;
        } else {
            return round(k / d);
        }
    }

    public static double getTotalKD() {
        double k = UndercastData.getKills() + UndercastData.stats.kills;
        double d = UndercastData.getDeaths() + UndercastData.stats.deaths;
        if (k == d && k == 0) {
            return 0D;
        } else if (k > 0 && d == 0) {
            return k;
        } else {
            return roundT(k / d);
        }
    }

    /**
     * Calculates the KK Ratio
     *
     * @return KK double rounded
     */
    public static double getKK() {
        double k = UndercastData.getKills();
        double kk = UndercastData.getKilled();
        if (k == kk && k == 0) {
            return 0D;
        } else if (k > 0 && kk == 0) {
            return k;
        } else if (k == kk && kk > 0) {
            return 1D;
        } else {
            return round(k / kk);
        }
    }

    public static double getTotalKK() {
        double k = UndercastData.getKills() + UndercastData.stats.kills;
        double kk = UndercastData.getKilled() + UndercastData.stats.getKilled();
        if (k == kk && k == 0) {
            return 0D;
        } else if (k > 0 && kk == 0) {
            return k;
        } else {
            return roundT(k / kk);
        }
    }

    /**
     * Detects if the server is the lobby and resets values which need to be reseted
     */
    public static void handleServerSwap() {
        if (UndercastData.server.equalsIgnoreCase("lobby")) {
            UndercastData.isLobby = true;
        } else {
            UndercastData.isLobby = false;
        }
        if (!UndercastData.previousServer.equalsIgnoreCase("lobby")) {
            UndercastData.finalStats = new FinalStats();
        }
        UndercastData.setTeam("Observers");
        UndercastData.teamColor = 'b';
        UndercastData.resetDeaths();
        UndercastData.resetKills();
        UndercastData.resetKilled();
        UndercastData.resetKillstreak();
        UndercastData.resetLargestKillstreak();
        UndercastData.resetPreviousKillstreak();
        UndercastData.resetScore();
        UndercastData.isGameOver = false;
        UndercastData.victimList.clear();
        UndercastData.killerList.clear();
        UndercastData.reloadStats();

        // stop the timer and reset it
        try {
            UndercastData.matchTimer.stop();
        } catch (Exception ignored) {
        }
        // and start one which starts from 0
        UndercastData.incrementMatchTime = true;
        UndercastData.matchTimeHours = 0;
        UndercastData.matchTimeMin = 0;
        UndercastData.matchTimeSec = 0;

        // in order to prevent spam /class is not executed if
        // the display is disabled.
        if (UndercastConfig.showGSClass) {
            // get the server type for the new server (does not need to wait for
            // an update)
            for (int c = 0; c < UndercastData.serverInformation.length; c++) {
                if (UndercastData.server.equals(UndercastData.serverInformation[c].name)) {
                    if (UndercastData.serverInformation[c].type == ServerType.ghostsquadron) {
                        Minecraft.getMinecraft().thePlayer.sendChatMessage("/class");
                    }
                }
            }
        }
    }

    /**
     * This is used in order to only display hours if you play at least for one
     */
    public static String getPlayingTimeString() {
        long currentMilis = System.currentTimeMillis();
        long difference = currentMilis - UndercastData.playTimeStartMillis;
        int hours = (int) TimeUnit.HOURS.convert(difference, TimeUnit.MILLISECONDS);
        int minutes = hours == 0 ? (int) TimeUnit.MINUTES.convert(difference, TimeUnit.MILLISECONDS) : (int) TimeUnit.MINUTES.convert(difference, TimeUnit.MILLISECONDS) - 60 * hours;

        if (hours == 0) {
            if (minutes < 10) {
                return (UndercastConfig.lessObstructive ? "PT: " : "Playing Time: ") + "\u00A7E0:0" + minutes;
            } else {
                return (UndercastConfig.lessObstructive ? "PT: " : "Playing Time: ") + "\u00A7E0:" + minutes;
            }
        } else {
            if (hours < 10) {
                return (UndercastConfig.lessObstructive ? "PT: " : "Playing Time: ") + "\u00A7E" + hours + ":0" + minutes;
            } else {
                return (UndercastConfig.lessObstructive ? "PT: " : "Playing Time: ") + "\u00A7E" + hours + ":" + minutes;
            }
        }
    }

    public static String getMatchTimeString() {
        // if it's a map with time limit and it's enabled in the config
        if (!UndercastData.incrementMatchTime && UndercastConfig.showMatchTimeSeconds) {
            if (UndercastData.matchTimeHours == 0 && UndercastData.matchTimeMin == 0) {
                if (UndercastData.matchTimeSec < 10) {
                    return (UndercastConfig.lessObstructive ? "MT: " : "Match Time: ") + " \u00A7E0:0" + UndercastData.matchTimeSec;
                } else {
                    return (UndercastConfig.lessObstructive ? "MT: " : "Match Time: ") + "\u00A7E0:" + UndercastData.matchTimeSec;
                }
            } else if (UndercastData.matchTimeHours == 0) {
                if (UndercastData.matchTimeSec < 10) {
                    return (UndercastConfig.lessObstructive ? "MT: " : "Match Time: ") + "\u00A7E" + UndercastData.matchTimeMin + ":0" + UndercastData.matchTimeSec;
                } else {
                    return (UndercastConfig.lessObstructive ? "MT: " : "Match Time: ") + "\u00A7E" + UndercastData.matchTimeMin + ":" + UndercastData.matchTimeSec;
                }
            } else {
                if (UndercastData.matchTimeMin < 10) {
                    if (UndercastData.matchTimeSec < 10) {
                        return (UndercastConfig.lessObstructive ? "MT: " : "Match Time: ") + "\u00A7E" + UndercastData.matchTimeHours + ":0" + UndercastData.matchTimeMin + ":0" + UndercastData.matchTimeSec;
                    } else {
                        return (UndercastConfig.lessObstructive ? "MT: " : "Match Time: ") + "\u00A7E" + UndercastData.matchTimeHours + ":0" + UndercastData.matchTimeMin + ":" + UndercastData.matchTimeSec;
                    }
                } else {
                    if (UndercastData.matchTimeSec < 10) {
                        return (UndercastConfig.lessObstructive ? "MT: " : "Match Time: ") + "\u00A7E" + UndercastData.matchTimeHours + ":" + UndercastData.matchTimeMin + ":0" + UndercastData.matchTimeSec;
                    } else {
                        return (UndercastConfig.lessObstructive ? "MT: " : "Match Time: ") + "\u00A7E" + UndercastData.matchTimeHours + ":" + UndercastData.matchTimeMin + ":" + UndercastData.matchTimeSec;
                    }
                }
            }
        } else {
            if (UndercastData.matchTimeHours == 0) {
                if (UndercastData.matchTimeMin < 10) {
                    return (UndercastConfig.lessObstructive ? "MT: " : "Match Time: ") + "\u00A7E0:0" + UndercastData.matchTimeMin;
                } else {
                    return (UndercastConfig.lessObstructive ? "MT: " : "Match Time: ") + "\u00A7E0:" + UndercastData.matchTimeMin;
                }
            } else {
                if (UndercastData.matchTimeMin < 10) {
                    return (UndercastConfig.lessObstructive ? "MT: " : "Match Time: ") + "\u00A7E" + UndercastData.matchTimeHours + ":0" + UndercastData.matchTimeMin;
                } else {
                    return (UndercastConfig.lessObstructive ? "MT: " : "Match Time: ") + "\u00A7E" + UndercastData.matchTimeHours + ":" + UndercastData.matchTimeMin;
                }
            }
        }
    }

    /**
     * Sorts UndercastData.sortedServerInformation using the UndercastData.sortindex
     */
    public static void sortAndFilterServers() {
        // if the index is moving to web then use the downloaded server list
        if (UndercastData.sortNames[UndercastData.sortIndex].equalsIgnoreCase("web")) {
            // just keep the order
            for (int c = 0; c < UndercastData.serverCount; c++) {
                UndercastData.sortedServerInformation[c] = UndercastData.serverInformation[c];
            }
        } // sort based on matchStatus
        else if (UndercastData.sortNames[UndercastData.sortIndex].equalsIgnoreCase("match")) {
            int index = 0;
            // Starting
            for (int c = 0; c < UndercastData.serverCount; c++) {
                if (UndercastData.serverInformation[c].matchState == UndercastData.MatchState.Starting) {
                    UndercastData.sortedServerInformation[index] = UndercastData.serverInformation[c];
                    index++;
                }
            }
            // Waiting
            for (int c = 0; c < UndercastData.serverCount; c++) {
                if (UndercastData.serverInformation[c].matchState == UndercastData.MatchState.Waiting) {
                    UndercastData.sortedServerInformation[index] = UndercastData.serverInformation[c];
                    index++;
                }
            }
            // Finished
            for (int c = 0; c < UndercastData.serverCount; c++) {
                if (UndercastData.serverInformation[c].matchState == UndercastData.MatchState.Finished) {
                    UndercastData.sortedServerInformation[index] = UndercastData.serverInformation[c];
                    index++;
                }
            }
            // Started
            for (int c = 0; c < UndercastData.serverCount; c++) {
                if (UndercastData.serverInformation[c].matchState == UndercastData.MatchState.Started) {
                    UndercastData.sortedServerInformation[index] = UndercastData.serverInformation[c];
                    index++;
                }
            }
            // Lobby
            for (int c = 0; c < UndercastData.serverCount; c++) {
                if (UndercastData.serverInformation[c].matchState == UndercastData.MatchState.Lobby) {
                    UndercastData.sortedServerInformation[index] = UndercastData.serverInformation[c];
                    index++;
                }
            }
            // Unknown
            for (int c = 0; c < UndercastData.serverCount; c++) {
                if (UndercastData.serverInformation[c].matchState == UndercastData.MatchState.Unknown) {
                    UndercastData.sortedServerInformation[index] = UndercastData.serverInformation[c];
                    index++;
                }
            }
        } // sort based on player count
        else if (UndercastData.sortNames[UndercastData.sortIndex].equalsIgnoreCase("players")) {
            // copy the player counts to an int array
            int[][] playerCounts = new int[UndercastData.serverCount][2];
            for (int c = 0; c < UndercastData.serverCount; c++) {
                playerCounts[c][0] = UndercastData.serverInformation[c].playerCount;
                // save the old index
                playerCounts[c][1] = c;
            }

            // sort the player counts
            Arrays.sort(playerCounts, new Comparator<int[]>() {
                // just compare the first int of the array (player count)
                @Override
                public int compare(final int[] entry1, final int[] entry2) {
                    final Integer int1 = entry1[0];
                    final Integer int2 = entry2[0];
                    return int1.compareTo(int2);
                }
            });

            for (int c = 0; c < UndercastData.serverCount; c++) {
                UndercastData.sortedServerInformation[c] = UndercastData.serverInformation[playerCounts[c][1]];
            }
        } // if the servers are being sorted abc sort the list and update
        else if (UndercastData.sortNames[UndercastData.sortIndex].equalsIgnoreCase("abc")) {
            // extract the server names to an Array list
            ArrayList<String> serverNames = new ArrayList<String>(UndercastData.serverCount);
            for (int c = 0; c < UndercastData.serverCount; c++) {
                serverNames.add(c, UndercastData.serverInformation[c].name + ":" + UndercastData.serverInformation[c].location);
            }

            // sort the names
            Collections.sort(serverNames);

            // put the whole server into order
            for (int c = 0; c < serverNames.size(); c++) {
                for (int i = 0; i < UndercastData.serverCount; i++) {
                    if (serverNames.get(c).equals(UndercastData.serverInformation[i].name + ":" + UndercastData.serverInformation[i].location)) {
                        UndercastData.sortedServerInformation[c] = UndercastData.serverInformation[i];
                        break;
                    }

                }
            }
        }

        // filter the servers
        // reset the filtered server count
        UndercastData.filteredServerCount = UndercastData.serverCount;
        // show which filter is chosen
        ServerType shownType = ServerType.Unknown;
        if (UndercastData.filterNames[UndercastData.filterIndex].equalsIgnoreCase("PA")) {
            shownType = ServerType.projectares;
        } else if (UndercastData.filterNames[UndercastData.filterIndex].equalsIgnoreCase("Blitz")) {
            shownType = ServerType.blitz;
        } else if (UndercastData.filterNames[UndercastData.filterIndex].equalsIgnoreCase("GS")) {
            shownType = ServerType.ghostsquadron;
        }

        // extract the servers
        ArrayList<UndercastServer> filteredServers = new ArrayList<UndercastServer>(UndercastData.serverCount);
        for (int c = 0; c < UndercastData.serverCount; c++) {
            if ((shownType == ServerType.Unknown || UndercastData.sortedServerInformation[c].type == shownType) && ((UndercastData.locationIndex == 0 && UndercastData.sortedServerInformation[c].location == ServerLocation.US) || (UndercastData.locationIndex == 1 && UndercastData.sortedServerInformation[c].location == ServerLocation.EU))) {
                filteredServers.add(UndercastData.sortedServerInformation[c]);
            }
        }

        UndercastData.filteredServerCount = filteredServers.size();
        // and put them back to the serverList
        for (int c = 0; c < filteredServers.size(); c++) {
            UndercastData.sortedServerInformation[c] = filteredServers.get(c);
        }
    }

    public static boolean isTeamkill(String unstripedMessage, String player1, String player2) {
        char colorchar1, colorchar2;
        //strip underline formatting code from highlighted messages, remove all non-color codes
        unstripedMessage = unstripedMessage.replace("\u00A7n", "").replace("\u00A7k", "").replace("\u00A7l", "").replace("\u00A7m", "").replace("\u00A7o", "").replace("\u00A7r", "");
        colorchar1 = unstripedMessage.charAt(unstripedMessage.indexOf(player1) - 1);
        colorchar2 = unstripedMessage.charAt(unstripedMessage.indexOf(player2) - 1);
        if (colorchar1 == colorchar2) {
            return true;
        } else {
            return false;
        }
    }

    public static String getKillDisplayString() {
        String str;
        if (UndercastData.team.equals("Observers") && UndercastData.kills == 0 && UndercastData.deaths == 0 && UndercastConfig.realtimeStats) {
            if (UndercastConfig.showTotalKills) {
                str = (UndercastConfig.lessObstructive ? "TK: " : "Total Kills: ") + "\u00A7a" + (int) (UndercastData.getKills() + UndercastData.stats.kills);
            } else {
                str = (UndercastConfig.lessObstructive ? "K: " : "Kills: ") + "\u00A7a" + (int) (UndercastData.getKills());
            }
        } else {
            str = (UndercastConfig.lessObstructive ? "K: " : "Kills: ") + "\u00A7a" + (int) UndercastData.getKills();
            if (UndercastConfig.showTotalKills && UndercastConfig.realtimeStats) {
                str += "\u00A7f/\u00A7a" + (int) (UndercastData.getKills() + UndercastData.stats.kills);
            }
        }
        return str;
    }

    public static String getDeathDisplayString() {
        String str;
        if (UndercastData.team.equals("Observers") && UndercastData.kills == 0 && UndercastData.deaths == 0 && UndercastConfig.realtimeStats) {
            if (UndercastConfig.showTotalKills) {
                str = (UndercastConfig.lessObstructive ? "TD: " : "Total Deaths: ") + "\u00A74" + (int) (UndercastData.getDeaths() + UndercastData.stats.deaths);
            } else {
                str = (UndercastConfig.lessObstructive ? "D: " : "Deaths: ") + "\u00A74" + (int) (UndercastData.getDeaths());
            }
        } else {
            str = (UndercastConfig.lessObstructive ? "D: " : "Deaths: ") + "\u00A74" + (int) UndercastData.getDeaths();
            if (UndercastConfig.showTotalKills && UndercastConfig.realtimeStats) {
                str += "\u00A7f/\u00A74" + (int) (UndercastData.getDeaths() + UndercastData.stats.deaths);
            }
        }
        return str;
    }

    public static String getRaindropDisplayString() {
        String str;
        if (UndercastData.team.equals("Observers") && UndercastData.kills == 0 && UndercastData.deaths == 0) {
            if (UndercastConfig.showTotalKills) {
                str = (UndercastConfig.lessObstructive ? "TRD: " : "Total Raindrops: ") + "\u00A7b" + (int) (RaindropManager.TotalRaindrops);
            } else {
                str = (UndercastConfig.lessObstructive ? "RD: " : "Raindrops: ") + "\u00A7b" + (int) (RaindropManager.RaindropsThisMatch);
            }
        } else {
            str = (UndercastConfig.lessObstructive ? "RD: " : "Raindrops: ") + "\u00A7b" + (int) RaindropManager.RaindropsThisMatch;
            if (UndercastConfig.showTotalKills) {
                str += "\u00A7f/\u00A7b" + (int) (RaindropManager.RaindropsThisMatch + RaindropManager.TotalRaindrops);
            }
        }
        return str;
    }

    public static String getKDDisplayString() {
        String str;
        if (UndercastData.team.equals("Observers") && UndercastData.kills == 0 && UndercastData.deaths == 0 && UndercastConfig.realtimeStats) {
            if (UndercastConfig.showTotalKills) {
                str = (UndercastConfig.lessObstructive ? "TKD: " : "Total K/D: ") + "\u00A73" + (UndercastData.stats.kd);
            } else {
                str = (UndercastConfig.lessObstructive ? "KD: " : "K/D: ") + "\u00A73" + (getKD());
            }
        } else {
            str = (UndercastConfig.lessObstructive ? "KD: " : "K/D: ") + "\u00A73" + getKD();
            if (UndercastConfig.showTotalKills && UndercastConfig.realtimeStats) {
                str += "\u00A7f/\u00A73" + getTotalKD();
            }
        }
        return str;
    }

    public static String getKKDisplayString() {
        String str;
        if (UndercastData.team.equals("Observers") && UndercastData.kills == 0 && UndercastData.deaths == 0 && UndercastConfig.realtimeStats) {
            if (UndercastConfig.showTotalKills) {
                str = (UndercastConfig.lessObstructive ? "TKK: " : "Total K/K: ") + "\u00A73" + (UndercastData.stats.kk);
            } else {
                str = (UndercastConfig.lessObstructive ? "KK: " : "K/K: ") + "\u00A73" + (getKK());
            }
        } else {
            str = (UndercastConfig.lessObstructive ? "KK: " : "K/K: ") + "\u00A73" + getKK();
            if (UndercastConfig.showTotalKills && UndercastConfig.realtimeStats) {
                str += "\u00A7f/\u00A73" + getTotalKK();
            }
        }
        return str;
    }

    public static void init() {
        Calendar cal = Calendar.getInstance();

        if (cal.get(2) == 3 && cal.get(5) == 1) {
            a1 = true;
        }
    }

    public static FontRenderer getFontRenderer() {
        if (!a1) {
            return mc.fontRendererObj;
        } else {
            c++;
            if (c == 30) {
                c = 0;
                a = !a;
            }
            if (a) {
                return mc.standardGalacticFontRenderer;
            } else {
                return mc.fontRendererObj;
            }
        }
    }


    public static boolean isSpecialObjective(int obj) {
        // detect special objectives like 50, 100, 150, 200, 250...
        int i1 = obj / 100;
        int i2 = (obj + 50) / 100;
        if ((i1 * 100 == obj) || (i2 * 100 == obj + 50)) {
            return true;
        }

        // detect special objectives like 111, 222, 1111...
        String s = String.valueOf(obj);
        if (s.length() >= 2) {
            char c1, c2, c3, c4;
            c1 = s.charAt(s.length() - 1);
            c2 = s.charAt(s.length() - 2);
            if (s.length() > 2) {
                c3 = s.charAt(s.length() - 3);
            } else {
                c3 = c2;
            }
            if (s.length() > 3) {
                c4 = s.charAt(s.length() - 4);
            } else {
                c4 = c3;
            }

            if (c1 == c2 && c1 == c3 && c1 == c4) {
                return true;
            }
        }
        return false;
    }


    public static ServerLocation getLocationForString(String s) {
        if (s == null) {
            return ServerLocation.US;
        }
        if (s.contains("eu") || s.contains("EU") || s.contains("eU") || s.contains("Eu")) {
            return ServerLocation.EU;
        } else {
            return ServerLocation.US;
        }
    }

    public static void parseTeamJoinMessage(String message, String messageWithFormattingCodes) {
        if (!message.contains("You joined the")) {
            return;
        }

        try {
            // Get team name
            String teamName = message.replace("You joined the ", "").replace(" Team", "").replace(" team", "").replace(" Squad", "").replace(" squad", "");

            // If the team name contains more than one word, get the first word because there might be formatting code between the two words.
            String firstword = teamName;
            if (teamName.contains(" ")) {
                firstword = teamName.substring(0, teamName.indexOf(" "));
            }

            // Get team color char
            char colorChar = messageWithFormattingCodes.charAt(messageWithFormattingCodes.indexOf(firstword) - 1);

            UndercastData.team = teamName;

            // Check if the colorChar has a vaild value
            if ("0123456789abcdef".indexOf(colorChar) == -1) {
                UndercastData.teamColor = '0';
            } else {
                UndercastData.teamColor = colorChar;
            }

        } catch (Exception e) {
            // this should never happen
            UndercastData.teamColor = '0';
            UndercastData.team = "Unknown";
        }
    }

    public static void resetAllStats() {
        UndercastData.resetKills();
        UndercastData.resetKilled();
        UndercastData.resetDeaths();
        UndercastData.resetKillstreak();
        UndercastData.resetLargestKillstreak();
        UndercastData.resetScore();
        UndercastData.setTeam("Observers");
        UndercastData.teamColor = 'b'; // b for aqua
        if (RaindropManager.manager != null) {
            RaindropManager.manager.resetCounter();
        }
        new Thread() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                NetManager.sendPacket(new Packet10GetServers());
            }
        }.start();
    }
}
