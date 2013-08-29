package undercast.client;

//You may not release this source under any condition, it must be linked to this page
//You may recompile and publish as long as skipperguy12 and Guru_Fraser are given credit
//You may not claim this to be your own
//You may not remove these comments

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import net.minecraft.client.Minecraft;
import undercast.client.UndercastData.ServerType;
import undercast.client.UndercastData.Teams;
import undercast.client.server.UndercastServer;

public class UndercastCustomMethods {

    private static Minecraft mc = Minecraft.getMinecraft();

    // simple rounding method
    private static double round(double d) {
        d = d * 100;
        d = Math.round(d);
        d = d / 100;
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

    /**
     * Detects if the server is the lobby and resets values which need to be
     * reseted
     */
    public static void handleServerSwap() {
        if (UndercastData.server.equalsIgnoreCase("lobby")) {
            UndercastData.isLobby = true;
        } else {
            UndercastData.isLobby = false;
        }
        UndercastData.setTeam(UndercastData.Teams.Observers);
        UndercastData.resetDeaths();
        UndercastData.resetKills();
        UndercastData.resetKilled();
        UndercastData.resetKillstreak();
        UndercastData.resetLargestKillstreak();
        UndercastData.resetPreviousKillstreak();
        UndercastData.resetScore();
        UndercastData.isGameOver = false;
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
        if (UndercastData.playTimeHours == 0) {
            if (UndercastData.playTimeMin < 10) {
                return "Playing Time: \u00A7E0:0" + UndercastData.playTimeMin;
            } else {
                return "Playing Time: \u00A7E0:" + UndercastData.playTimeMin;
            }
        } else {
            if (UndercastData.playTimeMin < 10) {
                return "Playing Time: \u00A7E" + UndercastData.playTimeHours + ":0" + UndercastData.playTimeMin;
            } else {
                return "Playing Time: \u00A7E" + UndercastData.playTimeHours + ":" + UndercastData.playTimeMin;
            }
        }
    }

    public static String getMatchTimeString() {
        // if it's a map with time limit and it's enabled in the config
        if (!UndercastData.incrementMatchTime && UndercastConfig.showMatchTimeSeconds) {
            if (UndercastData.matchTimeHours == 0 && UndercastData.matchTimeMin == 0) {
                if (UndercastData.matchTimeSec < 10) {
                    return "Match Time: \u00A7E0:0" + UndercastData.matchTimeSec;
                } else {
                    return "Match Time: \u00A7E0:" + UndercastData.matchTimeSec;
                }
            } else if (UndercastData.matchTimeHours == 0) {
                if (UndercastData.matchTimeSec < 10) {
                    return "Match Time: \u00A7E" + UndercastData.matchTimeMin + ":0" + UndercastData.matchTimeSec;
                } else {
                    return "Match Time: \u00A7E" + UndercastData.matchTimeMin + ":" + UndercastData.matchTimeSec;
                }
            } else {
                if (UndercastData.matchTimeMin < 10) {
                    if (UndercastData.matchTimeSec < 10) {
                        return "Match Time: \u00A7E" + UndercastData.matchTimeHours + ":0" + UndercastData.matchTimeMin + ":0" + UndercastData.matchTimeSec;
                    } else {
                        return "Match Time: \u00A7E" + UndercastData.matchTimeHours + ":0" + UndercastData.matchTimeMin + ":" + UndercastData.matchTimeSec;
                    }
                } else {
                    if (UndercastData.matchTimeSec < 10) {
                        return "Match Time: \u00A7E" + UndercastData.matchTimeHours + ":" + UndercastData.matchTimeMin + ":0" + UndercastData.matchTimeSec;
                    } else {
                        return "Match Time: \u00A7E" + UndercastData.matchTimeHours + ":" + UndercastData.matchTimeMin + ":" + UndercastData.matchTimeSec;
                    }
                }
            }
        } else {
            if (UndercastData.matchTimeHours == 0) {
                if (UndercastData.matchTimeMin < 10) {
                    return "Match Time: \u00A7E0:0" + UndercastData.matchTimeMin;
                } else {
                    return "Match Time: \u00A7E0:" + UndercastData.matchTimeMin;
                }
            } else {
                if (UndercastData.matchTimeMin < 10) {
                    return "Match Time: \u00A7E" + UndercastData.matchTimeHours + ":0" + UndercastData.matchTimeMin;
                } else {
                    return "Match Time: \u00A7E" + UndercastData.matchTimeHours + ":" + UndercastData.matchTimeMin;
                }
            }
        }
    }

    /**
     * Sorts UndercastData.sortedServerInformation using the
     * UndercastData.sortindex
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
                @Override
                // just compare the first int of the array (player count)
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
                serverNames.add(c, UndercastData.serverInformation[c].name);
            }

            // sort the names
            Collections.sort(serverNames);

            // put the whole server into order
            for (int c = 0; c < serverNames.size(); c++) {
                for (int i = 0; i < UndercastData.serverCount; i++) {
                    if (serverNames.get(c).equals(UndercastData.serverInformation[i].name)) {
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
        if (!UndercastData.filterNames[UndercastData.filterIndex].equalsIgnoreCase("all")) {
            ServerType shownType = ServerType.Unknown;
            if (UndercastData.filterNames[UndercastData.filterIndex].equalsIgnoreCase("PA")) {
                shownType = ServerType.projectares;
            } else if (UndercastData.filterNames[UndercastData.filterIndex].equalsIgnoreCase("Blitz")) {
                shownType = ServerType.blitz;
            } else if (UndercastData.filterNames[UndercastData.filterIndex].equalsIgnoreCase("GS")) {
                shownType = ServerType.ghostsquadron;
            }

            // if the shownType detection failed: do nothing
            if (shownType == ServerType.Unknown) {
                return;
            }

            // extract the servers
            ArrayList<UndercastServer> filteredServers = new ArrayList<UndercastServer>(UndercastData.serverCount);
            for (int c = 0; c < UndercastData.serverCount; c++) {
                if (UndercastData.sortedServerInformation[c].type == shownType) {
                    filteredServers.add(UndercastData.sortedServerInformation[c]);
                }
            }

            UndercastData.filteredServerCount = filteredServers.size();
            // and put them back to the serverList
            for (int c = 0; c < filteredServers.size(); c++) {
                UndercastData.sortedServerInformation[c] = filteredServers.get(c);
            }
        }
    }

    public static int getOnlineFriends() {
        int number = 0;
        for (Iterator<String> ir = UndercastData.friends.keySet().iterator(); ir.hasNext();) {
            String key = ir.next();
            String value = UndercastData.friends.get(key);
            if (!value.equals("offline")) {
                number++;
            }
        }
        return number;
    }

    public static boolean isTeamkill(String unstripedMessage, String player1, String player2) {
        char colorchar1, colorchar2;
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
        if (UndercastData.team == Teams.Observers && UndercastData.kills == 0 && UndercastData.deaths == 0 && UndercastConfig.realtimeStats) {
            if (UndercastConfig.showTotalKills) {
                str = "Total Kills: \u00A7a" + (int) (UndercastData.getKills() + UndercastData.stats.kills);
            } else {
                str = "Kills: \u00A7a" + (int) (UndercastData.getKills());
            }
        } else {
            str = "Kills: \u00A7a" + (int) UndercastData.getKills();
            if (UndercastConfig.showTotalKills && UndercastConfig.realtimeStats) {
                str += "\u00A7f/\u00A7a" + (int) (UndercastData.getKills() + UndercastData.stats.kills);
            }
        }
        return str;
    }

    public static String getDeathDisplayString() {
        String str;
        if (UndercastData.team == Teams.Observers && UndercastData.kills == 0 && UndercastData.deaths == 0 && UndercastConfig.realtimeStats) {
            if (UndercastConfig.showTotalKills) {
                str = "Total Deaths: \u00A74" + (int) (UndercastData.getDeaths() + UndercastData.stats.deaths);
            } else {
                str = "Deaths: \u00A74" + (int) (UndercastData.getDeaths());
            }
        } else {
            str = "Deaths: \u00A74" + (int) UndercastData.getDeaths();
            if (UndercastConfig.showTotalKills && UndercastConfig.realtimeStats) {
                str += "\u00A7f/\u00A74" + (int) (UndercastData.getDeaths() + UndercastData.stats.deaths);
            }
        }
        return str;
    }

    public static String getKDDisplayString() {
        String str;
        if (UndercastData.team == Teams.Observers && UndercastData.kills == 0 && UndercastData.deaths == 0 && UndercastConfig.realtimeStats) {
            if (UndercastConfig.showTotalKills) {
                str = "Total K/D: \u00A73" + (UndercastData.stats.kd);
            } else {
                str = "K/D: \u00A73" + (getKD());
            }
        } else {
            str = "K/D: \u00A73" + getKD();
            if (UndercastConfig.showTotalKills && UndercastConfig.realtimeStats) {
                str += "\u00A7f/\u00A73" + ((UndercastData.kills + UndercastData.stats.kills) / (UndercastData.deaths + UndercastData.stats.deaths));
                str = str.substring(0, str.lastIndexOf('.') + 4);
            }
        }
        return str;
    }

    public static String getKKDisplayString() {
        String str;
        if (UndercastData.team == Teams.Observers && UndercastData.kills == 0 && UndercastData.deaths == 0 && UndercastConfig.realtimeStats) {
            if (UndercastConfig.showTotalKills) {
                str = "Total K/K: \u00A73" + (UndercastData.stats.kk);
            } else {
                str = "K/K: \u00A73" + (getKK());
            }
        } else {
            str = "K/K: \u00A73" + getKK();
            if (UndercastConfig.showTotalKills && UndercastConfig.realtimeStats) {
                str += "\u00A7f/\u00A73" + (((UndercastData.kills + UndercastData.stats.kills) / (UndercastData.killed + UndercastData.stats.getKilled())));
                str = str.substring(0, str.lastIndexOf('.') + 4);
            }
        }
        return str;
    }

}
