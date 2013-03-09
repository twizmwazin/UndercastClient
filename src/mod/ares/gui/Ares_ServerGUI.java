package mod.ares.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiScreenAddServer;
import net.minecraft.client.gui.GuiScreenServerList;
import net.minecraft.client.gui.GuiYesNo;
import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraft.client.multiplayer.LanServer;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.ServerList;
import net.minecraft.util.StatCollector;
import net.minecraft.util.StringTranslate;

public class Ares_ServerGUI extends GuiScreen {
	/**
	 * Default constructor
	 */
	public Ares_ServerGUI() {

	}

	/**
	 * This method is a override method for drawing a gui
	 * All "painting" should take place in here
	 */
	public void drawScreen(int x, int y, float f) {
		this.controlList.clear();
		drawDefaultBackground();

		// page title
		drawString(this.fontRenderer, "Project Ares Servers", 10, 8, 16777215);
		
		// side buttons
		this.controlList.add(new GuiButton(1, this.width - (75 + 5),
				this.height - 50, 75, 20, "Old Menu"));
		this.controlList.add(new GuiButton(0, this.width - (75 + 5),
				this.height - 28, 75, 20, "Cancel"));

		// tells the super class to paint everything
		super.drawScreen(x, y, f);
	}

	/**
	 * If a button is clicked this method gets called. 
	 * The id is the number given to the button during init.
	 */
	protected void actionPerformed(GuiButton par1GuiButton) {
		if (par1GuiButton.enabled) {
			if (par1GuiButton.id == 0) {
				this.mc.displayGuiScreen(new GuiMainMenu());
			} else if (par1GuiButton.id == 1) {
				GuiListener.toggleMultiGUI(false);
				this.mc.displayGuiScreen(new GuiMultiplayer(new GuiMainMenu()));
			}
		}
	}
}
