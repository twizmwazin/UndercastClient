package undercast.client;

import java.util.List;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiYesNo;
import net.minecraft.client.gui.GuiYesNoCallback;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class UndercastGuiGameOver extends GuiScreen {
    private int field_146347_a;
    private boolean field_146346_f = false;

    public void initGui()
    {
        this.buttonList.clear();
        if (this.mc.theWorld.getWorldInfo().isHardcoreModeEnabled()) {
            if (this.mc.isIntegratedServerRunning())
                this.buttonList.add(new GuiButton(1, this.width / 2 - 100, this.height / 4 + 96, StatCollector.translateToLocal("deathScreen.deleteWorld")));
            else
                this.buttonList.add(new GuiButton(1, this.width / 2 - 100, this.height / 4 + 96, StatCollector.translateToLocal("deathScreen.leaveServer")));
        }
        else {
            this.buttonList.add(new GuiButton(0, this.width / 2 - 100, this.height / 4 + 72, StatCollector.translateToLocal("deathScreen.respawn")));
            this.buttonList.add(new GuiButton(1, this.width / 2 - 100, this.height / 4 + 96, StatCollector.translateToLocal("deathScreen.titleScreen")));

            if (this.mc.getSession() == null) {
                ((GuiButton)this.buttonList.get(1)).enabled = false;
            }
        }
        List<GuiButton> buttons = this.buttonList;
        for (GuiButton localGuiButton : buttons)
            localGuiButton.enabled = false;
    }

    protected void keyTyped(char paramChar, int paramInt)
    {
    }

    protected void actionPerformed(GuiButton p_146284_1_)
    {
        switch (p_146284_1_.id) {
        case 0:
            this.mc.thePlayer.respawnPlayer();
            this.mc.displayGuiScreen(null);
            break;
        case 1:
            GuiYesNo localGuiYesNo = new GuiYesNo((GuiYesNoCallback)this, I18n.format("deathScreen.quit.confirm", new Object[0]), "", I18n.format("deathScreen.titleScreen", new Object[0]), I18n.format("deathScreen.respawn", new Object[0]), 0);
            this.mc.displayGuiScreen(localGuiYesNo);
            localGuiYesNo.func_146350_a(20);
        }
    }

    public void confirmClicked(boolean paramBoolean, int paramInt)
    {
        if (paramBoolean) {
            this.mc.theWorld.sendQuittingDisconnectingPacket();
            this.mc.loadWorld(null);
            this.mc.displayGuiScreen(new GuiMainMenu());
        } else {
            this.mc.thePlayer.respawnPlayer();
            this.mc.displayGuiScreen(null);
        }
    }

    public void drawScreen(int paramInt1, int paramInt2, float paramFloat)
    {
        drawGradientRect(0, 0, this.width, this.height, 1615855616, -1602211792);

        GL11.glPushMatrix();
        GL11.glScalef(2.0F, 2.0F, 2.0F);

        boolean bool = this.mc.theWorld.getWorldInfo().isHardcoreModeEnabled();

        String str = bool ? StatCollector.translateToLocal("deathScreen.title.hardcore") : StatCollector.translateToLocal("deathScreen.title");
        drawCenteredString(this.fontRendererObj, str, this.width / 2 / 2, 30, 16777215);

        GL11.glPopMatrix();
        if (bool) {
            drawCenteredString(this.fontRendererObj, StatCollector.translateToLocal("deathScreen.hardcoreInfo"), this.width / 2, 144, 16777215);
        }
        drawCenteredString(this.fontRendererObj, StatCollector.translateToLocal("deathScreen.score") + ": " + EnumChatFormatting.YELLOW + this.mc.thePlayer.getScore(), this.width / 2, 100, 16777215);
        if (UndercastData.isOC) {
            this.drawCenteredString(this.fontRendererObj, "Killstreak" + ": " + EnumChatFormatting.YELLOW + (int) UndercastData.getPreviousKillstreak(), this.width / 2, 110, 16777215);
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
        List<GuiButton> buttons = this.buttonList;
        this.field_146347_a += 1;
        if (this.field_146347_a == 20)
            for (GuiButton localGuiButton : buttons)
                localGuiButton.enabled = true;
    }

    public void setTitleScreenButtonState(boolean activated) {
        GuiButton titleButton = (GuiButton) this.buttonList.get(1);
        titleButton.enabled = activated;
        this.buttonList.set(1, titleButton);
        updateScreen();
    }
}