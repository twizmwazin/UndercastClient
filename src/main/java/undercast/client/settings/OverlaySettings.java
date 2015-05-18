package undercast.client.settings;

import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Keyboard;
import undercast.client.UndercastConfig;
import undercast.client.UndercastData;
import undercast.client.UndercastData.ServerType;

public class OverlaySettings extends GuiScreen {

    public int pageIndex = 0;
    public int buttonPerPage = 14;
    public GuiScreen parentScreen;
    public String[] toggleSettings = new String[]{"showFPS", "showKills", "showDeaths", "showKilled", "showServer", "showTeam", "showKD", "showKK", "showMap", "showNextMap", "showStreak", "showPlayingTime", "fullBright", "showMatchTime", "showMatchTimeSeconds", "showGSClass", "showScore", "showTotalKills", "showRaindropCounter", "lessObstructive"};
    public String[] enabledStrings = new String[]{"FPS Shown", "Kills shown", "Deaths shown", "Killed shown", "Server shown", "Team shown", "KD Shown", "KK Shown", "Current map shown", "Next map shown", "Killstreak shown", "Playing time shown", "Full bright enabled", "Match Time shown", "Match time seconds shown", "Ghost Squadron class shown", "Score shown", "Total Kills shown", "Raindrop Counter shown", "Less Obstructive mode"};
    public String[] disabledStrings = new String[]{"FPS Hidden", "Kills hidden", "Deaths hidden", "Killed hidden", "Server hidden", "Team hidden", "KD Hidden", "KK Hidden", "Current map hidden", "Next map hidden", "Killstreak hidden", "Playing time hidden", "Full bright disabled", "Match time hidden", "Match time seconds hidden", "Ghost Squadron class hidden", "Score hidden", "Total Kills hidden", "Raindrop Counter hidden", "Normal display mode"};

    public OverlaySettings(GuiScreen gs) {
        super();
        parentScreen = gs;
    }

    @Override
    public void initGui() {
        // Add buttons
        int x1 = width / 2 - 150;
        int x2 = width / 2 + 10;
        int y = height / 2 - 80;
        for (int i = 0; i < 7; i++) {
            int j = pageIndex * buttonPerPage + i;
            if (j < toggleSettings.length) {

                this.buttonList.add(new SettingsToggleButton(i, x1, y + (i * 25), 150, 20, "", enabledStrings[j], disabledStrings[j], toggleSettings[j]));
            }
        }
        y = height / 2 - 80;
        for (int i = 7; i < 14; i++) {
            int j = pageIndex * buttonPerPage + i;
            if (j < toggleSettings.length) {
                this.buttonList.add(new SettingsToggleButton(i, x2, y + ((i - this.buttonPerPage / 2) * 25), 150, 20, "", enabledStrings[j], disabledStrings[j], toggleSettings[j]));
            }
        }
        int x = width / 2 - 75;
        y = y + this.buttonPerPage / 2 * 25;
        this.buttonList.add(new GuiButton(1, x, y, 150, 20, "Back"));
        this.buttonList.add(new GuiButton(15, width - 40, y, 20, 20, ">"));
        this.buttonList.add(new GuiButton(16, 20, y, 20, 20, "<"));
        if (this.pageIndex == 0) {
            ((GuiButton) this.buttonList.get(this.buttonList.size() - 1)).enabled = false;
        }
        if (this.toggleSettings.length < (pageIndex + 1) * buttonPerPage) {
            ((GuiButton) this.buttonList.get(this.buttonList.size() - 2)).enabled = false;
        }
    }

    @Override
    public void drawScreen(int par1, int par2, float par3) {
        drawDefaultBackground();
        // Draw label at top of screen
        drawCenteredString(fontRendererObj, "Overlay settings", width / 2, height / 2 - 80 - 20, 0x4444bb);

        // Draw buttons
        super.drawScreen(par1, par2, par3);
    }

    @Override
    protected void keyTyped(char par1, int par2) {
        if (par2 == Keyboard.KEY_ESCAPE) {
            if (UndercastConfig.showGSClass && UndercastData.currentGSClass.equals("Unknown") && UndercastData.currentServerType == ServerType.ghostsquadron) {
                Minecraft.getMinecraft().thePlayer.sendChatMessage("/class");
            }
            mc.displayGuiScreen(null);
        }
    }

    @Override
    protected void actionPerformed(GuiButton guibutton) {
        // If the button is clicked, toggle and save the setting
        if (guibutton instanceof SettingsToggleButton) {
            SettingsToggleButton button = (SettingsToggleButton) guibutton;
            button.buttonPressed();
        } else if (guibutton.id == 15) {
            this.pageIndex++;
            this.buttonList.clear();
            this.initGui();
        } else if (guibutton.id == 16) {
            this.pageIndex--;
            this.buttonList.clear();
            this.initGui();
        } else {
            if (UndercastConfig.showGSClass && UndercastData.currentGSClass.equals("Unknown") && UndercastData.currentServerType == ServerType.ghostsquadron) {
                Minecraft.getMinecraft().thePlayer.sendChatMessage("/class");
            }
            FMLClientHandler.instance().getClient().displayGuiScreen(parentScreen);
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return true;
    }
}