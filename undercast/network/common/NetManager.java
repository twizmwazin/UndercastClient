package undercast.network.common;

import undercast.client.UndercastModClass;
import undercast.network.common.packet.Packet;
import jexxus.common.Connection;
import jexxus.common.Delivery;

public abstract class NetManager {

    public static void sendPacket(Packet packet) {
        if (UndercastModClass.getInstance().connection.isConnected()) {
            Buffer buff = new Buffer(1 + packet.getPacketSize());
            buff.put(packet.getId());
            packet.writePacketData(buff);
            UndercastModClass.getInstance().connection.send(buff.array(), Delivery.RELIABLE);
        } else{
            System.out.println("Cannot send packet while not connected");
        }
    }
}
