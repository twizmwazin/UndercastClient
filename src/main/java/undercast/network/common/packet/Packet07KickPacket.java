package undercast.network.common.packet;

import undercast.network.client.NetClientManager;
import undercast.network.common.Buffer;

public class Packet07KickPacket extends ServerPacket {
    public String kickMessageL1;
    public String kickMessageL2;

    public Packet07KickPacket() {
    }

    public Packet07KickPacket(String message, String message2) {
        kickMessageL1 = message;
        kickMessageL2 = message2;
    }

    @Override
    public void handlePacket(NetClientManager networkManager) {
        networkManager.handleKick(this);
    }

    @Override
    public void readPacketData(Buffer bufferIn) {
        kickMessageL1 = bufferIn.getString();
        kickMessageL2 = bufferIn.getString();
    }

    @Override
    public void writePacketData(Buffer bufferOut) {
        bufferOut.putString(kickMessageL1);
        bufferOut.putString(kickMessageL2);
    }

    @Override
    public int getPacketSize() {
        return getSize(kickMessageL1) + getSize(kickMessageL2);
    }

    @Override
    public byte getId() {
        return 7;
    }
}
