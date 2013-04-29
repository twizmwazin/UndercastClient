package tc.oc;

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

/**
 * @author Flv92
 */
public class AresConnectionHandler implements IConnectionHandler {

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
     */
    @Override
    public void connectionOpened(NetHandler netClientHandler, String server, int port, INetworkManager manager) {
        AresData.setTeam(AresData.Teams.Observers);
        System.out.println("Client successfully connected to " + ((NetClientHandler) netClientHandler).getNetManager().getSocketAddress().toString());

        //if logging onto a project ares server, then enable the main mod
        if (((NetClientHandler) netClientHandler).getNetManager().getSocketAddress().toString().contains("oc.tc"))
        {
            // What happens if logs into project ares
            AresData.isPA = true;
            AresData.guiShowing = true;
            System.out.println("Connected to: " + ((NetClientHandler) netClientHandler).getNetManager().getSocketAddress().toString() + " Ares mod activated!");
            AresData.setTeam(AresData.Teams.Observers);
            AresData.isPA = true;
            AresData.setServer(AresCustomMethods.getServer(((NetClientHandler) netClientHandler).getNetManager().getSocketAddress().toString()));
        } else
        {
            AresData.isPA = false;
        }
        //update notifier
        if (!AresData.isUpdate())
        {
            Thread thread = new Thread() {
                Minecraft mc;

                public void run() {
                    try
                    {
                        Thread.sleep(1000);
                        mc = FMLClientHandler.instance().getClient();
                    } catch (InterruptedException e)
                    {
                    }
                    mc.thePlayer.addChatMessage("\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-");
                    mc.thePlayer.addChatMessage("[ProjectAres]: A New Version of the Project Ares Mod is avaliable");
                    mc.thePlayer.addChatMessage("[ProjectAres]: Link: \u00A74" + AresData.updateLink);
                    mc.thePlayer.addChatMessage("\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-");
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
        AresData.isPA = false;
        AresData.guiShowing = false;
        AresData.setTeam(AresData.Teams.Observers);
        AresData.resetKills();
        AresData.resetKilled();
        AresData.resetDeaths();
        AresData.resetKillstreak();
        AresData.resetLargestKillstreak();
        AresData.setMap("Attempting to fetch map...");
        if (mc.gameSettings.gammaSetting >= AresModClass.brightLevel)
        {
            AresModClass.brightActive = false;
            mc.gameSettings.gammaSetting = AresModClass.defaultLevel;
        }
    }

    @Override
    public void clientLoggedIn(NetHandler clientHandler, INetworkManager manager, Packet1Login login) {
        AresData.update();
    }
}
