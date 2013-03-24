package tc.oc;

//You may not release this source under any condition, it must be linked to this page
//You may recompile and publish as long as skipperguy12 and Guru_Fraser are given credit
//You may not claim this to be your own
//You may not remove these comments

import net.minecraft.src.KeyBinding;
import net.minecraft.src.mod_Ares;
import org.lwjgl.input.Keyboard;

import java.util.HashSet;

public class AresData {
    //Data Varibles
    public static String map;
    public static double kills;
    public static double deaths;
    public static double killed;
    public static int killstreak;
    public static int largestKillstreak;
    public static HashSet<String> friends = new HashSet<String>();
    public static String server;
    public static Teams team;
    public static boolean isPA;

    public static boolean guiShowing;
    public static KeyBinding keybind;
    public static KeyBinding keybind2;
    public static KeyBinding keybind3;

    public static enum Teams {Red, Blue, Purple, Cyan, Lime, Yellow, Green, Orange, Observers}

    ;

    public AresData() {
        setMap("Fetching...");
        resetKills();
        resetDeaths();
        resetKilled();
        resetLargestKillstreak();
        setTeam(Teams.Observers);
        guiShowing = true;
        keybind = new KeyBinding("gui", Keyboard.getKeyIndex(mod_Ares.CONFIG.keyGui));
        keybind2 = new KeyBinding("inGameGui", Keyboard.getKeyIndex(mod_Ares.CONFIG.keyGui2));
        keybind3 = new KeyBinding("fullBright", Keyboard.getKeyIndex(mod_Ares.CONFIG.keyGui3));
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

    public static void setServer(String servers) {
        server = servers;
    }

    public static String getServer() {
        return server;
    }
}
