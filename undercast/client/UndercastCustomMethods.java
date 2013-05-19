package undercast.client;
//You may not release this source under any condition, it must be linked to this page
//You may recompile and publish as long as skipperguy12 and Guru_Fraser are given credit
//You may not claim this to be your own
//You may not remove these comments

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import net.minecraft.client.Minecraft;

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
        } else {
            if (k > 0 && d == 0) {
                return k;
            } else {
                if (k == d && k > 0) {
                    return 1D;
                } else {
                    return round(k / d);
                }
            }
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
        } else {
            if (k > 0 && kk == 0) {
                return k;
            } else {
                if (k == kk && kk > 0) {
                    return 1D;
                } else {
                    return round(k / kk);
                }
            }
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
        UndercastData.isGameOver = false;

        // stop the timer and reset it
        try {
            UndercastData.matchTimer.stop();
        } catch (Exception ignored) {
        }
        //and start one which starts from 0
        UndercastData.incrementMatchTime = true;
        UndercastData.matchTimeHours = 0;
        UndercastData.matchTimeMin = 0;
        UndercastData.matchTimeSec = 0;
    }
    public static String getMatchTimeString() {
        // if it's a map with time limit and it's enabled in the config
        if(!UndercastData.incrementMatchTime && UndercastConfig.showMatchTimeSeconds) {
            if(UndercastData.matchTimeHours == 0 && UndercastData.matchTimeMin == 0) {
                if(UndercastData.matchTimeSec < 10) {
                    return "Match Time: \u00A7E0:0" + UndercastData.matchTimeSec;
                } else {
                    return "Match Time: \u00A7E0:" + UndercastData.matchTimeSec;
                }
            } else if (UndercastData.matchTimeHours == 0) {
                if(UndercastData.matchTimeSec < 10) {
                    return "Match Time: \u00A7E" + UndercastData.matchTimeMin + ":0" + UndercastData.matchTimeSec;
                } else {
                    return "Playing Time: \u00A7E" + UndercastData.matchTimeMin + ":" + UndercastData.matchTimeSec;
                }
            } else {
                if(UndercastData.matchTimeMin < 10) {
                    if(UndercastData.matchTimeSec < 10) {
                        return "Match Time: \u00A7E" + UndercastData.matchTimeHours + ":0" + UndercastData.matchTimeMin + ":0" + UndercastData.matchTimeSec;
                    } else {
                        return "Match Time: \u00A7E" + UndercastData.matchTimeHours + ":0" + UndercastData.matchTimeMin + ":" + UndercastData.matchTimeSec;
                    }
                } else {
                    if(UndercastData.matchTimeSec < 10) {
                        return "Match Time: \u00A7E" + UndercastData.matchTimeHours + ":" + UndercastData.matchTimeMin + ":0" + UndercastData.matchTimeSec;
                    } else {
                        return "Match Time: \u00A7E" + UndercastData.matchTimeHours + ":" + UndercastData.matchTimeMin + ":" + UndercastData.matchTimeSec;
                    }
                }
            }
        } else {
            if (UndercastData.matchTimeHours == 0) {
                if(UndercastData.matchTimeMin < 10) {
                    return "Match Time: \u00A7E0:0" + UndercastData.matchTimeMin;
                } else {
                    return "Match Time: \u00A7E0:" + UndercastData.matchTimeMin;
                }
            } else {
                if(UndercastData.matchTimeMin < 10) {
                    return "Match Time: \u00A7E" + UndercastData.matchTimeHours + ":0" + UndercastData.matchTimeMin;
                } else {
                    return "Match Time: \u00A7E" + UndercastData.matchTimeHours + ":" + UndercastData.matchTimeMin;
                }
            }
        }
    }
 
    /**
     * This is used in order to only display hours if you play at least for one
     */
    public static String getPlayingTimeString() {
        if(UndercastData.playTimeHours == 0) {
            if(UndercastData.playTimeMin < 10) {
                return "Playing Time: \u00A7E0:0" + UndercastData.playTimeMin;
            } else {
                return "Playing Time: \u00A7E0:" + UndercastData.playTimeMin;
            }
        } else {
            if(UndercastData.playTimeMin < 10) {
                return "Playing Time: \u00A7E" + UndercastData.playTimeHours + ":0" + UndercastData.playTimeMin;
            } else {
                return "Playing Time: \u00A7E" + UndercastData.playTimeHours + ":" + UndercastData.playTimeMin;
            }
        }
    }

    /**
     * Sorts UndercastData.sortedServerInformation using the
     * UndercastData.sortindex
     */
    public static void sortServers() {
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
    }
}
