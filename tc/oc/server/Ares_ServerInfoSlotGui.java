package tc.oc.server;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import tc.oc.AresData;

class Ares_ServerInfoSlotGui extends Ares_ServerSlotGui {

    final Ares_ServerGUI parent;

    /**
     * Default constructor to create list
     *
     * @param guiservers Main server gui screen
     * @param servers list of servers
     */
    public Ares_ServerInfoSlotGui(Ares_ServerGUI guiservers) {
        super(guiservers, guiservers.width, guiservers.height, 32, guiservers.height - 64, 36);
        this.parent = guiservers;
    }

    /**
     * Main draw method for the individual server boxes
     */
    protected void drawSlot(int i, int j, int k, int l, Tessellator tessellator) {
        AresServer server = AresData.sortedServerInformation[i];

        parent.drawString(Minecraft.getMinecraft().fontRenderer, getServerName(server), j + 2, k + 1, 16777215);
        int serveNameWidth = Minecraft.getMinecraft().fontRenderer.getStringWidth(getServerName(server));
        int serverwidth = Minecraft.getMinecraft().fontRenderer.getStringWidth(server.name);
        parent.drawString(Minecraft.getMinecraft().fontRenderer, Integer.toString(server.getPlayerCount()), j + 205, k + 1, 8421504);
        parent.drawString(Minecraft.getMinecraft().fontRenderer, server.getCurrentMap(), j + 2, k + 12, getMatchColor(server));
        parent.drawString(Minecraft.getMinecraft().fontRenderer, "Next: \u00A73" + server.getNextMap(), j + 2, k + 12 + 11, 8421504);

    }

    protected int getContentHeight() {
        return (this.getSize()) * 36;
    }

    protected int getSize() {
        return AresData.serverCount;
    }

    protected boolean isSelected(int i) {
        return parent.serverIndexSelected(i);
    }

    protected void drawBackground() {
        if (!parent.inGame) {
            parent.drawDefaultBackground();
        }
    }

    private String getServerName(AresServer server) {
        return ("\2475" + Character.toUpperCase(server.name.charAt(0)) + server.name.substring(1) + "\247f");
    }

    protected void elementClicked(int i, boolean flag) {
        //flag = double click
        if (flag) {
            parent.joinSelectedServer();
        } else {
            parent.selectServerIndex(i);
        }
    }

    private int getMatchColor(AresServer server) {
        switch (server.matchState) {
            case Started:
                return 0xFFFF00; // yellow
            case Starting:
                return 0x00FF00; // actually Lime
            case Finished:
                return 0x990000; // red
            case Waiting:
                return 0x000000; // white
            default:
                return 0xFFFF00; // yellow
        }
    }
}
