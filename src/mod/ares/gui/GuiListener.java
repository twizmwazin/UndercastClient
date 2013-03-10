package mod.ares.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiScreenServerList;
import net.minecraft.src.ModLoader;

public class GuiListener extends Thread {

	private static Minecraft mc = Minecraft.getMinecraft();
	private static boolean displayGui = true;
	
	/*
	 * MultiPlayer Menu Listener thread.
	 */
	public void run() {
		while (true) {
			if (mc.currentScreen instanceof GuiMultiplayer && displayGui) {
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
	
	/*
	 * Tells the listener that the old menu wants to/shouldnt be displayed
	 */
	public static void toggleMultiGUI(Boolean value){
		displayGui = value;
	}
}
