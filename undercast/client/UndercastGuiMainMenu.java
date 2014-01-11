package undercast.client;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.StringTranslate;
import undercast.client.achievements.UndercastAchievement;
import undercast.client.server.UndercastServerGUI;
import undercast.network.common.NetManager;
import undercast.network.common.packet.Packet00Authentication;

@SideOnly(Side.CLIENT)
public class UndercastGuiMainMenu extends GuiMainMenu {

    public UndercastGuiMainMenu() {
        super();
    }

    @Override
    public void initGui() {
        super.initGui();
        if (!UndercastModClass.getInstance().connection.isConnected()) {
            Runnable r1 = new Runnable() {
                @Override
                public void run() {
                    String username = Minecraft.getMinecraft().getSession().getUsername();
                    try {
                        UndercastModClass.getInstance().connection.connect();
                        NetManager.sendPacket(new Packet00Authentication(username));
                    } catch (Exception e) {
                        UndercastAchievement a = new UndercastAchievement(username, "\u00A74Server", "\u00A74down!");
                        UndercastModClass.getInstance().guiAchievement.queueTakenAchievement(a);
                    }

                }
            };
            new Thread(r1).start();
        }
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
