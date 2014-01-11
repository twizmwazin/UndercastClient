package undercast.network.common.packet;

import undercast.network.common.Buffer;

public class Packet02GetOnlinePlayers extends ClientPacket {

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
        return 2;
    }

}
