package undercast.client;

import java.util.List;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiYesNo;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.EnumChatFormatting;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class UndercastGuiGameOver extends GuiScreen {
	private int field_146347_a;
	private boolean field_146346_f = false;

	public void initGui()
	{
		this.field_146292_n.clear();
		if (this.field_146297_k.theWorld.getWorldInfo().isHardcoreModeEnabled()) {
			if (this.field_146297_k.isIntegratedServerRunning())
				this.field_146292_n.add(new GuiButton(1, this.field_146294_l / 2 - 100, this.field_146295_m / 4 + 96, I18n.getStringParams("deathScreen.deleteWorld", new Object[0])));
			else
				this.field_146292_n.add(new GuiButton(1, this.field_146294_l / 2 - 100, this.field_146295_m / 4 + 96, I18n.getStringParams("deathScreen.leaveServer", new Object[0])));
		}
		else {
			this.field_146292_n.add(new GuiButton(0, this.field_146294_l / 2 - 100, this.field_146295_m / 4 + 72, I18n.getStringParams("deathScreen.respawn", new Object[0])));
			this.field_146292_n.add(new GuiButton(1, this.field_146294_l / 2 - 100, this.field_146295_m / 4 + 96, I18n.getStringParams("deathScreen.titleScreen", new Object[0])));

			if (this.field_146297_k.getSession() == null) {
				((GuiButton)this.field_146292_n.get(1)).field_146124_l = false;
			}
		}
		List<GuiButton> buttons = this.field_146292_n;
		for (GuiButton localGuiButton : buttons)
			localGuiButton.field_146124_l = false;
	}

	protected void keyTyped(char paramChar, int paramInt)
	{
	}

	protected void func_146284_a(GuiButton p_146284_1_)
	{
		switch (p_146284_1_.field_146127_k) {
		case 0:
			this.field_146297_k.thePlayer.respawnPlayer();
			this.field_146297_k.func_147108_a(null);
			break;
		case 1:
			GuiYesNo localGuiYesNo = new GuiYesNo(this, I18n.getStringParams("deathScreen.quit.confirm", new Object[0]), "", I18n.getStringParams("deathScreen.titleScreen", new Object[0]), I18n.getStringParams("deathScreen.respawn", new Object[0]), 0);
			this.field_146297_k.func_147108_a(localGuiYesNo);
			localGuiYesNo.func_146350_a(20);
		}
	}

	public void confirmClicked(boolean paramBoolean, int paramInt)
	{
		if (paramBoolean) {
			this.field_146297_k.theWorld.sendQuittingDisconnectingPacket();
			this.field_146297_k.loadWorld(null);
			this.field_146297_k.func_147108_a(new GuiMainMenu());
		} else {
			this.field_146297_k.thePlayer.respawnPlayer();
			this.field_146297_k.func_147108_a(null);
		}
	}

	public void drawScreen(int paramInt1, int paramInt2, float paramFloat)
	{
		drawGradientRect(0, 0, this.field_146294_l, this.field_146295_m, 1615855616, -1602211792);

		GL11.glPushMatrix();
		GL11.glScalef(2.0F, 2.0F, 2.0F);

		boolean bool = this.field_146297_k.theWorld.getWorldInfo().isHardcoreModeEnabled();

		String str = bool ? I18n.getStringParams("deathScreen.title.hardcore", new Object[0]) : I18n.getStringParams("deathScreen.title", new Object[0]);
		drawCenteredString(this.field_146289_q, str, this.field_146294_l / 2 / 2, 30, 16777215);

		GL11.glPopMatrix();
		if (bool) {
			drawCenteredString(this.field_146289_q, I18n.getStringParams("deathScreen.hardcoreInfo", new Object[0]), this.field_146294_l / 2, 144, 16777215);
		}
		drawCenteredString(this.field_146289_q, I18n.getStringParams("deathScreen.score", new Object[0]) + ": " + EnumChatFormatting.YELLOW + this.field_146297_k.thePlayer.getScore(), this.field_146294_l / 2, 100, 16777215);
		if (UndercastData.isOC) {
			this.drawCenteredString(this.field_146289_q, "Killstreak" + ": " + EnumChatFormatting.YELLOW + (int) UndercastData.getPreviousKillstreak(), this.field_146294_l / 2, 110, 16777215);
		}
		super.drawScreen(paramInt1, paramInt2, paramFloat);
	}

	public boolean doesGuiPauseGame()
	{
		return false;
	}

	public void updateScreen()
	{
		super.updateScreen();
		List<GuiButton> buttons = this.field_146292_n;
		this.field_146347_a += 1;
		if (this.field_146347_a == 20)
			for (GuiButton localGuiButton : buttons)
				localGuiButton.field_146124_l = true;
	}

	public void setTitleScreenButtonState(boolean activated) {
		GuiButton titleButton = (GuiButton) this.field_146292_n.get(1);
		titleButton.field_146124_l = activated;
		this.field_146292_n.set(1, titleButton);
		updateScreen();
	}
}