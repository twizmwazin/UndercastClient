package undercast.client.settings;

import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Keyboard;

public class AchievementSettings extends GuiScreen {

    public GuiScreen parentScreen;
    public String[] toggleSettings = new String[]{"showAchievements", "showDeathAchievements", "showKillAchievements", "showFirstBloodAchievement", "showLastKillAchievement", "displaySpecialKillMessages", "displaySpecialObjectives", "showRevengeAchievement"};
    public String[] enabledStrings = new String[]{"Enabled Achievements shown", "Death Achievements shown", "Kill Achievements shown", "First Blood shown", "Last Kill shown", "Special Kill display enabled", "Special Objective display", "Revenge Achievements shown"};
    public String[] disabledStrings = new String[]{"No Achievements shown", "No Death Achievements", "No Kill Achievements", "No First Blood Achievement", "No Last Kill Achievement", "Special Kill display disabled", "No Special Objective display", "Revenge Achievements hidden"};

    public AchievementSettings(GuiScreen gs) {
        super();
        parentScreen = gs;
    }

    @Override
    public void initGui() {
        // Add buttons
        int x1 = width / 2 - 150;
        int x2 = width / 2 + 10;
        int y = height / 2 - 60;
        int offset = toggleSettings.length % 2 == 0 ? 1 : 0;
        for (int i = 0; i <= toggleSettings.length / 2 - offset; i++) {
            this.buttonList.add(new SettingsToggleButton(i, x1, y + (i * 25), 150, 20, "", enabledStrings[i], disabledStrings[i], toggleSettings[i]));
        }
        y = height / 2 - 60;
        for (int i = toggleSettings.length / 2 + 1 - offset; i < toggleSettings.length; i++) {
            this.buttonList.add(new SettingsToggleButton(i, x2, y + ((i - toggleSettings.length / 2 - 1 + offset) * 25), 150, 20, "", enabledStrings[i], disabledStrings[i], toggleSettings[i]));
        }
        int x = width / 2 - 150;
        y = height - 30;
        this.buttonList.add(new GuiButton(-1, x, y, 150, 20, "Back"));
        this.buttonList.add(new GuiButton(-2, x + 160, y, 150, 20, "Animation >"));
    }

    @Override
    public void drawScreen(int par1, int par2, float par3) {
        drawDefaultBackground();
        // Draw label at top of screen
        drawCenteredString(fontRendererObj, "Achievement settings", width / 2, height / 2 - 80 - 20, 0x4444bb);

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
        // If the button is clicked, toggle and save the setting
        if (guibutton instanceof SettingsToggleButton) {
            SettingsToggleButton button = (SettingsToggleButton) guibutton;
            button.buttonPressed();
        } else if (guibutton.id == -1) {
            FMLClientHandler.instance().getClient().displayGuiScreen(parentScreen);
        } else if (guibutton.id == -2) {
            Minecraft.getMinecraft().displayGuiScreen(new AchievementAnimationSettings(this));
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return true;
    }
}
