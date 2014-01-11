package undercast.network.common.packet;

import undercast.network.common.Buffer;

public class Packet00Authentication extends ClientPacket {

    public String username;
    
    public Packet00Authentication(){}
    
    public Packet00Authentication(String name){
        username = name;
    }
    @Override
    public void readPacketData(Buffer bufferIn) {
        username = bufferIn.getString();
    }

    @Override
    public void writePacketData(Buffer bufferOut) {
        bufferOut.putString(username);
    }

    @Override
    public int getPacketSize() {
        return username.length() + 1;
    }

    @Override
    public byte getId() {
        return 0;
    }

}
