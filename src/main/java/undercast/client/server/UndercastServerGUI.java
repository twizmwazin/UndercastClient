package undercast.client.server;

//You may not release this source under any condition, it must be linked to this page
//You may recompile and publish as long as skipperguy12 and Guru_Fraser are given credit
//You may not claim this to be your own
//You may not remove these comments

import java.awt.*;
import java.net.URI;
import java.util.Timer;
import java.util.TimerTask;

import cpw.mods.fml.client.FMLClientHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.StatCollector;
import net.minecraft.util.StringTranslate;
import undercast.client.*;
import undercast.client.achievements.UndercastAchievement;
import undercast.network.common.NetManager;
import undercast.network.common.packet.Packet00Authentication;

public class UndercastServerGUI extends GuiScreen {

    private UndercastServerInfoSlotGui guiServerInfoSlot;
    private UndercastServer selectedServer;
    private int selected = -1;
    private GuiButton guibuttonrefresh;
    public Boolean inGame;

    /**
     * Default constructor
     * 
     * @param inGame
     *            Boolean if you are on a server or not
     */
    public UndercastServerGUI(boolean inGame) {
        this.inGame = inGame;
        UndercastData.reloadServerInformations(true);
    }

    /**
     * This is init of the gui when it is about to get drawn. You should only have buttons/control elements in here.
     */
    @Override
    public void initGui() {
        if (!UndercastModClass.getInstance().connection.isConnected()) {
            connect();
        }
        this.buttonList.add(new GuiButton(0, width / 2 - 100, height - 52, 98, 20, StatCollector.translateToLocal("selectServer.select")));
        this.buttonList.add(guibuttonrefresh = new GuiButton(1, width / 2 + 2, height - 52, 98, 20, StatCollector.translateToLocal("selectServer.refresh")));
        this.buttonList.add(new GuiButton(2, width / 2 + 2, height - 28, 98, 20, StatCollector.translateToLocal("gui.cancel")));
        this.buttonList.add(new GuiButton(3, width / 2 - 100, height - 28, 98, 20, "Player Stats"));
        this.buttonList.add(new GuiButton(4, width / 2 - 150, height - 28, 48, 20, UndercastData.sortNames[UndercastData.sortIndex]));
        this.buttonList.add(new GuiButton(5, width / 2 + 102, height - 28, 48, 20, "Lobby"));
        this.buttonList.add(new GuiButton(6, width / 2 - 150, height - 52, 48, 20, UndercastData.filterNames[UndercastData.filterIndex]));
        if (!UndercastData.isUpdate()) {
            this.buttonList.add(new GuiButton(7, width - 54, 21, 48, 20, "Update"));
        }
        this.buttonList.add(new GuiButton(8, width / 2 + 102, height - 52, 48, 20, UndercastData.locationNames[UndercastData.locationIndex]));
        guiServerInfoSlot = new UndercastServerInfoSlotGui(this);
    }

