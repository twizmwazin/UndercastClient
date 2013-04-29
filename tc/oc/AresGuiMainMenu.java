package tc.oc;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraft.util.StringTranslate;
//import tc.oc.server.Ares_ServerGUI;

@SideOnly(Side.CLIENT)
public class AresGuiMainMenu extends GuiMainMenu {

    public AresGuiMainMenu() {
        super();
    }

    public void initGui() {
        super.initGui();
        this.buttonList.remove(1);
        this.buttonList.add(new GuiButton(2, this.width / 2 - 100, this.height / 4 + 72, 99, 20, StringTranslate.getInstance().translateKey("menu.multiplayer")));
        this.buttonList.add(new GuiButton(7, this.width / 2 + 1, this.height / 4 + 72, 99, 20, "Project Ares"));
    }

    protected void actionPerformed(GuiButton par1GuiButton) {
        super.actionPerformed(par1GuiButton);
        if (par1GuiButton.id == 7)
        {
            mc.displayGuiScreen(new GuiConnecting(this,FMLClientHandler.instance().getClient(),"us.oc.tc",25565));
            //mc.displayGuiScreen(new Ares_ServerGUI(false));
        }
    }
}
