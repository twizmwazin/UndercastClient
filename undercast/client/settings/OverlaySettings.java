package undercast.client.settings;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.src.ModLoader;
import org.lwjgl.input.Keyboard;

public class OverlaySettings extends GuiScreen {

    public String[] toggleSettings = new String[]{"showFPS", "showKills", "showDeaths", "showKilled", "showServer", "showTeam", "showKD", "showKK", "showFriends", "showMap",
        "showNextMap", "showStreak", "showPlayingTime", "fullBright"};
    public String[] enabledStrings = new String[]{"FPS Shown", "Kills shown", "Deaths shown", "Killed shown", "Server shown", "Team shown", "KD Shown", "KK Shown", "Friends shown", "Current map shown",
        "Next map shown", "Killstreak shown", "Playing time shown", "Full bright enabled"};
    public String[] disabledStrings = new String[]{"FPS Hidden", "Kills hidden", "Deaths hidden", "Killed hidden", "Server hidden", "Team hidden", "KD Hidden", "KK Hidden", "Friends hidden", "Current map hidden",
        "Next map hidden", "Killstreak hidden", "Playing time hidden", "Full bright disabled"};

    @Override
    public void initGui() {
        // Add buttons		
        int x1 = width / 2 - 150;
        int x2 = width / 2 + 10;
        int y = height / 2 - 80;
        for (int i = 0; i < toggleSettings.length / 2; i++) {
            this.buttonList.add(new SettingsToggleButton(i, x1, y + (i * 25), 150, 20, "", enabledStrings[i], disabledStrings[i], toggleSettings[i]));
        }
        y = height / 2 - 80;
        for (int i = toggleSettings.length / 2; i < toggleSettings.length; i++) {
            this.buttonList.add(new SettingsToggleButton(i, x2, y + ((i - toggleSettings.length / 2) * 25), 150, 20, "", enabledStrings[i], disabledStrings[i], toggleSettings[i]));
        }
        int x = width / 2 - 75;
        y = y + toggleSettings.length / 2 * 25;
        this.buttonList.add(new GuiButton(1, x, y, 150, 20, "Back"));
    }

    @Override
    public void drawScreen(int par1, int par2, float par3) {
        drawDefaultBackground();
        // Draw label at top of screen
        drawCenteredString(fontRenderer, "Overlay settings", width / 2, height / 2 - 80 - 20, 0x4444bb);

        // Draw buttons
        super.drawScreen(par1, par2, par3);
    }

    @Override
    protected void keyTyped(char par1, int par2) {
        if (par2 == Keyboard.KEY_ESCAPE) {
            mc.displayGuiScreen(null);
            return;
        }
    }

    @Override
    protected void actionPerformed(GuiButton guibutton) {
        // If the button is clicked, toggle and save the setting
        if (guibutton instanceof SettingsToggleButton) {
            SettingsToggleButton button = (SettingsToggleButton) guibutton;
            button.buttonPressed();
        } else {
            ModLoader.openGUI(mc.thePlayer, new SettingsGUI());
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return true;
    }
}