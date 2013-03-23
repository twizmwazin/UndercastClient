package net.minecraft.src;
//You may not release this source under any condition, it must be linked to this page
//You may recompile and publish as long as skipperguy12 and Guru_Fraser are given credit
//You may not claim this to be your own
//You may not remove these comments

import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Collections;

import tc.oc.AresChatHandler;
import tc.oc.AresCustomMethods;
import tc.oc.AresGuiListener;
import tc.oc.AresData;

import net.minecraft.client.Minecraft;
import tc.oc.server.AresConfig;
import tc.oc.server.GuiAresServers;

public class mod_Ares extends BaseMod {
	protected String username = "Not_Found";
	protected Minecraft mc = Minecraft.getMinecraft();
	private boolean deathScreenActive;
    public static ArrayList<String> servers = new ArrayList<String>();
    public static AresConfig config;
	
	public static String serverDomain;
	public static boolean showFPS;
	public static boolean showKills;
	public static boolean showDeaths;
	public static boolean showKilled;
	public static boolean showServer;
	public static boolean showTeam;
	public static boolean showKD;
	public static boolean showKK;
	public static boolean showFriends;
	public static boolean showMap;
	public static boolean showStreak;
	public static boolean showGuiChat;	
	public static boolean showGuiMulti;	
	public static String keyGui;	
	public static String keyGui2;
	public static int x;
	public static int y;
	public static boolean toggleTitleScreenButton;
	public static boolean filterTips;

	@Override
	public String getVersion() {
		return "1.5.1";
	}

	@Override
	public void load() {
        // Custom Config
        initConfig();

		//main hooks
		ModLoader.setInGUIHook(this, true, false);
		ModLoader.setInGameHook(this, true, false);

		ModLoader.addLocalization("keybind", "gui");

		//load variables defaults
		new AresData();

		//hook keybinds
		ModLoader.registerKey(this, AresData.keybind, false);
		ModLoader.registerKey(this, AresData.keybind2, false);

		// start thread listener
		new AresGuiListener().start();

        //Pulls servers from web for GUI Server List and sorts them
        servers = getServers();
        Collections.sort(servers);
	}

    private void initConfig() {
        config = new AresConfig();
        loadConfig();
    }

    public static void loadConfig() {
        serverDomain = config.getStringProperty("serverDomain");
        showFPS = config.getBoolProperty("showFPS");
        showKills = config.getBoolProperty("showKills");
        showDeaths = config.getBoolProperty("showDeaths");
        showKilled = config.getBoolProperty("showKilled");
        showServer = config.getBoolProperty("showServer");
        showTeam = config.getBoolProperty("showTeam");
        showKD = config.getBoolProperty("showKD");
        showKK = config.getBoolProperty("showKK");
        showFriends = config.getBoolProperty("showFriends");
        showMap = config.getBoolProperty("showMap");
        showStreak = config.getBoolProperty("showStreak");
        showGuiChat = config.getBoolProperty("showGuiChat");
        showGuiMulti = config.getBoolProperty("showGuiMulti");
        keyGui = config.getStringProperty("keyGui");
        keyGui2 = config.getStringProperty("keyGui2");
        x = config.getIntProperty("X");
        y = config.getIntProperty("Y");
        toggleTitleScreenButton = config.getBoolProperty("toggleTitleScreenButton");
        filterTips = config.getBoolProperty("filterTips");
    }

