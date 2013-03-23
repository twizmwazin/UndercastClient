package tc.oc.server;

import net.minecraft.client.Minecraft;
import net.minecraft.src.Tessellator;

import java.util.ArrayList;

class GuiServerInfoSlot extends GuiServerSlot {
    final GuiAresServers parent;
    private ArrayList<ServerInterface> servers;

    public GuiServerInfoSlot(GuiAresServers guiservers, ArrayList<ServerInterface> servers) {
        super(Minecraft.getMinecraft(), guiservers.width, guiservers.height, 32, guiservers.height - 64, 36);
        this.parent = guiservers;
        this.servers = servers;
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
        parent.drawDefaultBackground();
    }

    private String getServerName(String server) {
        return ("\2475" + Character.toUpperCase(server.charAt(0)) + server.substring(1) + "\247f").replace(".oc.tc", "");
    }

    protected void drawSlot(int i, int j, int k, int l, Tessellator tessellator) {
        ServerInterface server = servers.get(i);

        parent.drawString(Minecraft.getMinecraft().fontRenderer, getServerName(server.getServer()), j + 2, k + 1, 16777215);
        int serverwidth = Minecraft.getMinecraft().fontRenderer.getStringWidth(server.getServer());
        parent.drawString(Minecraft.getMinecraft().fontRenderer, server.getServer(), j + 215 - serverwidth, k + 1, 8421504);
        parent.drawString(Minecraft.getMinecraft().fontRenderer, server.getServerMOTD(), j + 2, k + 12, 8421504);
        parent.drawString(Minecraft.getMinecraft().fontRenderer, server.getServerPlayers(), j + 2, k + 12 + 11, 8421504);
        int pingwidth = Minecraft.getMinecraft().fontRenderer.getStringWidth(server.getServerVersion());
        parent.drawString(Minecraft.getMinecraft().fontRenderer, server.getServerVersion(), j + 215 - pingwidth, k + 12 + 11, 8421504);

    }

    protected void elementClicked(int i, boolean flag) {
        parent.selectServerIndex(i);
    }
}

