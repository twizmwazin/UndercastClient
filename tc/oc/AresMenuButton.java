package tc.oc;

import net.minecraft.client.Minecraft;
import net.minecraft.src.GuiButton;
import net.minecraft.src.ModLoader;
import tc.oc.server.Ares_ServerGUI;

public class AresMenuButton extends GuiButton {
    /**
     * Default constructor
     */
    public AresMenuButton(int par1, int par2, int par3, int par4, int par5, String par6Str) {
        super(par1, par2, par3, par4, par5, par6Str);

    }

    /**
     * When button is released this method is called.
     * When this button is released it opens the ares server gui
     */
    @Override
    public void mouseReleased(int par1, int par2) {
        Minecraft mc = Minecraft.getMinecraft();
        ModLoader.openGUI(mc.thePlayer, new Ares_ServerGUI(false));
    }
}
