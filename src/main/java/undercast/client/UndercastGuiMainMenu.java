package undercast.client;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.StatCollector;
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
                        NetManager.sendPacket(new Packet00Authentication(username, "UndercastClient-v" + UndercastModClass.MOD_VERSION));
                    } catch (Exception e) {
                        UndercastAchievement a = new UndercastAchievement(username, "\u00A74Server", "\u00A74down!");
                        UndercastModClass.getInstance().guiAchievement.queueTakenAchievement(a);
                    }

                }
            };
            new Thread(r1).start();
        }
        this.buttonList.remove(1);
        this.buttonList.add(new GuiButton(2, this.width / 2 - 100, this.height / 4 + 72, 99, 20, StatCollector.translateToLocal("menu.multiplayer")));
        this.buttonList.add(new GuiButton(7, this.width / 2 + 1, this.height / 4 + 72, 99, 20, "Overcast Network"));
    }

    @Override
    protected void actionPerformed(GuiButton par1GuiButton) {
        super.actionPerformed(par1GuiButton);
        if (par1GuiButton.id == 7) {
        	this.mc.displayGuiScreen(new UndercastServerGUI(false));
        }
    }
}
