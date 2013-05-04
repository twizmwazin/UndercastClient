package tc.oc;
//You may not release this source under any condition, it must be linked to this page
//You may recompile and publish as long as skipperguy12 and Guru_Fraser are given credit
//You may not claim this to be your own
//You may not remove these comments

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import net.minecraft.client.Minecraft;

public class AresCustomMethods {

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
        double k = AresData.getKills();
        double d = AresData.getDeaths();
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
        double k = AresData.getKills();
        double kk = AresData.getKilled();
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
        if (AresData.server.equalsIgnoreCase("lobby")) {
            AresData.isLobby = true;
        } else {
            AresData.isLobby = false;
        }
        AresData.setTeam(AresData.Teams.Observers);
        AresData.resetDeaths();
        AresData.resetKills();
        AresData.resetKilled();
        AresData.resetKillstreak();
        AresData.resetLargestKillstreak();
        AresData.isGameOver = false;
    }

    /**
     * This is used in order to only display hours if you play at least for one
     */
    public static String getPlayingTimeString() {
        if (AresData.playTimeHours == 0) {
            return "Playing Time: \u00A7E" + AresData.playTimeMin + "\u00A7Fmin";
        } else {
            return "Playing Time: \u00A7E" + AresData.playTimeHours + "\u00A7Fh \u00A7E" + AresData.playTimeMin + "\u00A7Fmin";
        }
    }

    /**
     * Sorts AresData.sortedServerInformation using the AresData.sortindex
     */
    public static void sortServers() {
        // if the index is moving to web then use the downloaded server list
        if (AresData.sortNames[AresData.sortIndex].equalsIgnoreCase("web")) {
            // just keep the order
            for (int c = 0; c < AresData.serverCount; c++) {
                AresData.sortedServerInformation[c] = AresData.serverInformation[c];
            }
        } // sort based on matchStatus
        else if (AresData.sortNames[AresData.sortIndex].equalsIgnoreCase("match")) {
            int index = 0;
            // Starting
            for (int c = 0; c < AresData.serverCount; c++) {
                if (AresData.serverInformation[c].matchState == AresData.MatchState.Starting) {
                    AresData.sortedServerInformation[index] = AresData.serverInformation[c];
                    index++;
                }
            }
            // Waiting
            for (int c = 0; c < AresData.serverCount; c++) {
                if (AresData.serverInformation[c].matchState == AresData.MatchState.Waiting) {
                    AresData.sortedServerInformation[index] = AresData.serverInformation[c];
                    index++;
                }
            }
            // Finished
            for (int c = 0; c < AresData.serverCount; c++) {
                if (AresData.serverInformation[c].matchState == AresData.MatchState.Finished) {
                    AresData.sortedServerInformation[index] = AresData.serverInformation[c];
                    index++;
                }
            }
            // Started
            for (int c = 0; c < AresData.serverCount; c++) {
                if (AresData.serverInformation[c].matchState == AresData.MatchState.Started) {
                    AresData.sortedServerInformation[index] = AresData.serverInformation[c];
                    index++;
                }
            }
            // Lobby 
            for (int c = 0; c < AresData.serverCount; c++) {
                if (AresData.serverInformation[c].matchState == AresData.MatchState.Lobby) {
                    AresData.sortedServerInformation[index] = AresData.serverInformation[c];
                    index++;
                }
            }
            // Unknown
            for (int c = 0; c < AresData.serverCount; c++) {
                if (AresData.serverInformation[c].matchState == AresData.MatchState.Unknown) {
                    AresData.sortedServerInformation[index] = AresData.serverInformation[c];
                    index++;
                }
            }
        } // sort based on player count
        else if (AresData.sortNames[AresData.sortIndex].equalsIgnoreCase("players")) {
            // copy the player counts to an int array 
            int[][] playerCounts = new int[AresData.serverCount][2];
            for (int c = 0; c < AresData.serverCount; c++) {
                playerCounts[c][0] = AresData.serverInformation[c].playerCount;
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


            for (int c = 0; c < AresData.serverCount; c++) {
                AresData.sortedServerInformation[c] = AresData.serverInformation[playerCounts[c][1]];
            }
        } // if the servers are being sorted abc sort the list and update
        else if (AresData.sortNames[AresData.sortIndex].equalsIgnoreCase("abc")) {
            // extract the server names to an Array list
            ArrayList<String> serverNames = new ArrayList<String>(AresData.serverCount);
            for (int c = 0; c < AresData.serverCount; c++) {
                serverNames.add(c, AresData.serverInformation[c].name);
            }

            // sort the names
            Collections.sort(serverNames);

            // put the whole server into order
            for (int c = 0; c < serverNames.size(); c++) {
                for (int i = 0; i < AresData.serverCount; i++) {
                    if (serverNames.get(c).equals(AresData.serverInformation[i].name)) {
                        AresData.sortedServerInformation[c] = AresData.serverInformation[i];
                        break;
                    }

                }
            }
        }
    }
}
