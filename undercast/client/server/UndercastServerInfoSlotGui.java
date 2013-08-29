package undercast.client.server;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import undercast.client.UndercastData;

class UndercastServerInfoSlotGui extends UndercastServerSlotGui {

    final UndercastServerGUI parent;

    /**
     * Default constructor to create list
     * 
     * @param guiservers
     *            Main server gui screen
     * @param servers
     *            list of servers
     */
    public UndercastServerInfoSlotGui(UndercastServerGUI guiservers) {
        super(guiservers, guiservers.width, guiservers.height, 32, guiservers.height - 64, 36);
        this.parent = guiservers;
    }

    /**
     * Main draw method for the individual server boxes
     */
    @Override
    protected void drawSlot(int i, int j, int k, int l, Tessellator tessellator) {
        try{
            UndercastServer server = UndercastData.sortedServerInformation[i];
            parent.drawString(Minecraft.getMinecraft().fontRenderer, getServerName(server), j + 2, k + 1, 16777215);
            parent.drawString(Minecraft.getMinecraft().fontRenderer, Integer.toString(server.getPlayerCount()), j + 198, k + 1, 8421504);
            parent.drawString(Minecraft.getMinecraft().fontRenderer, server.getCurrentMap(), j + 2, k + 12, getMatchColor(server));
            parent.drawString(Minecraft.getMinecraft().fontRenderer, "Next: \u00A73" + server.getNextMap(), j + 2, k + 12 + 11, 8421504);}
        catch(Exception e){

        }

    }

    @Override
    protected int getContentHeight() {
        return (this.getSize()) * 36;
    }

    @Override
    protected int getSize() {
        return UndercastData.filteredServerCount;
    }

    @Override
    protected boolean isSelected(int i) {
        return parent.serverIndexSelected(i);
    }

    @Override
    protected void drawBackground() {
        if (!parent.inGame) {
            parent.drawDefaultBackground();
        }
    }

    private String getServerName(UndercastServer server) {
        return ("\2475" + Character.toUpperCase(server.name.charAt(0)) + server.name.substring(1) + "\247f");
    }

    @Override
    protected void elementClicked(int i, boolean flag) {
        // flag = double click
        if (flag) {
            parent.joinSelectedServer();
        } else {
            parent.selectServerIndex(i);
        }
    }

    private int getMatchColor(UndercastServer server) {
        switch (server.matchState) {
        case Started:
            return 0xFFFF00; // yellow
        case Starting:
            return 0x00FF00; // actually Lime
        case Finished:
            return 0x990000; // red
        case Waiting:
            return 0x0000A0; // blue like on the signs
        default:
            return 0xF87431; // Sienna1 = orange
        }
    }
}
