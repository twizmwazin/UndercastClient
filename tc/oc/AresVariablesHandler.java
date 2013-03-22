package tc.oc;

//You may not release this source under any condition, it must be linked to this page
//You may recompile and publish as long as skipperguy12 and Guru_Fraser are given credit
//You may not claim this to be your own
//You may not remove these comments

import java.util.ArrayList;
import java.util.HashSet;

import org.lwjgl.input.Keyboard;

import net.minecraft.src.KeyBinding;
import net.minecraft.src.mod_Ares;

public class AresVariablesHandler {
	private static String map;
	private static double kills;
	private static double deaths;
	private static double killed;
	private static int killstreak;
	private static int largestKillstreak;
	private static HashSet<String> friends = new HashSet<String>();
	private static String server;
	private static String team;
	private static boolean isPA = false;

	private static boolean gui;
	private static KeyBinding keybind;
	private static KeyBinding keybind2;

	public AresVariablesHandler() {
		setMap("Fetching...");
		setKills(0);
		setDeaths(0);
		setKilled(0);
		setLargestKillstreak(0);
		setTeam("Observers");
		guiShowing(true);
		keybind = new KeyBinding("gui", Keyboard.getKeyIndex(mod_Ares.keyGui
				.toString()));
		keybind2 = new KeyBinding("inGameGui",
				Keyboard.getKeyIndex(mod_Ares.keyGui2.toString()));
	}

	public static String getMap() {
		return map;
	}

	public static void setMap(String s) {
		map = s;
	}

	public static double getKills() {
		return kills;
	}

	public static void setKills(double d) {
		kills = d;
	}

	public static void addKills(double d) {
		kills += d;
	}

	public static double getDeaths() {
		return deaths;
	}

	public static void setDeaths(double d) {
		deaths = d;
	}

	public static void addDeaths(double d) {
		deaths += d;
	}

	public static double getKilled() {
		return killed;
	}

	public static void setKilled(double d) {
		killed = d;
	}

	public static void addKilled(double d) {
		killed += d;
	}

	public static int getKillstreak() {
		return killstreak;
	}

	public static void setKillstreak(int i) {
		if (largestKillstreak < killstreak)
			largestKillstreak = killstreak;
		killstreak = i;
	}

	public static void addKillstreak(int i) {
		killstreak += i;
		if (largestKillstreak < killstreak)
			largestKillstreak = killstreak;
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

	public static String getServer() {
		return server;
	}

	public static void setServer(String s) {
		server = s;
	}

	public static String getTeam() {
		return team;
	}

	public static void setTeam(String s) {
		team = s;
	}

	public static boolean isPlayingAres() {
		return isPA;
	}

	public static boolean isPlayingAres(boolean b) {
		isPA = b;
		return isPA;
	}

	public static boolean guiShowing() {
		return gui;
	}

	public static boolean guiShowing(boolean b) {
		gui = b;
		return gui;
	}

	public static KeyBinding getKeybind() {
		return keybind;
	}

	public static void setKeybind(KeyBinding k) {
		keybind = k;
	}

	public static KeyBinding getKeybind2() {
		return keybind2;
	}

	public static void setKeybind2(KeyBinding k) {
		keybind2 = k;
	}

	public static void setKeybind(String name, int key) {
		keybind = new KeyBinding(name, key);
	}

	public static int getLargestKillstreak() {
		return largestKillstreak;
	}

	public static void setLargestKillstreak(int killstreak) {
		largestKillstreak = killstreak;
	}
}
