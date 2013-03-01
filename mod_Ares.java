//You may not release this source under any condition, it must be linked to this page
//You may recompile and publish as long as skipperguy12 and Guru_Fraser are given credit
//You may not claim this to be your own
//You may not remove these comments
package net.minecraft.src;

import java.util.ArrayList;
import java.awt.Color;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import net.minecraft.client.Minecraft;

public class mod_Ares extends BaseMod {
	protected static mod_Ares pa;

	public boolean onPA = false;

	private String[] FPS;
	private String fps;

	private int friendCount;
	public static final String serverDomain = "oc.tc";
	private ArrayList<String> friends;
	protected double kills;
	protected double deaths;
	protected double killed;
	public String map;
	private String IP;
	private String serverName;
	protected String team;
	private boolean showGUI;
	protected int killStreak;
	public static World world;
	protected String username = "Not_Found";
	protected Minecraft mc = Minecraft.getMinecraft();
	//public int height = mc.displayHeight;
	//public int width = mc.displayWidth;
	public int height = 2;


	public KeyBinding o = new KeyBinding("togglegui", 24);


	@MLProp(name="showFPS", info="true = Show FPS in Gui, false = Doesn't show it.")
	public static String showFPS = "true";

	@MLProp(name="showKills", info="true = Show Kills in Gui, false = Doesn't show it.")
	public static String showKills = "true";

	@MLProp(name="showDeaths", info="true = Show Deaths in Gui, false = Doesn't show it.")
	public static String showDeaths = "true";

	@MLProp(name="showKilled", info="true = Show Times killed via PVP not PVE in Gui, false = Doesn't show it")
	public static String showKilled = "true";

	@MLProp(name="showServer", info="true = Show what server you are on in Gui, false = Doesn't show it.")
	public static String showServer = "true";

	@MLProp(name="showTeam", info="true = Show what team your on in Gui, false = Doesn't show it.")
	public static String showTeam = "true";

	@MLProp(name="showKD", info="true = Show your Kill Death Ratio in Gui, false = Doesn't show it.")
	public static String showKD = "true";

	@MLProp(name="showKK", info="true = Show your Kill Killed Ratio in Gui, false = Doesn't show it.")
	public static String showKK = "true";

	@MLProp(name="showFriends", info="true = Show Friends Online in Gui, false = Doesn't show it.")
	public static String showFriends = "true";

	@MLProp(name="showMap", info="true = Show the current map you are playing in Gui, false = Doesn't show it.")
	public static String showMap = "true";

	@MLProp(name="showStreak", info="true = Show your kill streak in Gui, false = Doesn't show it.")
	public static String showStreak = "true";
	EntityPlayer player = null;

	// TODO: Check for captured wools


	@Override
	public String getVersion()
	{
		return "1.4.7";
	}

	@Override
	public void load()
	{
		ModLoader.setInGUIHook(this, true, false);
		ModLoader.setInGameHook(this, true, false);

		this.friends = new ArrayList();
		this.friendCount = 0;
		this.showGUI = true;
		this.killStreak = 0;
		ModLoader.registerKey(this, o, false);
		ModLoader.addLocalization("o","Toggle Gui");
	}


