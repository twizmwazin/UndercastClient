package net.minecraft.src;
//You may not release this source under any condition, it must be linked to this page
//You may recompile and publish as long as skipperguy12 and Guru_Fraser are given credit
//You may not claim this to be your own
//You may not remove these comments

import java.util.ArrayList;
import java.awt.Color;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.Minecraft;
import net.minecraft.src.BaseMod;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.EntityPlayerSP;
import net.minecraft.src.KeyBinding;
import net.minecraft.src.MLProp;
import net.minecraft.src.ModLoader;
import net.minecraft.src.NetClientHandler;
import net.minecraft.src.StringUtils;
import net.minecraft.src.World;

public class mod_Ares extends BaseMod {
	protected String username = "Not_Found";
	protected Minecraft mc = Minecraft.getMinecraft();
	private boolean deathScreenActive;
	
	@MLProp(name = "serverDomain", info = "The base domain of the ares server")
	public static final String serverDomain = "oc.tc";
	
	@MLProp(name = "showFPS", info = "true = Show FPS in Gui, false = Doesn't show it.")
	public static String showFPS = "true";

	@MLProp(name = "showKills", info = "true = Show Kills in Gui, false = Doesn't show it.")
	public static String showKills = "true";

	@MLProp(name = "showDeaths", info = "true = Show Deaths in Gui, false = Doesn't show it.")
	public static String showDeaths = "true";

	@MLProp(name = "showKilled", info = "true = Show Times killed via PVP not PVE in Gui, false = Doesn't show it")
	public static String showKilled = "true";

	@MLProp(name = "showServer", info = "true = Show what server you are on in Gui, false = Doesn't show it.")
	public static String showServer = "true";

	@MLProp(name = "showTeam", info = "true = Show what team your on in Gui, false = Doesn't show it.")
	public static String showTeam = "true";

	@MLProp(name = "showKD", info = "true = Show your Kill Death Ratio in Gui, false = Doesn't show it.")
	public static String showKD = "true";

	@MLProp(name = "showKK", info = "true = Show your Kill Killed Ratio in Gui, false = Doesn't show it.")
	public static String showKK = "true";

	@MLProp(name = "showFriends", info = "true = Show Friends Online in Gui, false = Doesn't show it.")
	public static String showFriends = "true";

	@MLProp(name = "showMap", info = "true = Show the current map you are playing in Gui, false = Doesn't show it.")
	public static String showMap = "true";

	@MLProp(name = "showStreak", info = "true = Show your kill streak in Gui, false = Doesn't show it.")
	public static String showStreak = "true";
	
	@MLProp(name = "showGuiChat", info = "true = Show gui when chat is open, false = Doesn't show it.")
	public static String showGuiChat = "true";	
	
	@MLProp(name = "showGuiMulti", info = "true = Show Ares server gui in the, false = Doesn't show it.")
	public static String showGuiMulti = "true";	

	@Override
	public String getVersion() {
		return "1.5";
	}

	@Override
	public void load() {
		ModLoader.setInGUIHook(this, true, false);
		ModLoader.setInGameHook(this, true, false);

		new AresVariablesHandler(true);
		ModLoader.registerKey(this, AresVariablesHandler.getKeybind(), false);
		ModLoader.registerKey(this, AresVariablesHandler.getKeybind2(), false);
		ModLoader.addLocalization("keybind", "gui");

		// start thread listener
		new AresGuiListener().start();
	}

	/**
	 * On client chat event this is called.
	 * Send all the info to the AresChatHandler
	 * NOTE: only sends none global ares messages
	 */
	public void clientChat(String var1) {
		Minecraft mc = ModLoader.getMinecraftInstance();
		EntityPlayer player = mc.thePlayer;
		username = mc.thePlayer.username;
		String message = StringUtils.stripControlCodes(var1);
		//stop global msg to go through
		if (!message.startsWith("<"))
			new AresChatHandler(message, username, player);
	}

