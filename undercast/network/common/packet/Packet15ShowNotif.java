package undercast.network.common.packet;

import undercast.network.client.NetClientManager;
import undercast.network.common.Buffer;

public class Packet15ShowNotif extends ServerPacket {

    public String line1, line2;

    public Packet15ShowNotif() {
    }

    public Packet15ShowNotif(String l1, String l2) {
        this.line1 = l1;
        this.line2 = l2;
    }

    @Override
    public void handlePacket(NetClientManager networkManager) {
        networkManager.handleShowNotif(this);
    }

    @Override
    public void readPacketData(Buffer bufferIn) {
        this.line1 = bufferIn.getString();
        this.line2 = bufferIn.getString();
    }

    @Override
    public void writePacketData(Buffer bufferOut) {
        bufferOut.putString(this.line1);
        bufferOut.putString(this.line2);
    }

    @Override
    public int getPacketSize() {
        return this.getSize(this.line1) + this.getSize(this.line2);
    }

    @Override
    public byte getId() {
        return 15;
    }
}