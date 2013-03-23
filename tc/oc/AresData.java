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
    public static String team;
    public static boolean isPA;

    public static boolean guiShowing;
    public static KeyBinding keybind;
    public static KeyBinding keybind2;

    public AresData() {
        map = "Fetching...";
        kills = 0;
        deaths = 0;
        killed = 0;
        largestKillstreak = 0;
        team = "Observers";
        guiShowing = true;
        keybind = new KeyBinding("gui", Keyboard.getKeyIndex(mod_Ares.keyGui));
        keybind2 = new KeyBinding("inGameGui", Keyboard.getKeyIndex(mod_Ares.keyGui2));
    }

    public static void addKills(double d) {
        kills += d;
    }

    public static void addDeaths(double d) {
        deaths += d;
    }

    public static void addKilled(double d) {
        killed += d;
    }

    public static void addKillstreak(int i) {
        killstreak += i;
        if (largestKillstreak < killstreak) {
            largestKillstreak = killstreak;
        }
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
}
