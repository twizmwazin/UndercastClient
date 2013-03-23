package tc.oc;
//You may not release this source under any condition, it must be linked to this page
//You may recompile and publish as long as GoldBattle is given credit
//You may not claim this to be your own
//You may not remove these comments

import net.minecraft.client.Minecraft;
import net.minecraft.src.*;
import org.lwjgl.input.Keyboard;
import tc.oc.server.GuiAresServers;

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
			if (mc.currentScreen instanceof GuiMultiplayer && displayGui && mod_Ares.showGuiMulti) {
				ModLoader.openGUI(mc.thePlayer,new GuiAresServers(false));
			}
			//if the user has gone back to the main menu. Set it back to Ares Gui
			else if (mc.currentScreen instanceof GuiMainMenu) {
				displayGui=true;
			}
            else if(mc.currentScreen instanceof GuiControls){
                if (!Keyboard.getKeyName(AresData.keybind.keyCode).toUpperCase().equals(mod_Ares.keyGui.toUpperCase())) {
                    mod_Ares.config.setProperty("keyGui", Keyboard.getKeyName(AresData.keybind.keyCode).toUpperCase());
                    mod_Ares.loadConfig();
                }

                if (!Keyboard.getKeyName(AresData.keybind2.keyCode).toUpperCase().equals(mod_Ares.keyGui2.toUpperCase())) {
                    mod_Ares.config.setProperty("keyGui2", Keyboard.getKeyName(AresData.keybind2.keyCode).toUpperCase());
                    mod_Ares.loadConfig();
                }
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
