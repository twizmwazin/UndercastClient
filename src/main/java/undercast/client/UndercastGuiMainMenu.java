package undercast.client;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.util.StatCollector;
import undercast.client.achievements.UndercastAchievement;
import undercast.client.gui.BeastnodeButton;
import undercast.client.server.UndercastServerGUI;
import undercast.network.common.NetManager;
import undercast.network.common.packet.Packet00Authentication;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URL;

@SideOnly(Side.CLIENT)
public class UndercastGuiMainMenu extends GuiMainMenu {

    public UndercastGuiMainMenu() {
        super();
    }

    public static void openWebpage(URI uri) {
        Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
        if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
            try {
                desktop.browse(uri);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void openWebpage(String url) {
        try {
            openWebpage(new URL(url).toURI());
        } catch (Exception e) {
            e.printStackTrace();
        }
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
                        NetManager.sendPacket(new Packet00Authentication(username, "UndercastClient-v" + "1.7.9"/*UndercastModClass.MOD_VERSION*/));
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
        this.buttonList.add(new BeastnodeButton(8, this.width - 51, this.height - 58));
    }

    @Override
    protected void actionPerformed(GuiButton par1GuiButton) throws IOException {
        super.actionPerformed(par1GuiButton);
        if (par1GuiButton.id == 7) {
            // Display Loading... on the button to notify the user that we're doing work
            par1GuiButton.displayString = "\u00A7cLoading...";
            par1GuiButton.enabled = false;
            this.buttonList.set(7, par1GuiButton);
            // redirect to the displayGui (using a Thread because otherwise it doesn't display the new button text)
            Thread t = new Thread() {
                @Override
                public void run() {
                    Minecraft.getMinecraft().displayGuiScreen(new UndercastServerGUI(false));
                    super.run();
                }
            };
            t.start();
        } else if (par1GuiButton.id == 8) {
            openWebpage("https://www.beastnode.com/portal/aff.php?aff=3151");
        }
    }
}
