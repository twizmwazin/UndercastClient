package undercast.client;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.util.StringTranslate;
import undercast.client.server.UndercastServerGUI;

@SideOnly(Side.CLIENT)
public class UndercastGuiMainMenu extends GuiMainMenu {

    public UndercastGuiMainMenu() {
        super();
    }

    public void initGui() {
        super.initGui();
        this.buttonList.remove(1);
        this.buttonList.add(new GuiButton(2, this.width / 2 - 100, this.height / 4 + 72, 99, 20, StringTranslate.getInstance().translateKey("menu.multiplayer")));
        this.buttonList.add(new GuiButtonTooltip(7, this.width / 2 + 1, this.height / 4 + 72, 99, 20, "Overcast Network", "Serverlist with Overcast Network Servers"));
    }

    protected void actionPerformed(GuiButton par1GuiButton) {
        super.actionPerformed(par1GuiButton);
        if (par1GuiButton.id == 7) {
            mc.displayGuiScreen(new UndercastServerGUI(false));
        }
    }
}
