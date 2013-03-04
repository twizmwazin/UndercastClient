package net.minecraft.src;

import java.util.ArrayList;
import java.util.HashSet;

import org.lwjgl.input.Keyboard;

public class AresVariablesHandler 
{
	private static String map;
	private static double kills;
	private static double deaths;
	private static double killed;
	private static int killstreak;
	private static HashSet<String> friends = new HashSet<String>();
	private static String server;
	private static String team;
	private static boolean isPA = false;
	private static boolean gui;
	private static KeyBinding keybind = new KeyBinding("gui", Keyboard.KEY_F6);
	
	public AresVariablesHandler(boolean b)
	{
		if(b)
		{
			setMap("Fetching...");
			setKills(0);
			setDeaths(0);
			setKilled(0);
			setTeam("Observers");
			guiShowing(true);
		}
	}
	
	protected static String getMap()
	{
		return map;
	}
	
	protected static void setMap(String s)
	{
		map = s;
	}
	
	protected static double getKills()
	{
		return kills;
	}
	
	protected static void setKills(double d)
	{
		kills = d;
	}
	
	protected static void addKills(double d)
	{
		kills += d;
	}
	
	protected static double getDeaths()
	{
		return deaths;
	}
	
	protected static void setDeaths(double d)
	{
		deaths = d;
	}
	
	protected static void addDeaths(double d)
	{
		deaths += d;
	}
	
	protected static double getKilled()
	{
		return killed;
	}
	
	protected static void setKilled(double d)
	{
		killed = d;
	}
	
	protected static void addKilled(double d)
	{
		killed += d;
	}
	
	protected static int getKillstreak()
	{
		return killstreak;
	}
	
	protected static void setKillstreak(int i)
	{
		killstreak = i;
	}
	
	protected static void addKillstreak(int i)
	{
		killstreak += i;
	}
	
	protected static int getFriends()
	{
		return friends.size();
	}
	
	protected static void addFriend(String s)
	{
		friends.add(s);
	}
	
	protected static void removeFriend(String s)
	{
		friends.remove(s);
	}
	
	protected static void clearFriends()
	{
		friends.clear();
	}
	
	protected static boolean isFriend(String s)
	{
		return friends.contains(s);
	}
	
	protected static String getServer()
	{
		return server;
	}
	
	protected static void setServer(String s)
	{
		server = s;
	}
	
	protected static String getTeam()
	{
		return team;
	}
	
	protected static void setTeam(String s)
	{
		team = s;
	}
	
	protected static boolean isPlayingAres()
	{
		return isPA;
	}
	
	protected static boolean isPlayingAres(boolean b)
	{
		isPA = b;
		return isPA;
	}
	
	protected static boolean guiShowing()
	{
		return gui;
	}
	
	protected static boolean guiShowing(boolean b)
	{
		gui = b;
		return gui;
	}
	
	protected static KeyBinding getKeybind()
	{
		return keybind;
	}
	
	protected static void setKeybind(KeyBinding k)
	{
		keybind = k;
	}
	
	protected static void setKeybind(String name, int key)
	{
		keybind = new KeyBinding(name, key);
	}

}
