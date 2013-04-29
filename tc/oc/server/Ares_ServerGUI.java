package tc.oc.server;
//You may not release this source under any condition, it must be linked to this page
//You may recompile and publish as long as skipperguy12 and Guru_Fraser are given credit
//You may not claim this to be your own
//You may not remove these comments


import java.awt.*;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.util.StringTranslate;
import net.minecraft.util.StringUtils;
import tc.oc.AresModClass;

public class Ares_ServerGUI extends GuiScreen {
    private Ares_ServerInfoSlotGui guiServerInfoSlot;

    public ArrayList<AresServerInterface> servers;
    private AresServerInterface selectedServer;
    private int selected = -1;
    private GuiButton guibuttonrefresh;
    public Boolean inGame;
    private boolean toggletooltip;
    private int sortIndex;
    private String[] sortNames = {"Web","Match","Player","Abc"};

    /**
     * Default constructor
     *
     * @param inGame Boolean if you are on a server or not
     */
    public Ares_ServerGUI(boolean inGame) {
        this.inGame = inGame;
        servers = new ArrayList<AresServerInterface>();
        sortIndex = 0;
        //creates server list
        for (String server : AresModClass.masterServerList) {
            servers.add(new AresServer(server, 25565));
        }
        //poll the servers
        pollServers();
    }

    /**
     * This is init of the gui when it is about to get drawn. You should only
     * have buttons/control elements in here.
     */
    public void initGui() {
        StringTranslate stringtranslate = StringTranslate.getInstance();

        this.buttonList.add(new GuiButton(0, this.width / 2 - 100, height - 52, 98, 20, stringtranslate.translateKey("selectServer.select")));
        this.buttonList.add(guibuttonrefresh = new GuiButton(1, this.width / 2 + 2, height - 52, 98, 20, stringtranslate.translateKey("selectServer.refresh")));
        this.buttonList.add(new GuiButton(2, this.width / 2 + 2, height - 28, 98, 20, stringtranslate.translateKey("gui.cancel")));
        this.buttonList.add(new GuiButton(3, this.width / 2 - 100, height - 28, 98, 20, "Player Stats"));
        this.buttonList.add(new GuiButton(4, this.width / 2 - 150, height - 28, 48, 20, sortNames[sortIndex]));
        guiServerInfoSlot = new Ares_ServerInfoSlotGui(this, servers);
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
            pollServers();
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
            //move sort index
            sortIndex++;
            //auto spill over function
            if(sortIndex>sortNames.length-1)
                sortIndex=0;
            //update button
            this.buttonList.set(4,new GuiButton(4, this.width / 2 - 150, height - 28, 48, 20, sortNames[sortIndex]));
            //if the index is moving to web then use the downloaded server list
            if(sortNames[sortIndex].equalsIgnoreCase("web")){
                servers.clear();
                for (String server : AresModClass.masterServerList) {
                    servers.add(new AresServer(server, 25565));
                }
                pollServers();
            }
            //sort based on motd match
            else if(sortNames[sortIndex].equalsIgnoreCase("match")){
                //copy the array of servers
                ArrayList<String> currentServerList = new ArrayList<String>();
                //sort the servers
                String[][] serverData = new String[servers.size()][2];
                for (int i=0; i<servers.size();i++) {
                    serverData[i][0]=servers.get(i).getServerMOTD().substring(1,2);
                    serverData[i][1]=servers.get(i).getServer();
                }
                //green
                for(String[] info: serverData){
                    if(info[0].equalsIgnoreCase("a")){
                        currentServerList.add(info[1]);
                    }
                }
                //white
                for(String[] info: serverData){
                    if(info[0].equalsIgnoreCase("7")){
                        currentServerList.add(info[1]);
                    }
                }
                //red
                for(String[] info: serverData){
                    if(info[0].equalsIgnoreCase("c")){
                        currentServerList.add(info[1]);
                    }
                }
                //yellow
                for(String[] info: serverData){
                    if(info[0].equalsIgnoreCase("6")){
                        currentServerList.add(info[1]);
                    }
                }
                ///unknown conditions
                for(String[] info: serverData){
                    if(info[0].equalsIgnoreCase("?")){
                        currentServerList.add(info[1]);
                    }
                }
                //add new servers and poll
                servers.clear();
                for (String server : currentServerList) {
                    servers.add(new AresServer(server, 25565));
                }
                pollServers();
            }
            //sort based on player count
            else if(sortNames[sortIndex].equalsIgnoreCase("player")){
                //copy the array of servers
                ArrayList<String> currentServerList = new ArrayList<String>();
                //sort the servers
                String[][] serverData = new String[servers.size()][2];
                for (int i=0; i<servers.size();i++) {
                    serverData[i][0]=StringUtils.stripControlCodes(servers.get(i).getServerPlayers().split("/")[0]);
                    //if player count it unknown have it on the bottem
                    if(serverData[i][0].contains("?")){
                        serverData[i][0]=""+Integer.MAX_VALUE;
                    }
                    serverData[i][1]=servers.get(i).getServer();
                }
                //sort
                Arrays.sort(serverData, new Comparator<String[]>() {
                    @Override
                    public int compare(final String[] entry1, final String[] entry2) {
                        final Integer pop1 = Integer.parseInt(entry1[0]);
                        final Integer pop2 = Integer.parseInt(entry2[0]);
                        return pop1.compareTo(pop2);
                    }
                });
                //add to arraylist
                for(String[] info: serverData){
                    currentServerList.add(info[1]);
                }
                //add new servers and poll
                servers.clear();
                for (String server : currentServerList) {
                    servers.add(new AresServer(server, 25565));
                }
                pollServers();
            }
            //if the servers are being sorted abc sort the list and update
            else if(sortNames[sortIndex].equalsIgnoreCase("abc")){
                servers.clear();
                //copy the array of servers
                ArrayList<String> currentServerList = new ArrayList<String>(AresModClass.masterServerList);
                //sort the servers
                Collections.sort(currentServerList);
                for (String server : currentServerList) {
                    servers.add(new AresServer(server, 25565));
                }
                pollServers();
            }

        }
    }

    /**
     * This method is a override method for drawing a gui All "painting" should
     * take place in here
     * If you are ingame; do not draw the default background
     */
    public void drawScreen(int i, int j, float f) {
        if (!inGame) {
            drawDefaultBackground();
        }
        this.guiServerInfoSlot.drawScreen(i, j, f);
        this.drawCenteredString(this.fontRenderer, "Project Ares Server List", this.width / 2, 20, 16777215);
        super.drawScreen(i, j, f);
    }

    /**
     * Gets the current selected server
     *
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

    /***
     * Join selected server
     */
    public void joinSelectedServer() {
        if(selectedServer != null) {
            String serverip = selectedServer.getServer();
            String serverport = Integer.toString(selectedServer.getPort());
            ServerData joinServer = new ServerData(serverip, serverip + ":" + serverport);
            // connect
            mc.displayGuiScreen(new GuiConnecting(this, this.mc, joinServer));
        }
    }

    /**
     * Poll all the servers(multi thread)
     */
    public void pollServers(){
        for (final AresServerInterface server : servers) {
            Thread thread = new Thread() {
                public void run() {
                    server.pollServer();
                }
            };
            thread.start();
        }
    }
}
