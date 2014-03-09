package undercast.client.settings;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import undercast.client.UndercastConfig;
import undercast.client.UndercastModClass;
import undercast.client.achievements.UndercastAchievement;

@SuppressWarnings("unchecked")
public class AchievementAnimationSettings extends GuiScreen {
    private final GuiScreen parentGui;

    public AchievementAnimationSettings(GuiScreen parent) {
        parentGui = parent;
    }

    @Override
    public void initGui() {
        this.buttonList.add(new GuiButton(0, width / 2 - 77, height - 35, 150, 20, "Back"));
        int x1 = width / 2 - 150;
        int x2 = width / 2 + 10;
        int y = height / 2 - 60;
        // Animate achievements?
        this.buttonList.add(new SettingsToggleButton(1, x1, y, 150, 20, "", "Achievements animated", "Animation disabled", "achievementAnimation"));
        this.buttonList.add(new SettingsToggleButton(2, x1, y + 25, 150, 20, "", "Display skin border", "Hide skin border", "displaySkinBorder"));
        this.buttonList.add(new GuiAchievementDurationSlider(3, x2, y, UndercastConfig.achievementAnimationDuration == 0.0F ? "Duration: OFF" : "Duration: " + UndercastConfig.achievementAnimationDuration + " sec", (float) UndercastConfig.achievementAnimationDuration));
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
    public void actionPerformed(GuiButton button) {
        if (button instanceof SettingsToggleButton) {
            ((SettingsToggleButton) button).buttonPressed();
            UndercastAchievement achievement = new UndercastAchievement(Minecraft.getMinecraft().getSession().getUsername(), "Settings","updated");
            UndercastModClass.getInstance().guiAchievement.queueTakenAchievement(achievement);
            return;
        }
        if (button instanceof GuiAchievementDurationSlider) {
            return;
        }
        switch (button.id) {
        case 1:
            Minecraft.getMinecraft().displayGuiScreen(parentGui);
            break;
        default:
            Minecraft.getMinecraft().displayGuiScreen(parentGui);
            break;
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return true;
    }
}
