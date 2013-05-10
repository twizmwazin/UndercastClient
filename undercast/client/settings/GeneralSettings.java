package undercast.client.settings;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.src.ModLoader;

import org.lwjgl.input.Keyboard;
import undercast.client.UndercastConfig;

public class GeneralSettings extends GuiScreen {

    // Toggle settings
    public String[] toggleSettings = new String[]{"showGuiChat", "showGuiMulti", "toggleTitleScreenButton", "filterTips", "matchOnServerJoin", "enableButtonTooltips", "showAchievements", "showDeathAchievements", "showKillAchievements"};
    public String[] enabledStrings = new String[]{"Chat gui shown", "Overcast Button shown", "Death screen cleared", "Tips filtered", "/match on server join", "Button tooltips shown", "Enabled Achievements shown", "Death Achievements shown", "Kill Achievements shown"};
    public String[] disabledStrings = new String[]{"Chat gui hidden", "Overcast Button hidden", " Default death screen", "No tips filtered", "No /match on server join", "Button tooltips hidden", "No Achievements shown", "No Death Achievements", "No Kill Achievements"};
    // X Offset vars
    public GuiButton xPlusButton;
    public GuiButton xMinusButton;
    public GuiTextField xTextField;
    // X Offset vars
    public GuiButton yPlusButton;
    public GuiButton yMinusButton;
    public GuiTextField yTextField;
    // Allowed chars
    public String allowedChars = "0123456789-\b";

    @Override
    public void initGui() {
        // Positioning
        int x1 = width / 2 - 150;
        int x2 = width / 2 + 10;
        int y = height / 2 - 80;

        // Add buttons
        for (int i = 0; i < toggleSettings.length / 2; i++) {
            this.buttonList.add(new SettingsToggleButton(0, x1, y + (i * 25), 150, 20, "", enabledStrings[i], disabledStrings[i], toggleSettings[i]));
        }
        y = height / 2 - 80;
        for (int i = toggleSettings.length / 2; i < toggleSettings.length; i++) {
            this.buttonList.add(new SettingsToggleButton(0, x2, y + ((i - toggleSettings.length / 2) * 25), 150, 20, "", enabledStrings[i], disabledStrings[i], toggleSettings[i]));
        }

        // Positioning
        y = (y - 50) + toggleSettings.length * 25;
        int x = width / 2 - 77;
        x2 = width / 2 + 75;

        //X and Y Offset
        xPlusButton = new GuiButton(3, x1, y - 40, 15, 20, "+");
        yPlusButton = new GuiButton(4, x2, y - 40, 15, 20, "+");
        xMinusButton = new GuiButton(5, x1 + 60, y - 40, 15, 20, "-");
        yMinusButton = new GuiButton(6, x2 + 60, y - 40, 15, 20, "-");
        this.buttonList.add(xPlusButton);
        this.buttonList.add(yPlusButton);
        this.buttonList.add(xMinusButton);
        this.buttonList.add(yMinusButton);
        xTextField = new GuiTextField(mc.fontRenderer, x1 + 20, y - 40, 35, 20);
        xTextField.setText(Integer.toString(UndercastConfig.x));
        yTextField = new GuiTextField(mc.fontRenderer, x2 + 20, y - 40, 35, 20);
        yTextField.setText(Integer.toString(UndercastConfig.y));

        // Back button
        this.buttonList.add(new GuiButton(1, x, y, 150, 20, "Back"));
    }

    @Override
    public void drawScreen(int par1, int par2, float par3) {
        drawDefaultBackground();
        // Draw label at top of screen
        drawCenteredString(fontRenderer, "General Settings", width / 2, height / 2 - 80 - 20, 0x4444bb);
        xTextField.drawTextBox();
        yTextField.drawTextBox();

        int x = width / 2 - 77,
                x2 = width / 2 + 75,
                x1 = width / 2 - 150;
        int y = (height / 2 - 80) + toggleSettings.length * 25;
        drawCenteredString(fontRenderer, "X Offset", x1 + 30, y - 52, 0xffffff);
        drawCenteredString(fontRenderer, "Y Offset", x2 + 30, y - 52, 0xffffff);
        // Draw buttons
        super.drawScreen(par1, par2, par3);
    }

    @Override
    protected void keyTyped(char par1, int par2) {
        if (par2 == Keyboard.KEY_ESCAPE) {
            //secure reading of the int values
            int x, y = 0;
            try {
                x = Integer.parseInt(xTextField.getText());
            } catch (Exception e) {
                x = 0;
            }
            try {
                y = Integer.parseInt(yTextField.getText());
            } catch (Exception e) {
                y = 0;
            }
            UndercastConfig.setIntProperty("X", Integer.parseInt(xTextField.getText()));
            UndercastConfig.x = Integer.parseInt(xTextField.getText());
            UndercastConfig.setIntProperty("Y", Integer.parseInt(xTextField.getText()));
            UndercastConfig.y = Integer.parseInt(yTextField.getText());
            mc.displayGuiScreen(null);
            return;
        }
        if (!allowedChars.contains(String.valueOf(par1))) {
            return;
        }
        if (xTextField.isFocused()) {
            xTextField.textboxKeyTyped(par1, par2);
        }
        if (yTextField.isFocused()) {
            yTextField.textboxKeyTyped(par1, par2);
        }
    }

    @Override
    protected void actionPerformed(GuiButton guibutton) {
        // If the button is clicked, toggle and save the setting
        if (guibutton instanceof SettingsToggleButton) {
            //Toggle button
            SettingsToggleButton button = (SettingsToggleButton) guibutton;
            button.buttonPressed();
        } else if (guibutton.id == 1) {
            //secure reading of the int values
            int x, y = 0;
            try {
                x = Integer.parseInt(xTextField.getText());
            } catch (Exception e) {
                x = 0;
            }
            try {
                y = Integer.parseInt(yTextField.getText());
            } catch (Exception e) {
                y = 0;
            }

            //Close screen
            UndercastConfig.setIntProperty("X", x);
            UndercastConfig.x = x;
            UndercastConfig.setIntProperty("Y", y);
            UndercastConfig.y = y;
            ModLoader.openGUI(mc.thePlayer, new SettingsGUI());
        }

        //Handle +/-
        if (guibutton.id == 3) {
            xTextField.setText(Integer.toString(Integer.parseInt(xTextField.getText()) + 1));
        }
        if (guibutton.id == 4) {
            yTextField.setText(Integer.toString(Integer.parseInt(yTextField.getText()) + 1));
        }
        if (guibutton.id == 5) {
            xTextField.setText(Integer.toString(Integer.parseInt(xTextField.getText()) - 1));
        }
        if (guibutton.id == 6) {
            yTextField.setText(Integer.toString(Integer.parseInt(yTextField.getText()) - 1));
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return true;
    }

    @Override
    protected void mouseClicked(int par1, int par2, int par3) {
        super.mouseClicked(par1, par2, par3);
        xTextField.mouseClicked(par1, par2, par3);
        yTextField.mouseClicked(par1, par2, par3);
    }
}
