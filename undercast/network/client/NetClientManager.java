package undercast.network.client;

import net.minecraft.client.Minecraft;
import jexxus.common.Connection;
import undercast.client.UndercastData;
import undercast.client.UndercastModClass;
import undercast.client.achievements.UndercastAchievement;
import undercast.network.common.NetManager;
import undercast.network.common.packet.Packet01IsValidAuthentication;
import undercast.network.common.packet.Packet03OnlinePlayersAnswer;
import undercast.network.common.packet.Packet07KickPacket;
import undercast.network.common.packet.Packet10GetServers;
import undercast.network.common.packet.Packet11SendServers;

public class NetClientManager extends NetManager {
    
    private Connection connection;
    private String username;
    public NetClientManager(Connection conn) {
        connection = conn;
        username = Minecraft.getMinecraft().getSession().getUsername();
    }

    public void handleAuthentification(Packet01IsValidAuthentication packet) {
        if(packet.isValid){
            NetManager.sendPacket(new Packet10GetServers());
            UndercastAchievement a = new UndercastAchievement(username, "\u00A7aConnection", "\u00A7aestablished");
            UndercastModClass.getInstance().guiAchievement.queueTakenAchievement(a);
        } else{
            UndercastAchievement a = new UndercastAchievement(username, "\u00A74" + username + " is already", "\u00A74connected");
            UndercastModClass.getInstance().guiAchievement.queueTakenAchievement(a);
        }
    }

    public void handleKick(Packet07KickPacket packet) {
        UndercastAchievement a = new UndercastAchievement(username, "\u00A74" + packet.kickMessageL1, "\u00A74" + packet.kickMessageL2);
        UndercastModClass.getInstance().guiAchievement.queueTakenAchievement(a);
        connection.close();
    }

    public void handleServersListAnswer(Packet11SendServers packet) {
        String[] servers = packet.servers.substring(1).split("@");
        String[][] mapData = new String[servers.length][6];
        for(int i = 0; i < servers.length; i++){
            String[] server = servers[i].split(";");
            for(int j = 0; j < 6; j++){
                mapData[i][j] = server[j];
            }
        }
        UndercastData.mapData = mapData;
        UndercastData.updateMap();
    }

    public void handleOnlinePlayersAnswer(Packet03OnlinePlayersAnswer packet) {
        
    }
}
