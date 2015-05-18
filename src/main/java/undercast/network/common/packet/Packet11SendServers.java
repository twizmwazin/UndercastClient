package undercast.network.common.packet;

import undercast.network.client.NetClientManager;
import undercast.network.common.Buffer;

public class Packet11SendServers extends ServerPacket {

    public String servers;

    public Packet11SendServers() {
    }

    @Override
    public void handlePacket(NetClientManager networkManager) {
        networkManager.handleServersListAnswer(this);
    }

    @Override
    public void readPacketData(Buffer bufferIn) {
        servers = bufferIn.getString();
    }

    @Override
    public void writePacketData(Buffer bufferOut) {
        bufferOut.putString(servers);
    }

    @Override
    public int getPacketSize() {
        return getSize(servers);
    }

    @Override
    public byte getId() {
        return 11;
    }

}
