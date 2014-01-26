package undercast.network.common.packet;

import undercast.network.client.NetClientManager;
import undercast.network.common.Buffer;

public class Packet01IsValidAuthentication extends ServerPacket {

    public boolean isValid;

    public Packet01IsValidAuthentication() {
    }

    public Packet01IsValidAuthentication(boolean bool) {
        isValid = bool;
    }

    @Override
    public void readPacketData(Buffer bufferIn) {
        isValid = bufferIn.getBool();
    }

    @Override
    public void writePacketData(Buffer bufferOut) {
        bufferOut.putBool(isValid);
    }

    @Override
    public int getPacketSize() {
        return 1;
    }

    @Override
    public void handlePacket(NetClientManager networkManager) {
        networkManager.handleAuthentification(this);
    }

    @Override
    public byte getId() {
        return 1;
    }

}
