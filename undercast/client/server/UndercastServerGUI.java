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
import net.minecraft.client.resources.I18n;
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
    	int width = this.field_146294_l;
    	int height = this.field_146295_m;
    	
        this.field_146292_n.add(new GuiButton(0, width / 2 - 100, height - 52, 98, 20, I18n.getStringParams("selectServer.select")));
        this.field_146292_n.add(guibuttonrefresh = new GuiButton(1, width / 2 + 2, height - 52, 98, 20, I18n.getStringParams("selectServer.refresh")));
        this.field_146292_n.add(new GuiButton(2, width / 2 + 2, height - 28, 98, 20, I18n.getStringParams("gui.cancel")));
        this.field_146292_n.add(new GuiButton(3, width / 2 - 100, height - 28, 98, 20, "Player Stats"));
        this.field_146292_n.add(new GuiButton(4, width / 2 - 150, height - 28, 48, 20, UndercastData.sortNames[UndercastData.sortIndex]));
        this.field_146292_n.add(new GuiButton(5, width / 2 + 102, height - 28, 48, 20, "Lobby"));
        this.field_146292_n.add(new GuiButton(6, width / 2 - 150, height - 52, 48, 20, UndercastData.filterNames[UndercastData.filterIndex]));
        if (!UndercastData.isUpdate()) {
            this.field_146292_n.add(new GuiButton(7, width - 54, 21, 48, 20, "Update"));
        }
        this.field_146292_n.add(new GuiButton(8, width / 2 + 102, height - 52, 48, 20, UndercastData.locationNames[UndercastData.locationIndex]));
        guiServerInfoSlot = new UndercastServerInfoSlotGui(this);
    }

    /**
     * If a button is clicked this method gets called. The id is the number given to the button during init.
     */
    @Override
    protected void func_146284_a(GuiButton guibutton) {
    	int width = this.field_146294_l;
    	int height = this.field_146295_m;
    	
        // join button
        if (guibutton.field_146127_k == 0) {
            joinSelectedServer();
        }
        // refresh button
        if (guibutton.field_146127_k == 1) {
            UndercastData.reloadServerInformations(true);
            GuiButton refreshButton = (GuiButton) field_146292_n.get(1);
            refreshButton.field_146124_l = false;
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                	GuiButton refreshButton = (GuiButton) field_146292_n.get(1);
                    refreshButton.field_146124_l = true;
                }
            }, 3000);
        }
        // cancel/back to main menu
        if (guibutton.field_146127_k == 2) {
            closeGui();
        }
        // stats button
        if (guibutton.field_146127_k == 3) {
        	/*
            String username = Minecraft.getMinecraft().func_110432_I().func_111285_a();
            try {
                Desktop.getDesktop().browse(new URI("http://oc.tc/" + username));
            } catch (Exception ignored) {
            }
            */ //TODO: FIX
        }
        // sort button
        if (guibutton.field_146127_k == 4) {
            // move sort index
            UndercastData.sortIndex++;
            // auto spill over function
            if (UndercastData.sortIndex > UndercastData.sortNames.length - 1) {
                UndercastData.sortIndex = 0;
            }
            // update button
            this.field_146292_n.set(4, new GuiButton(4, width / 2 - 150, height - 28, 48, 20, UndercastData.sortNames[UndercastData.sortIndex]));
            UndercastCustomMethods.sortAndFilterServers();
        }
        if (guibutton.field_146127_k == 5) {
            if (inGame && UndercastData.isPlayingOvercastNetwork() && this.isRightLocation()) {
                Minecraft.getMinecraft().thePlayer.sendChatMessage("/server lobby");
            } else {
                ServerData joinServer;
                if (UndercastData.locationIndex == 1) {
                    joinServer = new ServerData("eu.oc.tc", "eu.oc.tc:25565");
                } else {
                    joinServer = new ServerData("us.oc.tc", "us.oc.tc:25565");
                }
                Minecraft.getMinecraft().func_147108_a(new GuiConnecting(this, Minecraft.getMinecraft(), joinServer));
            }
        }
        if (guibutton.field_146127_k == 6) {
            // move filter index
            UndercastData.filterIndex++;
            // auto spill over function
            if (UndercastData.filterIndex > UndercastData.filterNames.length - 1) {
                UndercastData.filterIndex = 0;
            }
            // update the buttons
            this.field_146292_n.set(6, new GuiButton(6, width / 2 - 150, height - 52, 48, 20, UndercastData.filterNames[UndercastData.filterIndex]));
            UndercastCustomMethods.sortAndFilterServers();
            UndercastConfig.setIntProperty("lastUsedFilter", UndercastData.filterIndex);
        }
        if (guibutton.field_146127_k == 7) {
            try {
                Desktop.getDesktop().browse(new URI(UndercastData.updateLink));
            } catch (Exception ignored) {
            }
        }
        if (guibutton.field_146127_k == 8) {
            if (UndercastData.locationIndex < UndercastData.locationNames.length - 1) {
                UndercastData.locationIndex++;
            } else {
                UndercastData.locationIndex = 0;
            }
            this.field_146292_n.set(7, new GuiButton(8, width / 2 + 102, height - 52, 48, 20, UndercastData.locationNames[UndercastData.locationIndex]));
            UndercastCustomMethods.sortAndFilterServers();
            UndercastConfig.setIntProperty("lastUsedLocation", UndercastData.locationIndex);
        }

    }

    /**
     * This method is a override method for drawing a gui All "painting" should take place in here If you are ingame; do not draw the default background
     */
    @Override
    public void drawScreen(int i, int j, float f) {
    	int width = this.field_146294_l;
    	
        if (!inGame) {
        	func_146276_q_();
        }
        this.guiServerInfoSlot.drawScreen(i, j, f);
        this.drawCenteredString(this.field_146289_q, "Overcast Network Server List", width / 2, 20, 16777215);
        Minecraft mc = Minecraft.getMinecraft();
        if (!UndercastData.isUpdate()) {
            mc.fontRenderer.drawString("Used version: " + UndercastModClass.MOD_VERSION, (width - 4) - mc.fontRenderer.getStringWidth("Used version: " + UndercastModClass.MOD_VERSION), 3, 13369344);
            mc.fontRenderer.drawString("Latest version: " + UndercastData.latestVersion, (width - 4) - mc.fontRenderer.getStringWidth("Latest version: " + UndercastData.latestVersion), 12, 255);
        }
        super.drawScreen(i, j, f);
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
                ServerData joinServer;
                if (UndercastData.locationIndex == 1) {
                    joinServer = new ServerData("eu.oc.tc", "eu.oc.tc:25565");
                } else {
                    joinServer = new ServerData("us.oc.tc", "us.oc.tc:25565");
                }
                Minecraft.getMinecraft().func_147108_a(new GuiConnecting(this, Minecraft.getMinecraft(), joinServer));
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
        	Minecraft.getMinecraft().func_147108_a(new GuiMainMenu());
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
