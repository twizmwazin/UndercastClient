package undercast.network.client;

import net.minecraft.client.Minecraft;
import undercast.client.UndercastModClass;
import undercast.client.achievements.UndercastAchievement;
import undercast.network.common.Buffer;
import undercast.network.common.packet.Packet;
import undercast.network.common.packet.ServerPacket;
import jexxus.common.Connection;
import jexxus.common.ConnectionListener;
import jexxus.server.ServerConnection;

public class UndercastClientConnectionListener implements ConnectionListener {

    NetClientManager networkManager;

    @Override
    public void connectionBroken(Connection broken, boolean forced) {
    }

    @Override
    public void receive(byte[] data, Connection from) {
        try {
            if (networkManager == null) {
                networkManager = new NetClientManager(from);
            }
            Packet p = Packet.getPacketFromId(data[0]);
            if (p != null && p instanceof ServerPacket) {
                p.readPacketData(new Buffer(data));
                ((ServerPacket) p).handlePacket(networkManager);
            } else {
                // Mean that server sent something unknown, shouldn't happen
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void clientConnected(ServerConnection conn) {

    }

}
