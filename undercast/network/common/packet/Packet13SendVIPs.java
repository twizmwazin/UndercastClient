package undercast.network.common.packet;
import java.util.ArrayList;
import java.util.List;

import undercast.network.client.NetClientManager;
import undercast.network.common.Buffer;

public class Packet13SendVIPs extends ServerPacket {

    public String users_str;

    public Packet13SendVIPs() {
    }

    @Override
    public void handlePacket(NetClientManager networkManager) {
        networkManager.handleSendVIPs(this);
    }

    @Override
    public void readPacketData(Buffer bufferIn) {
        users_str = bufferIn.getString();
    }

    @Override
    public void writePacketData(Buffer bufferOut) {
    }

    @Override
    public int getPacketSize() {
        return getSize(users_str);
    }

    @Override
    public byte getId() {
        return 13;
    }
}
