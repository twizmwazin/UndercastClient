package undercast.network.common.packet;

import undercast.network.client.NetClientManager;

public abstract class ServerPacket extends Packet {
    public abstract void handlePacket(NetClientManager networkManager);

}
