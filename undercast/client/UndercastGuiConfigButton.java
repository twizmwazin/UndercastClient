/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package undercast.client;

import cpw.mods.fml.client.FMLClientHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import undercast.client.settings.SettingsGUI;

/**
 * 
 * @author Florian
 */
public class UndercastGuiConfigButton extends GuiButton {

    public GuiScreen parentScreen;

    public UndercastGuiConfigButton(int i, int i0, int i1, int i2, int i3, String undercast_config, GuiScreen gs) {
        super(i, i0, i1, i2, i3, undercast_config);
        parentScreen = gs;
    }

    @Override
    public boolean func_146116_c(Minecraft par1Minecraft, int par2, int par3) {
        if (this.field_146124_l && this.field_146125_m && par2 >= this.field_146128_h && par3 >= this.field_146129_i && par2 < this.field_146128_h + this.field_146120_f && par3 < this.field_146129_i + this.field_146121_g) {
            FMLClientHandler.instance().getClient().func_147108_a(new SettingsGUI(parentScreen));
        }
        return super.func_146116_c(par1Minecraft, par2, par3);
    }
}