	public void clientChat(String var1)
	{
		Minecraft mc = ModLoader.getMinecraftInstance();
		World world = mc.theWorld; //get the current world.
		EntityPlayer player = mc.thePlayer; //get the player entity.
		String message = StringUtils.stripControlCodes(var1);
		if (this.onPA)
		{
			username = mc.thePlayer.username.toString();
			if(username == null)
			{
				return;
			}
			if (!message.startsWith("<") && message.contains(" joined the game")) 
			{
				message = message.replace(" joined the game", "");

				if (!this.friends.contains(message))
				{
					this.friends.add(message.toString());
					++this.friendCount;
					if(this.showFriends.toString().equals("true"))
					{
						player.addChatMessage("One of your friends just logged in, "
								+ message
								+ ". There are now "
								+ this.friendCount + " online");
					}

				}
			}

			else if(!message.startsWith("<") && message.equalsIgnoreCase("welcome to project ares"))
			{
				this.map = null;
				this.map = AresCustomMethods.methods.getMap();
			}
			else if(!message.startsWith("<") && message.contains("Now Playing"))
			{
				message = message.replace("Now Playing ", "");
				map = message.split(" by ")[0];
			}
			else if (!message.startsWith("<") && message.contains("left the game"))
			{
				message = message.replace(" left the game", "");
				if (this.friends.contains(message))
				{
					--this.friendCount;
					this.friends.remove(message);
					if(this.showFriends.toString().equals("true"))
					{	
						player.addChatMessage("One of your friends just left, " + message + ". There are now " + this.friendCount + " online");
					}
				}
			}
			else if (message.startsWith("<") || !message.contains("was shot by " + username) && !message.contains("was blown up by " + username) && !message.contains("was slain by " + username))
			{
				if (!message.startsWith("<") && message.startsWith(username + " was"))
				{
					++this.killed;
					++this.deaths;
					AresCustomMethods.methods.endKillStreak();
				}
				else if (!message.startsWith("<") && message.startsWith(username))
				{
					AresCustomMethods.methods.endKillStreak();
					++this.deaths;
				}
			}
			else if(message.startsWith("<") || message.contains("was shot by " + username) && message.contains("was blown up by " + username) && message.contains("was slain by " + username))
			{
				this.kills++;
				this.killStreak++;
			}
			else if (!message.startsWith("<") && var1.contains("You joined the"))
			{
				this.kills = 0.0D;
				this.killed = 0.0D;
				this.deaths = 0.0D;
				this.team = message.replace("You joined the ", "");
			}

			else if (!message.startsWith("<") && message.toLowerCase().contains("cycling to") && message.contains("1 second"))
			{
				AresCustomMethods.methods.endGame(player);
			}
		}
	}

