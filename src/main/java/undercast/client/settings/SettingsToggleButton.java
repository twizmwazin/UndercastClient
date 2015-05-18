package undercast.client.settings;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.EnumChatFormatting;
import undercast.client.UndercastConfig;

public class SettingsToggleButton extends GuiButton {

    private String enabledString;
    private String disabledString;
    private String setting;

    public SettingsToggleButton(int par1, int par2, int par3, int par4, int par5, String par6Str, String en, String dis, String set) {
        super(par1, par2, par3, par4, par5, par6Str);
        this.enabledString = en;
        this.disabledString = dis;
        this.setting = set;
        this.updateString();
    }

    private void updateString() {
        if (UndercastConfig.config.get("UndercastMod", setting, false).getBoolean(false)) {
            this.displayString = EnumChatFormatting.GREEN + enabledString;
        } else {
            this.displayString = EnumChatFormatting.RED + disabledString;
        }
    }

    public void buttonPressed() {
        System.out.println("Setting " + setting + " to " + (!UndercastConfig.config.get("UndercastMod", setting, false).getBoolean(false)));
        boolean current = !UndercastConfig.config.get("UndercastMod", setting, false).getBoolean(false);
        UndercastConfig.setBooleanProperty(setting, current ? true : false);
        UndercastConfig.reloadConfig();
        this.updateString();
        System.out.println(UndercastConfig.showServer);
    }
}