	/**
	 * On game tick this is called.
	 * Draws the gui ingame based on the config file
	 */
	public boolean onTickInGame(float time, Minecraft mc) {
		//if the game over screen is active then you have died
		//if it is the first time it is active count a death
		//if it is not don't do anything
		if(mc.currentScreen instanceof GuiGameOver){
			if(!deathScreenActive){
				AresVariablesHandler.addDeaths(1);
				AresVariablesHandler.setKillstreak(0);
				deathScreenActive=true;
			}
		}else
			deathScreenActive=false;
		//get debug info for the fps
		String fps = mc.debug.split(",")[0];
		int height = 2;
		//if the gui is enabled display
		//if chat is open and config says yes then show gui
		if (AresVariablesHandler.guiShowing() && 
				(mc.inGameHasFocus || this.showGuiChat.toString().equals("true") && mc.currentScreen instanceof GuiChat)) {
			//show fps
			if (this.showFPS.toString().equals("true")) {
				mc.fontRenderer.drawStringWithShadow(fps, 2, height,
						0xffff);
				height += 8;

			}
		}
		//if on Ares server then display this info.
		//if chat is open and config says yes then show gui
		if (AresVariablesHandler.isPlayingAres() == true && AresVariablesHandler.guiShowing() == true && 
				(mc.inGameHasFocus || this.showGuiChat.toString().equals("true") && mc.currentScreen instanceof GuiChat) ) {
			// Server display
			if (this.showServer.toString().equals("true")) {
				mc.fontRenderer
						.drawStringWithShadow("Server: \u00A76"
								+ AresVariablesHandler.getServer(), 2, height,
								16777215);
				height += 8;
			}

			// Team display (based on color)
			if (this.showTeam.toString().equals("true")) {

				if (AresVariablesHandler.getTeam().equalsIgnoreCase("red team")) {
					mc.fontRenderer.drawStringWithShadow("Team: "
							+ AresVariablesHandler.getTeam(), 2, height,
							0x990000);
					height += 8;
				} else if (AresVariablesHandler.getTeam().equalsIgnoreCase("blue team")) {
					mc.fontRenderer.drawStringWithShadow("Team: "
							+ AresVariablesHandler.getTeam(), 2, height,
							0x0033FF);
					height += 8;
				} else if (AresVariablesHandler.getTeam().equalsIgnoreCase("purple team")) {
					mc.fontRenderer.drawStringWithShadow("Team: "
							+ AresVariablesHandler.getTeam(), 2, height,
							0x9933CC);
					height += 8;
				} else if (AresVariablesHandler.getTeam().equalsIgnoreCase("cyan team")) {
					mc.fontRenderer.drawStringWithShadow("Team: "
							+ AresVariablesHandler.getTeam(), 2, height,
							0x00FFFF);
					height += 8;
				} else if (AresVariablesHandler.getTeam().equalsIgnoreCase("lime team")) {
					mc.fontRenderer.drawStringWithShadow("Team: "
							+ AresVariablesHandler.getTeam(), 2, height,
							0x00FF00);
					height += 8;
				} else if (AresVariablesHandler.getTeam().equalsIgnoreCase("yellow team")) {
					mc.fontRenderer.drawStringWithShadow("Team: "
							+ AresVariablesHandler.getTeam(), 2, height,
							0xFFFF00);
					height += 8;
				} else if (AresVariablesHandler.getTeam().equalsIgnoreCase("green team")) {
					mc.fontRenderer.drawStringWithShadow("Team: "
							+ AresVariablesHandler.getTeam(), 2, height,
							0x006600);
					height += 8;
				} else if (AresVariablesHandler.getTeam().equalsIgnoreCase("orange team")) {
					mc.fontRenderer.drawStringWithShadow("Team: "
							+ AresVariablesHandler.getTeam(), 2, height,
							0xFF9900);
					height += 8;
				} else if (AresVariablesHandler.getTeam().equalsIgnoreCase("Observers")) {
					mc.fontRenderer.drawStringWithShadow("Team: "
							+ AresVariablesHandler.getTeam(), 2, 18, 0x00FFFF);
					height += 8;
				} else {
					mc.fontRenderer.drawStringWithShadow("Team: "
							+ AresVariablesHandler.getTeam(), 2, 18, 0x606060);
					height += 8;
				}
			}
			// Friend display:
			if (this.showFriends.toString().equals("true")) {
				mc.fontRenderer.drawStringWithShadow("Friends Online: \u00A73"
						+ AresVariablesHandler.getFriends(), 2, height,
						16777215);
				height += 8;
			}
			// Map fetcher:
			if (this.showMap.toString().equals("true")) {
				if (AresVariablesHandler.getMap() != null) {
					mc.fontRenderer.drawStringWithShadow("Current Map: \u00A7d"
							+ AresVariablesHandler.getMap(), 2, height,
							16777215);
					height += 8;
				} else {
					AresVariablesHandler.setMap("Fetching...");
					mc.fontRenderer.drawStringWithShadow("Current Map: \u00A78"
							+ AresVariablesHandler.getMap(), 2, height,
							16777215);
					height += 8;
				}
			}
			//Show KD Ratio
			if (this.showKD.equals("true")) {
				mc.fontRenderer.drawStringWithShadow("K/D: \u00A73"
						+ AresCustomMethods.getKD(), 2, height,
						16777215);
				height += 8;
			}
			//show KK Ratio
			if (this.showKK.equals("true")) {
				mc.fontRenderer.drawStringWithShadow("K/K: \u00A73"
						+ AresCustomMethods.getKK(), 2, height,
						16777215);
				height += 8;
			}
			//show amount of kills
			if (this.showKills.toString().equals("true")) {
				mc.fontRenderer.drawStringWithShadow("Kills: \u00A7a"
						+ AresVariablesHandler.getKills(), 2, height, 16777215);
				height += 8;
			}
			//show amount of deaths
			if (this.showDeaths.toString().equals("true")) {
				mc.fontRenderer
						.drawStringWithShadow("Deaths: \u00A74"
								+ AresVariablesHandler.getDeaths(), 2, height,
								16777215);
				height += 8;
			}
			// Kill Streak display
			if (this.showStreak.toString().equals("true")) {
				mc.fontRenderer.drawStringWithShadow(
						"Current Killstreak: \u00A75"
								+ AresVariablesHandler.getKillstreak()+"/"+AresVariablesHandler.getLargestKillstreak(), 2,
						height, 16777215);
				height += 8;
			}
		}
		return true;
	}

