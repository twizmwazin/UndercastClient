package undercast.client.settings;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import undercast.client.UndercastConfig;

@SuppressWarnings("unchecked")
public class AchievementAnimationSettings extends GuiScreen {
    private final GuiScreen parentGui;
    //buttonList -> field_146292_n
    //width -> field_146294_l
    //height -> field_146295_m

    public AchievementAnimationSettings(GuiScreen parent) {
        parentGui = parent;
    }

	@Override
    public void initGui() {
    	int width = this.field_146294_l;
    	int height = this.field_146295_m;
        this.field_146292_n.add(new GuiButton(0, width / 2 - 77, height - 35, 150, 20, "Back"));
        int x1 = width / 2 - 150;
        int x2 = width / 2 + 10;
        int y = height / 2 - 60;
        // Animate achievements?
        this.field_146292_n.add(new SettingsToggleButton(1, x1, y, 150, 20, "", "Achievements animated", "Animation disabled", "achievementAnimation"));
        this.field_146292_n.add(new SettingsToggleButton(2, x1, y + 25, 150, 20, "", "Display skin border", "Hide skin border", "displaySkinBorder"));
        this.field_146292_n.add(new GuiAchievementDurationSlider(3, x2, y, UndercastConfig.achievementAnimationDuration == 0.0F ? "Duration: OFF" : "Duration: " + UndercastConfig.achievementAnimationDuration + " sec", (float) UndercastConfig.achievementAnimationDuration));
    }

	//drawDefaultBackground -> func_146276_q_
	//fontRenderer -> field_146289_q
    @Override
    public void drawScreen(int par1, int par2, float par3) {
    	func_146276_q_();
        // Draw label at top of screen
        drawCenteredString(field_146289_q, "Achievement settings", field_146294_l / 2, field_146295_m / 2 - 80 - 20, 0x4444bb);

        // Draw buttons
        super.drawScreen(par1, par2, par3);
    }

    //ActionPerformed -> func_146284_a
    //id -> field_146127_k
    //displayGuiScreen -> func_147108_a
    @Override
    public void func_146284_a(GuiButton button) {
        if (button instanceof SettingsToggleButton) {
            ((SettingsToggleButton) button).buttonPressed();
            return;
        }
        if (button instanceof GuiAchievementDurationSlider) {
            return;
        }
        switch (button.field_146127_k) {
            case 1:
                Minecraft.getMinecraft().func_147108_a(parentGui);
                break;
            default:
                Minecraft.getMinecraft().func_147108_a(parentGui);
                break;
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return true;
    }
}
