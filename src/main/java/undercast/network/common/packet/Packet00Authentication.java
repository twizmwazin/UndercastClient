package undercast.network.common.packet;

import undercast.network.common.Buffer;

public class Packet00Authentication extends ClientPacket {

    public String username;
    public String version;
    
    public Packet00Authentication(){}
    
    public Packet00Authentication(String name, String version){
        this.username = name;
        this.version = version;
    }
    @Override
    public void readPacketData(Buffer bufferIn) {
        username = bufferIn.getString();
        version = bufferIn.getString();
    }

    @Override
    public void writePacketData(Buffer bufferOut) {
        bufferOut.putString(username);
        bufferOut.putString(version);
    }

    @Override
    public int getPacketSize() {
        return getSize(username) + getSize(version);
    }

    @Override
    public byte getId() {
        return 0;
    }

}