	/**
	 * Called on client connect to the server
	 * Sets variables if the server is a Ares server
	 */
	public void clientConnect(NetClientHandler var1) {
		AresVariablesHandler.setTeam("Observers");
		System.out.println("Client successfully connected to "+ var1.getNetManager().getSocketAddress().toString());

		if (var1.getNetManager().getSocketAddress().toString().contains(serverDomain)) {
			// What happens if logs into project ares
			AresVariablesHandler.guiShowing(true);
			System.out.println("Connected to: "+ var1.getNetManager().getSocketAddress().toString()+ " Ares mod activated!");
			AresVariablesHandler.setTeam("Observers");
			AresVariablesHandler.isPlayingAres(true);
			AresVariablesHandler.setServer(AresCustomMethods.getServer(var1.getNetManager().getSocketAddress().toString()));
			AresCustomMethods.getMap();
		} else {
			AresVariablesHandler.guiShowing(false);
		}

	}
	
	/**
	 * Called when client disconnects.
	 * Resets all the values
	 */
	public void onDisconnect(NetClientHandler handler) {
		AresVariablesHandler.isPlayingAres(false);
		AresVariablesHandler.guiShowing(false);
		AresVariablesHandler.setTeam("Observers");
		AresVariablesHandler.setKillstreak(0);
		AresVariablesHandler.setMap("Attempting to fetch map...");
	}

	/**
	 * Called when a key is pressed.
	 * Used to activate the gui ect.
	 */
	public void keyboardEvent(KeyBinding keybinding) {
		Minecraft mc = ModLoader.getMinecraftInstance();
		World world = mc.theWorld;
		EntityPlayerSP player = mc.thePlayer;
		if (mc.inGameHasFocus) {
			if (keybinding == AresVariablesHandler.getKeybind()) {
				if (AresVariablesHandler.guiShowing()) {
					AresVariablesHandler.guiShowing(false);
				} else {
					AresVariablesHandler.guiShowing(true);
				}
			} else if (keybinding == AresVariablesHandler.getKeybind2()) {
				ModLoader.openGUI(mc.thePlayer, new Ares_ServerGUI(true));
			}
		}
	}
}
