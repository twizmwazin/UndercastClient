package undercast.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.gui.GuiDisconnected;
import net.minecraft.client.gui.GuiDownloadTerrain;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.common.MinecraftForge;
import undercast.client.update.Undercast_UpdaterThread;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent;

/**
 * @author Flv92
 */
public class UndercastConnectionHandler {
	private boolean connected = false;
	
	public UndercastConnectionHandler() {
		MinecraftForge.EVENT_BUS.register(this);
	}
	
	@SubscribeEvent
	public void onGuiShow(GuiOpenEvent event) {
		if (event.gui instanceof GuiDownloadTerrain && !connected) {
			connected = true;
			onLogin(null);
		}
		if ((event.gui instanceof GuiDisconnected || event.gui instanceof GuiMainMenu) && connected) {
			connected = false;
			onLogout(null);
		}
	}

	public void onLogin(PlayerLoggedInEvent event) {
		String ip = new String();
		try{
			ip = Minecraft.getMinecraft().func_147104_D().serverIP;
		} catch(NullPointerException e){
			//SinglePlayer
			return;
		}
		UndercastData.setTeam("Observers");
		UndercastData.teamColor = 'b'; // b for aqua
		// if logging onto an OvercastNetwork server, then enable the main mod
		if (ip.contains(".oc.tc") && !ip.contains("mc.oc.tc")) {
			// What happens if logs into OvercastNetwork
			UndercastData.isOC = true;
			UndercastData.isLobby = true;
			UndercastData.guiShowing = true;
			System.out.println("Undercast Mod activated!");
			UndercastData.setTeam("Observers");
			UndercastData.teamColor = 'b'; // b for aqua
			UndercastData.setServer("Lobby");
			UndercastModClass.getInstance().playTimeCounter = new PlayTimeCounterThread();
			if (ip.contains("eu.oc.tc")) {
				UndercastData.isEU = true;
				// overwrite the location index loaded from config with the index we are actually joining
				// this is only necessary if the player don't join using our server list
				UndercastData.locationIndex = 1;
			} else {
				UndercastData.isEU = false;
				// overwrite the location index loaded from config with the index we are actually joining
				// this is only necessary if the player don't join using our server list
				UndercastData.locationIndex = 0;
			}
			UndercastConfig.setIntProperty("lastUsedLocation", UndercastData.locationIndex);
			// we don't want the lobby join to trigger the leave detection
			// it will be triggered when we get the welcome message
			UndercastData.lobbyLeaveDetectionStarted = false;

		} else {
			UndercastData.isOC = false;
		}
		// update notifier
		if (!UndercastData.isUpdate()) {
			Thread thread = new Thread() {
				@Override
				public void run() {
					try {
						Thread.sleep(3000);
						for (int c = 0; c < 10; c++) { // don't wait longer than
							// 10 sec
							Thread.sleep(1000);
							if (Undercast_UpdaterThread.finished) {
								break;
							}
						}
					} catch (InterruptedException e) {
					}
					sendMessage("\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-");
					sendMessage("[UndercastMod]: A New Version of the Undercast Mod is avaliable");
					sendMessage("[UndercastMod]: Link: \u00A74" + UndercastData.updateLink);
					sendMessage("\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-");
				}
			};
			thread.start();
		}
	}


	@SubscribeEvent
	public void onLogout(PlayerLoggedOutEvent event) {
		Minecraft mc = FMLClientHandler.instance().getClient();
		UndercastData.isOC = false;
		UndercastData.guiShowing = false;
		UndercastData.setTeam("Observers");
		UndercastData.teamColor = 'b';
		UndercastData.resetKills();
		UndercastData.resetKilled();
		UndercastData.resetDeaths();
		UndercastData.resetKillstreak();
		UndercastData.resetLargestKillstreak();
		UndercastData.resetPreviousKillstreak();
		UndercastData.setMap("Attempting to fetch map...");
		if (mc.gameSettings.gammaSetting >= UndercastModClass.getInstance().brightLevel) {
			UndercastModClass.brightActive = false;
			mc.gameSettings.gammaSetting = UndercastModClass.getInstance().defaultLevel;
		}
		UndercastData.welcomeMessageExpected = false;
	}

	private void sendMessage(String text) {
		IChatComponent thingy = new ChatComponentText(text);
		EntityClientPlayerMP player = Minecraft.getMinecraft().thePlayer;
		player.addChatMessage(thingy);
	}

}
