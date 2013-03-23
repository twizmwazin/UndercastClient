package tc.oc.server;

import net.minecraft.src.*;
import tc.oc.AresGuiListener;

import java.awt.*;
import java.net.URI;
import java.util.ArrayList;

public class Ares_ServerGUI extends GuiScreen {
    private Ares_ServerInfoSlotGui guiServerInfoSlot;

    public ArrayList<AresServerInterface> servers;
    private AresServerInterface selectedServer;
    private int selected = -1;
    private GuiButton guibuttonrefresh;
    public Boolean inGame;
    private boolean toggletooltip;

    /**
	 * Default constructor
	 * @param inGame Boolean if you are on a server or not
	 */
    public Ares_ServerGUI(boolean inGame) {
        this.inGame = inGame;
        servers = new ArrayList<AresServerInterface>();
        //creates server list
        for (String server : mod_Ares.servers) {
            servers.add(new AresServer(server,25565));
        }
        //poll the servers
        for (final AresServerInterface server : servers) {
        	Thread thread = new Thread() {
				public void run() {
					server.pollServer();
				}
			};
			thread.start();
        }
    }
    /**
	 * This is init of the gui when it is about to get drawn. You should only
	 * have buttons/control elements in here.
	 */
    public void initGui() {
        StringTranslate stringtranslate = StringTranslate.getInstance();

        this.buttonList.add(new GuiButton(0, this.width / 2 - 100, height - 52, 98, 20, stringtranslate.translateKey("selectServer.select")));
        this.buttonList.add(guibuttonrefresh = new GuiButton(1, this.width / 2 + 2, height - 52, 98, 20, stringtranslate.translateKey("selectServer.refresh")));
        this.buttonList.add(new GuiButton(2, this.width / 2 - 100, height - 28, stringtranslate.translateKey("gui.done")));
        if (!inGame) {
            this.buttonList.add(new GuiButton(3, this.width / 2 + 102, height - 28, 98, 20, "Old Menu"));
        }
        this.buttonList.add(new GuiButton(4, this.width / 2 - 200, height - 28, 98, 20, "Player Stats"));
        guiServerInfoSlot = new Ares_ServerInfoSlotGui(this, servers);
    }

    /**
	 * If a button is clicked this method gets called. The id is the number
	 * given to the button during init.
	 */
    protected void actionPerformed(GuiButton guibutton) {
        //join button
    	if (guibutton.id == 0) {
    		String serverip = selectedServer.getServer();
			String serverport = Integer.toString(selectedServer.getPort());
			ServerData joinServer = new ServerData(serverip,serverip+":"+serverport);
			//connect
			mc.displayGuiScreen(new GuiConnecting(this, this.mc, joinServer));
        }
    	//refresh button
    	if (guibutton.id == 1) {
    		//goes through each server and repolls them
    		for (final AresServerInterface server : servers) {
    			Thread thread = new Thread() {
    				public void run() {
    					server.pollServer();
    				}
    			};
    			thread.start();
    		}
    	}
        //cancel/back to main menu
        if (guibutton.id == 2) {
            if (!inGame) {
                this.mc.displayGuiScreen(new GuiMainMenu());
            } else {
                this.mc.setIngameFocus();
            }
        }
        //old menu button
        if (guibutton.id == 3) {
            if (!inGame) {
                AresGuiListener.toggleMultiGUI(false);
                this.mc.displayGuiScreen(new GuiMultiplayer(new GuiMainMenu()));
            }
        }
        //stats button
        if (guibutton.id == 4) {
            String username = this.mc.session.username;
            try {
                Desktop.getDesktop().browse(new URI("http://" + mod_Ares.CONFIG.serverDomain + "/" + username));
            } catch (Exception ignored) {
            }
        }
    }
    
    /**
	 * This method is a override method for drawing a gui All "painting" should
	 * take place in here
	 * If you are ingame; do not draw the default background
	 */
    public void drawScreen(int i, int j, float f) {
    	if(!inGame) {
			drawDefaultBackground();
        }
        this.guiServerInfoSlot.drawScreen(i, j, f);
        this.drawCenteredString(this.fontRenderer, "Project Ares Server List", this.width / 2, 20, 16777215);
        super.drawScreen(i, j, f);
    }

    /**
     * Gets the current selected server
     * @param var1 gets the server based on passed index
     */
    public void selectServerIndex(int var1) {
        this.selected = var1;
        if (var1 >= 0 && var1 <= servers.size()) {
            this.selectedServer = servers.get(selected);
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
}
