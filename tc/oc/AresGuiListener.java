package tc.oc;
//You may not release this source under any condition, it must be linked to this page
//You may recompile and publish as long as GoldBattle is given credit
//You may not claim this to be your own
//You may not remove these comments

import tc.oc.server.Ares_ServerGUI;
import net.minecraft.client.Minecraft;
import net.minecraft.src.ModLoader;
import net.minecraft.src.GuiMainMenu;
import net.minecraft.src.GuiMultiplayer;
import net.minecraft.src.mod_Ares;

public class AresGuiListener extends Thread {

	private static Minecraft mc = Minecraft.getMinecraft();
	private static boolean displayGui = true;
	
	/**
	 * MultiPlayer Menu Listener thread.
	 * If the multiplayer gui is open, it wants to be open, and the config says yes
	 * It displays the gui
	 */
	public void run() {
		while (true) {
			if (mc.currentScreen instanceof GuiMultiplayer && displayGui && mod_Ares.showGuiMulti.equalsIgnoreCase("true")) {
				ModLoader.openGUI(mc.thePlayer,new Ares_ServerGUI(false));
			}
			//if the user has gone back to the main menu. Set it back to Ares Gui
			else if (mc.currentScreen instanceof GuiMainMenu) {
				displayGui=true;
			}
			//sleep the listener for a bit
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {}
		}
	}
	
	/**
	 * Tells the listener that the old menu wants to/shouldnt be displayed
	 */
	public static void toggleMultiGUI(Boolean value){
		displayGui = value;
	}
}
