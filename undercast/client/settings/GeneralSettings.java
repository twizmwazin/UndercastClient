package undercast.client.settings;

import cpw.mods.fml.client.FMLClientHandler;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import org.lwjgl.input.Keyboard;
import undercast.client.UndercastConfig;

public class GeneralSettings extends GuiScreen {

    // Toggle settings
    public String[] toggleSettings = new String[] { "showGuiChat", "showGuiMulti", "toggleTitleScreenButton", "filterTips", "matchOnServerJoin", "enableButtonTooltips", "parseMatchState", "realtimeStats" };
    public String[] enabledStrings = new String[] { "Chat gui shown", "Overcast Button shown", "Death screen cleared", "Tips filtered", "/match on server join", "Button tooltips shown", "Match state shown", "Realtime stats" };
    public String[] disabledStrings = new String[] { "Chat gui hidden", "Overcast Button hidden", " Default death screen", "No tips filtered", "No /match on server join", "Button tooltips hidden", "Match state hidden", "Game stats" }; // X
                                                                                                                                                                                                                                           // Offset
                                                                                                                                                                                                                                           // vars
    public GuiButton xPlusButton;
    public GuiButton xMinusButton;
    public GuiTextField xTextField;
    // X Offset vars
    public GuiButton yPlusButton;
    public GuiButton yMinusButton;
    public GuiTextField yTextField;
    // Allowed chars
    public String allowedChars = "0123456789-\b";
    public GuiScreen parentScreen;

    public GeneralSettings(GuiScreen gs) {
        super();
        parentScreen = gs;
        
    }

    @Override
    public void initGui() {
    	int width = this.field_146294_l;
    	int height = this.field_146295_m;
        // Positioning
        int x1 = width / 2 - 150;
        int x2 = width / 2 + 10;
        int y = height / 2 - 80;

        // Add buttons
        for (int i = 0; i < toggleSettings.length / 2; i++) {
            this.field_146292_n.add(new SettingsToggleButton(0, x1, y + (i * 25), 150, 20, "", enabledStrings[i], disabledStrings[i], toggleSettings[i]));
        }
        y = height / 2 - 80;
        for (int i = toggleSettings.length / 2; i < toggleSettings.length; i++) {
            this.field_146292_n.add(new SettingsToggleButton(0, x2, y + ((i - toggleSettings.length / 2) * 25), 150, 20, "", enabledStrings[i], disabledStrings[i], toggleSettings[i]));
        }

        // Positioning
        y = (y - 30) + toggleSettings.length * 25;
        int x = width / 2 - 77;
        x2 = width / 2 + 75;

        // X and Y Offset
        xPlusButton = new GuiButton(3, x1, y - 35, 15, 20, "+");
        yPlusButton = new GuiButton(4, x2, y - 35, 15, 20, "+");
        xMinusButton = new GuiButton(5, x1 + 60, y - 35, 15, 20, "-");
        yMinusButton = new GuiButton(6, x2 + 60, y - 35, 15, 20, "-");
        this.field_146292_n.add(xPlusButton);
        this.field_146292_n.add(yPlusButton);
        this.field_146292_n.add(xMinusButton);
        this.field_146292_n.add(yMinusButton);
        xTextField = new GuiTextField(field_146297_k.fontRenderer, x1 + 20, y - 35, 35, 20);
        xTextField.func_146180_a(Integer.toString(UndercastConfig.x));
        yTextField = new GuiTextField(field_146297_k.fontRenderer, x2 + 20, y - 35, 35, 20);
        yTextField.func_146180_a(Integer.toString(UndercastConfig.y));

        // Back button
        this.field_146292_n.add(new GuiButton(1, x, y, 150, 20, "Back"));
    }

    @Override
    public void drawScreen(int par1, int par2, float par3) {
    	int width = this.field_146294_l;
    	int height = this.field_146295_m;
    	func_146276_q_();
        // Draw label at top of screen
        drawCenteredString(field_146289_q, "General settings", field_146294_l / 2, field_146295_m / 2 - 80 - 20, 0x4444bb);
        xTextField.func_146194_f();
        yTextField.func_146194_f();

        int x = width / 2 - 77, x2 = width / 2 + 75, x1 = width / 2 - 150;
        int y = (height / 2 - 120) + toggleSettings.length * 25;
        drawCenteredString(field_146289_q, "X Offset", x1 + 40, y - 38, 0xffffff);
        drawCenteredString(field_146289_q, "Y Offset", x2 + 40, y - 38, 0xffffff);
        // Draw buttons
        super.drawScreen(par1, par2, par3);
    }

    @Override
    protected void keyTyped(char par1, int par2) {
        if (par2 == Keyboard.KEY_ESCAPE) {
            // secure reading of the int values
            int x, y = 0;
            try {
                x = Integer.parseInt(xTextField.func_146179_b());
            } catch (Exception e) {
                x = 0;
            }
            try {
                y = Integer.parseInt(yTextField.func_146179_b());
            } catch (Exception e) {
                y = 0;
            }
            UndercastConfig.setIntProperty("X", Integer.parseInt(xTextField.func_146179_b()));
            UndercastConfig.x = Integer.parseInt(xTextField.func_146179_b());
            UndercastConfig.setIntProperty("Y", Integer.parseInt(xTextField.func_146179_b()));
            UndercastConfig.y = Integer.parseInt(yTextField.func_146179_b());
            field_146297_k.func_147108_a(null);
            return;
        }
        if (!allowedChars.contains(String.valueOf(par1))) {
            return;
        }
        if (xTextField.func_146181_i()) {
            xTextField.func_146201_a(par1, par2);
        }
        if (yTextField.func_146181_i()) {
            yTextField.func_146201_a(par1, par2);
        }
    }

    @Override
    protected void func_146284_a(GuiButton guibutton) {
        // If the button is clicked, toggle and save the setting
        if (guibutton instanceof SettingsToggleButton) {
            // Toggle button
            SettingsToggleButton button = (SettingsToggleButton) guibutton;
            button.buttonPressed();
        } else if (guibutton.field_146127_k == 1) {
            // secure reading of the int values
            int x, y;
            try {
                x = Integer.parseInt(xTextField.func_146179_b());
            } catch (Exception e) {
                x = 0;
            }
            try {
                y = Integer.parseInt(yTextField.func_146179_b());
            } catch (Exception e) {
                y = 0;
            }

            // Close screen
            UndercastConfig.setIntProperty("X", x);
            UndercastConfig.x = x;
            UndercastConfig.setIntProperty("Y", y);
            UndercastConfig.y = y;
            FMLClientHandler.instance().getClient().func_147108_a(parentScreen);
        }

        // Handle +/-
        if (guibutton.field_146127_k == 3) {
            xTextField.func_146180_a(Integer.toString(Integer.parseInt(xTextField.func_146179_b()) + 1));
        }
        if (guibutton.field_146127_k == 4) {
            yTextField.func_146180_a(Integer.toString(Integer.parseInt(yTextField.func_146179_b()) + 1));
        }
        if (guibutton.field_146127_k == 5) {
            xTextField.func_146180_a(Integer.toString(Integer.parseInt(xTextField.func_146179_b()) - 1));
        }
        if (guibutton.field_146127_k == 6) {
            yTextField.func_146180_a(Integer.toString(Integer.parseInt(yTextField.func_146179_b()) - 1));
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return true;
    }

    @Override
    protected void mouseClicked(int par1, int par2, int par3) {
        super.mouseClicked(par1, par2, par3);
        xTextField.func_146192_a(par1, par2, par3);
        yTextField.func_146192_a(par1, par2, par3);
    }
}
