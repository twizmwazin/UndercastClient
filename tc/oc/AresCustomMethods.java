package tc.oc;
//You may not release this source under any condition, it must be linked to this page
//You may recompile and publish as long as skipperguy12 and Guru_Fraser are given credit
//You may not claim this to be your own
//You may not remove these comments

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
        if (k == d && k == 0)
        {
            return 0D;
        } else if (k > 0 && d == 0)
        {
            return k;
        } else if (k == d && k > 0)
        {
            return 1D;
        } else
        {
            return round(k / d);
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
        if (k == kk && k == 0)
        {
            return 0D;
        } else if (k > 0 && kk == 0)
        {
            return k;
        } else if (k == kk && kk > 0)
        {
            return 1D;
        } else
        {
            return round(k / kk);
        }
    }

    /**
     * Detects if the server is the lobby and resets values which need to be
     * reseted
     */
    public static void handleServerSwap() {
        if (AresData.server.equalsIgnoreCase("lobby"))
        {
            AresData.isLobby = true;
        } else
        {
            AresData.isLobby = false;
        }
        AresData.setTeam(AresData.Teams.Observers);

        AresData.resetDeaths();
        AresData.resetKills();
        AresData.resetKilled();
        AresData.resetKillstreak();
        AresData.resetLargestKillstreak();
    }
}
