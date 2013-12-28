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
        this.field_146292_n.remove(1);
        this.field_146292_n.add(new GuiButton(2, this.field_146294_l / 2 - 100, this.field_146295_m / 4 + 72, 99, 20, I18n.getStringParams("menu.multiplayer")));
        this.field_146292_n.add(new GuiButton(7, this.field_146294_l / 2 + 1, this.field_146295_m / 4 + 72, 99, 20, "Overcast Network"));
    }

    @Override
    protected void func_146284_a(GuiButton par1GuiButton) {
        super.func_146284_a(par1GuiButton);
        if (par1GuiButton.field_146127_k == 7) {
        	this.field_146297_k.func_147108_a(new UndercastServerGUI(false));
        }
    }
}
