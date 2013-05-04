package tc.oc.server;
//You may not release this source under any condition, it must be linked to this page
//You may recompile and publish as long as skipperguy12 and Guru_Fraser are given credit
//You may not claim this to be your own
//You may not remove these comments

import java.awt.*;
import java.net.URI;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.util.StringTranslate;
import tc.oc.AresData;
import tc.oc.GuiButtonTooltip;
import tc.oc.AresCustomMethods;

public class Ares_ServerGUI extends GuiScreen {

    private Ares_ServerInfoSlotGui guiServerInfoSlot;
    private AresServer selectedServer;
    private int selected = -1;
    private GuiButton guibuttonrefresh;
    public Boolean inGame;
    private boolean toggletooltip;
    private int sortIndex;
    private String[] sortNames = {"Web", "Match", "Player", "Abc"};

    /**
     * Default constructor
     *
     * @param inGame Boolean if you are on a server or not
     */
    public Ares_ServerGUI(boolean inGame) {
        this.inGame = inGame;
        AresData.reload();
    }

    /**
     * This is init of the gui when it is about to get drawn. You should only
     * have buttons/control elements in here.
     */
    public void initGui() {
        StringTranslate stringtranslate = StringTranslate.getInstance();

        this.buttonList.add(new GuiButtonTooltip(0, this.width / 2 - 100, height - 52, 98, 20, stringtranslate.translateKey("selectServer.select"), "Join / Swap to the selected server"));
        this.buttonList.add(guibuttonrefresh = new GuiButtonTooltip(1, this.width / 2 + 2, height - 52, 98, 20, stringtranslate.translateKey("selectServer.refresh"), "Refresh the server list"));
        this.buttonList.add(new GuiButtonTooltip(2, this.width / 2 + 2, height - 28, 98, 20, stringtranslate.translateKey("gui.cancel"), "Close the server list"));
        this.buttonList.add(new GuiButtonTooltip(3, this.width / 2 - 100, height - 28, 98, 20, "Player Stats", "Open your player stats in the browser"));
        this.buttonList.add(new GuiButtonTooltip(4, this.width / 2 - 150, height - 28, 48, 20, AresData.sortNames[sortIndex], "Sort the servers - Match is currently disabled (because we don't know the status)"));
        this.buttonList.add(new GuiButtonTooltip(5, this.width / 2 + 102, height - 28, 48, 20, "Lobby", "Join / Swap to the lobby"));
        guiServerInfoSlot = new Ares_ServerInfoSlotGui(this);
    }

    /**
     * If a button is clicked this method gets called. The id is the number
     * given to the button during init.
     */
    protected void actionPerformed(GuiButton guibutton) {
        //join button
        if (guibutton.id == 0) {
            joinSelectedServer();
        }
        //refresh button
        if (guibutton.id == 1) {
            AresData.reload();
        }
        //cancel/back to main menu
        if (guibutton.id == 2) {
            if (!inGame) {
                this.mc.displayGuiScreen(new GuiMainMenu());
            } else {
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
            AresData.sortIndex++;
            // auto spill over function
            if (AresData.sortIndex > AresData.sortNames.length - 1) {
                AresData.sortIndex = 0;
            }
            // update button
            this.buttonList.set(4, new GuiButton(4, this.width / 2 - 150, height - 28, 48, 20, AresData.sortNames[AresData.sortIndex]));
            AresCustomMethods.sortServers();
        }
        if (guibutton.id == 5) {
            if (inGame) {
                Minecraft.getMinecraft().thePlayer.sendChatMessage("/server lobby");
            } else {
                ServerData joinServer = new ServerData("us.oc.tc", "us.oc.tc:25565");
                mc.displayGuiScreen(new GuiConnecting(this, this.mc, joinServer));
            }
        }
    }

    /**
     * This method is a override method for drawing a gui All "painting" should
     * take place in here If you are ingame; do not draw the default background
     */
    public void drawScreen(int i, int j, float f) {
        if (!inGame) {
            drawDefaultBackground();
        }
        this.guiServerInfoSlot.drawScreen(i, j, f);
        this.drawCenteredString(this.fontRenderer, "Overcast Network Server List", this.width / 2, 20, 16777215);
        super.drawScreen(i, j, f);
    }

    /**
     * Gets the current selected server
     *
     * @param var1 gets the server based on passed index
     */
    public void selectServerIndex(int var1) {
        this.selected = var1;
        if (var1 >= 0 && var1 <= AresData.sortedServerInformation.length) {
            this.selectedServer = AresData.sortedServerInformation[selected];
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
            if (inGame) {
                Minecraft.getMinecraft().thePlayer.sendChatMessage("/server " + AresData.sortedServerInformation[selected].name);
            } else {
                AresData.redirect = true;
                AresData.directionServer = AresData.sortedServerInformation[selected].name;
                ServerData joinServer = new ServerData("us.oc.tc", "us.oc.tc:25565");
                mc.displayGuiScreen(new GuiConnecting(this, this.mc, joinServer));
            }
        }
    }
}
