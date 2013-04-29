package tc.oc.server;

import net.minecraft.client.Minecraft;

import java.util.ArrayList;
import net.minecraft.client.renderer.Tessellator;

class Ares_ServerInfoSlotGui extends Ares_ServerSlotGui {
    final Ares_ServerGUI parent;
    private ArrayList<AresServerInterface> servers;

    /**
     * Default constructor to create list
     *
     * @param guiservers Main server gui screen
     * @param servers    list of servers
     */
    public Ares_ServerInfoSlotGui(Ares_ServerGUI guiservers, ArrayList<AresServerInterface> servers) {
        super(guiservers, guiservers.width, guiservers.height, 32, guiservers.height - 64, 36);
        this.parent = guiservers;
        this.servers = servers;
    }

    /**
     * Main draw method for the individual server boxes
     */
    protected void drawSlot(int i, int j, int k, int l, Tessellator tessellator) {
        AresServerInterface server = servers.get(i);

        parent.drawString(Minecraft.getMinecraft().fontRenderer, getServerName(server.getServer()), j + 2, k + 1, 16777215);
        int serveNameWidth = Minecraft.getMinecraft().fontRenderer.getStringWidth(getServerName(server.getServer()));
        parent.drawString(Minecraft.getMinecraft().fontRenderer, server.getPing(), j + 5 + serveNameWidth, k + 1, 16777215);
        int serverwidth = Minecraft.getMinecraft().fontRenderer.getStringWidth(server.getServer());
        parent.drawString(Minecraft.getMinecraft().fontRenderer, server.getServer(), j + 215 - serverwidth, k + 1, 8421504);
        parent.drawString(Minecraft.getMinecraft().fontRenderer, server.getServerMOTD(), j + 2, k + 12, 8421504);
        parent.drawString(Minecraft.getMinecraft().fontRenderer, server.getServerPlayers(), j + 2, k + 12 + 11, 8421504);
        int pingwidth = Minecraft.getMinecraft().fontRenderer.getStringWidth(server.getServerVersion());
        parent.drawString(Minecraft.getMinecraft().fontRenderer, server.getServerVersion(), j + 215 - pingwidth, k + 12 + 11, 8421504);

    }

    protected int getContentHeight() {
        return (this.getSize()) * 36;
    }

    protected int getSize() {
        return servers.size();
    }

    protected boolean isSelected(int i) {
        return parent.serverIndexSelected(i);
    }

    protected void drawBackground() {
        if (!parent.inGame) {
            parent.drawDefaultBackground();
        }
    }

    private String getServerName(String server) {
        return ("\2475" + Character.toUpperCase(server.charAt(0)) + server.substring(1) + "\247f").replace(".oc.tc", "");
    }

    protected void elementClicked(int i, boolean flag) {
    	//flag = double click
    	if(flag){
    		parent.joinSelectedServer();
    	}
    	else{
    		parent.selectServerIndex(i);
    	}
    }
}

