package undercast.client.server;
//You may not release this source under any condition, it must be linked to this page
//You may recompile and publish as long as skipperguy12 and Guru_Fraser are given credit
//You may not claim this to be your own
//You may not remove these comments

import java.awt.*;
import java.net.URI;
import java.util.Timer;
import java.util.TimerTask;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.util.StringTranslate;
import undercast.client.*;

public class UndercastServerGUI extends GuiScreen {

    private UndercastServerInfoSlotGui guiServerInfoSlot;
    private UndercastServer selectedServer;
    private int selected = -1;
    private GuiButton guibuttonrefresh;
    public Boolean inGame;

    /**
     * Default constructor
     *
     * @param inGame Boolean if you are on a server or not
     */
    public UndercastServerGUI(boolean inGame) {
        this.inGame = inGame;
        UndercastData.reload(true);
    }

    /**
     * This is init of the gui when it is about to get drawn. You should only
     * have buttons/control elements in here.
     */
    @Override
    public void initGui() {
        StringTranslate stringtranslate = StringTranslate.getInstance();

        this.buttonList.add(new GuiButtonTooltip(0, this.width / 2 - 100, height - 52, 98, 20, stringtranslate.translateKey("selectServer.select"), "Join / Swap to the selected server"));
        this.buttonList.add(guibuttonrefresh = new GuiButtonTooltip(1, this.width / 2 + 2, height - 52, 98, 20, stringtranslate.translateKey("selectServer.refresh"), "Refresh the server list"));
        this.buttonList.add(new GuiButtonTooltip(2, this.width / 2 + 2, height - 28, 98, 20, stringtranslate.translateKey("gui.cancel"), "Close the server list"));
        this.buttonList.add(new GuiButtonTooltip(3, this.width / 2 - 100, height - 28, 98, 20, "Player Stats", "Open your player stats in the browser"));
        this.buttonList.add(new GuiButtonTooltip(4, this.width / 2 - 150, height - 28, 48, 20, UndercastData.sortNames[UndercastData.sortIndex], "Sort the servers"));
        this.buttonList.add(new GuiButtonTooltip(5, this.width / 2 + 102, height - 28, 48, 20, "Lobby", "Join / Swap to the lobby"));
        this.buttonList.add(new GuiButtonTooltip(6, this.width / 2 - 150, height - 52, 48, 20, UndercastData.filterNames[UndercastData.filterIndex], "Filter the servers by the server type"));
        if (!UndercastData.isUpdate()) {
            this.buttonList.add(new GuiButtonTooltip(7, this.width - 54, 21, 48, 20, "Update", "Opens the download website for the latest version."));
        }
        guiServerInfoSlot = new UndercastServerInfoSlotGui(this);
    }

    /**
     * If a button is clicked this method gets called. The id is the number
     * given to the button during init.
     */
    @Override
    protected void actionPerformed(GuiButton guibutton) {
        //join button
        if (guibutton.id == 0) {
            joinSelectedServer();
        }
        //refresh button
        if (guibutton.id == 1) {
            UndercastData.reload(true);
            GuiButtonTooltip refreshButton = (GuiButtonTooltip) buttonList.get(1);
            refreshButton.enabled = false;
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    GuiButtonTooltip refreshButton = (GuiButtonTooltip) buttonList.get(1);
                    refreshButton.enabled = true;
                }
            }, 3000);
        }
        //cancel/back to main menu
        if (guibutton.id == 2) {
            if (!inGame) {
                this.mc.displayGuiScreen(new GuiMainMenu());
            } else {
                UndercastData.removeNextChatMessage = false;
                this.mc.setIngameFocus();
            }
        }
        //stats button
        if (guibutton.id == 3) {
            String username = this.mc.session.username;
            try {
                Desktop.getDesktop().browse(new URI("http://oc.tc/" + username));
            } catch (Exception ignored) {
            }
        }
        //sort button
        if (guibutton.id == 4) {
            // move sort index
            UndercastData.sortIndex++;
            // auto spill over function
            if (UndercastData.sortIndex > UndercastData.sortNames.length - 1) {
                UndercastData.sortIndex = 0;
            }
            // update button
            this.buttonList.set(4, new GuiButtonTooltip(4, this.width / 2 - 150, height - 28, 48, 20, UndercastData.sortNames[UndercastData.sortIndex], "Sort the servers"));
            UndercastCustomMethods.sortAndFilterServers();
        }
        if (guibutton.id == 5) {
            if (inGame && UndercastData.isPlayingOvercastNetwork()) {
                Minecraft.getMinecraft().thePlayer.sendChatMessage("/server lobby");
            } else {
                ServerData joinServer = new ServerData("us.oc.tc", "us.oc.tc:25565");
                mc.displayGuiScreen(new GuiConnecting(this, this.mc, joinServer));
            }
        }
        if (guibutton.id == 6) {
            // move filter index
            UndercastData.filterIndex++;
            // auto spill over function
            if (UndercastData.filterIndex > UndercastData.filterNames.length - 1) {
                UndercastData.filterIndex = 0;
            }
            //update the buttons
            this.buttonList.set(6, new GuiButtonTooltip(6, this.width / 2 - 150, height - 52, 48, 20, UndercastData.filterNames[UndercastData.filterIndex], "Filter the servers by the server type"));
            UndercastCustomMethods.sortAndFilterServers();
        }
        if (guibutton.id == 7) {
            try {
                Desktop.getDesktop().browse(new URI(UndercastData.updateLink));
            } catch (Exception ignored) {
            }
        }
    }

    /**
     * This method is a override method for drawing a gui All "painting" should
     * take place in here If you are ingame; do not draw the default background
     */
    @Override
    public void drawScreen(int i, int j, float f) {
        if (!inGame) {
            drawDefaultBackground();
        }
        this.guiServerInfoSlot.drawScreen(i, j, f);
        this.drawCenteredString(this.fontRenderer, "Overcast Network Server List", this.width / 2, 20, 16777215);
        if (!UndercastData.isUpdate()) {
            mc.fontRenderer.drawString("Used version: " + UndercastModClass.MOD_VERSION, (this.width - 4) - mc.fontRenderer.getStringWidth("Used version: " + UndercastModClass.MOD_VERSION), 3, 13369344);
            mc.fontRenderer.drawString("Latest version: " + UndercastData.latestVersion, (this.width - 4) - mc.fontRenderer.getStringWidth("Latest version: " + UndercastData.latestVersion), 12, 255);
        }
        super.drawScreen(i, j, f);
    }

    /**
     * Gets the current selected server
     *
     * @param var1 gets the server based on passed index
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
     * *
     * Join selected server
     */
    public void joinSelectedServer() {
        if (selected != -1) {
            if (inGame && UndercastData.isPlayingOvercastNetwork()) {
                Minecraft.getMinecraft().thePlayer.sendChatMessage("/server " + UndercastData.sortedServerInformation[selected].name);
            } else {
                UndercastData.redirect = true;
                UndercastData.directionServer = UndercastData.sortedServerInformation[selected].name;
                ServerData joinServer = new ServerData("us.oc.tc", "us.oc.tc:25565");
                mc.displayGuiScreen(new GuiConnecting(this, this.mc, joinServer));
            }
        }
    }

    protected void keyTyped(char par1, int par2) {
        if (par2 == 1) //Escape button
        {
            UndercastData.removeNextChatMessage = false;
        }
    }
}
