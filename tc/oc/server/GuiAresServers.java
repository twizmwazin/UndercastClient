package tc.oc.server;

import net.minecraft.src.*;
import tc.oc.AresGuiListener;

import java.awt.*;
import java.net.URI;
import java.util.ArrayList;

public class GuiAresServers extends GuiScreen {
    private GuiServerInfoSlot guiServerInfoSlot;

    public ArrayList<ServerInterface> servers;
    private ServerInterface selectedServer;
    private int selected = -1;
    private GuiButton guibuttonrefresh;
    private Boolean inGame;

    public GuiAresServers(boolean inGame) {
        this.inGame = inGame;
        servers = new ArrayList<ServerInterface>();

        new Thread(new Runnable() {
            public void run() {
                if (guibuttonrefresh != null) {
                    guibuttonrefresh.enabled = false;
                }
                for (String server : mod_Ares.servers) {
                    servers.add(new Servers(server));
                }
                if (guibuttonrefresh != null) {
                    guibuttonrefresh.enabled = true;
                }
            }
        }).start();
    }

    public void initGui() {
        StringTranslate stringtranslate = StringTranslate.getInstance();

        this.buttonList.add(new GuiButton(0, this.width / 2 - 100, height - 52, 98, 20, stringtranslate.translateKey("selectServer.select")));
        this.buttonList.add(guibuttonrefresh = new GuiButton(1, this.width / 2 + 2, height - 52, 98, 20, stringtranslate.translateKey("selectServer.refresh")));
        this.buttonList.add(new GuiButton(2, this.width / 2 - 100, height - 28, stringtranslate.translateKey("gui.done")));
        if (!inGame) {
            this.buttonList.add(new GuiButton(3, this.width - 102, height - 28, 98, 20, "Old Menu"));
        }
        this.buttonList.add(new GuiButton(4, 4, height - 28, 98, 20, "Player Stats"));
        guiServerInfoSlot = new GuiServerInfoSlot(this, servers);
    }

    protected void actionPerformed(GuiButton guibutton) {
        if (guibutton.id == 0) {
            mc.displayGuiScreen(new GuiConnecting(this, mc, selectedServer.getServer(), 25565));
        }

        if (guibutton.id == 1) {
            servers.clear();

            Thread threadupdate = new Thread(new Runnable() {
                public void run() {
                    guibuttonrefresh.enabled = false;
                    for (String server : mod_Ares.servers) {
                        servers.add(new Servers(server));
                    }
                    guibuttonrefresh.enabled = true;
                }
            });

            threadupdate.start();
        }

        if (guibutton.id == 2) {
            if (!inGame) {
                this.mc.displayGuiScreen(new GuiMainMenu());
            } else {
                this.mc.setIngameFocus();
            }
        }

        if (guibutton.id == 3) {
            if (!inGame) {
                AresGuiListener.toggleMultiGUI(false);
                this.mc.displayGuiScreen(new GuiMultiplayer(new GuiMainMenu()));
            }
        }

        if (guibutton.id == 4) {
            String username = this.mc.session.username;
            try {
                Desktop.getDesktop().browse(new URI("http://" + mod_Ares.serverDomain + "/" + username));
            } catch (Exception ignored) {
            }
        }
    }

    public void drawScreen(int i, int j, float f) {
        this.drawDefaultBackground();
        this.guiServerInfoSlot.drawScreen(i, j, f);
        this.drawCenteredString(this.fontRenderer, "Project Ares Server List", this.width / 2, 20, 16777215);
        super.drawScreen(i, j, f);
    }

    public void selectServerIndex(int var1) {
        this.selected = var1;
        if (var1 >= 0 && var1 <= servers.size()) {
            this.selectedServer = servers.get(selected);
        } else {
            this.selectedServer = null;
        }
    }

    public boolean serverIndexSelected(int var1) {
        return var1 == selected;
    }
}