	public boolean onTickInGame(float time, Minecraft mc)
	{

		world = mc.theWorld;

		this.fps = mc.debug.split(",")[0];
		height = 2;
		if(showGUI)
		{
			if(this.showFPS.toString().equals("true"))
			{
				mc.fontRenderer.drawStringWithShadow(this.fps, 2, height, 0xffff);
				height += 8;
				
			}
		}

		if (onPA == true && showGUI == true)
		{
			//Server display
			if(this.showServer.toString().equals("true"))
			{
				mc.fontRenderer.drawStringWithShadow("Server: \u00A76"
						+ this.serverName, 2, height, 16777215);
				height +=8;
			}

			//Team display
			if(this.showTeam.toString().equals("true"))
			{


				if(this.team.equalsIgnoreCase("red team"))
				{
					mc.fontRenderer.drawStringWithShadow("Team: "
							+ this.team, 2, height, 0x990000);
					height +=8;
				}
				else if(this.team.equalsIgnoreCase("blue team"))
				{
					mc.fontRenderer.drawStringWithShadow("Team: "
							+ this.team, 2, height, 0x0033FF);
					height +=8;
				}
				else if(this.team.equalsIgnoreCase("purple team"))
				{
					mc.fontRenderer.drawStringWithShadow("Team: "
							+ this.team, 2, height, 0x9933CC);
					height +=8;
				}
				else if(this.team.equalsIgnoreCase("cyan team"))
				{
					mc.fontRenderer.drawStringWithShadow("Team: "
							+ this.team, 2, height, 0x00FFFF);	
					height +=8;
				}
				else if(this.team.equalsIgnoreCase("lime team"))
				{
					mc.fontRenderer.drawStringWithShadow("Team: "
							+ this.team, 2, height, 0x00FF00);	
					height +=8;
				}
				else if(this.team.equalsIgnoreCase("yellow team"))
				{
					mc.fontRenderer.drawStringWithShadow("Team: "
							+ this.team, 2, height, 0xFFFF00);
					height +=8;
				}
				else if(this.team.equalsIgnoreCase("orange team"))
				{
					mc.fontRenderer.drawStringWithShadow("Team: "
							+ this.team, 2, height, 0x006600);
				}
				else if(this.team.equalsIgnoreCase("orange team"))
				{
					mc.fontRenderer.drawStringWithShadow("Team: "
							+ this.team, 2, height, 0xFF9900);
					height +=8;
				}
				else if(this.team.equalsIgnoreCase("Observers"))
				{
					mc.fontRenderer.drawStringWithShadow("Team: "
							+ this.team, 2, 18, 0x00FFFF);
					height +=8;
				}
			}
			// Friend display:
			if(this.showFriends.toString().equals("true"))
			{
				mc.fontRenderer.drawStringWithShadow("Friends Online: \u00A73"
						 + this.friendCount, 2, height, 16777215);
				height +=8;
			}


			// Map fetcher:
			if(this.showMap.toString().equals("true"))
			{
				if (this.map != null)
				{
					mc.fontRenderer.drawStringWithShadow(
							"Current Map: \u00A7d" + this.map, 2, height, 16777215);
					height +=8;
				}
				else
				{
					this.map = "Fetching...";
					mc.fontRenderer.drawStringWithShadow(
							"Current Map: \u00A78" + this.map, 2, height, 16777215);
					height +=8;
				}
			}
			// Kills, deaths, K/D and KK
			if(this.showKD.equals("true"))
			{
				mc.fontRenderer.drawStringWithShadow("K/D: \u00A73" + AresCustomMethods.methods.getKD(kills, deaths), 2, height,
						16777215);
				height +=8;
			}
			if(this.showKK.equals("true"))
			{
				mc.fontRenderer.drawStringWithShadow("K/K: \u00A73" + AresCustomMethods.methods.getKK(kills, killed), 2, height,
						16777215);
				height +=8;
			}

			if(this.showKills.toString().equals("true"))
			{
				mc.fontRenderer.drawStringWithShadow("Kills: \u00A7a" + this.kills, 2, height,
						16777215);
				height +=8;
			}
			if(this.showDeaths.toString().equals("true"))
			{
				mc.fontRenderer.drawStringWithShadow("Deaths: \u00A74" + this.deaths, 2, height,
						16777215);
				height +=8;
			}

			if(this.showStreak.toString().equals("true"))
			{

				//Kill Streak display
				mc.fontRenderer.drawStringWithShadow("Current Killstreak: \u00A75"
						+ this.killStreak, 2, height, 16777215);
				height +=8;
			}
		}
		return true;
	}

	public void clientConnect(NetClientHandler var1)
	{
		this.team = "Observers";
		System.out.println("Client successfully connected to "
				+ var1.getNetManager().getSocketAddress().toString());

		if (var1.getNetManager().getSocketAddress().toString().contains(serverDomain))
		{
			//What happens if logs into project ares
			this.showGUI = true;
			System.out.println("Connected to: " + var1.getNetManager().getSocketAddress().toString() + "Ares mod activated!");
			this.team = "Observers";
			onPA = true;
			serverName = AresCustomMethods.methods.getServer(var1.getNetManager().getSocketAddress().toString());
		}
		else
		{
			this.showGUI = false;
		}

	}


	public void onDisconnect(NetClientHandler handler) 
	{
		this.team = "Observers";
		if (onPA)
			onPA = false;
		this.showGUI = false;
		this.team = "Observers";
		AresCustomMethods.methods.endKillStreak();
		this.map = "Attempting to fetch map...";
	}

	public void keyboardEvent(KeyBinding keybinding)
	{
		Minecraft mc = ModLoader.getMinecraftInstance();
		World world = mc.theWorld;
		EntityPlayerSP player = mc.thePlayer;
		if(!(mc.currentScreen instanceof GuiChat))
		{
			if (keybinding == o)
			{
				if(showGUI==true)
				{
					showGUI = false;
				}
				else
				{
					showGUI = true;
				}
			}
		}
	}
}
