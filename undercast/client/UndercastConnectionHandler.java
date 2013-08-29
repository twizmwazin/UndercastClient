package undercast.client;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.network.IConnectionHandler;
import cpw.mods.fml.common.network.Player;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.NetClientHandler;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.NetLoginHandler;
import net.minecraft.network.packet.NetHandler;
import net.minecraft.network.packet.Packet1Login;
import net.minecraft.server.MinecraftServer;
import undercast.client.update.Undercast_UpdaterThread;

/**
 * @author Flv92
 */
public class UndercastConnectionHandler implements IConnectionHandler {

    @Override
    public void playerLoggedIn(Player player, NetHandler netHandler, INetworkManager manager) {
    }

    @Override
    public String connectionReceived(NetLoginHandler netHandler, INetworkManager manager) {
        return null;
    }

    /**
     * Fired when a remote connection is opened CLIENT SIDE
     * 
     * @param netClientHandler
     * @param server
     * @param port
     * @param manager
     */
    @Override
    public void connectionOpened(NetHandler netClientHandler, String server, int port, INetworkManager manager) {
        UndercastData.setTeam(UndercastData.Teams.Observers);
        // if logging onto an OvercastNetwork server, then enable the main mod
        if (((NetClientHandler) netClientHandler).getNetManager().getSocketAddress().toString().contains(".oc.tc") && !((NetClientHandler) netClientHandler).getNetManager().getSocketAddress().toString().contains("mc.oc.tc")) {
            // What happens if logs into OvercastNetwork
            UndercastData.isOC = true;
            UndercastData.isLobby = true;
            UndercastData.guiShowing = true;
            System.out.println("Undercast Mod activated!");
            UndercastData.setTeam(UndercastData.Teams.Observers);
            UndercastData.setServer("Lobby");
            UndercastModClass.getInstance().playTimeCounter = new PlayTimeCounterThread();
        } else {
            UndercastData.isOC = false;
        }
        // update notifier
        if (!UndercastData.isUpdate()) {
            Thread thread = new Thread() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(3000);
                        for (int c = 0; c < 10; c++) { // don't wait longer than
                                                       // 10 sec
                            Thread.sleep(1000);
                            if (Undercast_UpdaterThread.finished) {
                                break;
                            }
                        }
                    } catch (InterruptedException e) {
                    }
                    Minecraft mc = FMLClientHandler.instance().getClient();
                    mc.thePlayer
                            .addChatMessage("\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-");
                    mc.thePlayer.addChatMessage("[UndercastMod]: A New Version of the Undercast Mod is avaliable");
                    mc.thePlayer.addChatMessage("[UndercastMod]: Link: \u00A74" + UndercastData.updateLink);
                    mc.thePlayer
                            .addChatMessage("\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-");
                }
            };
            thread.start();
        }
    }

    /**
     * 
     * Fired when a local connection is opened
     * 
     * CLIENT SIDE
     * 
     * @param netClientHandler
     * @param server
     */
    @Override
    public void connectionOpened(NetHandler netClientHandler, MinecraftServer server, INetworkManager manager) {
    }

    @Override
    public void connectionClosed(INetworkManager manager) {
        Minecraft mc = FMLClientHandler.instance().getClient();
        UndercastData.isOC = false;
        UndercastData.guiShowing = false;
        UndercastData.setTeam(UndercastData.Teams.Observers);
        UndercastData.resetKills();
        UndercastData.resetKilled();
        UndercastData.resetDeaths();
        UndercastData.resetKillstreak();
        UndercastData.resetLargestKillstreak();
        UndercastData.resetPreviousKillstreak();
        UndercastData.setMap("Attempting to fetch map...");
        if (mc.gameSettings.gammaSetting >= UndercastModClass.getInstance().brightLevel) {
            UndercastModClass.brightActive = false;
            mc.gameSettings.gammaSetting = UndercastModClass.getInstance().defaultLevel;
        }
        UndercastData.welcomeMessageExpected = false;
    }

    @Override
    public void clientLoggedIn(NetHandler clientHandler, INetworkManager manager, Packet1Login login) {
    }
}