    public static ArrayList<String> getServers() {
        ArrayList<String> list = new ArrayList<String>();

        try {
            URL url = new URL("https://oc.tc/play");
            URLConnection con = url.openConnection();
            con.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 5.1) AppleWebKit/535.11 (KHTML, like Gecko) Chrome/17.0.963.56 Safari/535.11");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                if (line.startsWith("<li>") && line.endsWith("</li>")) {
                    list.add(line.replace("<li>", "").replace("</li>", ""));
                }
            }
        } catch (Exception ignored) {}

        return list;
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
                AresData.addDeaths(1);
                AresData.killstreak=0;
                deathScreenActive=true;
            }
            //get the title screen button
            GuiButton titleScreen = (GuiButton)mc.currentScreen.buttonList.get(1);
            //if the button is enabled and the user wants to disable it
            if(titleScreen.enabled && toggleTitleScreenButton){
                titleScreen.enabled = false;
                mc.currentScreen.buttonList.set(1, titleScreen);
                mc.currentScreen.updateScreen();
            }
        } else{
            deathScreenActive=false;
        }

		//get debug info for the fps
		String fps = mc.debug.split(",")[0];
		int height = this.x;
		int width = this.y;
		//if the gui is enabled display
		//if chat is open and config says yes then show gui
		if (AresData.guiShowing && (mc.inGameHasFocus || this.showGuiChat && mc.currentScreen instanceof GuiChat)) {
			//show fps
			if (this.showFPS) {
				mc.fontRenderer.drawStringWithShadow(fps, width, height,
						0xffff);
				height += 8;
			}
		}
		//if on Ares server then display this info.
		//if chat is open and config says yes then show gui
		if (AresData.isPlayingAres()&& AresData.guiShowing && (mc.inGameHasFocus || this.showGuiChat && mc.currentScreen instanceof GuiChat) ) {
			// Server display
			if (this.showServer) {
				mc.fontRenderer.drawStringWithShadow("Server: \u00A76"+AresData.server, width, height,16777215);
				height += 8;
			}

			// Team display (based on color)
			if (this.showTeam) {
				if (AresData.team.equalsIgnoreCase("red team")) {
					mc.fontRenderer.drawStringWithShadow("Team: "+AresData.team, width, height,0x990000);
					height += 8;
				} else if (AresData.team.equalsIgnoreCase("blue team")) {
					mc.fontRenderer.drawStringWithShadow("Team: "+AresData.team, width, height,0x0033FF);
					height += 8;
				} else if (AresData.team.equalsIgnoreCase("purple team")) {
					mc.fontRenderer.drawStringWithShadow("Team: "+AresData.team, width, height,0x9933CC);
					height += 8;
				} else if (AresData.team.equalsIgnoreCase("cyan team")) {
					mc.fontRenderer.drawStringWithShadow("Team: "
							+ AresData.team, width, height,0x00FFFF);
					height += 8;
				} else if (AresData.team.equalsIgnoreCase("lime team")) {
					mc.fontRenderer.drawStringWithShadow("Team: "
							+ AresData.team, width, height,0x00FF00);
					height += 8;
				} else if (AresData.team.equalsIgnoreCase("yellow team")) {
					mc.fontRenderer.drawStringWithShadow("Team: "
							+ AresData.team, width, height,0xFFFF00);
					height += 8;
				} else if (AresData.team.equalsIgnoreCase("green team")) {
					mc.fontRenderer.drawStringWithShadow("Team: "
							+ AresData.team, width, height,0x006600);
					height += 8;
				} else if (AresData.team.equalsIgnoreCase("orange team")) {
					mc.fontRenderer.drawStringWithShadow("Team: "+AresData.team, width, height,0xFF9900);
					height += 8;
				} else if (AresData.team.equalsIgnoreCase("Observers")) {
					mc.fontRenderer.drawStringWithShadow("Team: "+AresData.team, width, height, 0x00FFFF);
					height += 8;
				} else {
					mc.fontRenderer.drawStringWithShadow("Team: "+AresData.team, width, height, 0x606060);
					height += 8;
				}
			}
			// Friend display:
			if (this.showFriends) {
				mc.fontRenderer.drawStringWithShadow("Friends Online: \u00A73"+AresData.getFriends(), width, height,16777215);
				height += 8;
			}
			// Map fetcher:
			if (this.showMap) {
				if (AresData.map != null) {
					mc.fontRenderer.drawStringWithShadow("Current Map: \u00A7d"+AresData.map, width, height,16777215);
					height += 8;
				} else {
					AresData.map="Fetching...";
					mc.fontRenderer.drawStringWithShadow("Current Map: \u00A78"+AresData.map, width, height,16777215);
					height += 8;
				}
			}
			//Show KD Ratio
			if (this.showKD) {
				mc.fontRenderer.drawStringWithShadow("K/D: \u00A73"+AresCustomMethods.getKD(), width, height,16777215);
				height += 8;
			}
			//show KK Ratio
			if (this.showKK) {
				mc.fontRenderer.drawStringWithShadow("K/K: \u00A73"+AresCustomMethods.getKK(), width, height,16777215);
				height += 8;
			}
			//show amount of kills
			if (this.showKills) {
				mc.fontRenderer.drawStringWithShadow("Kills: \u00A7a"+AresData.kills, width, height, 16777215);
				height += 8;
			}
			//show amount of deaths
			if (this.showDeaths) {
				mc.fontRenderer.drawStringWithShadow("Deaths: \u00A74"+AresData.deaths, width, height,16777215);
				height += 8;
			}
			// Kill Streak display
			if (this.showStreak) {
				mc.fontRenderer.drawStringWithShadow("Current Killstreak: \u00A75"+AresData.killstreak+"/"+AresData.largestKillstreak, width, height, 16777215);
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
		AresData.team="Observers";
		System.out.println("Client successfully connected to "+ var1.getNetManager().getSocketAddress().toString());
		
		//if logging onto a project ares server, then enable the main mod
		if (var1.getNetManager().getSocketAddress().toString().contains(serverDomain)) {
			// What happens if logs into project ares
			AresData.guiShowing=true;
			System.out.println("Connected to: "+ var1.getNetManager().getSocketAddress().toString()+ " Ares mod activated!");
			AresData.team="Observers";
			AresData.isPA=true;
			AresData.server=AresCustomMethods.getServer(var1.getNetManager().getSocketAddress().toString());
			AresCustomMethods.getMap();
		} else {
			AresData.guiShowing=false;
		}
	}
	
	/**
	 * Called when client disconnects.
	 * Resets all the values
	 */
	public void onDisconnect(NetClientHandler handler) {
		AresData.isPA = false;
		AresData.guiShowing=false;
		AresData.team="Observers";
		AresData.kills=0;
		AresData.killed=0;
		AresData.deaths=0;
		AresData.killstreak=0;
		AresData.largestKillstreak=0;
		AresData.map="Attempting to fetch map...";
	}

	/**
	 * Called when a key is pressed.
	 * Used to activate the gui ect.
	 */
	public void keyboardEvent(KeyBinding keybinding) {
		
		if (mc.inGameHasFocus) {
			if (keybinding == AresData.keybind) {
                AresData.guiShowing = !AresData.guiShowing;
			} else if (keybinding == AresData.keybind2) {
				ModLoader.openGUI(mc.thePlayer, new GuiAresServers(true));
			}
		}
	}
}