    /**
     * If a button is clicked this method gets called. The id is the number given to the button during init.
     */
    @Override
    protected void actionPerformed(GuiButton guibutton) {
        // join button
        if (guibutton.id == 0) {
            joinSelectedServer();
        }
        // refresh button
        if (guibutton.id == 1) {
            UndercastData.reloadServerInformations(true);
            GuiButton refreshButton = (GuiButton) buttonList.get(1);
            refreshButton.enabled = false;
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    GuiButton refreshButton = (GuiButton) buttonList.get(1);
                    refreshButton.enabled = true;
                }
            }, 3000);
            if (!UndercastModClass.getInstance().connection.isConnected()) {
                connect();
            }

        }
        // cancel/back to main menu
        if (guibutton.id == 2) {
            closeGui();
        }
        // stats button
        if (guibutton.id == 3) {
            String username = Minecraft.getMinecraft().getSession().getUsername();
            try {
                Desktop.getDesktop().browse(new URI("http://oc.tc/" + username));
            } catch (Exception ignored) {
            }
        }
        // sort button
        if (guibutton.id == 4) {
            // move sort index
            UndercastData.sortIndex++;
            // auto spill over function
            if (UndercastData.sortIndex > UndercastData.sortNames.length - 1) {
                UndercastData.sortIndex = 0;
            }
            // update button
            this.buttonList.set(4, new GuiButton(4, width / 2 - 150, height - 28, 48, 20, UndercastData.sortNames[UndercastData.sortIndex]));
            UndercastCustomMethods.sortAndFilterServers();
        }
        if (guibutton.id == 5) {
            if (inGame && UndercastData.isPlayingOvercastNetwork() && this.isRightLocation()) {
                Minecraft.getMinecraft().thePlayer.sendChatMessage("/server lobby");
            } else {
                if (UndercastData.locationIndex == 1) {
                    UndercastData.isEU = true;
                    FMLClientHandler.instance().connectToServerAtStartup("eu.oc.tc", 25565);
                } else {
                    UndercastData.isEU = false;
                    FMLClientHandler.instance().connectToServerAtStartup("us.oc.tc", 25565);
                }
            }
        }
        if (guibutton.id == 6) {
            // move filter index
            UndercastData.filterIndex++;
            // auto spill over function
            if (UndercastData.filterIndex > UndercastData.filterNames.length - 1) {
                UndercastData.filterIndex = 0;
            }
            // update the buttons
            this.buttonList.set(6, new GuiButton(6, width / 2 - 150, height - 52, 48, 20, UndercastData.filterNames[UndercastData.filterIndex]));
            UndercastCustomMethods.sortAndFilterServers();
            UndercastConfig.setIntProperty("lastUsedFilter", UndercastData.filterIndex);
        }
        if (guibutton.id == 7) {
            try {
                Desktop.getDesktop().browse(new URI(UndercastData.updateLink));
            } catch (Exception ignored) {
            }
        }
        if (guibutton.id == 8) {
            if (UndercastData.locationIndex < UndercastData.locationNames.length - 1) {
                UndercastData.locationIndex++;
            } else {
                UndercastData.locationIndex = 0;
            }
            this.buttonList.set(this.buttonList.size() - 1, new GuiButton(8, width / 2 + 102, height - 52, 48, 20, UndercastData.locationNames[UndercastData.locationIndex]));
            UndercastCustomMethods.sortAndFilterServers();
            UndercastConfig.setIntProperty("lastUsedLocation", UndercastData.locationIndex);
        }

    }

    /**
     * This method is a override method for drawing a gui All "painting" should take place in here If you are ingame; do not draw the default background
     */
    @Override
    public void drawScreen(int i, int j, float f) {
        if (!inGame) {
            drawDefaultBackground();
        }
        try{
            this.guiServerInfoSlot.drawScreen(i, j, f);
        } catch(Exception e){

        }
        this.drawCenteredString(this.fontRendererObj, "Overcast Network Server List", width / 2, 20, 16777215);
        Minecraft mc = Minecraft.getMinecraft();
        if (!UndercastData.isUpdate()) {
            mc.fontRenderer.drawString("Used version: " + UndercastModClass.MOD_VERSION, (width - 4) - mc.fontRenderer.getStringWidth("Used version: " + UndercastModClass.MOD_VERSION), 3, 13369344);
            mc.fontRenderer.drawString("Latest version: " + UndercastData.latestVersion, (width - 4) - mc.fontRenderer.getStringWidth("Latest version: " + UndercastData.latestVersion), 12, 255);
        }
        super.drawScreen(i, j, f);
    }

    public void connect(){
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

    /**
     * Gets the current selected server
     * 
     * @param var1
     *            gets the server based on passed index
     */
    public void selectServerIndex(int var1) {
        this.selected = var1;
        if (var1 >= 0 && var1 <= UndercastData.sortedServerInformation.length) {
            this.selectedServer = UndercastData.sortedServerInformation[selected];
        } else {
            this.selectedServer = null;
        }
    }

    /**
     * sees if the index of a server is selected
     */
    public boolean serverIndexSelected(int var1) {
        return var1 == selected;
    }

    /**
     * * Join selected server
     */
    public void joinSelectedServer() {
        if (selected != -1) {
            if (inGame && UndercastData.isPlayingOvercastNetwork() && this.isRightLocation()) {
                Minecraft.getMinecraft().thePlayer.sendChatMessage("/server " + UndercastData.sortedServerInformation[selected].name);
            } else {
                UndercastData.redirect = true;
                UndercastData.directionServer = UndercastData.sortedServerInformation[selected].name;
                if (UndercastData.locationIndex == 1) {
                    UndercastData.isEU = true;
                    FMLClientHandler.instance().connectToServerAtStartup("eu.oc.tc", 25565);
                } else {
                    UndercastData.isEU = false;
                    FMLClientHandler.instance().connectToServerAtStartup("us.oc.tc", 25565);
                }
            }
        }
    }

    @Override
    protected void keyTyped(char par1, int par2) {
        if (par2 == 1) // Escape button
        {
            closeGui();
        }
        super.keyTyped(par1, par2);
    }

    public void closeGui() {
        if (!inGame) {
            Minecraft.getMinecraft().displayGuiScreen(new GuiMainMenu());
        } else {
            Minecraft.getMinecraft().setIngameFocus();
        }
    }

    public boolean isRightLocation() {
        if (UndercastData.isEU && UndercastData.locationIndex == 1) {
            return true;
        } else if (!UndercastData.isEU && UndercastData.locationIndex == 0) {
            return true;
        } else {
            return false;
        }
    }

}
