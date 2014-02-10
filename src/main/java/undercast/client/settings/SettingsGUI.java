package undercast.client.settings;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Keyboard;

public class SettingsGUI extends GuiScreen {

    public GuiScreen parentScreen;

    public SettingsGUI(GuiScreen gs) {
        super();
        parentScreen = gs;
    }

    public String[] toggleSettings = new String[] { "showFPS", "showKills", "showDeaths", "showKilled", "showServer", "showTeam", "showKD", "showKK", "showFriends", "showMap", "showNextMap", "showStreak", "showGuiChat", "showGuiMulti", "showPlayingTime", "toggleTitleScreenButton", "filterTips", "fullBright", "matchOnServerJoin", "enableButtonTooltips" };
    public String[] enabledStrings = new String[] { "FPS Shown", "Kills shown", "Deaths shown", "Killed shown", "Server shown", "Team shown", "KD Shown", "KK Shown", "Friends shown", "Current map shown", "Next map shown", "Killstreak shown", "Chat gui shown", "Multi gui shown", "Playing time shown", "Title screen button", "Tips filtered", "Full bright enabled", "/match on server join", "Button tooltips shown" };
    public String[] disabledStrings = new String[] { "FPS Hidden", "Kills hidden", "Deaths hidden", "Killed hidden", "Server hidden", "Team hidden", "KD Hidden", "KK Hidden", "Friends hidden", "Current map hidden", "Next map hidden", "Killstreak hidden", "Chat gui hidden", "Multi gui hidden", "Playing time hidden", "No title screen button", "No tips filtered", "Full bright disabled", "No /match on server join", "Button tooltips hidden" };

    @Override
    public void initGui() {
        // Add buttons
        int x = width / 2 - 75;
        int y = height / 2;
        this.buttonList.add(new GuiButton(1, x, y - 50, 150, 20, "Overlay Settings"));
        this.buttonList.add(new GuiButton(2, x, y - 20, 150, 20, "General Settings"));
        this.buttonList.add(new GuiButton(3, x, y + 10, 150, 20, "Achievement Settings"));
        this.buttonList.add(new GuiButton(4, x, y + 60, 150, 20, "Done"));
    }

    @Override
    public void drawScreen(int par1, int par2, float par3) {
    	drawDefaultBackground();

        drawCenteredString(fontRendererObj, "Undercast mod settings", width / 2, height / 2 - 80, 0x4444bb);

        // Draw label at top of screen

        // Draw buttons
        super.drawScreen(par1, par2, par3);
    }

    @Override
    protected void keyTyped(char par1, int par2) {
        if (par2 == Keyboard.KEY_ESCAPE) {
        	mc.displayGuiScreen(null);
        }
    }

    @Override
    protected void actionPerformed(GuiButton guibutton) {
        if (guibutton.id == 1) {
            Minecraft.getMinecraft().displayGuiScreen(new OverlaySettings(this));
        }
        if (guibutton.id == 2) {
            Minecraft.getMinecraft().displayGuiScreen(new GeneralSettings(this));
        }
        if (guibutton.id == 3) {
            Minecraft.getMinecraft().displayGuiScreen(new AchievementSettings(this));
        }
        if (guibutton.id == 4) {
        	mc.displayGuiScreen(parentScreen);
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return true;
    }
}