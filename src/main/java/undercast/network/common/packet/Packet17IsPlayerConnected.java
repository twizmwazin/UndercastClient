package undercast.network.common.packet;

import undercast.network.client.NetClientManager;
import undercast.network.common.Buffer;

public class Packet17IsPlayerConnected extends ServerPacket {

    @Override
    public void handlePacket(NetClientManager networkManager) {
        networkManager.handleIsPlayerConnected(this);
    }

    @Override
    public void readPacketData(Buffer bufferIn) {
    }

    @Override
    public void writePacketData(Buffer bufferOut) {
    }

    @Override
    public int getPacketSize() {
        return 0;
    }

    @Override
    public byte getId() {
        return 17;
    }

}