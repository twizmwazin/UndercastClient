package undercast.client;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.StringTranslate;
import undercast.client.server.UndercastServerGUI;

@SideOnly(Side.CLIENT)
public class UndercastGuiMainMenu extends GuiMainMenu {

    public UndercastGuiMainMenu() {
        super();
    }

    @Override
    public void initGui() {
        super.initGui();
        this.buttonList.remove(1);
        this.buttonList.add(new GuiButton(2, this.width / 2 - 100, this.height / 4 + 72, 99, 20, I18n.func_135053_a("menu.multiplayer")));
        this.buttonList.add(new GuiButtonTooltip(7, this.width / 2 + 1, this.height / 4 + 72, 99, 20, "Overcast Network", "Serverlist with Overcast Network Servers"));
    }

    @Override
    protected void actionPerformed(GuiButton par1GuiButton) {
        super.actionPerformed(par1GuiButton);
        if (par1GuiButton.id == 7) {
            mc.displayGuiScreen(new UndercastServerGUI(false));
        }
    }
}
